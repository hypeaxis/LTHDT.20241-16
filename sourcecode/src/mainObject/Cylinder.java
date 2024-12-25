package mainObject;


import force.AppliedForce;
import force.Friction;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Cylinder extends MainObject /*implements Calculator*/{
    private static float MAXRADIUS = 150;
    private FloatProperty radius = new SimpleFloatProperty();
    private float angularPosition;
    private float angularVelosity;
    private float angularAcceleration;


    public Cylinder(float radius) {
        super();
        this.radius.set(radius);
    }


    public float getRadius() {
        return radius.get();
    }


    public float getAngularPosition() {
        return angularPosition;
    }


    public void setRadius(float radius) {
        if(radius > MAXRADIUS) {
            return;
        }
        this.radius.set(radius);
    }

    public FloatProperty radiusProperty() {
        return radius;
    }

    @Override
    public void updateRotationMotion(AppliedForce F, Friction friction, float deltatime) {
        this.angularAcceleration = 2 * (friction.getValue()) / (this.getMass() * this.radius.get() * this.radius.get());

        this.angularVelosity += this.angularAcceleration * deltatime;

        this.angularPosition += this.angularVelosity * deltatime;

    }



}
