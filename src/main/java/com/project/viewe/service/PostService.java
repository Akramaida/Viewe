package com.project.viewe.service;

import com.project.viewe.dto.CommentDtoResponse;
import com.project.viewe.dto.PostDtoRequest;
import com.project.viewe.dto.PostDtoResponse;
import com.project.viewe.dto.SubCommentDto;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.Comment;
import com.project.viewe.model.Post;
import com.project.viewe.model.User;
import com.project.viewe.repo.PostRepo;
import com.project.viewe.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepo postRepo;
    private final UploadService uploadService;
    private final UserService userService;
    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    public String addPost(MultipartFile multipartFile, PostDtoRequest postDtoRequest) {
        Post post = new Post();
        String fileName = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String url;

        assert extension != null;
        if (extension.equals("mp4")) {
            url = uploadService.uploadVideoFile(multipartFile, fileName);
            post = mapToPostDtoRequest(postDtoRequest);
            post.setVideoUrl(url);
        } else if (extension.equals("jpg")) {
            url = uploadService.uploadPhotoFile(multipartFile, fileName);
            post = mapToPostDtoRequest(postDtoRequest);
            post.setVideoUrl(url);
        } else {
            url = "Extension does not support the file " + extension;
        }
        var user = userService.getCurrentUser();
        user.getPosts().add(post);
        postRepo.save(post);
        userRepo.save(user);
        return url;
    }

    private Post mapToPostDtoRequest(PostDtoRequest postDto) {
        //Post post = new Post();
        //post.setUsername(postDto.getUsername());
        //post.setPostStatus(postDto.getPostStatus());
        //post.setDescription(postDto.getDescription());
        //post.setTitle(postDto.getTitle());
        //for(int i = 0; i< postDto.getTags().size(); i++){
        //    post.getTags().add(i, postDto.getTags().get(i));
        //}
        ////post.getTags().forEach(postDto.getTags()::add);
        //post.setPostName(postDto.getName());
        return modelMapper.map(postDto, Post.class);
    }

    PostDtoResponse mapToPostDtoResponse(Post post) {
        PostDtoResponse postDto = new PostDtoResponse();
        //postDto.setPostStatus(post.getPostStatus());
        //postDto.setDescription(post.getDescription());
        //postDto.setPhotoUrl(post.getPhotoUrl());
        //postDto.setVideoUrl(post.getVideoUrl());
        //postDto.setCommentList(post.getCommentList().stream().map(this::mapToCommentDtoResponse).toList());
        //postDto.setDisLikeCount(post.getDisLikeCount());
        //postDto.setLikeCount(post.getLikeCount());
        //for(int i = 0; i< post.getTags().size(); i++){
        //    postDto.getTags().add(i, post.getTags().get(i));
        //}
        ////postDto.getTags().forEach(post.getTags()::add);
        //postDto.setLikeCount(post.getLikeCount());
        //postDto.setDisLikeCount(post.getDisLikeCount());
        //postDto.setName(post.getPostName());
        //postDto.setVideoThumbnailUrl(post.getVideoThumbnailUrl());
        //postDto.setViewCount(post.getViewCount());
        //postDto.setUsername(post.getUsername());
        postDto.setCommentList(post.getCommentList().stream().map(this::mapToCommentDtoResponse).toList());
        modelMapper.map(post, PostDtoResponse.class);
        return postDto;
    }

    public CommentDtoResponse mapToCommentDtoResponse(Comment comment) {
        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();
        //commentDtoResponse.setText(comment.getText());
        //commentDtoResponse.setUsername(comment.getUsername());
        //commentDtoResponse.setDisLikeCount(comment.getDisLikeCount());
        //commentDtoResponse.setLikeCount(comment.getLikeCount());
        //for(int i = 0; i< comment.getVisualComment().size(); i++){
        //    commentDtoResponse.getVisualComment().add(i, comment.getVisualComment().get(i));
        //}
        commentDtoResponse.setSubCommentDto(comment.getSubComment().stream().map(this::mapToSubCommentDto).toList());
        modelMapper.map(comment, CommentDtoResponse.class);
        return commentDtoResponse;
    }

    public SubCommentDto mapToSubCommentDto(Comment comment) {
        //SubCommentDto subCommentDto = new SubCommentDto();
        //subCommentDto.setText(comment.getText());
        //subCommentDto.setUsername(comment.getUsername());
        //subCommentDto.setDisLikeCount(comment.getDisLikeCount());
        //subCommentDto.setLikeCount(comment.getLikeCount());
        //for(int i = 0; i< comment.getVisualComment().size(); i++){
        //    subCommentDto.getVisualComment().add(i, comment.getVisualComment().get(i));
        //}
        return modelMapper.map(comment, SubCommentDto.class);
    }

    public PostDtoResponse getPostById(Long postId) {
        Post post = findPostById(postId);
        post.increaseViewCount();
        log.info("Count " + post.getViewCount().toString());
        if (!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), "anonymousUser")) {
            userService.addToViewHistory(postId);
        }
        postRepo.save(post);
        return mapToPostDtoResponse(post);
    }

    public List<PostDtoResponse> getAllPosts() {
        return postRepo.findAll().stream()
                .map(this::mapToPostDtoResponse).toList();
    }

    public String addThumbnail(Long postId, MultipartFile multipartFile) {
        Post post = findPostById(postId);
        String fileName = multipartFile.getOriginalFilename();
        String url = uploadService.uploadThumbnailFile(multipartFile, fileName);
        post.setVideoThumbnailUrl(url);
        postRepo.save(post);
        return url;
    }

    public Post findPostById(Long postId) {
        Post post = postRepo.findPostById(postId);
        if (post == null) {
            log.error("Post with id - {} has not been found.", postId);
            throw new ProjectException("Cannot find post by id: " + postId);
        }
        log.info("Post with id {} has been found. Post - {}", postId, post);
        return post;
    }

    public List<PostDtoResponse> getAllViewHistory() {
        User user = userService.getCurrentUser();
        var post = user.getViewHistory();
        return post.stream().map(id -> mapToPostDtoResponse(findPostById(id))).toList();
    }

}
