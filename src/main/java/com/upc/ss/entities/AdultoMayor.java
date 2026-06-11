package com.upc.ss.entities;

import com.upc.ss.security.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adulto_mayor")
public class AdultoMayor {

    @Id
    @Column(name = "id_adulto_mayor")
    private Long idAdultoMayor;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_adulto_mayor")
    private User user;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}