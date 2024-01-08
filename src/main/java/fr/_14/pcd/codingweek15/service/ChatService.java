package fr._14.pcd.codingweek15.service;

import fr._14.pcd.codingweek15.dao.MessageDAO;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.Message;
import fr._14.pcd.codingweek15.model.User;

import java.util.List;

public final class ChatService {

    public void sendMessage(User sender, User receiver, Loan loan, String message) {
        Message chat = new Message();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setLoan(loan);
        chat.setContent(message);

        MessageDAO.getInstance().saveMessages(chat);
    }

    public List<Message> getChats(User sender, User receiver, int fromMessage, int messageCount) {
        return List.of();
    }

    public List<Message> getChats(User sender, User receiver) {
        return getChats(sender, receiver, 0, 20);
    }
}
