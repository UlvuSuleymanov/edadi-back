package az.edadi.back.service;

import az.edadi.back.entity.post.Comment;
import az.edadi.back.model.request.CommentRequestModel;
import az.edadi.back.model.response.CommentResponseModel;

import java.util.List;

public interface CommentService {
    Comment commentBuilder(CommentRequestModel commentRequestModel, Long postId);
    List<CommentResponseModel> getComments(Long postId, int page, int size, String sort);
    Long likeComment(Long commentId,Long userId);
    void disLikeComment(Long commentId,Long userId);
    List<CommentResponseModel> commentsToResponseModels(List<Comment>comments);
    boolean userLiked(Long commentId,Long userId);
}