package github.com.rexfilius.tea.modules.auth.controller;

import github.com.rexfilius.tea.modules.auth.model.LoginDto;
import github.com.rexfilius.tea.modules.auth.model.RegisterDto;
import github.com.rexfilius.tea.modules.auth.model.VerifyUserDto;
import github.com.rexfilius.tea.modules.auth.service.AuthService;
import github.com.rexfilius.tea.modules.security.jwt.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(token);
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Register an account")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verify an account")
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyUserDto verifyUserDto) {
        String verifyResponse = authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok(verifyResponse);
    }

    @Operation(summary = "Resend code to verify account")
    @PostMapping("/resend/{email}")
    public ResponseEntity<String> resendCode(@PathVariable String email) {
        String resendResponse = authService.resendVerificationCode(email);
        return ResponseEntity.ok(resendResponse);
    }


}
