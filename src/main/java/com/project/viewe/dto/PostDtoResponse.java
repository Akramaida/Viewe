package com.project.viewe.dto;


import com.project.viewe.model.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDtoResponse {
    private String title;
    private String name;
    private String username;
    private String description;
    private List<String> tags = new ArrayList<>();
    private PostStatus postStatus;
    private List<CommentDtoResponse> commentList;
    private Long likeCount;
    private Long disLikeCount;
    private Long viewCount;
    private String photoUrl;
    private String videoUrl;
    private String videoThumbnailUrl;
}
