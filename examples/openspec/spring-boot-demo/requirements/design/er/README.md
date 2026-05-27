# Sakila Database Schema - Entity Relationship Diagram

## Overview

This directory contains the comprehensive Entity Relationship (ER) diagram for the Spring Boot Demo Implementation, based on a simplified version of the Sakila database schema. The diagram was generated using the Java Diagrams Skill (172-java-diagrams) following best practices for database schema documentation.

## Files

- `er-sakila-schema.puml` - PlantUML ER diagram source file with complete schema analysis
- `er-sakila-schema.png` - Generated PNG image of the comprehensive ER diagram

## Database Schema Analysis

### Entities

#### Film Entity
The core entity containing comprehensive film catalog information:

**Primary Key:** `film_id` (SERIAL)

**Attributes:**
- `title` (VARCHAR(255)) - Film title (required)
- `description` (TEXT) - Film synopsis
- `release_year` (YEAR) - Custom domain with constraint (1901-2155)
- `language_id` (SMALLINT) - Foreign key to Language (required)
- `original_language_id` (SMALLINT) - Foreign key to Language (optional)
- `rental_duration` (SMALLINT) - Rental period in days (default: 3)
- `rental_rate` (NUMERIC(4,2)) - Rental cost (default: 4.99)
- `length` (SMALLINT) - Film duration in minutes
- `replacement_cost` (NUMERIC(5,2)) - Replacement cost (default: 19.99)
- `rating` (MPAA_RATING) - Custom enum type (default: 'G')
- `last_update` (TIMESTAMP) - Auto-updated timestamp
- `special_features` (TEXT[]) - Array of special features
- `fulltext` (TSVECTOR) - Full-text search vector

#### Language Entity
Reference entity for film languages:

**Primary Key:** `language_id` (SERIAL)

**Attributes:**
- `name` (CHAR(20)) - Language name (required)
- `last_update` (TIMESTAMP) - Auto-updated timestamp

### Relationships

1. **Film → Language (spoken_in)**
   - **Type:** Many-to-One (required)
   - **Foreign Key:** `film.language_id` → `language.language_id`
   - **Constraint:** NOT NULL, ON UPDATE CASCADE, ON DELETE RESTRICT
   - **Purpose:** Primary spoken language of the film

2. **Film → Language (original_language)**
   - **Type:** Many-to-One (optional)
   - **Foreign Key:** `film.original_language_id` → `language.language_id`
   - **Constraint:** NULL allowed, ON UPDATE CASCADE, ON DELETE RESTRICT
   - **Purpose:** Original language for dubbed films

### Custom PostgreSQL Types

#### MPAA_Rating Enum
```sql
CREATE TYPE mpaa_rating AS ENUM (
    'G',        -- General Audiences
    'PG',       -- Parental Guidance Suggested
    'PG-13',    -- Parents Strongly Cautioned
    'R',        -- Restricted
    'NC-17'     -- Adults Only
);
```

#### Year Domain
```sql
CREATE DOMAIN year AS integer
    CONSTRAINT year_check CHECK (((VALUE >= 1901) AND (VALUE <= 2155)));
```

### Database Features

#### Indexes
- `idx_title` - B-tree index on film.title for efficient title searches
- `film_fulltext_idx` - GiST index on film.fulltext for full-text search

#### Triggers
- `last_updated` - Automatically updates last_update timestamp on both tables

#### Full-Text Search
- PostgreSQL tsvector for efficient text search across film titles and descriptions
- English language configuration for text processing

## Spring Data JDBC Integration

### Entity Mapping
The Spring Boot application uses a simplified Film entity:

```java
@Table("film")
public record Film(
    @Id @Column("film_id") Integer filmId,
    @Column("title") String title
) {}
```

**Design Decisions:**
- **Immutable Record:** Uses Java record for thread safety and immutability
- **Minimal Mapping:** Only maps essential fields (film_id, title) for API performance
- **Read-Only Operations:** Designed for film query API use cases
- **Explicit Annotations:** Clear database column mapping with @Column

### Repository Pattern
- Extends `ListCrudRepository<Film, Integer>` for optimal List return types
- Custom queries for title-based searches with case-insensitive matching
- Proper parameter binding with @Query and @Param annotations

## Migration Management

### Flyway Migrations

#### V1__sakila_schema.sql
- Creates complete database schema (tables, types, domains)
- Defines indexes and constraints
- Sets up triggers for automatic timestamp updates
- Establishes foreign key relationships with proper referential integrity

#### V2__sakila_film_data.sql
- Inserts sample language data (English)
- Populates 62 sample films for testing
- Focuses on films starting with 'A' for comprehensive test coverage
- Includes diverse film data across all MPAA ratings

## Usage Instructions

### Viewing the Diagram

#### Using PlantUML
```bash
# Render to PNG
java -jar plantuml.jar er-sakila-schema.puml

# Render to SVG
java -jar plantuml.jar -tsvg er-sakila-schema.puml
```

#### Using JBang (Alternative)
```bash
# Install and use the PlantUML rendering tool
jbang puml-to-png@jabrena -f er-sakila-schema.puml
```

#### Online Rendering
Copy the content of `er-sakila-schema.puml` and paste into:
- [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
- [PlantText](https://www.planttext.com/)

### Integration with Development

#### Database Development
- Use the ER diagram as reference for schema changes
- Update diagram when adding new tables or relationships
- Validate foreign key relationships against the visual model

#### API Development
- Reference entity relationships when designing API endpoints
- Consider database constraints when implementing business logic
- Use full-text search capabilities for advanced film queries

## Architecture Decisions

### Simplified Entity Mapping
The Spring Data JDBC entity only maps essential fields (film_id, title) because:
- **Performance:** Reduces memory footprint for API responses
- **Focus:** API only needs film identification and title for current use cases
- **Simplicity:** Avoids complex mapping for unused fields
- **Extensibility:** Can be extended when additional fields are needed

### Database Design Choices
- **Custom Types:** MPAA_Rating enum ensures data integrity for film ratings
- **Domain Constraints:** Year domain validates realistic release years
- **Full-Text Search:** TSVECTOR enables efficient film content search
- **Referential Integrity:** Proper foreign key constraints with CASCADE/RESTRICT

### Performance Optimizations
- **Strategic Indexing:** B-tree on title, GiST on fulltext for query patterns
- **Trigger Efficiency:** Simple timestamp updates with minimal overhead
- **Normalized Design:** Separate Language table reduces data redundancy

## Future Extensions

The schema design supports easy extension for additional Sakila entities:
- **Actor Management:** actor and film_actor tables for cast information
- **Genre Classification:** category and film_category tables
- **Rental Business Logic:** store, rental, and payment tables
- **Customer Management:** customer and address tables

This modular approach allows incremental schema evolution while maintaining data integrity and performance.

## Maintenance

### Updating the Diagram
1. Modify the `.puml` source file when schema changes occur
2. Regenerate the PNG using PlantUML
3. Update this documentation to reflect schema changes
4. Validate diagram accuracy against actual database schema

### Version Control
- Keep both `.puml` source and `.png` output in version control
- Update diagrams as part of database migration pull requests
- Use conventional commit messages for diagram updates

This ER diagram serves as the definitive visual reference for the Sakila database schema used in the Spring Boot Demo Implementation.