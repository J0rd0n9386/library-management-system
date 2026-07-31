package in.ankitboot.librarymanagment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {
@Mock
    private JwtUtil jwtUtil;
@Mock
    private  CustomUserDetailsService customUserDetailsService;
@Mock
    private HttpServletRequest request;
@Mock
    private HttpServletResponse response;
@Mock
    private FilterChain filterChain;
@Mock
    private UserDetails userDetails;

@InjectMocks
    private JwtAuthFilter jwtAuthFilter;

@BeforeEach
    void Setup(){
    SecurityContextHolder.clearContext();
}
    //Test 1: Valid Token → Authentication Set Hona Chahiye
    @Test
    void testValidToken_SetsAuthentication() throws Exception {
        String token = "token@132";
        String username = "ankit123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        verify(filterChain, times(1)).doFilter(request, response);
    }

//Test 2: Header Hi Nahi Hai → Authentication Set Nahi Honi Chahiye
    @Test
    void testNoAuthorizationHeader_DoesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    //Test 3: Galat Format Header → Authentication Set Nahi Honi Chahiye
    @Test
    void testInvalidBearerFormat_DoesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }


    //Test 4: Token Hai Par Invalid/Expired Hai
    @Test
    void testInvalidToken_DoesNotSetAuthentication() throws Exception {
        String token = "expired.or.invalid.token";
        String username = "ankit123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
