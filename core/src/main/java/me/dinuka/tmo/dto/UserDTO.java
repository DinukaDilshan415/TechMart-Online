package me.dinuka.tmo.dto;

public class UserDTO implements java.io.Serializable{
    private int id;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private String confirmPassword;

    public UserDTO() {
    }

    public UserDTO(int id, String username, String email, String mobile, String password, String confirmPassword) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserDTO(String username, String email, String mobile, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
