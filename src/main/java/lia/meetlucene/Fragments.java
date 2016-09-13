package lia.meetlucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

// From chapter 1

/**
 * Just contains any code fragments from chapter 1
 */
public class Fragments {

    public void simpleSearch() throws IOException {
        Directory dir = FSDirectory.open(new File("/tmp/index"));
        IndexSearcher iSearcher = new IndexSearcher(dir);
        Query q = new TermQuery(new Term("contents", "lucene"));
        TopDocs tds = iSearcher.search(q, 10);
        iSearcher.close();
    }

}
