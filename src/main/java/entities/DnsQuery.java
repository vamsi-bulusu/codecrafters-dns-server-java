package entities;

import java.util.List;

public class DnsQuery {

    private Header header;

    private List<Question> questions;

    public DnsQuery(){
    }

    public Header getHeader() {
        return header;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


}
