package simulator;

import java.util.function.Consumer;
import force.AppliedForce;
import force.Force;
import force.Friction;
import force.Gravity;
import force.NormalForce;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import mainObject.MainObject;
import surface.Surface;

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
	ImageView background;
	@FXML
	ImageView background2;
	@FXML
	ImageView applied;
	@FXML
	ImageView friction;
	@FXML
	TextField staticField;
	@FXML
	Slider staticSlider;
	@FXML
	TextField kineticField;
	@FXML
	Slider kineticSlider;
	
	private double Xdistance = 0;
	private double Ydistance = 0;
	private IntegerProperty numberOfObjects = new SimpleIntegerProperty();
	private double startY;
	private boolean isdragged = false;
	private double Xdrop;
	private double Ydrop;
	private final String RED = "-fx-border-color: red;";
	private double edge;
	private StringProperty typeOfObject = new SimpleStringProperty();
	mainObject.Cube cube = new mainObject.Cube(100);
	mainObject.Cylinder cylinder = new mainObject.Cylinder(50);
    Surface surface = new Surface();
	AppliedForce appliedForce = new AppliedForce();
	Gravity gravity = new Gravity();
	NormalForce normal = new NormalForce();
	Friction frictionForce = new Friction();	
	
	public void setDraggable(ImageView obj, MainObject object) {
		double localX = obj.getLayoutX();
		double localY = obj.getLayoutY();
		edge = obj.getFitHeight();
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
				if(numberOfObjects.get() == 0 && obj.getLayoutY() <= 490 && obj.getLayoutY() >= 0) {
					obj.setLayoutX(Xdrop - obj.getFitHeight()/2);
					obj.setLayoutY(Ydrop - obj.getFitWidth());
					numberOfObjects.set(1);
					if(object instanceof mainObject.Cube) {
						typeOfObject.set("Cube");
					} else if(object instanceof mainObject.Cylinder) {
						typeOfObject.set("Cylinder");
					}
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
						((mainObject.Cube) object).setSizeLength((float) edge);
					} else if(object instanceof mainObject.Cylinder) {
						((mainObject.Cylinder) object).setRadius((float) edge/2);
					}
					object.setMass(0);
					numberOfObjects.set(0);
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
	
	public void updateFrictionPosition(Friction fric, AppliedForce force) {
		if (force.getValue() == 0 || fric.getValue() == 0) {
	        friction.setVisible(false);
	    } else if (force.getValue() > 0) {
	        friction.setVisible(true);
	        friction.setRotate(180);
	        friction.setLayoutX(Xdrop - fric.getValue());
	        friction.setLayoutY(Ydrop-15);
	        friction.setFitWidth(fric.getValue());
	    } else {
	        friction.setVisible(true);
	        friction.setRotate(0);
	        friction.setLayoutX(Xdrop);
	        friction.setLayoutY(Ydrop-15);
	        friction.setFitWidth(fric.getValue());
	    }
	}
	
	public void updateAppliedPosition(AppliedForce force, float length) {
		if (force.getValue() == 0) {
	        applied.setVisible(false);
	    } else if (force.getValue() > 0) {
	        applied.setVisible(true);
	        applied.setRotate(0);
	        applied.setLayoutX(Xdrop);
	        applied.setLayoutY(Ydrop - 15 - length);
	        applied.setFitWidth(force.getValue());
	    } else {
	        applied.setVisible(true);
	        applied.setRotate(180);
	        applied.setLayoutX(Xdrop + force.getValue());
	        applied.setLayoutY(Ydrop - 15 - length);
	        applied.setFitWidth(-force.getValue());
	    }
	}
	
	
	public void initialize() {
		Xdrop = border.getPrefWidth()/2;
		Ydrop = 500;
		
		

		
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
	    
	    ContextMenu cylinderContextMenu = new ContextMenu();
	    MenuItem cylinderMenuItem1 = new MenuItem("Radius");
	    MenuItem cylinderMenuItem2 = new MenuItem("Mass");
	    cylinderContextMenu.getItems().addAll(cylinderMenuItem1, cylinderMenuItem2);
	    
	    
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
		
	    
	    
	

		cube.getMassProperty().addListener((obs, oldValue, newValue) -> {
			gravity.calculateGravity(cube);
			normal.calculateNormalForce(gravity);
			frictionForce.calculateFriction(surface, cube, normal, appliedForce);
		});
		cylinder.getMassProperty().addListener((obs, oldValue, newValue) -> {
			gravity.calculateGravity(cylinder);
			normal.calculateNormalForce(gravity);
			frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
		});

		frictionForce.getValueProperty().addListener((obs, oldValue, newValue) -> {
			updateFrictionPosition(frictionForce, appliedForce);
		});
		
		
		
		numberOfObjects.addListener((obs, oldVal, newVal) -> {
			if(newVal.intValue() == 1) {
				appliedField.setDisable(false);
				appliedSlider.setDisable(false);
			} else {
				appliedField.setDisable(true);
				appliedSlider.setDisable(true);
				appliedField.setText("");
			}
		});
		


		appliedSlider.valueProperty().bindBidirectional(appliedForce.getValueProperty());
	    appliedField.textProperty().bindBidirectional(appliedSlider.valueProperty(), new NumberStringConverter());
	    appliedField.textProperty().bindBidirectional(appliedForce.getValueProperty(), new NumberStringConverter());
	    
	    
	    appliedField.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
	            appliedField.setText(oldValue);
	        }
	        if ("Cube".equals(typeOfObject.get())) {
	        	updateAppliedPosition(appliedForce, cube.getSizeLength()/2);
				frictionForce.calculateFriction(surface, cube, normal, appliedForce);
	        } else if ("Cylinder".equals(typeOfObject.get())) {
	        	updateAppliedPosition(appliedForce, cylinder.getRadius());
	        	frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
	        }
	    });
	    
	    
		staticSlider.valueProperty().bindBidirectional(surface.getStaticProperty());
		staticField.textProperty().bindBidirectional(staticSlider.valueProperty(), new NumberStringConverter());
		staticField.textProperty().bindBidirectional(surface.getStaticProperty(), new NumberStringConverter());
		
		staticField.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (!newValue.matches("\\d*(\\.\\d*)?")) {
	            staticField.setText(oldValue);
	        }
	    });
		
		surface.getStaticProperty().addListener((obs, oldValue, newValue) -> {
	        if(newValue.floatValue() < surface.getKineticCoefficient()) {
	        	surface.setStaticCoefficient(surface.getKineticCoefficient());
	        }
	        if ("Cube".equals(typeOfObject.get())) {
				frictionForce.calculateFriction(surface, cube, normal, appliedForce);
	        } else if ("Cylinder".equals(typeOfObject.get())) {
	        	frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
	        }
		});
		
		kineticSlider.valueProperty().bindBidirectional(surface.getKineticProperty());
		kineticField.textProperty().bindBidirectional(kineticSlider.valueProperty(), new NumberStringConverter());
		kineticField.textProperty().bindBidirectional(surface.getKineticProperty(), new NumberStringConverter());
		
		kineticField.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (!newValue.matches("\\d*(\\.\\d*)?")) {
	            kineticField.setText(oldValue);
	        }
	    });
		
		surface.getKineticProperty().addListener((obs, oldValue, newValue) -> {
	        if(newValue.floatValue() > surface.getStaticCoefficient()) {
	        	surface.setKineticCoefficient(surface.getStaticCoefficient());
	        }
	        if ("Cube".equals(typeOfObject.get())) {
				frictionForce.calculateFriction(surface, cube, normal, appliedForce);
	        } else if ("Cylinder".equals(typeOfObject.get())) {
	        	frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
	        }
		});
	    
		
	    
		Timeline timeline = new Timeline(
			new KeyFrame(Duration.millis(16), event -> {
				
				background.setLayoutX(background.getLayoutX() - 2);
				background2.setLayoutX(background2.getLayoutX() - 2);
				
				if(background.getLayoutX() <= -1200) {
					background.setLayoutX(background2.getLayoutX() + 1200);
				}
				if(background2.getLayoutX() <= -1200) {
					background2.setLayoutX(background.getLayoutX() + 1200);
				}
			})
		);
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		
		
		
	}
}
