
/*
1. User /api/auth/login pe POST karta hai (username, password)
       ↓
2. AuthController AuthenticationManager se verify karta hai
       ↓
3. Verify ho gaya → JwtUtil.generateToken() se token banta hai → response mein bhejta hai
       ↓
4. Client agli har request mein header bhejta hai: Authorization: Bearer <token>
       ↓
5. JwtAuthFilter har request pe chalta hai (login/register chhod ke)
       ↓
6. Filter token se username nikalta hai (JwtUtil.extractUsername)
       ↓
7. CustomUserDetailsService DB se user dhoondhta hai (UserRepository)
       ↓
8. Token valid hai → SecurityContextHolder mein "ye user authenticated hai" set ho jaata hai
       ↓
9. SecurityConfig ke rules check hote hain (role ADMIN/MEMBER ke basis pe access milta hai ya 403)
 */
