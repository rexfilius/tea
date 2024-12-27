package github.com.rexfilius.tea.modules.comment.controller;

import github.com.rexfilius.tea.modules.comment.model.CommentDto;
import github.com.rexfilius.tea.modules.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a comment")
    @PostMapping("v1/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable long postId,
            @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(
                commentService.createComment(postId, commentDto),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get all comments under a post")
    @GetMapping("v1/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @Operation(summary = "Get a comment under a post")
    @GetMapping("v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @Operation(summary = "Update a comment")
    @PutMapping("v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        CommentDto updatedComment = commentService.updateCommentById(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @Operation(summary = "Delete a comment")
    @DeleteMapping("v1/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
