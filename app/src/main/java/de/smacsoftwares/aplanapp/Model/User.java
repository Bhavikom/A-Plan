
package de.smacsoftwares.aplanapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Contact")
    @Expose
    private String contact;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Gender")
    @Expose
    private int gender;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("IsActive")
    @Expose
    private boolean isActive;
    @SerializedName("IsDeleted")
    @Expose
    private boolean isDeleted;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Password")
    @Expose
    private Object password;

    /**
     * 
     * @return
     *     The contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * 
     * @param contact
     *     The Contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 
     * @return
     *     The designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * 
     * @param designation
     *     The Designation
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The Gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * 
     * @param isActive
     *     The IsActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 
     * @return
     *     The isDeleted
     */
    public boolean isIsDeleted() {
        return isDeleted;
    }

    /**
     * 
     * @param isDeleted
     *     The IsDeleted
     */
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The password
     */
    public Object getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The Password
     */
    public void setPassword(Object password) {
        this.password = password;
    }

}
