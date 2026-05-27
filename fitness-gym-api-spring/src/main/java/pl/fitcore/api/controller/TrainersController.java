package pl.fitcore.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.Trainer;

@RestController
@RequestMapping("/api/trainers")
public class TrainersController {
    private final FitnessBackendService service;

    public TrainersController(FitnessBackendService service) {
        this.service = service;
    }

    @GetMapping
    public List<Trainer> trainers() {
        return service.trainers();
    }
}
