package in.ankitboot.librarymanagment.controller;

import in.ankitboot.librarymanagment.entity.IssueRecord;
import in.ankitboot.librarymanagment.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    // GET /api/issues  -> ADMIN dekh sakta hai sab records
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IssueRecord>> getAllIssueRecords() {
        return ResponseEntity.ok(issueService.getAllIssueRecords());
    }

    // POST /api/issues/issue?memberId=1&bookId=2  -> SIRF ADMIN (librarian issue karta hai)
    @PostMapping("/issue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IssueRecord> issueBook(@RequestParam Long memberId,
                                                 @RequestParam Long bookId) {
        IssueRecord record = issueService.issueBook(memberId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    // PUT /api/issues/return/{issueId}  -> SIRF ADMIN
    @PutMapping("/return/{issueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IssueRecord> returnBook(@PathVariable Long issueId) {
        IssueRecord record = issueService.returnBook(issueId);
        return ResponseEntity.ok(record);
    }

    // GET /api/issues/overdue  -> ADMIN, saari pending/overdue books
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<IssueRecord>> getOverdueBooks() {
        return ResponseEntity.ok(issueService.getOverdueBooks());
    }
}