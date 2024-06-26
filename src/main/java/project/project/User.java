package project.project;

public class User {
    public String name;
    public String role;
    public String password;
    public String pesel;
    public String city;
    public String address;
    public String phoneNumber;

    public User(String name, String role, String password, String pesel, String city, String address, String phoneNumber) {
        this.name = name;
        this.role = role;
        this.password = password;
        this.pesel = pesel;
        this.city = city;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public User(String name, String pesel, String password) {
        this.name = name;
        this.pesel = pesel;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
