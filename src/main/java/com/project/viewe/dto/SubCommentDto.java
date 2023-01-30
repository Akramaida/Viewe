package com.project.viewe.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubCommentDto {
    private String text;
    private Long likeCount;
    private Long disLikeCount;
    private String username;
    private List<String> visualComment = new ArrayList<>();
}
