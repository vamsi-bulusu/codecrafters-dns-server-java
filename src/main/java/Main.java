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
        // Logs for debugging
        System.out.println("Logs from your program will appear here!");

        // Split server address and port
        String[] serverAddress = args[1].split(":");
        SocketAddress dnsServerAddress = new InetSocketAddress(serverAddress[0], Integer.parseInt(serverAddress[1]));

        try (DatagramSocket serverSocket = new DatagramSocket(2053)) {
            // Main loop for processing DNS queries
            while (true) {
                // Buffer to receive incoming DNS requests
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
                resolverQuery.setHeader(new Header(query.getHeader())); // Copy header
                resolverQuery.getHeader().setQdcount((short) 1); // Only forward one question
                // Forward query to upstream DNS server for each question
                for (Question question : query.getQuestions()) {
                    // Prepare resolver query

                    resolverQuery.getQuestions().add(question);  // Add question

                    // Send query to upstream DNS server
                    byte[] resolverPacket = DnsUtil.writeQueryBytes(resolverQuery).array();
                    DatagramPacket dnsQueryPacket = new DatagramPacket(resolverPacket, resolverPacket.length, dnsServerAddress);
                    serverSocket.send(dnsQueryPacket);  // Send to upstream

                    // Receive the response from upstream DNS server
                    byte[] responseBuffer = new byte[512];
                    DatagramPacket dnsResponsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                    serverSocket.receive(dnsResponsePacket);  // Get response

                    // Parse upstream response
                    DnsResponse resolverResponse = DnsUtil.readResponse(dnsResponsePacket.getData());

                    // If answers are present, add them to the response
                    if (!resolverResponse.getAnswers().isEmpty()) {
                        response.getAnswers().add(resolverResponse.getAnswers().getFirst());
                    }
                }

                // Set the correct answer count (Ancount)
                response.getHeader().setAncount((short) response.getQuestions().size());

                // Send the final response back to the client
                byte[] responsePacket = DnsUtil.writeResponseBytes(response).array();
                DatagramPacket packetResponse = new DatagramPacket(responsePacket, responsePacket.length, packet.getSocketAddress());
                serverSocket.send(packetResponse);  // Send the response back to client
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
}

