package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.repos.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {
    private TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAllTypes() {
    return typeRepository.findAll();
    }

    public Type getOneTypeById(long id) {
    return typeRepository.findById(id).orElse(null);
    }

    public Type saveOneType(Type newType) {
    return typeRepository.save(newType);
    }

    public Type updateOneType(long typeId, Type newType) {
     Optional<Type> type = typeRepository.findById(typeId);
     if (type.isPresent()) {
         type.get().setName(newType.getName());
         return typeRepository.save(type.get());
     }else {
         return null;
     }
    }
    public void deleteOneType(long typeId) {
        typeRepository.deleteById(typeId);
    }
}
