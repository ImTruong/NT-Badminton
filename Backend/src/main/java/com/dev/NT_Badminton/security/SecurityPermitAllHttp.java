package com.dev.NT_Badminton.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SecurityPermitAllHttp {
    private static final Map<String, Set<String>> PERMIT_ALL_ENDPOINTS = new HashMap<>();

    static {
//        PERMIT_ALL_ENDPOINTS.put("/**", Set.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        PERMIT_ALL_ENDPOINTS.put("/user/login", Set.of("POST"));
        PERMIT_ALL_ENDPOINTS.put("/user/register", Set.of("POST"));
        PERMIT_ALL_ENDPOINTS.put("/user/genders", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/user/cities-districts", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/order/finish-online-payment", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/category/all", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/category/root", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/product", Set.of("GET"));
        PERMIT_ALL_ENDPOINTS.put("/product/{id}", Set.of("GET"));
    }

    public static Map<String, Set<String>> getPermitAllEndpoints() {
        return PERMIT_ALL_ENDPOINTS;
    }
}
