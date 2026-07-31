package in.ankitboot.librarymanagment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_copies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many BookCopies -> One Book (owning side, foreign key yaha banega)
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "copy_number", nullable = false)
    private String copyNumber;   // e.g. "COPY-001"

    @Column(nullable = false)
    private String status = "AVAILABLE";   // AVAILABLE, ISSUED, LOST, DAMAGED
}