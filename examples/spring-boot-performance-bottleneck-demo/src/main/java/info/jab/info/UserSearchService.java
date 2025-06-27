package info.jab.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserSearchService {

    private List<User> users;
    private List<String> permittedRoles;

    public UserSearchService() {
        initializeData();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<String> getPermittedRoles() {
        return new ArrayList<>(permittedRoles);
    }

    private void initializeData() {
        // Initialize with sample data
        users = new ArrayList<>();
        String[] names = {"Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince",
                         "Eve Wilson", "Frank Miller", "Grace Lee", "Henry Davis",
                         "Ivy Chen", "Jack Taylor", "Karen White", "Leo Martinez"};
        String[] departments = {"Engineering", "Marketing", "Sales", "HR", "Finance"};
        String[] roles = {"Manager", "Developer", "Tester", "Analyst", "Coordinator"};

        // Create 1000 users for performance testing
        for (int i = 0; i < 1000; i++) {
            users.add(new User(
                (long) i,
                names[i % names.length] + " " + i,
                names[i % names.length].toLowerCase().replace(" ", ".") + i + "@company.com",
                departments[i % departments.length],
                roles[i % roles.length],
                Math.random() > 0.1 // 90% active users
            ));
        }

        permittedRoles = Arrays.asList("Manager", "Developer", "Tester", "Analyst");
    }
}
