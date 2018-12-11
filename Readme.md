# Summary
This Repo deals with the following problem:
Examining the resemblance of an XML-File and its corresponding Java-Object counterpart, based on their struture and data.

This problem is solved using the following strategies:

* Design a data structure for representing an abstract tree.
* Develop a JavaObject-to-Tree Parser and an XML-to-Tree parser.
* Develop a Comparator to compare the trees resulting from our parsers.

# Usage
## XML to ObjectElement:
Parse your XML-File using the DomXmlParser.

```java
ObjectElement xmlElement;
try {
    DomXmlParser domXmlParser = new DomXmlParser("yourXMLFile.xml");
    xmlElement = domXmlParser.getParseResult();
} catch (ParserConfigurationException | SAXException | IOException) {
    ...
}
```

## Java Object to ObjectElement:
Parse a Java Object using the JavaObjectParser.

```java
JavaObjectParser javaObjectParser = new JavaObjectParser();
ObjectElement javaElement;
try {
    javaElement = javaObjectParser.getObjectElement(yourObject);
} catch (IllegalArgumentException | IllegalAccessException ex) {
    ...
}
```

## Compare both results:
Use the Comparator to compare trees resulting from same data structures:

```java
Comparator comparator = new Comparator();
boolean result = comparator.compare(element1, element2);
```

Use the Comparator extension JavaXmlComparator to compare a tree resulting from a Java Object with a tree from Xml:

```java
Comparator comparator = new JavaXmlComparator();
boolean result = comparator.compare(javaElement, xmlElement);
```

# Prerequisites

* Java SDK 8+ (with Java SDK binaries in the PATH or JAVA_HOME set up)
* Maven
