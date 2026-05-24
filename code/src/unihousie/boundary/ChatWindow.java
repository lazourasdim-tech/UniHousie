package unihousie.boundary;

import unihousie.entity.Message;

import javax.swing.*;

public class ChatWindow extends JFrame {

    public ChatWindow() {
        super("UC05 — Chat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 640);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void typeAndSendMessage(String matchId, String messageText) {

    }

    public void appendMessageToSenderScreen(Message message) {

    }

    public void updateChatUI() {

    }
}
