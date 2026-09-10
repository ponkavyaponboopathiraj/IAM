package IAM.IAM.service;

import IAM.IAM.entity.Role;
import IAM.IAM.entity.User;
import IAM.IAM.repository.RoleRepository;
import IAM.IAM.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        // Check username
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check password
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Get default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() ->
                        new RuntimeException("USER role not found"));

        // Make sure roles collection is initialized
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Assign USER role
        user.getRoles().add(userRole);

        // Save user
        return userRepository.save(user);
    }
}