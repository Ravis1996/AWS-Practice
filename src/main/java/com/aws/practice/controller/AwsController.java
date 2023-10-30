package com.aws.practice.controller;

import com.aws.practice.service.AwsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/aws")
public class AwsController {

    @Autowired
    private AwsService awsService;

    @PostMapping
    public ResponseEntity<Void> createService(List<String> services) {
        awsService.createService(services);
        return ResponseEntity.ok().build();
    }
}
