package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.services.TypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
public class TypeController {

    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<Type> getTypeById(@PathVariable long typeId) {
        Type type = typeService.getOneTypeById(typeId);
        if (type == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(type);
    }

    @PostMapping
    public ResponseEntity<Type> addType(@RequestBody Type type) {
        Type createdType = typeService.saveOneType(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdType);
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<Type> updateType(@PathVariable long typeId, @RequestBody Type type) {
        Type updatedType = typeService.updateOneType(typeId, type);
        if (updatedType == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedType);
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<Void> deleteTypeById(@PathVariable long typeId) {
        Type type = typeService.getOneTypeById(typeId);
        if (type == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        typeService.deleteOneType(typeId);
        return ResponseEntity.noContent().build();
    }
}
