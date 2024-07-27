package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

public class SpamDetector {
    /* Outline
    1. Create file lists
    2. Count how many times a word appears in a directory
    3. Calculate the probability it is spam, append these to List<TestFile>
    4. Testing

     */

    // inputs: List of all ham or spam test files, the probability map, and the real class of the files
    // outputs: List of TestFile objects
    // For each file, calculate Pr(S|F) and append it to toReturn
    public List<TestFile> makeTestFile(File[] fileList, Map<String, Double> probabilityMap, String realClass) throws IOException {
        ArrayList<TestFile> toReturn = new ArrayList<>();
        for (File email : fileList) {
            double probableSpam = probSpamFile(email, probabilityMap);
            TestFile t = new TestFile(email.getName(), probableSpam, realClass);
            toReturn.add(t);
        }
        return toReturn; // return test files
    }

    // Input: a file and the Pr(S|W) map
    // OutPut: Pr(S|F) for the file
    /* Calculate the probability of a file being spam
        by using the function from the instructions
    */
    public double probSpamFile(File file, Map<String, Double> spamMap) throws IOException {
        double n = 0;
        Set<String> wordsInFile = getIndividualWords(file);
        for (String w : wordsInFile) {
            w = w.toLowerCase();
            if (spamMap.containsKey(w)) {
                double probSpamWord = spamMap.get(w);
                if(probSpamWord > 0){
                n += Math.log(1 - probSpamWord) - Math.log(probSpamWord);
                }
            }
        }
        return 1 / (1 + Math.pow(Math.E, n));
    }

    // Inputs: The array of each train directory
    // Outputs: The map of words to the probability that a file is spam given the word i.e. Pr(S|W)
    /*
        Initializes the Pr(S|W), trainSpamFreq, and trainHamFreq maps
        Then, assign each trainFreq map the words and the number of files that contain the word
        Then, after assigning "words", calculate Pr(S|W) for each word and store it in the probabilities map
     */
    public Map<String, Double> trainModel(File[] trainHam, File[] trainSpam, File[] trainHam2) throws IOException {
        Map<String, Double> probabilities = new HashMap<>(); // Map of words to their probabilities
        Map<String, Set<File>> trainSpamFreq = new HashMap<>(); // Map of words to the # of spam files that contains the word
        Map<String, Set<File>> trainHamFreq = new HashMap<>(); // Map of words to the # of ham files that contains the word

        double hamNum = trainHam.length + trainHam2.length; // Number of ham files
        double spamNum = trainSpam.length; // Number of spam files

        // Mapping word to # of ham/spam files that contain the word
        trainHamFreq = getWordFreq(trainHam, trainHamFreq);
        trainHamFreq = getWordFreq(trainHam2, trainHamFreq);
        trainSpamFreq = getWordFreq(trainSpam, trainSpamFreq);

        // Union of the keys from both maps to have a list of all words
        Set<String> words = new HashSet<>();
        words.addAll(trainHamFreq.keySet());
        words.addAll(trainSpamFreq.keySet());

        // Calculate Pr(S|W) for each word and stores it in the probabilities map
        for (String word : words) {
            double probW_S =  trainSpamFreq.getOrDefault(word, new HashSet<>()).size() / spamNum; // Getting size because Prob(W|S) = # of spam files containing word / # of spam files
            double probW_H =  trainHamFreq.getOrDefault(word, new HashSet<>()).size() / hamNum; // Same thing about, but for ham
            if((probW_S + probW_H) == 0){
                probabilities.put(word,0.0);
            }
            else {
                double probS_W = probW_S / (probW_S + probW_H); // Pr(S|W) = Pr(W|S) / (Pr(W|S) + Pr(W|H))
                if(probS_W != 1 && probS_W != 0) { // Handling infinites when using logs in probSpamFile()
                    probabilities.put(word, probS_W);
                }
            }
        }
        return probabilities;
    }

    // Input: a file
    // Output: a set of all the individual words in the file
    // Uses a BufferedReader to read the file line by line and add each word to the set
    public HashSet<String> getIndividualWords(File file) throws IOException {
        HashSet<String> words = new HashSet<>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while((line = in.readLine()) != null){
            String[] strings = line.split(" ");
            for(String word : strings){
                if(!word.isEmpty()){
                    words.add(word);
                }
            }
        }
        return words;
    }

    // Input: an array of files, either containing the ham, ham2, or spam train files
    // Output: the map of words to the # of ham/spam files that contain the word
    public Map<String, Set<File>> getWordFreq(File[] files, Map<String, Set<File>> trainFreq) throws IOException {
        for (File file : files) {
            Set<String> words = getIndividualWords(file);
            for (String w : words) {
                w = w.toLowerCase();
                trainFreq.putIfAbsent(w, new HashSet<>()); // adds fresh set into map with new word
                trainFreq.get(w).add(file); // Adds new value (Doesn't add duplicate files because of duplicate value handling)
            }
        }
        return trainFreq;
    }

    // Input: The main directory of all data
    // Output: The list of TestFile objects
    /*
        Initializes the testFiles list
        Calculate the Pr(S|W) map using the trainModel function
        Then, using the list of files, the probability map
     and passing them in makeTestFile with the respective realClass, calculate the list of TestFiles
        Add them to a culminated list of testFiles and return
     */
    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
        //        TODO: main method of loading the directories and files, training and testing the model

        File[] testspamFiles = new File(mainDirectory, "/test/spam").listFiles();
        File[] testhamFiles = new File(mainDirectory, "/test/ham").listFiles();
        File[] trainhamFiles = new File(mainDirectory, "/train/ham").listFiles();
        File[] trainspamFiles = new File(mainDirectory, "/train/spam").listFiles();
        File[] trainham2Files = new File(mainDirectory, "/train/ham2").listFiles();

        List<TestFile> testFiles = new ArrayList<>();
        assert trainhamFiles != null;
        assert trainspamFiles != null;
        assert trainham2Files != null;
        Map<String, Double> probabilityMap = trainModel(trainhamFiles,trainspamFiles,trainham2Files );

        // Make a List of testFile objects
        assert testspamFiles != null;
        List<TestFile> spamTestFileList = makeTestFile(testspamFiles,probabilityMap , "spam");

        assert testhamFiles != null;
        List<TestFile> hamTestFileList = makeTestFile(testhamFiles, probabilityMap, "ham");

        testFiles.addAll(hamTestFileList);
        testFiles.addAll(spamTestFileList);

        return testFiles;
    }
}