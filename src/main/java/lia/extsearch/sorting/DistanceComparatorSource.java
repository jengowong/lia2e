package lia.extsearch.sorting;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.SortField;

import java.io.IOException;

// From chapter 6

public class DistanceComparatorSource extends FieldComparatorSource { // #1

    private int x;
    private int y;

    public DistanceComparatorSource(int x, int y) {                  // #2
        this.x = x;
        this.y = y;
    }

    public FieldComparator newComparator(String fieldName,           // #3
                                         int numHits,                // #3
                                         int sortPos,                // #3
                                         boolean reversed)           // #3
            throws IOException {                                     // #3
        return new DistanceScoreDocLookupComparator(fieldName, numHits);
    }

    private class DistanceScoreDocLookupComparator extends FieldComparator { // #4
        private int[] xDoc, yDoc;                     // #5
        private float[] values;                       // #6
        private float bottom;                         // #7
        String fieldName;

        public DistanceScoreDocLookupComparator(String fieldName, int numHits) throws IOException {
            values = new float[numHits];
            this.fieldName = fieldName;
        }

        public void setNextReader(IndexReader reader, int docBase) throws IOException {
            xDoc = FieldCache.DEFAULT.getInts(reader, "x");  // #8
            yDoc = FieldCache.DEFAULT.getInts(reader, "y");  // #8
        }

        private float getDistance(int doc) {                             // #9
            int deltax = xDoc[doc] - x;                                  // #9
            int deltay = yDoc[doc] - y;                                  // #9
            return (float) Math.sqrt(deltax * deltax + deltay * deltay); // #9
        }

        public int compare(int slot1, int slot2) {          // #10
            if (values[slot1] < values[slot2]) return -1;   // #10
            if (values[slot1] > values[slot2]) return 1;    // #10
            return 0;                                       // #10
        }

        public void setBottom(int slot) {                   // #11
            bottom = values[slot];
        }

        public int compareBottom(int doc) {                 // #12
            float docDistance = getDistance(doc);
            if (bottom < docDistance) return -1;            // #12
            if (bottom > docDistance) return 1;             // #12
            return 0;                                       // #12
        }

        public void copy(int slot, int doc) {               // #13
            values[slot] = getDistance(doc);                // #13
        }

        public Comparable value(int slot) {                 // #14
            return new Float(values[slot]);                 // #14
        }                                                   // #14

        public int sortType() {
            return SortField.CUSTOM;
        }
    }

    public String toString() {
        return "Distance from (" + x + "," + y + ")";
    }

}

/*
#1  Extend FieldComparatorSource
#2  Give constructor base location
#3  Create comparator
#4  FieldComparator implementation
#5  Array of x, y per document
#6  Distances for documents in the queue
#7  Worst distance in the queue
#8  Get x, y values from field cache
#9  Compute distance for one document
#10 Compare two docs in the top N
#11 Record worst scoring doc in the top N
#12 Compare new doc to worst scoring doc
#13 Insert new doc into top N
#14 Extract value from top N
*/
