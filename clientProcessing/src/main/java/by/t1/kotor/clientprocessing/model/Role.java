package by.t1.kotor.clientprocessing.model;

import by.t1.kotor.clientprocessing.model.enums.RoleEnum;
import by.t1.kotor.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

}
