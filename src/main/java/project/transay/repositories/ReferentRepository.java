package project.transay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transay.models.Referent;

@Repository
public interface ReferentRepository extends JpaRepository<Referent, Integer> {
}
