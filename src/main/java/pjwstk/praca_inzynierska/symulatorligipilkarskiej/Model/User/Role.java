package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User;

public enum Role {
    ADMIN ("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");
    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
