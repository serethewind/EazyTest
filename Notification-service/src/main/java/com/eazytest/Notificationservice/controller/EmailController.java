package com.eazytest.Notificationservice.controller;

import com.eazytest.Notificationservice.dto.EmailDetails;
import com.eazytest.Notificationservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("api/v1/email"))
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @GetMapping("testing")
    public ResponseEntity<String> checkTesting(){
        return ResponseEntity.ok("Testing api gateway");
    }

    @PostMapping("simpleMessage")
    public ResponseEntity<String> sendSimpleMessage(@RequestBody EmailDetails emailDetails){
        return ResponseEntity.ok(emailService.sendSimpleMessage(emailDetails));
    }

    @PostMapping("message")
    public ResponseEntity<String> sendMessageWithAttachment(@RequestBody EmailDetails emailDetails){
        return ResponseEntity.ok(emailService.sendMessageWithAttachment(emailDetails));
    }
}
