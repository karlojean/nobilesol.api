package com.br.nobilesol.event;

public record EmployeeCreatedEvent(
        String name,
        String email,
        String temporaryPassword
) {}