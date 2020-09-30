package Client;

import Commands.*;
import Controller.Message;
import DataBase.Encrypt;
import DataBase.User;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ClientWork {
    public static List<String> scripts = new ArrayList<>();
    //-------------------------------------------------------------------------------
    // settings for Server
    public static List<String> executed_scripts = new ArrayList<>();
    public static DatagramChannel сlientChannel;
    private SocketAddress serverAddress;
    public static Message sendMessage;
    public static Message receivedMessage;
    public static User currentUser;
    private String msg = "";
    ByteBuffer byte_buff;
    byte[] buffer;
    static  public BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    //-------------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------------
    public ClientWork(String hostname,int port)  {
        // start server
        try {
            сlientChannel =  DatagramChannel.open();
            сlientChannel.configureBlocking(false);
            DatagramSocket socketClient = сlientChannel.socket();

            socketClient.bind(null);

            serverAddress = new InetSocketAddress(hostname, port);
        }catch (Exception e){e.printStackTrace();}
    }
    //-------------------------------------------------------------------------------
    // Main
    //-------------------------------------------------------------------------------
    public void StartClient()  {
      /*  Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Exited!");
            }
        });


       */
       try {
           byte_buff =  ByteBuffer.allocateDirect(8 * 1024);
          sendMessage=Init();
          if(sendMessage==null){
              System.out.println("ну null и null,че бубнить то");
              sendMessage=Init();
          }else {
              FirsRule(sendMessage);
          }

           System.out.println("Пожалуйста, введите команду help, для того что бы получить список команд, используйте \"help\".");

           while (true){

               Thread.sleep(100);
               if(scripts.size()!=0){
                   sendMessage=selectCommand(scripts.get(0));
                   sendData(sendMessage);
               }else {

               }
               getData();

               String line = bufferedReader.readLine();

               sendMessage=selectCommand(line);

               if(sendMessage==null){
                   System.out.println("Неизвестная команда");
                   continue;
               }else {
                   sendData(sendMessage);

               }





           }
       }catch (Exception e){e.printStackTrace();
           System.out.println("Что то пошло не так");}
        //for (;;);
    }
    //-------------------------------------------------------------------------------
    // Получение данных от сервера
    //-------------------------------------------------------------------------------
    private void getData() {
       try {
           SocketAddress sa=сlientChannel.receive(byte_buff);
           {
               if(sa!=null){
                   byte_buff.flip();
                   buffer = new byte[byte_buff.remaining()];
                   byte_buff.get(buffer);

                   receivedMessage=Message.class.cast(deserialize(buffer));

                   if (receivedMessage!=null){
                       if(receivedMessage.getaBoolean()!=null){
                           if (receivedMessage.getaBoolean()==false){
                               System.out.println(receivedMessage.getDescription());
                               System.out.println("Пройдие данную процедуру снова");
                               FirsRule(Init());
                           }else System.out.println(receivedMessage.getDescription());
                       }else {
                           System.out.println(receivedMessage.getDescription());
                       }

                   }else System.out.println("Ну null и null.че бубнить то");
                   //msg = new String(buffer);

                   byte_buff.clear();
               }else {
                   //System.out.println(" Медоносная пчела за секунду совершает 250 взмахов крыльями, разгоняясь при этом до 65 км/ч.");
               }
           }

       }catch (Exception e){
           e.printStackTrace();
       }
    }
    //-------------------------------------------------------------------------------
    // Отправка данных сервера
    //-------------------------------------------------------------------------------
    private  void sendData(Message message) throws IOException {
            buffer=serialize(message);
            byte_buff.clear();
            byte_buff.put(buffer);
            byte_buff.flip();
            сlientChannel.send(byte_buff,serverAddress);
            byte_buff.clear();




    }

    public static Message selectCommand(String string){
        try {
            User primalUser=currentUser;
            Message extraMessage;
            if(string==null){
               // System.out.println("Ты думал,что сможешь переиграть меня?");
                System.out.println("Вы думали я вас не переиграю?");
                System.out.println("Я вас переиграю! Я вас уничтожу !");
                System.exit(0);

            }
            else {
                if (scripts.size()!=0){
                    scripts.remove(0);
                }
                String[] args=string.split(" ");
                String command;
                switch (args[0]){
                    case"add":
                        extraMessage=Add.add(args[0],primalUser);
                        return extraMessage;
                    case "clear":
                        command="clear";
                        extraMessage=new Message(null,command,null,primalUser);
                        return extraMessage;
                    case "count_less_than_official_address":
                        command="count_less_than_official_address";
                        return null;
                    case "execute_script":
                        try {
                            if(args.length==2){
                                extraMessage=Execute_script.execute_script(args[1],currentUser);;
                                return extraMessage;
                            }else {
                                System.out.println("Аргументов должно быть ровно 2 !");
                                break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    case "help":
                        command="help";
                        extraMessage=new Message(new Help(),command,null);
                        return extraMessage;
                    case "info":
                        command="info";
                        extraMessage=new Message(new Info(),command,null);
                        return  extraMessage;
                    case "remove_by_id":
                        try {
                            if(args.length==2){
                                command="remove_by_id "+args[1];
                                extraMessage=new Message(null,command,null,primalUser);
                                return extraMessage;
                            }else{
                                System.out.println("Аргументов должно быть ровно 2 !");
                                break;
                            }
                        }catch (Exception e){e.printStackTrace();}

                    case "replace_if_greater":
                        try{
                            if(args.length==3){
                                command="replace_if_greater "+args[1]+ " "+args[2];
                                String argument=args[1]+" "+args[2];
                                if(Replace_if_greater.validator(argument)==true){
                                    extraMessage= new Message(null,command,null,primalUser);
                                    return extraMessage;
                                }else {
                                    System.out.println("Валидация не пройдена.Ключ и значение  несовместимы");
                                    break;
                                }


                            }else{
                                System.out.println("Аргументов должно быть ровно 3 !");
                                break;
                            }
                        }catch (Exception e){e.printStackTrace();}

                    case "replace_if_lower":
                        try{
                            if(args.length==3){
                                command="replace_if_lower "+args[1]+ " "+args[2];
                                String argument=args[1]+" "+args[2];
                                if(Replace_if_lower.validator(argument)==true){
                                    extraMessage= new Message(null,command,null,primalUser);
                                    return extraMessage;
                                }else {
                                    System.out.println("Валидация не пройдена.Ключ и значение  несовместимы");
                                    break;
                                }


                            }else{
                                System.out.println("Аргументов должно быть ровно 3 !");
                                break;
                            }
                        }catch (Exception e){e.printStackTrace();}
                    case "update_id":
                        try{
                            if(args.length==2){
                                command="update_id " + args[1];
                                extraMessage=Update.update_id(command,primalUser);
                                return  extraMessage;

                            } else{
                                System.out.println("Аргументов должно быть ровно 2!");
                                break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    case "exit":
                        System.exit(0);
                        return null;
                    case "show":
                        command="show";
                        extraMessage=new Message(null,command,null);
                        return  extraMessage;
                    case "sort":
                        command="sort";
                        extraMessage=new Message(null,command,null);
                        return extraMessage;

                    default:

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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

    public static  Message Init(){
        try {
            User user1=new User();
            User user2=new User();
            System.out.println("Ну что, регистрируемся... Или пароли запоминать надо");
            System.out.println("registration или login");
            String read=bufferedReader.readLine();
            switch (read){
                case"registration":
                    String name=User.inputName(bufferedReader);
                    user1.setLogin(name);
                    String pass=User.inputPassword(bufferedReader);
                    user1.setPassword(Encrypt.EncryptSHA(pass));
                    currentUser=user1;
                    sendMessage=new Message(null,"registration",null,null,user1);
                    return sendMessage;
                case "login":
                    String logN=User.inputName(bufferedReader);
                    user2.setLogin(logN);
                    String passN=User.inputPassword(bufferedReader);
                    user2.setPassword(Encrypt.EncryptSHA(passN));
                    currentUser=user2;
                    sendMessage=new Message(null,"login",null,user2);
                    return  sendMessage;
                default:

            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  void FirsRule(Message message){
        try {

            sendData(message);
            getData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
