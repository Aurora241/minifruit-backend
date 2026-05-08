package com.minifruit.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadImage(String filename, byte[] data, String contentType) {
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filename;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.set("x-upsert", "true");

        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + filename;
    }
}
