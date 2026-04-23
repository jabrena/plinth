**Question 1**: Does your service call any external REST APIs (e.g. third-party services, other microservices)?

Options:
- Yes — I need to stub HTTP calls with WireMock
- No — no outbound HTTP calls

**Note**: If "Yes" is selected, Question 1a is asked next.

---

**Question 1a**: What are the base URLs or logical names of those external services?

- Provide one or more names or base URLs, separated by commas
- Examples: `payment-service`, `notification-service`, `https://api.partner.com`

**Note**: This question is only asked if "Yes" was selected in Question 1.

---
