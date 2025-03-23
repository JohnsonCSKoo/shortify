package com.johnsoncskoo.shortify.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLinkRequest {
    @NotNull private String id;
}
