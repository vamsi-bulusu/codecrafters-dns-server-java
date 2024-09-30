package entities;

import java.nio.ByteBuffer;

public class Answer extends Question {

    private int ttl;           // Time-To-Live for the record
    private short rdlength;    // Length of the RDATA field
    private byte[] rData;      // Data specific to the record type

    // Default constructor
    public Answer() {
        this.ttl = 3600;
        this.rdlength = 4;
    }

    public int getTtl() {
        return ttl;
    }

    public short getRdlength() {
        return rdlength;
    }

    public byte[] getrData() {
        return rData;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setRdlength(short rdlength) {
        this.rdlength = rdlength;
    }

    public void setrData(byte[] rData) {
        this.rData = rData;
    }

}
