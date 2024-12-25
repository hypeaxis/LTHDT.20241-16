package simulator;

import java.util.function.Consumer;
import force.AppliedForce;
import force.Friction;
import force.Gravity;
import force.NormalForce;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
	ImageView sum;
	@FXML
	TextField staticField;
	@FXML
	Slider staticSlider;
	@FXML
	TextField kineticField;
	@FXML
	Slider kineticSlider;
	@FXML
	CheckBox forceCheckBox;
	@FXML
	CheckBox massCheckBox;
	@FXML
	CheckBox sumCheckBox;
	@FXML
	private Label massLabel;
	
	@FXML
	private VBox infoBox;
	
	@FXML
	private Button pauseButton;

	@FXML
	private Button resetButton;

	private VBox buttonBox;
	
	MainObject selectedObject;


	private Label velocityLabel = new Label("Velocity: 0 m/s");
	private Label accelerationLabel = new Label("Acceleration: 0 m/s²");

	
	private Timeline timeline;
	private boolean isReadyToRun = false; // Cờ để xác định khi nào Timeline có thể chạy

	private double Xdistance = 0;
	private double Ydistance = 0;
	private IntegerProperty numberOfObjects = new SimpleIntegerProperty();
	private double startY;
	private boolean isdragged = false;
	private double Xdrop;
	private double Ydrop;
	private final String RED = "-fx-border-color: red;";
	mainObject.Cube cube = new mainObject.Cube(100);
	mainObject.Cylinder cylinder = new mainObject.Cylinder(50);
    Surface surface = new Surface();
	AppliedForce appliedForce = new AppliedForce();
	Gravity gravity = new Gravity();
	NormalForce normal = new NormalForce();
	Friction frictionForce = new Friction();
	private boolean isVisible = true;
	private boolean isVisible2 = false;	
	
	private void handlePauseButton() {
	    if (timeline.getStatus() == Timeline.Status.RUNNING) {
	        timeline.pause();
	        pauseButton.setText("Resume"); // Đổi nút thành Resume
	    } else {
	        timeline.play();
	        pauseButton.setText("Pause"); // Đổi nút lại thành Pause
	    }
	}
	
	private void handleResetButton() {
	    
		timeline.stop(); // Dừng Timeline

        float deltaTime = 0.016f;
        
        if(selectedObject instanceof mainObject.Cube) {
        	cube.reset();
            appliedForce.setValue(0);
    		updateAppliedPosition(appliedForce, cube.getSizeLength()/2);
    		gravity.calculateGravity(cube);
    		normal.calculateNormalForce(gravity);
    		frictionForce.calculateFriction(surface, cube, normal, appliedForce);
    		updateFrictionPosition(frictionForce);
    		cube.updateTranslationMotion(appliedForce, frictionForce, deltaTime);
    		updateSumPosition(appliedForce, frictionForce);
    		velocityLabel.setText(String.format("Velocity: %.2f m/s", cube.getVelocity()));
            accelerationLabel.setText(String.format("Acceleration: %.2f m/s²", cube.getAcceleration()));

    	} else if (selectedObject instanceof mainObject.Cylinder) {
        	cylinder.reset();
        	appliedForce.setValue(0);
    		updateAppliedPosition(appliedForce, cylinder.getRadius());
    		gravity.calculateGravity(cylinder);
    		normal.calculateNormalForce(gravity);
    		frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
    		updateFrictionPosition(frictionForce);
    		cylinder.updateTranslationMotion(appliedForce, frictionForce, deltaTime);
			cylinder.updateRotationMotion(appliedForce, frictionForce, deltaTime);
			updateSumPosition(appliedForce, frictionForce);
			velocityLabel.setText(String.format("Velocity: %.2f m/s", cylinder.getVelocity()));
            accelerationLabel.setText(String.format("Acceleration: %.2f m/s²", cylinder.getAcceleration()));
            
    	}
        
        
    
		
	}
	
	private boolean areInputsValid() {
	    try {
	        // Kiểm tra đối tượng được chọn
	        if (selectedObject == null) {
	            return false;
	        }
	        // Kiểm tra mass
	        if (selectedObject.getMass() == 0) {
	            return false;
	        }
	        // Kiểm tra lực tác dụng
	        if (appliedForce.getValue() == 0) {
	            return false;
	        }
	        // Kiểm tra các hệ số ma sát
	        if (surface.getKineticCoefficient() == 0 || surface.getStaticCoefficient() == 0) {
	            return false;
	        }
	        return true; // Tất cả điều kiện thỏa mãn
	    } catch (Exception e) {
	        return false; // Bắt lỗi nếu có ngoại lệ
	    }
	}
	

	public void setDraggable(ImageView obj, MainObject object) {
	    double localX = obj.getLayoutX();
	    double localY = obj.getLayoutY();
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
	        if (!isdragged) return;



	        if (localY == startY) {
	            if (numberOfObjects.get() == 0 && obj.getLayoutY() <= 490 && obj.getLayoutY() >= 0) {
	                obj.setLayoutX(Xdrop - obj.getFitHeight() / 2);
	                obj.setLayoutY(Ydrop - obj.getFitWidth());
	                numberOfObjects.set(1);
	    	        selectedObject = object;
	            } else {
	                obj.setLayoutX(localX);
	                obj.setLayoutY(localY);
	            }
	        } else {
	            if (obj.getLayoutY() <= 490 && obj.getLayoutY() >= 0) {
	                obj.setLayoutX(Xdrop - obj.getFitHeight() / 2);
	                obj.setLayoutY(Ydrop - obj.getFitWidth());
	            } else {
	                obj.setLayoutX(localX);
	                obj.setLayoutY(localY);
	                numberOfObjects.set(0);
	                if (object instanceof mainObject.Cube) {
	                    ((mainObject.Cube) object).reset();
	                } else if (object instanceof mainObject.Cylinder) {
	                    ((mainObject.Cylinder) object).reset();
	                }
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
	 
	public void updateFrictionPosition(Friction fric) {
		if (fric.getValue() == 0) {
	        friction.setVisible(false);
	    } else if (fric.getValue() > 0) {
	    	if(isVisible) {
	    		friction.setVisible(true);
	    	} else {
	    		friction.setVisible(false);
	    	}
	        friction.setRotate(180);
	        friction.setLayoutX(Xdrop - fric.getValue());
	        friction.setLayoutY(Ydrop-15);
	        friction.setFitWidth(fric.getValue());
	    } else {
	    	if(isVisible) {
	    		friction.setVisible(true);
	    	} else {
	    		friction.setVisible(false);
	    	}
	        friction.setRotate(0);
	        friction.setLayoutX(Xdrop);
	        friction.setLayoutY(Ydrop-15);
	        friction.setFitWidth(-fric.getValue());
	    }
	}
	
	public void updateAppliedPosition(AppliedForce force, float length) {
		if (force.getValue() == 0) {
	        applied.setVisible(false);
	    } else if (force.getValue() > 0) {
	    	if(isVisible) {
	    		applied.setVisible(true);
	    	} else {
	    		applied.setVisible(false);
	    	}
	        applied.setRotate(0);
	        applied.setLayoutX(Xdrop);
	        applied.setLayoutY(Ydrop - 15 - length);
	        applied.setFitWidth(force.getValue());
	    } else {
	    	if(isVisible) {
	    		applied.setVisible(true);
	    	} else {
	    		applied.setVisible(false);
	    	}
	        applied.setRotate(180);
	        applied.setLayoutX(Xdrop + force.getValue());
	        applied.setLayoutY(Ydrop - 15 - length);
	        applied.setFitWidth(-force.getValue());
	    }
	}
	
	public void updateSumPosition(AppliedForce force, Friction friction) {
		float total = force.getValue() - friction.getValue();
		if (total == 0) {
			sum.setVisible(false);
		} else if (total > 0) {
			if(isVisible2) {
				sum.setVisible(true);
	    	} else {
				sum.setVisible(false);
	    	}
			sum.setRotate(0);
			sum.setLayoutX(600);
			sum.setLayoutY(300);
			sum.setFitWidth(total);
		} else if (total < 0) {
			if(isVisible2) {
				sum.setVisible(true);
	    	} else {
				sum.setVisible(false);
	    	}
			sum.setRotate(180);
			sum.setLayoutX(600 + total);
			sum.setLayoutY(300);
			sum.setFitWidth(-total);
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
	            cube.setMass(value); // Cập nhật giá trị mass cho Cube
	            selectedObject = cube; // Cập nhật đối tượng được chọn
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
	            cylinder.setMass(value); // Cập nhật giá trị mass cho Cylinder
	            selectedObject = cylinder; // Cập nhật đối tượng được chọn
	        });
	    });
	    
	    if (pauseButton == null) pauseButton = new Button("Pause");
	    if (resetButton == null) resetButton = new Button("Reset");

	 // Tạo các nút nếu chưa có trong FXML
	    if (pauseButton == null) pauseButton = new Button("Pause");
	    if (resetButton == null) resetButton = new Button("Reset");

	    // Tạo VBox để chứa các nút
	    buttonBox = new VBox(10, pauseButton, resetButton); // Khoảng cách giữa các nút là 10
	    buttonBox.setStyle("-fx-alignment: center-right;"); // Căn phải các nút

	    // Đặt vị trí VBox ở giữa bên phải
	    buttonBox.setLayoutX(border.getPrefWidth() - 100); // 100: chiều rộng ước lượng của VBox
	    buttonBox.setLayoutY(border.getPrefHeight() / 2 - 50); // 50: chiều cao ước lượng của VBox / 2

	    // Thêm VBox vào giao diện chính
	    border.getChildren().add(buttonBox);

	    // Gắn sự kiện cho nút Pause
	    pauseButton.setOnAction(event -> handlePauseButton());

	    // Gắn sự kiện cho nút Reset
	    resetButton.setOnAction(event -> handleResetButton());
	
	    
	    // Tạo VBox chứa velocityLabel và positionLabel
	    VBox infoBox = new VBox(10); // Khoảng cách giữa các nhãn
	    infoBox.setLayoutX(10); // Vị trí X
	    infoBox.setLayoutY(10); // Vị trí Y
	    infoBox.getChildren().addAll(velocityLabel, accelerationLabel); // Thay đổi nhãn
	    border.getChildren().add(infoBox);
	    
	    forceCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
	        if(newValue) {
	            isVisible = true;
	        } else {
	        	isVisible = false;
	        }
	    });
	    
	    sumCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
	        if(newValue) {
	            isVisible2  = true;
	        } else {
	        	isVisible2 = false;
	        }
	    });
	    
	    if (massLabel == null) {
	        massLabel = new Label();
	        massLabel.setStyle("-fx-background-color: white; -fx-border-color: black;");
	        massLabel.setVisible(false); // Ban đầu ẩn Label
	        border.getChildren().add(massLabel); // Thêm vào giao diện
	    }
	    
	    massCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue) {
	            // Khi tick vào checkbox, hiển thị giá trị mass
	            if (selectedObject != null) {
	                massLabel.setText(String.format("%.2f kg", selectedObject.getMass()));

	                // Đặt vị trí Label ở giữa vật thể
	                double centerX = Cube.getLayoutX() + Cube.getFitWidth() / 2 - massLabel.getWidth() / 2;
	                double centerY = Cube.getLayoutY() + Cube.getFitHeight() / 2 - massLabel.getHeight() / 2;

	                massLabel.setLayoutX(centerX);
	                massLabel.setLayoutY(centerY);
	                massLabel.setVisible(true);
	            }
	        } else {
	            // Khi bỏ tick, ẩn Label
	            massLabel.setVisible(false);
	        }
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
	    
		
		appliedSlider.valueProperty().bindBidirectional(appliedForce.getValueProperty());
	    appliedField.textProperty().bindBidirectional(appliedSlider.valueProperty(), new NumberStringConverter());
	    appliedField.textProperty().bindBidirectional(appliedForce.getValueProperty(), new NumberStringConverter());
	    
	    
	//ktra nhap va xu li ngoai le
	    appliedField.textProperty().addListener((obs, oldValue, newValue) -> {

	        try {
	            //  ktra gtri moi co hop le ko
	            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
	                throw new IllegalArgumentException("Invalid input in appliedField");
	            }
	            appliedField.setStyle(""); // tra ve mau trang
	        } catch (IllegalArgumentException e) {
	            appliedField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu viền và nền khi nhập sai
	        }
	    });
	    

	    
	    staticSlider.valueProperty().bindBidirectional(surface.getStaticProperty());
	    staticField.textProperty().bindBidirectional(staticSlider.valueProperty(), new NumberStringConverter());
	    staticField.textProperty().bindBidirectional(surface.getStaticProperty(), new NumberStringConverter());

	    kineticSlider.valueProperty().bindBidirectional(surface.getKineticProperty());
	    kineticField.textProperty().bindBidirectional(kineticSlider.valueProperty(), new NumberStringConverter());
	    kineticField.textProperty().bindBidirectional(surface.getKineticProperty(), new NumberStringConverter());

	    // Đảm bảo static >= kinetic
	    staticSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue.doubleValue() < kineticSlider.getValue()) {
	            staticSlider.setValue(kineticSlider.getValue());
	        }
	    });

	    kineticSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue.doubleValue() > staticSlider.getValue()) {
	            kineticSlider.setValue(staticSlider.getValue());
	        }
	    });

	    // Kiểm tra giá trị nhập vào staticField
	    staticField.textProperty().addListener((obs, oldValue, newValue) -> {
	        try {
	            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
	                throw new IllegalArgumentException("Invalid input in staticField");
	            }
	            double staticValue = Double.parseDouble(newValue);
	            double kineticValue = kineticSlider.getValue();

	            if (staticValue < kineticValue) {
	                staticSlider.setValue(kineticValue); // Cập nhật giá trị static nếu nhỏ hơn kinetic
	            }

	            staticField.setStyle(""); // Trả về màu trắng nếu nhập đúng
	        } catch (IllegalArgumentException e) {
	            staticField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu nếu nhập sai
	        }
	    });

	    // Kiểm tra giá trị nhập vào kineticField
	    kineticField.textProperty().addListener((obs, oldValue, newValue) -> {
	        try {
	            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
	                throw new IllegalArgumentException("Invalid input in kineticField");
	            }
	            double kineticValue = Double.parseDouble(newValue);
	            double staticValue = staticSlider.getValue();

	            if (kineticValue > staticValue) {
	                kineticSlider.setValue(staticValue); // Cập nhật giá trị kinetic nếu lớn hơn static
	            }

	            kineticField.setStyle(""); // Trả về màu trắng nếu nhập đúng
	        } catch (IllegalArgumentException e) {
	            kineticField.setStyle("-fx-border-color: red; -fx-background-color: lightpink;"); // Đổi màu nếu nhập sai
	        }
	    });


		timeline = new Timeline(
		        new KeyFrame(Duration.millis(16), event -> {
		            float deltaTime = 0.016f;
		            
		            if(selectedObject instanceof mainObject.Cube) {
		        		if(numberOfObjects.get() == 0) {
		            		cube.reset();
			            	appliedForce.setValue(0);
		        		}
			    		updateAppliedPosition(appliedForce, cube.getSizeLength()/2);
			    		gravity.calculateGravity(cube);
			    		normal.calculateNormalForce(gravity);
			    		frictionForce.calculateFriction(surface, cube, normal, appliedForce);
			    		updateFrictionPosition(frictionForce);
			    		cube.updateTranslationMotion(appliedForce, frictionForce, deltaTime);
			    		updateSumPosition(appliedForce, frictionForce);
			    		velocityLabel.setText(String.format("Velocity: %.2f m/s", cube.getVelocity()));
		                accelerationLabel.setText(String.format("Acceleration: %.2f m/s²", cube.getAcceleration()));
		                
		                
		                background.setLayoutX(background.getLayoutX() -  cube.getVelocity()*deltaTime);
						background2.setLayoutX(background2.getLayoutX() - cube.getVelocity()*deltaTime);
						
						if(background.getLayoutX() <= -1200) {
							background.setLayoutX(background2.getLayoutX() + 1200);
						}
						if(background2.getLayoutX() <= -1200) {
							background2.setLayoutX(background.getLayoutX() + 1200);
						}
						if(background.getLayoutX() >= 1200) {
							background.setLayoutX(background2.getLayoutX() - 1200);
						}
						if(background2.getLayoutX() >= 1200) {
							background2.setLayoutX(background.getLayoutX() - 1200);
						}
			    	} else if (selectedObject instanceof mainObject.Cylinder) {
			    		if(numberOfObjects.get() == 0) {
		            		cylinder.reset();
		            		appliedForce.setValue(0);
			    		}
			    		updateAppliedPosition(appliedForce, cylinder.getRadius());
			    		gravity.calculateGravity(cylinder);
			    		normal.calculateNormalForce(gravity);
			    		frictionForce.calculateFriction(surface, cylinder, normal, appliedForce);
			    		updateFrictionPosition(frictionForce);
			    		cylinder.updateTranslationMotion(appliedForce, frictionForce, deltaTime);
						cylinder.updateRotationMotion(appliedForce, frictionForce, deltaTime);
						updateSumPosition(appliedForce, frictionForce);
						velocityLabel.setText(String.format("Velocity: %.2f m/s", cylinder.getVelocity()));
		                accelerationLabel.setText(String.format("Acceleration: %.2f m/s²", cylinder.getAcceleration()));
	                    
	                    background.setLayoutX(background.getLayoutX() - cylinder.getVelocity()*deltaTime);
						background2.setLayoutX(background2.getLayoutX() - cylinder.getVelocity()*deltaTime);
						
						if(background.getLayoutX() <= -1200) {
							background.setLayoutX(background2.getLayoutX() + 1200);
						}
						if(background2.getLayoutX() <= -1200) {
							background2.setLayoutX(background.getLayoutX() + 1200);
						}
						if(background.getLayoutX() >= 1200) {
							background.setLayoutX(background2.getLayoutX() - 1200);
						}
						if(background2.getLayoutX() >= 1200) {
							background2.setLayoutX(background.getLayoutX() - 1200);
						}
						
						Cylinder.setRotate(cylinder.getAngularVelocity()*0.016);
			    	}
		            
		            
		        })
		    );

	    timeline.setCycleCount(Timeline.INDEFINITE);


	    appliedForce.getValueProperty().addListener((observable, oldValue, newValue) -> {
	        if (areInputsValid() && !isReadyToRun) {
	            isReadyToRun = true;
	            timeline.play(); // Chạy Timeline khi đủ điều kiện
	        }
	    });

	    // Listener cho hệ số ma sát
	    surface.getStaticProperty().addListener((observable, oldValue, newValue) -> {
	        if (areInputsValid() && !isReadyToRun) {
	            isReadyToRun = true;
	            timeline.play();
	        }
	    });

	    surface.getKineticProperty().addListener((observable, oldValue, newValue) -> {
	        if (areInputsValid() && !isReadyToRun) {
	            isReadyToRun = true;
	            timeline.play();
	        }
	    });

	    // Listener cho mass
	    if (selectedObject != null) {
	        selectedObject.getMassProperty().addListener((observable, oldValue, newValue) -> {
	            if (areInputsValid() && !isReadyToRun) {
	                isReadyToRun = true;
	                timeline.play();
	            }
	        });
	    }

		

		
	} 
} 
