package insa.vulnerables.user;

public class AppUser {

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UserStatus userStatus;
    private Role role;
    private String rating;

    private static AppUser loggedInUser;

    public record Role(int roleId, String roleName) {}

    public AppUser(Long userId, String firstName, String lastName, String username, String password, UserStatus userStatus, Role role, String rating) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userStatus = userStatus;
        this.role = role;
        this.rating = rating;
    }

    public AppUser() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public static AppUser getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(AppUser loggedInUser) {
        if (AppUser.loggedInUser == null || loggedInUser == null) {
            AppUser.loggedInUser = loggedInUser;
        } else {
            throw new RuntimeException("This can only be set once!");
        }
    }
}
