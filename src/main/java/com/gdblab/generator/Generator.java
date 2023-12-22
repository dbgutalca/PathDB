package com.gdblab.generator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {
    public static void main(String[] args) {

        int totalNodos = 50;
        int totalLineas = 20000;

        // Genera los datos
        try {
            generateData("dbfull.txt", totalNodos, totalLineas);
            System.out.println("Datos generados con éxito en graph_data.txt");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al escribir el archivo.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }

    }

    private static void generateData(String fileName, int totalNodos, int totalLineas) throws IOException {
        Random random = new Random();
        Set<String> generatedLines = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            while (generatedLines.size() < totalLineas) {
                String sourceNode = "N" + (random.nextInt(totalNodos) + 1);
                String targetNode = "N" + (random.nextInt(totalNodos) + 1);
                char label = (char) ('A' + random.nextInt(26));

                String line = sourceNode + "," + label + "," + targetNode;

                if (!generatedLines.contains(line)) {
                    writer.write(line);
                    writer.newLine();
                    generatedLines.add(line);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
