# Developer notes

```
./mvnw clean verify
./mvnw clean generate-resources -pl site

jwebserver -p 8000 -d "$(pwd)/docs/www"
jwebserver -p 8000 -d "$(pwd)/docs/dvbe25/"
```
