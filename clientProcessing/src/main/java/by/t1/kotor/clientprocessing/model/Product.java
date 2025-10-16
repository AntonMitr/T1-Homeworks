package by.t1.kotor.clientprocessing.model;

import by.t1.kotor.clientprocessing.model.enums.KeyEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_key")
    private KeyEnum key;

    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ClientProduct> clientProductList = new ArrayList<>();

    @Transient
    public String getProductId() {
        return key.name() + super.getId();
    }
}
