package com.dentalmoovi.website.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dentalmoovi.website.services.cache.CacheSer;

@SpringBootTest
class CacherSerTest {

    @Autowired
    private CacheSer cacheSer;

    @Test
    void verifyCacheWorks(){
        //add cache
        cacheSer.addToOrUpdateRegistrationCache("dataTest", "Example");
        //get cache
        String expected1 = cacheSer.getFromRegistrationCache("dataTest");
        //compare cache added vs cache getted
        assertEquals("Example", expected1);
        //update cache
        cacheSer.addToOrUpdateRegistrationCache("dataTest", "Example2");
        //get again
        String expected2 = cacheSer.getFromRegistrationCache("dataTest");
        //compare cache updated vs cache getted
        assertEquals("Example2", expected2);
        //remove cache
        cacheSer.removeFromRegistrationCache("dataTest");
        //get cache
        String expected3 = cacheSer.getFromRegistrationCache("dataTest");
        //make sure cache was deleted
        assertNull(expected3);
    }
}
