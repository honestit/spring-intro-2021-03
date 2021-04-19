package com.projects.ams.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddAdvertRequest {

    @NotBlank
    @Size(min = 5, max = 120)
    private String title;
    @NotBlank
    @Size(min = 25, max = 650)
    private String description;
}
