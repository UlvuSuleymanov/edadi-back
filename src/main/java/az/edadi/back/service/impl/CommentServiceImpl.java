package az.edadi.back.service.impl;

import az.edadi.back.entity.User;
import az.edadi.back.entity.post.Comment;
import az.edadi.back.entity.post.CommentVote;
import az.edadi.back.entity.post.Post;
import az.edadi.back.service.CommentService;
import az.edadi.back.service.ImageService;
import az.edadi.back.model.request.CommentRequestModel;
import az.edadi.back.model.response.CommentResponseModel;
import az.edadi.back.repository.CommentRepository;
import az.edadi.back.repository.PostRepository;
import az.edadi.back.repository.UserRepository;
import az.edadi.back.utility.AuthUtil;
import az.edadi.back.repository.CommentVoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl  implements CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final CommentVoteRepository commentVoteRepository;


    @Autowired
    public CommentServiceImpl(UserRepository userRepository,
                              PostRepository postRepository,
                              CommentRepository commentRepository,
                              ImageService imageService,
                              CommentVoteRepository commentVoteRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.imageService = imageService;
        this.commentVoteRepository = commentVoteRepository;
    }

    @Override
    public Comment commentBuilder(CommentRequestModel commentRequestModel, Long postId) {

       Long id = AuthUtil.getCurrentUserId();

       Post post = postRepository.getOne(postId);
       User user = userRepository.getOne(id);


         Comment comment = new Comment();
         comment.setDate(new Date());
         comment.setUser(user);
         comment.setPost(post);
         comment.setCommentText(commentRequestModel.getCommentText());
         return comment;

    }



    @Override
    public List<CommentResponseModel> getComments(Long postId, int page, int size, String sort) {

         Pageable pageable= PageRequest.of(page,size);
         List<Comment> commentList = new ArrayList<>();

         Optional<Post> post = postRepository.findById(postId);
         if(post.isPresent()) {
             switch (sort) {
                 case"mostLiked":commentList=commentRepository.getTopLikedComment(postId,pageable);

                 default:
                     commentList = commentRepository.findByPost_id(postId,pageable);

             }

             return commentsToResponseModels(commentList);

         }
         return null;

    }

    @Override
    public Long likeComment(Long commentId, Long userId) {
        CommentVote commentVote = new CommentVote();
        commentVote.setDate(new Date());
        commentVote.setUser(userRepository.getOne(userId));
        commentVote.setComment(commentRepository.getOne(commentId));

        return commentVoteRepository.save(commentVote).getId();
    }


    @Override
    public void disLikeComment(Long commentId, Long userId){
        Optional<CommentVote> commentVote = commentVoteRepository.getCommentVoteByIds(userId,commentId);
        if(commentVote.isPresent()){
            commentVoteRepository.delete(commentVote.get());
        }

     }

    @Override
    public List<CommentResponseModel> commentsToResponseModels(List<Comment> comments) {
        boolean authenticated=AuthUtil.userIsAuthenticated();
        boolean isLiked=false;
        Long userId =null;

        if(authenticated)
        {
             AuthUtil.getCurrentUserId();
             userId=AuthUtil.getCurrentUserId();
        }
        List<CommentResponseModel> commentResponseModelList = new ArrayList<>();

        for(Comment comment:comments){
            isLiked=false;

            if(authenticated){
                isLiked=userLiked(comment.getId(),userId);
             }
            commentResponseModelList.add(new CommentResponseModel(comment,isLiked));

        }

        return commentResponseModelList;

    }

    @Override
    public boolean userLiked(Long commentId, Long userId) {
        Optional<CommentVote> commentVote = commentVoteRepository.getCommentVoteByIds(userId,commentId);

        if(commentVote.isPresent())
        {
            return true;
        }

        return false;

    }
}