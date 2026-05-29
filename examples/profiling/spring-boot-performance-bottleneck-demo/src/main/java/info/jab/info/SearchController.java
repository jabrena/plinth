package info.jab.info;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private UserSearchService userSearchService;

    // BAD: O(n²) - Nested loops for finding users with matching criteria
    @GetMapping("/bad/users-with-colleagues")
    public SearchResult findUsersWithColleagues(@RequestParam String department) {
        long startTime = System.currentTimeMillis();

        List<User> allUsers = userSearchService.getAllUsers();
        List<User> departmentUsers = new ArrayList<>();
        List<User> result = new ArrayList<>();
        int comparisons = 0;

        // First loop: Find users in the department - O(n)
        for (User user : allUsers) {
            comparisons++;
            if (user.department().equalsIgnoreCase(department)) {
                departmentUsers.add(user);
            }
        }

        // TERRIBLE: Nested loops to find users who have colleagues - O(n²)
        for (User user : departmentUsers) {
            for (User colleague : departmentUsers) {
                comparisons++;
                if (!user.id().equals(colleague.id()) &&
                    user.department().equals(colleague.department())) {

                    // BAD: Adding user multiple times if they have multiple colleagues
                    if (!result.contains(user)) {
                        result.add(user);
                    }
                    break; // At least one colleague found
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return new SearchResult(result, endTime - startTime, "O(n²) Nested Loops", comparisons);
    }

    // BAD: O(n²) - Cross-referencing two lists inefficiently
    @GetMapping("/bad/active-users-with-permissions")
    public SearchResult findActiveUsersWithPermissions(@RequestParam String role) {
        long startTime = System.currentTimeMillis();

        List<User> allUsers = userSearchService.getAllUsers();
        List<String> permittedRoles = userSearchService.getPermittedRoles();
        List<User> result = new ArrayList<>();
        int comparisons = 0;

        // TERRIBLE: For each user, scan through all permitted roles - O(n²)
        for (User user : allUsers) {
            if (user.active()) {
                // BAD: Linear search through roles for each user
                for (String permittedRole : permittedRoles) {
                    comparisons++;
                    if (user.role().equalsIgnoreCase(permittedRole) &&
                        user.role().toLowerCase().contains(role.toLowerCase())) {
                        result.add(user);
                        break;
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return new SearchResult(result, endTime - startTime, "O(n²) Cross-Reference", comparisons);
    }

    // BAD: O(n²) - Duplicate detection with nested loops
    @GetMapping("/bad/similar-users")
    public SearchResult findSimilarUsers(@RequestParam String keyword) {
        long startTime = System.currentTimeMillis();

        List<User> allUsers = userSearchService.getAllUsers();
        List<User> matchingUsers = new ArrayList<>();
        List<User> result = new ArrayList<>();
        int comparisons = 0;

        // First pass: Find users matching keyword - O(n)
        for (User user : allUsers) {
            comparisons++;
            if (user.name().toLowerCase().contains(keyword.toLowerCase()) ||
                user.email().toLowerCase().contains(keyword.toLowerCase())) {
                matchingUsers.add(user);
            }
        }

        // TERRIBLE: Nested loops to find similar users - O(n²)
        for (User user1 : matchingUsers) {
            for (User user2 : matchingUsers) {
                comparisons++;
                if (!user1.id().equals(user2.id()) &&
                    user1.department().equals(user2.department()) &&
                    user1.role().equals(user2.role())) {

                    // BAD: Checking if already exists with linear search
                    boolean alreadyExists = false;
                    for (User existing : result) {
                        comparisons++;
                        if (existing.id().equals(user1.id())) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    if (!alreadyExists) {
                        result.add(user1);
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return new SearchResult(result, endTime - startTime, "O(n²) Duplicate Detection", comparisons);
    }

    // BAD: O(n³) - Triple nested loops (even worse!)
    @GetMapping("/bad/team-formation")
    public SearchResult findTeamFormation(@RequestParam String department) {
        long startTime = System.currentTimeMillis();

        List<User> allUsers = userSearchService.getAllUsers();
        List<User> result = new ArrayList<>();
        int comparisons = 0;

        // HORRIBLE: Triple nested loops - O(n³)
        for (User manager : allUsers) {
            if (manager.role().equals("Manager") &&
                manager.department().equalsIgnoreCase(department)) {

                for (User developer : allUsers) {
                    if (developer.role().equals("Developer") &&
                        developer.department().equals(manager.department())) {

                        for (User tester : allUsers) {
                            comparisons++;
                            if (tester.role().equals("Tester") &&
                                tester.department().equals(manager.department()) &&
                                !manager.id().equals(developer.id()) &&
                                !developer.id().equals(tester.id()) &&
                                !manager.id().equals(tester.id())) {

                                // Found a complete team
                                if (!result.contains(manager)) result.add(manager);
                                if (!result.contains(developer)) result.add(developer);
                                if (!result.contains(tester)) result.add(tester);
                            }
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return new SearchResult(result, endTime - startTime, "O(n³) Triple Nested", comparisons);
    }
}
