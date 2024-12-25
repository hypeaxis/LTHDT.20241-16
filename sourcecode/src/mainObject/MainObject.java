package mainObject;


import force.AppliedForce;
import force.Friction;
import force.Gravity;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class MainObject{

	private FloatProperty mass = new SimpleFloatProperty(0.1f);
	private FloatProperty position = new SimpleFloatProperty(0);
	private FloatProperty velocity = new SimpleFloatProperty(0);
	private FloatProperty acceleration = new SimpleFloatProperty(0);



	
	public MainObject() {
		// TODO Auto-generated constructor stub
	}
	
	public MainObject(float mass) {
		super();
		this.mass.set(mass);
	}
	

	public float getMass() {
		return mass.get();
	}

	public FloatProperty getMassProperty() {
		return mass;
	}


	
	public void setMass(float mass) {
		this.mass.set(mass);
	}

	public void setPosition(float position) {
		this.position.set(position);
	}

	public void setVelocity(float velocity) {
		this.velocity.set(velocity);
	}

	public void setAcceleration(float acceleration) {
		this.acceleration.set(acceleration);
	}


	public float getPosition() {
		return position.get();
	}

	public float getVelocity() {
		return velocity.get();
	}
	
	public float getAcceleration() {
		return acceleration.get();
	}
	
	public FloatProperty getPositionProperty() {
		return position;
	}
	
	public FloatProperty getVelocityProperty() {
		return velocity;
	}
	

	public FloatProperty getAccelerationProperty() {
		return acceleration;
	}
	
	public void updateTranslationMotion(AppliedForce F, Friction friction, float deltatime) {

		this.acceleration.set((F.getValue() - friction.getValue()) / this.mass.get());;			
		
		this.velocity.set(this.velocity.get() + this.acceleration.get() * deltatime);
		
		this.position.set(this.position.get() + this.velocity.get() * deltatime);

		

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
