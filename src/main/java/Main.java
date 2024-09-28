import entities.Handler;
import entities.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

     try(DatagramSocket serverSocket = new DatagramSocket(2053)) {
       while(true) {
         final byte[] buf = new byte[512];
         final DatagramPacket packet = new DatagramPacket(buf, buf.length);
         serverSocket.receive(packet);

         System.out.println("Received data: " + Arrays.toString(packet.getData()));

         List<Packet> responsePackets = Handler.handle(packet.getData());

         for(Packet p: responsePackets){
             final DatagramPacket packetResponse = new DatagramPacket(p.build(), p.build().length, packet.getSocketAddress());
             serverSocket.send(packetResponse);
         }
       }
     } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
     }
  }
}
