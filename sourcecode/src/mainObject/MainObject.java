package mainObject;


import force.AppliedForce;
import force.Friction;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class MainObject {
	private FloatProperty mass = new SimpleFloatProperty();
	private float position;
	private float velocity;
	private float acceleration;
	
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


	public float getMass() {
		return mass.get();
	}


	public void setMass(float mass) {
		this.mass.set(mass);
	}
	
	public float getPosition() {
		return position;
	}

	public float getVelocity() {
		return velocity;
	}
	
	
	public void updateTranslationMotion(AppliedForce F, Friction friction, float deltatime) {
		this.acceleration = (F.getValue() - friction.getValue()) / this.mass.get();
		
		this.velocity += this.acceleration * deltatime;
		
		this.position += this.velocity * deltatime;
	}

	
	
	public abstract void updateRotationMotion(AppliedForce F, Friction friction, float deltatime);
	

}
