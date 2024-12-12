package simulator;


import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;


public class ForceSimulator extends Application{
	Stage window;
	Scene scene1, scene2;
	public static void main(String[] args) {
		launch(args);
	}

	
	
	@Override
    public void start(Stage primaryStage) throws Exception{
//		Dialog<Pair<String,String>> dialog = new Dialog<>();
//		dialog.setTitle("login Dialog");
//		dialog.setHeaderText("Sign up");
//		
//		ButtonType loginButtonType = new ButtonType("login", ButtonBar.ButtonData.OK_DONE);
//		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//		
//		GridPane grid = new GridPane();
//		grid.setHgap(10);
//		grid.setVgap(10);
//		grid.setPadding(new Insets(20,150,10,10));
//		
//		TextField userName = new TextField();
//		userName.setPromptText("UserName");
//		PasswordField password = new PasswordField();
//		password.setPromptText("Password");
//		
//		grid.add(new Label("UserName: "), 0, 0);
//		grid.add(userName, 1, 0);
//		grid.add(new Label("Password"), 0, 1);
//		grid.add(password, 1, 1);
//		
//		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
//		loginButton.setDisable(true);
//		
//		userName.textProperty().addListener((observable, oldValue, newValue) -> {
//			loginButton.setDisable(newValue.trim().isEmpty());
//		});
//		dialog.getDialogPane().setContent(grid);
//		
//		dialog.setResultConverter(dialogButton -> {
//			if(dialogButton == loginButtonType) {
//				return new Pair<>(userName.getText(), password.getText());
//			}
//			return null;
//		});
//		Optional<Pair<String,String>> result = dialog.showAndWait();
//		
//		result.ifPresent(userNamepassword -> {
//			System.out.println("UserName="+userNamepassword.getKey()+", Password="+userNamepassword.getValue());
//		});
		
		Label label = new Label(  "Programming Language");
		CheckBox box1 = new CheckBox( "Java") ;
		CheckBox box2 = new CheckBox(  "C#");
		CheckBox box3 = new CheckBox( "Python");
		box1.setSelected(true);
		Button button = new Button(  "Submit");
		button. setOnAction(e -> {
		String message = "Your language:";
		if (box1.isSelected())
			message += box1.getText();
		if (box2.isSelected())
			message += box2.getText();
		if (box3.isSelected())
			message += box3.getText();
		System.out.println(message);
		});
		
		HBox layoutH = new HBox( 10);
		layoutH.getChildren().addAll(box1, box2, box3);
		VBox layoutV = new VBox(  10);
		layoutV.getChildren().addAll(label, layoutH, button);
		Scene scene = new Scene(layoutV, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.show( );
	}
	
	

}
