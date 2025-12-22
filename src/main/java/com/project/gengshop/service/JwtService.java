package com.project.gengshop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //JwtService ni ibarat "kilang" untuk generate dan validate token

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    // method utk dptkan signing key dari secret key
    // method ni convert jadi Key supaya JJWT boleh guna sebagai cop rahsia
    private SecretKey getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // method untuk extract allClaims dari token
    // method yang pecahkan token dan baca isi dia
    public Claims extractAllClaims (String token) {
        return Jwts
                .parser() // Macam buka mesin scanner untuk baca dokumen
                .verifyWith(getSignInKey()) // bagi mesin tu "cop mohor" untuk compare - kalau token tu dicipta dengan kunci lain, mesin reject
                .build() // Lock settings, siap sedia nak proses
                .parseSignedClaims(token) // Masukkan token string tu untuk diproses, engine akan decrypt dan validate
                .getPayload(); // Keluarkan "isi surat" dari token - semua Claims (data) yang ada dalam tu
    }

    //method untuk extractClaim dari token (ambil maklumat tertentu je)
    // Generic method <T> - boleh return any type (String, Date, List, etc)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Panggil method extractAllClaims untuk dapatkan SEMUA data dalam token
        final Claims claims = extractAllClaims(token);
        // claimsResolver ni macam "instruction" nak ambil data mana
        return claimsResolver.apply(claims);
    }

    //method untuk extract subject(email) dari token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //method untuk extract expiration date dari token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //method untuk check sama ada token dah expired ke belum
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //method untuk generate token with extra claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    //method untuk generate token without extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
        // Panggil method generateToken yang full version
        // Pass empty HashMap sebab takde extra claims nak tambah
        // Cuma nak username je dalam token
    }

    //method untuk validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email =extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
