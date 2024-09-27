package entities;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class DnsAnswer {
    private List<DnsResourceRecord> resourceRecords;
    private byte[] buffResponse;
    public DnsAnswer(byte[] response){
        ByteBuffer byteBuffer = ByteBuffer.allocate(response.length);
        for(DnsResourceRecord dnsResourceRecord: resourceRecords){
            buffResponse = byteBuffer.put(dnsResourceRecord.getBuffResponse())
                    .array();
        }

    }
    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
