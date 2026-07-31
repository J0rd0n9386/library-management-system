package in.ankitboot.librarymanagment.service;

import in.ankitboot.librarymanagment.entity.Book;
import in.ankitboot.librarymanagment.exception.ResourceNotFoundException;
import in.ankitboot.librarymanagment.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;   // fake repository - real DB nahi chahiye

    @InjectMocks
    private BookService bookService;          // mock repository isme automatically inject hoga

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Atomic Habits");
        book.setAuthor("James Clear");
        book.setGenre("Self-help");
        book.setIsbn("9781234567890");
        book.setTotalCopies(5);
        book.setAvailableCopies(5);
    }

    @Test
    @DisplayName("getBookById - book mile to return kare")
    void testGetBookById_Success() {
        // Mockito ko sikhaya: jab findById(1L) call ho, to ye fake Book do
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        /*
        assertEquals(expected, actual);
        assertEquals() JUnit ka assertion method hai.
        if expecteed and actual , same nhi hue then test fail
         */
        assertEquals("Atomic Habits", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);   // confirm karo method exactly 1 baar call hua
    }

    @Test
    @DisplayName("getBookById - book na mile to ResourceNotFoundException aaye")
    void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Assert karo ki exception throw hoti hai
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getBookById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    @DisplayName("getAllBooks - saari books ki list return kare")
    void testGetAllBooks() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Deep Work");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book, book2));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("saveBook - naya book save ho jaana chahiye")
    void testSaveBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertNotNull(result);
        assertEquals("Atomic Habits", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("deleteBook - repository ka deleteById call hona chahiye")
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
/*
assertEquals(expected, actual);
 assertEquals() JUnit ka assertion method hai.

Iska kaam hota hai:

Expected value aur Actual value ko compare karna.

Agar dono same hain → ✅ Test Pass

Agar alag hain → ❌ Test Fail
 */