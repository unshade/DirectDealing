package fr.quatorze.pcd.codingweekquinze.database.seeder;

import fr.quatorze.pcd.codingweekquinze.dao.NotificationDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;

import java.util.List;

public class NotificationSeeder {

    public void seed() {

        NotificationDAO notificationDAO = NotificationDAO.getInstance();


        List<User> users = UserDAO.getInstance().getAll();
        for (User user : users) {
            notificationDAO.createNotification(user, "Hello 1 " + user.getFirstName() + " " + user.getLastName() + "!");
        }
        for (User user : users) {
            notificationDAO.createNotification(user, "Hi 2 " + user.getFirstName() + " " + user.getLastName() + "!");
        }
    }
}