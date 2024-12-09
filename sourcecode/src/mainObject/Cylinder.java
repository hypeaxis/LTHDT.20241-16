package mainObject;

public class Cylinder extends MainObject /*implements Calculator*/{
	private static float MAXRADIUS = 100;
	private float radius;



	public Cylinder(float mass, float radius) {
		super(mass);
		this.radius = radius;
	}



	public float getRadius() {
		return radius;
	}



	public void setRadius(float radius) {
		if(radius > MAXRADIUS) {
			return;
		}
		this.radius = radius;
	}
	
	
}
