package lia.tools.XmlQueryParser;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.xmlparser.DOMUtils;
import org.apache.lucene.xmlparser.FilterBuilder;
import org.apache.lucene.xmlparser.ParserException;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

// From chapter 9

public class AgoFilterBuilder implements FilterBuilder {

    static HashMap<String, Integer> timeUnits = new HashMap<String, Integer>();

    public Filter getFilter(Element element) throws ParserException {
        String fieldName = DOMUtils.getAttributeWithInheritanceOrFail(element, "fieldName"); //A
        String timeUnit = DOMUtils.getAttribute(element, "timeUnit", "days");                //A
        Integer calUnit = timeUnits.get(timeUnit);                                           //A
        if (calUnit == null) {                                                               //A
            throw new ParserException("Illegal time unit:" + timeUnit + " - must be days, months or years"); //A
        }                                                          //A
        int agoStart = DOMUtils.getAttribute(element, "from", 0);  //A
        int agoEnd = DOMUtils.getAttribute(element, "to", 0);      //A
        if (agoStart < agoEnd) {
            int oldAgoStart = agoStart;
            agoStart = agoEnd;
            agoEnd = oldAgoStart;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   //B

        Calendar start = Calendar.getInstance();                   //B
        start.add(calUnit, agoStart * -1);                         //B

        Calendar end = Calendar.getInstance();                     //B
        end.add(calUnit, agoEnd * -1);                             //B

        return NumericRangeFilter.newIntRange(                     //C
                fieldName,                                         //C
                Integer.valueOf(sdf.format(start.getTime())),      //C
                Integer.valueOf(sdf.format(end.getTime())),        //C
                true,
                true);                                             //C
    }

    static {
        timeUnits.put("days", Calendar.DAY_OF_YEAR);
        timeUnits.put("months", Calendar.MONTH);
        timeUnits.put("years", Calendar.YEAR);
    }
}
/*
#A Extract field, time unit, from and to
#B Parse date/times
#C Create NumericRangeFilter
*/
