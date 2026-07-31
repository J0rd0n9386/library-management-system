package in.ankitboot.librarymanagment.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "issue_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id" ,nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "book_Copy_id" ,nullable = false)
    private BookCopy bookCopy;

    @Column(name = "issue_date" , nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date",nullable = false)
    private LocalDate returnDate;  // null jab tak book return na ho

    @Column(name = "fine_amount" ,nullable = false)
    private Double fineAmount;

    @Column(nullable = false)
    private boolean returned = false;

}
