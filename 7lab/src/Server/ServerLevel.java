package Server;

import Commands.*;
import Controller.Commands;
import Controller.Message;
import Controller.OrganizationCollection;
import DataBase.DataBaseExtaProtocol;
import DataBase.User;
import DataBase.UserAuthorisation;
import Organization.Organization;
import Utilities.FileParser;
import Utilities.ReadFromFile;
import Utilities.WriterToFile;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ServerLevel {
    //-------------------------------------------------------------------------------
    // settings for Server
    //-------------------------------------------------------------------------------
   /* static Logger LOGGER = Logger.getLogger(ServerLevel.class.getName());
    private DatagramChannel  server;
    private SocketAddress clientAddress;





    public static Connection connection=Server.connection;

    public static Statement statement=Server.statement;

public Organization organization = new Organization();
public static String filename="kompanii.txt";
    //-------------------------------------------------------------------------------
    private int session_token;
    private int sizeBuff =  8 * 1024;

    //-------------------------------------------------------------------------------
    public static ByteBuffer byte_buff = ByteBuffer.allocateDirect(102400);
    private String msg;
    private StringBuffer temp;
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    public static Message receiveMessage=new Message();
    public static Message sendMessage=new Message();

    public ServerLevel(int port)  {
        try {



            // start server
            LOGGER.log(Level.INFO, "Открытие магического портала для передачи данных (шутка)");
            server =  DatagramChannel.open();
            // неблокирующий режим канала
            server.configureBlocking(false);
            InetSocketAddress  addressServer = new InetSocketAddress(port);

            LOGGER.log(Level.INFO, "Связываем пространство и время (шутка: присваивоение имени сокету)");
            server.bind(addressServer);
            System.out.println("Server Started: " + addressServer);
        }catch (Exception e){e.printStackTrace();}
    }
    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    public void MainServer()  {
       try {
            //ConnectToTheDB();
            Server.collection=DataBaseExtaProtocol.Load(connection,statement);

            //DataBaseExtaProtocol.createOrganization(connection);
          // FileParser.parse(ReadFromFile.readFromFile(String.valueOf(Server.getFile())));

           //System.out.println(collection.size());
           byte_buff = ByteBuffer.allocate(sizeBuff);
            Runnable readCommand=new ReadCommand();
           while (true){
               clientAddress = server.receive(byte_buff);

               if(listenConnect()){
                   temp = new StringBuffer();

                   readCommand();
                   sendDataClnt();

                   temp = null;
                   System.gc();
               }
           }
       }catch (Exception e){e.printStackTrace();}
       }





    //-------------------------------------------------------------------------------
    // Модуль прослушивания и соединения с клиентом (модуль приема подключений)
    //-------------------------------------------------------------------------------
    public boolean listenConnect(){
        if(clientAddress != null){

            session_token = (int) getRandomIntegerBetweenRange(1,10000);
            return true;
        }
        else
            return false;
    }
    //-------------------------------------------------------------------------------
    // Модуль чтения команд
    //-------------------------------------------------------------------------------
    private void readCommand() throws IOException, ClassNotFoundException {
        byte_buff.flip();
        byte[] buffer = new byte[byte_buff.remaining()];
        byte_buff.get(buffer);
        receiveMessage=Message.class.cast(deserialize(buffer));
       // System.out.println("[+] #info#\t get data from user : "+receiveMessage.getDescription());

        startCommand(receiveMessage);

        // подготовка данных перед отправкой
        //msg = "[+] #info#\t User --> [ id_token: " + session_token + "]\t command --> " + msg;
        //System.out.println(msg);

        byte_buff.clear();

        byte_buff.put(serialize(sendMessage));
    }
    //-------------------------------------------------------------------------------
    // Модуль выполнения команды
    //-------------------------------------------------------------------------------
    public static void startCommand(Message receiveMessage)  {

        //getFromMessage(receiveMessage.getDescription(),receiveMessage.getOrganizationCollection());
        if(receiveMessage!=null){
            getFromMessage(receiveMessage.getDescription(),receiveMessage.getOrganizationCollection(),receiveMessage.getUser());
        }else {
            String string="Мне бо-бо,но я выживу....";
            byte_buff.put(string.getBytes());
        }







        /*try {
            Commands commands = commandMap.entrySet().stream().filter(k -> k.getKey().toLowerCase().equals(msg.toLowerCase())).findAny().get().getValue();
            if(commands != null)
                commands.execute(organizationCollection, temp);
            System.out.println("[______________________]:" + temp.toString());
            msg += temp.toString();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    */
    //-------------------------------------------------------------------------------
    // Модуль отправки данных клиенту (модуль отправки ответов клиенту)
    //-------------------------------------------------------------------------------
    /*private boolean sendDataClnt() throws IOException{
        byte_buff.flip();
        server.send(byte_buff, clientAddress);
        byte_buff.clear();

        System.out.println("[+] #info#\tsuccess sending data to client");
        return true;
    }
    //-------------------------------------------------------------------------------
    // Функция для рандомной генерации уникального номера сессии
    //-------------------------------------------------------------------------------
    private static double getRandomIntegerBetweenRange(double min, double max){
        double x = ((int)(Math.random()*((max-min)+1))+min);
        return x;
    }


    public static void getFromMessage(String description, Organization organization, User user){
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
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null,OrganizationCollection.Info(Server.collection),null);
                    break;
                case "update_id":
                    //Update.update(Integer.parseInt(strings[1]),msgs);
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null,Update.parseUpdateId(Integer.parseInt(strings[1]),Server.collection,organization,user), null);
                    break;
                case "help":
                    sendMessage=new Message(null,Help.getHelp(),null);
                    break;
                case "show":
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null,Show.ShowElement(Server.collection),null);
                    break;
                case "remove_by_id":
                    //Remove_by_id.remove_by_id(Integer.parseInt(strings[1]),collection);
                    Server.collection=DataBaseExtaProtocol.Load(connection,statement);
                    sendMessage=new Message(null,Remove_by_id.remove_by_idS(Integer.parseInt(strings[1]),Server.collection,user),null);
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
                        sendMessage=new Message(null, user.getLogin() +" ,теперь твоё очко принадлежит мне",null,null,true);

                    }else {
                        sendMessage=new Message(null,"тёмный властелин не смог завладеть твоим очком ",null,null,false);

                    }
                    break;
                case "login":
                    if(UserAuthorisation.TryLog(user,connection)==true){
                        sendMessage=new Message(null,user.getLogin() +" добро пожаловать в подземелье! Мастер ждет вас...",null,null,true);

                    }else {
                        sendMessage=new Message(null,"Эй,дружок-пирожок,твой ник уже занят",null,null,false);
                    }
                    break;



                default:
                    System.out.println("не знаю что это");


            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Я не могу ничего делать.У меня лапки...");
        }
    }


    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        in.close();
        is.close();
        return is.readObject();
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }




     */
}
