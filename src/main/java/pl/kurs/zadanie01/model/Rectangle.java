package pl.kurs.zadanie01.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Objects;

@JsonTypeName("rectangle")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")

public class Rectangle extends Figure {
    private double width;

    public Rectangle(double height, double width) {
        super(height);
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public double getSize() {
        return super.getSize();
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setSize(double size) {
        super.setSize(size);
    }

    @Override
    public double calculateArea() {
        checkIfSideIsGreaterThan0(getSize(), width);
        return getSize() * width;
    }

    @Override
    public double calculatePerimeter() {
        checkIfSideIsGreaterThan0(getSize(), width);
        return 2 * getSize() + 2 * width;
    }

    private boolean checkIfSideIsGreaterThan0(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("Input value needs to be greater than 0");
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(rectangle.width, width) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width);
    }


    @Override
    public String toString() {
        return super.toString() + " " +
                "width=" + width;
    }
}
