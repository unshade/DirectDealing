package fr.quatorze.pcd.codingweekquinze.service;

import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.User;
import lombok.Getter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Getter
public class AuthService {

    private static AuthService instance;

    private final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    private User currentUser;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public static String hashPassword(String password) {
        return getInstance().encoder.encode(password);
    }

    public void endSession() {
        currentUser = null;
    }

    public boolean authenticate(String email, String enteredPassword) {
        if (currentUser != null) {
            return true;
        }
        User user = UserDAO.getInstance().getUserByEmail(email);
        currentUser = user;

        return encoder.matches(enteredPassword, user.getPassword());
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
