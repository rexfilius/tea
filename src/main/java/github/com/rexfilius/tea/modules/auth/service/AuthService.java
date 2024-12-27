package github.com.rexfilius.tea.modules.auth.service;

import github.com.rexfilius.tea.exception.TeaApiException;
import github.com.rexfilius.tea.modules.auth.model.LoginDto;
import github.com.rexfilius.tea.modules.auth.model.RegisterDto;
import github.com.rexfilius.tea.modules.security.jwt.JwtTokenProvider;
import github.com.rexfilius.tea.modules.security.model.Role;
import github.com.rexfilius.tea.modules.security.model.User;
import github.com.rexfilius.tea.modules.security.repository.RoleRepository;
import github.com.rexfilius.tea.modules.security.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        System.out.println("isAdmin from DTO: " + registerDto.isAdmin());
        user.setAdmin(registerDto.isAdmin());
        System.out.println("user: " + user);

        Role role;
        if (user.isAdmin()) {
            role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new TeaApiException(HttpStatus.NOT_FOUND, "Admin role not found"));
            System.out.println("role from db: suppose admin role: " + role);
        } else {
            role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new TeaApiException(HttpStatus.NOT_FOUND, "User role not found"));
            System.out.println("role from db: suppose user role: " + role);
        }
        user.setRole(role);
        System.out.println("user after setting role: " + user);

        userRepository.save(user);
        return "User registered successfully";
    }
}
