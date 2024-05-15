/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RealTimeController {

//    @GetMapping("/stream")
//    public Flux<ServerSentEvent<String>> streamData() {
//        // 模拟实时数据生成，每秒发送一条数据
//        return Flux.interval(Duration.ofSeconds(1))
//                .map(sequence -> ServerSentEvent.<String>builder()
//                        .id(String.valueOf(sequence))
//                        .event("data")
//                        .data("This is a message at sequence " + sequence)
//                        .build());
//    }

    @GetMapping("/helloworld")
    public String getHelloWorld() {
        return "Hello World";
    }


}
