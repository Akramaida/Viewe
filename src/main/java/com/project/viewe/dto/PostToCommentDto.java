package com.project.viewe.dto;

import com.project.viewe.model.Post;
import lombok.Data;

@Data
public class PostToCommentDto {
    private String text;
    private String username;
}
