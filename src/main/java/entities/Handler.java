package entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Handler {
    public static byte[] handle(byte[] query) {
        // Parse Header from the incoming DNS query
        Header header = QParser.parseHeader(query);

        // Parse Questions from the query, starting after the header (12 bytes)
        List<Question> questionList = QParser.parseQuestions(query);

        // Create a list to store the answers
        List<Answer> answerList = new ArrayList<>();

        // For each question, resolve the answer and parse it
        for (Question question : questionList) {
            // Use Resolver to get the response for this question
//            byte[] response = Resolver.resolve(header, question);
//            Answer answer = QParser.parseAnswer(response, getAnswerStartPosition(response));
            Answer answer = Resolver.createDummyAnswer(question);
            // Parse the answer from the response packet

            // Add the parsed answer to the answer list
            answerList.add(answer);
        }
        header.setQDCOUNT((short) questionList.size());
        header.setARCOUNT((short) answerList.size());
        // Build the DNS response packet from the header, questions, and answers
        Packet packet = new Packet(header, questionList, answerList);
        System.out.println(Arrays.toString(packet.build()));
        return packet.build();
    }
    public static int getAnswerStartPosition(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);

        // Step 1: Skip the header (12 bytes)
        buffer.position(12);

        // Step 2: Parse domain name in the question section
        while (true) {
            byte length = buffer.get();  // Length of each label
            if (length == 0) {
                break;  // End of the domain name
            }
            buffer.position(buffer.position() + length);  // Skip the label
        }

        // Step 3: Skip Type (2 bytes) and Class (2 bytes) in the question section
        buffer.position(buffer.position() + 4);

        // The current position in the buffer is the start of the answer section
        return buffer.position();
    }

}
