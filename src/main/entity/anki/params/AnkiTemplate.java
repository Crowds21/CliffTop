package main.entity.anki.params;

public class AnkiTemplate {
    String Name;
    String Front;
    String Back;

    public AnkiTemplate(String name, String front, String back) {
        Name = name;
        Front = front;
        Back = back;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFront() {
        return Front;
    }

    public void setFront(String front) {
        Front = front;
    }

    public String getBack() {
        return Back;
    }

    public void setBack(String back) {
        Back = back;
    }
}
