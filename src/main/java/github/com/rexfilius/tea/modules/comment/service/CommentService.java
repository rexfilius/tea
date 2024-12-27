package github.com.rexfilius.tea.modules.comment.service;

import github.com.rexfilius.tea.exception.ResourceNotFoundException;
import github.com.rexfilius.tea.exception.TeaApiException;
import github.com.rexfilius.tea.modules.comment.model.Comment;
import github.com.rexfilius.tea.modules.comment.model.CommentDto;
import github.com.rexfilius.tea.modules.comment.repo.CommentRepository;
import github.com.rexfilius.tea.modules.post.model.Post;
import github.com.rexfilius.tea.modules.post.repo.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static github.com.rexfilius.tea.utils.ModelMapper.dtoToModel;
import static github.com.rexfilius.tea.utils.ModelMapper.modelToDto;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = dtoToModel(commentDto);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return modelToDto(savedComment);
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map((comment) -> modelToDto(comment))
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return modelToDto(comment);
    }

    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return modelToDto(updatedComment);
    }

    public void deleteCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }


}
