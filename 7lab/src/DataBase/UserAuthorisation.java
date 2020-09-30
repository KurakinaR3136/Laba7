package DataBase;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthorisation implements Serializable {
    public static boolean TryReg(User user, Connection connection){
        try {

            DataBaseExtaProtocol.checkUser(connection);
            String a="select  * from users where login=?";
            PreparedStatement preparedStatement=connection.prepareStatement(a);
            preparedStatement.setString(1,user.getLogin());
            ResultSet resultSet=preparedStatement.executeQuery();

            if(resultSet.next()){
                preparedStatement.close();
                resultSet.close();
                return false;
            }
            preparedStatement.close();
            resultSet.close();


            String sql="insert into users (login,password) values(?,?) ";
            PreparedStatement preparedStatement1=connection.prepareStatement(sql);
            preparedStatement1.setString(1,user.getLogin());
            preparedStatement1.setString(2,user.getPassword());
            int t=preparedStatement1.executeUpdate();
            System.out.println(t);
            return true;


        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean TryLog(User user,Connection connection){
        try
        {
            DataBaseExtaProtocol.checkUser(connection);
            //find a user with our login
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
            preparedStatement.setString(1, user.getLogin());
            System.out.println((user.getPassword()));
            //if the found user login and password match -> return true
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isResultSetEmpty = !resultSet.next();
            if(isResultSetEmpty){
                return false;
            }

            if(user.getLogin().equals(resultSet.getString("login")) && (user.getPassword()).equals(resultSet.getString("password"))) {
                return true;
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return false;

    }
    }

