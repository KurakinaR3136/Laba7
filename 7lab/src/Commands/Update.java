package Commands;

import Controller.*;
import DataBase.DataBaseExtaProtocol;
import DataBase.User;
import Organization.Organization;
import Organization.ReadData;
import Server.Server;
import Server.ServerLevel;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

public class Update extends Commands implements Commandable, Serializable, Crutch {
    OrganizationCollection collection = new OrganizationCollection();
    public static ReadData readData = new ReadData();

    @Override
    public void execute(Object arg, StringBuffer msg) throws IOException {
        try {
            if (collection.isIndexBusy(Integer.parseInt((String) arg))) {
                Vector<Organization> organizations = collection.getCollection();
                Iterator<Organization> it = organizations.iterator();
                while (it.hasNext()) {
                    Organization organization1 = it.next();
                    if (organization1.getId() == Integer.parseInt((String) arg)) {
                        it.remove();
                        Organization organization = readData.getNewOrganization();
                        organization.setId(Integer.parseInt((String) arg));
                        collection.add(organization);
                        msg.insert(0,"\nОрганизация [id:" + arg + "] успешно обновлена.");
                        System.out.println("Организация [id:" + arg + "] успешно обновлена.");
                    }
                }
            } else{
                msg.insert(0,"\nОрганизации с таким id нет.");
                System.out.println("Организации с таким id нет.");
            }
        } catch (Exception e) {
            msg.insert(0,"\nТакого элемента нет в коллекции");
            System.out.println("Такого элемента нет в коллекции");
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public void toInventTheWheel() {

    }
    public static void update(Object arg, StringBuffer msg){
        OrganizationCollection collection = new OrganizationCollection();
        ReadData readData = new ReadData();
            if (collection.isIndexBusy(Integer.parseInt((String) arg))) {
                Vector<Organization> organizations = collection.getCollection();
                Iterator<Organization> it = organizations.iterator();
                while (it.hasNext()) {
                    Organization organization1 = it.next();
                    if (organization1.getId() == Integer.parseInt((String) arg)) {
                        it.remove();
                        Organization organization = readData.getNewOrganization();
                        organization.setId(Integer.parseInt((String) arg));
                        collection.add(organization);
                    }
                }
            }

    }


    public static Message update_id(String string,User user){
        Organization organization=readData.getNewOrganization();
        organization.setUser(user);
        Message message=new Message(new Add(),string,organization,user);
        return message;
    }

    public static String parseUpdateId(int id, Vector<Organization> collection, Organization org, User user){
        try {
            String string;
            Iterator<Organization> iterator=collection.iterator();

            if(collection.size()!=0){
                while (iterator.hasNext()){
                    Organization organization=iterator.next();
                    if(organization.getId()==id){

                        organization=org;
                        if(organization.getUser().getLogin().equals(user.getLogin())){
                            System.out.println(id);
                            UpdateElementInDatabase(organization, Server.connection,Server.statement,id);
                        }

                    }
                }
            }else {
                string="Коллекция пустая.Попробуйте добавить элемент";
                return string;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void UpdateElementInDatabase(Organization organization, Connection connection, Statement statement,int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET orgname=?, coordx=?, coordy=?, datecreat=?, annualturnover=?, fullname=?, employeescount=?, typeorg=?, street=?, town=?, username=? WHERE id=?");
            preparedStatement.setString(1, organization.getName());

            preparedStatement.setInt(2, organization.getCoordinates().getX());

            preparedStatement.setFloat(3, organization.getCoordinates().getY());

            preparedStatement.setString(4, organization.getTime());

            preparedStatement.setDouble(5, organization.getAnnualTurnover());

            preparedStatement.setString(6, organization.getFullName());

            preparedStatement.setInt(7, organization.getEmployeesCount());

            preparedStatement.setString(8, organization.getType().toString());

            preparedStatement.setString(9, organization.getOfficialAddress().getStreet());

            preparedStatement.setString(10, organization.getOfficialAddress().getTown());

            preparedStatement.setString(11,organization.getUser().getLogin());

            preparedStatement.setInt(12,id);

           int t= preparedStatement.executeUpdate();
            System.out.println(t);
            preparedStatement.close();
            Server.collection= DataBaseExtaProtocol.Load(Server.connection,Server.statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

