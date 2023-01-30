package com.project.viewe.service;

import com.project.viewe.dto.CommentDto;
import com.project.viewe.dto.CommentDtoResponse;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.Comment;
import com.project.viewe.model.Post;
import com.project.viewe.model.User;
import com.project.viewe.repo.CommentRepo;
import com.project.viewe.repo.PostRepo;
import com.project.viewe.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    private final PostService postService;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final UploadVisualComment uploadVisualComment;
    private final ModelMapper modelMapper;

    public void addComment(Long postId, CommentDto commentDto) {
        var user = userService.getCurrentUser();
        var post = postService.findPostById(postId);
        var comment = mapToCommentDto(commentDto, user);
        user.getComments().add(comment);
        userRepo.save(user);
        post.getCommentList().add(comment);
        postRepo.save(post);
        log.info("I am CommentService.addComment under saving");
        save(post, comment);
    }

    private Comment mapToCommentDto(CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUsername(user.getUsername());
        commentRepo.save(comment);
        return comment;
    }

    public void addSubComment(Long postId, Long commentId, CommentDto commentDto) {
        var user = userService.getCurrentUser();
        var post = postService.findPostById(postId);
        var comment = findCommentById(commentId);
        user.getComments().remove(comment);
        post.getCommentList().remove(comment);
        comment.getSubComment().add(mapToCommentDto(commentDto, user));
        post.getCommentList().add(comment);
        user.getComments().add(comment);
        userRepo.save(user);
        save(post, comment);
    }

    public List<CommentDtoResponse> getAllComments(Long postId) {
        var post = postService.findPostById(postId);
        var comment = post.getCommentList();
        return comment.stream().map(this::mapToCommentDtoResponse).toList();
    }

    public CommentDtoResponse mapToCommentDtoResponse(Comment comment){
        return modelMapper.map(comment, CommentDtoResponse.class);
    }

    public String addVisualCommentAndTextToComment(Long postId, Long commentId, MultipartFile multipartFile, CommentDto commentDto) {
        var user = userService.getCurrentUser();
        var post = postService.findPostById(postId);
        var comment = findCommentById(commentId);
        post.getCommentList().remove(comment);
        user.getComments().remove(comment);
        comment.getSubComment().add(mapToVisualCommentDtoRequest(user, commentDto, multipartFile));
        user.getComments().add(comment);
        userRepo.save(user);
        post.getCommentList().add(comment);
        save(post, comment);
        return "Added visual comment successfully";
    }

    public Comment createVisualComment(MultipartFile multipartFile, Comment visualComment) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String url;

        assert extension != null;
        if(extension.equals("mp4")){
            url = uploadVisualComment.uploadVideoFile(multipartFile, fileName);
            visualComment.getVisualComment().add(url);
        }else if(extension.equals("jpeg")){
            url = uploadVisualComment.uploadPhotoFile(multipartFile, fileName);
            visualComment.getVisualComment().add(url);
        }
        return visualComment;
    }

    public void addVisualCommentToPost(Long postId, MultipartFile multipartFile, CommentDto commentDto) {
        var post = postRepo.findById(postId)
                .orElseThrow(()-> new ProjectException("Cannot find post by id: " + postId));
        var user = userService.getCurrentUser();
        var visualComment = mapToVisualCommentDtoRequest(user, commentDto, multipartFile);
        user.getComments().add(visualComment);
        userRepo.save(user);
        post.getCommentList().add(visualComment);
        save(post, visualComment);
    }

    private Comment mapToVisualCommentDtoRequest(User user, CommentDto commentDto, MultipartFile multipartFile) {
        Comment visualComment = new Comment();
        visualComment = createVisualComment(multipartFile, visualComment);
        visualComment.setText(commentDto.getText());
        visualComment.setUsername(user.getUsername());
        commentRepo.save(visualComment);
        return visualComment;
    }

    public Comment findCommentById(Long id){
        return commentRepo.findCommentById(id)
                .orElseThrow(()-> new ProjectException("Cannot find comment by id " + id));
    }

    public void save(Post post, Comment comment){
        postRepo.save(post);
        commentRepo.save(comment);
    }
}
