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
    public void createService(List<String> services) {

        services.forEach(s -> {
            if(s.equals("EC2")) {
                Ec2Service ec2Service = new Ec2Service();
                Thread t1 = new Thread(ec2Service);
                t1.start();

            } else if(s.equals("S3")) {
                S3Service s3Service = new S3Service();
                Thread t2 = new Thread(s3Service);
                t2.start();
            }
        });
    }

}
