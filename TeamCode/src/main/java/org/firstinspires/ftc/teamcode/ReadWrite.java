package org.firstinspires.ftc.teamcode;

/**
 * Created by epryor on 1/30/2017.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;

public class ReadWrite {
    public  String readEntireFile(File file) { // Reads the file
        String retString = "";
        if (file.exists()) {
            try {
                Scanner scan = new Scanner(file);
                scan.useDelimiter("\\Z");
                if (scan.hasNext()) {
                    retString = scan.next();
                }
                scan.close();
            } catch (FileNotFoundException ignored) {
                return "File not found for path: " + file;
            }
        }

        return retString;
    }
    public ArrayList readNstoreArrayList(String filePath, String delimiter) { // Reads the
        // file and stores it in a ArrayList
        File file = new File(filePath);
        String retString = "";
        ArrayList<String> words = new ArrayList<String>();
        words = new ArrayList<String>(50);

        if (file.exists()) {
            try {
                Scanner scan = new Scanner(file);
                scan.useDelimiter(delimiter); // this causes it to stop after each
                // space
                while (scan.hasNext()) {
                    retString = scan.next(); // This records everything between
                    // the spaces
                    words.add(retString); // this adds the string into
                    // the ArrayList
                    retString = "";// sets string to nothing, so we don't get
                    // stuff stuck in there that we don't want

                }
                scan.close();
            } catch (FileNotFoundException ignored) {
                return null;
            }
        }
        return words; // returns the element at specified
        // location
    }
    public File createFile(String filePath) { // makes a file
        File file = new File(filePath);
        if(file.exists()){
            System.out.println("File already exists");
            return null;
        }
        else{
            try {
                file.createNewFile();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
            return file;
        }
    }

    public File createFileIfNotExists(String filePath) { // makes a file
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
        return file;
    }

    public void appendToFile(String line, File file) { // adds on to file
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append(line);
            writer.close();
        } catch (IOException ignored) {
        }
    }
    public void overwriteFileWithString(String contents, File file) {
        //erases all the text in the file and writes new text
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
        } catch (IOException ignored) {
        }
    }
}