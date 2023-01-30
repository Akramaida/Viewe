package com.project.viewe.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserProfileResponseDto {
    private String username;
    private String email;
    private String status;
    private List<String> photoProfile = new ArrayList<>();
    private String userDescription;
    private String firstName;
    private String lastName;
}
