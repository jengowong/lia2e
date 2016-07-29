package lia.extsearch.payloads;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;

// From chapter 6

public class BulletinPayloadsAnalyzer extends Analyzer {

    private boolean isBulletin;
    private float boost;

    BulletinPayloadsAnalyzer(float boost) {
        this.boost = boost;
    }

    void setIsBulletin(boolean v) {
        isBulletin = v;
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        BulletinPayloadsFilter stream =
                new BulletinPayloadsFilter(
                        new StandardAnalyzer(Version.LUCENE_30).tokenStream(fieldName, reader),
                        boost
                );
        stream.setIsBulletin(isBulletin);
        return stream;
    }

}
