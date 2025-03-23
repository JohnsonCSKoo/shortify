package com.johnsoncskoo.shortify.dto;

import com.johnsoncskoo.shortify.model.Link;

public class LinkMapper {
    public static Link toLink(LinkResponse linkResponse) {
        return Link.builder()
                .id(linkResponse.getId())
                .url(linkResponse.getUrl())
                .build();
    }

    public static LinkResponse toLinkResponse(Link link) {
        return LinkResponse.builder()
                .id(link.getId())
                .url(link.getUrl())
                .build();
    }
}
