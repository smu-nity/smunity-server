package com.smunity.server.domain.auth.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String LOGIN_URL = "https://smsso.smu.ac.kr/Login.do";
    private static final String BASE_URL = "https://smul.smu.ac.kr";

    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        JSONObject response = getData(requestDto, "/UsrSchMng/selectStdInfo.do");
        return AuthResponseDto.from(response.getJSONArray("dsStdInfoList"));
    }

    public List<AuthCourseResponseDto> getCourses(AuthRequestDto requestDto) {
        JSONObject response = getData(requestDto, "/UsrRecMatt/list.do");
        return AuthCourseResponseDto.from(response.getJSONArray("dsRecMattList"));
    }

    private Map<String, String> login(AuthRequestDto requestDto) {
        try {
            Connection.Response loginResponse = executeLogin(requestDto);
            return getSessionCookies(loginResponse);
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.AUTH_LOGIN_FAIL);
        }
    }

    private Connection.Response executeLogin(AuthRequestDto requestDto) throws IOException {
        Connection.Response response = Jsoup.connect(LOGIN_URL)
                .data("user_id", requestDto.username())
                .data("user_password", requestDto.password())
                .method(Connection.Method.POST)
                .execute();
        if (response.url().toString().equals(LOGIN_URL))
            throw new GeneralException(ErrorCode.AUTH_UNAUTHORIZED);
        return response;
    }

    private Map<String, String> getSessionCookies(Connection.Response loginResponse) throws IOException {
        return Jsoup.connect(BASE_URL + "/index.do")
                .method(Connection.Method.GET)
                .cookies(loginResponse.cookies())
                .execute()
                .cookies();
    }

    private JSONObject getData(AuthRequestDto requestDto, String url) {
        Map<String, String> session = login(requestDto);
        try {
            HttpURLConnection connection = createConnection(BASE_URL + url, session);
            connection.getOutputStream().write(createRequestData(requestDto));
            return readResponse(connection);
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.AUTH_INTERNAL_SERVER_ERROR);
        }
    }

    private HttpURLConnection createConnection(String url, Map<String, String> session) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        session.forEach((key, value) -> connection.addRequestProperty("Cookie", key + "=" + value));
        connection.setDoOutput(true);
        return connection;
    }

    private byte[] createRequestData(AuthRequestDto requestDto) {
        return "@d#=@d1#&@d1#tp=dm&_AUTH_MENU_KEY=usrCPsnlInfoUpd-STD&@d1#strStdNo=".concat(requestDto.username()).getBytes();
    }

    private JSONObject readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);
        }
        return new JSONObject(response.toString());
    }
}