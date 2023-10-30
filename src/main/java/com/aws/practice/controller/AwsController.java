package com.aws.practice.controller;

import com.aws.practice.service.AwsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/aws")
public class AwsController {

    @Autowired
    private AwsService awsService;

    @PostMapping
    public ResponseEntity<Void> createService(@RequestBody List<String> services) {
        awsService.createService(services);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<String>> getData(@PathVariable String type) {
        return ResponseEntity.ok(awsService.getData(type));
    }

    @GetMapping("/s3/{name}/files")
    public ResponseEntity<Void> getS3Data(@PathVariable String name) {
        awsService.getS3Data(name);
        return ResponseEntity.ok().build();
    }
}
