package com.dentalmoovi.website.services.cache;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;

@Service
public class CacheSer {
    private Cache<String, String> registrationCodeConfig;
    private Cache<String, String> replayCodeRestrict;

    public CacheSer(Cache<String, String> registrationCodeConfig, Cache<String, String> replayCodeRestrict) {
        this.registrationCodeConfig = registrationCodeConfig;
        this.replayCodeRestrict = replayCodeRestrict;
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

    public void addToOrUpdateReplayCodeRestrict(String key, String value) {
        replayCodeRestrict.put(key, value);
    }

    public String getFromReplayCodeRestrict(String key) {
        return replayCodeRestrict.getIfPresent(key);
    }
}
