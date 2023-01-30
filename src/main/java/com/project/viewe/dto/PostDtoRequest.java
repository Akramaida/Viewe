package com.project.viewe.dto;

import com.project.viewe.model.PostStatus;
import com.project.viewe.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoRequest {
    private String username;
    private String name;
    private String title;
    private String description;
    private List<String> tags = new ArrayList<>();
    private PostStatus postStatus;
}
