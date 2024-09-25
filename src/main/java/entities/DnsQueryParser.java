package entities;

import java.util.List;

public class DnsQueryParser {
    // Responsibility is to take a byte stream and parse into header, question and answer section
    private DnsHeader header;
    private List<DnsQuestion> dnsQuestions;

    public void packetParser(byte[] packet){

    }
}
