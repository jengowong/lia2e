package lia.analysis.stopanalyzer;

import junit.framework.TestCase;
import lia.analysis.AnalyzerUtils;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.util.Version;

// From chapter 4

public class StopAnalyzerTest extends TestCase {
    private StopAnalyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_30);

    public void testHoles() throws Exception {
        String[] expected = {"one", "enough"};

        AnalyzerUtils.assertAnalyzesTo(stopAnalyzer, "one is not enough", expected);
        AnalyzerUtils.assertAnalyzesTo(stopAnalyzer, "one is enough", expected);
        AnalyzerUtils.assertAnalyzesTo(stopAnalyzer, "one enough", expected);
        AnalyzerUtils.assertAnalyzesTo(stopAnalyzer, "one but not enough", expected);
    }

}
