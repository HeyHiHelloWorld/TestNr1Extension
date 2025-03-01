package pl.kurs.zadanie01;

import pl.kurs.zadanie01.model.Circle;
import pl.kurs.zadanie01.model.Figure;
import pl.kurs.zadanie01.model.Rectangle;
import pl.kurs.zadanie01.model.Square;
import pl.kurs.zadanie01.service.FiguresService;
import pl.kurs.zadanie01.service.ShapeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FiguresRunner {
    public static void main(String[] args) throws IOException {

        ShapeFactory shapeFactory = ShapeFactory.getInstance();
        FiguresService fs = new FiguresService();
        Circle c1 = shapeFactory.createCircle(10);
        Circle c2 = shapeFactory.createCircle(10);
        Circle c3 = shapeFactory.createCircle(11);
        System.out.println(c1 == c2);

        Rectangle r1 = shapeFactory.createRectangle(10, 200);
        Rectangle r2 = shapeFactory.createRectangle(10, 10);

        Square s1 = shapeFactory.createSquare(6);

        System.out.println(s1.calculateArea());
        System.out.println(c2.calculateArea());
        System.out.println(r2.calculatePerimeter());

        List<Figure> figureList = List.of(c1, c2, c3, r1, r2, s1);

        System.out.println(fs.findFigureWithGreatestArea(figureList));
        System.out.println(fs.findFigureWithGreatestPerimeterPerSelectedType(figureList, Circle.class));

//        fs.exportListToJson(figureList, "test10.json");
        System.out.println(fs.importListFromJson("test10.json"));

    }
}
