package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.services.TypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Type getTypeById(@PathVariable long typeId) {
        return typeService.getOneTypeById(typeId);
    }
    @PostMapping
    public Type addType(@RequestBody Type type) {
        return typeService.saveOneType(type);
    }
    @PutMapping("/{typeId}")
    public Type updateType(@PathVariable long typeId,@RequestBody Type type) {
        return typeService.updateOneType(typeId,type);
    }
    @DeleteMapping("/{typeId}")
    public void deleteTypeById(@PathVariable long typeId) {
        typeService.deleteOneType(typeId);
    }
}
