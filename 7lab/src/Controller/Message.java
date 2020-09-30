package Controller;

import DataBase.User;
import Organization.Organization;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.util.Vector;

public class Message implements Serializable {
    private Crutch crutch;
    private String description;
    private Organization organization;
    private Vector<Organization> organizations;
    private User user;
    private Boolean aBoolean;

    public User getUser() {
        return user;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public Organization getOrganizationCollection() {
        return organization;
    }

    public String getDescription() {
        return description;
    }public Message(){}
    public Message(Crutch crutch,String description,Organization organization){
        this.crutch=crutch;
        this.description=description;
        this.organization=organization;
    }
    public Message(Crutch crutch,String description,Organization organization,User user){
        this.user=user;
        this.crutch=crutch;
        this.description=description;
        this.organization=organization;
    }
    public Message(Crutch crutch,String description,Organization organization,User user,Boolean aBoolean){
        this.user=user;
        this.crutch=crutch;
        this.description=description;
        this.organization=organization;
        this.aBoolean=aBoolean;
    }

    public Message(Crutch crutch,String description,Organization organization,Vector<Organization> organizations){
        this.crutch=crutch;
        this.description=description;
        this.organization=organization;
        this.organizations=organizations;
    }
    public Message(Crutch crutch, String description, Organization organization, Vector<Organization> organizations, User user){
        this.crutch=crutch;
        this.description=description;
        this.organization=organization;
        this.organizations=organizations;
        this.user=user;
    }
}
