package com.johnsoncskoo.shortify;

import com.johnsoncskoo.shortify.dto.CreateLinkRequest;
import com.johnsoncskoo.shortify.dto.GetLinkRequest;
import com.johnsoncskoo.shortify.exception.ResourceNotFoundException;
import com.johnsoncskoo.shortify.model.Link;
import com.johnsoncskoo.shortify.repository.LinkRepository;
import com.johnsoncskoo.shortify.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LinkServiceTests {

    @Autowired
    private LinkService linkService;

    @MockitoBean
    private LinkRepository linkRepository;

    @Test
    void createLink_newUrl_returnsNewLink() {
        // Arrange
        String url = "https://www.google.com";
        when(linkRepository.save(any(Link.class))).thenAnswer(invocation -> {
            return invocation.<Link>getArgument(0);
        });

        // Act
        var result = linkService.createLink(new CreateLinkRequest(url));

        // Assert
        assertNotNull(result);
        assertEquals(url, result.getUrl());
    }

    @Test
    void createLink_existingUrl_returnsExistingLink() {
        // Arrange
        String url = "https://www.google.com";
        Link existingLink = new Link("OneTwoThree", url);
        when(linkRepository.findByUrl(url)).thenReturn(existingLink);

        // Act
        var result = linkService.createLink(new CreateLinkRequest(url));

        // Assert
        assertNotNull(result);
        assertEquals(url, result.getUrl());
    }

    @Test
    void getLink_invalidId_throwsResourceNotFoundException() {
        // Arrange
        String id = "OneTwoThree";
        when(linkRepository.findById(id)).thenReturn(null);

        // Act, Assert
        assertThrows(ResourceNotFoundException.class, () -> linkService.getLink(new GetLinkRequest(id)));
    }

    @Test
    void getLink_validId_returnsExistingLink() {
        // Arrange
        String url = "https://google.com";
        String id = "OneTwoThree";
        Link existingLink = new Link(id, url);
        when(linkRepository.findByIdIgnoreCase(id)).thenReturn(existingLink);

        // Act
        var result = linkService.getLink(new GetLinkRequest(id));

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
