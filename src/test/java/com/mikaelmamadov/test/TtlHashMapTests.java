package com.mikaelmamadov.test;


import com.mikaelmamadov.test.repository.TtlHashMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TtlHashMapTests {
    
    @Test
    public void basicGetTest(){
        TtlHashMap<Long, String> ttlHashMap = new TtlHashMap<>();
        ttlHashMap.put(1L, "One");
        assertEquals(ttlHashMap.get(1L), "One");
    }

    @Test
    public void basicExpireTest() throws InterruptedException {
        TtlHashMap<Long, String> ttlHashMap = new TtlHashMap<>();
        ttlHashMap.put(1L, "One", 1L);
        Thread.sleep(1000 * 61);
        assertNull(ttlHashMap.get(1L));
    }
}
