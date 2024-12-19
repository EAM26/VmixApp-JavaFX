package org.eam.code.vmixapp.model;

public class Scene {

    private int id;
    private int number;
    private String name;
    private String description;
    private Sequence sequence;
    private MyCamera camera;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public MyCamera getCamera() {
        return camera;
    }

    public void setCamera(MyCamera camera) {
        this.camera = camera;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sequence=" + sequence +
                ", camera=" + camera +
                '}';
    }
}
