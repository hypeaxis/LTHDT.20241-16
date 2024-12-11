package mainObject;

import force.AppliedForce;
import force.Friction;

public abstract class MainObject {
	private float mass;
	private float position;
	private float velocity;
	private float acceleration;
	
	
	public MainObject(float mass) {
		super();
		this.mass = mass;
	}
	
	public MainObject(float mass, float position) {
		super();
		this.mass = mass;
		this.position = position;
	}

	public float getMass() {
		return mass;
	}


	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getPosition() {
		return position;
	}

	
	
	
	public void updateTranslationMotion(AppliedForce F, Friction friction, float deltatime) {
		this.acceleration = (F.getValue() - friction.getValue()) / this.mass;
		
		this.velocity += this.acceleration * deltatime;
		
		this.position += this.velocity * deltatime;
	}

	
	
	public abstract void updateRotationMotion(AppliedForce F, Friction friction, float deltatime);
	
}
