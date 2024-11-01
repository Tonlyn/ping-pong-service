package com.tek.pingservice.vo;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockVo {


    private RandomAccessFile randomAccessFile;
    private FileChannel channel;
    private FileLock lock;

    public FileLockVo(RandomAccessFile randomAccessFile, FileChannel channel, FileLock lock) {
        this.randomAccessFile = randomAccessFile;
        this.channel = channel;
        this.lock = lock;
    }

    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public FileChannel getChannel() {
        return channel;
    }

    public FileLock getLock() {
        return lock;
    }
}
