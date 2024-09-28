package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class Packet {
    private final Header header;
    private final Question question;

    private final Answer answer;

    public Packet(Header header, Question question, Answer answer){
        this.header = header;
        this.question = question;
        this.answer = answer;
    }
    public byte[] build(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(512)
                .order(ByteOrder.BIG_ENDIAN)
                .put(header.getBuffResponse())
                .put(question.getBuffResponse())
                .put(answer.getBuffResponse());

        return byteBuffer.array();
    }
}
