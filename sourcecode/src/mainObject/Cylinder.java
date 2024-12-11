package mainObject;


import force.AppliedForce;
import force.Friction;

public class Cylinder extends MainObject /*implements Calculator*/{
	private static float MAXRADIUS = 100;
	private float radius;
	private float angularPosition;
	private float angularVelosity;
	private float angularAcceleration;



	public Cylinder(float mass, float radius) {
		super(mass);
		this.radius = radius;
	}



	public float getRadius() {
		return radius;
	}
	

	public float getAngularPosition() {
		return angularPosition;
	}



	public void setRadius(float radius) {
		if(radius > MAXRADIUS) {
			return;
		}
		this.radius = radius;
	}



	@Override
	public void updateRotationMotion(AppliedForce F, Friction friction, float deltatime) {
		this.angularAcceleration = 2 * (friction.getValue()) / (this.getMass() * this.radius * this.radius);
		
		this.angularVelosity += this.angularAcceleration * deltatime;
		
		this.angularPosition += this.angularVelosity * deltatime;
		
	}

	
}
