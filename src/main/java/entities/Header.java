package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {
    private short id;
    private short flags;
    private short qdcount;
    private short ancount;
    private short nscount;
    private short arcount;

    public Header(){

    }
    // Constructor
    public Header(Header header) {
        this.id = header.id;
        this.flags = header.flags;
        this.qdcount = header.qdcount;
        this.ancount = header.ancount;
        this.nscount = header.nscount;
        this.arcount = header.arcount;
    }

    public void setId(short id) {
        this.id = id;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public void setQdcount(short qdcount) {
        this.qdcount = qdcount;
    }

    public void setAncount(short ancount) {
        this.ancount = ancount;
    }

    public void setNscount(short nscount) {
        this.nscount = nscount;
    }

    public void setArcount(short arcount) {
        this.arcount = arcount;
    }

    public short getId() {
        return id;
    }

    public short getFlags() {
        return flags;
    }

    public short getQdcount() {
        return qdcount;
    }

    public short getAncount() {
        return ancount;
    }

    public short getNscount() {
        return nscount;
    }

    public short getArcount() {
        return arcount;
    }

    // Create the DNS header response as a byte array
    public ByteBuffer getByteBuff() {
        return ByteBuffer.allocate(12)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort(id)
                .putShort(flags)
                .putShort(qdcount)
                .putShort(ancount)
                .putShort(nscount)
                .putShort(arcount);
    }
}