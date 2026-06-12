# MongoDB Sakila Database

## How to run in local?

```bash
docker compose up
docker compose up -d
docker compose ps
docker compose logs -f sakiladb
docker compose exec sakiladb mongosh myDatabase --eval 'db.films.find({ title: /^A/ }, { _id: 0, title: 1, rating: 1 }).limit(5)'
docker compose down
docker compose down -v
```
