package com.noobisoftcontrolcenter.tokemon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noobisoftcontrolcenter.tokemon.model.MetadataRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PinataService {

    @Value("${pinata.api.key}")
    private String pinataApiKey;

    @Value("${pinata.api.secret}")
    private String pinataApiSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PinataService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> getMetadata(String ipfsHash) {
        String url = "https://ipfs.io/ipfs/" + ipfsHash;
        return restTemplate.getForObject(url, List.class);
    }

    public String pinJson(MetadataRequest metadataRequest) throws Exception {
        String url = "https://api.pinata.cloud/pinning/pinJSONToIPFS";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("pinata_api_key", pinataApiKey);
        headers.set("pinata_secret_api_key", pinataApiSecret);

        Map<String, Object> body = new HashMap<>();
        body.put("pinataContent", metadataRequest.getMetadata());
        body.put("pinataMetadata", Map.of("name", metadataRequest.getFilename()));

        String json = objectMapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        return response.getBody();
    }
}
