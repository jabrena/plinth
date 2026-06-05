# kill-port

## Purpose
Free a localhost port by stopping the process listening on it.

## Usage
```
/kill-port <port-number>
```

## Parameters
- `<port-number>`: TCP port number to free (required, e.g., 8080, 8820, 5432)

## Description
Identifies and terminates any process listening on the specified port, freeing it for reuse. Commonly used to release ports held by development servers, databases, or other services that may have hung or weren't properly shut down.

## Example Usage
```bash
# Free port 8820 (JBake local-preview)
/kill-port 8820

# Free port 8080 (common HTTP server port)
/kill-port 8080

# Free port 5432 (PostgreSQL default)
/kill-port 5432
```

## Output
- Confirmation message if process was killed
- Notification if port was already free
- Optional verification command suggestion
