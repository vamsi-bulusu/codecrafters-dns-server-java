package entities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Answer {
    private String name;       // The domain name
    private short type;        // Type of the DNS record (e.g., A, CNAME)
    private short clazz;       // Class of the DNS record (usually IN)
    private int ttl;           // Time-To-Live for the record
    private short rdlength;    // Length of the RDATA field
    private String rData;      // Data specific to the record type

    // Default constructor
    public Answer() {
        this.name = "example.com";    // Dummy domain name
        this.type = 1;                // Type A (IPv4 address)
        this.clazz = 1;               // Class IN (Internet)
        this.ttl = 3600;              // TTL of 3600 seconds (1 hour)
        this.rdlength = 4;            // Length of RDATA for IPv4 address (4 bytes)
        this.rData = "192.0.2.1";     // Dummy IPv4 address (A record)
    }

    // Constructor to initialize the fields
    public Answer(String name, short type, short clazz, int ttl, String rData) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
        this.ttl = ttl;
        this.rData = rData;
        this.rdlength = calculateRDataLength(); // Set RDLENGTH based on the type
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public short getType() {
        return type;
    }

    public short getClazz() {
        return clazz;
    }

    public int getTtl() {
        return ttl;
    }

    public short getRdlength() {
        return rdlength;
    }

    public String getrData() {
        return rData;
    }

    public void setType(short type) {
        this.type = type;
        this.rdlength = calculateRDataLength(); // Recalculate RDLENGTH if type changes
    }

    public void setClass(short clazz) {
        this.clazz = clazz;
    }

    public void setTTL(int ttl) {
        this.ttl = ttl;
    }

    public void setRData(String rData) {
        this.rData = rData;
        this.rdlength = calculateRDataLength(); // Update RDLENGTH when RDATA changes
    }

    // Method to create a byte array representation of the answer
    public byte[] getBuffResponse() {
        byte[] nameBytes = encodeName(name); // Encode the domain name to bytes
        byte[] rDataBytes = encodeRData();   // Encode RDATA based on the type

        // Calculate the total size needed for the response
        int totalSize = nameBytes.length + 10 + rDataBytes.length; // Name + Type + Class + TTL + RDLENGTH + RData
        ByteBuffer buffer = ByteBuffer.allocate(totalSize);

        buffer.put(nameBytes)          // Add the encoded Name byte array
                .putShort(type)          // Add Type (2 bytes)
                .putShort(clazz)         // Add Class (2 bytes)
                .putInt(ttl)            // Add TTL (4 bytes)
                .putShort(rdlength)      // Add RDLENGTH (2 bytes)
                .put(rDataBytes);       // Add RData (variable length)

        return buffer.array();         // Return the resulting byte array
    }

    // Helper method to encode a domain name into the required format
    public byte[] encodeName(String domainName) {
        String[] labels = domainName.split("\\."); // Split by dots
        ByteBuffer buffer = ByteBuffer.allocate(domainName.length() + labels.length); // Allocate buffer

        for (String label : labels) {
            byte[] labelBytes = label.getBytes(StandardCharsets.UTF_8); // Get the bytes for the label
            buffer.put((byte) labelBytes.length); // Add the length of the label
            buffer.put(labelBytes);               // Add the label bytes
        }
        buffer.put((byte) 0); // End of the domain name with a zero-length byte
        return buffer.array(); // Return the complete byte array
    }

    // Method to encode RData based on the record type
    public byte[] encodeRData() {
        return switch (type) {
            case 1 ->  // A record (IPv4 address)
                    encodeIPv4RData();
            case 5 ->  // CNAME record
                    encodeName(rData); // Reuse the domain name encoding logic
            default -> rData.getBytes(StandardCharsets.UTF_8); // Default to simple UTF-8 encoding
        };
    }

    // Helper method to encode IPv4 addresses for A records
    public byte[] encodeIPv4RData() {
        String[] octets = rData.split("\\."); // Split the IPv4 address into octets
        byte[] ipBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            ipBytes[i] = (byte) Integer.parseInt(octets[i]);
        }
        return ipBytes;
    }

    // Method to calculate the length of RData based on the record type
    public short calculateRDataLength() {
        return switch (type) {
            case 1 ->  // A record
                    4; // IPv4 addresses are always 4 bytes
            case 5 ->  // CNAME record
                    (short) encodeName(rData).length;
            default -> (short) rData.getBytes(StandardCharsets.UTF_8).length;
        };
    }
}
