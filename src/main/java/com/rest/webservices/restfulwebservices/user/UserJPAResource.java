/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author barno
 */
@RestController
public class UserJPAResource {
   
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private PostRepository postRepo;
    
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepo.findAll();
        
    }
    
    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/jpa/users/{id}")
    public Optional<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepo.findById(id);
        
        if(!user.isPresent())
            throw new UserNotFoundException("id-" + id);
        
  
        return user;
    }
    
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
        
   }
    
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = userRepo.save(user);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        
        return ResponseEntity.created(location).build();
        
    }
    
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrieveAllUsers(@PathVariable int id) {
        Optional <User> userOptional = userRepo.findById(id);
        
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("id-" + id);
        }
        
        return userOptional.get().getPosts();
        
    }
    
    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional <User> userOptional = userRepo.findById(id);
        
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("id-" + id);
        }
        
        User user = userOptional.get();
        
        post.setUser(user);
        
        postRepo.save(post);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId()).toUri();
        
        return ResponseEntity.created(location).build();
        
    }
    
}
