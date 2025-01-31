package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // Create user (/register) POST
        app.post("/register", this::registerUserHandler);

        // Verify User (/login) POST
        app.post("/login", this::loginHandler);

        // Create message (/messages) POST
        app.post("/messages", this::createMessageHandler);

        // Get all messages (/messages) GET
        app.get("/messages", this::getAllMessagesHandler);

        // Get message by id (/messages/{message_id}) GET
        app.get("/messages/{message_id}", this::getMessageByIDHandler);

        // Delete message by id (/messages/{message_id})
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);

        // Update message text by id (/messages/{message_id})
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // Get all messages from user by id (/accounts/{account_id}/messages)
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountIDHandler);

        return app;
    }

    public void registerUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);    // client error
        }
    }

    public void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account author = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.getAccount(author.getUsername(), author.getPassword());
        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(401); // unauthorized
        }
    }

    public void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);  // request does not contain message id (only other message fields)
        Message addedMessage = messageService.addMessage(message);  // returns message with id now
        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
        } else {
            context.status(400);
        }
    }

    public void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        context.json(mapper.writeValueAsString(messages));
    }

    public void getMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.json("");
        }
    }

    public void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.json("");
        }
    }

    public void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // {"message_text": "updated message" }    
        Message message = mapper.readValue(context.body(), Message.class);  // body contains only message_text?
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message.getMessage_text());
        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
        } else {
            context.status(400);
        }
    }

    public void getMessageByAccountIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessageByAccountID(account_id);
        context.json(mapper.writeValueAsString(messages));
    }
}