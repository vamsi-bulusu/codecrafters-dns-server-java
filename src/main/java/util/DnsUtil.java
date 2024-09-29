package util;

import entities.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class DnsUtil {

    public static DnsQuery readQuery(byte[] query){
        ByteBuffer buffer = ByteBuffer.wrap(query).order(ByteOrder.BIG_ENDIAN);
        DnsQuery dnsQuery = new DnsQuery();
        dnsQuery.setHeader(readHeader(buffer));
        dnsQuery.setQuestions(readQuestions(buffer, dnsQuery.getHeader().getQdcount()));
        return dnsQuery;
    }

    public static DnsResponse readResponse(byte[] query){
        ByteBuffer buffer = ByteBuffer.wrap(query).order(ByteOrder.BIG_ENDIAN);
        DnsResponse dnsResponse = new DnsResponse();
        dnsResponse.setHeader(readHeader(buffer));
        dnsResponse.setQuestions(readQuestions(buffer, dnsResponse.getHeader().getQdcount()));
        dnsResponse.setAnswers(readAnswers(buffer, dnsResponse.getHeader().getQdcount()));
        return dnsResponse;
    }

    public static Header readHeader(ByteBuffer buffer){
        Header header = new Header();
        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQdcount(buffer.getShort());
        header.setAncount(buffer.getShort());
        header.setNscount(buffer.getShort());
        header.setNscount(buffer.getShort());
        return header;
    }
    public static List<Question> readQuestions(ByteBuffer buffer, short QdCount) {
        List<Question> questions = new ArrayList<>();
        for(int i = 0; i < QdCount; i++) {
            Question question = new Question();
            String domainName = parseDomainName(buffer);
            question.setName(domainName);
            question.setType(buffer.getShort());
            question.setQClass(buffer.getShort());
            questions.add(question);
        }
        return questions;
    }
    public static List<Answer> readAnswers(ByteBuffer buffer, short anCount) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < anCount; i++) {
            Answer answer = new Answer();
            String domainName = parseDomainName(buffer);
            answer.setName(domainName);
            answer.setType(buffer.getShort());
            answer.setQClass(buffer.getShort());
            answer.setTtl(buffer.getInt());
            short rdLength = buffer.getShort();
            answer.setRdlength(rdLength);

            byte[] rData = new byte[rdLength];
            buffer.get(rData);
            answer.setrData(rData);
            answers.add(answer);
        }
        return answers;
    }

    // Helper function to parse domain names from the buffer, handling compression
    private static String parseDomainName(ByteBuffer buffer) {
        StringBuilder domainName = new StringBuilder();
        int position = -1;

        while (buffer.hasRemaining()) {
            byte labelLength = buffer.get();

            if ((labelLength & 0xC0) == 0xC0) {
                int pointerOffset = ((labelLength & 0x3F) << 8) | (buffer.get() & 0xFF);
                position = buffer.position();
                buffer.position(pointerOffset);
                domainName.append(parseDomainName(buffer));
                break;
            }
            if (labelLength == 0) {
                break;
            }
            byte[] label = new byte[labelLength];
            buffer.get(label);
            domainName.append(new String(label)).append(".");
        }

        if (position != -1) {
            buffer.position(position);
        }
        if (!domainName.isEmpty() && domainName.charAt(domainName.length() - 1) == '.') {
            domainName.setLength(domainName.length() - 1);
        }
        return domainName.toString();
    }

    public static ByteBuffer writeQueryBytes(DnsQuery query){
        ByteBuffer buffer = ByteBuffer.allocate(512).order(ByteOrder.BIG_ENDIAN);
        buffer.put(query.getHeader().getByteBuff().array());
        for(int i = 0; i < query.getHeader().getQdcount(); i++){
            buffer.put(query.getQuestions().get(i).getByteBuff());
        }
        return buffer;
    }

    public static ByteBuffer writeResponseBytes(DnsResponse response){
        ByteBuffer buffer = ByteBuffer.allocate(512).order(ByteOrder.BIG_ENDIAN);
        buffer.put(response.getHeader().getByteBuff().array());
        for(int i = 0; i < response.getHeader().getQdcount(); i++){
            buffer.put(response.getQuestions().get(i).getByteBuff());
        }
        for(int i = 0; i < response.getHeader().getQdcount(); i++){
            buffer.put(response.getAnswers().get(i).getByteBuff());
        }
        return buffer;
    }

}
