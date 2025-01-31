package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }


    public Message addMessage(Message message) {
        if (message.getMessage_text().isBlank()) {
            return null;
        }

        if (message.getMessage_text().length() > 225) {
            return null;
        }

        if (accountDAO.getAccountByID(message.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(Integer message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    public Message deleteMessage(Integer message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public Message updateMessage(Integer message_id, String message_text) {
        if (message_text.isBlank()) {
            return null;
        }

        if (message_text.length() > 255) {
            return null;
        }

        if (messageDAO.getMessageByID(message_id) == null) {
            return null;
        }

        messageDAO.updateMessage(message_id, message_text);
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getAllMessageByAccountID(int account_id) {
        return messageDAO.getAllMessageByAccountID(account_id);
    }
}
