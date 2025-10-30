package by.t1.kotor.clientprocessing.model.enums;

public enum Metrics {
    CLIENT_CONTROLLER_REQUEST_COUNT("products_total");

    private final String name;

    Metrics(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
