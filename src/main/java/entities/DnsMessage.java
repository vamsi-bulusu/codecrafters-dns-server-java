package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DnsMessage {
    private final byte[] buffResponse;

    public DnsMessage(byte[] dnsPacket){
        byte[] dnsHeader = DnsParser.parseHeader(Arrays.copyOf(dnsPacket, 12)).getBufferResponse();
        byte[] dnsQuestion = DnsParser.parseQuestion(Arrays.copyOfRange(dnsPacket, 12, dnsPacket.length)).getBuffResponse();
        byte[] dnsAnswer = DnsParser.parseAnswer(dnsQuestion).getBuffResponse();
        buffResponse = ByteBuffer.allocate(512)
                .order(ByteOrder.BIG_ENDIAN)
                .put(dnsHeader)
                .put(dnsQuestion)
                .put(dnsAnswer)
                .array();
    }

    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
