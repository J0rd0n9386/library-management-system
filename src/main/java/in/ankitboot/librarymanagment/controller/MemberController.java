package in.ankitboot.librarymanagment.controller;


import in.ankitboot.librarymanagment.entity.Member;
import in.ankitboot.librarymanagment.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // GET /api/members  -> SIRF ADMIN (sab members ki list)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // GET /api/members/{id}  -> ADMIN + MEMBER (apna profile dekh sakta hai)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // PUT /api/members/{id}  -> SIRF ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        Member existing = memberService.getMemberById(id);
        existing.setName(member.getName());
        existing.setEmail(member.getEmail());
        existing.setPhone(member.getPhone());
        return ResponseEntity.ok(memberService.saveMember(existing));
    }

    // DELETE /api/members/{id}  -> SIRF ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }
}