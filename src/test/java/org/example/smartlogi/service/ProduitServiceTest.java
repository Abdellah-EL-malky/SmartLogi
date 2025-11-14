package org.example.smartlogi.service;

import org.example.smartlogi.dto.ProduitDTO;
import org.example.smartlogi.entity.Produit;
import org.example.smartlogi.mapper.ProduitMapper;
import org.example.smartlogi.mapper.ProduitMapperImpl;
import org.example.smartlogi.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    private final ProduitMapper produitMapper = new ProduitMapperImpl();

    private ProduitService produitService;
    private ProduitDTO dtoRequest;

    @BeforeEach
    void setup() {
        produitService = new ProduitService(produitRepository, produitMapper);

        dtoRequest = new ProduitDTO();
        dtoRequest.setNom("Laptop Dell");
        dtoRequest.setCategorie("Informatique");
        dtoRequest.setPoids(BigDecimal.valueOf(2.5));
        dtoRequest.setPrix(BigDecimal.valueOf(8500.00));
    }

    @Test
    public void createProduit_withSuccess() {
        //Arrange
        when(produitRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ProduitDTO retour = produitService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("Laptop Dell", retour.getNom());
        assertEquals("Informatique", retour.getCategorie());
        assertEquals(0, BigDecimal.valueOf(2.5).compareTo(retour.getPoids()));
        assertEquals(0, BigDecimal.valueOf(8500.00).compareTo(retour.getPrix()));
    }

    @Test
    public void findById_withSuccess() {
        //Arrange
        Produit produit = produitMapper.toEntity(dtoRequest);
        produit.setId(1L);
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        //Act
        ProduitDTO retour = produitService.findById(1L);

        //Assert
        assertNotNull(retour);
        assertEquals("Laptop Dell", retour.getNom());
        assertEquals("Informatique", retour.getCategorie());
    }

    @Test
    public void findById_withError() {
        //Arrange
        when(produitRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> produitService.findById(999L));
    }

    @Test
    public void findAll_withSuccess() {
        //Arrange
        Produit produit1 = produitMapper.toEntity(dtoRequest);
        produit1.setId(1L);

        ProduitDTO dto2 = new ProduitDTO();
        dto2.setNom("iPhone 15");
        dto2.setCategorie("Electronique");
        dto2.setPoids(BigDecimal.valueOf(0.2));
        dto2.setPrix(BigDecimal.valueOf(12000.00));
        Produit produit2 = produitMapper.toEntity(dto2);
        produit2.setId(2L);

        when(produitRepository.findAll()).thenReturn(Arrays.asList(produit1, produit2));

        //Act
        List<ProduitDTO> retour = produitService.findAll();

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertEquals("Laptop Dell", retour.get(0).getNom());
        assertEquals("iPhone 15", retour.get(1).getNom());
    }

    @Test
    public void findAllPaginated_withSuccess() {
        //Arrange
        Produit produit = produitMapper.toEntity(dtoRequest);
        produit.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(Arrays.asList(produit), pageable, 1);
        when(produitRepository.findAll(pageable)).thenReturn(page);

        //Act
        Page<ProduitDTO> retour = produitService.findAll(pageable);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.getTotalElements());
        assertEquals("Laptop Dell", retour.getContent().get(0).getNom());
    }

    @Test
    public void updateProduit_withSuccess() {
        //Arrange
        Produit existingProduit = produitMapper.toEntity(dtoRequest);
        existingProduit.setId(1L);

        ProduitDTO updateDTO = new ProduitDTO();
        updateDTO.setNom("Laptop HP");
        updateDTO.setCategorie("Informatique");
        updateDTO.setPoids(BigDecimal.valueOf(2.3));
        updateDTO.setPrix(BigDecimal.valueOf(9000.00));

        when(produitRepository.findById(1L)).thenReturn(Optional.of(existingProduit));
        when(produitRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ProduitDTO retour = produitService.update(1L, updateDTO);

        //Assert
        assertNotNull(retour);
        verify(produitRepository).save(any());
    }

    @Test
    public void updateProduit_withError() {
        //Arrange
        when(produitRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> produitService.update(999L, dtoRequest));
        verify(produitRepository, never()).save(any());
    }

    @Test
    public void deleteProduit_withSuccess() {
        //Arrange
        when(produitRepository.existsById(1L)).thenReturn(true);

        //Act
        produitService.delete(1L);

        //Assert
        verify(produitRepository).deleteById(1L);
    }

    @Test
    public void deleteProduit_withError() {
        //Arrange
        when(produitRepository.existsById(999L)).thenReturn(false);

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> produitService.delete(999L));
        verify(produitRepository, never()).deleteById(anyLong());
    }

    @Test
    public void findByCategorie_withSuccess() {
        //Arrange
        Produit produit = produitMapper.toEntity(dtoRequest);
        when(produitRepository.findByCategorie("Informatique")).thenReturn(Arrays.asList(produit));

        //Act
        List<ProduitDTO> retour = produitService.findByCategorie("Informatique");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("Informatique", retour.get(0).getCategorie());
    }

    @Test
    public void findByCategorie_withNoResults() {
        //Arrange
        when(produitRepository.findByCategorie("Alimentation")).thenReturn(Arrays.asList());

        //Act
        List<ProduitDTO> retour = produitService.findByCategorie("Alimentation");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }

    @Test
    public void searchByNom_withSuccess() {
        //Arrange
        Produit produit = produitMapper.toEntity(dtoRequest);
        when(produitRepository.findByNomContainingIgnoreCase("Laptop")).thenReturn(Arrays.asList(produit));

        //Act
        List<ProduitDTO> retour = produitService.searchByNom("Laptop");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertTrue(retour.get(0).getNom().contains("Laptop"));
    }

    @Test
    public void searchByNom_withNoResults() {
        //Arrange
        when(produitRepository.findByNomContainingIgnoreCase("XYZ")).thenReturn(Arrays.asList());

        //Act
        List<ProduitDTO> retour = produitService.searchByNom("XYZ");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }

    @Test
    public void findByCategorieOrderByPrix_withSuccess() {
        //Arrange
        Produit produit1 = produitMapper.toEntity(dtoRequest);
        produit1.setPrix(BigDecimal.valueOf(8500));

        ProduitDTO dto2 = new ProduitDTO();
        dto2.setNom("Laptop HP");
        dto2.setCategorie("Informatique");
        dto2.setPoids(BigDecimal.valueOf(2.3));
        dto2.setPrix(BigDecimal.valueOf(7500.00));
        Produit produit2 = produitMapper.toEntity(dto2);

        when(produitRepository.findByCategorieOrderByPrixAsc("Informatique"))
                .thenReturn(Arrays.asList(produit2, produit1));

        //Act
        List<ProduitDTO> retour = produitService.findByCategorieOrderByPrix("Informatique");

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertTrue(retour.get(0).getPrix().compareTo(retour.get(1).getPrix()) <= 0);
    }
}