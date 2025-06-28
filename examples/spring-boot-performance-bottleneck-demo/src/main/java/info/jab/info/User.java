package info.jab.info;

import java.time.LocalDateTime;

record User(
    Long id,
    String name,
    String email,
    String department,
    String role,
    LocalDateTime lastLogin,
    boolean active
) {

    // Alternative constructor that sets lastLogin to a random past date
    public User(Long id, String name, String email, String department, String role, boolean active) {
        this(id, name, email, department, role,
             LocalDateTime.now().minusDays((long)(Math.random() * 365)), active);
    }
}
