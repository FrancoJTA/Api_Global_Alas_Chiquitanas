package com.example.global_api.Controller;

import com.example.global_api.Class.User;
import com.example.global_api.Service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/global_registro")
public class RegistrarController {

    @Autowired
    private RegistroService registroService;

    // Franco
    @PostMapping("/alasA")
    public ResponseEntity<Boolean> registoAlasA(@RequestBody User user) {
        return ResponseEntity.ok(registroService.registroUsuario(user,0));
    }
    //Bruno
    @PostMapping("/alasB")
    public ResponseEntity<Boolean> registoAlasB(@RequestBody User user) {
        return ResponseEntity.ok(registroService.registroUsuario(user,1));
    }
    //Jhair
    @PostMapping("/alasC")
    public ResponseEntity<Boolean> registoAlasC(@RequestBody User user) {
        return ResponseEntity.ok(registroService.registroUsuario(user,2));
    }
    //Renato
    @PostMapping("/alasD")
    public ResponseEntity<Boolean> registoAlasD(@RequestBody User user) {
        return ResponseEntity.ok(registroService.registroUsuario(user,3));
    }
    //Diogo
    @PostMapping("/alasE")
    public ResponseEntity<Boolean> registoAlasE(@RequestBody User user) {
        return ResponseEntity.ok(registroService.registroUsuario(user,4));

    }


}

