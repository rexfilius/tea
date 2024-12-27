package github.com.rexfilius.tea.modules.post.controller;

import github.com.rexfilius.tea.modules.post.model.AllPostResponse;
import github.com.rexfilius.tea.modules.post.model.PostDto;
import github.com.rexfilius.tea.modules.post.service.PostService;
import github.com.rexfilius.tea.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Posts")
@RequestMapping("v1/posts")
@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a post")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all posts")
    @GetMapping
    public AllPostResponse getPosts(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(summary = "Get a post")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "Update a post")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable long id,
            @Valid @RequestBody PostDto postDto
    ) {
        return ResponseEntity.ok(postService.updatePost(postDto, id));
    }

    @Operation(summary = "Delete a post")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }

    @Operation(summary = "Get all posts under a category")
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(
            @PathVariable("id") Long categoryId
    ) {
        List<PostDto> posts = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }
}
