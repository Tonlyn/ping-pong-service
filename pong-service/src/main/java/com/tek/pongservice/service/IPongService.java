package com.tek.pongservice.service;

import org.springframework.http.ResponseEntity;

public interface IPongService {


    ResponseEntity<String> pong(String message);
}
