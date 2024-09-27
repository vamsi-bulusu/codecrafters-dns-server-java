package entities;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsQuestion {

    private final byte[] buffResponse;
    public DnsQuestion() {
        // Name - A domain name, represented as a sequence of "labels"
        // Labels are encoded as <length><content>, where <length> is a single byte that specifies the length of the label,
        // and <content> is the actual content of the label. The sequence of labels is terminated by a null byte (\x00).

         byte[] Name = {12, 99, 111, 100, 101, 99, 114, 97, 102, 116, 101, 114, 115, 2, 105, 111, 0};

        // Type: 2-byte int; the type of record (1 for an A record, 5 for a CNAME record etc.)
        short Type = 1; // Set byte order to Big Endian


        // Class: 2-byte int; usually set to 1
        short Class = 1;

        buffResponse = ByteBuffer.allocate(Name.length + 4)
                .put(Name)
                .putShort(Type)
                .putShort(Class).array();

    }

        public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}
