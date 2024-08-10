package aryaprdni.restful.controller;

import aryaprdni.restful.model.CreateDataRequest;
import aryaprdni.restful.model.SearchUserRequest;
import aryaprdni.restful.model.UpdateUserRequest;
import aryaprdni.restful.model.UserResponse;
import aryaprdni.restful.model.WebResponse;
import aryaprdni.restful.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500") 
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> create(@RequestBody CreateDataRequest request){
        log.info("Received request: {}", request);
        userService.create(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @PatchMapping(
        path = "/api/users/{NIK}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(@PathVariable Integer NIK, @RequestBody UpdateUserRequest request) {
        log.info("Received update request for NIK: {} with data: {}", NIK, request);
        request.setNIK(NIK);
        UserResponse userResponse = userService.update(request);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @GetMapping(
    path = "/api/users/{NIK}",
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(@PathVariable Integer NIK) {
        UserResponse userResponse = userService.get(NIK);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @GetMapping(
    path = "/api/users",
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> getAll() {
        List<UserResponse> users = userService.getAll();
        return WebResponse.<List<UserResponse>>builder().data(users).build();
    }

    @DeleteMapping("/api/users/{NIK}")
    public ResponseEntity<Void> delete(@PathVariable Integer NIK) {
        userService.delete(NIK);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(
        path = "/api/users/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> search(
            @RequestParam(required = false) Integer NIK,
            @RequestParam(required = false) String Nama_Lengkap
    ) {
        SearchUserRequest request = new SearchUserRequest();
        request.setNIK(NIK);
        request.setNama_Lengkap(Nama_Lengkap);
        
        List<UserResponse> users = userService.search(request);
        return WebResponse.<List<UserResponse>>builder().data(users).build();
    }
}
