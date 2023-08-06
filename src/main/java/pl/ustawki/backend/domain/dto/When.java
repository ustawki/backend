package pl.ustawki.backend.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class When {
    private List<Integer> days;
    private int hour;
    private int minute;
}
