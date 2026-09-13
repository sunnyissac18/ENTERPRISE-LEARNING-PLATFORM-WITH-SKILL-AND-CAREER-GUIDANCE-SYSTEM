package com.skillsphere.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/api/debug/auth")
    public Map<String, Object> debugAuth(Authentication auth) {
        Map<String, Object> map = new HashMap<>();
        if (auth == null) {
            map.put("error", "No authentication");
            return map;
        }
        map.put("name", auth.getName());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        map.put("authorities", authorities.stream().map(GrantedAuthority::getAuthority).toList());
        return map;
    }
}
