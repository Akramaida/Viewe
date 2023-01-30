package com.project.viewe.controller;

import com.project.viewe.dto.PostDtoRequest;
import com.project.viewe.dto.PostDtoResponse;
import com.project.viewe.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addPost(@RequestPart("file") MultipartFile file, @RequestPart("post") PostDtoRequest postDto) {
        return postService.addPost(file, postDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDtoResponse> getPostById(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<PostDtoResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/{postId}/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String addThumbnail(@PathVariable Long postId, @RequestParam("file") MultipartFile multipartFile) {
        return postService.addThumbnail(postId, multipartFile);
    }

}
