package IAM.IAM.service;

import IAM.IAM.entity.Role;
import IAM.IAM.entity.User;
import IAM.IAM.repository.RoleRepository;
import IAM.IAM.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() ->
                        new RuntimeException("USER role not found"));

        user.getRoles().add(userRole);

        return userRepository.save(user);
    }
}