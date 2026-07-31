package in.ankitboot.librarymanagment.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
/*
 * ===== GLOBALEXCEPTIONHANDLER - REVISION NOTES =====
 *
 * ANALOGY: Poore application ka CENTRAL COMPLAINT DEPARTMENT
 * Chahe error kisi bhi Controller (Login counter, Book counter, Member
 * counter) me ho, ye class use catch karke ek CONSISTENT, PROFESSIONAL
 * JSON response deti hai — bina har Controller me alag try-catch likhe.
 *
 * -------------------------------------
 * @RestControllerAdvice
 *    -> Spring ko batata hai: "poore application ke SAARE Controllers
 *       ke exceptions yaha globally handle karo"
 *    -> Bina isके: har Controller me manually try-catch likhna padta
 *       (repetitive, messy code)
 *    -> Isके saath: ek hi jagah se saara error-handling manage hota hai
 *
 * @ExceptionHandler(SomeException.class)
 *    -> jab bhi application me kahi bhi "SomeException" throw hoga,
 *       Spring AUTOMATICALLY is method ko call karega
 *    -> tumhe manually catch karne ki zarurat nahi kisi Controller me
 *
 * =======================================================
 * HANDLER 1: ResourceNotFoundException -> 404 NOT FOUND
 * =======================================================
 * KAB TRIGGER HOTA HAI: jab koi ID DB me nahi milta
 *    (jaise "Book not found", "Member not found")
 *
 * ex.getMessage() -> jo custom message exception throw karte waqt diya
 *    throw new ResourceNotFoundException("Book not found with id: 5")
 *
 * ANALOGY: Library me non-existent Book ID poochne pe "ye book exist
 * hi nahi karti" jawab milta hai.
 *
 * =======================================================
 * HANDLER 2: RuntimeException -> 400 BAD REQUEST
 * =======================================================
 * KAB TRIGGER HOTA HAI: manually throw kiye generic RuntimeExceptions
 *    (jaise AuthService me "Username already taken")
 *
 * STATUS 400 KYU: client ki galti hai (invalid/duplicate data bheja),
 *    server ki nahi
 *
 * =======================================================
 * HANDLER 3: AccessDeniedException -> 403 FORBIDDEN
 * =======================================================
 * KAB TRIGGER HOTA HAI: role/permission na hone par
 *    (jaise MEMBER role wala ADMIN-only endpoint hit kare)
 *
 * MESSAGE HARDCODED KYU: Spring Security ka default message
 *    technical/confusing hota hai -> isliye clean custom message diya
 *
 * 401 vs 403 - YAAD RAKHNE KA TAREEKA:
 *    401 Unauthorized -> "TUM KAUN HO PATA HI NAHI" (login/token invalid)
 *    403 Forbidden     -> "PATA HAI TUM KAUN HO, LEKIN IJAZAT NAHI"
 *
 * =======================================================
 * HANDLER 4: MethodArgumentNotValidException -> 400 (Validation errors)
 * =======================================================
 * KAB TRIGGER HOTA HAI: @Valid DTO validation fail hone par
 *    (jaise @NotBlank, @Email constraints)
 *
 * CODE FLOW:
 *    Map<String, String> errors = new HashMap<>();
 *    -> field-naam ko KEY, error-message ko VALUE banake store karta hai
 *
 *    ex.getBindingResult().getFieldErrors()
 *    -> saare FAILED fields ki list nikalta hai (multiple ho sakte hain)
 *
 *    for (FieldError fieldError : ...) {
 *        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
 *    }
 *    -> loop chala ke har field ka naam + uska error message map me daala
 *
 * OUTPUT EXAMPLE:
 *    {
 *        "username": "Username is required",
 *        "password": "Password is required"
 *    }
 *
 * YE ErrorResponse RETURN NAHI KARTA (Map<String,String> return karta hai)
 *    KYU: multiple field-errors ek saath dikhane hain, jo ErrorResponse
 *    ka single-message structure handle nahi kar sakta
 *
 * =======================================================
 * HANDLER 5: Exception (Generic) -> 500 INTERNAL SERVER ERROR
 * =======================================================
 * KAB TRIGGER HOTA HAI: koi bhi UNEXPECTED exception jo upar ke kisi
 *    specific handler se match nahi hui (NullPointerException,
 *    SQLException, koi bhi random crash)
 *
 * KYU ZAROORI HAI: SAFETY NET — bina isके, client ko RAW STACK TRACE
 *    dikh sakta tha (security risk — internal code details leak hote)
 *
 * ANALOGY: General helpdesk — koi specific department na handle kar
 *    paye to "kuch gadbad hui hai, hum dekh rahe hain" jawab milta hai,
 *    kabhi khaali/crash response nahi jaata.
 *
 * -------------------------------------
 * PRIORITY ORDER — Spring kaise decide karta hai kaunsa handler chale:
 *
 *    ResourceNotFoundException        -> SABSE SPECIFIC (pehle check)
 *    RuntimeException                  -> thoda generic
 *    AccessDeniedException             -> specific
 *    MethodArgumentNotValidException   -> specific
 *    Exception                         -> SABSE GENERIC (fallback, aakhri)
 *
 * Spring hamesha MOST SPECIFIC match wala handler pehle try karta hai.
 *
 * -------------------------------------
 * FULL FLOW EXAMPLE:
 *
 * User duplicate username se register karta hai
 *        |
 * AuthService.register() throws: RuntimeException("Username already taken")
 *        |
 * GlobalExceptionHandler catch karta hai (handleRuntimeException)
 *        |
 * Client ko clean JSON milta hai:
 * {
 *     "status": 400,
 *     "message": "Username already taken: ankit",
 *     "timestamp": "2026-07-28T10:30:00"
 * }
 *
 * -------------------------------------
 * DEPENDENCY CLASSES (inka bhi code dekhna zaroori hai):
 *    - ErrorResponse       -> custom DTO (status, message, timestamp fields)
 *    - ResourceNotFoundException -> custom exception class jo RuntimeException
 *                                   extend karti hai
 */