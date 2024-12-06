package org.eam.code.vmixapp.model;

public class Camera {

    private int id;
    private String name;
    private int number;
    private Sequence sequence;

    public Camera(int id, String name, int number, Sequence sequence) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.sequence = sequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
}
