package com.tek.pingservice.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration from YAML File
 *
 * @author linshy
 * @date 2024/10/30
 */
@Component
public class ConfigValue {


    @Value("${pong-service.url:}")
    public String PONG_SERVICE_URL;


    @Value("${file.lock.dir:locks}")
    public String FILE_LOCK_DIR;

    @Value("${file.lock.fileName:fileLock}")
    public String FILE_LOCK_FILE_NAME;

    @Value("${file.lock.limit:2}")
    public Integer FILE_LOCK_NUM;
}
