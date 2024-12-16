package force;

public class AppliedForce extends Force{

	public AppliedForce() {
		super();
	}

	public void updateAppliedForce(float value) {
		this.setValue(value);
		if(value>=0) {
			this.setDirection("right");
		} else {
			this.setDirection("left");
		}
	}
}
