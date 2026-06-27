///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0
//SOURCES ../../markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java
//MAIN MarkdownValidator

public class MarkdownValidator {

    public static void main(String... args) {
        info.jab.markdownvalidator.MarkdownValidator.main(args);
    }
}
