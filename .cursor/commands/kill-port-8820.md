---
description: Free localhost port 8820 (JBake local-preview)
---

# Kill process on port 8820

Free `http://localhost:8820/` by stopping whatever is listening on TCP port 8820 (typically the JBake `local-preview` site: `./mvnw ... jbake:inline -pl site-generator -P local-preview`).

Run this in the integrated terminal from the repo root:

```bash
pids=$(lsof -ti :8820); [ -n "$pids" ] && kill -9 $pids && echo "Freed port 8820" || echo "Port 8820 was already free"
```

If nothing changed, verify with `lsof -i :8820`.
