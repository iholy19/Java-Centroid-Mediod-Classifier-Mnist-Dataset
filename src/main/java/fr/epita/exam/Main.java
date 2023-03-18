package fr.epita.exam;

import fr.epita.exam.classifiers.CentroidClassifier;
import fr.epita.exam.classifiers.MedoidClassifier;
import fr.epita.exam.services.ImageCsvDAO;
import fr.epita.exam.services.ModelPrediction;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        // PART 1
        System.out.println("\n"+"*".repeat(50)+" Train Data "+"*".repeat(50)+"\n");
        File csv1 = new File("/Users/sameeraholysheikabdullah/IdeaProjects/JavaExam_SameeraHoly/src/main/java/fr/epita/exam/data/mnist_train.csv");
        ImageCsvDAO imageCsvDAO = new ImageCsvDAO(csv1);
        List<Image> trainData = imageCsvDAO.getAllImages(28);
        // To get the image for the row number 23 - index:22
        imageCsvDAO.showMatrix(trainData,22);

        //Count of each label of the TRAIN data
        Map<Double, Integer> sortedDigitsCount = imageCsvDAO.calculateDistribution(trainData);
        //System.out.println(sortedDigitsCount);

        CentroidClassifier centroidClassifier = new CentroidClassifier(new ModelPrediction());
        //Get centroid matrix by taking average(Mean)
        centroidClassifier.trainCentroids(trainData,sortedDigitsCount);

        // PART 2
        //Test Data
        System.out.println("\n"+"*".repeat(50)+" Test Data "+"*".repeat(50)+"\n");
        File test = new File("/Users/sameeraholysheikabdullah/IdeaProjects/JavaExam_SameeraHoly/src/main/java/fr/epita/exam/data/mnist_test.csv");
        ImageCsvDAO imageCsvDAOTest = new ImageCsvDAO(test);
        List<Image> testData = imageCsvDAOTest.getAllImages(28);

        //Predicting for first 10 data for the digit 0.0
        System.out.println("--- Predicting for first 10 test data for the digit 0.0 ---");
        List<Image> testRows = Classifier.getSpecificRows(0.0,10,testData);
        List<Image> testPredictedRows = centroidClassifier.getModelPrediction().predict(testRows, centroidClassifier.getClassifier());
        centroidClassifier.getModelPrediction().printPredictionClass(testPredictedRows);

        // PART 3
        // --- Using Centroid Classifier
        System.out.println("\n"+"*".repeat(50)+" Performance for Centroid Classifier "+"*".repeat(50));
        System.out.println("\n--- Performance analysis on whole test data - Average centroids ---");
        List<Image> testPredictedDataCentroids = centroidClassifier.getModelPrediction().predict(testData, centroidClassifier.getClassifier());
        PerformanceMetrics centroidModel = new PerformanceMetrics("Average Centroids",centroidClassifier.getClassifier(),testPredictedDataCentroids);
        // Calculate confusion matrix, accuracy, precision, recall, f1score, specificity
        centroidClassifier.setPerformanceMetrics(centroidModel);
        centroidClassifier.getPerformanceMetrics().calculateAllMetrics();
        centroidClassifier.getPerformanceMetrics().printAllMetrics();

        // PART 3
        // --- Using Mediod Classifier
        System.out.println("\n"+"*".repeat(50)+" Performance for Mediod Classifier "+"*".repeat(50));
        System.out.println("\n--- Performance analysis on whole test data - Median Matrix ---");
        MedoidClassifier medoidClassifier = new MedoidClassifier(new ModelPrediction());
        medoidClassifier.trainMatrix(trainData);
        List<Image> testPredictedDataMediod = medoidClassifier.getModelPrediction().predict(testData,medoidClassifier.getClassifier());
        PerformanceMetrics medianModel = new PerformanceMetrics("Median Matrix",medoidClassifier.getClassifier(),testPredictedDataMediod);
        // Calculate confusion matrix, accuracy, precision, recall, f1score, specificity
        medoidClassifier.setPerformanceMetrics(medianModel);
        medoidClassifier.getPerformanceMetrics().calculateAllMetrics();
        medoidClassifier.getPerformanceMetrics().printAllMetrics();


    }
}