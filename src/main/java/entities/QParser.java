package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QParser {

    // Method to parse the request packet and create a DNS header response
    public static Header parseHeader(byte[] packet) {
        Header dnsHeader = new Header();
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);

        // Parse and mimic the packet ID from the request
        dnsHeader.setID(buffer.getShort());

        // Read the flags (QOATR and RZR)
        dnsHeader.setQOATR(buffer.get());
        dnsHeader.setRZR(buffer.get());

        // Set QR to 1 (response)
        dnsHeader.setQR(true);

        // Mimic the OPCODE from the request
        int opcode = (dnsHeader.getQOATR() >> 3) & 0x0F; // Extract OPCODE from the QOATR
        dnsHeader.setOPCODE(opcode);

        // Set static values for AA, TC, RA, and Z based on your requirements
        dnsHeader.setAA(false);  // Set AA to 0
        dnsHeader.setTC(false);  // Set TC to 0
        dnsHeader.setRA(false);  // Set RA to 0
        dnsHeader.setZ(0);       // Set Z to 0

        // Set RCODE based on specific logic or error state
        dnsHeader.setRCODE(0);   // Assuming 0 for no error; adjust logic as necessary

        // Set QDCOUNT, ANCOUNT, NSCOUNT, ARCOUNT based on your logic
        dnsHeader.setQDCOUNT((short) 0);  // Example value, adjust as necessary
        dnsHeader.setANCOUNT((short) 0);
        dnsHeader.setNSCOUNT((short) 0);
        dnsHeader.setARCOUNT((short) 0);

        return dnsHeader;
    }

    public static List<Question> parseQuestions(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);
        List<Question> questions = new ArrayList<>();
        buffer.position(12); // Skip the DNS header (12 bytes)

        while (buffer.hasRemaining()) {
            Question dnsQuestion = new Question();
            StringBuilder domainName = new StringBuilder();

            // Parse Domain Name (handling compression)
            domainName.append(parseDomainName(packet, buffer));

            // Set the domain name as a string
            dnsQuestion.setName(domainName.toString());

            // Parse Type (16 bits)
            if (buffer.remaining() < 4 || domainName.isEmpty()) break; // Ensure there's enough data left
            dnsQuestion.setType(buffer.getShort());

            // Parse Class (16 bits)
            dnsQuestion.setQClass(buffer.getShort());

            // Add the parsed question to the list
            questions.add(dnsQuestion);
        }

        return questions;
    }

    // Helper method to parse domain names, including compression
    private static String parseDomainName(byte[] packet, ByteBuffer buffer) {
        StringBuilder domainName = new StringBuilder();

        while (true) {
            byte length = buffer.get(); // Get the length of the next label

            // Check for compression (pointer)
            if ((length & 0xC0) == 0xC0) {
                // Read the pointer offset
                int pointerOffset = ((length & 0x3F) << 8) | (buffer.get() & 0xFF);
                // Move buffer to the position pointed to by the pointer
                ByteBuffer newBuffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);
                newBuffer.position(pointerOffset);
                // Parse the domain name from the new position
                domainName.append(parseDomainName(packet, newBuffer));
                break; // Exit since we've now parsed the domain name
            }

            // If length is zero, end of the domain name
            if (length == 0) {
                break;
            }

            // Read the label
            byte[] label = new byte[length];
            buffer.get(label); // Get the label bytes
            domainName.append(new String(label)).append(".");
        }

        // Remove the trailing dot from the domain name
        if (!domainName.isEmpty()) {
            domainName.setLength(domainName.length() - 1); // Remove the last dot
        }

        return domainName.toString();
    }

    public static Answer parseAnswer(byte[] packet, int startPosition) {
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);
        buffer.position(startPosition); // Start from the given position in the packet

        Answer answer = new Answer();

        // Parse Name (Domain Name)
        StringBuilder domainName = new StringBuilder();
        while (true) {
            byte length = buffer.get();  // Get the length of the next label
            if (length == 0) {
                break; // End of the domain name
            }

            byte[] label = new byte[length];
            buffer.get(label);  // Get the label bytes
            domainName.append(new String(label)).append(".");
        }

        // Remove the trailing dot from the domain name
        if (!domainName.isEmpty()) {
            domainName.setLength(domainName.length() - 1); // Remove the last dot
        }

        answer.setName(domainName.toString()); // Set the parsed name

        // Parse Type (16 bits)
        answer.setType(buffer.getShort());

        // Parse Class (16 bits)
        answer.setClass(buffer.getShort());

        // Parse TTL (4 bytes)
        answer.setTTL(buffer.getInt());

        // Parse RDLENGTH (2 bytes)
        short rdLength = buffer.getShort();

        // Parse RDATA based on the RDLENGTH
        byte[] rDataBytes = new byte[rdLength];
        buffer.get(rDataBytes); // Get the RDATA bytes

        String rData;
        if (answer.getType() == 1) {  // A record (IPv4 address)
            // Convert the RDATA to an IPv4 address
            rData = (rDataBytes[0] & 0xFF) + "." + (rDataBytes[1] & 0xFF) + "."
                    + (rDataBytes[2] & 0xFF) + "." + (rDataBytes[3] & 0xFF);
        } else if (answer.getType() == 5) {  // CNAME record
            // Convert the RDATA bytes to a domain name
            StringBuilder cname = new StringBuilder();
            int index = 0;
            while (index < rdLength) {
                int labelLength = rDataBytes[index++];
                if (labelLength == 0) {
                    break;  // End of the domain name
                }
                cname.append(new String(rDataBytes, index, labelLength)).append(".");
                index += labelLength;
            }
            if (!cname.isEmpty()) {
                cname.setLength(cname.length() - 1); // Remove the trailing dot
            }
            rData = cname.toString();
        } else {
            // For other types, just convert the RDATA to a string
            rData = new String(rDataBytes);
        }

        answer.setRData(rData); // Set the parsed RDATA

        return answer;
    }
}
