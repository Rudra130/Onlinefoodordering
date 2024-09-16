package com.food.OnlineFoodOrdering.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, 
               @SuppressWarnings("null") HttpServletResponse response, 
               @SuppressWarnings("null") FilterChain filterChain)
                  throws ServletException, IOException {


                    String jwt=request.getHeader(JwtConstant.JWT_HEADER);
                    if(jwt!=null){
                        jwt=jwt.substring(7);
                        try{
                            SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                            String email = String.valueOf(claims.get("email"));
                            String authorities = String.valueOf(claims.get("authorities"));

                             

                            List<GrantedAuthority>  auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                           Authentication  authentication = new UsernamePasswordAuthenticationToken(email,null, auth);
                            SecurityContextHolder.getContext().setAuthentication(authentication);


                        }catch(ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | WeakKeyException | IllegalArgumentException e){
                            throw new BadCredentialsException("Invalid token........");
                        }
                    }
                    filterChain.doFilter(request, response);

     
       
    }

}
/* 


public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
               HttpServletResponse response, 
               FilterChain filterChain)
                  throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Log the exception message and stack trace for debugging
                e.printStackTrace();
                throw new BadCredentialsException("Invalid token: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
    */

