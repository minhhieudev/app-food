package com.example.appfood;

public class UserInfo {
    private Contact contact;
    private Info info;

    // Constructor mặc định cần thiết cho Firebase
    public UserInfo() {
    }

    // Constructor với tham số
    public UserInfo(Contact contact, Info info) {
        this.contact = contact;
        this.info = info;
    }

    // Getters và Setters
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    // Lớp Contact
    public static class Contact {
        private String phoneNumber;
        private String address;
        private String email;

        // Constructor mặc định
        public Contact() {
            this.phoneNumber = "";
            this.address = "";
            this.email = "";
        }

        // Constructor với tham số
        public Contact(String phoneNumber, String address, String email) {
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.email = email;
        }

        // Getters và Setters
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // Lớp Info
    public static class Info {
        private String firstName;
        private String lastName;
        private String gender;

        // Constructor mặc định
        public Info() {
            this.firstName = "";
            this.lastName = "";
            this.gender = "male";
        }

        // Constructor với tham số
        public Info(String firstName, String lastName, String gender) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
        }

        // Getters và Setters
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
