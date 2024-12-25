package mainObject;


import force.AppliedForce;
import force.Friction;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Cube extends MainObject/* implements Calculator*/{
	private static float MAXLENGTH = 300;
	private FloatProperty sideLength = new SimpleFloatProperty();



	public Cube(float sizeLength) {
		super();
		this.sideLength.set(sizeLength);
	}


	public float getSizeLength() {
		return sideLength.get();
	}



	public void setSizeLength(float sideLength) {
		if(sideLength > MAXLENGTH) {
			return;
		}
		this.sideLength.set(sideLength);
	}

	public FloatProperty sidelengthProperty() {
		return sideLength;
	}

	@Override
	public void updateRotationMotion(AppliedForce F, Friction friction, float deltatime) {
		// TODO Auto-generated method stub

	}



}
