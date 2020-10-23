package org.example.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClippingsLoader {
    public static final ClippingsLoader INSTANCE = new ClippingsLoader();
    private Map<String, List<KindleNote>> booksNotes;

    private ClippingsLoader() {
        booksNotes = new HashMap<>();
    }

    /**
     * Load clipping from file
     */
    // TODO: 23.10.2020 Prevent from adding same notes
    public void loadClippingsFromFile(File file) throws FileNotFoundException {
        List<String> loadedArray = loadArrayFromFile(file);
        validateUserInput(loadedArray);
        convertArrayToKindleNotes(loadedArray);
    }

    public List<String> getBooksTitles() {
        return new ArrayList<>(booksNotes.keySet());
    }

    public List<KindleNote> getBookNotes(String title) {
        return booksNotes.get(title);
    }

    public void saveNotes(String title) {
        Path file = Paths.get(title.concat(".txt"));

        try(BufferedWriter bw = Files.newBufferedWriter(file)) {
            bw.write(title.concat("\n"));
            bw.write(booksNotes.get(title).get(0).getAuthor().concat("\n"));

            for (KindleNote s : booksNotes.get(title)) {
                bw.write(s.getInfo().concat("\n"));
                bw.write(s.getNote().concat("\n\n"));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private List<String> loadArrayFromFile(File file) {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                result.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Validates the Array extracted from file provided by user
     */
    private void validateUserInput(List<String> userArray) throws FileNotFoundException{
        if(userArray.size() < 5) throw new FileNotFoundException();
        for (String line: userArray) {
            if(line.equals("==========")) return;
        }
        throw new FileNotFoundException();
    }

    /**
     * Separate notes from MyClippings file to list of KindleNote objects
     */
    private void convertArrayToKindleNotes(List<String> arrayNotes) {
        List<String> note = new ArrayList<>();

        for (int i = 0; i < arrayNotes.size() - 1; i++) {
            if (!arrayNotes.get(i).equals("==========")) {
                note.add(arrayNotes.get(i));
            } else {
                addNote(note);
                note.clear();
            }
        }
    }

    /**
     * Adds new note to KindleNotes list from given 4 lines of text
     */
    private void addNote(List<String> lines) {
        KindleNote note = new KindleNote();
        String title;
        String author;

        // Note verification
        if (lines.size() != 4) return;

        // If ')' char is on last place, it means the book has given author
        if (lines.get(0).lastIndexOf(')') == lines.get(0).length() - 1) {
            title = lines.get(0).substring(0, lines.get(0).lastIndexOf('(') - 1);
            author = lines.get(0).substring(lines.get(0).lastIndexOf('(') + 1, lines.get(0).length() - 1);
        } else {
            title = lines.get(0);
            author = "";
        }

        note.setTile(removeUTF8BOM(title));
        note.setAuthor(removeUTF8BOM(author));
        note.setInfo(removeUTF8BOM(lines.get(1)));
        note.setNote(removeUTF8BOM(lines.get(3)));

        if (booksNotes.containsKey(note.getTile())) {
            booksNotes.get(note.getTile()).add(note);
        } else {
            List<KindleNote> newNotes = new ArrayList<>();
            newNotes.add(note);
            booksNotes.put(note.getTile(), newNotes);
        }
    }

    /**
     * Removes BOM character from the text beginning
     */
    private String removeUTF8BOM(String s) {
        String UTF8_BOM = "\uFEFF";
        if (s.startsWith(UTF8_BOM)) {
            s = s.replace(UTF8_BOM,"");
        }
        return s;
    }
}
