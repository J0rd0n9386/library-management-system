package in.ankitboot.librarymanagment.controller;

import in.ankitboot.librarymanagment.entity.BookCopy;
import in.ankitboot.librarymanagment.service.BookCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-copies")
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    // GET /api/book-copies/book/{bookId}  -> ek book ki saari copies
    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<List<BookCopy>> getCopiesByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookCopyService.getCopiesByBookId(bookId));
    }

    // POST /api/book-copies  -> SIRF ADMIN (nayi copy add karna)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookCopy> addCopy(@RequestBody BookCopy bookCopy) {
        BookCopy saved = bookCopyService.saveCopy(bookCopy);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}