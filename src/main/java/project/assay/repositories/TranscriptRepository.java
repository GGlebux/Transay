package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Transcript;

import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
  Optional<Transcript> findByNameAndGender(String name, String gender);
}
