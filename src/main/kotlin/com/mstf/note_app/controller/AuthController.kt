package com.mstf.note_app.controller

import com.mstf.note_app.security.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    data class AuthRequest(
        // email validation
        @field: Email(message = "Invalid email format.")
        val email: String,
        // password validation pattern
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$",
            message = "Password must be at least 9 characters long and contain aat least one digit, uppercase and lowercase character.",
        )
        val password: String,
    )

    data class RefreshRequest(
        val refreshToken: String,
    )

    @PostMapping("/register")
    fun register(
        // we must mention @Valid for any of the method's param that we need spring boot to validate
        @Valid @RequestBody authRequest: AuthRequest,
    ) {
        authService.register(authRequest.email, authRequest.password)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody authRequest: AuthRequest,
    ): AuthService.TokenPair {
        return authService.login(authRequest.email, authRequest.password)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody refreshRequest: RefreshRequest,
    ): AuthService.TokenPair {
        return authService.refresh(refreshRequest.refreshToken)
    }
}