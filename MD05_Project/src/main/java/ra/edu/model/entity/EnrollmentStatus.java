package ra.edu.model.entity;

public enum EnrollmentStatus {
    WAITING, DENIED, CANCELLED, CONFIRMED;

    public static EnrollmentStatus fromString(String value) {
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}

