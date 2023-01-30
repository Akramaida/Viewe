package com.project.viewe.repo;

import com.project.viewe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.id =:id")
    Post findPostById(@Param("id") Long id);
}
