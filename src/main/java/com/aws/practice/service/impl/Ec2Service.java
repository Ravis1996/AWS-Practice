package com.aws.practice.service.impl;

import com.aws.practice.domain.ServiceRecord;
import com.aws.practice.repository.AwsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

@Service
public class Ec2Service implements Runnable{

    @Autowired
    private AwsRepository awsRepository;

    @Override
    public void run() {
        software.amazon.awssdk.regions.Region region = software.amazon.awssdk.regions.Region.AP_SOUTHEAST_1;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
        String name = "kartika";
        String instanceId = createEC2Instance(ec2, name ,"ami-06791f9213cbb608b");
        ServiceRecord record = new ServiceRecord( name, "EC2" ,instanceId);
        awsRepository.save(record);
    }

    private String createEC2Instance(Ec2Client ec2, String name, String amiId)  {

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(InstanceType.T2_MICRO)
                .maxCount(1)
                .minCount(1)
                .build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();

        Tag tag = Tag.builder()
                .key("Name")
                .value(name)
                .build();

        CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                .resources(instanceId)
                .tags(tag)
                .build();

        try {
            CreateTagsResponse createTagsResponse = ec2.createTags(tagRequest);
            System.out.printf(
                    "Successfully started EC2 Instance %s based on AMI %s",
                    instanceId, amiId);

            return instanceId;

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }

}