package aryaprdni.restful.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDataRequest {

    private Integer NIK;

    @NotBlank
    @Size(max = 255)
    private String Nama_Lengkap;

    @NotBlank
    @Size(max = 255)
    private String Jenis_Kelamin;

    @NotNull
    @Past
    private LocalDate Tanggal_Lahir;

    @NotBlank
    @Size(max = 255)
    private String Alamat;

    @NotBlank
    @Size(max = 255)
    private String Negara;
}
