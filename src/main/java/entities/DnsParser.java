package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DnsParser {

    // Method to parse the request packet and create a DNS header response
    public static DnsHeader parseHeader(byte[] packet) {
        DnsHeader dnsHeader = new DnsHeader();
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

        // Set static values for AA, TC, RA, and Z
        dnsHeader.setAA(false);  // Set AA to 0
        dnsHeader.setTC(false);  // Set TC to 0
        dnsHeader.setRA(false);  // Set RA to 0
        dnsHeader.setZ(0);       // Set Z to 0

        // Mimic RCODE based on the OPCODE value
        dnsHeader.setRCODE(opcode == 0 ? 0 : 4);  // RCODE: 0 for standard query, else 4 (not implemented)

        // Set QDCOUNT, ANCOUNT, NSCOUNT, ARCOUNT to example values
        dnsHeader.setQDCOUNT((short) 1);  // Example value, adjust as necessary
        dnsHeader.setANCOUNT((short) 1);
        dnsHeader.setNSCOUNT((short) 0);
        dnsHeader.setARCOUNT((short) 0);

        return dnsHeader;
    }

    public static DnsQuestion parseQuestion(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);
        buffer.position(0);  // Set buffer to start at the question section
        DnsQuestion dnsQuestion = new DnsQuestion();

        // Parse Domain Name
        int startPos = buffer.position();
        StringBuilder domainName = new StringBuilder();

        while (true) {
            byte length = buffer.get();  // Get the length of the next label
            if (length == 0) {
                // End of the domain name
                break;
            }

            byte[] label = new byte[length];
            buffer.get(label);  // Get the label bytes
            domainName.append(new String(label)).append(".");
        }

        // Calculate the total length of the domain name (from startPos to current buffer position)
        int domainLength = buffer.position() - startPos;
        byte[] domainBytes = Arrays.copyOfRange(packet, startPos, startPos + domainLength);

        // Set the domain name as byte array
        dnsQuestion.setName(domainBytes);

        // Parse Type (16 bits)
        dnsQuestion.setType(buffer.getShort());

        // Parse Class (16 bits)
        dnsQuestion.setQClass(buffer.getShort());

        return dnsQuestion;
    }

    public static DnsResourceRecord parseAnswer(byte[] packet){
        DnsQuestion dnsQuestion = parseQuestion(packet);
        DnsResourceRecord dnsResourceRecord = new DnsResourceRecord();
        dnsResourceRecord.setName(dnsQuestion.getName());
        dnsResourceRecord.setType(dnsQuestion.getType());
        dnsResourceRecord.setClass(dnsQuestion.getQClass());
        dnsResourceRecord.setTTL(60);
        dnsResourceRecord.setRDLENGTH((short) 4);
        dnsResourceRecord.setRData(new byte[]{1, 56, 1, 56, 1, 56, 1, 56, 0});
        return dnsResourceRecord;
    }

}
