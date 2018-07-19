package ua.nure.yaritenko.SummaryTask4.db.entity;
/**
 * User entity.
 *
 * @author V.Yaritenko
 *
 */
public class User extends Entity {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Integer bill;
    private Integer roleId;
    private Integer userLockId;

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
        this.bill = bill;
    }

    public Integer getUserLockId() {
        return userLockId;
    }

    public void setUserLockId(Integer userLockId) {
        this.userLockId = userLockId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public static User createUser(String login, String password, String firstName, String lastName, String email) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBill(0);
        user.setRoleId(2);
        user.setUserLockId(2);
        return user;
    }

    @Override
    public String toString() {
        return "User [login=" + login
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", bill=" + bill
                + ", roleId=" + roleId
                + ", userLockId=" + userLockId + "]";
    }
}
