package fr.quatorze.pcd.codingweekquinze.service;

import com.calendarfx.model.Calendar;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.model.calendar.ElementEntry;
import fr.quatorze.pcd.codingweekquinze.model.calendar.LoanEntry;
import lombok.Getter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Getter
public class AuthService {

    private static AuthService instance;

    private final Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    private User currentUser;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public User getCurrentUser() {
        UserDAO.getInstance().refresh(currentUser);
        return currentUser;
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

        if (user == null) {
            return false;
        }

        if (!encoder.matches(enteredPassword, user.getPassword())) {
            return false;
        }
        currentUser = user;

        // Setup calendars, users should not be able to modify them manually
        Calendar<?> c1 = new Calendar<>("Mes emprunts");
        Calendar<?> c2 = new Calendar<>("Mes prêts et services");
        c1.setStyle(Calendar.Style.STYLE1);
        c1.setReadOnly(true);
        c2.setStyle(Calendar.Style.STYLE2);
        c2.setReadOnly(true);

        for (Loan borrowedLoan : user.getBorrowedLoans()) {
            c1.addEntry(new LoanEntry(borrowedLoan));
        }
        for (Element element : user.getOwnedElements()) {
            for (Availability availability : element.getAvailabilities()) {
                c2.addEntry(new ElementEntry(element, availability).createRecurrence());
            }
        }

        user.setLoansCalendar(c1);
        user.setMyElementsCalendar(c2);

        return true;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
