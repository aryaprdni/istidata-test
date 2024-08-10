package aryaprdni.restful.service;

import aryaprdni.restful.entity.User;
import aryaprdni.restful.model.CreateDataRequest;
import aryaprdni.restful.model.SearchUserRequest;
import aryaprdni.restful.model.UpdateUserRequest;
import aryaprdni.restful.model.UserResponse;
import aryaprdni.restful.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotations.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void create(CreateDataRequest request){
        validationService.validate(request);

        if(request.getNIK() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIK tidak boleh null");
        }

        if(userRepository.existsById(request.getNIK())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIK sudah terdaftar");
        }

        User user = new User();
        user.setNIK(request.getNIK());
        user.setNama_Lengkap(request.getNama_Lengkap());
        user.setJenis_Kelamin(request.getJenis_Kelamin());
        user.setAlamat(request.getAlamat());
        user.setNegara(request.getNegara());
        user.setTanggal_Lahir(request.getTanggal_Lahir());

        userRepository.save(user);
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request){
        log.info("Updating user with request: {}", request);

        validationService.validate(request);

        User existingUser = userRepository.findById(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NIK tidak ditemukan"));

        log.info("Existing user found: {}", existingUser);

        if (request.getNIK() != null) {
            existingUser.setNIK(request.getNIK());
        }

        if (request.getNama_Lengkap() != null) {
            existingUser.setNama_Lengkap(request.getNama_Lengkap());
        }

        if (request.getJenis_Kelamin() != null) {
            existingUser.setJenis_Kelamin(request.getJenis_Kelamin());
        }

        if (request.getTanggal_Lahir() != null) {
            existingUser.setTanggal_Lahir(request.getTanggal_Lahir());
        }

        if (request.getAlamat() != null) {
            existingUser.setAlamat(request.getAlamat());
        }

        if (request.getNegara() != null) {
            existingUser.setNegara(request.getNegara());
        }

        userRepository.save(existingUser);

        log.info("User updated successfully: {}", existingUser);

        return UserResponse.builder()
                .NIK(existingUser.getNIK())
                .Nama_Lengkap(existingUser.getNama_Lengkap())
                .Jenis_Kelamin(existingUser.getJenis_Kelamin())
                .Tanggal_Lahir(existingUser.getTanggal_Lahir())
                .Alamat(existingUser.getAlamat())
                .Negara(existingUser.getNegara())
                .build();
    }

    @Transactional
    public UserResponse get(Integer NIK) {
        User user = userRepository.findById(NIK)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NIK tidak ditemukan"));

        return UserResponse.builder()
                .NIK(user.getNIK())
                .Nama_Lengkap(user.getNama_Lengkap())
                .Jenis_Kelamin(user.getJenis_Kelamin())
                .Tanggal_Lahir(user.getTanggal_Lahir())
                .Alamat(user.getAlamat())
                .Negara(user.getNegara())
                .build();
    }

    @Transactional
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserResponse.builder()
                        .NIK(user.getNIK())
                        .Nama_Lengkap(user.getNama_Lengkap())
                        .Jenis_Kelamin(user.getJenis_Kelamin())
                        .Tanggal_Lahir(user.getTanggal_Lahir())
                        .Alamat(user.getAlamat())
                        .Negara(user.getNegara())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer NIK) {
        User existingUser = userRepository.findById(NIK)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NIK tidak ditemukan"));
        userRepository.delete(existingUser);
    }

    @Transactional
    public List<UserResponse> search(SearchUserRequest request) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (Objects.nonNull(request.getNIK())) {
                predicates.add(criteriaBuilder.equal(root.get("NIK"), request.getNIK()));
            }
            
            if (Objects.nonNull(request.getNama_Lengkap())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("Nama_Lengkap")),
                        "%" + request.getNama_Lengkap().toLowerCase() + "%"
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<User> users = userRepository.findAll(specification);
        return users.stream()
                    .map(this::mapToUserResponse)
                    .toList();
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setNIK(user.getNIK());
        response.setNama_Lengkap(user.getNama_Lengkap());
        response.setJenis_Kelamin(user.getJenis_Kelamin());
        response.setTanggal_Lahir(user.getTanggal_Lahir());
        response.setAlamat(user.getAlamat());
        response.setNegara(user.getNegara());
        return response;
    }
}
