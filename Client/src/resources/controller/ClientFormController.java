package resources.controller;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {
    public static String username;
    public TextField messageTextField;
    public ScrollPane sp_main;
    public Label usernameLabel;
    public VBox vb_main;
    public AnchorPane clientContext;
    public ImageView emojiIcon;
    public ScrollPane sp_emoji;
    public GridPane gp_emoji;
    public ImageView imgPhotos;
    public FlowPane flowPane;
    public ImageView imgMenu;
    public Circle showProPic;
    public AnchorPane profilePane;
    private Client client;
    PrintWriter printWriter;
    public boolean toggleChat = false, toggleProfile = false;
    int[] emojis = {0x1F606, 0x1F601, 0x1F602, 0x1F609, 0x1F618, 0x1F610, 0x1F914, 0x1F642, 0x1F635, 0x1F696, 0x1F636, 0x1F980, 0x1F625, 0x1F634, 0x1F641,0x1F643,};

    public void sendOnAction(ActionEvent actionEvent) {
        sp_emoji.setVisible(false);
        String message = messageTextField.getText();

        if(!message.isEmpty()){
            sendMessage(message);
            messageTextField.clear();
            client.clientSendMessage(message);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEmojisToPane();

        sp_emoji.setVisible(false);
        sp_emoji.setStyle("-fx-background-color: coral");

        if (username != null){
            usernameLabel.setText(username);
        }else {
            System.err.println("Your Username Is Null");
        }
        try {
            client = new Client(new Socket("localhost", 4100),username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientContext.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });
        client.listenForMessage(vb_main);
        client.clientSendMessage("");
    }

    public static void receiveMessage(String message, VBox vBox){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    private void sendMessage(String message) {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #27ae60;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vb_main.getChildren().add(hBox);
    }

    public void emojiOnClick(MouseEvent mouseEvent) {
        if (sp_emoji.isVisible()){
            sp_emoji.setVisible(false);
        }else {
            sp_emoji.setVisible(true);
            Text text = new Text(new String(Character.toChars(emojis[4])));
            text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Noto Emoji';");
            gp_emoji.add(text, 0, 0);
        }
    }

    private void setEmojisToPane() {
        int EMOJI_INDEX = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                Label text = new Label(new String(Character.toChars(emojis[EMOJI_INDEX++])));
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        messageTextField.appendText(text.getText());
                    }
                });
                text.setStyle("-fx-font-size: 25px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gp_emoji.add(text, i, j);
            }
        }
    }

    public void openImageChooser(MouseEvent mouseEvent) throws IOException {

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Image");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
//            printWriter.println(userName + ": " + file.toURI().toURL());
        }
        if (file != null) {
            System.out.println("File Was Selected");
            URL url = file.toURI().toURL();
            System.out.println(url);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 10, 5, 5));
            ImageView imageView = new ImageView();
            Image image = new Image(String.valueOf(url));
            imageView.setImage(image);
            imageView.setFitWidth(75);
            imageView.setFitHeight(75);
            VBox vBox = new VBox(imageView);
            vBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.setPadding(new Insets(5, 10, 5, 5));
            vb_main.getChildren().add(vBox);
        }
      /*  FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image","*.jpg;*.png;*.jpeg;*.gif;"));
        fileChooser.setTitle("Select image to send.");
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println(file.getParent());
        BufferedImage bufferedImage = ImageIO.read(new File(file.getPath()));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String fileName = file.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        ImageIO.write(bufferedImage,extension,bos);
        byte[] data = bos.toByteArray();
        client.clientSendImage(data,extension,file.getName());*/

    }

//    public void sentImageOnMouseClicked(MouseEvent mouseEvent) throws MalformedURLException {
//        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Choose a Image");
//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            printWriter.println(username + ": " + file.toURI().toURL());
//        }
//        if (file != null) {
//            System.out.println("File Was Selected");
//            URL url = file.toURI().toURL();
//            System.out.println(url);
//            HBox hBox = new HBox();
//            hBox.setAlignment(Pos.CENTER_RIGHT);
//            hBox.setPadding(new Insets(5, 10, 5, 5));
//            ImageView imageView = new ImageView();
//            Image image = new Image(String.valueOf(url));
//            imageView.setImage(image);
//            imageView.setFitWidth(75);
//            imageView.setFitHeight(75);
//            VBox vBox = new VBox(imageView);
//            vBox.setAlignment(Pos.CENTER_RIGHT);
//            vBox.setPadding(new Insets(5, 10, 5, 5));
//            vb_main.getChildren().add(vBox);
//        }





    public void openprofileChooser(MouseEvent mouseEvent) {

    }
}
