package com.tek.pingservice.lock;

import com.tek.pingservice.constant.ConfigValue;
import com.tek.pingservice.constant.Constants;
import com.tek.pingservice.dto.PingRespDto;
import com.tek.pingservice.vo.FileLockVo;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Abstract class of FileLock
 *
 * @author linshy
 * @date 2024/10/30
 */
public abstract class AbstractLockHandler {


    /**
     * send request to Pong Service with Rate Limit Control by FileLock
     * @return
     * @throws IOException
     */
    public synchronized Mono<PingRespDto> handle() throws IOException {

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


    /**
     * get File Lock
     * @return
     * @throws IOException
     */
    private synchronized FileLockVo getFileLock() throws IOException {
        RandomAccessFile randomAccessFile = null;
        FileChannel channel = null;
        FileLock lock = null;

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


    /**
     * create Lock File if file not exists
     * @param configValue
     * @param fullPath
     * @throws IOException
     */
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

    /**
     * release FileLock
     * @param lock
     * @param channel
     * @param randomAccessFile
     * @throws IOException
     */
    private synchronized void close(FileLock lock, FileChannel channel, RandomAccessFile randomAccessFile) throws IOException {
        if (lock != null) {
            lock.release();
        }
        channel.close();
        randomAccessFile.close();
    }

    /**
     * doing something after get FileLock
     * @return response of Pong Service
     */
    public abstract Mono<PingRespDto> doBusiness();

    /**
     * doing something after fail to get FileLock
     * @return
     */
    public abstract String lockFail();

    /**
     * get Configuration for abstract class
     * @return
     */
    public abstract ConfigValue getConfigValue();
}
