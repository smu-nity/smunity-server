package com.smunity.server.domain.account.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpUtil {

    private static final List<String> IP_HEADER_CANDIDATES = List.of(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
    );

    public static String getClientIp(HttpServletRequest request) {
        return IP_HEADER_CANDIDATES.stream()
                .map(request::getHeader)
                .filter(IpUtil::isValidIp)
                .map(IpUtil::extractFirstIp)
                .findFirst()
                .orElseGet(request::getRemoteAddr);
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    private static String extractFirstIp(String ip) {
        return ip.split(",")[0].trim();
    }
}
