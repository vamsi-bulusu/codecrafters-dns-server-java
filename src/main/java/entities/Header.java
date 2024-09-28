package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {

    // 16-bit Packet Identifier (ID)
    private short ID;

    // Flags stored in two bytes
    private byte QOATR; // QR, OPCODE, AA, TC, RD
    private byte RZR;   // RA, Z, RCODE

    // 16-bit counts for various sections of the DNS packet
    private short QDCOUNT;
    private short ANCOUNT;
    private short NSCOUNT;
    private short ARCOUNT;

    // Getters and Setters
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

    // Bit manipulation helper methods
    private byte setBit(byte b, int position, boolean value) {
        return value ? (byte) (b | (1 << position)) : (byte) (b & ~(1 << position));
    }

    private boolean getBit(byte b, int position) {
        return (b & (1 << position)) != 0;
    }

    // Set QR flag (1 for response)
    public void setQR(boolean isResponse) {
        QOATR = setBit(QOATR, 7, isResponse); // QR is the highest bit
    }

    // Set OPCODE value (4 bits)
    public void setOPCODE(int opcode) {
        QOATR = (byte) (QOATR & 0x87); // Clear OPCODE bits (111 0000)
        QOATR = (byte) (QOATR | ((opcode & 0x0F) << 3)); // Set the OPCODE bits
    }

    // Set flags (AA, TC, RD) in QOATR byte
    public void setAA(boolean aa) {
        QOATR = setBit(QOATR, 2, aa);
    }

    public void setTC(boolean tc) {
        QOATR = setBit(QOATR, 1, tc);
    }

    public void setRD(boolean rd) {
        QOATR = setBit(QOATR, 0, rd);
    }

    // Set RA flag in RZR byte
    public void setRA(boolean ra) {
        RZR = setBit(RZR, 7, ra);
    }

    // Set Z (3 bits) in RZR byte
    public void setZ(int z) {
        RZR = (byte) (RZR & 0x8F); // Clear Z bits (1110 0000)
        RZR = (byte) (RZR | ((z & 0x07) << 4)); // Set Z bits
    }

    // Set RCODE (4 bits) in RZR byte
    public void setRCODE(int rcode) {
        RZR = (byte) (RZR & 0xF0); // Clear RCODE bits (0000 1111)
        RZR = (byte) (RZR | (rcode & 0x0F)); // Set RCODE bits
    }

    // Create the DNS header response as a byte array
    public byte[] getBuffResponse() {
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
