package force;

import mainObject.MainObject;

public class Gravity extends Force{
	
	public Gravity() {
		super();
	}

	public void calculateGravity(MainObject obj) {
		this.setDirection("down");
		this.setValue(10 * obj.getMass());
	}
=======
public class Gravity extends Force{
	
}
