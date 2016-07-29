package lia.extsearch.payloads;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.Payload;

import java.io.IOException;

// From chapter 6

public class BulletinPayloadsFilter extends TokenFilter {

    private TermAttribute termAtt;
    private PayloadAttribute payloadAttr;
    private boolean isBulletin;
    private Payload boostPayload;

    BulletinPayloadsFilter(TokenStream in, float warningBoost) {
        super(in);
        payloadAttr = addAttribute(PayloadAttribute.class);
        termAtt = addAttribute(TermAttribute.class);
        boostPayload = new Payload(PayloadHelper.encodeFloat(warningBoost));
    }

    void setIsBulletin(boolean v) {
        isBulletin = v;
    }

    public final boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            if (isBulletin && termAtt.term().equals("warning")) {   // #A
                payloadAttr.setPayload(boostPayload);               // #A
            } else {
                payloadAttr.setPayload(null);                       // #B
            }
            return true;
        } else {
            return false;
        }
    }

}

/*
#A Add payload boost
#B Clear payload
*/
