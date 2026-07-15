# ADR-002: Authentication Strategy

## Status

Accepted

## Context

The ecommerce platform requires secure authentication
for customers, sellers, and administrators.

The system should support future scalability and
possible migration to microservices.

## Decision

We will use JWT based authentication with Refresh Tokens.

Access tokens will be short-lived and refresh tokens
will be stored and managed separately.

Authorization will use Role Based Access Control (RBAC).

## Consequences

Positive:

- Stateless authentication
- Scalable API design
- Suitable for distributed systems
- Industry standard approach

Negative:

- Token lifecycle management is required
- More complexity compared to sessions