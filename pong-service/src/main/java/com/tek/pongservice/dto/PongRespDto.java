package com.tek.pongservice.dto;

import java.io.Serializable;

/**
 * Pong Response Dto
 *
 * @author linshy
 * @date 2024/10/30
 */
public class PongRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    public PongRespDto() {}

    public PongRespDto(String code) {
        this.code = code;
    }

    public PongRespDto(String code, String message) {
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
