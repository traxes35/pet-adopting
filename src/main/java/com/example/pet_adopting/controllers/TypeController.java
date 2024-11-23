package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.services.TypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/types")
public class TypeController {
    TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }
    @GetMapping
    public List<Type> getAllTypes() {
     return typeService.getAllTypes();
    }
    @GetMapping("/{typeId}")
    public Optional<Type> getTypeById(long typeId) {
        return typeService.getOneTypeById(typeId);
    }
    @PostMapping
    public Type addType(Type type) {
        return typeService.saveOneType(type);
    }
    @PutMapping("/{typeId}")
    public Type updateType(long typeId,Type type) {
        return typeService.updateOneType(typeId,type);
    }
    @DeleteMapping("/typeId")
    public void deleteTypeById(long typeId) {
        typeService.deleteOneType(typeId);
    }
}
