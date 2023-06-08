package resources.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFormController {
    @FXML
    private AnchorPane loginWindow;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private Label lblError;


    private Matcher UserNameMatcher;

    public void initialize() {
        setPatterns();
    }

    @FXML
    void btnStartedOnAction(ActionEvent event) throws IOException {
        if (txtUserName.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Added User Name First!").show();
        } else {
            if (UserNameMatcher.matches()) {

            }
            ClientFormController.username = txtUserName.getText();
            Stage stage1 = new Stage();
            try {
                /*Parent parent=FXMLLoader.load(getClass().getResource("../view/ClientForm.fxml"));
                loginWindow.getChildren().clear();
                loginWindow.getChildren().add(parent);*/

                stage1.close();
                stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/resources/view/ClientForm.fxml"))));
                stage1.show();
                stage1.setMaximized(false);
                stage1.setResizable(false);

            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }

        }
        txtUserName.clear();

    }


    private void setPatterns() {
        Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9]{4,}$");
        UserNameMatcher = userNamePattern.matcher(txtUserName.getText());
    }


    @FXML
    void txtUserNameKeyTypedOnAction(KeyEvent event) {
        lblError.setText("");
        txtUserName.setFocusColor(Paint.valueOf("purple"));

        Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9]{4,}$");
        UserNameMatcher = userNamePattern.matcher(txtUserName.getText());
        if (!UserNameMatcher.matches()) {
            txtUserName.requestFocus();
            txtUserName.setFocusColor(Paint.valueOf("Red"));
            lblError.setText("invalid user name");
        }

    }
}
