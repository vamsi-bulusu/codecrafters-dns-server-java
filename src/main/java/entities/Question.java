package entities;

import java.nio.ByteBuffer;

public class Question {
    private String Name;
    private short Type;
    private short QClass;

    public Question() {
    }

    public Question(String name, short type, short qClass) {
        this.Name = name;
        this.Type = type;
        this.QClass = qClass;
    }

    // Getter and Setter methods
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public short getType() {
        return Type;
    }

    public void setType(short type) {
        this.Type = type;
    }

    public short getQClass() {
        return QClass;
    }

    public void setQClass(short qClass) {
        this.QClass = qClass;
    }

    // Helper method to convert the question to bytes for response building
    public byte[] getBuffResponse() {
        // Convert the domain name to byte array using the DNS label format
        byte[] nameBytes = encodeDomainName(Name);

        // Create a buffer to store the name + type + class (2 bytes each for type and class)
        ByteBuffer buffer = ByteBuffer.allocate(nameBytes.length + 4);
        buffer.put(nameBytes);
        buffer.putShort(Type);   // 2 bytes for the type
        buffer.putShort(QClass); // 2 bytes for the class

        return buffer.array();
    }

    // Method to encode domain name into DNS label format
    public byte[] encodeDomainName(String domainName) {
        String[] labels = domainName.split("\\.");
        ByteBuffer buffer = ByteBuffer.allocate(domainName.length() + 2); // +2 for the trailing 0
        for (String label : labels) {
            buffer.put((byte) label.length());  // length byte
            buffer.put(label.getBytes());       // label bytes
        }
        buffer.put((byte) 0); // End of the domain name (null byte)
        return buffer.array();
    }
}
