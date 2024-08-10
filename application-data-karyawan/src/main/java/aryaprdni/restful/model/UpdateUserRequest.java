package aryaprdni.restful.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    
    private Integer NIK; 
    
    @Size(max = 255)
    private String Nama_Lengkap;

    @Size(max = 255)
    private String Jenis_Kelamin;
    
    private LocalDate Tanggal_Lahir; 
    
    @Size(max = 255)
    private String Alamat;
    
    @Size(max = 255)
    private String Negara;
}
