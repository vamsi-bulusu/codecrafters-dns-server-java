package entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DnsQuestion {

    private final byte[] buffResponse;
    public DnsQuestion() {
        // Name - A domain name, represented as a sequence of "labels"
        // Labels are encoded as <length><content>, where <length> is a single byte that specifies the length of the label,
        // and <content> is the actual content of the label. The sequence of labels is terminated by a null byte (\x00).

//        byte[] Name = {12, 99, 111, 100, 101, 99, 114, 97, 102, 116, 101, 114, 115, 2, 105, 111, 0};
        byte[] Name = new byte[0];
        try {
            Name = encodeDomainName("codecrafters.io");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Type: 2-byte int; the type of record (1 for an A record, 5 for a CNAME record etc.)
        short Type = 1; // Set byte order to Big Endian


        // Class: 2-byte int; usually set to 1
        short Class = 1;

        buffResponse = ByteBuffer.allocate(Name.length + 4)
                .order(ByteOrder.BIG_ENDIAN)
                .put(Name)
                .putShort(Type)
                .putShort(Class).array();

    }

    public byte[] encodeDomainName(String name) throws IOException {
        // Initialize ByteArrayOutputStream outside of the try block
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Split the domain name by "."
            for (String label : name.split("\\.")) {
                // Write the length of the label
                byteArrayOutputStream.write(label.length());
                // Write the label bytes
                byteArrayOutputStream.write(label.getBytes());
            }
            // Write a zero byte to indicate the end of the domain name
            byteArrayOutputStream.write(0);
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            throw new IOException("Error encoding domain name: " + e.getMessage(), e);
        }

        // Return the byte array
        return byteArrayOutputStream.toByteArray();
    }
    public byte[] getBuffResponse() {
        return Arrays.copyOf(buffResponse, buffResponse.length);
    }
}