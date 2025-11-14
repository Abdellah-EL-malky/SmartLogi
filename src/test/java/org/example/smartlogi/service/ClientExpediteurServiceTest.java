package org.example.smartlogi.service;

import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.entity.ClientExpediteur;
import org.example.smartlogi.mapper.ClientExpediteurMapper;
import org.example.smartlogi.mapper.ClientExpediteurMapperImpl;
import org.example.smartlogi.repository.ClientExpediteurRepository;
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
public class ClientExpediteurServiceTest {

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    private final ClientExpediteurMapper clientExpediteurMapper = new ClientExpediteurMapperImpl();

    private ClientExpediteurService clientExpService;
    private ClientExpediteurDTO dtoRequest;

    @BeforeEach
    void setup() {
        clientExpService = new ClientExpediteurService(clientExpediteurRepository, clientExpediteurMapper);

        dtoRequest = new ClientExpediteurDTO();
        dtoRequest.setEmail("hamada@tete.com");
        dtoRequest.setAdresse("youcode");
        dtoRequest.setNom("ahmad");
        dtoRequest.setPrenom("tawdi");
        dtoRequest.setTelephone("0616262738");
    }

    @Test
    public void createClient_withSuccess() {
        //Arrange
        when(clientExpediteurRepository.existsByEmail("hamada@tete.com")).thenReturn(false);
        when(clientExpediteurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ClientExpediteurDTO retour = clientExpService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("ahmad", retour.getNom());
        assertEquals("tawdi", retour.getPrenom());
        assertEquals("hamada@tete.com", retour.getEmail());
    }

    @Test
    public void createClient_withError() {
        //Arrange
        when(clientExpediteurRepository.existsByEmail(dtoRequest.getEmail())).thenReturn(true);

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> clientExpService.create(dtoRequest));
        verify(clientExpediteurRepository, never()).save(any());
    }

    @Test
    public void findById_withSuccess() {
        //Arrange
        ClientExpediteur client = clientExpediteurMapper.toEntity(dtoRequest);
        client.setId(1L);
        when(clientExpediteurRepository.findById(1L)).thenReturn(Optional.of(client));

        //Act
        ClientExpediteurDTO retour = clientExpService.findById(1L);

        //Assert
        assertNotNull(retour);
        assertEquals("ahmad", retour.getNom());
        assertEquals("hamada@tete.com", retour.getEmail());
    }

    @Test
    public void findById_withError() {
        //Arrange
        when(clientExpediteurRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> clientExpService.findById(999L));
    }

    @Test
    public void findAll_withSuccess() {
        //Arrange
        ClientExpediteur client1 = clientExpediteurMapper.toEntity(dtoRequest);
        client1.setId(1L);

        ClientExpediteurDTO dto2 = new ClientExpediteurDTO();
        dto2.setNom("Bennani");
        dto2.setPrenom("Hassan");
        dto2.setEmail("hassan@email.com");
        dto2.setTelephone("0698765432");
        dto2.setAdresse("456 Avenue Test");
        ClientExpediteur client2 = clientExpediteurMapper.toEntity(dto2);
        client2.setId(2L);

        when(clientExpediteurRepository.findAll()).thenReturn(Arrays.asList(client1, client2));

        //Act
        List<ClientExpediteurDTO> retour = clientExpService.findAll();

        //Assert
        assertNotNull(retour);
        assertEquals(2, retour.size());
        assertEquals("ahmad", retour.get(0).getNom());
        assertEquals("Bennani", retour.get(1).getNom());
    }

    @Test
    public void findAllPaginated_withSuccess() {
        //Arrange
        ClientExpediteur client = clientExpediteurMapper.toEntity(dtoRequest);
        client.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientExpediteur> page = new PageImpl<>(Arrays.asList(client), pageable, 1);
        when(clientExpediteurRepository.findAll(pageable)).thenReturn(page);

        //Act
        Page<ClientExpediteurDTO> retour = clientExpService.findAll(pageable);

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.getTotalElements());
        assertEquals("ahmad", retour.getContent().get(0).getNom());
    }

    @Test
    public void updateClient_withSuccess() {
        //Arrange
        ClientExpediteur existingClient = clientExpediteurMapper.toEntity(dtoRequest);
        existingClient.setId(1L);

        ClientExpediteurDTO updateDTO = new ClientExpediteurDTO();
        updateDTO.setNom("NouveauNom");
        updateDTO.setPrenom("NouveauPrenom");
        updateDTO.setEmail("hamada@tete.com");
        updateDTO.setTelephone("0699999999");
        updateDTO.setAdresse("Nouvelle Adresse");

        when(clientExpediteurRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(clientExpediteurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ClientExpediteurDTO retour = clientExpService.update(1L, updateDTO);

        //Assert
        assertNotNull(retour);
        assertEquals("NouveauNom", retour.getNom());
        assertEquals("NouveauPrenom", retour.getPrenom());
    }

    @Test
    public void updateClient_withError() {
        //Arrange
        when(clientExpediteurRepository.findById(999L)).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> clientExpService.update(999L, dtoRequest));
        verify(clientExpediteurRepository, never()).save(any());
    }

    @Test
    public void updateClient_withDuplicateEmail_shouldThrowError() {
        //Arrange
        ClientExpediteur existingClient = clientExpediteurMapper.toEntity(dtoRequest);
        existingClient.setId(1L);
        existingClient.setEmail("hamada@tete.com");

        ClientExpediteurDTO updateDTO = new ClientExpediteurDTO();
        updateDTO.setNom("NouveauNom");
        updateDTO.setEmail("autre@email.com"); // Nouveau email différent

        when(clientExpediteurRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(clientExpediteurRepository.existsByEmail("autre@email.com")).thenReturn(true);

        //Assert(Act)
        assertThrows(IllegalArgumentException.class, () -> clientExpService.update(1L, updateDTO));
        verify(clientExpediteurRepository, never()).save(any());
    }

    @Test
    public void deleteClient_withSuccess() {
        //Arrange
        when(clientExpediteurRepository.existsById(1L)).thenReturn(true);

        //Act
        clientExpService.delete(1L);

        //Assert
        verify(clientExpediteurRepository).deleteById(1L);
    }

    @Test
    public void deleteClient_withError() {
        //Arrange
        when(clientExpediteurRepository.existsById(999L)).thenReturn(false);

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> clientExpService.delete(999L));
        verify(clientExpediteurRepository, never()).deleteById(anyLong());
    }

    @Test
    public void findByEmail_withSuccess() {
        //Arrange
        ClientExpediteur client = clientExpediteurMapper.toEntity(dtoRequest);
        client.setId(1L);
        when(clientExpediteurRepository.findByEmail("hamada@tete.com")).thenReturn(Optional.of(client));

        //Act
        ClientExpediteurDTO retour = clientExpService.findByEmail("hamada@tete.com");

        //Assert
        assertNotNull(retour);
        assertEquals("hamada@tete.com", retour.getEmail());
        assertEquals("ahmad", retour.getNom());
    }

    @Test
    public void findByEmail_withError() {
        //Arrange
        when(clientExpediteurRepository.findByEmail("inconnu@email.com")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> clientExpService.findByEmail("inconnu@email.com"));
    }

    @Test
    public void findByTelephone_withSuccess() {
        //Arrange
        ClientExpediteur client = clientExpediteurMapper.toEntity(dtoRequest);
        client.setId(1L);
        when(clientExpediteurRepository.findByTelephone("0616262738")).thenReturn(Optional.of(client));

        //Act
        ClientExpediteurDTO retour = clientExpService.findByTelephone("0616262738");

        //Assert
        assertNotNull(retour);
        assertEquals("0616262738", retour.getTelephone());
        assertEquals("ahmad", retour.getNom());
    }

    @Test
    public void findByTelephone_withError() {
        //Arrange
        when(clientExpediteurRepository.findByTelephone("0699999999")).thenReturn(Optional.empty());

        //Assert(Act)
        assertThrows(RuntimeException.class, () -> clientExpService.findByTelephone("0699999999"));
    }

    @Test
    public void searchByNom_withSuccess() {
        //Arrange
        ClientExpediteur client = clientExpediteurMapper.toEntity(dtoRequest);
        when(clientExpediteurRepository.findByNomContainingIgnoreCase("ahm")).thenReturn(Arrays.asList(client));

        //Act
        List<ClientExpediteurDTO> retour = clientExpService.searchByNom("ahm");

        //Assert
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("ahmad", retour.get(0).getNom());
    }

    @Test
    public void searchByNom_withNoResults() {
        //Arrange
        when(clientExpediteurRepository.findByNomContainingIgnoreCase("xyz")).thenReturn(Arrays.asList());

        //Act
        List<ClientExpediteurDTO> retour = clientExpService.searchByNom("xyz");

        //Assert
        assertNotNull(retour);
        assertTrue(retour.isEmpty());
    }

    @Test
    public void emailExists_shouldReturnTrue() {
        //Arrange
        when(clientExpediteurRepository.existsByEmail("hamada@tete.com")).thenReturn(true);

        //Act
        boolean retour = clientExpService.emailExists("hamada@tete.com");

        //Assert
        assertTrue(retour);
    }

    @Test
    public void emailExists_shouldReturnFalse() {
        //Arrange
        when(clientExpediteurRepository.existsByEmail("inexistant@email.com")).thenReturn(false);

        //Act
        boolean retour = clientExpService.emailExists("inexistant@email.com");

        //Assert
        assertFalse(retour);
    }
}