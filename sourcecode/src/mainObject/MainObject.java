package mainObject;


import force.AppliedForce;
import force.Friction;
import force.Gravity;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class MainObject {
	protected FloatProperty mass = new SimpleFloatProperty();
	public float velocity = 0; // Vận tốc
	public float position = 0; // Vị trí
	public float acceleration = 0; // Gia tốc

	// Thuộc tính cho chuyển động quay (nếu áp dụng)
	public float angularVelocity = 0; // Vận tốc góc
	public float angularAcceleration = 0; // Gia tốc góc
	public float rotationAngle = 0; // Góc quay
	
	public MainObject() {
		// TODO Auto-generated constructor stub
	}
	
	public MainObject(float mass) {
		super();
		this.mass.set(mass);
	}
	
	public MainObject(float mass, float position) {
		super();
		this.mass.set(mass);
		this.position = position;
	}

	public double getAngularVelocity() {
	    return angularVelocity;
	}

	public double getRotationAngle() {
	    return rotationAngle;
	}


	public float getMass() {
		return mass.get();
	}

	public FloatProperty getMassProperty() {
		return mass;
	}

	public void setMass(float mass) {
	    this.mass.set(mass); // Gán giá trị mass vào thuộc tính
	}
	
	public float getPosition() {
		return position;
	}

	public float getVelocity() {
		return velocity;
	}
	
	public float getAcceleration() {
		return acceleration;
	}
	
	
	public void updateTranslationMotion(double appliedForce, double friction, float deltaTime) {
	    acceleration = (float) (Math.abs(appliedForce - friction) / getMass()); // Sử dụng getMass()
	    velocity += acceleration * deltaTime;
	    position += velocity * deltaTime;
	}


	public void updateRotationMotion(AppliedForce F, Friction friction, float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	public void setMass(FloatProperty mass, Gravity gravity) {
	    this.mass = mass;

	    // Tính lại giá trị gravity
	    gravity.calculateGravity(this);
	}
}
