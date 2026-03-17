# Spring Boot Demo Implementation

A Spring Boot application demonstrating best practices for building a film catalog API using a simplified Sakila database schema.

## Architecture

### Database Schema

The application uses a PostgreSQL database with a simplified version of the Sakila schema, focusing on film catalog functionality.

For detailed database schema documentation and ER diagrams, see: [Database Schema Documentation](docs/database-schema.md)

**Key Tables:**
- `film`: Contains film catalog information (title, description, rating, etc.)
- `language`: Defines available languages for films

**ER Diagram:**
```plantuml
@startchen
entity Film {
  film_id : SERIAL <<key>>
  title : VARCHAR(255)
  language_id : SMALLINT
}

entity Language {
  language_id : SERIAL <<key>>
  name : CHAR(20)
}

relationship SPOKEN_IN {
}

Film -N- SPOKEN_IN
SPOKEN_IN -1- Language
@endchen
```

## Features

- **Film Query API**: Search films by title prefix
- **Spring Data JDBC**: Lightweight data access with immutable entities
- **PostgreSQL Integration**: Full-text search and advanced SQL features
- **Flyway Migrations**: Database schema versioning
- **Native Compilation**: GraalVM native image support

## References

- https://github.com/sakiladb/postgres