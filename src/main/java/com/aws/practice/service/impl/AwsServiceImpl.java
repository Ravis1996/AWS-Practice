package com.aws.practice.service.impl;

import com.aws.practice.repository.AwsRepository;
import com.aws.practice.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwsServiceImpl implements AwsService {

    @Autowired
    private AwsRepository awsRepository;

    @Override
    public List<String> createService(List<String> services) {
        return null;
    }
}
