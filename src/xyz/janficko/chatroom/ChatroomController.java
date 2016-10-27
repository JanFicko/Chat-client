package xyz.janficko.chatroom;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class that handles client calls.
 */
public class ChatroomController implements Initializable {

    private ChatClient chatClient = new ChatClient();

    @FXML
    TextField nickname = new TextField();
    @FXML
    TextField message = new TextField();
    @FXML
    ScrollPane scrollPane = new ScrollPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshChatBox();
    }

    public void refreshChatBox() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> chatBox()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void sendMessage(){
        if (!nickname.getText().isEmpty() && !message.getText().isEmpty()){
            chatClient.postChatMessage(nickname.getText(), message.getText());
        }
        message.clear();
    }

    public void chatBox(){
        ArrayList<String> messages = chatClient.getChatMessages();
        Text text = new Text();
        int numberOfMessages = messages.size();
        for(int i = (numberOfMessages - 1); i >= 0; i--){
            text.setText(text.getText() + "\n" + messages.get(i));
        }
        scrollPane.setContent(text);
        scrollPane.setVvalue(1.0);
    }
}
