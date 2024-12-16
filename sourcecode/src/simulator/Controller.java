package simulator;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import mainObject.MainObject;

public class Controller {
	@FXML
	Pane border;
	@FXML
	ImageView Cube;
	@FXML
	ImageView Cylinder;
	@FXML
	TextField appliedField;
	@FXML
	Slider appliedSlider;
	@FXML
	ImageView surface;
	@FXML
	ImageView surface2;
	@FXML
	ImageView applied;
	@FXML
	ImageView gravity;
	@FXML
	ImageView nomarl;
	@FXML
	ImageView friction;
	
	private double Xdistance = 0;
	private double Ydistance = 0;
	private int numberOfObjects = 0;
	private double startY;
	private boolean isdragged = false;
	private double Xdrop;
	private double Ydrop;
	private final String RED = "-fx-border-color: red;";
	
	UnaryOperator<TextFormatter.Change> floatFilter = change -> {
	    String newText = change.getControlNewText();
	    if (newText.matches("\\d*\\.?\\d*")) {
	        return change;
	    }
	    return null;
	};
	
	TextFormatter<String> floatFormatter = new TextFormatter<>(floatFilter);
	
	public void setDraggable(ImageView obj, MainObject object) {
		double localX = obj.getLayoutX();
		double localY = obj.getLayoutY();
		float edge = (float) obj.getFitHeight();
		obj.setOnMousePressed(event -> {
			Xdistance = event.getSceneX() - obj.getLayoutX();
			Ydistance = event.getSceneY() - obj.getLayoutY();
			startY = obj.getLayoutY();
			isdragged = false;
		});
		obj.setOnMouseDragged(event -> {
			obj.setLayoutX(event.getSceneX() - Xdistance);
			obj.setLayoutY(event.getSceneY() - Ydistance);
			isdragged = true;
		});
		obj.setOnMouseReleased(event -> {
			
			if(!isdragged ) {
				return;
			}
			
			if(localY == startY) {
				if(numberOfObjects == 0 && obj.getLayoutY() <= 490 && obj.getLayoutY() >= 0) {
					obj.setLayoutX(Xdrop - obj.getFitHeight()/2);
					obj.setLayoutY(Ydrop - obj.getFitWidth());
					numberOfObjects++;
				} else {
					obj.setLayoutX(localX);
					obj.setLayoutY(localY);
				}
			} else {
				if(obj.getLayoutY() <= 490 && obj.getLayoutY() >= 0) {
					obj.setLayoutX(Xdrop - obj.getFitHeight()/2);
					obj.setLayoutY(Ydrop - obj.getFitWidth());
				} else {
					obj.setLayoutX(localX);
					obj.setLayoutY(localY);
					if(object instanceof mainObject.Cube) {
						((mainObject.Cube) object).setSizeLength(edge);
					} else if(object instanceof mainObject.Cylinder) {
						((mainObject.Cylinder) object).setRadius(edge/2);
					}
					numberOfObjects--;
				}
			}
		});
	}
	
	public void showInputDialog(ImageView obj, String content, Consumer<Float> callback) {
		TextField field = new TextField();
		field.setPromptText(content);
		field.setLayoutX(Xdrop);
		field.setLayoutY(Ydrop);
		
		border.getChildren().add(field);
		
		field.setOnAction(event -> {
	        try {
	            float value = Float.parseFloat(field.getText());
	            callback.accept(value);
	            border.getChildren().remove(field);
	        } catch (NumberFormatException e) {
	            field.setStyle(RED);
	        }
	    });

	    field.focusedProperty().addListener((observable, oldValue, newValue) -> {
	        if (!newValue) {
	            border.getChildren().remove(field);
	        }
	    });
	}
	
	public void initialize() {
		Xdrop = border.getPrefWidth()/2;
		Ydrop = 500;
		
		
		mainObject.Cube cube = new mainObject.Cube(100);
		mainObject.Cylinder cylinder = new mainObject.Cylinder(50);
		
		Cube.fitHeightProperty().bind(cube.sidelengthProperty());
		Cube.fitWidthProperty().bind(cube.sidelengthProperty());
		
		Cylinder.fitHeightProperty().bind(cylinder.radiusProperty().multiply(2));
		Cylinder.fitWidthProperty().bind(cylinder.radiusProperty().multiply(2));
		
		setDraggable(Cube, cube);
		setDraggable(Cylinder, cylinder);
		
		
		ContextMenu cubeContextMenu = new ContextMenu();
	    MenuItem cubeMenuItem1 = new MenuItem("Size-length");
	    MenuItem cubeMenuItem2 = new MenuItem("Mass");
	    cubeContextMenu.getItems().addAll(cubeMenuItem1, cubeMenuItem2);
	    
	    
	    cubeMenuItem1.setOnAction(event -> {
	    	showInputDialog(Cube, "Size-length: <= 300", value -> {
	    		cube.setSizeLength(value);
	    		Cube.setLayoutX(Xdrop - Cube.getFitHeight()/2);
	    		Cube.setLayoutY(Ydrop - Cube.getFitWidth());	    		
	    	});
	    });
	    
	    
	    cubeMenuItem2.setOnAction(event -> {
	    	showInputDialog(Cube, "Mass: ", value -> {
	    		cube.setMass(value);
	    	});	    	
	    });
	    
	    ContextMenu cylinderContextMenu = new ContextMenu();
	    MenuItem cylinderMenuItem1 = new MenuItem("Radius");
	    MenuItem cylinderMenuItem2 = new MenuItem("Mass");
	    cylinderContextMenu.getItems().addAll(cylinderMenuItem1, cylinderMenuItem2);
		
	    
	    cylinderMenuItem1.setOnAction(event -> {
	    	showInputDialog(Cylinder, "Radius: <= 150", value -> {
	    		cylinder.setRadius(value);
	    		Cylinder.setLayoutX(Xdrop - Cylinder.getFitHeight()/2);
	    		Cylinder.setLayoutY(Ydrop - Cylinder.getFitWidth());
	    	});	    	
	    });
	    
	    
	    cylinderMenuItem2.setOnAction(event -> {
	    	showInputDialog(Cylinder, "Mass: ", value -> {
	    		cylinder.setMass(value);
	    	});	    	
	    });
	    
    	Cube.setOnContextMenuRequested(event -> {
    		if(Cube.getLayoutY() <= 490 && Cube.getLayoutY() >= 0) {
    			cubeContextMenu.show(Cube, event.getScreenX(), event.getScreenY());
	    	}
    	});
		
	    Cylinder.setOnContextMenuRequested(event -> {
	    	if(Cylinder.getLayoutY() <= 490 && Cylinder.getLayoutY() >= 0) {
				cylinderContextMenu.show(Cylinder, event.getScreenX(), event.getScreenY());
	    	}
	    });
		
	    	
	    
	    
	    
	    
	    
	    
		Timeline timeline = new Timeline(
			new KeyFrame(Duration.millis(16), event -> {
				surface.setLayoutX(surface.getLayoutX() - 2);
				surface2.setLayoutX(surface2.getLayoutX() - 2);
				
				if(surface.getLayoutX() <= -1200) {
					surface.setLayoutX(surface2.getLayoutX() + 1200);
				}
				if(surface2.getLayoutX() <= -1200) {
					surface2.setLayoutX(surface.getLayoutX() + 1200);
				}
			})
		);
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		
		
		
	}
}
