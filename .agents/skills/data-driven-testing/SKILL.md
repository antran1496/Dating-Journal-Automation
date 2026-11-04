---
name: data-driven-testing
description: Dùng khi thêm hoặc sửa test data (file JSON trong src/test/resources/testdata), tạo model record trong data/models, hoặc viết TestNG DataProvider. Mô tả cấu trúc JSON, placeholder ${KEY} cho dữ liệu nhạy cảm và cách nối với test.
---

# Skill: Data-Driven Testing

Mẫu chuẩn: [login.json](../../../src/test/resources/testdata/login.json) → [LoginData.java](../../../src/main/java/com/datingjournal/data/models/LoginData.java) → [LoginDataProvider.java](../../../src/test/java/com/datingjournal/dataproviders/LoginDataProvider.java) → [LoginTest.java](../../../src/test/java/com/datingjournal/tests/auth/LoginTest.java)

## 1. File JSON — `src/test/resources/testdata/<tính-năng>.json`

```json
{
  "_comment": "Mô tả file bằng tiếng Việt",
  "<tenSection>": [
    {
      "caseId": "TC_<MODULE>_<NN>_<MM>",
      "description": "Mô tả tiếng Việt, hiện trên báo cáo",
      "email": "${TEST_EMAIL}",
      "expectedXxx": "Thông báo copy nguyên văn từ UI"
    }
  ]
}
```

- Mỗi **section** = 1 nhóm kịch bản cùng kiểu (ví dụ `validLogin`, `invalidLogin`) ↔ 1 `@DataProvider`.
- **Bắt buộc** có `caseId` và `description`.
- Dữ liệu nhạy cảm/thay đổi theo môi trường: dùng placeholder `${KEY}` → tự thay bằng cấu hình `key` (ví dụ `${TEST_EMAIL}` → `test.email`). **CẤM ghi mật khẩu thật vào JSON.**
- Trường không áp dụng: bỏ qua (record sẽ nhận `""`).
- Thêm kịch bản mới = thêm phần tử JSON, **không sửa Java**.

## 2. Model — `src/main/java/com/datingjournal/data/models/<Tên>Data.java`

- Dùng Java `record`, tên field trùng key JSON (camelCase).
- Compact constructor chuyển `null` → `""` cho các field chuỗi tùy chọn.
- Override `toString()` = `caseId + " - " + description` (không in mật khẩu).
- `JsonDataReader` bật `FAIL_ON_UNKNOWN_PROPERTIES` → sai tên key sẽ báo lỗi ngay.

## 3. DataProvider — `src/test/java/com/datingjournal/dataproviders/<TínhNăng>DataProvider.java`

```java
public final class XxxDataProvider {
    private static final String FILE = "xxx.json";
    public static final String VALID_XXX = "validXxx";   // tên provider = tên section

    private XxxDataProvider() { }

    @DataProvider(name = VALID_XXX, parallel = true)
    public static Object[][] validXxx() {
        return JsonDataReader.toDataProvider(JsonDataReader.readList(FILE, VALID_XXX, XxxData.class));
    }
}
```

## 4. Dùng trong test

```java
@Test(dataProvider = XxxDataProvider.VALID_XXX, dataProviderClass = XxxDataProvider.class, groups = {...})
public void xxxShouldYyy(XxxData data) { ... }
```

## 5. Kiểm tra
- `mvn -q -B clean test-compile` rồi chạy test: số lần chạy method phải bằng số phần tử JSON.
