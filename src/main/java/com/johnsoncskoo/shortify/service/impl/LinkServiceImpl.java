package com.johnsoncskoo.shortify.service.impl;

import com.johnsoncskoo.shortify.dto.LinkMapper;
import com.johnsoncskoo.shortify.service.LinkService;
import com.johnsoncskoo.shortify.dto.LinkDTO;
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
    public LinkDTO createLink(String url) {
        var existingUrl = linkRepository.findByUrl(url);

        if (existingUrl != null) {
            return LinkMapper.toLinkDTO(existingUrl);
        }

        var id = generateUniqueLink();

        var link = Link.builder()
                .id(id)
                .url(url)
                .build();

        var savedLink = linkRepository.save(link);
        return LinkMapper.toLinkDTO(savedLink);
    }

    @Override
    public LinkDTO getLink(String id) {
        var link = linkRepository.findByIdIgnoreCase(id);
        if (link == null) {
            throw ResourceNotFoundException.toException(Link.class, id);
        }

        return LinkMapper.toLinkDTO(link);
    }

    @Override
    public LinkDTO updateLink(String id, String url) {
        var link = linkRepository.findByIdIgnoreCase(id);
        if (link == null) {
            throw ResourceNotFoundException.toException(Link.class, id);
        }

        link.setUrl(url);
        var updatedLink = linkRepository.save(link);
        return LinkMapper.toLinkDTO(updatedLink);
    }

    @Override
    public void deleteLink(String id, String url) {
        if (linkRepository.existsByIdAndUrl(id.toLowerCase(), url.toLowerCase())) {
            linkRepository.deleteById(id.toLowerCase());
        } else {
            throw ResourceNotFoundException.toException(Link.class, id);
        }
    }

    private String generateUniqueLink() {
        var operand = this.keywords.size() + 1;
        var currentTimestamp = System.currentTimeMillis();
        var offset1 = Math.random() * operand;
        var offset2 = Math.random() * operand;

        var word1 = this.keywords.get(Math.abs((int) currentTimestamp % operand));
        var word2 = this.keywords.get(Math.abs((int) ((currentTimestamp + offset1) % operand)));
        var word3 = this.keywords.get(Math.abs((int) ((currentTimestamp + offset2) % operand)));
        var newId = word1 + word2 + word3;

        if (linkRepository.existsByIdIgnoreCase(newId)) {
            return generateUniqueLink();
        }

        return newId;
    }
}
