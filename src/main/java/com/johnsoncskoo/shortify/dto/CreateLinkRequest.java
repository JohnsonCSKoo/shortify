package com.johnsoncskoo.shortify.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkRequest {
    @NotNull private String url;
}
