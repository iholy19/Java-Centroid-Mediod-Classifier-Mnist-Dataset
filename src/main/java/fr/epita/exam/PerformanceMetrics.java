package fr.epita.exam;

import fr.epita.exam.classifiers.CentroidClassifier;

import java.util.List;

public class PerformanceMetrics {
    private String modelName;
    private List<Classifier> trainDataCentroids;
    private List<Image> testPredictionData;
    private Integer[][] confusionMatrix;
    private Integer[][] metrics; //To store TP, TN, FP, FN for each class
    private Double accuracy;
    private Double[] precision;
    private Double[] recall;
    private Double[] f1score;
    private Double[] specificity;

    public PerformanceMetrics(String modelName, List<Classifier> trainDataCentroids, List<Image> testPredictionData) {
        this.modelName = modelName;
        this.trainDataCentroids = trainDataCentroids;
        this.testPredictionData = testPredictionData;

        Integer len = trainDataCentroids.size();
        this.confusionMatrix = new Integer[len][len];
        this.precision = new Double[len];
        this.f1score = new Double[len];
        this.recall = new Double[len];
        this.specificity = new Double[len];

        for(int i=0;i<len;i++){
            this.precision[i] = 0.0;
            this.f1score[i] = 0.0;
            this.recall[i] = 0.0;
            this.specificity[i] = 0.0;

            for(int j=0;j<len;j++){
                this.confusionMatrix[i][j]=0;
            }
        }

        this.metrics = new Integer[len][4];

        for(int i=0;i<len;i++){
            for(int j=0;j<4;j++){
                this.metrics[i][j]=0;
            }
        }

    }

    public String getModelName() {
        return modelName;
    }

    public List<Classifier> getTrainDataCentroids() {
        return trainDataCentroids;
    }

    public List<Image> getTestPredictionData() {
        return testPredictionData;
    }

    public Integer[][] getConfusionMatrix() {
        return confusionMatrix;
    }

    public Integer[][] getMetrics() {
        return metrics;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Double[] getPrecision() {
        return precision;
    }

    public Double[] getRecall() {
        return recall;
    }

    public Double[] getF1score() {
        return f1score;
    }

    public Double[] getSpecificity() {
        return specificity;
    }

    public void setSpecificity(Double[] specificity) {
        this.specificity = specificity;
    }

    public void setMetrics(Integer[][] metrics) {
        this.metrics = metrics;
    }

    public void setConfusionMatrix(Integer[][] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setPrecision(Double[] precision) {
        this.precision = precision;
    }

    public void setRecall(Double[] recall) {
        this.recall = recall;
    }

    public void setF1score(Double[] f1score) {
        this.f1score = f1score;
    }

    public void updateConfusionMatrix(){
        List<Image> testData = this.testPredictionData;
        Integer[][] confusionMatrix = this.getConfusionMatrix();
        for(Image image: testData){
            int actualLabel = (image.getLabel()).intValue();
            int predictedLabel = (image.getPredictedDigit()).intValue();
            confusionMatrix[actualLabel][predictedLabel] += 1;
        }
        setConfusionMatrix(confusionMatrix);
    }

    public void printConfusionMatrix(){
        System.out.println();
        Integer[][] confusionMatrix = this.confusionMatrix;
        System.out.format("%50s","CONFUSION MATRIX (ACTUAL X PREDICTED)\n");
        int len = confusionMatrix.length;

        System.out.format("%4s"," ".repeat(4));
        for(int i=0;i<len;i++){
            System.out.format("%2s%4d","|",i);
        }
        System.out.println();
        System.out.format("%66s","-".repeat(66));
        System.out.println();

        for(int i=0;i<len;i++){
            System.out.format("%4d%2s",i,"|");
            for(int j=0;j<len;j++){
                System.out.format("%6d",confusionMatrix[i][j]);
            }
            System.out.println();
        }
    }

    public void calculateMetrics(){
        Integer[][] confusionMatrix = this.confusionMatrix;
        int len = confusionMatrix.length;
        Integer[][] metrics = this.metrics;

        // TP: True Positive
        for(int i=0; i<len; i++){
            metrics[i][0] = confusionMatrix[i][i];
        }

        // TN: True Negative
        for(int label=0; label<len; label++){
            for(int i=0; i<len;i++){
                for(int j=0; j<len;j++){
                    if ((label!=i) & (label!=j)){
                        metrics[label][1] += confusionMatrix[i][j];
                    }
                }
            }
        }

        // FP: False Positive
        for(int label=0; label<len; label++) {
            for (int i = 0; i < len; i++) {
                if (i!=label){
                    metrics[label][2] += confusionMatrix[i][label];
                }
            }
        }

        // FP: False Negative
        for(int label=0; label<len; label++) {
            for (int i = 0; i < len; i++) {
                if (i!=label){
                    metrics[label][3] += confusionMatrix[label][i];
                }
            }
        }
        setMetrics(metrics);
    }

    public void calculateAccuracy(){
        Integer[][] metrics = getMetrics();
        Integer TP = 0;
        Integer TN = 0;
        Integer FP = 0;
        Integer FN = 0;
        Double accuracy;

        for(int label=0; label<metrics.length; label++) {
            TP += metrics[label][0];
            TN += metrics[label][1];
            FP += metrics[label][2];
            FN += metrics[label][3];
        }

        accuracy = (Double.valueOf(TP+TN))/(Double.valueOf(TP+TN+FP+FN));
        setAccuracy(accuracy*100);
    }

    public void calculatePerformanceValues(){
        Integer[][] metrics = getMetrics();
        Integer TP = 0;
        Integer TN = 0;
        Integer FP = 0;
        Integer FN = 0;

        Double preci, rec, f1sc, speci;
        Double[] precision = getPrecision();
        Double[] recall = getRecall();
        Double[] f1score = getF1score();
        Double[] specificity = getSpecificity();

        for(int label=0; label<metrics.length; label++) {
            TP = metrics[label][0];
            TN = metrics[label][1];
            FP = metrics[label][2];
            FN = metrics[label][3];

            preci = Double.valueOf(TP)/Double.valueOf(TP+FP);
            precision[label] = preci;

            rec = Double.valueOf(TP)/Double.valueOf(TP+FN);
            recall[label] = rec;

            speci = Double.valueOf(TN)/Double.valueOf(TN+FP);
            specificity[label] = speci;

            f1sc = (preci*rec)/(preci+rec);
            f1score[label] = 2*f1sc;

        }
        this.specificity = specificity;
        this.f1score = f1score;
        this.precision = precision;
        this.recall = recall;
    }

    public void calculateAllMetrics(){
        updateConfusionMatrix();
        calculateMetrics();
        calculateAccuracy();
        calculatePerformanceValues();
    }

    public void printAllMetrics(){
        printConfusionMatrix();
        System.out.format("\n--- The accuracy of the model (%s) is %.2f %s\n",getModelName(),getAccuracy(),"%");
        Double[] precision = getPrecision();
        Double[] recall = getRecall();
        Double[] f1score = getF1score();
        Double[] specificity = getSpecificity();

        System.out.format("\n%15s%15s%15s%15s%15s\n","Label","Precision","Recall","Specificity","F1-Score");
        for (int i=0; i<precision.length; i++){
            System.out.format("%15d%15.2f%15.2f%15.2f%15.2f\n",i,precision[i],recall[i],specificity[i],f1score[i]);
        }
    }
}
