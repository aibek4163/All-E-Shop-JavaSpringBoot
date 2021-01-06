package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Comment comment);
    List<Comment> getAllComments();
    Comment getCommentById(Long id);
    void deleteComment(Comment comment);
    Comment updateComment(Comment comment);
    List<Comment> findAllByItem_Id(Long id);
}
