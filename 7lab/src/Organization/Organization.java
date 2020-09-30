package Organization;

import Controller.Commands;
import DataBase.User;

import java.awt.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Organization implements Comparable<Organization>, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double annualTurnover; //Значение поля должно быть больше 0
    private String fullName; //Значение этого поля должно быть уникальным, Поле может быть null
    private int employeesCount; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле не может быть null
    private User user;
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization() {
        id = Commands.id + 1;
        Commands.id += 1;
//        double num;
//        num = Math.random() * 141400;
//        id = (int) num;
//        creationDate = ZonedDateTime.now();
    }

    public int getEmployeesCount() {
        return employeesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setAnnualTurnover(double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public double getAnnualTurnover() {
        return annualTurnover;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreationDate() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        this.creationDate = dateTime;
    }
    public void setTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        ZonedDateTime dateTime = ZonedDateTime.now();
        String format=dateTime.format(formatter);
        this.time=format;
    }

    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public String getInfo() {
        return "Организация [" + id + "]:\n\tНазвание:" + name + "\n\tКоординаты:\n\t\tx: " + coordinates.getX() +
                "\n\t\ty: " + coordinates.getY() + "\n\tДата создания: " + time + "\n\tГодовой оборот: " +
                annualTurnover + "\n\tПолное название: " + fullName + "\n\tКоличество владельцев: " + employeesCount +
                "\n\tТип организации: " + type + "\n\tАдрес: " + "\n\t\tГород: " + officialAddress.getTown() +
                "\n\t\tУлица: " + officialAddress.getStreet();
    }

    @Override
    public String toString() {
        return "name = '" + name +
                ",' id = " + id +
                ", coordinates = X: " + coordinates.getX() + " Y: " + coordinates.getY() +
                ", annualTurnover =" + annualTurnover +
                ", fullName ='" + fullName + '\'' +
                ", employeesCount = " + employeesCount + " .";
    }

    @Override
    public int compareTo(Organization organization) {
        return this.getInfo().length() - organization.getInfo().length();
    }
}
