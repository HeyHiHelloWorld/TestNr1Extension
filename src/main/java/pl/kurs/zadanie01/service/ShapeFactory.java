package pl.kurs.zadanie01.service;

import pl.kurs.zadanie01.model.Circle;
import pl.kurs.zadanie01.model.Rectangle;
import pl.kurs.zadanie01.model.Square;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ShapeFactory {

    private static final ShapeFactory INSTANCE = new ShapeFactory();
    private final Map<Double, Circle> circleCache = new ConcurrentHashMap<>();
    private final Map<String, Rectangle> rectangleCache = new ConcurrentHashMap<>();
    private final Map<Double, Square> squareCache = new ConcurrentHashMap<>();


    public static ShapeFactory getInstance() {
        return INSTANCE;
    }

    public Circle createCircle(double r) {
        return circleCache.computeIfAbsent(r, c -> new Circle(r));
    }

    public Rectangle createRectangle(double width, double height) {
        String key = width + " " + height;
        return rectangleCache.computeIfAbsent(key, k -> new Rectangle(width, height));
    }

    public Square createSquare(double a) {
        return squareCache.computeIfAbsent(a, s -> new Square(a));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapeFactory that = (ShapeFactory) o;
        return Objects.equals(circleCache, that.circleCache) && Objects.equals(rectangleCache, that.rectangleCache) && Objects.equals(squareCache, that.squareCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(circleCache, rectangleCache, squareCache);
    }
}
