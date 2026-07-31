package in.ankitboot.librarymanagment.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // BCrypt se encrypted store hoga

    @Column(nullable = false)
    private String role;   // "ADMIN" ya "MEMBER"

    // Owning side yaha nahi hai - Member.java me @JoinColumn hai
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Member member;
    /*
    mappedBy = "user" jis side pe likha hota hai, wo side owning nahi hoti —
    ye inverse side hoti hai (yani User)
    jis side pe @JoinColumn hota hai (yani Member), wo owning side hoti hai
   — ye hi actual foreign key column control karti hai DB mein
     */
}

