package entities;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class DnsHeader {

    private final byte[] buffResponse;
    public DnsHeader(){

        // Packet Identifier (ID) - 16 bits - 2 bytes
        short ID = (short) 1234;

        // Query/Response Indicator (QR) - 1 bit
        // Operation Code (OPCODE) - 4 bits
        // Authoritative Answer (AA) - 1 bit
        // Truncation (TC) - 1 bit
        // Recursion Desired (RD) - 1 bit

        byte QOATR = (byte)(1 << 7);


        //  Recursion Available (RA) - 1 bit
        //  Reserved (Z) - - 3 bits
        //  Response Code (RCODE) - 4 bits
        byte RZR = 0;

        //  Question Count (QDCOUNT) - 2 bytes
        //  Answer Record Count (ANCOUNT) - 2 bytes
        //  Authority Record Count (NSCOUNT) - 2 bytes
        //  Additional Record Count (ARCOUNT) - 2 bytes

        short QDCOUNT = 1;
        short ANCOUNT = 0;
        short NSCOUNT = 0;
        short ARCOUNT = 0;

        buffResponse = ByteBuffer.allocate(512)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort(ID)
                .put(QOATR)
                .put(RZR)
                .putShort(QDCOUNT)
                .putShort(ANCOUNT)
                .putShort(NSCOUNT)
                .putShort(ARCOUNT)
                .array();

    }
    public byte[] getBufferResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
