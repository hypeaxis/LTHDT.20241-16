package force;

public abstract class Force {
	private float value;
	private String direction;
	
	public Force() {
		
	}


	public float getValue() {
		return value;
	}


	public String getDirection() {
		return direction;
	}


	public void setValue(float value) {
		this.value = value;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String reverse() {
		if(this.direction.equals("left")) {
			return "right";
		}
		if(this.direction.equals("right")) {
			return "left";
		}
		return null;
	}
	
}
