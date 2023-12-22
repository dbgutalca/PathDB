package com.gdblab.tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Tester {

    public static void main(String[] args) {

        System.out.print("\033[H\033[2J");
        System.out.flush();


        String URLFile1 = "alg85k.txt";
        ArrayList<String> configA = new ArrayList<>();
        ArrayList<String> pathsA = new ArrayList<>();
        String URLFile2 = "dfs85k.txt";
        ArrayList<String> configB = new ArrayList<>();
        ArrayList<String> pathsB = new ArrayList<>();
        
        loadFileA(URLFile1, configA, pathsA);
        loadFileB(URLFile2, configB, pathsB);
        System.out.println("Algebra Time: " + ANSI_YELLOW + configA.get(4) + ANSI_RESET + " ms");
        System.out.println("DFS Time: " + ANSI_YELLOW + configB.get(4) + ANSI_RESET + " ms");
        boolean res = runCheck(pathsA, pathsB, configA, configB);
        if(res) {
            System.out.println(ANSI_GREEN + "Test Approved" + ANSI_RESET + " -" + ANSI_ITALIC + configA.get(3).split(":")[1] + " -" + configA.get(2).split(":")[1] + ANSI_RESET);
        }
        else {
            System.out.println(ANSI_RED + "Test Failed" + ANSI_RESET + " -" + ANSI_ITALIC + configA.get(3).split(":")[1] + " -" + configA.get(2).split(":")[1] + ANSI_RESET);
        }
        System.out.println();

        // int j = 1;
        // int approved = 0;
        // int failed = 0;

        // for (int i = 1; i <= 11; i++) {
        //     System.out.println(ANSI_BOLD + ANSI_CYAN + "TEST " + j + ANSI_RESET + " ");
        //     String URLFile1 = "tests/1_a_alg_" + i + ".txt";
        //     ArrayList<String> configA = new ArrayList<>();
        //     ArrayList<String> pathsA = new ArrayList<>();
        //     String URLFile2 = "tests/1_a_dfs_" + i + ".txt";
        //     ArrayList<String> configB = new ArrayList<>();
        //     ArrayList<String> pathsB = new ArrayList<>();
            
        //     loadFileA(URLFile1, configA, pathsA);
        //     loadFileB(URLFile2, configB, pathsB);
        //     System.out.println("Algebra Time: " + ANSI_YELLOW + configA.get(4) + ANSI_RESET + " ms");
        //     System.out.println("DFS Time: " + ANSI_YELLOW + configB.get(4) + ANSI_RESET + " ms");
        //     boolean res = runCheck(pathsA, pathsB, configA, configB);
        //     if(res) {
        //         approved++;
        //     }
        //     else {
        //         failed++;
        //     }
        //     System.out.println();
        //     j++;
        // }

        // for (int i = 1; i <= 11; i++) {
        //     System.out.println(ANSI_BOLD + ANSI_CYAN + "Test " + j + ANSI_RESET + " ");
        //     String URLFile1 = "tests/2_sp_alg_" + i + ".txt";
        //     ArrayList<String> configA = new ArrayList<>();
        //     ArrayList<String> pathsA = new ArrayList<>();
        //     String URLFile2 = "tests/2_sp_dfs_" + i + ".txt";
        //     ArrayList<String> configB = new ArrayList<>();
        //     ArrayList<String> pathsB = new ArrayList<>();
            
        //     loadFileA(URLFile1, configA, pathsA);
        //     loadFileB(URLFile2, configB, pathsB);
        //     System.out.println("Algebra Time: " + ANSI_YELLOW + configA.get(4) + ANSI_RESET + " ms");
        //     System.out.println("DFS Time: " + ANSI_YELLOW + configB.get(4) + ANSI_RESET + " ms");
        //     boolean res = runCheck(pathsA, pathsB, configA, configB);
        //     if(res) {
        //         approved++;
        //     }
        //     else {
        //         failed++;
        //     }
        //     System.out.println();
        //     j++;
        // }

        // for (int i = 1; i <= 11; i++) {
        //     System.out.println(ANSI_BOLD + ANSI_CYAN + "Test " + j + ANSI_RESET + " ");
        //     String URLFile1 = "tests/3_t_alg_" + i + ".txt";
        //     ArrayList<String> configA = new ArrayList<>();
        //     ArrayList<String> pathsA = new ArrayList<>();
        //     String URLFile2 = "tests/3_t_dfs_" + i + ".txt";
        //     ArrayList<String> configB = new ArrayList<>();
        //     ArrayList<String> pathsB = new ArrayList<>();
            
        //     loadFileA(URLFile1, configA, pathsA);
        //     loadFileB(URLFile2, configB, pathsB);
        //     System.out.println("Algebra Time: " + ANSI_YELLOW + configA.get(4) + ANSI_RESET + " ms");
        //     System.out.println("DFS Time: " + ANSI_YELLOW + configB.get(4) + ANSI_RESET + " ms");
        //     boolean res = runCheck(pathsA, pathsB, configA, configB);
        //     if(res) {
        //         approved++;
        //     }
        //     else {
        //         failed++;
        //     }
        //     System.out.println();
        //     j++;
        // }

        // System.out.println();
        // System.out.println(ANSI_BOLD + ANSI_GREEN + "TESTS APPROVED: " + approved + ANSI_RESET);
        // System.out.println(ANSI_BOLD + ANSI_RED + "TESTS FAILED: " + failed + ANSI_RESET);
        // System.out.println(ANSI_BOLD + ANSI_GREEN + "PERCENT APPROVED: " + (approved * 100) / (approved + failed) + "%" + ANSI_RESET);
    }

    private static void loadFileA(String file, ArrayList<String> configA, ArrayList<String> pathsA) {
        boolean configFlag = false;
        boolean pathsFlag = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.equals("--Configuration--") && !configFlag) {
                    configFlag = true;
                    pathsFlag = false;
                    continue;
                }
                else if(line.equals("--Configuration--") && configFlag) {
                    configFlag = false;
                    pathsFlag = true;
                    continue;
                }
                else if(configFlag) {
                    configA.add(line);
                    continue;
                }
                if(line.equals("--Paths--") && !pathsFlag) {
                    configFlag = false;
                    pathsFlag = true;
                    continue;
                }
                else if(line.equals("--Paths--") && pathsFlag) {
                    configFlag = false;
                    pathsFlag = false;
                    continue;
                }
                else {
                    pathsA.add(line);
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFileB(String file, ArrayList<String> configB, ArrayList<String> pathsB) {
        boolean configFlag = false;
        boolean pathsFlag = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.equals("--Configuration--") && !configFlag) {
                    configFlag = true;
                    pathsFlag = false;
                    continue;
                }
                else if(line.equals("--Configuration--") && configFlag) {
                    configFlag = false;
                    pathsFlag = true;
                    continue;
                }
                else if(configFlag) {
                    configB.add(line);
                    continue;
                }
                if(line.equals("--Paths--") && !pathsFlag) {
                    configFlag = false;
                    pathsFlag = true;
                    continue;
                }
                else if(line.equals("--Paths--") && pathsFlag) {
                    configFlag = false;
                    pathsFlag = false;
                    continue;
                }
                else {
                    pathsB.add(line);
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean runCheck(ArrayList<String> pathsA, ArrayList<String> pathsB, ArrayList<String> configA, ArrayList<String> configB) {
        int approved = 0;
        int failed = 0;

        // Sort both list
        Collections.sort(pathsA, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(pathsB, String.CASE_INSENSITIVE_ORDER);

        // // print config A and B
        // for (String lineString : configA) {
        //     System.out.println(ANSI_YELLOW + lineString + ANSI_RESET);
        // }

        // for (String lineString : configB) {
        //     System.out.println(ANSI_YELLOW + lineString + ANSI_RESET);
        // }
        // System.out.println(

        // );

        if(pathsA.size() == pathsB.size()) {
            // System.out.println(ANSI_GREEN + "Size Test: Approved" + ANSI_RESET);
            for(int i = 0; i < pathsA.size(); i++) {
                String pathA = pathsA.get(i).replaceAll("\\s+", " ").trim();
                String pathB = pathsB.get(i).replaceAll("\\s+", " ").trim();
                if(pathA.equals(pathB)) {
                    approved++;
                }
                else {
                    failed++;
                }
            }

            // System.out.println(ANSI_GREEN + "Paths Approved: " + approved + ANSI_RESET);
            // System.out.println(ANSI_RED + "Paths Failed: " + failed + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Test Approved" + ANSI_RESET + " -" + ANSI_ITALIC + configA.get(3).split(":")[1] + " -" + configA.get(2).split(":")[1] + ANSI_RESET);
            return true;
        }
        else {
            // System.out.println(ANSI_RED + "Size Test: Failed" + ANSI_RESET);
            // System.out.println(ANSI_RED + "Size A: " + pathsA.size() + " items" + ANSI_RESET);
            // System.out.println(ANSI_RED + "Size B: " + pathsB.size() + " items" + ANSI_RESET);
            System.out.println(ANSI_RED + "Test Failed" + ANSI_RESET + " -" + ANSI_ITALIC + configA.get(3).split(":")[1] + " -" + configA.get(2).split(":")[1] + ANSI_RESET);
            return false;
        }
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_ITALIC = "\u001B[3m";
}
