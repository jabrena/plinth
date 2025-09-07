# Developer notes

```
./mvnw clean verify
./mvnw clean generate-resources -pl site -P site-update

jwebserver -p 8000 -d "$(pwd)/docs/www"
jwebserver -p 8000 -d "$(pwd)/docs/dvbe25/"
```
