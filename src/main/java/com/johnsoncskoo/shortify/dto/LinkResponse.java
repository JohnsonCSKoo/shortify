package com.johnsoncskoo.shortify.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkResponse {
    private String id;
    private String url;
}
