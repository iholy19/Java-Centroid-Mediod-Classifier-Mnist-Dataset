package fr.epita.exam.services;

import fr.epita.exam.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImageCsvDAO {
    private final File csvfile;
    public ImageCsvDAO(File csvfile) throws IOException {
        this.csvfile = csvfile;
        if (!csvfile.exists()){
            try {
                Files.createFile(csvfile.toPath());
            }catch (IOException e){
                throw new IOException("unable to access persistent storage for csv service", e);
            }
        }
    }

    public List<String[]> readCSV() throws IOException{
        List<String> csvData = Files.readAllLines(csvfile.toPath());
        List<String[]> rowData = new ArrayList<>();

        if (csvData.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            System.out.println("--- The first 2 lines of the file ---");
            Integer i = 0;
            for (String row : csvData) {
                System.out.println(row);
                if (i == 1) {
                    break;
                }
                i += 1;
            }
            System.out.println();
            csvData.remove(0);

            for (String row:csvData) {
                String[] rowValues = row.split(",");
                rowData.add(rowValues);
            }
        }
        return rowData;
    }

    public List<Image> getDataMatrix(List<String[]> csvData, int size) throws IOException {
        List<Image> trainData = new ArrayList<>();

        for (String[] rowValues:csvData){
            Double[] flatArray = new Double[(rowValues.length)-1];
            Double digit = Double.parseDouble(rowValues[0]);

            for (int j=1; j< rowValues.length; j++){
                flatArray[j-1] = Double.parseDouble(rowValues[j]);
            }

            Double[][] matrix = FlatToMatrix(flatArray,size);

            Image image = new Image(digit,matrix);
            trainData.add(image);

        }
        return trainData;
    }

    public static Double[][] FlatToMatrix(Double[] flatArray, int size) throws IOException {


        Double[][] imageMatrix = new Double[size][size];

        int k=0;
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                imageMatrix[i][j] = flatArray[k];
                k+=1;
            }
        }

        return imageMatrix;
    }

    public void showMatrix(List<Image> trainData,  int rowNum) throws IOException {
        int k=1;
        System.out.println();
        Image n = trainData.get(rowNum);

        int size = n.getDataMatrix().length;
        Double[][] imageMatrix = n.getDataMatrix();

        Double val;
        System.out.print("Digit Label: ");
        System.out.println(n.getLabel());
        for (int i=0; i<size; i++) {
            for (int j = 0; j < size; j++) {
                val = imageMatrix[i][j];
                if (val > 100) {
                    System.out.print("xx");
                } else {
                    System.out.print("..");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public List<Image> getAllImages(int size) throws IOException {
        // To get all the rows from the CSV
        List<String[]> rowData = readCSV();
        // Convert the flatArray to a matrix
        List<Image> trainData = getDataMatrix(rowData,size);

        return trainData;
    }

    public Map<Double, Integer> calculateDistribution(List<Image> trainData) throws IOException {

        Map<Double, Integer> groupByLabel = new LinkedHashMap<>();
        for (Image image : trainData)
        {
            Integer count = groupByLabel.getOrDefault(image.getLabel(), 0) + 1;
            groupByLabel.put(image.getLabel(), count);
        }

        Map<Double, Integer> sortedLabel = new LinkedHashMap<>();

        groupByLabel.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedLabel.put(x.getKey(), x.getValue()));

        //System.out.println(sortedLabel);
        return sortedLabel;
    }


}
