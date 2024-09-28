package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class Packet {
    private final Header header;
    private final List<Question> questionList;

    private final List<Answer> answerList;

    public Packet(Header header, List<Question> questionList, List<Answer> answerList){
        this.header = header;
        this.questionList = questionList;
        this.answerList = answerList;
    }
    public byte[] build(){
        // put header
        ByteBuffer byteBuffer = ByteBuffer.allocate(512)
                .order(ByteOrder.BIG_ENDIAN)
                .put(header.getBuffResponse());
        // put questionList
        for(Question question: questionList){
            byteBuffer = byteBuffer.put(question.getBuffResponse());
        }

        // put answerList
        for(Answer answer: answerList){
            byteBuffer = byteBuffer.put(answer.getBuffResponse());
        }

        return byteBuffer.array();
    }
}
