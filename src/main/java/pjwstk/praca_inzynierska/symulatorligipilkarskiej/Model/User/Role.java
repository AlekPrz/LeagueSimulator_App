package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

public enum Role {
    ADMIN ("ROLE_ADMIN"),
    USER("ROLE_USER"),
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
