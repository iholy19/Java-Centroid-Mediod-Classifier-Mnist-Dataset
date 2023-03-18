package fr.epita.exam;

public class Image {
    private Double label;
    private Double predictedDigit; //Used in case of TEST data only
    private Double[][] dataMatrix;

    public Image(Double label, Double[][] dataMatrix) {
        this.label = label;
        this.dataMatrix = dataMatrix;
    }

    public Double getLabel() {
        return label;
    }

    public Double[][] getDataMatrix() {
        return dataMatrix;
    }

    public void setPredictedDigit(Double predictedDigit) {
        this.predictedDigit = predictedDigit;
    }

    public Double getPredictedDigit() {
        return predictedDigit;
    }
}
