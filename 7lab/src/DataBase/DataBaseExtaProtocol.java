package DataBase;

import Organization.*;
import Organization.Coordinates;
import Organization.OrganizationType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;

public class DataBaseExtaProtocol {

    public static void createOrganization(Connection connection){
        try {
            String a="create table  organization (id BIGSERIAL not null primary key ," +
                    "orgname varchar," +
                    "coordx int8 not null ,coordy float not  null ,datecreat varchar," +
                    "annualturnover  double precision not null ,fullname varchar  not null ,employeescount int8 not null ," +
                    "typeorg  varchar  not null ,street varchar not null,town varchar not null,username varchar not null )";
            System.out.println("Создаю таблицу");
            connection.createStatement().executeQuery(a);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isOrgExists(Connection connection){
        try {
            ResultSet resultSet=connection.getMetaData().getTables(null,null,"organization",null);
            if(resultSet.next()){
                return true;
            }else return false;
        }catch (Exception e){
            System.out.println("Таблица не найдена");
            System.out.println("Активирую матрицу");
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<Organization> Load(Connection connection, Statement statement){
        try {
            DataBaseExtaProtocol.CheckingForExistence(connection);
            Vector<Organization> vector=new Vector<>();
            ResultSet resultSet=statement.executeQuery("select * from organization");
            while (resultSet.next()){
                Organization organization=new Organization();
                organization.setId(resultSet.getInt("id"));
                organization.setName(resultSet.getString("orgname"));
                Integer x=resultSet.getInt("coordx");
                Float y=resultSet.getFloat("coordy");
                organization.setCoordinates(new Coordinates(x,y));

                organization.setTime(resultSet.getString("datecreat"));
                organization.setAnnualTurnover(resultSet.getDouble("annualturnover"));
                organization.setFullName(resultSet.getString("fullname"));
                organization.setEmployeesCount(resultSet.getInt("employeescount"));
                organization.setType(OrganizationType.valueOf(resultSet.getString("typeorg")));
                Address address=new Address();
                address.setStreet(resultSet.getString("street"));
                address.setTown(resultSet.getString("town"));
                organization.setOfficialAddress(address);
                User user=new User();
                user.setLogin(resultSet.getString("username"));
                organization.setUser(user);
                vector.add(organization);



            }
            return vector;
        }catch (SQLException e){
            e.printStackTrace();
        }
return null;
    }

    public static void CreateUserTable(Connection connection){
        try {


            System.out.println("Создаю таблицу с пользователями ");
            String a = "create table users(password varchar not null ,login varchar  not null )";
            connection.createStatement().executeQuery(a);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void checkUser(Connection connection){
        try {
            if (isUserExists(connection)==false){
                CreateUserTable(connection);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isUserExists(Connection connection){
        try {
            ResultSet resultSet=connection.getMetaData().getTables(null,null,"users",null);
            if(resultSet.next()){
                return  true;
            }else {
                return false;
            }
        }catch (Exception e){
            System.out.println("Таблица пользователей не найдена ");
            System.out.println("Запускаю защитный протокол");
            e.printStackTrace();
        }
        return false;
    }

    public static void CheckingForExistence(Connection connection){
        try {
            if(isOrgExists(connection)==false){
                createOrganization(connection);
            }
        }catch (Exception e){

        }
    }

}
