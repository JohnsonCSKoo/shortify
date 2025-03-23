package com.johnsoncskoo.shortify.service.impl;

import com.johnsoncskoo.shortify.dto.*;
import com.johnsoncskoo.shortify.service.LinkService;
import com.johnsoncskoo.shortify.exception.ResourceNotFoundException;
import com.johnsoncskoo.shortify.model.Link;
import com.johnsoncskoo.shortify.repository.LinkRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Value("${shortify.word-list}")
    private String WORD_LIST_PATH;
    private List<String> keywords;

    @PostConstruct
    public void init() throws IOException {
        try (var reader = this.getClass().getClassLoader().getResourceAsStream(WORD_LIST_PATH)) {
            if (reader == null) {
                throw new FileNotFoundException("Word list not found.");
            }

            keywords = new BufferedReader(new InputStreamReader(reader))
                    .lines()
                    .toList();
        } catch (IOException e) {
            throw new IOException("An error occurred while reading word list.");
        }
    }

    @Override
    public LinkResponse createLink(CreateLinkRequest request) {
        var existingUrl = linkRepository.findByUrlIgnoreCase(request.getUrl());

        if (existingUrl != null) {
            return LinkMapper.toLinkResponse(existingUrl);
        }

        var id = generateUniqueLink();

        var link = Link.builder()
                .id(id)
                .url(request.getUrl().toLowerCase())
                .build();

        var savedLink = linkRepository.save(link);
        return LinkMapper.toLinkResponse(savedLink);
    }

    @Override
    public LinkResponse getLink(GetLinkRequest request) {
        var link = linkRepository.findByIdIgnoreCase(request.getId());
        if (link == null) {
            throw ResourceNotFoundException.toException(Link.class, request.getId());
        }

        return LinkMapper.toLinkResponse(link);
    }

    @Override
    public LinkResponse updateLink(UpdateLinkRequest request) {
        var link = linkRepository.findByIdIgnoreCase(request.getId());
        if (link == null) {
            throw ResourceNotFoundException.toException(Link.class, request.getId());
        }

        link.setUrl(request.getUrl().toLowerCase());
        var updatedLink = linkRepository.save(link);
        return LinkMapper.toLinkResponse(updatedLink);
    }

    @Override
    public void deleteLink(DeleteLinkRequest request) {
        if (linkRepository.existsByIdAndUrl(request.getUrl().toLowerCase(), request.getUrl().toLowerCase())) {
            linkRepository.deleteByIdIgnoreCase(request.getId());
        } else {
            throw ResourceNotFoundException.toException(Link.class, request.getId());
        }
    }

    private String generateUniqueLink() {
        int retryCount = 0;
        int maxRetries = 10;

        var operand = this.keywords.size() + 1;
        var currentTimestamp = System.currentTimeMillis();

        while (retryCount < maxRetries) {
            var offset1 = Math.random() * operand;
            var offset2 = Math.random() * operand;

            var word1 = this.keywords.get(Math.abs((int) currentTimestamp % operand));
            var word2 = this.keywords.get(Math.abs((int) ((currentTimestamp + offset1) % operand)));
            var word3 = this.keywords.get(Math.abs((int) ((currentTimestamp + offset2) % operand)));
            var newId = word1 + word2 + word3;

            if (!linkRepository.existsByIdIgnoreCase(newId)) {
                return newId;
            }

            retryCount++;
        }

        throw new RuntimeException("Failed to generate unique link after " + maxRetries + " attempts.");
    }
}
