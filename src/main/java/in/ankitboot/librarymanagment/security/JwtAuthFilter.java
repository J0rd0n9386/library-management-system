package in.ankitboot.librarymanagment.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // "Bearer <token>" format check
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // Agar username mila aur abhi tak koi authenticate nahi hua
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
/*
 * ===== JWT AUTH FILTER - REVISION NOTES =====
 *
 * ANALOGY: Ye class = SECURITY GUARD jo har office gate pe khada hai
 * JwtUtil = pass banane/check karne ki machine
 * JwtAuthFilter = wo guard jo us machine ko use karke
 *                 HAR request pe pass check karta hai
 *
 * -------------------------------------
 * @Component
 *    -> Spring automatically is guard ko duty pe laga deta hai
 *
 * @RequiredArgsConstructor (Lombok)
 *    -> final fields (jwtUtil, userDetailsService) ke liye
 *       constructor auto-generate karta hai for DI
 *    -> guard ko duty join karte hi "pass checker" + "employee DB"
 *       dono handover ho jaate hain
 *
 * extends OncePerRequestFilter
 *    -> guarantee: ye check HAR REQUEST PE SIRF EK BAAR chalega
 *    -> guard ek hi baar check karta hai, andar dobara nahi rokta
 *
 * -------------------------------------
 * doFilterInternal() -> MAIN LOGIC (step by step)
 *
 * STEP 1: Header nikaalo
 *    authHeader = request.getHeader("Authorization")
 *    -> guard bolta hai "pass dikhao"
 *
 * STEP 2: Format check + token/username extract
 *    if authHeader starts with "Bearer "
 *        jwt = substring(7)              -> "Bearer " hata ke asli token
 *        username = jwtUtil.extractUsername(jwt)
 *    -> guard format check karta hai, fir pass pe likha naam padhta hai
 *
 * STEP 3: Double-check guard
 *    if (username != null && already authenticated NAHI hai)
 *    -> naam mila hai AUR ye request pehle se authenticate nahi hui
 *    -> tabhi aage badhna hai (avoid duplicate auth)
 *
 * STEP 4: DB se user details laao
 *    userDetails = userDetailsService.loadUserByUsername(username)
 *    -> guard apne register me naam search karta hai
 *       "ye humara employee hai kya? role/designation kya hai?"
 *
 * STEP 5: Token validate karo
 *    if (jwtUtil.validateToken(jwt, userDetails))
 *    -> seal genuine hai? naam match? expire nahi hua?
 *    -> guard UV light se seal check karta hai + expiry dekhta hai
 *
 * STEP 6: Authenticate mark karo
 *    authToken = new UsernamePasswordAuthenticationToken(
 *                    userDetails, null, authorities)
 *    authToken.setDetails(...)
 *    SecurityContextHolder.getContext().setAuthentication(authToken)
 *    -> guard visitor badge issue karta hai + register me entry likhta hai
 *    -> is REQUEST ke duration tak "authenticated" maana jaata hai
 *
 * STEP 7: Aage bhejo (chahe authenticated ho ya na ho)
 *    filterChain.doFilter(request, response)
 *    -> guard visitor ko aage bhej deta hai
 *    -> agar pass invalid tha, to aage controller/security rules
 *       khud access rok denge (protected room nahi khulega)
 *
 * -------------------------------------
 * FULL FLOW (JwtUtil + JwtAuthFilter milke):
 *
 * 1. LOGIN -> JwtUtil.generateToken() -> pass milta hai
 * 2. Har next request -> "Authorization: Bearer <token>" header bhejta hai
 * 3. JwtAuthFilter intercept karta hai (guard duty, OncePerRequestFilter)
 * 4. Token se username nikalta hai -> DB se details laata hai
 * 5. validateToken() se verify karta hai
 * 6. Valid -> SecurityContext me "authenticated" mark
 * 7. Request aage controller tak jaati hai -> access allow/deny decide hota hai
 *
 * NEXT STEP: SecurityConfig me ye filter register karna hota hai
 * (addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class))
 */