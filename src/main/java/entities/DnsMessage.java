package entities;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsMessage {
    private final byte[] buffResponse;

    public DnsMessage(){
        byte[] dnsHeader = new DnsHeader().getBufferResponse();
        byte[] dnsQuestion = new DnsQuestion().getBuffResponse();

        buffResponse = ByteBuffer.allocate(dnsHeader.length + dnsQuestion.length)
                .put(dnsHeader)
                .put(dnsQuestion)
                .array();
    }

    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
