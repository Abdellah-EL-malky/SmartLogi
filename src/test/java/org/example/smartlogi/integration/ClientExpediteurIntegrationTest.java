package org.example.smartlogi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartlogi.entity.ClientExpediteur;
import org.example.smartlogi.repository.ClientExpediteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClientExpediteurIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientExpediteurRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    public void createClient_shouldSaveInDatabaseAndReturnCreated() throws Exception {
        // Arrange - Préparer les données
        String clientJson = """
            {
                "nom": "Alami",
                "prenom": "Mohammed",
                "email": "alami@test.com",
                "telephone": "0612345678",
                "adresse": "123 Rue Test, Casablanca"
            }
        """;

        // Act - Faire la requête POST
        mockMvc.perform(post("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                // Assert - Vérifier la réponse HTTP
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nom", is("Alami")))
                .andExpect(jsonPath("$.email", is("alami@test.com")));

        List<ClientExpediteur> clients = repository.findAll();
        assertEquals(1, clients.size(), "Doit avoir exactement 1 client dans la base");

        ClientExpediteur savedClient = clients.get(0);
        assertEquals("Alami", savedClient.getNom());
        assertEquals("alami@test.com", savedClient.getEmail());
        assertNotNull(savedClient.getId(), "L'ID doit être généré");
    }

    @Test
    public void getAllClients_shouldReturnClientsFromDatabase() throws Exception {
        // Arrange - Créer des clients directement dans la base
        ClientExpediteur client1 = new ClientExpediteur();
        client1.setNom("Alami");
        client1.setPrenom("Mohammed");
        client1.setEmail("alami@test.com");
        client1.setTelephone("0612345678");
        client1.setAdresse("Casablanca");
        repository.save(client1);

        ClientExpediteur client2 = new ClientExpediteur();
        client2.setNom("Bennani");
        client2.setPrenom("Fatima");
        client2.setEmail("bennani@test.com");
        client2.setTelephone("0698765432");
        client2.setAdresse("Rabat");
        repository.save(client2);

        // Act & Assert - Faire la requête GET et vérifier
        mockMvc.perform(get("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", is("Alami")))
                .andExpect(jsonPath("$[1].nom", is("Bennani")));
    }

    @Test
    public void completeScenario_createReadUpdateDelete() throws Exception {
        String createJson = """
            {
                "nom": "Test",
                "prenom": "User",
                "email": "test@test.com",
                "telephone": "0600000000",
                "adresse": "Test Address"
            }
        """;

        String createResponse = mockMvc.perform(post("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clientId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/clients-expediteurs/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Test")))
                .andExpect(jsonPath("$.email", is("test@test.com")));

        assertTrue(repository.existsById(clientId), "Le client doit exister dans la base");

        String updateJson = """
            {
                "nom": "TestModifie",
                "prenom": "UserModifie",
                "email": "test@test.com",
                "telephone": "0611111111",
                "adresse": "New Address"
            }
        """;

        mockMvc.perform(put("/api/clients-expediteurs/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("TestModifie")))
                .andExpect(jsonPath("$.telephone", is("0611111111")));

        ClientExpediteur updatedClient = repository.findById(clientId).orElseThrow();
        assertEquals("TestModifie", updatedClient.getNom());

        mockMvc.perform(delete("/api/clients-expediteurs/" + clientId))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(clientId), "Le client ne doit plus exister dans la base");
    }

    @Test
    public void createClient_withDuplicateEmail_shouldReturnBadRequest() throws Exception {
        // Arrange - Créer un premier client
        ClientExpediteur existing = new ClientExpediteur();
        existing.setNom("Existing");
        existing.setPrenom("Client");
        existing.setEmail("duplicate@test.com");
        existing.setTelephone("0600000000");
        existing.setAdresse("Address");
        repository.save(existing);

        // Act - Essayer de créer un client avec le même email
        String duplicateJson = """
            {
                "nom": "New",
                "prenom": "Client",
                "email": "duplicate@test.com",
                "telephone": "0611111111",
                "adresse": "Other Address"
            }
        """;

        // Assert - Doit échouer
        mockMvc.perform(post("/api/clients-expediteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJson))
                .andExpect(status().isBadRequest());

        assertEquals(1, repository.count());
    }

    @Test
    public void findByEmail_shouldReturnClientFromDatabase() throws Exception {
        // Arrange - Créer un client dans la base
        ClientExpediteur client = new ClientExpediteur();
        client.setNom("Searchable");
        client.setPrenom("Client");
        client.setEmail("search@test.com");
        client.setTelephone("0600000000");
        client.setAdresse("Address");
        repository.save(client);

        // Act & Assert - Rechercher par email
        mockMvc.perform(get("/api/clients-expediteurs/email/search@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Searchable")))
                .andExpect(jsonPath("$.email", is("search@test.com")));
    }
}