package in.ankitboot.librarymanagment.service;

import in.ankitboot.librarymanagment.entity.BookCopy;
import in.ankitboot.librarymanagment.repository.BookCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    public List<BookCopy> getCopiesByBookId(Long bookId) {
        return bookCopyRepository.findByBookId(bookId);
    }

    public BookCopy saveCopy(BookCopy copy) {
        return bookCopyRepository.save(copy);
    }

    // Book issue karte time pehli AVAILABLE copy dhoondhta hai
    public BookCopy getAvailableCopy(Long bookId) {
        return bookCopyRepository.findFirstByBookIdAndStatus(bookId, "AVAILABLE")
                .orElseThrow(() -> new RuntimeException("No available copy for book id: " + bookId));
    }
}