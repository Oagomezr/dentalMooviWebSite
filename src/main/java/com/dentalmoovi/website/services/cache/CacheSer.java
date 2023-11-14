package com.dentalmoovi.website.services.cache;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;

@Service
public class CacheSer {
    private Cache<String, String> registrationCodeConfig;

    public CacheSer(Cache<String, String> registrationCodeConfig) {
        this.registrationCodeConfig = registrationCodeConfig;
    }

    public void addToOrUpdateRegistrationCache(String key, String value) {
        registrationCodeConfig.put(key, value);
    }

    public String getFromRegistrationCache(String key) {
        return registrationCodeConfig.getIfPresent(key);
    }

    public void removeFromRegistrationCache(String key) {
        registrationCodeConfig.invalidate(key);
    }
}
