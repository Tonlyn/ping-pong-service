package com.tek.pongservice.constant;

public class Constants {

    /**
     * 最大令牌数，即同一时刻限流数
     */
    public static final int RATE_LIMIT_MAX_TOKEN = 1;
    /**
     * 定时器延时时长，单位：秒
     */
    public static final long SCHEDULER_INITIAL_DELAY = 0;
    /**
     * 定时器时间间隔，单位秒
     */
    public static final long SCHEDULER_PERIOD = 1;

    /**
     * Pong response content
     */
    public static final String PONG_RESP_CONTENT = "World";

}
