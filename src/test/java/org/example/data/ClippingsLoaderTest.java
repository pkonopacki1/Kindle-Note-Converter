package org.example.data;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ClippingsLoaderTest extends TestCase {
    File testFile;
    List<String> testList;
    ClippingsLoader clippingsLoader;


    public void setUp(){
        clippingsLoader = ClippingsLoader.INSTANCE;
        Path path = Paths.get("src/test/resources/Test Clippings.txt");
        loadFromPath(path);
    }

    public void testLoadFromList() {
        clippingsLoader.loadFromList(testList);
    }

    public void testLoadFromFile() {
    }

    private void loadFromPath(Path path) {

        try {
            testList = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}