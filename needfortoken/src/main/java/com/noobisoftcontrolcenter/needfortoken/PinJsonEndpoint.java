package com.noobisoftcontrolcenter.needfortoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PinJsonEndpoint {

    @Autowired
    private PinataService pinataService;

    @PostMapping("/pinJson")
    public String pinJson(@RequestBody MetadataRequest metadataRequest) {
        try {
            return pinataService.pinJson(metadataRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to pin JSON to IPFS: " + e.getMessage();
        }
    }
}

