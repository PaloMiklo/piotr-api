package com.api.piotr.controller;

import static java.lang.String.valueOf;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.palomiklo.streampulse.connection.Connection.createConnection;
import com.palomiklo.streampulse.connection.ConnectionHolder;
import static com.palomiklo.streampulse.connection.CustomConnectionConfig.streamPulseBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/stream-pulse")
@Slf4j
public class StreamController {

    @GetMapping(value = "/connect", produces = TEXT_EVENT_STREAM_VALUE)
    public void streamEvents(HttpServletRequest request, HttpServletResponse response) {
        ConnectionHolder streamPulse = createConnection(streamPulseBuilder().pingInterval((byte) 3).connectionTimeout((byte) (1 * 60)).ping(() -> valueOf(Math.random() * 100)).build(), request, response);
    }
}
