package com.projects.ams.rest.converters;

import com.projects.ams.model.domain.User;
import com.projects.ams.rest.request.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User from(CreateUserRequest request) {
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }
}
