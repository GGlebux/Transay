package project.assay.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Transcript;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
    @Query("SELECT t FROM Transcript t " +
            "WHERE t.name = :name " +
            "AND (t.gender = :gender or t.gender = 'both')")
    Optional<Transcript> findByNameAndGender(@Param("name") String name,
                                             @Param("gender") String gender);


    @EntityGraph(attributePaths = {"falls", "raises"})
    @Query("SELECT t FROM Transcript t" +
            " WHERE t.name IN :names " +
            "AND (t.gender = :gender or t.gender = 'both')")
    Set<Transcript> findAllByNameIn(@Param("names") Set<String> names,
                                        @Param("gender") String gender);
}
