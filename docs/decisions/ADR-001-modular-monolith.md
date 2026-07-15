# ADR-001: Use Modular Monolith Architecture

## Status

Accepted

## Context

The Ecommerce platform needs a scalable architecture
that supports future growth while keeping initial
development and deployment simple.

## Decision

We will implement the system as a Modular Monolith.

Each business capability will be isolated into
a separate module with clear boundaries.

## Consequences

Positive:

- Easier development and deployment
- Clear domain boundaries
- Easier future migration to microservices

Negative:

- Requires discipline to maintain boundaries
- More architectural planning is required