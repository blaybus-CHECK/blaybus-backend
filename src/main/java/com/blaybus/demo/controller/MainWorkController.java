package com.blaybus.demo.controller;

import com.blaybus.demo.MainWorkPropertyValidator;
import com.blaybus.demo.command.RegisterMainWorkCommand;
import com.blaybus.demo.entity.MainWork;
import com.blaybus.demo.entity.MainWorkRepository;
import com.blaybus.demo.view.MainWorkView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

import static com.blaybus.demo.MainWorkPropertyValidator.*;

@RestController
public record MainWorkController(
    MainWorkRepository repository
) {

    @PostMapping("/api/main-work")
    ResponseEntity<?> registerMainWork(@RequestBody RegisterMainWorkCommand command, Principal user) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }
        UUID id = UUID.randomUUID();
        MainWork mainWork = new MainWork();
        mainWork.setId(id);
        mainWork.setMemberId(UUID.fromString(user.getName()));
        mainWork.setTitle(command.title());
        mainWork.setDescription(command.description());
        repository.save(mainWork);

        URI uri = URI.create("/api/main-work/" + id);
        return ResponseEntity.created(uri).build();
    }

    private static boolean isCommandValid(RegisterMainWorkCommand command) {
        return isTitleValid(command)
            && isDescriptionValid(command);
//            && isImageUrlsValid(command);
    }

    @GetMapping("/api/main-work/{id}")
    ResponseEntity<?> getMainWork(@PathVariable UUID id, Principal user) {
        UUID memberId = UUID.fromString(user.getName());
        return repository
            .findById(id)
            .filter(mainWork -> mainWork.getMemberId().equals(memberId))
            .map(mainWork -> new MainWorkView(
                mainWork.getId(),
                mainWork.getTitle(),
                mainWork.getDescription()
            )
            )
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
