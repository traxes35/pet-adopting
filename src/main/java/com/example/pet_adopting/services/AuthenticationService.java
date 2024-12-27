    package com.example.pet_adopting.services;

    import com.example.pet_adopting.entities.User;
    import com.example.pet_adopting.repos.UserRepository;
    import com.example.pet_adopting.requests.LoginUserRequest;
    import com.example.pet_adopting.requests.RegisterUserRequest;
    import com.example.pet_adopting.requests.VerifyUserRequest;
    import jakarta.mail.MessagingException;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.Optional;
    import java.util.Random;

    @Service
    public class AuthenticationService {
        private final UserRepository userRepository;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;

        public AuthenticationService(
                UserRepository userRepository,
                UserService userService,
                AuthenticationManager authenticationManager,
                PasswordEncoder passwordEncoder,
                EmailService emailService
        ) {
            this.authenticationManager = authenticationManager;
            this.userRepository = userRepository;
            this.userService = userService;
            this.passwordEncoder = passwordEncoder;
            this.emailService = emailService;
        }

        public User signup(RegisterUserRequest input) {
            User user = new User(input.getName(),input.getSurname(),input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiryDate(LocalDateTime.now().plusMinutes(15));
            user.setActive(false);
            userService.saveOneUser(user);
          //  sendVerificationEmail(user);
            userService.assignRoleToUser(user.getId(), "ROLE_USER");
            return user;
        }

        public User authenticate(LoginUserRequest input) {
            User user = userRepository.findByUsername(input.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.isActive()) {
                throw new RuntimeException("Account not verified. Please verify your account.");
            }

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
                );

            return user;
        }

        public void verifyUser(VerifyUserRequest input) {
            Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getVerificationExpiryDate().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("Verification code has expired");
                }
                if (user.getVerificationCode().equals(input.getVerificationCode())) {
                    user.setActive(true);
                    user.setVerificationCode(null);
                    user.setVerificationExpiryDate(null);
                    userRepository.save(user);
                } else {
                    throw new RuntimeException("Invalid verification code");
                }
            } else {
                throw new RuntimeException("User not found");
            }
        }

        public void resendVerificationCode(String email) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.isEnabled()) {
                    throw new RuntimeException("Account is already verified");
                }
                user.setVerificationCode(generateVerificationCode());
                user.setVerificationExpiryDate(LocalDateTime.now().plusHours(1));
                sendVerificationEmail(user);
                userRepository.save(user);
            } else {
                throw new RuntimeException("User not found");
            }
        }

        private void sendVerificationEmail(User user) { //TODO: Update with company logo
            String subject = "Account Verification";
            String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
            String htmlMessage = "<html>"
                    + "<body style=\"font-family: Arial, sans-serif;\">"
                    + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                    + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
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
                // Handle email sending exception
                e.printStackTrace();
            }
        }
        private String generateVerificationCode() {
            Random random = new Random();
            int code = random.nextInt(900000) + 100000;
            return String.valueOf(code);
        }
    }
