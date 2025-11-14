package org.example.smartlogi.service;

import org.example.smartlogi.dto.LivreurDTO;
import org.example.smartlogi.entity.Livreur;
import org.example.smartlogi.entity.Zone;
import org.example.smartlogi.mapper.LivreurMapper;
import org.example.smartlogi.mapper.LivreurMapperImpl;
import org.example.smartlogi.repository.LivreurRepository;
import org.example.smartlogi.repository.ZoneRepository;
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
public class LivreurServiceTest {

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private ZoneRepository zoneRepository;

    private final LivreurMapper livreurMapper = new LivreurMapperImpl();

    private LivreurService livreurService;
    private LivreurDTO dtoRequest;
    private Zone zoneTest;

    @BeforeEach
    void setup() {
        livreurService = new LivreurService(livreurRepository, zoneRepository, livreurMapper);

        // Créer une zone pour les tests
        zoneTest = new Zone();
        zoneTest.setId(1L);
        zoneTest.setNom("Zone Nord");
        zoneTest.setVille("Casablanca");
        zoneTest.setCodePostal("20000");

        dtoRequest = new LivreurDTO();
        dtoRequest.setNom("Bennani");
        dtoRequest.setPrenom("Mohammed");
        dtoRequest.setTelephone("0612345678");
        dtoRequest.setVehicule("Moto");
        dtoRequest.setZoneAssigneeId(1L);
        dtoRequest.setActif(true);
    }

    @Test
    public void createLivreur_withSuccess() {
        //Arrange
        when(livreurRepository.findByTelephone("0612345678")).thenReturn(Optional.empty());
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zoneTest));
        when(livreurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        LivreurDTO retour = livreurService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("Bennani", retour.getNom());
        assertEquals("Mohammed", retour.getPrenom());
        assertEquals("Moto", retour.getVehicule());
        verify(zoneRepository).findById(1L);
        verify(livreurRepository).save(any());
    }

    @Test
    public void createLivreur_withDuplicateTelephone_shouldThrowError() {
        //Arrange
        Livreur existingLivreur = new Livreur();
        existingLivreur.setId(2L);
        existingLivreur.setTelephone("0612345678");

        when(livreurRepository.findByTelephone("0612345678")).thenReturn(Optional.of(existingLivreur));

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> livreurService.create(dtoRequest));
        verify(livreurRepository, never()).save(any());
    }

    @Test
    public void createLivreur_withNonExistingZone_shouldThrowError() {
        //Arrange
        when(livreurRepository.findByTelephone("0612345678")).thenReturn(Optional.empty());
        when(zoneRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> livreurService.create(dtoRequest));
        verify(livreurRepository, never()).save(any());
    }

    @Test
    public void findById_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setId(1L);
        livreur.setZoneAssignee(zoneTest);
        when(livreurRepository.findById(1L)).thenReturn(Optional.of(livreur));

        //Act
        LivreurDTO retour = livreurService.findById(1L);

        //Assert
        assertNotNull(retour);
        assertEquals("Bennani", retour.getNom());
        assertEquals(1L, retour.getZoneAssigneeId());
    }

    @Test
    public void findById_withError() {
        //Arrange
        when(livreurRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> livreurService.findById(999L));
    }

    @Test
    public void findAll_withSuccess() {
        //Arrange
        Livreur livreur1 = livreurMapper.toEntity(dtoRequest);
        livreur1.setId(1L);
        livreur1.setZoneAssignee(zoneTest);

        LivreurDTO dto2 = new LivreurDTO();
        dto2.setNom("Alami");
        dto2.setPrenom("Ahmed");
        dto2.setTelephone("0698765432");
        dto2.setVehicule("Voiture");
        dto2.setZoneAssigneeId(1L);
        Livreur livreur2 = livreurMapper.toEntity(dto2);
        livreur2.setId(2L);
        livreur2.setZoneAssignee(zoneTest);

        when(livreurRepository.findAll()).thenReturn(Arrays.asList(livreur1, livreur2));

        //Act
        List<LivreurDTO> retour = livreurService.findAll();

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertEquals("Bennani", retour.get(0).getNom());
        assertEquals("Alami", retour.get(1).getNom());
    }

    @Test
    public void findAllPaginated_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setId(1L);
        livreur.setZoneAssignee(zoneTest);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(Arrays.asList(livreur), pageable, 1);
        when(livreurRepository.findAll(pageable)).thenReturn(page);

        //Act
        Page<LivreurDTO> retour = livreurService.findAll(pageable);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.getTotalElements());
        assertEquals("Bennani", retour.getContent().get(0).getNom());
    }

    @Test
    public void updateLivreur_withSuccess() {
        //Arrange
        Livreur existingLivreur = livreurMapper.toEntity(dtoRequest);
        existingLivreur.setId(1L);
        existingLivreur.setZoneAssignee(zoneTest);

        when(livreurRepository.findById(1L)).thenReturn(Optional.of(existingLivreur));
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zoneTest));
        when(livreurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        LivreurDTO retour = livreurService.update(1L, dtoRequest);

        //Assert
        assertNotNull(retour);
        verify(livreurRepository).save(any());
        verify(zoneRepository).findById(1L);
    }

    @Test
    public void updateLivreur_withError() {
        //Arrange
        when(livreurRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> livreurService.update(999L, dtoRequest));
        verify(livreurRepository, never()).save(any());
    }

    @Test
    public void updateLivreur_withDuplicateTelephone_shouldThrowError() {
        //Arrange
        Livreur existingLivreur = new Livreur();
        existingLivreur.setId(1L);
        existingLivreur.setTelephone("0611111111");

        Livreur otherLivreur = new Livreur();
        otherLivreur.setId(2L);
        otherLivreur.setTelephone("0622222222");

        LivreurDTO updateDTO = new LivreurDTO();
        updateDTO.setTelephone("0622222222");
        updateDTO.setZoneAssigneeId(1L);

        when(livreurRepository.findById(1L)).thenReturn(Optional.of(existingLivreur));
        when(livreurRepository.findByTelephone("0622222222")).thenReturn(Optional.of(otherLivreur));

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> livreurService.update(1L, updateDTO));
        verify(livreurRepository, never()).save(any());
    }

    @Test
    public void deleteLivreur_withSuccess() {
        //Arrange
        when(livreurRepository.existsById(1L)).thenReturn(true);

        //Act
        livreurService.delete(1L);

        //Assert
        verify(livreurRepository).deleteById(1L);
    }

    @Test
    public void deleteLivreur_withError() {
        //Arrange
        when(livreurRepository.existsById(999L)).thenReturn(false);

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> livreurService.delete(999L));
        verify(livreurRepository, never()).deleteById(anyLong());
    }

    @Test
    public void findByTelephone_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setId(1L);
        livreur.setZoneAssignee(zoneTest);
        when(livreurRepository.findByTelephone("0612345678")).thenReturn(Optional.of(livreur));

        //Act
        LivreurDTO retour = livreurService.findByTelephone("0612345678");

        //Assert
        assertNotNull(retour);
        assertEquals("0612345678", retour.getTelephone());
    }

    @Test
    public void findByTelephone_withError() {
        //Arrange
        when(livreurRepository.findByTelephone("0699999999")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> livreurService.findByTelephone("0699999999"));
    }

    @Test
    public void findByZone_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setZoneAssignee(zoneTest);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zoneTest));
        when(livreurRepository.findByZoneAssignee(zoneTest)).thenReturn(Arrays.asList(livreur));

        //Act
        List<LivreurDTO> retour = livreurService.findByZone(1L);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
    }

    @Test
    public void findActifs_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setZoneAssignee(zoneTest);
        livreur.setActif(true);

        when(livreurRepository.findByActif(true)).thenReturn(Arrays.asList(livreur));

        //Act
        List<LivreurDTO> retour = livreurService.findActifs();

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertTrue(retour.get(0).getActif());
    }

    @Test
    public void findActifsByZone_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setZoneAssignee(zoneTest);
        livreur.setActif(true);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zoneTest));
        when(livreurRepository.findByZoneAssigneeAndActif(zoneTest, true)).thenReturn(Arrays.asList(livreur));

        //Act
        List<LivreurDTO> retour = livreurService.findActifsByZone(1L);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
    }

    @Test
    public void searchByNom_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setZoneAssignee(zoneTest);

        when(livreurRepository.findByNomContainingIgnoreCase("Benn")).thenReturn(Arrays.asList(livreur));

        //Act
        List<LivreurDTO> retour = livreurService.searchByNom("Benn");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertTrue(retour.get(0).getNom().contains("Benn"));
    }

    @Test
    public void findByVehicule_withSuccess() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setZoneAssignee(zoneTest);

        when(livreurRepository.findByVehicule("Moto")).thenReturn(Arrays.asList(livreur));

        //Act
        List<LivreurDTO> retour = livreurService.findByVehicule("Moto");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("Moto", retour.get(0).getVehicule());
    }

    @Test
    public void setActif_shouldChangeStatusToInactive() {
        //Arrange
        Livreur livreur = livreurMapper.toEntity(dtoRequest);
        livreur.setId(1L);
        livreur.setZoneAssignee(zoneTest);
        livreur.setActif(true);

        when(livreurRepository.findById(1L)).thenReturn(Optional.of(livreur));
        when(livreurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        LivreurDTO retour = livreurService.setActif(1L, false);

        //Assert
        assertNotNull(retour);
        verify(livreurRepository).save(any());
    }

    @Test
    public void countActifsByZone_withSuccess() {
        //Arrange
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zoneTest));
        when(livreurRepository.countLivreursActifsByZone(zoneTest)).thenReturn(5L);

        //Act
        Long retour = livreurService.countActifsByZone(1L);

        //Assert
        assertNotNull(retour);
        assertEquals(5L, retour);
    }
}