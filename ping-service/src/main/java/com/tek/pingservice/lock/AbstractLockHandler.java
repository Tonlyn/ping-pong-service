package com.tek.pingservice.lock;

import com.tek.pingservice.constant.ConfigValue;
import com.tek.pingservice.constant.Constants;
import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.vo.FileLockVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public abstract class AbstractLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLockHandler.class);


    public synchronized Mono<PingRespDto> handle() throws Exception {

        //get file lock
        FileLockVo fileLockVo = getFileLock();
        RandomAccessFile randomAccessFile = fileLockVo.getRandomAccessFile();
        FileChannel channel = fileLockVo.getChannel();
        FileLock lock = fileLockVo.getLock();

        if (lock == null) {
            String msg = lockFail();
            return Mono.just(new PingRespDto(Constants.DTO_RES_FAIL, msg));
        }
        try {
            //get lock success
            return doBusiness();
        } finally {
            close(lock, channel, randomAccessFile);
        }
    }


    private synchronized FileLockVo getFileLock() throws IOException {
        RandomAccessFile randomAccessFile = null;
        FileChannel channel = null;
        FileLock lock = null;

        //ConfigValue configValue = (ConfigValue) ApplicationContextUtil.getBean(ConfigValue.class);
        ConfigValue configValue = getConfigValue();

        //文件锁路径
        String fileLockPath = configValue.FILE_LOCK_DIR + File.separator + configValue.FILE_LOCK_FILE_NAME;

        for (int i = 0; i < configValue.FILE_LOCK_NUM; i++) {
            // full name of lock file, create file if not exists
            String fullFileLockPath = fileLockPath + i;
            fileExists(configValue, fullFileLockPath);

            randomAccessFile = new RandomAccessFile(fullFileLockPath, "rw");
            channel = randomAccessFile.getChannel();
            lock = channel.tryLock();

            if (lock == null) {
                close(lock, channel, randomAccessFile);
                continue;
            }
            return new FileLockVo(randomAccessFile, channel, lock);
        }
        return new FileLockVo(randomAccessFile, channel, lock);
    }


    private synchronized void fileExists(ConfigValue configValue, String fullPath) throws IOException {
        File dirFile = new File(configValue.FILE_LOCK_DIR);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File lockFile = new File(fullPath);
        if (!lockFile.exists()) {
            lockFile.createNewFile();
        }
    }

    private synchronized void close(FileLock lock, FileChannel channel, RandomAccessFile randomAccessFile) throws IOException {
        if (lock != null) {
            lock.release();
        }
        channel.close();
        randomAccessFile.close();
    }

    public abstract Mono<PingRespDto> doBusiness();

    public abstract String lockFail();

    public abstract ConfigValue getConfigValue();
}
