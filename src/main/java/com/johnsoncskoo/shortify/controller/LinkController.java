package com.johnsoncskoo.shortify.controller;

import com.johnsoncskoo.shortify.dto.*;
import com.johnsoncskoo.shortify.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("/")
    public ResponseEntity<LinkResponse> createLink(
            @Validated @RequestBody final CreateLinkRequest linkRequest
    ) {
        var response = linkService.createLink(linkRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<LinkResponse> getLink(
            @PathVariable String id
    ) {
        var response = linkService.getLink(new GetLinkRequest(id));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<LinkResponse> updateLink(
            @PathVariable String id,
            @Validated @RequestBody final UpdateLinkRequest linkRequest
    ) {
        var response = linkService.updateLink(linkRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteLink(
            @PathVariable String id,
            @Validated @RequestBody final DeleteLinkRequest linkRequest
            ) {
        linkService.deleteLink(linkRequest);
        return HttpStatus.OK;
    }
}
