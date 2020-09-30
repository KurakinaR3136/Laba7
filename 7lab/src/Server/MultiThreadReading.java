package Server;

import Controller.Message;
import DataBase.DataBaseExtaProtocol;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Server.Server.*;



public class MultiThreadReading implements Runnable{
    static Logger LOGGER = Logger.getLogger(ServerLevel.class.getName());
    private DatagramChannel  server;
    private SocketAddress clientAddress;
    public static ByteBuffer byte_buff = ByteBuffer.allocateDirect(102400);
    private String msg;
    private StringBuffer temp;
    private int sizeBuff =  8 * 1024;
    private int session_token;
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    public static Message receiveMessage=new Message();
    public static Message sendMessage=new Message();
    @Override
    public void run() {
        MainServer();
    }


    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        in.close();
        is.close();
        return is.readObject();
    }


    public MultiThreadReading(int port)  {
        try {



            // start server
            LOGGER.log(Level.INFO, "Открытие магического портала для передачи данных (шутка)");
            server =  DatagramChannel.open();
            // неблокирующий режим канала
            server.configureBlocking(false);
            InetSocketAddress addressServer = new InetSocketAddress(port);

            LOGGER.log(Level.INFO, "Связываем пространство и время (шутка: присваивоение имени сокету)");
            server.bind(addressServer);
            System.out.println("Server Started: " + addressServer);

        }catch (Exception e){e.printStackTrace();}
    }

    public void MainServer()  {
        try {
            //ConnectToTheDB();
            Server.collection= DataBaseExtaProtocol.Load(Server.connection,Server.statement);

            //DataBaseExtaProtocol.createOrganization(connection);
            // FileParser.parse(ReadFromFile.readFromFile(String.valueOf(Server.getFile())));

            //System.out.println(collection.size());
            byte_buff = ByteBuffer.allocate(sizeBuff);

            while (true){
                clientAddress = server.receive(byte_buff);

                if(listenConnect()){
                   // temp = new StringBuffer();

                    readCommand();
                    sendDataClnt();

                    //temp = null;
                    System.gc();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //-------------------------------------------------------------------------------
    // Модуль чтения команд
    //-------------------------------------------------------------------------------
    private void readCommand() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        byte_buff.flip();
        byte[] buffer = new byte[byte_buff.remaining()];
        byte_buff.get(buffer);
        receiveMessage=Message.class.cast(deserialize(buffer));
        // System.out.println("[+] #info#\t get data from user : "+receiveMessage.getDescription());

        sendMessage=startCommand(receiveMessage);

        // подготовка данных перед отправкой
        //msg = "[+] #info#\t User --> [ id_token: " + session_token + "]\t command --> " + msg;
        //System.out.println(msg);

        byte_buff.clear();

        byte_buff.put(serialize(sendMessage));
    }
    //-------------------------------------------------------------------------------
    // Модуль выполнения команды
    //-------------------------------------------------------------------------------
    public static Message startCommand(Message receiveMessage) throws ExecutionException, InterruptedException {

        //getFromMessage(receiveMessage.getDescription(),receiveMessage.getOrganizationCollection());
        if(receiveMessage!=null){
            FutureTask<Message> futureTask=new FutureTask<>(new MultiThreadAnswer(receiveMessage));
            fixedThreadPool.execute(futureTask);
            sendMessage=futureTask.get();
            //getFromMessage(receiveMessage.getDescription(),receiveMessage.getOrganizationCollection(),receiveMessage.getUser());
        }else {
            String string="Мне бо-бо,но я выживу....";
            byte_buff.put(string.getBytes());
        }
        return sendMessage;
    }
    //-------------------------------------------------------------------------------
    // Модуль отправки данных клиенту (модуль отправки ответов клиенту)
    //-------------------------------------------------------------------------------
    private boolean sendDataClnt() throws IOException{
        byte_buff.flip();
        server.send(byte_buff, clientAddress);
        byte_buff.clear();

        System.out.println("[+] #info#\tsuccess sending data to client");
        return true;
    }


    public boolean listenConnect(){
        if(clientAddress != null){

            session_token = (int) getRandomIntegerBetweenRange(1,10000);
            return true;
        }
        else
            return false;
    }
    private static double getRandomIntegerBetweenRange(double min, double max){
        double x = ((int)(Math.random()*((max-min)+1))+min);
        return x;
    }

}
