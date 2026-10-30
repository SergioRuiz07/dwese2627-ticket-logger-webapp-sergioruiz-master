package org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.JoinColumnOrFormula;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "provinces")
public class Province {
    private long id;
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    @Column(name = "code",nullable = false,length = 2)
    private String code;


    // Campo que almacena el nombre completo de la región, como "Andalucía" o "Cataluña".
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "{msg.province.region.notNUll}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}