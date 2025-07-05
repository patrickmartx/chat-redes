package uff.redes.chat.service;

import uff.redes.chat.entity.Duvida;
import uff.redes.chat.repository.DuvidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DuvidaService {

    private final DuvidaRepository duvidaRepository;

    public DuvidaService(DuvidaRepository duvidaRepository) {
        this.duvidaRepository = duvidaRepository;
    }

    @Transactional
    public Duvida salvarDuvida(Duvida duvida, MultipartFile imagemFile) {
        if (imagemFile != null && !imagemFile.isEmpty()) {
            try {
                duvida.setDadosImagem(imagemFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Falha ao processar o arquivo de imagem.", e);
            }
        }
        return duvidaRepository.save(duvida);
    }

    public Optional<Duvida> buscarPorId(Long id) {
        return duvidaRepository.findById(id);
    }

    public List<Duvida> listarTodas() {
        return duvidaRepository.findAll();
    }
}