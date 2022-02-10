package az.edadi.back.controller;

import az.edadi.back.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    ResponseEntity addTopic(@RequestBody String title) {
        return ResponseEntity.ok(topicService.addTopic(title));
    }

    @GetMapping
    ResponseEntity getTopics(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(topicService.getTopicList(page));
    }

    @GetMapping("/{id}")
    ResponseEntity getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }
}
