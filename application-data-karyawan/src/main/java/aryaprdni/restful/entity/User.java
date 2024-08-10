package aryaprdni.restful.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private Integer NIK;

    private String Nama_Lengkap;

    private String Jenis_Kelamin;

    private LocalDate Tanggal_Lahir;

    private String Alamat;

    private String Negara;
}
