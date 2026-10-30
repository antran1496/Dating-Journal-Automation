#!/usr/bin/env bash
# =====================================================================
# Kiểm tra tuân thủ cấu trúc & luật của framework (xem AGENTS.md).
# Cách dùng:  bash scripts/verify-structure.sh
# Thoát với mã 1 nếu có vi phạm. Agent BẮT BUỘC chạy trước khi báo hoàn thành / commit.
# =====================================================================
set -u

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

MAIN="src/main/java/com/datingjournal"
TEST="src/test/java/com/datingjournal"
RES="src/test/resources"
ERRORS=0

fail() { echo "  ✘ $1"; ERRORS=$((ERRORS + 1)); }
pass() { echo "  ✔ $1"; }

# grep_check <mô tả> <pattern> <đường dẫn...>: vi phạm nếu tìm thấy pattern
grep_check() {
    local desc="$1" pattern="$2"; shift 2
    local hits
    hits=$(grep -rnE --include='*.java' "$pattern" "$@" 2>/dev/null)
    if [ -n "$hits" ]; then
        fail "$desc"
        echo "$hits" | sed 's/^/      /'
    else
        pass "$desc"
    fi
}

echo "▶ 1. Cấu trúc thư mục"
for dir in "$MAIN/config" "$MAIN/core" "$MAIN/pages" "$MAIN/components" "$MAIN/data/models" "$MAIN/utils" \
           "$TEST/base" "$TEST/listeners" "$TEST/dataproviders" "$TEST/tests" \
           "$RES/config" "$RES/testdata" "$RES/suites" ".agents/rules" ".agents/skills"; do
    [ -d "$dir" ] || fail "Thiếu thư mục bắt buộc: $dir"
done
for f in "AGENTS.md" ".env.example" ".gitignore" "$MAIN/pages/BasePage.java" "$TEST/base/BaseTest.java"; do
    [ -f "$f" ] || fail "Thiếu file bắt buộc: $f"
done

ALLOWED_MAIN="config core pages components data utils"
for d in "$MAIN"/*/; do
    name=$(basename "$d")
    case " $ALLOWED_MAIN " in *" $name "*) ;; *) fail "Package lạ trong main: $name (chỉ cho phép: $ALLOWED_MAIN)";; esac
done
ALLOWED_TEST="base listeners dataproviders tests"
for d in "$TEST"/*/; do
    name=$(basename "$d")
    case " $ALLOWED_TEST " in *" $name "*) ;; *) fail "Package lạ trong test: $name (chỉ cho phép: $ALLOWED_TEST)";; esac
done
[ $ERRORS -eq 0 ] && pass "Đúng cấu trúc đã khóa"

echo "▶ 2. Quy tắc đặt tên"
before=$ERRORS
for f in "$MAIN"/pages/*.java; do
    case "$(basename "$f")" in *Page.java) ;; *) fail "Page phải kết thúc bằng 'Page.java': $f";; esac
done
while IFS= read -r f; do
    case "$(basename "$f")" in *Test.java) ;; *) fail "Class test phải kết thúc bằng 'Test.java': $f";; esac
done < <(find "$TEST/tests" -name '*.java')
for f in "$TEST"/dataproviders/*.java; do
    [ -e "$f" ] || continue
    case "$(basename "$f")" in *DataProvider.java) ;; *) fail "DataProvider phải kết thúc bằng 'DataProvider.java': $f";; esac
done
for f in "$MAIN"/data/models/*.java; do
    [ -e "$f" ] || continue
    case "$(basename "$f")" in *Data.java) ;; *) fail "Model phải kết thúc bằng 'Data.java': $f";; esac
done
for f in "$RES"/testdata/*; do
    [ -e "$f" ] || continue
    basename "$f" | grep -qE '^[a-z0-9]+(-[a-z0-9]+)*\.json$' || fail "Test data phải là kebab-case .json: $f"
done
[ $ERRORS -eq $before ] && pass "Tên file hợp lệ"

echo "▶ 3. Danh sách CẤM trong code"
grep_check "Không dùng Thread.sleep" 'Thread\.sleep\(' src
grep_check "Không dùng System.out / printStackTrace" 'System\.(out|err)\.print|printStackTrace\(' src
grep_check "Test không được dùng Selenium trực tiếp (findElement/By/WebDriver)" 'import org\.openqa\.selenium|findElement|By\.(id|xpath|cssSelector|name|className|tagName|linkText)' "$TEST"
grep_check "Page/Component không được assert" 'org\.testng\.Assert|SoftAssert|assertThat|assertEquals|assertTrue' "$MAIN/pages" "$MAIN/components"
grep_check "Không hardcode URL trong Java" '"https?://' src
grep_check "Không dùng XPath tuyệt đối" '"/html' src
grep_check "Implicit wait phải bằng 0" 'implicitlyWait\((Duration\.of|[0-9])' src
grep_check "Không dùng dependsOnMethods giữa các test" 'dependsOnMethods' "$TEST"

echo "▶ 4. Bảo mật dữ liệu"
before=$ERRORS
if [ -f .env ]; then
    pwd_value=$(grep -E '^ADMIN_PASSWORD=' .env | cut -d= -f2- | tr -d '\r')
    if [ -n "$pwd_value" ] && grep -rnF -- "$pwd_value" src >/dev/null 2>&1; then
        fail "Mật khẩu thật trong .env bị hardcode trong src/ — dùng placeholder \${ADMIN_PASSWORD}"
    fi
fi
grep -qE '^\.env$' .gitignore || fail ".gitignore phải chứa .env"
if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
    if git ls-files --error-unmatch .env >/dev/null 2>&1 || git diff --cached --name-only | grep -qE '^\.env$'; then
        fail ".env đang bị git theo dõi/stage — chạy: git rm --cached .env"
    fi
fi
[ $ERRORS -eq $before ] && pass "Không lộ thông tin nhạy cảm"

echo "▶ 5. Thư viện được phép (pom.xml)"
before=$ERRORS
ALLOWED_DEPS="selenium-java testng jackson-databind log4j-api log4j-core allure-testng aspectjweaver"
for a in $(sed -n '/<dependencies>/,/<\/dependencies>/p' pom.xml | grep -oE '<artifactId>[^<]+' | sed 's/<artifactId>//'); do
    case " $ALLOWED_DEPS " in *" $a "*) ;; *) fail "Dependency chưa được duyệt: $a (cần người dùng đồng ý + cập nhật AGENTS.md)";; esac
done
[ $ERRORS -eq $before ] && pass "Chỉ dùng thư viện đã duyệt"

echo "▶ 6. Nhánh làm việc Git (không dev trực tiếp trên main)"
before=$ERRORS
if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
    current_branch=$(git branch --show-current 2>/dev/null)
    if [ "$current_branch" = "main" ] && [ -n "$(git status --porcelain 2>/dev/null)" ]; then
        fail "Đang có thay đổi trực tiếp trên nhánh 'main' — CẤM dev ở nhánh main, hãy chuyển sang branch riêng (git checkout -b <loại>/<mô-tả>)"
    else
        pass "Nhánh hợp lệ (branch: ${current_branch:-unknown})"
    fi
fi

echo
if [ $ERRORS -gt 0 ]; then
    echo "❌ KHÔNG ĐẠT: $ERRORS vi phạm. Sửa theo AGENTS.md rồi chạy lại."
    exit 1
fi
echo "✅ ĐẠT: framework tuân thủ đúng cấu trúc và luật."
