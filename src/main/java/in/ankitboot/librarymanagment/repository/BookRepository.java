package in.ankitboot.librarymanagment.repository;

import in.ankitboot.librarymanagment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book,Long> {
    List<Book> findByAuthorContainingIgnoreCase(String author);

    Optional<Book> findByIsbn(String isbn);
    List<Book> findByGenre(String genre);
    Optional<Book> findByTitleIgnoreCaseContaining(String title);

}
