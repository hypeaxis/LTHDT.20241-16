package force;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class Force {
	private FloatProperty value = new SimpleFloatProperty();

	
	public Force() {
		
	}

	public float getValue() {
		return value.get();
	}
	
	public FloatProperty getValueProperty() {
		return value;
	}


	public void setValue(float value) {
		this.value.set(value);
	}



	
}
