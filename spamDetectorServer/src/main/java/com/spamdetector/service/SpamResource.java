package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/spam")
public class SpamResource {
    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();

    // The List of TestFiles whose data we are using for frontend
    List<TestFile> testFiles;

    // Constructor, may throw IOException
    SpamResource() throws IOException {
//        TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.testFiles = this.trainAndTest(); // Abstraction of the construction
    }

    // The endpoint to get the spam results
    // Output: testFiles in a Response object as a JSON
    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object

        JSONArray data = new JSONArray();
        for(TestFile testFile : this.testFiles){
            JSONObject obj = new JSONObject();
            String spamProbRounded = String.format("%.5f", testFile.getSpamProbability() * 100);
            String spamProb = String.format("%e%n", testFile.getSpamProbability() * 100);

            obj.put("spamProbRounded",spamProbRounded);
            obj.put("fileName",testFile.getFilename());
            obj.put("spamProbability",spamProb);
            obj.put("actualClass",testFile.getActualClass());
            data.put(obj);
        }
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin","*")
                .header("Content-Type","application/json")
                .entity(data.toString())
                .build();
    }

    // The endpoint to get the accuracy
    // Calculates accuracy; # of correct guesses / # of total guesses; TruePositives + TrueNegatives / TotalFiles
    // Output: the accuracy in a Response object as a JSON
    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() { // page 4 on instructions
//      TODO: return the accuracy of the detector, return in a Response object
        double correctGuesses = 0;
        for (TestFile file : this.testFiles) {
            if (file.getActualClass().equals("spam") && file.getSpamProbability() >= 0.5) {
                correctGuesses++;
            } else if (file.getActualClass().equals("ham") && file.getSpamProbability() < 0.5) {
                correctGuesses++;
            }
        }

        Response response;
        if (this.testFiles != null) {
            double accuracy = correctGuesses/this.testFiles.size();
            response = Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Content-Type", "application/json") // Could set as another data format
                    .entity(accuracy * 100)
                    .build();
        } else {
            return Response.status(404).build();
        }
        return response;
    }

    // The endpoint to get the precision
    /* Calculates precision; # of true positives / (# of true positives + # of false positives);
    TruePositives / (TruePositives + FalsePositives) */
    // Output: the precision in a Response object as a JSON
    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() { // page 4 on instructions
        //      TODO: return the precision of the detector, return in a Response object
        double truePos = 0;
        int falsePos = 0;
        for (TestFile file : this.testFiles) {
            if (file.getActualClass().equals("spam") && file.getSpamProbability() >= 0.5) {
                truePos++;
            } else if (file.getActualClass().equals("ham") && file.getSpamProbability() >= 0.5){
                falsePos++;
            }
        }

        Response response;
        if (this.testFiles != null) {
            double precision = truePos/(truePos+falsePos);
            response = Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Content-Type", "application/json") // Could set as another data format
                    .entity(precision * 100)
                    .build();
        } else {
            return Response.status(404).build();
        }

        return response;
    }

    // The endpoint that abstract the constructor and calls trainAndTest from SpamDetector
    // Creates a File called mainDirectory and passed it into trainAndTest
    // Has exception handling
    // Output: The list of TestFiles
    private List<TestFile> trainAndTest() throws IOException {
        if (this.detector == null) {
            this.detector = new SpamDetector();
        }
        URL resource = this.getClass().getClassLoader().getResource("data"); // changed from /data
        if(resource == null){
            throw new NullPointerException("File not found!");
        }
        File mainDirectory;
        try {
            mainDirectory = new File(resource.toURI());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("File not found!");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this.detector.trainAndTest(mainDirectory);
    }
}