package entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Resolver {

    private static final String DNS_SERVER = "8.8.8.8"; // Google Public DNS

    public static byte[] resolve(Header header, Question question) {
        try {
            // Step 1: Build the DNS query packet
            byte[] queryPacket = buildQueryPacket(header, question);

            // Step 2: Send the query to the DNS server
            InetAddress dnsAddress = InetAddress.getByName(DNS_SERVER);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(queryPacket, queryPacket.length, dnsAddress, 53);
            socket.send(packet);

            // Step 3: Receive the response
            byte[] responseBuffer = new byte[512]; // Standard DNS response size
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);
            socket.close();

            // Step 4: Parse the response (assuming a proper response structure)
            return parseResponse(responseBuffer, header, question);

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions properly in production code
            return null;
        }
    }

    private static byte[] buildQueryPacket(Header header, Question question) {
        ByteBuffer buffer = ByteBuffer.allocate(512); // Standard size for DNS query

        // Write header
        buffer.putShort(header.getId()); // Transaction ID
        buffer.putShort((short) 0x0100); // Standard query with recursion desired
        buffer.putShort((short) 1); // Questions count
        buffer.putShort((short) 0); // Answer RRs
        buffer.putShort((short) 0); // Authority RRs
        buffer.putShort((short) 0); // Additional RRs

        // Encode the question using the Question class's encoder
        byte[] nameBytes = question.encodeDomainName(question.getName());
        buffer.put(nameBytes);
        buffer.putShort(question.getType()); // Query type
        buffer.putShort(question.getQClass()); // Query class

        return buffer.array();
    }

    private static byte[] parseResponse(byte[] responseBuffer, Header header, Question question) {
        // Update header flags based on response status
        // (e.g., if there are no answers, set the appropriate flags in the header)

        // This is a placeholder for the actual parsing logic
        // For now, just return the response buffer
        return responseBuffer;
    }

    public static Answer createDummyAnswer(Question question) {
        // Create an Answer object using the default constructor
        Answer answer = new Answer();

        // Set the name in the Answer object to match the domain name from the Question object
        answer.setName(question.getName());

        // The rest of the fields will remain at their default values
        // Assuming the Answer class has appropriate getters and setters.

        return answer;
    }
}
