package in.ankitboot.librarymanagment.repository;

import in.ankitboot.librarymanagment.entity.Member;
import in.ankitboot.librarymanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUser(User user);

}
