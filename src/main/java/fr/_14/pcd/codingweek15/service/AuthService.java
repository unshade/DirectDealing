package fr._14.pcd.codingweek15.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.model.User;

public class AuthService {
    private static AuthService instance;

    private User currentUser;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void endSession() {
        currentUser = null;
    }

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean authenticate(User user, String enteredPassword) {
        String storedHash = getStoredPasswordHash(user.getEmail());

        return storedHash != null && BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHash).verified;
    }

    private String getStoredPasswordHash(String email) {
        User user = UserDAO.getInstance().getUserByEmail(email);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }
}
