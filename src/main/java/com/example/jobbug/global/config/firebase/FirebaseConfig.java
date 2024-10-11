package com.example.jobbug.global.config.firebase;

import com.example.jobbug.global.property.FirebaseProperty;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Configuration
public class FirebaseConfig {

    private final FirebaseProperty firebaseProperty;
    private final FirebaseMessageListener firebaseMessageListener;

    // 스프링 빈 초기화 이후 수행
    @PostConstruct
    public void initFirebase() {
        ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");

        try (InputStream serviceAccountStream = serviceAccount.getInputStream()) { // try-with-resources
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setDatabaseUrl(firebaseProperty.getDatabaseUrl())
                    .build();

            FirebaseApp.initializeApp(options);

            firebaseMessageListener.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
