package info.jab.pml;

import java.io.StringReader;
import java.util.stream.Stream;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.SAXException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Validates {@code skill-indexes/*.xml} sources against the local {@code skills.xsd} baseline
 * (an unchanged copy of PML 0.8.0 {@code pml.xsd}).
 * <p>
 * Structured after {@code plinth-commands-generator}'s {@code CommandSchemaTest}: enumerate the
 * complete inventory via the artifact's own Indexes class, load the schema once via a classpath
 * resource with external DTD/schema access denied, and cover both the full inventory and a
 * representative invalid fixture. Distinct in name and scope from {@link RemoteSchemaValidationTest},
 * which continues to validate {@code skill-references/} against the remote PML schema.
 */
@DisplayName("Skill Index Schema Tests")
class SkillIndexSchemaValidationTest {

    private final Schema schema = loadSchema();

    private static Stream<String> provideSkillIndexXmlResources() {
        return SkillIndexes.skillDescriptors()
            .filter(SkillIndexes.SkillDescriptor::useXml)
            .map(d -> "skill-indexes/" + extractNumericId(d.skillId()) + "-skill.xml");
    }

    @ParameterizedTest
    @MethodSource("provideSkillIndexXmlResources")
    @DisplayName("Every inventoried skill-index XML source must satisfy skills.xsd")
    void should_validateAllSkillIndexSources_when_inventoryIsLoaded(String resource) {
        assertThatCode(() -> validateResource(resource))
            .as("Schema-valid skill-index source: %s", resource)
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("A skill-index document missing the required <goal> element fails validation with a meaningful diagnostic")
    void should_rejectMissingGoal_when_promptIsValidated() {
        String xml = "<prompt xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:noNamespaceSchemaLocation=\"skills.xsd\" id=\"999-invalid\">"
            + "<metadata><description>Description</description></metadata>"
            + "<title>Invalid Skill</title>"
            + "</prompt>";

        assertThatThrownBy(() -> validate(xml))
            .isInstanceOf(SAXException.class)
            .hasMessageContaining("goal");
    }

    private void validateResource(String resource) throws Exception {
        try (var stream = SkillIndexSchemaValidationTest.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalStateException("Missing skill-index resource: " + resource);
            }
            schema.newValidator().validate(new StreamSource(stream));
        }
    }

    private void validate(String xml) throws Exception {
        schema.newValidator().validate(new StreamSource(new StringReader(xml)));
    }

    private static String extractNumericId(String skillId) {
        int dash = skillId.indexOf('-');
        return dash > 0 ? skillId.substring(0, dash) : skillId;
    }

    private static Schema loadSchema() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newSchema(SkillIndexSchemaValidationTest.class.getClassLoader().getResource("skills.xsd"));
        } catch (SAXException e) {
            throw new IllegalStateException("Cannot load skills.xsd", e);
        }
    }
}
