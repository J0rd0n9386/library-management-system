package in.ankitboot.librarymanagment.service;

import in.ankitboot.librarymanagment.entity.Book;
import in.ankitboot.librarymanagment.entity.Member;
import in.ankitboot.librarymanagment.repository.BookRepository;
import in.ankitboot.librarymanagment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }
}
