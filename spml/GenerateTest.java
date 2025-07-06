import info.jab.xml.CursorRuleGenerator;
public class GenerateTest {
    public static void main(String[] args) {
        CursorRuleGenerator generator = new CursorRuleGenerator();
        try {
            String result = generator.generate("125-java-concurrency.xml", "cursor-rule-generator-1.1.xsl", "spml-1.1.xsd");
            System.out.println("Generated content:");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
