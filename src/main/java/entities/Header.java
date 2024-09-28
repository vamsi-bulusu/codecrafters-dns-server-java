package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {
    private short id;
    private byte flags1;  // Combines QR, OPCODE, AA, TC, RD
    private byte flags2;  // Combines RA, Z, RCODE
    private short qdcount;
    private short ancount;
    private short nscount;
    private short arcount;

    // Constructor
    public Header() {
        // Initialize with default values if needed
    }

    // Getter and setter for id
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    // Getter and setter for flags1
    public byte getFlags1() {
        return flags1;
    }

    public void setFlags1(byte flags1) {
        this.flags1 = flags1;
    }

    // Getter and setter for flags2
    public byte getFlags2() {
        return flags2;
    }

    public void setFlags2(byte flags2) {
        this.flags2 = flags2;
    }

    // Getters and setters for qdcount, ancount, nscount, arcount
    public short getQdcount() {
        return qdcount;
    }

    public void setQdcount(short qdcount) {
        this.qdcount = qdcount;
    }

    public short getAncount() {
        return ancount;
    }

    public void setAncount(short ancount) {
        this.ancount = ancount;
    }

    public short getNscount() {
        return nscount;
    }

    public void setNscount(short nscount) {
        this.nscount = nscount;
    }

    public short getArcount() {
        return arcount;
    }

    public void setArcount(short arcount) {
        this.arcount = arcount;
    }

    // Helper methods for individual flags
    public boolean getQR() {
        return (flags1 & 0x80) != 0;
    }

    public void setQR(boolean qr) {
        flags1 = (byte) (qr ? (flags1 | 0x80) : (flags1 & 0x7F));
    }

    public byte getOpcode() {
        return (byte) ((flags1 & 0x78) >>> 3);
    }

    public void setOpcode(byte opcode) {
        flags1 = (byte) ((flags1 & 0x87) | ((opcode & 0x0F) << 3));
    }

    public boolean getAA() {
        return (flags1 & 0x04) != 0;
    }

    public void setAA(boolean aa) {
        flags1 = (byte) (aa ? (flags1 | 0x04) : (flags1 & 0xFB));
    }

    public boolean getTC() {
        return (flags1 & 0x02) != 0;
    }

    public void setTC(boolean tc) {
        flags1 = (byte) (tc ? (flags1 | 0x02) : (flags1 & 0xFD));
    }

    public boolean getRD() {
        return (flags1 & 0x01) != 0;
    }

    public void setRD(boolean rd) {
        flags1 = (byte) (rd ? (flags1 | 0x01) : (flags1 & 0xFE));
    }

    public boolean getRA() {
        return (flags2 & 0x80) != 0;
    }

    public void setRA(boolean ra) {
        flags2 = (byte) (ra ? (flags2 | 0x80) : (flags2 & 0x7F));
    }

    public byte getZ() {
        return (byte) ((flags2 & 0x70) >>> 4);
    }

    public void setZ(byte z) {
        flags2 = (byte) ((flags2 & 0x8F) | ((z & 0x07) << 4));
    }

    public byte getRcode() {
        return (byte) (flags2 & 0x0F);
    }

    public void setRcode(byte rcode) {
        flags2 = (byte) ((flags2 & 0xF0) | (rcode & 0x0F));
    }

    // Create the DNS header response as a byte array
    public byte[] getBuffResponse() {
        ByteBuffer buffer = ByteBuffer.allocate(12); // DNS header is 12 bytes
        buffer.order(ByteOrder.BIG_ENDIAN); // Network byte order

        buffer.putShort(id);
        buffer.put(flags1);
        buffer.put(flags2);
        buffer.putShort(qdcount);
        buffer.putShort(ancount);
        buffer.putShort(nscount);
        buffer.putShort(arcount);

        return buffer.array();
    }
}