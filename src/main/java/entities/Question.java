package entities;

import java.nio.ByteBuffer;

public class Question {
    private String Name;
    private short Type;
    private short QClass;

    public Question() {

    }

    public void setName(String name) {
        Name = name;
    }

    public void setType(short type) {
        Type = type;
    }

    public void setQClass(short QClass) {
        this.QClass = QClass;
    }

    public String getName() {
        return Name;
    }

    public short getType() {
        return Type;
    }

    public short getQClass() {
        return QClass;
    }

    public ByteBuffer getByteBuff() {
        byte[] nameBytes = encodeDomainName(Name);
        ByteBuffer buffer = ByteBuffer.allocate(nameBytes.length + 4);
        buffer.put(nameBytes);
        buffer.putShort(Type);
        buffer.putShort(QClass);

        return buffer;
    }


    public byte[] encodeDomainName(String domainName) {
        String[] labels = domainName.split("\\.");
        int totalLength = 0;

        // Calculate the total length needed for the buffer
        for (String label : labels) {
            totalLength += 1 + label.length();
        }

        ByteBuffer buffer = ByteBuffer.allocate(totalLength + 1);
        for (String label : labels) {
            buffer.put((byte) label.length());
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0);

        return buffer.array();
    }

}
