package Commands;

import Controller.Commandable;
import Controller.Commands;
import Controller.OrganizationCollection;
import DataBase.User;
import Organization.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Vector;

public class Replace_if_lower extends Commands implements Commandable, Serializable {
    OrganizationCollection collection = new OrganizationCollection();
    ReadData readData = new ReadData();
    @Override
    public void execute(Object o, StringBuffer msg) throws IOException {
        try {

            //Проверка на возмыжные варианты
            if (o.equals("x") || o.equals("y") || o.equals("employeesCount") || o.equals("annualTurnover")) {

                //Считывание значения, больше кторого надо удалить
                System.out.print("Введите значение, меньше которого обновить объекты:  ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String value = reader.readLine();
//                reader.close();

                //Получение коллекции
                Vector<Organization> organizations = collection.getCollection();

                //Перебор коллекции, сравнение и замена значений
                Iterator<Organization> it = organizations.iterator();
                while (it.hasNext()) {
                    Organization organization = it.next();
                    //Свитч для сравнения
                    switch ((String) o) {
                        case ("x"):
                            if (organization.getCoordinates().getX() < Integer.parseInt(value)) {
                                System.out.println("Введите на какое значение вы хотите заменить X организации: \n" + organization.toString());
                                organization.getCoordinates().setX(Integer.parseInt(reader.readLine()));
                            } break;
                        case ("y"):
                            if (organization.getCoordinates().getY() < Double.parseDouble(value)) {
                                System.out.println("Введите на какое значение вы хотите заменить Y организации: \n" + organization.toString());
                                organization.getCoordinates().setY(Float.parseFloat(reader.readLine()));
                            } break;
                        case ("employeesCount"):
                            if (organization.getEmployeesCount() < Integer.parseInt(value)) {
                                System.out.println("Введите на какое значение вы хотите заменить EmployeesCount организации: \n" + organization.toString());
                                organization.setEmployeesCount(Integer.parseInt(reader.readLine()));
                            } break;
                        case ("annualTurnover"):
                            if (organization.getAnnualTurnover() < Double.parseDouble(value)) {
                                System.out.println("Введите на какое значение вы хотите заменить AnnualTurnover организации: \n" + organization.toString());
                                organization.setAnnualTurnover(Double.parseDouble(reader.readLine()));
                            } break;
                    }
                }

                System.out.println("Замена проведена успешно");

            } else System.out.println("Такого поля нет, вот возможные варианты: x, y, employeesCount, annualTurnover.");

        } catch (NumberFormatException e) {
            System.out.println("Неверный формат числа");
        } catch (Exception e) {
            System.out.println("Коллекция пустая, поэтому элементы коллекции не могут быть удалены");
        }
    }

    @Override
    public String getName() {
        return "replace_if_lower";
    }

    public static void replace(String key, String value, Vector<Organization> collection, Connection connection, User user){
        try {
            Iterator<Organization> iterator = collection.iterator();
            while (iterator.hasNext()){
                Organization organization=iterator.next();
                switch (key){
                    case"x":
                        if (organization.getCoordinates().getX() > Integer.parseInt(value)& organization.getUser().getLogin().equals(user.getLogin())) {

                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET coordx=? WHERE username=?");
                            preparedStatement.setInt(1, Integer.parseInt(value));
                            preparedStatement.setString(2,organization.getUser().getLogin());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        break;
                    case "y":
                        if (organization.getCoordinates().getY() > Integer.parseInt(value)& organization.getUser().getLogin().equals(user.getLogin())) {

                            //Замена поля организации
                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET coordy=? WHERE username=?");
                            preparedStatement.setFloat(1, Float.parseFloat(value));
                            preparedStatement.setString(2,organization.getUser().getLogin());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        break;
                    case "employeesCount" :
                        if (organization.getEmployeesCount() > Integer.parseInt(value)& organization.getUser().getLogin().equals(user.getLogin())) {

                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET employeescount=? WHERE username=?");
                            preparedStatement.setInt(1, Integer.parseInt(value));
                            preparedStatement.setString(2,organization.getUser().getLogin());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        break;
                    case ("annualTurnover"):
                        if (organization.getAnnualTurnover() > Double.parseDouble(value)& organization.getUser().getLogin().equals(user.getLogin())) {

                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE organization SET annualturnover=? WHERE username=?");
                            preparedStatement.setDouble(1, Double.parseDouble(value));
                            preparedStatement.setString(2,organization.getUser().getLogin());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        break;

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean validator(String string){
        String[] args = string.split(" ");
        switch (args[0]){
            case "x":
                try {
                    if(isX(args[1])==true){
                        return true;
                    }else {
                        return false;
                    }
                }catch (Exception e){e.printStackTrace();}
            case"y":
                try {
                    if(isY(args[1])==true){
                        return true;
                    }else {
                        return false;
                    }
                }catch (Exception e){e.printStackTrace();}
            case "employeesCount":
                try {
                    if(isEmployeesCount(args[1])==true){
                        return true;
                    }else {
                        return false;
                    }
                }catch (Exception e){e.printStackTrace();}
            case"annualTurnover":
                try {
                    if(isAnnualTurnover(args[1])==true){
                        return true;
                    }else {
                        return false;
                    }
                }catch (Exception e){e.printStackTrace();}
            default:
                System.out.println("Я ничего не понял");
        }
        return false;
    }


    public static boolean isX(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isY(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Float.parseFloat(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static boolean isEmployeesCount(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static boolean isAnnualTurnover(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
