package com.reins.bookstore.service.impl;

import com.reins.bookstore.constants.Messages;
import com.reins.bookstore.dao.BookDAO;
import com.reins.bookstore.dao.CommentDAO;
import com.reins.bookstore.entity.Book;
import com.reins.bookstore.entity.Comment;
import com.reins.bookstore.entity.Tag;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.models.CommentDTO;
import com.reins.bookstore.models.PagedItems;
import com.reins.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookDAO bookDAO;

    @Autowired
    CommentDAO commentDAO;

    @Override
    public PagedItems<Book> searchBooks(String tag, String keyword, Integer pageIndex, Integer pageSize) {
        Page<Book> pagedBooks;
        if (tag == null || tag.isEmpty()) {
            pagedBooks = bookDAO.searchBooksByKeyword(keyword, pageIndex, pageSize);
        } else {
            pagedBooks = bookDAO.searchBooksByTagAndKeyword(tag, keyword, pageIndex, pageSize);
        }
        return new PagedItems<>(pagedBooks.getTotalPages(), pagedBooks.getContent());
    }

    @Override
    public Book getBookById(Long id) {
        return bookDAO.getById(id);
    }

    @Override
    public void deleteBook(Long id) {
        bookDAO.deleteBook(id);
    }

    @Override
    public Book addBook(Book book) {
        return bookDAO.addBook(book);
    }

    @Override
    public List<Book> getTop10BestsellingBooks() {
        return bookDAO.getTop10BestsellingBooks();
    }

    @Override
    public List<String> getAllTags() {
        return bookDAO.getAllTags().stream().map(Tag::getName).collect(Collectors.toList());
    }

    @Override
    public PagedItems<CommentDTO> getBookComments(Long bookId, Integer pageIndex, Integer pageSize, String sort, Long userId) {
        Page<Comment> pagedComments;
        if (sort.equals("createdTime")) {
            pagedComments = commentDAO.getBookCommentsOrderByCreatedTime(bookId, pageIndex, pageSize);
        } else {
            pagedComments = commentDAO.getBookCommentsOrderByLike(bookId, pageIndex, pageSize);
        }

        List<CommentDTO> dtos = new ArrayList<>();
        for (Comment comment : pagedComments.getContent()) {
            dtos.add(CommentDTO.from(comment, commentDAO.isLiked(userId, comment.getId())));
        }
        return new PagedItems<>(pagedComments.getTotalPages(), dtos);
    }

    @Override
    public ApiResponseBase addBookComment(Long id, Long userId, String content) {
        commentDAO.addComment(id, userId, content);
        return ApiResponseBase.succeed(Messages.ADD_COMMENT_SUCCEED);
    }
}
