package com.projects.ams.requests;

import lombok.Data;

@Data
public class EditAdvertRequest {

    private Long advertId;
    private String title;
    private String description;
}
