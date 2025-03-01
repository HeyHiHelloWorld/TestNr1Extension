package pl.kurs.zadanie01.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("circle")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public class Circle extends Figure {


    @JsonCreator
    public Circle(double radius) {
        super(radius);
    }

    public Circle() {
    }

    @Override
    public double getSize() {
        return super.getSize();
    }

    @Override
    public void setSize(double size) {
        super.setSize(size);
    }

    @Override
    public double calculateArea() {
        checkIfSideIsGreaterThan0(getSize());
        return Math.PI * getSize() * getSize();
    }

    @Override
    public double calculatePerimeter() {
        checkIfSideIsGreaterThan0(getSize());
        return 2 * Math.PI * getSize();
    }

    private boolean checkIfSideIsGreaterThan0(double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Input value needs to be greater than 0");
        return true;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
