# Database Schema Documentation

## Overview

This document describes the database schema for the Spring Boot Demo Implementation, which uses a simplified version of the Sakila database schema. The schema is designed for a film catalog API that allows querying films by title.

## ER Diagram

The following Entity Relationship diagram shows the complete database schema using Chen's notation:

```plantuml
@startchen

title Sakila Database Schema - ER Diagram
note top : Spring Boot Demo Implementation\nSimplified Sakila Database Schema

entity Film {
  film_id : SERIAL <<key>>
  title : VARCHAR(255)
  description : TEXT
  release_year : YEAR
  language_id : SMALLINT
  original_language_id : SMALLINT
  rental_duration : SMALLINT
  rental_rate : NUMERIC(4,2)
  length : SMALLINT
  replacement_cost : NUMERIC(5,2)
  rating : MPAA_RATING
  last_update : TIMESTAMP
  special_features : TEXT[]
  fulltext : TSVECTOR
}

entity Language {
  language_id : SERIAL <<key>>
  name : CHAR(20)
  last_update : TIMESTAMP
}

relationship SPOKEN_IN {
}

relationship ORIGINAL_LANGUAGE {
}

Film -N- SPOKEN_IN
SPOKEN_IN -1- Language

Film -(0,1)- ORIGINAL_LANGUAGE
ORIGINAL_LANGUAGE -1- Language

note right of Film : Contains film catalog information\nwith rating, rental details,\nand full-text search capability

note right of Language : Defines available languages\nfor films (spoken and original)

note right of SPOKEN_IN : Primary language relationship\n(required - NOT NULL)

note right of ORIGINAL_LANGUAGE : Original language relationship\n(optional - can be NULL)

@endchen
```

## Tables

### Film Table

The `film` table is the core entity of the schema, containing comprehensive information about each film in the catalog.

**Columns:**
- `film_id` (SERIAL, PRIMARY KEY): Unique identifier for each film
- `title` (VARCHAR(255), NOT NULL): Film title
- `description` (TEXT): Film description/synopsis
- `release_year` (YEAR): Year of film release (domain constraint: 1901-2155)
- `language_id` (SMALLINT, NOT NULL, FK): Primary spoken language (references language.language_id)
- `original_language_id` (SMALLINT, FK): Original language if different from spoken language
- `rental_duration` (SMALLINT, DEFAULT 3, NOT NULL): Rental period in days
- `rental_rate` (NUMERIC(4,2), DEFAULT 4.99, NOT NULL): Cost to rent the film
- `length` (SMALLINT): Film duration in minutes
- `replacement_cost` (NUMERIC(5,2), DEFAULT 19.99, NOT NULL): Cost to replace the film
- `rating` (MPAA_RATING, DEFAULT 'G'): MPAA film rating (G, PG, PG-13, R, NC-17)
- `last_update` (TIMESTAMP, DEFAULT now(), NOT NULL): Last modification timestamp
- `special_features` (TEXT[]): Array of special features
- `fulltext` (TSVECTOR): Full-text search vector for title and description

**Indexes:**
- `idx_title`: B-tree index on title for fast title-based searches
- `film_fulltext_idx`: GiST index on fulltext for full-text search operations

**Triggers:**
- `last_updated`: Automatically updates last_update timestamp on row modifications

### Language Table

The `language` table defines the available languages for films.

**Columns:**
- `language_id` (SERIAL, PRIMARY KEY): Unique identifier for each language
- `name` (CHAR(20), NOT NULL): Language name
- `last_update` (TIMESTAMP, DEFAULT now(), NOT NULL): Last modification timestamp

**Triggers:**
- `last_updated`: Automatically updates last_update timestamp on row modifications

## Relationships

### Film ↔ Language Relationships

The schema defines two relationships between Film and Language entities:

1. **SPOKEN_IN (Required Relationship)**
   - **Cardinality**: Many films to one language (N:1)
   - **Foreign Key**: `film.language_id` → `language.language_id`
   - **Constraint**: NOT NULL (every film must have a primary language)
   - **Referential Integrity**: ON UPDATE CASCADE, ON DELETE RESTRICT

2. **ORIGINAL_LANGUAGE (Optional Relationship)**
   - **Cardinality**: Many films to one language (N:1, optional)
   - **Foreign Key**: `film.original_language_id` → `language.language_id`
   - **Constraint**: NULL allowed (films may not have a different original language)
   - **Use Case**: For dubbed films where original language differs from spoken language

## Custom Types and Domains

### MPAA_Rating Enum
```sql
CREATE TYPE mpaa_rating AS ENUM (
    'G',        -- General Audiences
    'PG',       -- Parental Guidance Suggested
    'PG-13',    -- Parents Strongly Cautioned
    'R',        -- Restricted
    'NC-17'     -- Adults Only
);
```

### Year Domain
```sql
CREATE DOMAIN year AS integer
    CONSTRAINT year_check CHECK (((VALUE >= 1901) AND (VALUE <= 2155)));
```

## Database Features

### Full-Text Search
- The `fulltext` column uses PostgreSQL's tsvector type for efficient text search
- Automatically populated with English language text search vectors
- Indexed with GiST for fast text search operations

### Automatic Timestamps
- Both tables use triggers to automatically update `last_update` timestamps
- Ensures data consistency and audit trail

### Data Integrity
- Foreign key constraints ensure referential integrity
- Check constraints validate data ranges (year domain)
- NOT NULL constraints ensure required data is present

## Usage in Spring Boot Application

The Spring Boot application uses Spring Data JDBC with a simplified entity model:

```java
@Table("film")
public record Film(
    @Id @Column("film_id") Integer filmId,
    @Column("title") String title
) {}
```

**Note**: The JPA entity only maps essential fields (film_id, title) for the specific use case of the film query API. The full database schema supports more comprehensive film catalog operations.

## Migration Scripts

The schema is managed using Flyway migrations:
- `V1__sakila_schema.sql`: Creates tables, types, indexes, and constraints
- `V2__sakila_film_data.sql`: Populates initial film data

## Performance Considerations

1. **Title Search Optimization**: B-tree index on title column supports efficient prefix searches
2. **Full-Text Search**: GiST index enables fast text search across film descriptions
3. **Foreign Key Indexes**: Automatic indexes on foreign key columns for join performance
4. **Trigger Efficiency**: Simple timestamp update triggers with minimal overhead

## Future Extensions

The schema design supports easy extension for additional Sakila entities:
- Actor and film_actor tables for cast information
- Category and film_category tables for genre classification
- Store, rental, and payment tables for rental business logic
- Customer and address tables for customer management

This modular approach allows incremental schema evolution while maintaining data integrity and performance.