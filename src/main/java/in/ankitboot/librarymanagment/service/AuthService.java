package in.ankitboot.librarymanagment.service;

import in.ankitboot.librarymanagment.dto.LoginRequest;
import in.ankitboot.librarymanagment.dto.LoginResponse;
import in.ankitboot.librarymanagment.dto.RegisterRequest;
import in.ankitboot.librarymanagment.entity.Member;
import in.ankitboot.librarymanagment.entity.User;
import in.ankitboot.librarymanagment.repository.MemberRepository;
import in.ankitboot.librarymanagment.repository.UserRepository;
import in.ankitboot.librarymanagment.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public String register(RegisterRequest request) {

        // Duplicate username check
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }

        // Role decide karo - agar client ne nahi bheja to default MEMBER
        String role = (request.getRole() == null || request.getRole().isBlank())
                ? "MEMBER"
                : request.getRole().toUpperCase();

        // User entity banao - password ENCRYPT karke store karo, plain text kabhi nahi
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        // Member profile bhi bana do, User se One-to-One link karke
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setUser(savedUser);

        memberRepository.save(member);

        return "User registered successfully with username: " + savedUser.getUsername();
    }

    public LoginResponse login(LoginRequest request) {

        // AuthenticationManager khud username+password verify karta hai
        // (CustomUserDetailsService + PasswordEncoder use karke)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        // Verify ho gaya -> ab token banao
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Member member = memberRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Member Profile Not Found"));

        return new LoginResponse(token, user.getUsername(), user.getRole() ,   member.getName(),
                member.getEmail(),
                member.getPhone() );
    }
}
