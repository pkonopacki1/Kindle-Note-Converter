package org.example.data;

public class KindleNote {
    private String tile;
    private String author;
    private String info;
    private String note;

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "KindleNote{" +
                "tile='" + tile + '\'' +
                ", author='" + author + '\'' +
                ", info='" + info + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
