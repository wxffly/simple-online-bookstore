package com.reins.bookstore;

import com.reins.bookstore.entity.Book;
import com.reins.bookstore.repository.BookRepository;
import com.reins.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SimpleOnlineBookstoreApplicationTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void whenGetAllBooks_thenSuccess() {
        // given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        List<Book> bookList = Arrays.asList(book1);

        when(bookRepository.findAll()).thenReturn(bookList);

        // when
        List<Book> result = bookService.getTop10BestsellingBooks();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void whenFindBookById_thenSuccess() {
        // given
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Book 1");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // when
        Book result = bookService.getBookById(id);

        // then
        assertEquals("Book 1", result.getTitle());
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    public void whenDeleteBook_thenSuccess() {
        // given
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Book 1");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // when
        bookService.deleteBook(id);

        // then
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    public void whenCreateBook_thenSuccess() {
        // given
        Book book = new Book();
        book.setTitle("New Book");

        // when
        Book savedBook = bookService.addBook(book);

        // then
        assertNotNull(savedBook);
        assertEquals("New Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

   
}