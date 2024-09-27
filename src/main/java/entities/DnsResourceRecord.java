package entities;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsResourceRecord {
    /*
        Name	Label Sequence	The domain name encoded as a sequence of labels.
        Type	2-byte Integer	1 for an A record, 5 for a CNAME record etc., full list here
        Class	2-byte Integer	Usually set to 1 (full list here)
        TTL (Time-To-Live)	4-byte Integer	The duration in seconds a record can be cached before requerying.
        Length (RDLENGTH)	2-byte Integer	Length of the RDATA field in bytes.
        Data (RDATA)	Variable	Data specific to the record type.
     */
    private final byte[] buffResponse;
    public DnsResourceRecord(){
        byte[] Name = {12, 99, 111, 100, 101, 99, 114, 97, 102, 116, 101, 114, 115, 2, 105, 111, 0};

        short Type = 1;

        short Class = 1;

        int TTL = 60;

        short RDLENGTH = 4;

        byte[] RData = {1, 56, 1, 56, 1, 56, 1, 56, 0};
        buffResponse = ByteBuffer.allocate(Name.length + RData.length + 10)
                .put(Name)
                .putShort(Type)
                .putShort(Class)
                .putInt(TTL)
                .putShort(RDLENGTH)
                .put(RData)
                .array();
    }
    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }

}
