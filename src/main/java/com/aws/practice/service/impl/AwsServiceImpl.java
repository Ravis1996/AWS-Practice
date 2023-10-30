package com.aws.practice.service.impl;

import com.aws.practice.domain.ServiceRecord;
import com.aws.practice.repository.AwsRepository;
import com.aws.practice.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> getData(String type) {
        List<ServiceRecord> records = awsRepository.findAll().stream()
                .filter(serviceRecord -> type.equals(serviceRecord.getType())).collect(Collectors.toList());
        if("EC2".equals(type)) {
            return records.stream().map(ServiceRecord::getJobId).collect(Collectors.toList());
        } else if("S3".equals(type)) {
            return records.stream().map(ServiceRecord::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void getS3Data(String name) {
        software.amazon.awssdk.regions.Region region = software.amazon.awssdk.regions.Region.AP_SOUTHEAST_1;

        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
        List<String> files = listBucketObjects(s3, name);
        ServiceRecord serviceRecord = awsRepository.findByNameAndType(name, "S3");
        serviceRecord.setFiles(files);
        awsRepository.save(serviceRecord);
    }

    @Override
    public Integer getS3FilesCount(String name) {
        ServiceRecord serviceRecord = awsRepository.findByNameAndType(name, "S3");
        return serviceRecord.getFiles().size();
    }

    private List<String> listBucketObjects(S3Client s3, String bucketName) {

        List<String> files = new ArrayList<>();
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                files.add(myValue.key());
                System.out.print("\n The name of the key is " + myValue.key());
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return files;
    }

}
