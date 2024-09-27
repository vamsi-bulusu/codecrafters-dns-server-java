package entities;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsMessage {
    private final byte[] buffResponse;

    public DnsMessage(byte[] dnsPacket){
        byte[] dnsHeader = new DnsHeader().getBufferResponse();
        byte[] dnsQuestion = new DnsQuestion().getBuffResponse();
//        byte[] dnsAnswer = new DnsAnswer().getBuffResponse();
        byte[] dnsAnswer = new DnsResourceRecord().getBuffResponse();
        buffResponse = ByteBuffer.allocate(512)
                .put(dnsHeader)
                .put(dnsQuestion)
                .put(dnsAnswer)
                .array();
    }

    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
