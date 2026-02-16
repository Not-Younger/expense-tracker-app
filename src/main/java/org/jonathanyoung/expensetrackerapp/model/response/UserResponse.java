package org.jonathanyoung.expensetrackerapp.model.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        String token
) {
}
