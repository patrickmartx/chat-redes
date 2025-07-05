package uff.redes.chat.service;

import uff.redes.chat.entity.Professor;
import uff.redes.chat.repository.ProfessorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProfessorUserDetailsService implements UserDetailsService {

    private final ProfessorRepository professorRepository;

    public ProfessorUserDetailsService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Professor não encontrado com o email: " + email));

        return User.builder()
                .username(professor.getEmail())
                .password(professor.getSenha())
                .authorities(new ArrayList<>())
                .build();
    }
}
