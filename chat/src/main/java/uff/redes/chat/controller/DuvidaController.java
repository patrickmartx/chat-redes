package uff.redes.chat.controller;

import uff.redes.chat.entity.Duvida;
import uff.redes.chat.service.DuvidaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/duvidas")
public class DuvidaController {

    private final DuvidaService duvidaService;

    public DuvidaController(DuvidaService duvidaService) {
        this.duvidaService = duvidaService;
    }

    @GetMapping("/nova")
    public String mostrarFormularioNovaDuvida(Model model) {
        model.addAttribute("duvida", new Duvida());
        return "enviar-duvida";
    }

    @PostMapping("/salvar")
    public String salvarDuvida(@ModelAttribute Duvida duvida,
                               @RequestParam("imagemFile") MultipartFile imagemFile,
                               RedirectAttributes redirectAttributes) {
        try {
            Duvida duvidaSalva = duvidaService.salvarDuvida(duvida, imagemFile);
            redirectAttributes.addFlashAttribute("successMessage", "Dúvida #" + duvidaSalva.getId() + " enviada com sucesso! Você será redirecionado para o chat.");

            return "redirect:/chat/" + duvidaSalva.getId();

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao enviar dúvida: " + e.getMessage());
            return "redirect:/duvidas/nova";
        }
    }

    @GetMapping("/acessar-chat")
    public String acessarChatExistente(@RequestParam Long duvidaId) {
        return "redirect:/chat/" + duvidaId;
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> exibirImagem(@PathVariable Long id) {
        Duvida duvida = duvidaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Dúvida não encontrada com ID: " + id));

        if (duvida.getDadosImagem() == null || duvida.getDadosImagem().length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(duvida.getDadosImagem());
    }

    @GetMapping
    public String listarDuvidas(Model model) {
        model.addAttribute("duvidas", duvidaService.listarTodas());
        return "lista-duvidas";
    }

    @GetMapping("/{id}/detalhes")
    public String detalharDuvida(@PathVariable Long id, Model model) {
        Duvida duvida = duvidaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Dúvida não encontrada com ID: " + id));

        model.addAttribute("duvida", duvida);

        return "detalhe-duvida";
    }
}