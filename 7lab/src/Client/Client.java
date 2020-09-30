package Client;

public class Client {

    // java18 -jar name.jar localhost 1431


    public static void main(String[] args) throws Exception{



        if(args.length!=2){
            System.out.println("Ожидася хост и порт");
            return;
        }
            if(isNumeric(args[1])==true){
                new ClientWork(args[0],Integer.parseInt(args[1])).StartClient();

            }else {
                System.out.println("Чо то тут не то");
                return;
            }




    }
    public static boolean isNumeric(String strNum) {
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
