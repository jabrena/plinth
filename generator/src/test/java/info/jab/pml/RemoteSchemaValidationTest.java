package info.jab.pml;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThatCode;

class RemoteSchemaValidationTest {

    private static final String REMOTE_XSD = "https://jabrena.github.io/pml/schemas/0.1.0-SNAPSHOT/pml.xsd";

    private static Stream<String> provideXmlFileNames() {
        return Stream.of(
            "100-java-cursor-rules-list.xml",
            "110-java-maven-best-practices.xml",
            "111-java-maven-dependencies.xml",
            "112-java-maven-plugins.xml",
            "113-java-maven-documentation.xml",
            "121-java-object-oriented-design.xml",
            "122-java-type-design.xml",
            "123-java-general-guidelines.xml",
            "124-java-secure-coding.xml",
            "125-java-concurrency.xml",
            "126-java-logging.xml",
            "127-java-functional-exception-handling.xml",
            "131-java-unit-testing.xml",
            "141-java-refactoring-with-modern-features.xml",
            "142-java-functional-programming.xml",
            "143-java-data-oriented-programming.xml",
            "151-java-performance-jmeter.xml",
            "161-java-profiling-detect.xml",
            "162-java-profiling-analyze.xml",
            "164-java-profiling-compare.xml",
            "170-java-documentation.xml"
        );
    }

    @ParameterizedTest
    @MethodSource("provideXmlFileNames")
    void xmlFilesAreValidAgainstRemoteSchema(String fileName) throws Exception {
        String resourcePath = "/" + fileName;
        try (InputStream xml = getClass().getResourceAsStream(resourcePath)) {
            if (Objects.isNull(xml)) {
                throw new IllegalStateException("Test resource not found: " + resourcePath);
            }

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "all");
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "all");

            Schema schema = schemaFactory.newSchema(URI.create(REMOTE_XSD).toURL());
            Validator validator = schema.newValidator();

            Source source = new StreamSource(xml);
            assertThatCode(() -> validator.validate(source)).doesNotThrowAnyException();
        }
    }
}
