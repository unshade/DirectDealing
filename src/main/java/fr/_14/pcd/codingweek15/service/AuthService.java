package fr._14.pcd.codingweek15.service;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.model.User;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Getter
public class AuthService {
    private static AuthService instance;

    private User currentUser;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public static String hashPassword(String password) {
        return sha256(password);
    }

    public static String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void endSession() {
        currentUser = null;
    }

    public boolean authenticate(String email, String enteredPassword) {
        if (currentUser != null) {
            return true;
        }
        User user = UserDAO.getInstance().getUserByEmail(email);
        System.out.println("user email: " + user.getEmail());
        currentUser = user;
        return user.getPassword().equals(sha256(enteredPassword));
    }

}
