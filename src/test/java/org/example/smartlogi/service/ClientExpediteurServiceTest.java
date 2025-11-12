package org.example.smartlogi.service;

import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.mapper.ClientExpediteurMapper;
import org.example.smartlogi.mapper.ClientExpediteurMapperImpl;
import org.example.smartlogi.repository.ClientExpediteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientExpediteurServiceTest {

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    //@Spy
    private final ClientExpediteurMapper clientExpediteurMapper = new ClientExpediteurMapperImpl();

//    @InjectMocks
    private ClientExpediteurService clientExpService;
    private ClientExpediteurDTO dtoRequest;
    @BeforeEach
    void setup(){
        clientExpService =new ClientExpediteurService(clientExpediteurRepository,clientExpediteurMapper);

        dtoRequest = new ClientExpediteurDTO();

        dtoRequest.setEmail("hamada@tete.com");
        dtoRequest.setAdresse("youcode");
        dtoRequest.setNom("ahmad");
        dtoRequest.setPrenom("tawdi");
        dtoRequest.setTelephone("0616262738");

    }

    @Test
    public void createClient_withSuccess(){
        //Arrange
        when(clientExpediteurRepository.existsByEmail("hamada@tete.com")).thenReturn(false);
        when(clientExpediteurRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        ClientExpediteurDTO retour =clientExpService.create(dtoRequest);

        //Assert
        assertNotNull(retour);
        assertEquals("ahmad", retour.getNom());


    }

    @Test
    public void createClient_withError(){

        //Arrange
        when(clientExpediteurRepository.existsByEmail(dtoRequest.getEmail())).thenReturn(true);

        //Assert(Act)
        assertThrows(IllegalArgumentException.class,()-> clientExpService.create(dtoRequest) );
        verify(clientExpediteurRepository,never()).save(any());
    }


}
