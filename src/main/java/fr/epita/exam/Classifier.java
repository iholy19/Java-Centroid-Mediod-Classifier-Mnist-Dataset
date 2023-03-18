package fr.epita.exam;

import fr.epita.exam.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Classifier {
    private Double digit;
    private Double[][] trainMatrix;

    public Double getDigit() {
        return digit;
    }

    public void setDigit(Double digit) {
        this.digit = digit;
    }

    public Double[][] getTrainMatrix() {
        return trainMatrix;
    }

    public void setTrainMatrix(Double[][] trainMatrix) {
        this.trainMatrix = trainMatrix;
    }

    public static Map<Double, List<Double[][]>> digitPairs(List<Image> trainData){
        Map<Double,List<Double[][]>> digitList = new LinkedHashMap<>();

        for(Image image: trainData)
        {

            Double digit = image.getLabel();
            //List<Double[][]> defaultList = new ArrayList<>();
            List<Double[][]> l = digitList.getOrDefault(image.getLabel(), new ArrayList<>());
            l.add(image.getDataMatrix());
            digitList.put(image.getLabel(), l);

        }
        return digitList;
    }

    public static List<Image> getSpecificRows(Double label, Integer count,List<Image> testData){
        List<Image> labelRows = new ArrayList<>();
        Integer cnt = 0;

        for(Image image:testData){
            Double digit = image.getLabel();
            if(cnt==count){
                break;
            }
            if(digit.equals(label)){
                labelRows.add(image);
                cnt+=1;
            }
        }
        return labelRows;
    }
}
