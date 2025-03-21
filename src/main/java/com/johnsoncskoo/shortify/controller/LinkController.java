package com.johnsoncskoo.shortify.controller;

import com.johnsoncskoo.shortify.service.LinkService;
import com.johnsoncskoo.shortify.dto.LinkDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/")
    public ResponseEntity<LinkDTO> createLink(
            @RequestBody String url
    ) {
        var response = linkService.createLink(url);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<LinkDTO> updateLink(
            @PathVariable String id,
            @RequestBody String url
    ) {
        var response = linkService.updateLink(id, url);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteLink(
            @PathVariable String id,
            @RequestBody String url
    ) {
        linkService.deleteLink(id, url);
        return HttpStatus.OK;
    }
}
