package com.driver.controllers;

import com.driver.model.User;
import com.driver.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestParam String name, @RequestParam String phoneNumber, @RequestParam String password) {
        userService.register(name, phoneNumber, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updatePassword(@RequestParam Integer userId, @RequestParam String password) {
        User updatedUser = userService.updatePassword(userId, password);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam Integer userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
