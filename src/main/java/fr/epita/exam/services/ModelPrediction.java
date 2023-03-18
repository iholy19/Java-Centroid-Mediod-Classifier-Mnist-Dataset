package fr.epita.exam.services;

import fr.epita.exam.Classifier;
import fr.epita.exam.classifiers.CentroidClassifier;
import fr.epita.exam.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelPrediction {
    public List<Image> predict(List<Image> testRows, List<Classifier> centroids){

        for(Image image:testRows){
            Map<Double,Double> predictedDigit = calculateDistance(image.getDataMatrix(),centroids);
            Double minDist=null;
            Double resultDigit=null;

            for (Map.Entry<Double, Double> entry : predictedDigit.entrySet()) {
                Double label = entry.getKey();
                Double dist = entry.getValue();

                if(minDist==null){
                    minDist=dist;
                    resultDigit=label;
                }
                else{
                    if(dist<minDist){
                        minDist=dist;
                        resultDigit=label;
                    }
                }
            }
            image.setPredictedDigit(resultDigit);
        }
        return testRows;
    }

    public Map<Double,Double> calculateDistance(Double[][] testMatrix,List<Classifier> trainData){
        Map<Double,Double> distanceList = new HashMap();

        for(Classifier classifier: trainData){
            Integer len = classifier.getTrainMatrix().length;
            Double[][] centroidMatrix = classifier.getTrainMatrix();
            Double absDiff;
            Double sum = 0.0;

            for(Integer i=0;i<len;i++){
                for(Integer j=0;j<len;j++){
                    absDiff = Math.abs(centroidMatrix[i][j] - testMatrix[i][j]);
                    sum+=absDiff;
                }
            }

            Double dist = Math.sqrt(sum);
            distanceList.put(classifier.getDigit(),dist);

        }
        return distanceList;
    }

    public void printPredictionClass(List<Image> testRows){
        System.out.println();
        System.out.format("%10s%20s%10s","Label","Predicted Digit","Output");
        System.out.println();

        for(Image image: testRows){
            Boolean x;
            if (image.getLabel().equals(image.getPredictedDigit())){
                x = true;
            }
            else{
                x = false;
            }
            System.out.format("%10.1f%20.1f%10b", image.getLabel(), image.getPredictedDigit(),x);
            System.out.println();
        }
    }
}
