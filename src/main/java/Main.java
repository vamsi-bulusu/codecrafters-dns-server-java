import entities.DnsQuery;
import entities.DnsResponse;
import entities.Header;
import util.DnsUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        String[] serverAddress = args[1].split(":");
        InetSocketAddress dnsServerAddress = new InetSocketAddress(serverAddress[0], Integer.parseInt(serverAddress[1]));
        try (DatagramSocket serverSocket = new DatagramSocket(2053)) {
            while (true) {
                // Buffer to receive incoming DNS requests
                final byte[] buf = new byte[512];
                final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);

                System.out.println("Received data: " + Arrays.toString(packet.getData()));

                DnsQuery query = DnsUtil.readQuery(packet.getData());
                DnsResponse response = new DnsResponse();
                response.setHeader(new Header(query.getHeader()));
                response.setQuestions(query.getQuestions());

                DnsQuery resolverQuery = new DnsResponse();
                resolverQuery.setHeader(new Header(query.getHeader()));
                resolverQuery.getHeader().setQdcount((short) 1);

                for(int i = 0; i < query.getHeader().getQdcount(); i++){

                    resolverQuery.getQuestions().add(query.getQuestions().get(i));
                    byte[] resolverPacket = DnsUtil.writeQueryBytes(resolverQuery).array();
                    DatagramPacket dnsQueryPacket = new DatagramPacket(resolverPacket, resolverPacket.length, dnsServerAddress);
                    DatagramSocket forwardSocket = new DatagramSocket();
                    forwardSocket.send(dnsQueryPacket);

                    byte[] responseBuffer = new byte[512];
                    DatagramPacket dnsResponsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                    forwardSocket.receive(dnsResponsePacket);
                    forwardSocket.close();

                    DnsResponse resolverResponse = DnsUtil.readResponse(dnsResponsePacket.getData());
                    if(!resolverResponse.getAnswers().isEmpty()){
                        response.setAnswers(resolverResponse.getAnswers());
                    }
                    resolverQuery.getQuestions().clear();
                }
                response.getHeader().setAncount(response.getHeader().getAncount());
                byte[] responsePacket = DnsUtil.writeResponseBytes(response).array();
                final DatagramPacket packetResponse = new DatagramPacket(responsePacket, responsePacket.length, packet.getSocketAddress());
                serverSocket.send(packetResponse);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
