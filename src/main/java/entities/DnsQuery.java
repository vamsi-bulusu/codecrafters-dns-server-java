package entities;

import util.DnsUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class DnsQuery {

    private Header header;

    private List<Question> questions;

    public DnsQuery(){
        this.questions = new ArrayList<>();
    }

    public Header getHeader() {
        return header;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public ByteBuffer byteBuffer(){
        byte[] bytes = new byte[512];
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        DnsUtil.writeHeader(buffer, header);
        DnsUtil.writeQuestions(buffer, questions);
        return buffer;
    }
}
