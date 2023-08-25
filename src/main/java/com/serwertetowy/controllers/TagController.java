package com.serwertetowy.controllers;

import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    private final TagRepository tagRepository;
    //not sure if that's needed
    record TagRequest(String name){}

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    //get for all tags
    @GetMapping
    public List<Tags> getAllTags(){
        return tagRepository.findAllByOrderByIdAsc();
    }
    //get for specific tag data
    @GetMapping("/{id}")
    public ResponseEntity<Tags> getTagById(@PathVariable Integer id){
        Optional<Tags> tag = tagRepository.findById(id);
        return tag.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //post for adding a tag
    @PostMapping
    public ResponseEntity<Tags> addTag(@RequestBody TagRequest request){
        Tags tag = new Tags();
        tag.setName(request.name);
        tagRepository.save(tag);
        return ResponseEntity.ok(tag);
    }
    //delete for a tag (may cause problems with series)
    @DeleteMapping("/{id}")
    public ResponseEntity<Tags> deleteTag(@PathVariable Integer id){
        if(tagRepository.existsById(id)){
            tagRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.notFound().build();
    }
    //put tag request
    @PutMapping("/{id}")
    public ResponseEntity<Tags> updateTag(@PathVariable Integer id, @RequestBody TagRequest request){
        Optional<Tags> tag = tagRepository.findById(id);
        if(tag.isPresent()){
            Tags updatedTag = tag.get();
            updatedTag.setName(request.name);
            tagRepository.save(updatedTag);
            return ResponseEntity.ok(updatedTag);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
