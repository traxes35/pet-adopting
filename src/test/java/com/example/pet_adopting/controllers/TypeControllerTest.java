package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.services.TypeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeControllerTest {

    private TypeController typeController;
    private TypeService typeService;

    @Before
    public void setUp() {
        typeService = Mockito.mock(TypeService.class);
        typeController = new TypeController(typeService);
    }

    @Test
    public void whenGetAllTypesCalled_itShouldReturnTypeList() {
        // Arrange
        Type type1 = new Type();
        type1.setId(1L);
        type1.setName("Dog");

        Type type2 = new Type();
        type2.setId(2L);
        type2.setName("Cat");

        List<Type> typeList = List.of(type1, type2);
        when(typeService.getAllTypes()).thenReturn(typeList);

        // Act
        ResponseEntity<List<Type>> response = typeController.getAllTypes();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(typeList, response.getBody());
        verify(typeService).getAllTypes();
    }

    @Test
    public void whenGetTypeByIdCalledWithValidId_itShouldReturnType() {
        // Arrange
        Long typeId = 1L;
        Type type = new Type();
        type.setId(typeId);
        type.setName("Dog");

        when(typeService.getOneTypeById(typeId)).thenReturn(type);

        // Act
        ResponseEntity<Type> response = typeController.getTypeById(typeId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(type, response.getBody());
        verify(typeService).getOneTypeById(typeId);
    }

    @Test
    public void whenGetTypeByIdCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long typeId = 99L;
        when(typeService.getOneTypeById(typeId)).thenReturn(null);

        // Act
        ResponseEntity<Type> response = typeController.getTypeById(typeId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(typeService).getOneTypeById(typeId);
    }

    @Test
    public void whenAddTypeCalledWithValidRequest_itShouldReturnCreatedType() {
        // Arrange
        Type type = new Type();
        type.setName("Bird");

        Type createdType = new Type();
        createdType.setId(3L);
        createdType.setName("Bird");

        when(typeService.saveOneType(type)).thenReturn(createdType);

        // Act
        ResponseEntity<Type> response = typeController.addType(type);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdType, response.getBody());
        verify(typeService).saveOneType(type);
    }

    @Test
    public void whenUpdateTypeCalledWithValidId_itShouldReturnUpdatedType() {
        // Arrange
        Long typeId = 1L;
        Type type = new Type();
        type.setName("Updated Dog");

        Type updatedType = new Type();
        updatedType.setId(typeId);
        updatedType.setName("Updated Dog");

        when(typeService.updateOneType(typeId, type)).thenReturn(updatedType);

        // Act
        ResponseEntity<Type> response = typeController.updateType(typeId, type);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedType, response.getBody());
        verify(typeService).updateOneType(typeId, type);
    }

    @Test
    public void whenUpdateTypeCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long typeId = 99L;
        Type type = new Type();
        type.setName("Non-existent Type");

        when(typeService.updateOneType(typeId, type)).thenReturn(null);

        // Act
        ResponseEntity<Type> response = typeController.updateType(typeId, type);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(typeService).updateOneType(typeId, type);
    }

    @Test
    public void whenDeleteTypeByIdCalledWithValidId_itShouldReturnNoContent() {
        // Arrange
        Long typeId = 1L;
        Type type = new Type();
        type.setId(typeId);

        when(typeService.getOneTypeById(typeId)).thenReturn(type);

        // Act
        ResponseEntity<Void> response = typeController.deleteTypeById(typeId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(typeService).getOneTypeById(typeId);
        verify(typeService).deleteOneType(typeId);
    }

    @Test
    public void whenDeleteTypeByIdCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long typeId = 99L;
        when(typeService.getOneTypeById(typeId)).thenReturn(null);

        // Act
        ResponseEntity<Void> response = typeController.deleteTypeById(typeId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(typeService).getOneTypeById(typeId);
        verify(typeService, never()).deleteOneType(typeId);
    }
}
