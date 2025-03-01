package pl.kurs.zadanie01.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("square")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public class Square extends Figure {

    public Square(double size) {
        super(size);
    }

    public Square() {
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
        return 4 * getSize();
    }

    @Override
    public double calculatePerimeter() {
        checkIfSideIsGreaterThan0(getSize());
        return getSize() * getSize();
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
