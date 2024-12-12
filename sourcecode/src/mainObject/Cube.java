package mainObject;


import force.AppliedForce;
import force.Friction;

public class Cube extends MainObject/* implements Calculator*/{
	private static float MAXLENGTH = 100;
	private float sizeLength;



	public Cube(float mass, float sizeLength) {
		super(mass);
		this.sizeLength = sizeLength;
	}



	public float getSizeLength() {
		return sizeLength;
	}



	public void setSizeLength(float sizeLength) {
		if(sizeLength > MAXLENGTH) {
			return;
		}
		this.sizeLength = sizeLength;
	}



	@Override
	public void updateRotationMotion(AppliedForce F, Friction friction, float deltatime) {
		// TODO Auto-generated method stub
		
	}
	
	

}
