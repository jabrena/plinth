/**
 * Skills Generator package for XML/XSLT-driven skill artifacts.
 *
 * <h2>Purpose</h2>
 * This package transforms skill-reference and skill-index XML resources into markdown artifacts
 * used by the repository skill distribution pipeline. It focuses on predictable classpath-based
 * processing with XInclude and XSLT transforms.
 *
 * <h2>Main Components</h2>
 * <ul>
 * <li>{@link info.jab.pml.SkillReferenceGenerator} - Transforms skill-reference XML into markdown via XSLT.</li>
 * <li>{@link info.jab.pml.SkillsGenerator} - Builds skill outputs (SKILL.md and references) from indexes and resources.</li>
 * <li>{@link info.jab.pml.SkillIndexes} - Loads and validates entries from {@code skills.xml}.</li>
 * <li>{@link info.jab.pml.InventoryXmlLoader} - Centralizes safe XML parser configuration for inventory documents.</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * Schema validation is not part of the active transformation path; generation is currently based on
 * XML resource loading, XInclude resolution, and XSLT execution.
 *
 * @since 0.10.0
 * @author Juan Antonio Breña Moral
 */
package info.jab.pml;
