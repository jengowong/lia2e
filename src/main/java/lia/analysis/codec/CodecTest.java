package lia.analysis.codec;

import junit.framework.TestCase;
import org.apache.commons.codec.language.Metaphone;

// From chapter 4

public class CodecTest extends TestCase {
    public void testMetaphone() throws Exception {
        Metaphone metaphoner = new Metaphone();
        assertEquals(metaphoner.encode("cute"), metaphoner.encode("cat"));
    }
}
