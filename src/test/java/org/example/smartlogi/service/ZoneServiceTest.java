package org.example.smartlogi.service;

import org.example.smartlogi.dto.ZoneDTO;
import org.example.smartlogi.entity.Zone;
import org.example.smartlogi.mapper.ZoneMapper;
import org.example.smartlogi.mapper.ZoneMapperImpl;
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
public class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper = new ZoneMapperImpl();

    private ZoneService zoneService;
    private ZoneDTO dtoRequest;

    @BeforeEach
    void setup() {
        zoneService = new ZoneService(zoneRepository, zoneMapper);

        dtoRequest = new ZoneDTO();
        dtoRequest.setNom("Zone Nord");
        dtoRequest.setCodePostal("20000");
        dtoRequest.setVille("Casablanca");
    }

    @Test
    public void createZone_withSuccess() {
        //Arrange
        when(zoneRepository.existsByNom("Zone Nord")).thenReturn(false);
        when(zoneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ZoneDTO retour = zoneService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("Zone Nord", retour.getNom());
        assertEquals("Casablanca", retour.getVille());
        assertEquals("20000", retour.getCodePostal());
    }

    @Test
    public void createZone_withDuplicateName_shouldThrowError() {
        //Arrange
        when(zoneRepository.existsByNom("Zone Nord")).thenReturn(true);

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> zoneService.create(dtoRequest));
        verify(zoneRepository, never()).save(any());
    }

    @Test
    public void findById_withSuccess() {
        //Arrange
        Zone zone = zoneMapper.toEntity(dtoRequest);
        zone.setId(1L);
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        //Act
        ZoneDTO retour = zoneService.findById(1L);

        //Assert
        assertNotNull(retour);
        assertEquals("Zone Nord", retour.getNom());
        assertEquals("Casablanca", retour.getVille());
    }

    @Test
    public void findById_withError() {
        //Arrange
        when(zoneRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> zoneService.findById(999L));
    }

    @Test
    public void findAll_withSuccess() {
        //Arrange
        Zone zone1 = zoneMapper.toEntity(dtoRequest);
        zone1.setId(1L);

        ZoneDTO dto2 = new ZoneDTO();
        dto2.setNom("Zone Sud");
        dto2.setCodePostal("30000");
        dto2.setVille("Rabat");
        Zone zone2 = zoneMapper.toEntity(dto2);
        zone2.setId(2L);

        when(zoneRepository.findAll()).thenReturn(Arrays.asList(zone1, zone2));

        //Act
        List<ZoneDTO> retour = zoneService.findAll();

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertEquals("Zone Nord", retour.get(0).getNom());
        assertEquals("Zone Sud", retour.get(1).getNom());
    }

    @Test
    public void findAllPaginated_withSuccess() {
        //Arrange
        Zone zone = zoneMapper.toEntity(dtoRequest);
        zone.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Zone> page = new PageImpl<>(Arrays.asList(zone), pageable, 1);
        when(zoneRepository.findAll(pageable)).thenReturn(page);

        //Act
        Page<ZoneDTO> retour = zoneService.findAll(pageable);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.getTotalElements());
        assertEquals("Zone Nord", retour.getContent().get(0).getNom());
    }

    @Test
    public void updateZone_withSuccess() {
        //Arrange
        Zone existingZone = zoneMapper.toEntity(dtoRequest);
        existingZone.setId(1L);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existingZone));
        when(zoneRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ZoneDTO retour = zoneService.update(1L, dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("Zone Nord", retour.getNom());
        verify(zoneRepository).save(any());
    }

    @Test
    public void updateZone_withDuplicateName_shouldThrowError() {
        //Arrange
        Zone existingZone = zoneMapper.toEntity(dtoRequest);
        existingZone.setId(1L);
        existingZone.setNom("AncienNom");

        ZoneDTO updateDTO = new ZoneDTO();
        updateDTO.setNom("NouveauNom");
        updateDTO.setVille("Casablanca");
        updateDTO.setCodePostal("20000");

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existingZone));
        when(zoneRepository.existsByNom("NouveauNom")).thenReturn(true);

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> zoneService.update(1L, updateDTO));
        verify(zoneRepository, never()).save(any());
    }

    @Test
    public void updateZone_withError() {
        //Arrange
        when(zoneRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> zoneService.update(999L, dtoRequest));
        verify(zoneRepository, never()).save(any());
    }

    @Test
    public void deleteZone_withSuccess() {
        //Arrange
        when(zoneRepository.existsById(1L)).thenReturn(true);

        //Act
        zoneService.delete(1L);

        //Assert
        verify(zoneRepository).deleteById(1L);
    }

    @Test
    public void deleteZone_withError() {
        //Arrange
        when(zoneRepository.existsById(999L)).thenReturn(false);

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> zoneService.delete(999L));
        verify(zoneRepository, never()).deleteById(anyLong());
    }

    @Test
    public void findByNom_withSuccess() {
        //Arrange
        Zone zone = zoneMapper.toEntity(dtoRequest);
        zone.setId(1L);
        when(zoneRepository.findByNom("Zone Nord")).thenReturn(Optional.of(zone));

        //Act
        ZoneDTO retour = zoneService.findByNom("Zone Nord");

        //Assert
        assertNotNull(retour);
        assertEquals("Zone Nord", retour.getNom());
    }

    @Test
    public void findByNom_withError() {
        //Arrange
        when(zoneRepository.findByNom("Zone Inexistante")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> zoneService.findByNom("Zone Inexistante"));
    }

    @Test
    public void findByVille_withSuccess() {
        //Arrange
        Zone zone = zoneMapper.toEntity(dtoRequest);
        when(zoneRepository.findByVille("Casablanca")).thenReturn(Arrays.asList(zone));

        //Act
        List<ZoneDTO> retour = zoneService.findByVille("Casablanca");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("Casablanca", retour.get(0).getVille());
    }

    @Test
    public void findByVille_withNoResults() {
        //Arrange
        when(zoneRepository.findByVille("Paris")).thenReturn(Arrays.asList());

        //Act
        List<ZoneDTO> retour = zoneService.findByVille("Paris");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }

    @Test
    public void findByCodePostal_withSuccess() {
        //Arrange
        Zone zone = zoneMapper.toEntity(dtoRequest);
        zone.setId(1L);
        when(zoneRepository.findByCodePostal("20000")).thenReturn(Optional.of(zone));

        //Act
        ZoneDTO retour = zoneService.findByCodePostal("20000");

        //Assert
        assertNotNull(retour);
        assertEquals("20000", retour.getCodePostal());
    }

    @Test
    public void findByCodePostal_withError() {
        //Arrange
        when(zoneRepository.findByCodePostal("99999")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> zoneService.findByCodePostal("99999"));
    }

    @Test
    public void existsByNom_shouldReturnTrue() {
        //Arrange
        when(zoneRepository.existsByNom("Zone Nord")).thenReturn(true);

        //Act
        boolean retour = zoneService.existsByNom("Zone Nord");

        //Assert
        assertTrue(retour);
    }

    @Test
    public void existsByNom_shouldReturnFalse() {
        //Arrange
        when(zoneRepository.existsByNom("Zone Inexistante")).thenReturn(false);

        //Act
        boolean retour = zoneService.existsByNom("Zone Inexistante");

        //Assert
        assertFalse(retour);
    }
}