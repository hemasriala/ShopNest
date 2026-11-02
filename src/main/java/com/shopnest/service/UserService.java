package com.shopnest.service;

import com.shopnest.model.User;
import com.shopnest.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase()).isPresent();
    }

    public void register(String name, String email, String phone, String password) {
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPhone(phone);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    /** Returns the user if email + password are correct, otherwise null. */
    public User login(String email, String password) {
        Optional<User> found = userRepository.findByEmail(email.trim().toLowerCase());
        if (found.isPresent() && encoder.matches(password, found.get().getPassword())) {
            return found.get();
        }
        return null;
    }

    public boolean passwordMatches(User user, String rawPassword) {
        return encoder.matches(rawPassword, user.getPassword());
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    /** Gets the logged-in user from the session (null if not logged in). */
    public User currentUser(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }
}
