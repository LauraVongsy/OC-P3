package com.chatop.filter;

import com.chatop.services.UserService;
import com.chatop.utils.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Checks every incoming request to make sure the user has a valid login token and sets up their login status if everything is ok
 * Examines each incoming HTTP request for a JWT in the Authorization header.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Used to fetch the UserService bean from ApplicationContext
     * Done to avoid circular dependency issues
     */
    private UserService getUserService() {
        return applicationContext.getBean(UserService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Extract token from request header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
        }

        //If username is extracted from token and there is no authentication in the current SecurityContext
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Loading UserDetails by username extracted from the token
            UserDetails userDetails = getUserService().loadUserByUsername(userName);

            //Validates the token with loaded UserDetails
            if (jwtService.validateToken(token, userDetails)) {
                //Creates an authentication token using UserDetails
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //Setting authentication details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Setting the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        //Proceeding with the filter chain
        filterChain.doFilter(request, response);

    }
}
