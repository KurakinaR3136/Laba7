package Commands;

import Controller.CommandWithoutArg;
import Controller.Commands;
import Controller.OrganizationCollection;
import DataBase.User;
import Organization.Organization;
import Server.Server;
import Server.ServerLevel;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

public class Clear extends Commands implements CommandWithoutArg, Serializable {
	OrganizationCollection collection = new OrganizationCollection();

	@Override
	public void execute(Object o, StringBuffer msg) throws FileNotFoundException {
		collection.clear();
		msg.insert(0,"\nКоллекция успешно очищена.");
		System.out.println("Коллекция успешно очищена.");
	}

	public static void DeleteElementInDatabase(Organization organization, Connection connection){
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM organization WHERE username=?");
			preparedStatement.setString(1,organization.getUser().getLogin());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void clearData(User user, Vector<Organization> organizationVector){
	Organization organization= new Organization();
	for(Organization org:organizationVector){
		if(org.getUser().getLogin().equals(user.getLogin())){
			DeleteElementInDatabase(org, Server.connection);
		}

	}
	}


	@Override
	public String getName() {
		return "clear";
	}
}