/*
Jackson (jo Spring internally use karta hai JSON banane ke liye) kabhi bhi private
 field ko directly nahi chhuta.
 Wo sirf ye dekhta hai: "is class mein get___() naam ka koi public method hai kya?"
  Agar hai, to wo use call karke JSON mein daal deta hai.

Matlab:

private sirf batata hai — "field ko seedha user.password likh ke access nahi kar sakta"
Lekin getPassword() public hai
(kyunki Lombok @Getter ya manual getter hamesha public banata hai)
 — aur Jackson isी public getter ko call karta hai
Ek Line Mein Samajh

private sirf ye rokta hai ki tu directly user.password likh ke access kare.
Lekin JSON banane wala tool (Jackson) getter ke through access karta hai — aur getter hamesha public hota hai. Isliye private field bhi JSON mein pahुँच jaati hai.

Ab DTO Kya Karta Hai (Solution)

DTO mein tu bas wo field hi nahi banata jo bhejni nahi hai:
Ab Jackson jitni bhi getters call kare, password naam ka koi getter hi nahi milega
 — kyunki DTO mein wo field hi nahi hai. Isliye leak hona physically possible hi nahi hai.

Real-Life Analogy (Final)

Tera User Entity ek almirah hai jisme sab kuch hai
 — kapde, gehna, cash, sab. private ka matlab hai "koi bahar se seedha haath daal ke nahi nikaal sakta."
  Lekin agar tu almirah ki chaabi (getter) kisi ko de de, wo sab kuch nikaal sakta hai.

DTO matlab tu almirah nahi bhejta — tu ek chhota sa purse (naya object) banata hai
 jisme sirf wahi cheezein rakhta hai jo dikhani hain.
 Purse mein cash rakha hi nahi, to koi nikaal bhi nahi sakta — chahe purse ki chaabi kisi ko bhi de do.
 */
