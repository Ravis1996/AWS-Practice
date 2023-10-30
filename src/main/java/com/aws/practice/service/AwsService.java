package com.aws.practice.service;

import java.util.List;

public interface AwsService {

    void createService(List<String> services);

    List<String> getData(String type);

    void getS3Data(String name);

    Integer getS3FilesCount(String name);

}
