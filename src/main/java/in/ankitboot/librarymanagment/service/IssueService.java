package in.ankitboot.librarymanagment.service;


import in.ankitboot.librarymanagment.entity.BookCopy;
import in.ankitboot.librarymanagment.entity.IssueRecord;
import in.ankitboot.librarymanagment.entity.Member;
import in.ankitboot.librarymanagment.repository.IssueRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookCopyService bookCopyService;
    private final MemberService memberService;
    private final FineCalculatorService fineCalculatorService;

    private static final int DUE_DAYS = 14;   // 14 din me return karna hai

    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordRepository.findAll();
    }

    // Book issue karna
    public IssueRecord issueBook(Long memberId, Long bookId) {
        Member member = memberService.getMemberById(memberId);
        BookCopy copy = bookCopyService.getAvailableCopy(bookId);

        copy.setStatus("ISSUED");
        bookCopyService.saveCopy(copy);

        IssueRecord record = new IssueRecord();
        record.setMember(member);
        record.setBookCopy(copy);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(DUE_DAYS));
        record.setReturned(false);
        record.setFineAmount(0.0);

        return issueRecordRepository.save(record);
    }

    // Book return karna - fine calculate karke
    public IssueRecord returnBook(Long issueId) {
        IssueRecord record = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue record not found: " + issueId));

        record.setReturnDate(LocalDate.now());
        record.setReturned(true);

        double fine = fineCalculatorService.calculateFine(record.getDueDate(), LocalDate.now());
        record.setFineAmount(fine);

        BookCopy copy = record.getBookCopy();
        copy.setStatus("AVAILABLE");
        bookCopyService.saveCopy(copy);

        return issueRecordRepository.save(record);
    }

    public List<IssueRecord> getOverdueBooks() {
        return issueRecordRepository.findByReturnedFalse();
    }
}

