package entities;

import java.nio.ByteBuffer;


public class DnsQuestion {
    private byte[] Name;

    private short Type;


    private short QClass;

    public byte[] getName() {
        return Name;
    }

    public short getType() {
        return Type;
    }


    public short getQClass() {
        return QClass;
    }


    public void setName(byte[] name) {
        Name = name;
    }

    public void setType(short type) {
        Type = type;
    }

    public void setQClass(short aClass) {
        QClass = aClass;
    }

    public DnsQuestion() {

    }

    public byte[] getBuffResponse() {
        return ByteBuffer.allocate(Name.length + 4)
                .put(Name)
                .putShort(Type)
                .putShort(QClass).array();
    }
}
