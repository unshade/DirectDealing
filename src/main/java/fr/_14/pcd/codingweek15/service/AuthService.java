package fr._14.pcd.codingweek15.service;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.model.User;
import org.mindrot.jbcrypt.BCrypt;

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
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean authenticate(User user, String enteredPassword) {
        String storedHash = getStoredPasswordHash(user.getEmail());

        if (storedHash != null && BCrypt.checkpw(enteredPassword, storedHash)) {

            return true;
        }
        return false;
    }

    private String getStoredPasswordHash(String email) {
        User user = UserDAO.getInstance().getUserByEmail(email);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }
}
