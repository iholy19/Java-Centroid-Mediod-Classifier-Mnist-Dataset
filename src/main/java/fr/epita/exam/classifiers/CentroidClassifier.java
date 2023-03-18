package fr.epita.exam.classifiers;

import fr.epita.exam.Classifier;
import fr.epita.exam.Image;
import fr.epita.exam.PerformanceMetrics;
import fr.epita.exam.services.ModelPrediction;

import java.util.*;

public class CentroidClassifier {
    List<Classifier> classifier;
    ModelPrediction modelPrediction;
    PerformanceMetrics performanceMetrics;

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public CentroidClassifier(ModelPrediction modelPrediction) {
        this.modelPrediction = modelPrediction;
    }

    public ModelPrediction getModelPrediction() {
        return modelPrediction;
    }

    public List<Classifier> getClassifier() {
        return classifier;
    }

    public void trainCentroids(List<Image> trainData, Map<Double, Integer> sortedDigitsCount){
        Map<Double,List<Double[][]>> digitList = Classifier.digitPairs(trainData);
        List<Classifier> centroids = new ArrayList<>();

        for (Map.Entry<Double, List<Double[][]>> entry : digitList.entrySet()) {
            Double digit = entry.getKey();
            Classifier classifier = new Classifier();

            List<Double[][]> imageMatrix = entry.getValue();
            Double[][] centroidMatrix = new Double[28][28];

            for(Double[][] image: imageMatrix){
                for(Integer i=0;i<image.length;i++){
                    for(Integer j=0;j<image[i].length;j++){
                        if (centroidMatrix[i][j]==null){
                            centroidMatrix[i][j]=0.0;
                        }
                        centroidMatrix[i][j] = centroidMatrix[i][j]+image[i][j];
                    }
                }
            }
            Integer digitCount = sortedDigitsCount.get(digit);

            for(Integer i=0;i<28;i++){
                for(Integer j=0;j<28;j++){
                    centroidMatrix[i][j] = centroidMatrix[i][j]/digitCount;
                }
            }
            classifier.setDigit(digit);
            classifier.setTrainMatrix(centroidMatrix);
            centroids.add(classifier);
        }
        this.classifier = centroids;
    }

}
