package com.staybits.gigmapapi.notifications.infrastructure.cloudmessaging.firebase.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;

@Service
public class FirebaseConfig {

    // Change it to new
    // FileInputStream("/etc/secrets/firebase-service-account.json"); before
    // production
    // Change it to new
    // FileInputStream("src/main/resources/firebase-service-account.json"); when
    // development
    @PostConstruct
    public void initialize() throws IOException {
        try (FileInputStream serviceAccount =
                     new FileInputStream("src/main/resources/firebase-service-account.json")) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("FirebaseConfig initialized successfully");
            }
        }
    }
}