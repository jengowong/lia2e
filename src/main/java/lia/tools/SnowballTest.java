package lia.tools;

import junit.framework.TestCase;
import lia.analysis.AnalyzerUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.util.Version;

// From chapter 8

public class SnowballTest extends TestCase {
    public void testEnglish() throws Exception {
        Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_30, "English");
        AnalyzerUtils.assertAnalyzesTo(
                analyzer,
                "stemming algorithms",
                new String[]{"stem", "algorithm"});
    }

    public void testSpanish() throws Exception {
        Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_30, "Spanish");
        AnalyzerUtils.assertAnalyzesTo(
                analyzer,
                "algoritmos",
                new String[]{"algoritm"});
    }

}
