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

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setRdlength(short rdlength) {
        this.rdlength = rdlength;
    }

    public void setrData(byte[] rData) {
        this.rData = rData;
    }

    public ByteBuffer getByteBuff() {
        byte[] question = super.getByteBuff().array();
        int totalSize = question.length + 10 + rData.length;
        return ByteBuffer.allocate(totalSize)
                .put(question)
                .putInt(ttl)
                .putShort(rdlength)
                .put(rData);
    }

}
