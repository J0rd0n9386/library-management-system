package in.ankitboot.librarymanagment.service;

import in.ankitboot.librarymanagment.entity.Book;
import in.ankitboot.librarymanagment.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import in.ankitboot.librarymanagment.exception.ResourceNotFoundException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Book not found with id: " + id) );
    }
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
