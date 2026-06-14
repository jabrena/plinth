# Simulated Feature Request: Checkout Delivery Instructions

## Summary

Add customer-provided delivery instructions to `CheckoutService` so shoppers can
attach structured delivery preferences during checkout. The change affects the
checkout database model and the Kafka messages emitted by the checkout saga.

## Business Context

Shoppers currently choose a delivery time slot, but they cannot provide delivery
instructions such as access codes, preferred drop-off location, or contactless
delivery preference. Support teams receive these instructions through free-text
channels after checkout, which creates manual work and inconsistent delivery
handoffs.

## Requested Behavior

When a shopper confirms checkout, `CheckoutService` must accept an optional
`deliveryInstructions` object and persist it with the order. The checkout saga
must include the same delivery instruction data when it publishes delivery and
order events so downstream services can reserve delivery capacity, notify the
shopper, and expose order status with the correct delivery context.

Example request fragment:

```json
{
  "deliverySlotId": "slot-2026-06-14T18:00",
  "deliveryInstructions": {
    "dropOffLocation": "building_reception",
    "accessCodeRequired": true,
    "contactlessDelivery": true,
    "note": "Leave the package with reception after 18:00"
  }
}
```

## CheckoutService Database Impact

The order database needs a backward-compatible schema migration.

Add nullable columns to the `orders` table:

```sql
ALTER TABLE orders
    ADD COLUMN delivery_drop_off_location VARCHAR(64),
    ADD COLUMN delivery_access_code_required BOOLEAN DEFAULT FALSE NOT NULL,
    ADD COLUMN delivery_contactless BOOLEAN DEFAULT FALSE NOT NULL,
    ADD COLUMN delivery_instruction_note VARCHAR(280);
```

Constraints:

- `delivery_drop_off_location` must use a controlled value set such as
  `front_door`, `building_reception`, `locker`, or `neighbor`.
- `delivery_instruction_note` must be optional, length-limited, and excluded from
  operational logs.
- Existing orders must remain valid with default values.
- Future retention and privacy rules must treat the note as personal data because
  it can contain free text.

## Kafka Message Impact

The checkout saga must add `deliveryInstructions` to events sent through Kafka.

Affected messages:

- `delivery.slot.requested`
- `order.created`
- `order.confirmed`

New message fragment:

```json
{
  "orderId": "order-123",
  "sagaId": "saga-456",
  "schemaVersion": 3,
  "deliveryInstructions": {
    "dropOffLocation": "building_reception",
    "accessCodeRequired": true,
    "contactlessDelivery": true,
    "notePresent": true
  }
}
```

Kafka contract rules:

- Do not publish the free-text `note` by default.
- Publish `notePresent` so downstream services know extra delivery context exists.
- If the delivery service needs the full note, use a service-to-service lookup with
  field-level authorization instead of broadcasting free text to every consumer.
- Increment the event schema version and keep consumers compatible with previous
  versions during rollout.

## Acceptance Criteria

- Given a checkout request with delivery instructions, when `CheckoutService`
  confirms the order, then the order database stores the structured delivery
  instruction fields.
- Given a checkout request without delivery instructions, when `CheckoutService`
  confirms the order, then existing order creation behavior is unchanged.
- Given a confirmed order with delivery instructions, when the checkout saga emits
  `delivery.slot.requested`, `order.created`, or `order.confirmed`, then each
  message includes the structured non-free-text delivery instruction fields.
- Given a delivery instruction note is provided, when Kafka events are emitted,
  then the free-text note is not included in the Kafka payload.
- Given an older consumer reads the event, when `schemaVersion` changes, then the
  consumer can ignore the new `deliveryInstructions` field without failing.

## Testing Notes

- Add database migration tests for nullable/default column behavior.
- Add unit tests for request validation and mapping into the order aggregate.
- Add contract tests for Kafka message schema version `3`.
- Add privacy-safe logging tests to verify free-text notes are not logged or
  emitted into Kafka payloads.
