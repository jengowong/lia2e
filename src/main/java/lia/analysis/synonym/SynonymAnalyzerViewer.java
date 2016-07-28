package lia.analysis.synonym;

import lia.analysis.AnalyzerUtils;

import java.io.IOException;

// From chapter 4

public class SynonymAnalyzerViewer {

    public static void main(String[] args) throws IOException {
        //SynonymEngine engine = new WordNetSynonymEngine(new File(args[0]));
        SynonymEngine engine = new TestSynonymEngine();

        AnalyzerUtils.displayTokensWithPositions(
                new SynonymAnalyzer(engine),
                "The quick brown fox jumps over the lazy dog");

    /*
    AnalyzerUtils.displayTokensWithPositions(
      new SynonymAnalyzer(engine),
      "\"Oh, we get both kinds - country AND western!\" - B.B.");
    */
    }

}
