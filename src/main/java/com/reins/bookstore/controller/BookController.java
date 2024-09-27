package com.reins.bookstore.controller;

import com.reins.bookstore.entity.Book;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.models.CommentDTO;
import com.reins.bookstore.models.CommentRequest;
import com.reins.bookstore.models.PagedItems;
import com.reins.bookstore.service.BookService;
import com.reins.bookstore.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Book", description = "books API")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/api/books")
    @Operation(summary = "search books")
    ResponseEntity<PagedItems<Book>> searchBooks(@RequestParam String tag,
                                                 @RequestParam String keyword,
                                                 @RequestParam Integer pageIndex,
                                                 @RequestParam Integer pageSize) {
        if (pageIndex == null || pageIndex < 0 || pageSize == null || pageSize < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bookService.searchBooks(tag, keyword, pageIndex, pageSize));
    }

    @GetMapping("/api/book/tags")
    @Operation(summary = "get all books tags")
    ResponseEntity<List<String>> getBookTags() {
        return ResponseEntity.ok(bookService.getAllTags());
    }

    @GetMapping("/api/book/{id}")
    @Operation(summary = "get book by id")
    ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/api/books/rank")
    @Operation(summary = "get top 10 best selling books")
    ResponseEntity<List<Book>> getTop10BestsellingBooks() {
        return ResponseEntity.ok(bookService.getTop10BestsellingBooks());
    }

    @GetMapping("/api/book/{id}/comments")
    @Operation(summary = "get book comments")
    ResponseEntity<PagedItems<CommentDTO>> getBookComments(@PathVariable Long id,
                                                           @RequestParam Integer pageIndex,
                                                           @RequestParam Integer pageSize,
                                                           @RequestParam String sort) {
        if (pageIndex == null || pageIndex < 0 || pageSize == null || pageSize < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bookService.getBookComments(id, pageIndex, pageSize, sort, SessionUtils.getUserId()));
    }

    @PostMapping("/api/book/{id}/comments")
    @Operation(summary = "add comment")
    ResponseEntity<ApiResponseBase> addComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(bookService.addBookComment(id, SessionUtils.getUserId(), commentRequest.getContent()));
    }
}
