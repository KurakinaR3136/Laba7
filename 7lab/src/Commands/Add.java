package Commands;

import Controller.*;
import DataBase.User;
import Organization.Organization;
import Organization.ReadData;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Add extends Commands implements CommandWithoutArg, CommandWithObject, Serializable,Crutch {
    OrganizationCollection collection = new OrganizationCollection();
    public  static ReadData readData = new ReadData();
    @Override
    public boolean check(Object arg) {
        return true;
    }

    @Override
    public void execute(Object o, StringBuffer msg) throws FileNotFoundException {
		collection.add(readData.getNewOrganization());
		msg.insert(0,"\nОрганизация успешно добавлена.");
        System.out.println("Организация успешно добавлена.");
    }



    public static Message add(String string, User user){
        Organization organization=readData.getNewOrganization();
        organization.setUser(user);
        Message message=new Message(new Add(),string,organization,user);
        return message;
    }


    public static int addToData(Connection connection,Organization organization,Statement statement) throws Exception{
        try {


            PreparedStatement preparedStatement = connection.prepareStatement("insert  into organization (orgname,coordx,coordy,datecreat,annualturnover,fullname,  employeescount,typeorg,street,town,username) values (?,?,?,?,?,?,?,?,?,?,?)", RETURN_GENERATED_KEYS);
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

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new Exception("Невозможно добавить новый эллемент ");
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void toInventTheWheel() {

    }


}