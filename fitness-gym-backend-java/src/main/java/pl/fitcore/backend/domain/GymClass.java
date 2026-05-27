package pl.fitcore.backend.domain;

import java.time.LocalDateTime;

public class GymClass {
    private final String id;
    private final String name;
    private final String trainerId;
    private final LocalDateTime startsAt;
    private final int capacity;
    private final String level;

    public GymClass(String id, String name, String trainerId, LocalDateTime startsAt, int capacity, String level) {
        this.id = id;
        this.name = name;
        this.trainerId = trainerId;
        this.startsAt = startsAt;
        this.capacity = capacity;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLevel() {
        return level;
    }
}
