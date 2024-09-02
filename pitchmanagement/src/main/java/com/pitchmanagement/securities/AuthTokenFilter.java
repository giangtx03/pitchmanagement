package com.pitchmanagement.securities;

import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException
    {
        try{
            if(request.getServletPath().contains("public")){
                filterChain.doFilter(request,response);
                return;
            }

            String token = parseJwt(request);

            if(token == null || !jwtUtil.IsValidToken(token)){
                logger.error("Token invalid!!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String username = jwtUtil.extractUsername(token);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");
        }

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AuthConstant.TOKEN_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AuthConstant.TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
