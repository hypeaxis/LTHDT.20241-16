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
import javafx.beans.value.ChangeListener;
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
		if (fric.getValue() == 0) {
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
		
	    ChangeListener<? super Number> listener = (obs, oldValue, newValue) -> {
	    	if("Cube".equals(typeOfObject.get())) {
	    		updateAppliedPosition(appliedForce, cube.getSizeLength()/2);
	    		gravity.calculateGravity(cube);
	    		normal.calculateNormalForce(gravity);
	    		frictionForce.calculateFriction(surface, cube, normal, appliedForce);
	    		updateFrictionPosition(frictionForce, appliedForce);
	    	} else if ("Cylinder".equals(typeOfObject.get())) {
	    		updateAppliedPosition(appliedForce, cylinder.getRadius());
	    		gravity.calculateGravity(cylinder);
	    		normal.calculateNormalForce(gravity);
	    		frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
	    		updateFrictionPosition(frictionForce, appliedForce);
	    	}
	    };
	    
		
		appliedSlider.valueProperty().bindBidirectional(appliedForce.getValueProperty());
	    appliedField.textProperty().bindBidirectional(appliedSlider.valueProperty(), new NumberStringConverter());
	    appliedField.textProperty().bindBidirectional(appliedForce.getValueProperty(), new NumberStringConverter());
	
	    // Doan code nay su dung listener (trinh lang nghe) de theo doi su thay doi gia tri
	    // huc hien kiem tra tinh hop le cua gia tri moi duoc nhap.
	    //Kiem tra xem gia tri moi co tuan theo dinh dang so hop le (bao gom so am, so nguyen hoac so thuc) hay khong
	   // Neu khong, truong nhap lieu se thay doi giao dien de bao loi cho nguoi dung.


	//ktra nhap va xu li ngoai le neu thanh cong hoac khong thanh cong
	    appliedField.textProperty().addListener((obs, oldValue, newValue) -> {
	        try {
	            //  ktra gtri moi co hop le ko .neu khong thi se mau do
	            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
	                throw new IllegalArgumentException("Invalid input in appliedField");
	            }
	            appliedField.setStyle(""); // tra ve mau trang
	        } catch (IllegalArgumentException e) {
	            appliedField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu viền và nền khi nhập sai
	        }
	    });
	    
	    appliedForce.getValueProperty().addListener(listener);
	    
		staticSlider.valueProperty().bindBidirectional(surface.getStaticProperty());
		staticField.textProperty().bindBidirectional(staticSlider.valueProperty(), new NumberStringConverter());
		staticField.textProperty().bindBidirectional(surface.getStaticProperty(), new NumberStringConverter());
		
		staticField.textProperty().addListener((obs, oldValue, newValue) -> {
		    try {
		        // ktra gtri moi co hop le ko newValue la gia tri moi
		        if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
		            throw new IllegalArgumentException("Invalid input in staticField");
		        }
		        staticField.setStyle(""); //tra ve mau trang
		    } catch (IllegalArgumentException e) {
		        staticField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu viền và nền khi nhập sai
		    }
		});
		
		surface.getStaticProperty().addListener(listener);
		
		kineticSlider.valueProperty().bindBidirectional(surface.getKineticProperty());
		kineticField.textProperty().bindBidirectional(kineticSlider.valueProperty(), new NumberStringConverter());
		kineticField.textProperty().bindBidirectional(surface.getKineticProperty(), new NumberStringConverter());
		
		kineticField.textProperty().addListener((obs, oldValue, newValue) -> {
		    try {
		        // ktra gtri moi co hop le ko, hop le thi giu nguyen khong thi hien do
		        if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
		            throw new IllegalArgumentException("Invalid input in kineticField");
		        }
		        kineticField.setStyle(""); // tra ve mau trang
		    } catch (IllegalArgumentException e) {
		        kineticField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu viền và nền khi nhập sai
		    }
		});;
		
		surface.getKineticProperty().addListener(listener);
	    
		
		kineticSlider.valueProperty().bindBidirectional(surface.getKineticProperty());
		kineticField.textProperty().bindBidirectional(kineticSlider.valueProperty(), new NumberStringConverter());
		kineticField.textProperty().bindBidirectional(surface.getKineticProperty(), new NumberStringConverter());
		
		kineticField.textProperty().addListener((obs, oldValue, newValue) -> {
		    try {
		        // ktra gtri moi co hop le ko, hop le thi giu nguyen khong thi hien do
		        if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
		            throw new IllegalArgumentException("Invalid input in kineticField");
		        }
		        kineticField.setStyle(""); // tra ve mau trang
		    } catch (IllegalArgumentException e) {
		        kineticField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu viền và nền khi nhập sai
		    }
		});;
		
		surface.getKineticProperty().addListener(listener);
	    
		
	    
		Timeline timeline = new Timeline(
			    new KeyFrame(Duration.millis(16), event -> {
			        
			        // Cập nhật chuyển động cho Cube
			        if ("Cube".equals(typeOfObject.get())) {
			            // Cập nhật chuyển động dịch chuyển của Cube
			            cube.updateTranslationMotion(appliedForce, frictionForce, 0.016f);
			            System.out.println(cube.getPosition());
			        } 
			        // Cập nhật chuyển động cho Cylinder
			        else if ("Cylinder".equals(typeOfObject.get())) {
			            // Cập nhật chuyển động dịch chuyển và quay của Cylinder
			            cylinder.updateTranslationMotion(appliedForce, frictionForce, 0.016f);
			            cylinder.updateRotationMotion(appliedForce, frictionForce, 0.016f);
			        }
 
			        // Di chuyển nền (background) dựa trên giá trị lực
			        double backgroundShift = -appliedForce.getValue() * 0.016f;  // Dựa trên lực
			        background.setLayoutX(background.getLayoutX() + backgroundShift);
			        background2.setLayoutX(background2.getLayoutX() + backgroundShift);

			        // Cập nhật vị trí nền khi nó ra ngoài màn hình
			        if (background.getLayoutX() <= -1200) {
			            background.setLayoutX(background2.getLayoutX() + 1200);
			        }
			        if (background2.getLayoutX() <= -1200) {
			            background2.setLayoutX(background.getLayoutX() + 1200);
			        }
			    })
			);

			// Thiết lập để Timeline chạy mãi mãi
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();




		
		
		
		
	} 
} 