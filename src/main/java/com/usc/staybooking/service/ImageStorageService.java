package com.usc.staybooking.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.usc.staybooking.exception.GCSUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageStorageService {
    @Value("${gcs.bucket}")
    private String bucketName;

    public String save(MultipartFile file) throws GCSUploadException {
        Credentials credentials = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("credentials.json");
            credentials = GoogleCredentials.fromStream(is);
        } catch (IOException exception) {
            throw new GCSUploadException("Failed to load credentials.json");
        }
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        // blob is a file I want to upload to cloud
        // BTW, look at this UUID utility! It creates a random universally unique ID, and it comes with java!
        String filename = UUID.randomUUID().toString();
        BlobInfo blobInfo = null;
        try {
            blobInfo = storage.createFrom(
                    BlobInfo
                            .newBuilder(bucketName, filename)
                            .setContentType("image/jpeg")
                            .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                            .build(),
                    file.getInputStream());
        } catch (IOException e) {
            throw new GCSUploadException("Failed to upload images to GCS");
        }

        return blobInfo.getMediaLink();
    }

}
