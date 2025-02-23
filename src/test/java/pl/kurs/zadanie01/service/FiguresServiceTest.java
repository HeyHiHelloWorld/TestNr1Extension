package pl.kurs.zadanie01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie01.config.ObjectMapperHolder;
import pl.kurs.zadanie01.model.Circle;
import pl.kurs.zadanie01.model.Figure;
import pl.kurs.zadanie01.model.Rectangle;
import pl.kurs.zadanie01.model.Square;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class FiguresServiceTest {
    private ShapeFactory shapeFactory;
    private List<Figure> figureList;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private FiguresService figuresService;


    @Before
    public void init() {
        figuresService = new FiguresService();
        shapeFactory = ShapeFactory.getInstance();
        objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        figureList = new ArrayList<>();
        figureList.add(shapeFactory.createCircle(5));
        figureList.add(shapeFactory.createCircle(1));
        figureList.add(shapeFactory.createSquare(50));
        figureList.add(shapeFactory.createSquare(3));
        figureList.add(shapeFactory.createRectangle(2, 4));
        figureList.add(null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkIfCalculateAreaIsGreaterThan0ForCircle() {
        Circle circle = shapeFactory.createCircle(3);
        double expectedResult = circle.calculateArea();
        assertEquals(28.274333882308138, expectedResult, 0);
    }

    @Test
    public void checkIfCalculatePerimeterIsGreaterThan0ForCircle() {
        Circle circle = shapeFactory.createCircle(3);
        double expectedResult = circle.calculatePerimeter();
        assertEquals(18.84955592153876, expectedResult, 0);
    }

    @Test
    public void checkIfCalculateAreaIsGreaterThan0ForSquare() {
        Square square = shapeFactory.createSquare(4);
        double expectedResult = square.calculateArea();
        assertEquals(16, expectedResult, 0);
    }

    @Test
    public void checkIfCalculatePerimeterIsGreaterThan0ForSquare() {
        Square square = shapeFactory.createSquare(4);
        double expectedResult = square.calculatePerimeter();
        assertEquals(16, expectedResult, 0);
    }


    @Test
    public void checkIfCalculateAreaIsGreaterThan0ForRectangle() {
        Rectangle rectangle = shapeFactory.createRectangle(3, 6);
        double expectedResult = rectangle.calculateArea();
        assertEquals(18, expectedResult, 0);
    }

    @Test
    public void checkIfCalculatePerimeterIsGreaterThan0ForRectangle() {
        Rectangle rectangle = shapeFactory.createRectangle(3, 6);
        double expectedResult = rectangle.calculatePerimeter();
        assertEquals(18, expectedResult, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfValueIsLessThan0InCalculatePerimeter() {
        Rectangle rectangle = shapeFactory.createRectangle(-3, 6);
        double expectedResult = rectangle.calculatePerimeter();
        assertEquals(16, expectedResult, 0);
    }


    @Test
    public void shouldReturnSquareAsFigureWithGreatestArea() {
        Figure expectedResult = figuresService.findFigureWithGreatestArea(figureList);
        assertSame(figureList.get(2), expectedResult);
    }


    @Test
    public void shouldReturnSquareWithSide50ForGreatestPerimeter() {
        Figure expectedResult = figuresService.findFigureWithGreatestPerimeterPerSelectedType(figureList, Square.class);
        assertEquals(figureList.get(2), expectedResult);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenElementInTheListIsNullForCheckGreatestPerimeter() {
        List<Figure> testList = new ArrayList<>();
        testList.add(null);
        figuresService.findFigureWithGreatestPerimeterPerSelectedType(testList, Rectangle.class);
    }


    @Test
    public void exportListToJsonShouldBeSuccessful() throws IOException {
        String filePath = "test_output.json";

        figuresService.exportListToJson(figureList, filePath);

        Mockito.verify(objectMapper, Mockito.times(1)).writeValue(new File(filePath), figureList);
    }

}