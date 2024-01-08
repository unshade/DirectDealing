package fr._14.pcd.codingweek15.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import fr._14.pcd.codingweek15.model.User;

public class UserController {

  private Dao<User, Integer> userDao;

  public UserController(String databaseUrl) throws Exception {
    ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
    userDao = DaoManager.createDao(connectionSource, User.class);
  }

  public void createUser(String name) throws Exception {
    User user = new User();
    user.setName(name);
    userDao.create(user);
  }

  // Autres méthodes pour lire, mettre à jour, supprimer des utilisateurs
}

