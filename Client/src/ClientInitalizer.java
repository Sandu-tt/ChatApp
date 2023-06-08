import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientInitalizer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/resources/view/LoginForm.fxml"))));
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();

    }
}
