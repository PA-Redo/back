package fr.croixrouge.exposition.dto.core;

public class Donation {

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCivility() {
        return civility;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String amount;
    private String email;
    private String firstName;
    private String lastName;
    private String civility;
    private String address;
    private String city;
    private String zipCode;
    private String country;


    public Donation() {
    }

    public Donation(String amount, String email, String firstName, String lastName, String civility, String address, String city, String zipCode, String country) {
        this.amount = amount;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.civility = civility;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }
}
