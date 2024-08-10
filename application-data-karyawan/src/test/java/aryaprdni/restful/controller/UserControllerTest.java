package aryaprdni.restful.controller;

import aryaprdni.restful.entity.User;
import aryaprdni.restful.model.CreateDataRequest;
import aryaprdni.restful.model.UpdateUserRequest;
import aryaprdni.restful.model.UserResponse;
import aryaprdni.restful.model.WebResponse;
import aryaprdni.restful.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        CreateDataRequest request = new CreateDataRequest();
        request.setNIK(11231);
        request.setNama_Lengkap("Arya Perdana Irawan");
        request.setJenis_Kelamin("Laki-Laki");
        request.setAlamat("Jl Matraman");
        request.setNegara("Indonesia");
        request.setTanggal_Lahir(LocalDate.of(2003, 7, 9));

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        CreateDataRequest request = new CreateDataRequest();
        request.setNIK(11231);
        request.setNama_Lengkap("");
        request.setJenis_Kelamin("");
        request.setAlamat("");
        request.setNegara("");
        request.setTanggal_Lahir(LocalDate.of(2003, 7, 9));

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setNIK(12345);
        user.setNama_Lengkap("test");
        user.setJenis_Kelamin("Laki-laki");
        user.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        user.setAlamat("Jl Matraman");
        user.setNegara("Indonesia");
        userRepository.save(user);

        CreateDataRequest request = new CreateDataRequest();
        request.setNIK(11231);
        request.setNama_Lengkap("");
        request.setJenis_Kelamin("");
        request.setAlamat("");
        request.setNegara("");
        request.setTanggal_Lahir(LocalDate.of(2003, 7, 9));

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setNIK(12345);
        user.setNama_Lengkap("test");
        user.setJenis_Kelamin("Laki-laki");
        user.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        user.setAlamat("Jl Matraman");
        user.setNegara("Indonesia");
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNama_Lengkap("Arya Perdana Irawan");
        request.setJenis_Kelamin("Laki-laki");
        request.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        request.setAlamat("Jl Matraman");
        request.setNegara("Indonesia");

        mockMvc.perform(
                patch("/api/users/12345")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertEquals("Arya Perdana Irawan", response.getData().getNama_Lengkap());
            assertEquals("Laki-laki", response.getData().getJenis_Kelamin());
            assertEquals("Jl Matraman", response.getData().getAlamat());
            assertEquals("Indonesia", response.getData().getNegara());
            assertEquals(LocalDate.of(2003, 7, 9), response.getData().getTanggal_Lahir());
            assertEquals(12345, response.getData().getNIK());

            User userDb = userRepository.findById(12345).get();
            assertNotNull(userDb);
            assertTrue(userDb.getNama_Lengkap().equals("Arya Perdana Irawan"));
        });
    }
    
    @Test
    void testGetUserSuccess() throws Exception {
        User user = new User();
        user.setNIK(12345);
        user.setNama_Lengkap("Arya Perdana Irawan");
        user.setJenis_Kelamin("Laki-laki");
        user.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        user.setAlamat("Jl Matraman");
        user.setNegara("Indonesia");
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/12345")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("Arya Perdana Irawan", response.getData().getNama_Lengkap());
            assertEquals("Laki-laki", response.getData().getJenis_Kelamin());
            assertEquals("Jl Matraman", response.getData().getAlamat());
            assertEquals("Indonesia", response.getData().getNegara());
            assertEquals(LocalDate.of(2003, 7, 9), response.getData().getTanggal_Lahir());
            assertEquals(12345, response.getData().getNIK());
        });
    }

    @Test
    void testGetUserNotFound() throws Exception {
        mockMvc.perform(
                get("/api/users/99999")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
        .andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("NIK tidak ditemukan"));
        });
    }

    @Test
    void testGetAllUsersSuccess() throws Exception {
        User user1 = new User();
        user1.setNIK(12345);
        user1.setNama_Lengkap("Arya Perdana Irawan");
        user1.setJenis_Kelamin("Laki-laki");
        user1.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        user1.setAlamat("Jl Matraman");
        user1.setNegara("Indonesia");
        userRepository.save(user1);

        User user2 = new User();
        user2.setNIK(67890);
        user2.setNama_Lengkap("Jane Doe");
        user2.setJenis_Kelamin("Perempuan");
        user2.setTanggal_Lahir(LocalDate.of(1990, 1, 1));
        user2.setAlamat("Jl Kebon Jeruk");
        user2.setNegara("Indonesia");
        userRepository.save(user2);

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(2, response.getData().size());

            UserResponse responseUser1 = response.getData().get(0);
            assertEquals("Arya Perdana Irawan", responseUser1.getNama_Lengkap());
            assertEquals("Laki-laki", responseUser1.getJenis_Kelamin());
            assertEquals("Jl Matraman", responseUser1.getAlamat());
            assertEquals("Indonesia", responseUser1.getNegara());
            assertEquals(LocalDate.of(2003, 7, 9), responseUser1.getTanggal_Lahir());
            assertEquals(12345, responseUser1.getNIK());

            UserResponse responseUser2 = response.getData().get(1);
            assertEquals("Jane Doe", responseUser2.getNama_Lengkap());
            assertEquals("Perempuan", responseUser2.getJenis_Kelamin());
            assertEquals("Jl Kebon Jeruk", responseUser2.getAlamat());
            assertEquals("Indonesia", responseUser2.getNegara());
            assertEquals(LocalDate.of(1990, 1, 1), responseUser2.getTanggal_Lahir());
            assertEquals(67890, responseUser2.getNIK());
        });
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        User user = new User();
        user.setNIK(12345);
        user.setNama_Lengkap("test");
        user.setJenis_Kelamin("Laki-laki");
        user.setTanggal_Lahir(LocalDate.of(2003, 7, 9));
        user.setAlamat("Jl Matraman");
        user.setNegara("Indonesia");
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/users/12345")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        assertFalse(userRepository.existsById(12345));
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/users/99999")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
