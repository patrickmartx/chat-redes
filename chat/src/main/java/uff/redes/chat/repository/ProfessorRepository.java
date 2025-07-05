package uff.redes.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uff.redes.chat.entity.Professor;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByEmail(String email);
    Optional<Professor> findByNome(String nome);
}
