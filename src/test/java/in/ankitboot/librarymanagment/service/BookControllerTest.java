package in.ankitboot.librarymanagment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ankitboot.librarymanagment.controller.BookController;
import in.ankitboot.librarymanagment.entity.Book;
import in.ankitboot.librarymanagment.security.CustomUserDetailsService;
import in.ankitboot.librarymanagment.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

// @WebMvcTest sirf Controller layer load karta hai, poori app nahi - fast test
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc; // fake HTTP requests bhejne ke liye, real server chalu nahi karna padta

    @MockitoBean
    private BookService bookService;  // Service ko mock kar diya, real DB nahi chahiye

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = {"ADMIN", "MEMBER"})  // fake logged-in user simulate karta hai
    void  testGetAllBooks_Success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Atomic Habits");
        book.setAuthor("James Howard");

        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Atomic Habits"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")  // ADMIN role se book add karna - allow hona chahiye

    void  testAddBook_AsAdmin_Success() throws Exception{
        Book newBook = new Book();
        newBook.setTitle("Deep Work");
        newBook.setAuthor("Cal Newport");
        newBook.setTotalCopies(3);
        newBook.setAvailableCopies(3);

        when(bookService.saveBook(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "MEMBER") // MEMBER role se book add karna - 403 aana chahiye
    void testAddBook_AsMember_Forbidden() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("Deep Work");

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllBooks_WithoutLogin_Unauthorized() throws Exception {
        // @WithMockUser bilkul nahi lagaya - matlab bina login ke request ja rahi hai
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

}
