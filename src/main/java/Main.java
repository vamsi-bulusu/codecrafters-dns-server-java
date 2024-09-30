import entities.DnsQuery;
import entities.DnsResponse;
import entities.Header;
import entities.Question;
import util.DnsUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        // Split server address and port
        String[] serverAddress = args[1].split(":");
        SocketAddress dnsServerAddress = new InetSocketAddress(serverAddress[0], Integer.parseInt(serverAddress[1]));

        try (DatagramSocket serverSocket = new DatagramSocket(2053)) {

            while (true) {

                byte[] buf = new byte[512];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);  // Receive incoming query

                System.out.println("Received data: " + Arrays.toString(packet.getData()));

                // Parse incoming query
                DnsQuery query = DnsUtil.readQuery(packet.getData());

                // Prepare response object based on the query
                DnsResponse response = new DnsResponse();
                response.setHeader(new Header(query.getHeader()));
                response.setQuestions(query.getQuestions());

                DnsQuery resolverQuery = new DnsQuery();
                resolverQuery.setHeader(new Header(query.getHeader()));
                resolverQuery.getHeader().setQdcount((short) 1); // Only forward one question

                // Forward query to upstream DNS server for each question
                for (Question question : query.getQuestions()) {
                    // Prepare resolver query

                    resolverQuery.getQuestions().add(question);  // Add question

                    // Send query to upstream DNS server
                    byte[] resolverQBytes = resolverQuery.byteBuffer().array();
                    DatagramPacket resolverPacket = new DatagramPacket(resolverQBytes, resolverQBytes.length, dnsServerAddress);
                    serverSocket.send(resolverPacket);  // Send to upstream

                    // Receive the response from upstream DNS server
                    byte[] resolverRBytes = new byte[512];
                    DatagramPacket resolverResponsePacket = new DatagramPacket(resolverRBytes, resolverRBytes.length);
                    serverSocket.receive(resolverResponsePacket);  // Get response

                    // Parse upstream response
                    DnsResponse resolverResponse = DnsUtil.readResponse(resolverResponsePacket.getData());

                    // If answers are present, add them to the response
                    if (!resolverResponse.getAnswers().isEmpty()) {
                        response.getAnswers().add(resolverResponse.getAnswers().getFirst());
                    }
                    resolverQuery.getQuestions().clear();
                }

                // Set the correct answer count (Ancount)
                response.getHeader().setAncount(response.getHeader().getQdcount());

                // Send the final response back to the client
                byte[] responsePacket = response.byteBuffer().array();
                DatagramPacket packetResponse = new DatagramPacket(responsePacket, responsePacket.length, packet.getSocketAddress());
                serverSocket.send(packetResponse);  // Send the response back to client
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}

