package pl.fitcore.backend.domain;

public class Trainer {
    private final String id;
    private final String name;
    private final String specialization;

    public Trainer(String id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }
}
