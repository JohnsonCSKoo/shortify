package com.johnsoncskoo.shortify;

import com.johnsoncskoo.shortify.dto.LinkDTO;

public interface LinkService {
    LinkDTO createLink(String url);
    LinkDTO getLink(String id);
    LinkDTO updateLink(String id, String url);
    void deleteLink(String id, String url);
}
