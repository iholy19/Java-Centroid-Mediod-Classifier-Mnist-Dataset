package fr.epita.exam.classifiers;

import fr.epita.exam.Classifier;
import fr.epita.exam.Image;
import fr.epita.exam.PerformanceMetrics;
import fr.epita.exam.services.ModelPrediction;

import java.util.*;

public class MedoidClassifier {
    List<Classifier> classifier;
    ModelPrediction modelPrediction;
    PerformanceMetrics performanceMetrics;
    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }
    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public MedoidClassifier(ModelPrediction modelPrediction) {
        this.modelPrediction = modelPrediction;
    }

    public ModelPrediction getModelPrediction() {
        return modelPrediction;
    }

    public List<Classifier> getClassifier() {
        return classifier;
    }

    public void trainMatrix(List<Image> trainData){
        Map<Double,List<Double[][]>> digitList = Classifier.digitPairs(trainData);
        List<Classifier> mediods = new ArrayList<>();

        for (Map.Entry<Double, List<Double[][]>> entry : digitList.entrySet()) {
            Double digit = entry.getKey();
            Classifier classifier = new Classifier();

            List<Double[][]> imageMatrix = entry.getValue();
            int len = imageMatrix.get(0).length;

            Double[][] medoidMatrix = new Double[28][28];
            for(Integer i=0;i<len;i++) {
                for (Integer j = 0; j < len; j++) {
                    medoidMatrix[i][j]=0.0;
                }
            }
            Map<String,List<Double>> flatArray = new HashMap();

            for(Double[][] image: imageMatrix){
                for(Integer i=0;i<image.length;i++){
                    for(Integer j=0;j<image[i].length;j++){
                        String key = Integer.toString(i)+","+Integer.toString(j);
                        List<Double> l = flatArray.getOrDefault(key, new ArrayList<>());
                        l.add(image[i][j]);
                        flatArray.put(key, l);
                    }
                }
            }

            for (Map.Entry<String,List<Double>> e : flatArray.entrySet()) {
                String key = e.getKey();
                int i = Integer.valueOf(key.split(",")[0]);
                int j = Integer.valueOf(key.split(",")[1]);

                List<Double> l = e.getValue();
                Collections.sort(l);
                int lenL = l.size();

                Double val=0.0;

                if (lenL%2==0){
                    int ind1 = (lenL/2);
                    int ind2 = (lenL/2)-1;
                    val = (l.get(ind1)+l.get(ind2))/2;
                }
                else{
                    int ind = (lenL+1/2)-1;
                    val = l.get(ind);
                }
                medoidMatrix[i][j] = val;
            }
            classifier.setDigit(digit);
            classifier.setTrainMatrix(medoidMatrix);
            mediods.add(classifier);
        }
        this.classifier = mediods;
    }

}
