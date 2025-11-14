package org.example.smartlogi.service;

import org.example.smartlogi.dto.DestinataireDTO;
import org.example.smartlogi.entity.Destinataire;
import org.example.smartlogi.mapper.DestinataireMapper;
import org.example.smartlogi.mapper.DestinataireMapperImpl;
import org.example.smartlogi.repository.DestinataireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DestinataireServiceTest {

    @Mock
    private DestinataireRepository destinataireRepository;

    private final DestinataireMapper destinataireMapper = new DestinataireMapperImpl();

    private DestinataireService destinataireService;
    private DestinataireDTO dtoRequest;

    @BeforeEach
    void setup() {
        destinataireService = new DestinataireService(destinataireRepository, destinataireMapper);

        dtoRequest = new DestinataireDTO();
        dtoRequest.setEmail("fatima@email.com");
        dtoRequest.setAdresse("123 Rue Test, Casablanca");
        dtoRequest.setNom("Alami");
        dtoRequest.setPrenom("Fatima");
        dtoRequest.setTelephone("0612345678");
    }

    @Test
    public void createDestinataire_withSuccess() {
        //Arrange
        when(destinataireRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        DestinataireDTO retour = destinataireService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("Alami", retour.getNom());
        assertEquals("Fatima", retour.getPrenom());
        assertEquals("fatima@email.com", retour.getEmail());
    }

    @Test
    public void findById_withSuccess() {
        //Arrange
        Destinataire destinataire = destinataireMapper.toEntity(dtoRequest);
        destinataire.setId(1L);
        when(destinataireRepository.findById(1L)).thenReturn(Optional.of(destinataire));

        //Act
        DestinataireDTO retour = destinataireService.findById(1L);

        //Assert
        assertNotNull(retour);
        assertEquals("Alami", retour.getNom());
        assertEquals("fatima@email.com", retour.getEmail());
    }

    @Test
    public void findById_withError() {
        //Arrange
        when(destinataireRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> destinataireService.findById(999L));
    }

    @Test
    public void findAll_withSuccess() {
        //Arrange
        Destinataire dest1 = destinataireMapper.toEntity(dtoRequest);
        dest1.setId(1L);

        DestinataireDTO dto2 = new DestinataireDTO();
        dto2.setNom("Bennani");
        dto2.setPrenom("Hassan");
        dto2.setEmail("hassan@email.com");
        dto2.setTelephone("0698765432");
        dto2.setAdresse("456 Avenue Test");
        Destinataire dest2 = destinataireMapper.toEntity(dto2);
        dest2.setId(2L);

        when(destinataireRepository.findAll()).thenReturn(Arrays.asList(dest1, dest2));

        //Act
        List<DestinataireDTO> retour = destinataireService.findAll();

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertEquals("Alami", retour.get(0).getNom());
        assertEquals("Bennani", retour.get(1).getNom());
    }

    @Test
    public void findAllPaginated_withSuccess() {
        //Arrange
        Destinataire dest = destinataireMapper.toEntity(dtoRequest);
        dest.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Destinataire> page = new PageImpl<>(Arrays.asList(dest), pageable, 1);
        when(destinataireRepository.findAll(pageable)).thenReturn(page);

        //Act
        Page<DestinataireDTO> retour = destinataireService.findAll(pageable);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.getTotalElements());
        assertEquals("Alami", retour.getContent().get(0).getNom());
    }

    @Test
    public void updateDestinataire_withSuccess() {
        //Arrange
        Destinataire existingDest = destinataireMapper.toEntity(dtoRequest);
        existingDest.setId(1L);

        DestinataireDTO updateDTO = new DestinataireDTO();
        updateDTO.setNom("NouveauNom");
        updateDTO.setPrenom("NouveauPrenom");
        updateDTO.setEmail("nouveau@email.com");
        updateDTO.setTelephone("0699999999");
        updateDTO.setAdresse("Nouvelle Adresse");

        when(destinataireRepository.findById(1L)).thenReturn(Optional.of(existingDest));
        when(destinataireRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        DestinataireDTO retour = destinataireService.update(1L, updateDTO);

        //Assert
        assertNotNull(retour);
        assertEquals("NouveauNom", retour.getNom());
        assertEquals("NouveauPrenom", retour.getPrenom());
        assertEquals("nouveau@email.com", retour.getEmail());
    }

    @Test
    public void updateDestinataire_withError() {
        //Arrange
        when(destinataireRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> destinataireService.update(999L, dtoRequest));
        verify(destinataireRepository, never()).save(any());
    }

    @Test
    public void deleteDestinataire_withSuccess() {
        //Arrange
        when(destinataireRepository.existsById(1L)).thenReturn(true);

        //Act
        destinataireService.delete(1L);

        //Assert
        verify(destinataireRepository).deleteById(1L);
    }

    @Test
    public void deleteDestinataire_withError() {
        //Arrange
        when(destinataireRepository.existsById(999L)).thenReturn(false);

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> destinataireService.delete(999L));
        verify(destinataireRepository, never()).deleteById(anyLong());
    }

    @Test
    public void findByEmail_withSuccess() {
        //Arrange
        Destinataire dest = destinataireMapper.toEntity(dtoRequest);
        dest.setId(1L);
        when(destinataireRepository.findByEmail("fatima@email.com")).thenReturn(Optional.of(dest));

        //Act
        DestinataireDTO retour = destinataireService.findByEmail("fatima@email.com");

        //Assert
        assertNotNull(retour);
        assertEquals("fatima@email.com", retour.getEmail());
        assertEquals("Alami", retour.getNom());
    }

    @Test
    public void findByEmail_withError() {
        //Arrange
        when(destinataireRepository.findByEmail("inconnu@email.com")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> destinataireService.findByEmail("inconnu@email.com"));
    }

    @Test
    public void findByTelephone_withSuccess() {
        //Arrange
        Destinataire dest = destinataireMapper.toEntity(dtoRequest);
        dest.setId(1L);
        when(destinataireRepository.findByTelephone("0612345678")).thenReturn(Optional.of(dest));

        //Act
        DestinataireDTO retour = destinataireService.findByTelephone("0612345678");

        //Assert
        assertNotNull(retour);
        assertEquals("0612345678", retour.getTelephone());
        assertEquals("Alami", retour.getNom());
    }

    @Test
    public void findByTelephone_withError() {
        //Arrange
        when(destinataireRepository.findByTelephone("0699999999")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> destinataireService.findByTelephone("0699999999"));
    }

    @Test
    public void searchByNom_withSuccess() {
        //Arrange
        Destinataire dest = destinataireMapper.toEntity(dtoRequest);
        when(destinataireRepository.findByNomContainingIgnoreCase("Ala")).thenReturn(Arrays.asList(dest));

        //Act
        List<DestinataireDTO> retour = destinataireService.searchByNom("Ala");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("Alami", retour.get(0).getNom());
    }

    @Test
    public void searchByNom_withNoResults() {
        //Arrange
        when(destinataireRepository.findByNomContainingIgnoreCase("XYZ")).thenReturn(Arrays.asList());

        //Act
        List<DestinataireDTO> retour = destinataireService.searchByNom("XYZ");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }

    @Test
    public void searchByVille_withSuccess() {
        //Arrange
        Destinataire dest = destinataireMapper.toEntity(dtoRequest);
        when(destinataireRepository.findByAdresseContainingIgnoreCase("Casablanca")).thenReturn(Arrays.asList(dest));

        //Act
        List<DestinataireDTO> retour = destinataireService.searchByVille("Casablanca");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertTrue(retour.get(0).getAdresse().contains("Casablanca"));
    }

    @Test
    public void searchByVille_withNoResults() {
        //Arrange
        when(destinataireRepository.findByAdresseContainingIgnoreCase("Paris")).thenReturn(Arrays.asList());

        //Act
        List<DestinataireDTO> retour = destinataireService.searchByVille("Paris");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }
}