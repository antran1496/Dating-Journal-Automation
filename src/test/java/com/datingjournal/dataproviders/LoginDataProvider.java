package com.datingjournal.dataproviders;

import com.datingjournal.data.JsonDataReader;
import com.datingjournal.data.models.LoginData;
import org.testng.annotations.DataProvider;

/**
 * DataProvider cho các test Đăng nhập. Dữ liệu nằm trong {@code testdata/login.json}.
 * Thêm case mới = thêm phần tử JSON, KHÔNG cần sửa code.
 */
public final class LoginDataProvider {

    private static final String FILE = "login.json";

    public static final String VALID_LOGIN = "validLogin";
    public static final String INVALID_LOGIN = "invalidLogin";

    private LoginDataProvider() {
    }

    @DataProvider(name = VALID_LOGIN)
    public static Object[][] validLogin() {
        return JsonDataReader.toDataProvider(JsonDataReader.readList(FILE, VALID_LOGIN, LoginData.class));
    }

    @DataProvider(name = INVALID_LOGIN, parallel = true)
    public static Object[][] invalidLogin() {
        return JsonDataReader.toDataProvider(JsonDataReader.readList(FILE, INVALID_LOGIN, LoginData.class));
    }
}
