# Developer notes

```bash
./mvnw clean verify
./mvnw clean verify -pl system-prompts-generator
./mvnw clean install -pl system-prompts-generator
./mvnw clean generate-resources -pl site-generator -P site-update

jwebserver -p 8000 -d "$(pwd)/docs"
jwebserver -p 8000 -d "$(pwd)/documentation/dvbe25/"
```
