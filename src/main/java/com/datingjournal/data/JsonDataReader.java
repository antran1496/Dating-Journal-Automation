package com.datingjournal.data;

import com.datingjournal.config.AppConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Đọc test data JSON từ {@code src/test/resources/testdata/}.
 * <p>
 * Hỗ trợ placeholder {@code ${KEY}} trong giá trị chuỗi: được thay bằng cấu hình tương ứng
 * (ví dụ {@code ${ADMIN_EMAIL}} → {@link AppConfig#resolve(String)} với key {@code admin.email}).
 * Nhờ đó dữ liệu nhạy cảm (tài khoản, mật khẩu) KHÔNG bao giờ nằm trong file JSON.
 */
public final class JsonDataReader {

    private static final String TEST_DATA_DIR = "testdata/";
    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([A-Z0-9_]+)}");
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    private JsonDataReader() {
    }

    /**
     * Đọc một mảng dữ liệu trong file JSON.
     *
     * @param fileName tên file trong thư mục testdata, ví dụ {@code "login.json"}
     * @param section  key của mảng ở cấp gốc, ví dụ {@code "invalidLogin"}
     * @param type     kiểu record/POJO đích
     */
    public static <T> List<T> readList(String fileName, String section, Class<T> type) {
        JsonNode array = readRoot(fileName).path(section);
        if (!array.isArray()) {
            throw new IllegalArgumentException("Không tìm thấy mảng '" + section + "' trong " + fileName);
        }
        List<T> result = new ArrayList<>();
        for (JsonNode item : array) {
            try {
                result.add(MAPPER.treeToValue(resolvePlaceholders(item), type));
            } catch (IOException e) {
                throw new IllegalStateException("Sai cấu trúc dữ liệu trong " + fileName + " > " + section + ": " + item, e);
            }
        }
        return result;
    }

    /** Chuyển List thành Object[][] cho TestNG {@code @DataProvider}. */
    public static <T> Object[][] toDataProvider(List<T> items) {
        return items.stream().map(item -> new Object[]{item}).toArray(Object[][]::new);
    }

    // ---------------------------------------------------------------- nội bộ

    private static JsonNode readRoot(String fileName) {
        String path = TEST_DATA_DIR + fileName;
        try (InputStream in = JsonDataReader.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Không tìm thấy file test data: " + path);
            }
            return MAPPER.readTree(in);
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được file test data: " + path, e);
        }
    }

    private static JsonNode resolvePlaceholders(JsonNode node) {
        if (node.isTextual()) {
            return TextNode.valueOf(replace(node.asText()));
        }
        if (node.isObject()) {
            ObjectNode copy = ((ObjectNode) node).deepCopy();
            Iterator<Map.Entry<String, JsonNode>> fields = copy.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                field.setValue(resolvePlaceholders(field.getValue()));
            }
            return copy;
        }
        return node;
    }

    private static String replace(String value) {
        Matcher matcher = PLACEHOLDER.matcher(value);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String configKey = matcher.group(1).toLowerCase().replace('_', '.');
            matcher.appendReplacement(sb, Matcher.quoteReplacement(AppConfig.resolve(configKey)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
