package de.smacsoftwares.aplanapp.ClientResponsibleModel;

/**
 * Created by SSoft-13 on 21-12-2016.
 */

public class ClientDataSetClass {
    String name;
    String id;
    String email;
    String gendar;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGendar() {
        return gendar;
    }

    public void setGendar(String gendar) {
        this.gendar = gendar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContatct() {
        return contatct;
    }

    public void setContatct(String contatct) {
        this.contatct = contatct;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    String type;
    String contatct;
    String designation;


    public String getStrId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
