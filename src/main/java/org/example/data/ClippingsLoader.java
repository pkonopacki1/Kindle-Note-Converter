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
    private List<String> loadedFile;
    private Map<String, List<KindleNote>> booksNotes;
    private final String UTF8_BOM = "\uFEFF";

    private ClippingsLoader() {
        loadedFile = new ArrayList<>();
        booksNotes = new HashMap<>();
    }

    /**
     * Load clippings from file represented as a string list
     * @param notesList
     */
    public void loadFromList(List<String> notesList) {
        loadedFile = notesList;
        extractNotes();
    }

    /**
     * Load clipping from file
     */
    public void loadFromFile(File file) {
        List<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                result.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadedFile = result;
    }

    /**
     * Separate notes from MyClippings file to list of KindleNote objects
     */
    private void extractNotes() {
        List<List<String>> seperatedNotes = new ArrayList<>();
        List<String> note = new ArrayList<>();

        for (int i = 0; i < loadedFile.size() - 1; i++) {
            if (!loadedFile.get(i).equals("==========")) {
                note.add(loadedFile.get(i));
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
        if (s.startsWith(UTF8_BOM)) {
            s = s.replace(UTF8_BOM,"");
        }
        return s;
    }

    public List<String> returnBooksTitles() {
        return new ArrayList<>(booksNotes.keySet());
    }

    public List<KindleNote> returnNotes(String title) {
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
}
