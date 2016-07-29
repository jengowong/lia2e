package lia.tools;

import junit.framework.TestCase;
import lia.common.TestUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

import java.io.StringReader;

// From chapter 8

public class HighlightTest extends TestCase {
    public void testHighlighting() throws Exception {
        String text = "The quick brown fox jumps over the lazy dog";

        TermQuery query = new TermQuery(new Term("field", "fox"));

        TokenStream tokenStream = new SimpleAnalyzer().tokenStream("field", new StringReader(text));

        QueryScorer scorer = new QueryScorer(query, "field");
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        Highlighter highlighter = new Highlighter(scorer);
        highlighter.setTextFragmenter(fragmenter);
        assertEquals(
                "The quick brown <B>fox</B> jumps over the lazy dog",
                highlighter.getBestFragment(tokenStream, text));
    }

    public void testHits() throws Exception {
        IndexSearcher searcher = new IndexSearcher(TestUtil.getBookIndexDirectory());
        TermQuery query = new TermQuery(new Term("title", "action"));
        TopDocs hits = searcher.search(query, 10);

        QueryScorer scorer = new QueryScorer(query, "title");
        Highlighter highlighter = new Highlighter(scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));

        Analyzer analyzer = new SimpleAnalyzer();

        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            String title = doc.get("title");

            TokenStream stream = TokenSources.getAnyTokenStream(
                    searcher.getIndexReader(),
                    sd.doc,
                    "title",
                    doc,
                    analyzer);
            String fragment = highlighter.getBestFragment(stream, title);

            System.out.println(fragment);
        }
    }

}
