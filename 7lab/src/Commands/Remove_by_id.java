package Commands;

import Controller.Commandable;
import Controller.Commands;
import Controller.Crutch;
import Controller.OrganizationCollection;
import DataBase.User;
import Organization.Organization;
import Server.Server;
import Server.ServerLevel;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.stream.Stream;

public class Remove_by_id extends Commands implements Commandable, Serializable, Crutch {
    OrganizationCollection collection = new OrganizationCollection();
    @Override
    public void execute(Object arg, StringBuffer msg) throws IOException {
        if (collection.getSize() == 0) System.out.println("Коллекция пустая.");
        else {
            int id = 0;
            try {
                id = Integer.parseInt((String) arg);
            } catch (NumberFormatException exp) {
                System.out.println("Нет организации с таким ID");
            }

            String ans = "Нет организации с таким id.";
            Iterator<Organization> it = (Iterator<Organization>) collection.getCollection().iterator();
            while (it.hasNext()) {
                Organization organization = (Organization) it.next();
                int humanId = (int) organization.getId();
                if (id == humanId) {
                    it.remove();
                    ans = "Организация успешно удалена.";
                }
            }
        }
    }

    public static void remove_by_id(int id, Vector<Organization> collection){

        try {
            collection.removeIf((i)->{
                return i.getId()==id;
            });

        }catch (Exception e){e.printStackTrace();}



    }

    public static String remove_by_idS(int id, Vector<Organization> collection, User users){
        String a="";
        try {
            Organization cityToRemove=null;
            for (Organization city:collection){
                if(city.getId()==id){
                    cityToRemove=city;
                }
            }

            if(cityToRemove!=null){
                if(cityToRemove.getUser().getLogin().equals(users.getLogin())){
                    collection.remove(cityToRemove);
                    DeleteElementInDatabase(cityToRemove, Server.getConnection());
                    a="Organization с id "+id+" был уничтожен";
                    return a;
                }else {
                    a="Нельзя аннигилировать ";
                    return a;
                }
            }
        }catch (Exception e){
            a="ошибощке ";

            e.printStackTrace();
            return a;

        }
        return a;

    }



    public static void DeleteElementInDatabase(Organization organization, Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM organization WHERE id=?");
            preparedStatement.setInt(1,organization.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public String getName() {
        return "remove_by_id";
    }


    @Override
    public void toInventTheWheel() {

    }
}
