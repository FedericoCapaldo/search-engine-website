/**
 * Created by rocco on 3/12/17.
 */
public class Server {
    public static void main(String[] args) {
        SingleThreadedServer server = new SingleThreadedServer(9000);
        new Thread(server).start();
//        try {
//            System.out.println("server running ");
//            Thread.sleep(100 * 1000);
//        } catch (InterruptedException e) {
//
//            e.printStackTrace();
//        }
//        System.out.println("Stopping Server");
//        server.stop();
    }
}
