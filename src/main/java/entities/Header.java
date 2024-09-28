package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {

    // 16-bit Packet Identifier (ID)
    private short ID;

    // QR (1 bit), OPCODE (4 bits), AA (1 bit), TC (1 bit), RD (1 bit) stored in one byte
    private boolean QR;
    private int OPCODE;
    private boolean AA;
    private boolean TC;
    private boolean RD;

    // RA (1 bit), Z (3 bits), RCODE (4 bits) stored in one byte
    private boolean RA;
    private int Z;
    private int RCODE;

    // 16-bit counts for various sections of the DNS packet
    private short QDCOUNT;
    private short ANCOUNT;
    private short NSCOUNT;
    private short ARCOUNT;

    // Getters and Setters

    // Packet Identifier (ID)
    public short getID() {
        return ID;
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    // QR (1 bit)
    public boolean isQR() {
        return QR;
    }

    public void setQR(boolean QR) {
        this.QR = QR;
    }

    // OPCODE (4 bits)
    public int getOPCODE() {
        return OPCODE;
    }

    public void setOPCODE(int OPCODE) {
        this.OPCODE = OPCODE & 0x0F; // Ensure it's within 4 bits
    }

    // AA (1 bit)
    public boolean isAA() {
        return AA;
    }

    public void setAA(boolean AA) {
        this.AA = AA;
    }

    // TC (1 bit)
    public boolean isTC() {
        return TC;
    }

    public void setTC(boolean TC) {
        this.TC = TC;
    }

    // RD (1 bit)
    public boolean isRD() {
        return RD;
    }

    public void setRD(boolean RD) {
        this.RD = RD;
    }

    // RA (1 bit)
    public boolean isRA() {
        return RA;
    }

    public void setRA(boolean RA) {
        this.RA = RA;
    }

    // Z (3 bits)
    public int getZ() {
        return Z;
    }

    public void setZ(int Z) {
        this.Z = Z & 0x07; // Ensure it's within 3 bits
    }

    // RCODE (4 bits)
    public int getRCODE() {
        return RCODE;
    }

    public void setRCODE(int RCODE) {
        this.RCODE = RCODE & 0x0F; // Ensure it's within 4 bits
    }

    // QDCOUNT
    public short getQDCOUNT() {
        return QDCOUNT;
    }

    public void setQDCOUNT(short QDCOUNT) {
        this.QDCOUNT = QDCOUNT;
    }

    // ANCOUNT
    public short getANCOUNT() {
        return ANCOUNT;
    }

    public void setANCOUNT(short ANCOUNT) {
        this.ANCOUNT = ANCOUNT;
    }

    // NSCOUNT
    public short getNSCOUNT() {
        return NSCOUNT;
    }

    public void setNSCOUNT(short NSCOUNT) {
        this.NSCOUNT = NSCOUNT;
    }

    // ARCOUNT
    public short getARCOUNT() {
        return ARCOUNT;
    }

    public void setARCOUNT(short ARCOUNT) {
        this.ARCOUNT = ARCOUNT;
    }

    // Create the DNS header response as a byte array
    public byte[] getBuffResponse() {
        ByteBuffer buffer = ByteBuffer.allocate(12).order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(ID);

        // Construct QOATR byte (QR, OPCODE, AA, TC, RD)
        byte QOATR = 0;
        QOATR |= (QR ? 1 << 7 : 0);        // QR (bit 7)
        QOATR |= (OPCODE & 0x0F) << 3;     // OPCODE (bits 3-6)
        QOATR |= (AA ? 1 << 2 : 0);        // AA (bit 2)
        QOATR |= (TC ? 1 << 1 : 0);        // TC (bit 1)
        QOATR |= (RD ? 1 : 0);             // RD (bit 0)
        buffer.put(QOATR);

        // Construct RZR byte (RA, Z, RCODE)
        byte RZR = 0;
        RZR |= (RA ? 1 << 7 : 0);          // RA (bit 7)
        RZR |= (Z & 0x07) << 4;            // Z (bits 4-6)
        RZR |= (RCODE & 0x0F);             // RCODE (bits 0-3)
        buffer.put(RZR);

        buffer.putShort(QDCOUNT);
        buffer.putShort(ANCOUNT);
        buffer.putShort(NSCOUNT);
        buffer.putShort(ARCOUNT);

        return buffer.array();
    }
}
