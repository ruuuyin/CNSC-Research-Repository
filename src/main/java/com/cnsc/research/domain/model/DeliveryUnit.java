package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "delivery_unit")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUnit {

    @Id
    @Column(name = "unit_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitId;

    @Column(name = "unit_name", nullable = false, length = 250)
    private String unitName;

    @OneToMany(mappedBy = "deliveryUnit", fetch = FetchType.LAZY)
    private Collection<Research> researchByUnitId;


}
