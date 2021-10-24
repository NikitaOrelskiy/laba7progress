package com.itmo.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itmo.data.City;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    public static final Logger log =  (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FileManager.class);

    private ObjectMapper objectMapper;

    public FileManager() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }


    public Map<Long, City> getCollection(String filename) {
        try {
            if (!new File(filename).exists()){
                return new HashMap<>();
            }
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            return objectMapper.readValue(bis, new TypeReference<Map<Long, City>>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HashMap<>();
        }
    }

    public boolean saveCollection(Map<Long, City> map, String filename) {
        try {
            FileOutputStream fouts = new FileOutputStream(filename);
            objectMapper.writeValue(fouts, map);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
