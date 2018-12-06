package org.softlang.xmltracer.comparator;

import org.softlang.xmltracer.comparator.model.Base;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.parser.DomXmlParser;
import org.softlang.xmltracer.parser.JavaObjectParser;
import org.xml.sax.SAXException;

public class ComparatorTest {

    @Test
    public void testCompareJavaSelf() {
        JavaObjectParser javaObjectParser = new JavaObjectParser();
        ObjectElement objectElement;
        try {
            objectElement = javaObjectParser.getObjectElement(new Base());
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            fail(ex);
            return;
        }

        Comparator comparator = new Comparator();
        assertTrue(comparator.compare(objectElement, objectElement));
    }

    @Test
    public void testCompareXmlSelf() {
        ObjectElement objectElement;
        try {
            DomXmlParser domXmlParser = new DomXmlParser("input/base.xml");
            objectElement = domXmlParser.getParseResult();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            fail(ex);
            return;
        }

        Comparator comparator = new Comparator();
        assertTrue(comparator.compare(objectElement, objectElement));
    }
}
