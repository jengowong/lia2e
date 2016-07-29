package lia.tika;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// From chapter 7

public class SAXXMLDocument extends DefaultHandler {

    private StringBuilder elementBuffer = new StringBuilder();
    private Map<String, String> attributeMap = new HashMap<String, String>();

    private Document doc;

    public Document getDocument(InputStream is)  // #1
            throws DocumentHandlerException {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(is, this);
        } catch (Exception e) {
            throw new DocumentHandlerException(
                    "Cannot parse XML document", e);
        }

        return doc;
    }

    public void startDocument() {               // #2
        doc = new Document();
    }

    public void startElement(String uri,        // #3
                             String localName,  // #3
                             String qName,      // #3
                             Attributes atts)   // #3
            throws SAXException {               // #3

        elementBuffer.setLength(0);
        attributeMap.clear();
        int numAtts = atts.getLength();
        if (numAtts > 0) {
            for (int i = 0; i < numAtts; i++) {
                attributeMap.put(atts.getQName(i), atts.getValue(i));
            }
        }
    }

    public void characters(char[] text, int start, int length) {                             // #4
        elementBuffer.append(text, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException { // #5
        if (qName.equals("address-book")) {
            return;
        } else if (qName.equals("contact")) {
            for (Entry<String, String> attribute : attributeMap.entrySet()) {
                String attName = attribute.getKey();
                String attValue = attribute.getValue();
                doc.add(new Field(attName, attValue, Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
        } else {
            doc.add(new Field(qName, elementBuffer.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }

    public static void main(String args[]) throws Exception {
        SAXXMLDocument handler = new SAXXMLDocument();
        Document doc = handler.getDocument(new FileInputStream(new File(args[0])));
        System.out.println(doc);
    }

}
/*
#1 Start parser
#2 Called when parsing begins
#3 Beginning of new XML element
#4 Append element contents to elementBuffer
#5 Called when closing XML elements are processed
*/
