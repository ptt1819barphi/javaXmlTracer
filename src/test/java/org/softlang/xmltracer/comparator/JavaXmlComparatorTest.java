package org.softlang.xmltracer.comparator;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.softlang.xmltracer.comparator.model.Base;
import org.softlang.xmltracer.comparator.model.Company;
import org.softlang.xmltracer.comparator.model.Department;
import org.softlang.xmltracer.comparator.model.Employee;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.parser.DomXmlParser;
import org.softlang.xmltracer.parser.JavaObjectParser;
import org.xml.sax.SAXException;

public class JavaXmlComparatorTest {

    @Test
    public void testCompareJavaXml() {
        test("input/base.xml", new Base());
    }

    @Test
    public void testCompareJavaXmlCompany() {
        Company company = new Company("ACME Corporation");

        Department department = new Department("Research", new Employee("Craig", "Redmond", 123456));
        department.addEmployee(new Employee("Erik", "Utrecht", 12345));
        department.addEmployee(new Employee("Ralf", "Koblenz", 1234));
        company.addDepartment(department);

        department = new Department("Development", new Employee("Ray", "Redmond", 234567));
        company.addDepartment(department);
        Department dev1 = new Department("Dev1", new Employee("Klaus", "Boston", 23456));
        department.addSubDepartment(dev1);
        Department dev11 = new Department("Dev1.1", new Employee("Karl", "Riga", 2345));
        dev11.addEmployee(new Employee("Joe", "Wifi City", 2344));
        dev1.addSubDepartment(dev11);

        test("input/company.xml", company);
    }

    private void test(String path, Object object) {
        JavaObjectParser javaObjectParser = new JavaObjectParser();

        ObjectElement javaElement;
        ObjectElement xmlElement;
        try {
            DomXmlParser domXmlParser = new DomXmlParser(path);
            xmlElement = domXmlParser.getParseResult();
            javaElement = javaObjectParser.getObjectElement(object);
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException | IllegalAccessException ex) {
            fail(ex);
            return;
        }
        Comparator comparator = new JavaXmlComparator();
        assertTrue(comparator.compare(javaElement, xmlElement));
    }
}
