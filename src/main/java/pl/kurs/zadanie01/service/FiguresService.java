package pl.kurs.zadanie01.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.zadanie01.config.ObjectMapperHolder;
import pl.kurs.zadanie01.model.Figure;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FiguresService implements Serializable {
    private ObjectMapper objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
    private List<Figure> figureList;

    public FiguresService() {
    }

    public List<Figure> getFigureList() {
        return figureList;
    }

    public void setFigureList(List<Figure> figureList) {
        this.figureList = figureList;
    }

    public Figure findFigureWithGreatestArea(List<Figure> figureList) {
        return figureList.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingDouble(Figure::calculateArea))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect details passed."));
    }

    public Figure findFigureWithGreatestPerimeterPerSelectedType(List<Figure> figureList, Class<?> type) {
        return figureList.stream()
                .filter(Objects::nonNull)
                .filter(shape -> shape.getClass().equals(type))
                .max(Comparator.comparingDouble(Figure::calculatePerimeter))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect details passed."));
    }

    public void exportListToJson(List<Figure> figureList, String filePath) throws IOException {
        FiguresService figuresService = new FiguresService();
        figuresService.setFigureList(figureList);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), figuresService);
    }

    public List<Figure> importListFromJson(String filePath) throws IOException {
        FiguresService figuresService = objectMapper.readValue(new File(filePath), FiguresService.class);
        return figuresService.getFigureList();
    }


}