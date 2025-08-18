/**
 * Cursor Rules Generator - XML/XSLT transformation engine for generating Cursor AI rules
 *
 * <h2>Purpose</h2>
 * This package provides a comprehensive transformation engine specifically designed to convert
 * structured XML rule definitions into Markdown Cursor (MDC) files for the Cursor AI code editor.
 * The package implements a robust, schema-validated transformation pipeline that ensures consistent
 * and reliable generation of AI coding assistance rules from XML specifications.
 *
 * <h2>Main Components</h2>
 * <ul>
 * <li>{@link info.jab.pml.CursorRulesGenerator} - The primary transformation engine that orchestrates
 * the entire rule generation process using functional programming principles and immutable data structures</li>
 * <li>{@link info.jab.pml.CursorRulesGenerator.TransformationSources} - Immutable record encapsulating
 * XML and XSLT input streams for thread-safe resource management throughout the transformation pipeline</li>
 * <li>{@link info.jab.pml.CursorRulesGenerator.ValidationErrorHandler} - Specialized error handler
 * providing comprehensive XSD validation reporting for precise schema violation identification</li>
 * </ul>
 *
 * <h2>Dependencies</h2>
 * The package leverages standard Java XML processing capabilities including DOM and SAX parsers
 * for XInclude processing, javax.xml.transform for XSLT transformations, and comprehensive XSD
 * schema validation. Logging is implemented through SLF4J with Logback for operation tracking
 * and debugging support.
 *
 * @since 0.10.0-SNAPSHOT
 * @author Juan Antonio Breña Moral
 */
package info.jab.pml;
