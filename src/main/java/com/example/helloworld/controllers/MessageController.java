package com.example.helloworld.controllers;


import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.helloworld.models.Message;
import com.example.helloworld.services.MessageService;

import com.example.helloworld.controllers.dto.SetupInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

  private final MessageService messageService;
  private final ResourceLoader resourceLoader;

  @GetMapping("/public")
  public Message getPublic() {
    return messageService.getPublicMessage();
  }

  @GetMapping("/public/setups")
  public List getAll() throws IOException {
    Resource resource = resourceLoader.getResource(
            "classpath:setups.json");
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), List.class);
  }

  @GetMapping("/protected")
  public Message getProtected() {
    return messageService.getProtectedMessage();
  }

  @GetMapping("/protected/setups")
  public List<SetupInfo> getAllProtected() throws IOException {
    Resource resource = resourceLoader.getResource(
            "classpath:setups.json");
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), List.class);
  }

  @GetMapping("/admin")
  public Message getAdmin() {
    return messageService.getAdminMessage();
  }
}
