package entities;

import util.DnsUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DnsResponse extends DnsQuery {
    private List<Answer> answers;

    public DnsResponse() {
         answers = new ArrayList<>();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setHeader(Header header) {
        super.setHeader(header);
        short flags = header.getFlags();
        short qr = (short)0x8000;  // Binary: 1000 0000 0000 0000
        short opcode = (short) (flags & 0x7800);  // Binary: 0111 1000 0000 0000
        short rd = (short) (flags & 0x0100);  // Binary: 0000 0001 0000 0000
        short rcode = (opcode == 0) ? (short)0 : 0x0004;  // Binary: 0000 0000 0000 0100
        flags = (short) (qr | opcode | rd | rcode);
        header.setFlags(flags);
    }

    public ByteBuffer byteBuffer(){
        ByteBuffer buffer = super.byteBuffer();
        DnsUtil.writeAnswers(buffer, answers);
        return buffer;
    }

}
