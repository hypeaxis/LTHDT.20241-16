package simulator;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ForceSimulator extends Application{



	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		System.out.println(getClass().getResource("/resources/ForceSimulator.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/ForceSimulator.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 1200, 800);

		primaryStage.setTitle("Force Simulator");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}


