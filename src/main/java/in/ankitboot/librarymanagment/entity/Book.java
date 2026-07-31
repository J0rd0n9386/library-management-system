package in.ankitboot.librarymanagment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.List;

@Entity

@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String author;

    private String genre;

    @Column(unique = true)
    private String isbn;

    @Column(name = "total_copies")
    private Integer totalCopies;

    @Column(name = "available_copies")
    private Integer availableCopies;

    // Ek Book ki multiple physical copies ho sakti hain

    @OneToMany(mappedBy = "book" , cascade = CascadeType.ALL ,orphanRemoval = true )
    private List<BookCopy> Copies;


}
