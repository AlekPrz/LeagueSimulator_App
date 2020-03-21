package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model;

public enum Role {
    ADMIN ("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MANAGER("MANAGER");
    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
