package com.hcmute.myanime.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.hcmute.myanime.config.DigitalOceanSpaceConfig.*;

@Service
public class DigitalOceanSpaceService {

    private final AmazonS3 space;

    public DigitalOceanSpaceService() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )
        );

        space = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                serviceEndpoint,
                                signingRegion)
                )
                .build();
    }

    public List<String> getFileNames() {

        ListObjectsV2Result result = space.listObjectsV2(spaceName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        objects.stream()
                .forEach(s3ObjectSummary -> {
                    System.out.println(s3ObjectSummary.toString());
                });
        return objects.stream()
                .map(s3ObjectSummary -> {
                    return s3ObjectSummary.getKey();
                }).collect(Collectors.toList());
    }

    public String uploadFile(InputStream fileInputStream, String FileContentType, String imgName, String directory) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(FileContentType);
        String url = "-1";
        try {
            space.putObject(
                    new PutObjectRequest(
                            spaceName + "/" + directory,
                            imgName,
                            fileInputStream,
                            objectMetadata
                            ).withCannedAcl(CannedAccessControlList.PublicRead)
            );
            url = space.getUrl(spaceName,  directory + "/" + imgName).toString();
        } catch (Exception exception) {
            return url;
        }
        return url;
    }

    public boolean deleteFileVideo(String path) {
        try {
            space.deleteObject(new DeleteObjectRequest(spaceName, path));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
