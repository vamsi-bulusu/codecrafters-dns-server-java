package entities;

import java.nio.ByteBuffer;

public class DnsResourceRecord {
    /*
        Name	Label Sequence	The domain name encoded as a sequence of labels.
        Type	2-byte Integer	1 for an A record, 5 for a CNAME record etc., full list here
        Class	2-byte Integer	Usually set to 1 (full list here)
        TTL (Time-To-Live)	4-byte Integer	The duration in seconds a record can be cached before requerying.
        Length (RDLENGTH)	2-byte Integer	Length of the RDATA field in bytes.
        Data (RDATA)	Variable	Data specific to the record type.
        {12, 99, 111, 100, 101, 99, 114, 97, 102, 116, 101, 114, 115, 2, 105, 111, 0}
     */
    private byte[] Name;
    private short Type;
    private short Class;
    private int TTL;
    private short RDLENGTH;
    private byte[] RData;

    public void setName(byte[] name) {
        Name = name;
    }

    public void setType(short type) {
        Type = type;
    }

    public void setClass(short aClass) {
        Class = aClass;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    public void setRDLENGTH(short RDLENGTH) {
        this.RDLENGTH = RDLENGTH;
    }

    public void setRData(byte[] RData) {
        this.RData = RData;
    }


    public DnsResourceRecord(){


    }
    public byte[] getBuffResponse() {
        return ByteBuffer.allocate(Name.length + RData.length + 10)
                .put(Name)
                .putShort(Type)
                .putShort(Class)
                .putInt(TTL)
                .putShort(RDLENGTH)
                .put(RData)
                .array();
    }

}
