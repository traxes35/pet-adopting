package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.repos.TypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TypeServiceTest {
    private TypeService typeService;
    private TypeRepository typeRepository;
    @org.junit.Before
    public void setUp() throws Exception {
        typeRepository = Mockito.mock(TypeRepository.class);
        typeService = new TypeService(typeRepository);
    }

    @Test
    public void whenCreateTypeCalledWithValidRequest_itShouldReturnType() {
        // Arrange
        Type type = Type.builder()
                .id(1L)
                .name("dog")
                .build();

        Mockito.when(typeRepository.save(type)).thenReturn(type);

        // Act
        Type result = typeService.saveOneType(type);

        // Assert
        assertNotNull(result); // Dönen sonuç null olmamalı
        assertEquals(type, result); // Dönen sonuç, kaydedilen tip olmalı

        // Verify
        Mockito.verify(typeRepository).save(type); // Save metodu doğru nesneyle çağrılmış mı
    }
    @Test
    public void whenGetTypeCalledWithValidId_itShouldReturnType() {
        // Arrange
        Long typeId = 1L;
        Type type = Type.builder()
                .id(typeId)
                .name("dog")
                .build();

        Mockito.when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));

        // Act
        Type result = typeService.getOneTypeById(typeId);

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertEquals(type.getId(), result.getId()); // ID eşleşmeli
        assertEquals("dog", result.getName()); // Name eşleşmeli

        // Verify
        Mockito.verify(typeRepository).findById(typeId); // findById doğru çağrılmış mı kontrol et
    }

    @Test
    public void whenGetTypeCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long invalidTypeId = 99L;

        Mockito.when(typeRepository.findById(invalidTypeId)).thenReturn(Optional.empty());

        // Act
        Type result = typeService.getOneTypeById(invalidTypeId);

        // Assert
        assertNull(result); // Sonuç null olmalı

        // Verify
        Mockito.verify(typeRepository).findById(invalidTypeId); // findById doğru ID ile çağrılmış mı
    }

    @Test
    public void whenGetAllTypesCalled_itShouldReturnTypeList() {
        // Arrange
        Type type1 = Type.builder()
                .id(1L)
                .name("dog")
                .build();

        Type type2 = Type.builder()
                .id(2L)
                .name("cat")
                .build();

        List<Type> typeList = List.of(type1, type2);

        Mockito.when(typeRepository.findAll()).thenReturn(typeList);

        // Act
        List<Type> result = typeService.getAllTypes();

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertEquals(2, result.size()); // Liste boyutu kontrolü
        assertEquals("dog", result.get(0).getName()); // İlk eleman kontrolü
        assertEquals("cat", result.get(1).getName()); // İkinci eleman kontrolü

        // Verify
        Mockito.verify(typeRepository).findAll(); // findAll çağrılmış mı kontrol et
    }

    @Test
    public void whenGetAllTypesCalledWithNoTypes_itShouldReturnEmptyList() {
        // Arrange
        Mockito.when(typeRepository.findAll()).thenReturn(List.of()); // Boş liste dön

        // Act
        List<Type> result = typeService.getAllTypes();

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertTrue(result.isEmpty()); // Liste boş olmalı

        // Verify
        Mockito.verify(typeRepository).findAll(); // findAll çağrılmış mı kontrol et
    }
}