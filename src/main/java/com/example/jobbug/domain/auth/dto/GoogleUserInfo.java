package com.example.jobbug.domain.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserInfo {

    private Map<String, Object> attributes;

    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    public String getProvider() {
        return "google";
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return attributes.get("email").toString();
    }
}
