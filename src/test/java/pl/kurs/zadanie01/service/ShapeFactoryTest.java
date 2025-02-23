package pl.kurs.zadanie01.service;

import org.junit.Before;
import org.junit.Test;
import pl.kurs.zadanie01.model.Circle;
import pl.kurs.zadanie01.model.Rectangle;
import pl.kurs.zadanie01.model.Square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ShapeFactoryTest {

    private ShapeFactory shapeFactory;

    @Before
    public void init() {
        shapeFactory = ShapeFactory.getInstance();
    }


    @Test
    public void testCreateCircle() {
        Circle circle1 = shapeFactory.createCircle(5.0);
        Circle circle2 = shapeFactory.createCircle(5.0);

        assertNotNull(circle1);
        assertEquals(circle1, circle2);
        assertSame(circle1, circle2);
        assertNotSame(circle1, shapeFactory.createCircle(6.0));
    }

    @Test
    public void testCreateRectangle() {
        Rectangle rectangle1 = shapeFactory.createRectangle(4.0, 5.0);
        Rectangle rectangle2 = shapeFactory.createRectangle(4.0, 5.0);

        assertNotNull(rectangle1);
        assertEquals(rectangle1, rectangle2);
        assertSame(rectangle1, rectangle2);
        assertNotSame(rectangle1, shapeFactory.createRectangle(6.0, 5.0));
    }

    @Test
    public void testCreateSquare() {
        Square square1 = shapeFactory.createSquare(3.0);
        Square square2 = shapeFactory.createSquare(3.0);

        assertNotNull(square1);
        assertEquals(square1, square2);
        assertSame(square1, square2);
        assertNotSame(square1, shapeFactory.createSquare(4.0));
    }
}