package com.example.app.dto.lot;

import com.example.app.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private String text;
    private String name;

    public static CommentResponseDto build(Comment comment){
        return new CommentResponseDto(comment.getCommentText(), comment.getUser().getName());
    }

}
