package pl.fitcore.backend.domain;

public class Member {
    private final String id;
    private final String name;
    private final String email;
    private final String qrCode;

    public Member(String id, String name, String email, String qrCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.qrCode = qrCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getQrCode() {
        return qrCode;
    }
}
