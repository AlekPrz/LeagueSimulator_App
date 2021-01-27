package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User;

public enum Role {
    ADMIN ("ROLE_ADMIN"),
    FAN("ROLE_FAN"),
    MANAGER("ROLE_MANAGER"),
    PLAYER("ROLE_PLAYER");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
