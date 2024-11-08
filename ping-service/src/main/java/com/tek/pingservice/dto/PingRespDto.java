package com.tek.pingservice.dto;

import java.io.Serializable;

/**
 * Ping Response Dto
 *
 * @author linshy
 * @date 2024/10/30
 */
public class PingRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    public PingRespDto() {}

    public PingRespDto(String code) {
        this.code = code;
    }

    public PingRespDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
