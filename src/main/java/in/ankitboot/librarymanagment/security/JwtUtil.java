/*
Token generate/verify karne ka "engine" — pure logic, koi Spring Security dependency nahi
 */

package in.ankitboot.librarymanagment.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Production me ye .env / application.properties se aana chahiye, hardcode mat karna
    private static final String SECRET_KEY = "ThisIsADevSecretKeyForLibraryManagementSystemJWT123456";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Token generate karta hai username ke basis pe
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

/*
 * ===== JWT UTIL - REVISION NOTES =====
 *
 * ANALOGY: JWT = Office ka Gate Pass
 * - HR (server) pass banata hai, naam + validity likhi hoti hai
 * - Seal (signature) hoti hai jo fake pass rokti hai
 * - Guard sirf seal check karta hai, DB dobara query nahi karta
 *
 * -------------------------------------
 * 1) @Component
 *    -> Spring ko bolta hai object banao aur container me rakho
 *       taaki inject kiya ja sake, manual "new" na karna pade
 *
 * 2) SECRET_KEY
 *    -> Seal lagane/check karne ki chaabi
 *    -> PRODUCTION ME: hardcode NAHI, .env / application.properties se lo
 *
 * 3) EXPIRATION_TIME
 *    -> milliseconds me: 1000*60*60*10 = 10 hours
 *    -> pass itni der baad expire ho jaayega
 *
 * 4) getSigningKey()
 *    -> plain string key -> crypto-usable SecretKey object me convert
 *    -> Keys.hmacShaKeyFor() se karta hai
 *
 * -------------------------------------
 * 5) generateToken(userDetails)
 *    -> naya gate pass PRINT karta hai
 *    -> subject()     = kiske naam pass hai (username)
 *    -> issuedAt()    = kab bana
 *    -> expiration()  = kab tak valid
 *    -> signWith()    = HS256 algo se seal lagana
 *    -> compact()     = sab combine -> final JWT string
 *    -> LOGIN success hone par ye call hota hai
 *
 * -------------------------------------
 * 6) extractUsername(token)
 *    -> token se naam nikalta hai (Claims::getSubject)
 *
 * 7) extractExpiration(token)
 *    -> token se expiry date nikalta hai (Claims::getExpiration)
 *
 * 8) extractClaim(token, claimsResolver) -> GENERIC HELPER
 *    -> pehle poora claims nikalta hai
 *    -> fir passed lambda (Claims::getSubject / getExpiration) apply karta hai
 *    -> isse repeat code nahi likhna padta har field ke liye
 *
 * 9) extractAllClaims(token) -> ASLI PARSING + VERIFICATION
 *    -> Jwts.parser().verifyWith(key) = SEAL CHECK (tamper/fake detect)
 *    -> parseSignedClaims(token)      = decode karta hai
 *    -> getPayload()                  = andar ka data (claims) return
 *    -> agar signature match nahi -> EXCEPTION throw (fake pass reject)
 *
 * -------------------------------------
 * 10) isTokenExpired(token)
 *     -> expiry date ko AAJ ki date se compare karta hai
 *     -> expiry < now => true (expired)
 *
 * 11) validateToken(token, userDetails) -> FINAL GATEKEEPER
 *     -> CHECK 1: token ka username == logged-in user ka username
 *     -> CHECK 2: token expired nahi hai
 *     -> dono true tabhi -> valid, andar jaane do
 *
 * -------------------------------------
 * FULL FLOW (yaad rakhna):
 * LOGIN -> generateToken() -> user token ko Authorization header me bhejta hai
 *   -> har request pe validateToken() check karta hai:
 *      signature genuine? + naam match? + expire nahi hua?
 *   -> tabhi request process hoti hai
 *
 * NEXT STEP: JwtAuthFilter (OncePerRequestFilter) banega jo
 * ye validateToken() ko har incoming request pe use karega
 */
