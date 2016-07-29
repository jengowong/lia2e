package lia.common;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// From chapter 6

/**
 * Gathers all documents from a search.
 */
public class AllDocCollector extends Collector {

    List<ScoreDoc> docs = new ArrayList<ScoreDoc>();
    private Scorer scorer;
    private int docBase;

    public boolean acceptsDocsOutOfOrder() {
        return true;
    }

    public void setScorer(Scorer scorer) {
        this.scorer = scorer;
    }

    public void setNextReader(IndexReader reader, int docBase) {
        this.docBase = docBase;
    }

    public void collect(int doc) throws IOException {
        docs.add(
                new ScoreDoc(doc + docBase,  // #A
                        scorer.score()));    // #B
    }

    public void reset() {
        docs.clear();
    }

    public List<ScoreDoc> getHits() {
        return docs;
    }
}

/*
#A Create absolute docID
#B Record score
*/
