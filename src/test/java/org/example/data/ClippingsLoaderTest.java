package org.example.data;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClippingsLoaderTest extends TestCase {
    File testFile;
    File testFalseFile;
    ClippingsLoader clippingsLoader;

    public void setUp(){
        clippingsLoader = ClippingsLoader.INSTANCE;
        testFile = new File(getClass().getClassLoader().getResource("Test-clippings.txt").getFile());
        testFalseFile = new File(getClass().getClassLoader().getResource("Test-false-clippings.txt").getFile());
    }

    public void testLoadFromFile() {
        try {
            clippingsLoader.loadFromFile(testFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private List<String> loadListFromPath(Path path) {
        List<String> result = new ArrayList<>();
        try {
            result = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}