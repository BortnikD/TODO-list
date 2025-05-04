package com.bortnik.todo.infrastructure.security

import com.bortnik.todo.domain.exceptions.auth.AuthenticationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthorizationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val jwt = authorizationHeader.substring(7)
            try {
                val username = jwtUtil.extractUsername(jwt)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        val authenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authenticationToken
                    }
                }
            }
            catch(e: Exception) {
                throw AuthenticationException("Authentication error", e)
            }
        }

        filterChain.doFilter(request, response)
    }
}