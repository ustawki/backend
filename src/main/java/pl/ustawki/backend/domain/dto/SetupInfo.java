package pl.ustawki.backend.domain.dto;

import lombok.Data;

@Data
public class SetupInfo {
    private long id;
    private MyPosition myPosition;
    private String title;
    private When when;
    private String description;
    private String www;
    private String facebook;
    private String type;
    private boolean male;
    private boolean female;
}
