package com.mikaelmamadov.test.service;

import com.mikaelmamadov.test.repository.TtlHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class TtlHashMapService {


    TtlHashMap<Long, String> ttlHashMap;

    @Autowired
    public TtlHashMapService(TtlHashMap ttlHashMap){
        this.ttlHashMap = ttlHashMap;
    }

    public String getElementByKey (Long key){

        return ttlHashMap.get(key);
    }

    public String save(Long key, String value, Long timeout) {
        if(timeout != null)
            return ttlHashMap.put(key, value, timeout);
        return ttlHashMap.put(key, value);
    }

    public void removeByKey(Long key) {
        ttlHashMap.remove(key);
    }

    public void dump() {
        try(
            FileOutputStream fileOutputStream = new FileOutputStream("ttlhashmap.ser");
            ObjectOutputStream objectOutPutStream = new ObjectOutputStream(fileOutputStream)
        )
        {
            objectOutPutStream.writeObject(ttlHashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        TtlHashMap<Long, String> tempTtlHashMap;

        try(
            FileInputStream fileInputStream = new FileInputStream("ttlhashmap.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        )
        {
            tempTtlHashMap = (TtlHashMap) objectInputStream.readObject();



            for(Map.Entry<Long, String> entry : tempTtlHashMap.entrySet()){
                Long key = entry.getKey();
                String value = entry.getValue();
                if(!ttlHashMap.containsKey(key)) {
                    ttlHashMap.put(key, value);
                    ttlHashMap.getCreatedAt().put(key,tempTtlHashMap.getCreatedAt().get(key));
                    ttlHashMap.getTimeout().put(key, tempTtlHashMap.getTimeout().get(key));
                }
            }
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
