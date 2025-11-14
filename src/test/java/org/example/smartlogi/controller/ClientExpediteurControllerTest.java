package org.example.smartlogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.service.ClientExpediteurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientExpediteurController.class)
public class ClientExpediteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientExpediteurService clientExpediteurService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientExpediteurDTO clientDTO;

    @BeforeEach
    void setup() {
        clientDTO = new ClientExpediteurDTO();
        clientDTO.setId(1L);
        clientDTO.setNom("Alami");
        clientDTO.setPrenom("Mohammed");
        clientDTO.setEmail("alami@email.com");
        clientDTO.setTelephone("0612345678");
        clientDTO.setAdresse("123 Rue Test, Casablanca");
    }

    @Test
    public void getAllClients_shouldReturnListOfClients() throws Exception {
        // Arrange - Préparer
        ClientExpediteurDTO client2 = new ClientExpediteurDTO();
        client2.setId(2L);
        client2.setNom("Bennani");
        client2.setEmail("bennani@email.com");

        when(clientExpediteurService.findAll()).thenReturn(Arrays.asList(clientDTO, client2));

        // Act & Assert - Agir et Vérifier
        mockMvc.perform(get("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", is("Alami")))
                .andExpect(jsonPath("$[0].email", is("alami@email.com")))
                .andExpect(jsonPath("$[1].nom", is("Bennani")));
    }

    @Test
    public void createClient_shouldReturnCreatedClient() throws Exception {
        // Arrange - Préparer
        ClientExpediteurDTO requestDTO = new ClientExpediteurDTO();
        requestDTO.setNom("Nouveau");
        requestDTO.setPrenom("Client");
        requestDTO.setEmail("nouveau@email.com");
        requestDTO.setTelephone("0698765432");
        requestDTO.setAdresse("456 Avenue Test");

        ClientExpediteurDTO responseDTO = new ClientExpediteurDTO();
        responseDTO.setId(3L);
        responseDTO.setNom("Nouveau");
        responseDTO.setPrenom("Client");
        responseDTO.setEmail("nouveau@email.com");
        responseDTO.setTelephone("0698765432");
        responseDTO.setAdresse("456 Avenue Test");

        when(clientExpediteurService.create(any(ClientExpediteurDTO.class))).thenReturn(responseDTO);

        // Act & Assert - Agir et Vérifier
        mockMvc.perform(post("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nom", is("Nouveau")))
                .andExpect(jsonPath("$.email", is("nouveau@email.com")));
    }
}