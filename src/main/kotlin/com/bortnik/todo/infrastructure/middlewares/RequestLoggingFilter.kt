package com.bortnik.todo.infrastructure.middlewares

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RequestLoggingFilter: Filter {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val startTime = System.currentTimeMillis()

        chain.doFilter(request, response)

        val duration = System.currentTimeMillis() - startTime

        logger.info("Request: ${httpRequest.method} ${httpRequest.requestURI} | " +
                    "Status: ${httpResponse.status} | Time ${duration}ms")
    }
}