package com.project.gengshop.config;

import com.project.gengshop.service.CustomUserDetailsService;
import com.project.gengshop.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, //object request http yang masuk
            HttpServletResponse response,  // object response http yang dihantar balik
            FilterChain filterChain      // rantaian filter yang lain
    ) throws ServletException, IOException {

        //1. dapatkan authorization header dari request
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail;

        //2. check kalau header ada bermula dengan "bearer" dalam token
        if (authorizationHeader == null || authorizationHeader.startsWith("Bearer ")) {
            //kalau takde, abaikan filter ni & teruskan ke filter lain
            filterChain.doFilter(request, response);
            return;
        }

        //3. extract token (buang "bearer")
        jwt = authorizationHeader.substring(7);
        userEmail = null;

        //4. extract email dari token guna jwtservice
        try {
            userEmail = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            // Kalau token dah expired, format salah, atau signaturnya tak valid:
            // Biarkan request tu teruskan (mungkin akan dapat 403 Forbidden nanti)
            filterChain.doFilter(request, response);
            return;
        }

        //5.kalau user ada dan user belum disahkan lagi dalam current request
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. Kalau belum sah, load UserDetails penuh dari database guna email tu
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userEmail);

            // 7. Check sekali lagi: Adakah token tu sendiri masih valid? (expiration date, etc)
            if(jwtService.isTokenValid(jwt, userDetails)) {
                // 8. Kalau valid, cipta objek 'authentication token' standard Spring
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getAuthorities()
                );

                // 9. Tambah detail request web (IP Address, session ID, etc.) ke token tu
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. INI BARIS PALING PENTING: Set authentication dalam Security Context.
                // Sekarang, seluruh aplikasi Spring tahu SIAPA user ni dan APA ROLES dia.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Sambung balik rantaian filter Spring yang asal.
        // Kali ni, request dah ada maklumat authentication yang lengkap.
        filterChain.doFilter(request,response);

    }
}
