package com.projects.ams.rest.request;

import com.projects.ams.validation.constraints.StrongPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 12)
    @Pattern(regexp = "[a-z][0-9a-z]{2,11}")
    private String username;
    @StrongPassword
    private String password;
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    private String lastName;
}
