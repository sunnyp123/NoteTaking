package com.example.sunnypariharflash.notetaking;

public class NoteData {
    String Title;
    String Note;
    String Date;
    String Time;
    String Star;

    public NoteData(String title, String note,String date,String time,String star) {
        Title = title;
        Note = note;
        Date = date;
        Time = time;
        Star = star;
           }
public NoteData(){

}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
    }
}
