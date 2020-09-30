package DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static String inputName(BufferedReader bufferedReader)throws IOException {
            System.out.println("Введите имя");
            String name=bufferedReader.readLine();
            if(name.isEmpty()){
                name=inputName(bufferedReader);
            }
            return name;
    }


    public static String inputPassword(BufferedReader bufferedReader) throws IOException{
        System.out.println("Введите пароль");
        String pass=bufferedReader.readLine();
        if(pass.isEmpty()){
            pass=inputName(bufferedReader);
        }
        return pass;
    }
}
