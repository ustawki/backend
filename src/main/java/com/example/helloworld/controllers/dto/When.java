package com.example.helloworld.controllers.dto;

import java.util.List;

import lombok.Data;

@Data
public class When {
    private List<Integer> days;
    private int hour;
    private int minute;
}
