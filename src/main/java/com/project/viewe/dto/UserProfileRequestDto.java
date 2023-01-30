package com.project.viewe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDto {
    private String username;
    private String email;
    private String status;
    private String userDescription;
    private String firstName;
    private String lastName;
    private String oldPass;
    private String newPassword;
}
