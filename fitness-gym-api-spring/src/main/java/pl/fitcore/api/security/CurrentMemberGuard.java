package pl.fitcore.api.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentMemberGuard {
    public String requireCurrentMember(String requestedMemberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentMemberId = authentication == null ? null : authentication.getName();
        if (currentMemberId == null || "anonymousUser".equals(currentMemberId)) {
            throw new AccessDeniedException("Brak zalogowanego uzytkownika.");
        }

        if (requestedMemberId != null
            && !requestedMemberId.trim().isEmpty()
            && !currentMemberId.equals(requestedMemberId.trim())) {
            throw new AccessDeniedException("Nie masz dostepu do danych innego uzytkownika.");
        }
        return currentMemberId;
    }
}
