package pl.fitcore.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.ApiMessageResponse;
import pl.fitcore.api.dto.CheckInRequest;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.CheckInResult;

@RestController
@RequestMapping("/api/access")
public class AccessController {
    private final FitnessBackendService service;

    public AccessController(FitnessBackendService service) {
        this.service = service;
    }

    @PostMapping("/checkin")
    public ApiMessageResponse checkIn(@RequestBody CheckInRequest request) {
        String memberId = valueOrDefault(request.getMemberId(), "M-1001");
        String qrCode = valueOrDefault(request.getQrCode(), "QR-M-1001");
        CheckInResult result = service.checkIn(memberId, qrCode);
        return new ApiMessageResponse(result.isAccepted(), result.getMessage());
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
