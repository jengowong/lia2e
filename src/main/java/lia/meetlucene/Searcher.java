package lia.meetlucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

// From chapter 1

/**
 * This code was originally written for Erik's Lucene intro java.net article
 */
public class Searcher {

    public static void main(String[] args)
            throws IllegalArgumentException, IOException, ParseException {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Usage: java " + Searcher.class.getName() + " <index dir> <query>");
        }

        String indexDir = args[0];                            //1
        String q = args[1];                                   //2

        search(indexDir, q);
    }

    public static void search(String indexDir, String qStr) throws IOException, ParseException {
        Directory dir = FSDirectory.open(new File(indexDir)); //3
        IndexSearcher iSearcher = new IndexSearcher(dir);     //3

        QueryParser qParser = new QueryParser(
                Version.LUCENE_30,                            //4
                "contents",                                   //4
                new StandardAnalyzer(Version.LUCENE_30));     //4
        Query q = qParser.parse(qStr);                        //4
        long beg = System.currentTimeMillis();
        TopDocs tds = iSearcher.search(q, 10);                //5
        long end = System.currentTimeMillis();

        System.err.println("Found " + tds.totalHits +         //6
                " document(s) (in " + (end - beg) +           //6
                " millis) that matched query '" +             //6
                qStr + "':");                                 //6

        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = iSearcher.doc(sd.doc);             //7
            System.out.println(doc.get("fullpath"));          //8
        }

        iSearcher.close();                                    //9
    }

}
/*
#1 Parse provided index directory
#2 Parse provided query string
#3 Open index
#4 Parse query
#5 Search index
#6 Write search stats
#7 Retrieve matching document
#8 Display filename
#9 Close IndexSearcher
*/
