package com.project.viewe.controller;

import com.project.viewe.dto.CommentDtoRequest;
import com.project.viewe.dto.CommentDtoResponse;
import com.project.viewe.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/post/{postId}/")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@PathVariable Long postId, @RequestBody CommentDtoRequest commentDtoRequest) {
        commentService.addComment(postId, commentDtoRequest);
    }

    @PostMapping("comment/{commentId}/sub-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSubComment(@PathVariable Long postId,
                              @PathVariable Long commentId,
                              @RequestBody CommentDtoRequest commentDtoRequest) {
        log.info("I am in addCommentToComment");
        commentService.addSubComment(postId, commentId, commentDtoRequest);
    }

    @GetMapping("comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getAllComments(@PathVariable Long postId) {
        return commentService.getAllComments(postId);
    }

    @PostMapping("comment/{commentId}/visual-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public String addVisualCommentAndTextToComment(@PathVariable Long postId,
                                                   @PathVariable Long commentId,
                                                   @RequestPart("multipartFile") MultipartFile multipartFile,
                                                   @RequestPart("commentDto") CommentDtoRequest commentDtoRequest) {
        return commentService.addVisualCommentAndTextToComment(postId, commentId, multipartFile, commentDtoRequest);
    }

    @PostMapping("comment/visual-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVisualCommentToPost(@PathVariable Long postId,
                                          @RequestPart("multipartFile") MultipartFile multipartFile,
                                          @RequestPart("commentDto") CommentDtoRequest commentDtoRequest) {
        commentService.addVisualCommentToPost(postId, multipartFile, commentDtoRequest);
    }


}
