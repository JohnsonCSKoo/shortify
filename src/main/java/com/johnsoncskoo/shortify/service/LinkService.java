package com.johnsoncskoo.shortify.service;

import com.johnsoncskoo.shortify.dto.*;

public interface LinkService {
    LinkResponse createLink(CreateLinkRequest request);
    LinkResponse getLink(GetLinkRequest request);
    LinkResponse updateLink(UpdateLinkRequest request);
    void deleteLink(DeleteLinkRequest request);
}
