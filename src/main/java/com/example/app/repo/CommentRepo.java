package com.example.app.repo;

import com.example.app.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true, value = "select * from comment where lot_id = :id")
    List<Comment> getCommentsByLot(@Param("id") Long id);

}
