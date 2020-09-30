package Server;

import Commands.*;
import Controller.Message;
import Controller.OrganizationCollection;
import DataBase.DataBaseExtaProtocol;
import DataBase.User;
import DataBase.UserAuthorisation;
import Organization.Organization;
import java.util.concurrent.locks.ReentrantLock;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;

public class MultiThreadAnswer implements Callable {
    private Message receiveMessage7;
    static ReentrantLock lock=new ReentrantLock();
    public MultiThreadAnswer(Message receiveMessage7){
        this.receiveMessage7=receiveMessage7;
    }

    public static Message getFromMessage(String description, Organization organization, User user){
	lock.lock();
        Message sendMessage = null;
        Connection connection=Server.connection;
        Statement statement=Server.statement;

        try {
            String[] strings=description.split(" ");
            switch (strings[0]){
                case "add":
                    Add.addToData(connection,organization,statement);
                    sendMessage=new Message(null,"На сервер добавден следующий элемент \n"+organization.toString(),null);
                    break;
                //  case "sort":
                //     sendMessage= new Message(null, organization.compareTo(OrganizationCollection),null);
                case "info":
                    Server.collection= DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null, OrganizationCollection.Info(Server.collection),null);
                    break;
                case "update_id":
                    //Update.update(Integer.parseInt(strings[1]),msgs);
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null, Update.parseUpdateId(Integer.parseInt(strings[1]),Server.collection,organization,user), null);
                    break;
                case "help":
                    sendMessage=new Message(null, Help.getHelp(),null);
                    break;
                case "show":
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null, Show.ShowElement(Server.collection),null);
                    break;
                case "remove_by_id":
                    //Remove_by_id.remove_by_id(Integer.parseInt(strings[1]),collection);
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null, Remove_by_id.remove_by_idS(Integer.parseInt(strings[1]),Server.collection,user),null);
                    break;

                case "clear":
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    Clear.clearData(user,Server.collection);
                    sendMessage=new Message(null,"Теперь тут пусто",null);
                    break;
                case "replace_if_greater":
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    Replace_if_greater.replace(strings[1],strings[2],Server.collection,connection,user);
                    sendMessage = new Message(null, "Шанс успешной замены составляет 99.9% ! Используйте show для проверки обновления ", null);
                    break;
                case "replace_if_lower":
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    Replace_if_lower.replace(strings[1],strings[2],Server.collection,connection,user);
                    sendMessage = new Message(null, "Шанс успешной замены составляет 99.9% ! Используйте show для проверки обновления ", null);
                    break;
                case "registration":
                    if(UserAuthorisation.TryReg(user,connection)==true){
                        sendMessage=new Message(null, user.getLogin() +" ,теперь твоя душа принадлежит мне",null,null,true);

                    }else {
                        sendMessage=new Message(null,"Эй,дружок-пирожок,твой ник уже занят",null,null,false);

                    }
                    break;
                case "login":
                    if(UserAuthorisation.TryLog(user,connection)==true){
                        sendMessage=new Message(null,user.getLogin() +",добро пожаловать в подземелье! Мастер ждет вас...",null,null,true);

                    }else {
                        sendMessage=new Message(null,"тёмный властелин не смог завладеть твоей душой ",null,null,false);
                    }
                    break;



                default:
                    System.out.println("не знаю что это");


            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Я не могу ничего делать.У меня лапки...");
        }finally {
            lock.unlock();
        }return sendMessage;
    }

    @Override
    public Object call() throws Exception {
        Message sendMessage=getFromMessage(receiveMessage7.getDescription(),receiveMessage7.getOrganizationCollection(),receiveMessage7.getUser());
        return sendMessage;
    }
}
