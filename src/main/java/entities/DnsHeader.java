package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DnsHeader {

    // Packet Identifier (ID) - 16 bits
    private short ID;

    // 1st byte for QR, OPCODE, AA, TC, RD
    private byte QOATR;

    // 2nd byte for RA, Z, RCODE
    private byte RZR;

    // 16-bit counts
    private short QDCOUNT;
    private short ANCOUNT;
    private short NSCOUNT;
    private short ARCOUNT;

    // Getters and setters for each field
    public short getID() {
        return ID;
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    public byte getQOATR() {
        return QOATR;
    }

    public void setQOATR(byte QOATR) {
        this.QOATR = QOATR;
    }

    public byte getRZR() {
        return RZR;
    }

    public void setRZR(byte RZR) {
        this.RZR = RZR;
    }

    public short getQDCOUNT() {
        return QDCOUNT;
    }

    public void setQDCOUNT(short QDCOUNT) {
        this.QDCOUNT = QDCOUNT;
    }

    public short getANCOUNT() {
        return ANCOUNT;
    }

    public void setANCOUNT(short ANCOUNT) {
        this.ANCOUNT = ANCOUNT;
    }

    public short getNSCOUNT() {
        return NSCOUNT;
    }

    public void setNSCOUNT(short NSCOUNT) {
        this.NSCOUNT = NSCOUNT;
    }

    public short getARCOUNT() {
        return ARCOUNT;
    }

    public void setARCOUNT(short ARCOUNT) {
        this.ARCOUNT = ARCOUNT;
    }

    // Helper method to set a specific bit in a byte
    private byte setBit(byte b, int bitPosition, boolean value) {
        if (value) {
            return (byte) (b | (1 << bitPosition));
        } else {
            return (byte) (b & ~(1 << bitPosition));
        }
    }

    // Helper method to get the value of a specific bit
    private boolean getBit(byte b, int bitPosition) {
        return (b & (1 << bitPosition)) != 0;
    }

    // Set QR flag (1 for response)
    public void setQR(boolean isResponse) {
        QOATR = setBit(QOATR, 7, isResponse); // QR is the highest bit
    }

    // Set OPCODE value (4 bits)
    public void setOPCODE(int opcode) {
        QOATR = (byte) (QOATR & 0x8F); // Clear the OPCODE bits (1111 0000)
        QOATR = (byte) (QOATR | ((opcode & 0x0F) << 3)); // Set the OPCODE bits
    }

    // Set AA, TC, RD flags (1 bit each)
    public void setAA(boolean aa) {
        QOATR = setBit(QOATR, 2, aa);
    }

    public void setTC(boolean tc) {
        QOATR = setBit(QOATR, 1, tc);
    }

    public void setRD(boolean rd) {
        QOATR = setBit(QOATR, 0, rd);
    }

    // Set RA flag (1 bit)
    public void setRA(boolean ra) {
        RZR = setBit(RZR, 7, ra);
    }

    // Set Z (3 bits)
    public void setZ(int z) {
        RZR = (byte) (RZR & 0x87); // Clear the Z bits (1110 0000)
        RZR = (byte) (RZR | ((z & 0x07) << 4)); // Set the Z bits
    }

    // Set RCODE (4 bits)
    public void setRCODE(int rcode) {
        RZR = (byte) (RZR & 0xF0); // Clear the RCODE bits (0000 1111)
        RZR = (byte) (RZR | (rcode & 0x0F)); // Set the RCODE bits
    }

    // Function to create a DNS header buffer response
    public byte[] getBufferResponse() {
        return ByteBuffer.allocate(12)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort(ID)          // ID
                .put(QOATR)            // QR, OPCODE, AA, TC, RD
                .put(RZR)              // RA, Z, RCODE
                .putShort(QDCOUNT)     // QDCOUNT
                .putShort(ANCOUNT)     // ANCOUNT
                .putShort(NSCOUNT)     // NSCOUNT
                .putShort(ARCOUNT)     // ARCOUNT
                .array();
    }
}
