package aryaprdni.restful.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    
    private Integer NIK;

    private String Nama_Lengkap;

    private String Jenis_Kelamin;

    private LocalDate Tanggal_Lahir;

    private String Alamat;

    private String Negara;
}
