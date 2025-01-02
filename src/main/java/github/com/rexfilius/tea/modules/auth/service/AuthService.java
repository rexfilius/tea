package github.com.rexfilius.tea.modules.auth.service;

import github.com.rexfilius.tea.exception.TeaApiException;
import github.com.rexfilius.tea.modules.auth.model.LoginDto;
import github.com.rexfilius.tea.modules.auth.model.RegisterDto;
import github.com.rexfilius.tea.modules.auth.model.VerifyUserDto;
import github.com.rexfilius.tea.modules.security.jwt.JwtTokenProvider;
import github.com.rexfilius.tea.modules.security.model.Role;
import github.com.rexfilius.tea.modules.security.model.User;
import github.com.rexfilius.tea.modules.security.repository.RoleRepository;
import github.com.rexfilius.tea.modules.security.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public AuthService(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtTokenProvider jwtTokenProvider,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    public String login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }
        // create user
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAdmin(registerDto.isAdmin());

        // assign role to user
        Role role;
        if (user.isAdmin()) {
            role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new TeaApiException(HttpStatus.NOT_FOUND, "Admin role not found"));
        } else {
            role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new TeaApiException(HttpStatus.NOT_FOUND, "User role not found"));
        }
        user.setRole(role);

        // send verification code to user
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(30));
        user.setEnabled(false);
        sendVerificationEmail(user);

        userRepository.save(user);
        return "User registered successfully";
    }

    public String verifyUser(VerifyUserDto input) {
        if (!userRepository.existsByEmail(input.getEmail())) {
            throw new TeaApiException(HttpStatus.NOT_FOUND, "This email does not exist");
        }
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new TeaApiException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Verification code expired");
        }

        if (user.getVerificationCode().equals(input.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);
        } else {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "Verification code is invalid");
        }
        return "User has been verified successfully";
    }

    public String resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new
                        TeaApiException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
        if (user.isEnabled()) {
            throw new TeaApiException(HttpStatus.BAD_REQUEST, "User is already verified");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(5));
        sendVerificationEmail(user);
        userRepository.save(user);
        return "Verification code has been resent";
    }

    private void sendVerificationEmail(User user) {
        String subject = "Tea App - Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Verify Your Account</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new TeaApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send verification email");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
