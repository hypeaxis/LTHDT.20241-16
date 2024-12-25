package force;

public class AppliedForce extends Force {

	public AppliedForce() {
		super();
	}

	@Override
	public void setValue(float value) {
		super.setValue(value);  // Gọi phương thức setValue của lớp cha (Force)
	}

	@Override
	public float getValue() {
		return super.getValue();  // Gọi phương thức getValue của lớp cha (Force)
	}
}
