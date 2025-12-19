package com.project.gengshop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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
    public String extractClaim(String token) {
        return extractClaim(token, Claims::getSubject);
    }




}
