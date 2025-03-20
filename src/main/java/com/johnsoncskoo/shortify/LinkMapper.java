package com.johnsoncskoo.shortify;

import com.johnsoncskoo.shortify.dto.LinkDTO;
import com.johnsoncskoo.shortify.model.Link;

public class LinkMapper {
    public static Link toLink(LinkDTO linkDTO) {
        return Link.builder()
                .id(linkDTO.getId())
                .url(linkDTO.getUrl())
                .build();
    }

    public static LinkDTO toLinkDTO(Link link) {
        return LinkDTO.builder()
                .id(link.getId())
                .url(link.getUrl())
                .build();
    }
}
