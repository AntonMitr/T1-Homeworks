package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.Role;
import by.t1.kotor.clientprocessing.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
