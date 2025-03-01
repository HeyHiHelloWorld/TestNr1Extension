package pl.kurs.zadanie01.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name = "circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle"),
        @JsonSubTypes.Type(value = Square.class, name = "square")})
public abstract class Figure {
    private double size;

    public Figure(double size) {
        this.size = size;
    }

    public Figure() {
    }

    public abstract double calculateArea();

    public abstract double calculatePerimeter();

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return Double.compare(figure.size, size) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "size=" + size + '\'';
    }
}
