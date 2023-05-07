package com.serwertetowy.controllers;

import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    private final TagRepository tagRepository;
    record TagRequest(String name){}

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Tags> getTags(){
        return tagRepository.findAllByOrderByIdAsc();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tags> getById(@PathVariable Integer id){
        Optional<Tags> tag = tagRepository.findById(id);
        return tag.map(tags -> new ResponseEntity<>(tags, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<Tags> addTag(@RequestBody TagRequest request){
        Tags tag = new Tags();
        tag.setName(request.name);
        tagRepository.save(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Tags> deleteTag(@PathVariable Integer id){
        if(tagRepository.existsById(id)){
            tagRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Tags> updateTag(@PathVariable Integer id, @RequestBody TagRequest request){
        Optional<Tags> tag = tagRepository.findById(id);
        if(tag.isPresent()){
            Tags tag1 = tag.get();
            tag1.setName(request.name);
            tagRepository.save(tag1);
            return new ResponseEntity<>(tag1, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
