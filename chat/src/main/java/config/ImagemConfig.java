package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImagemConfig {

    @Value("${duvidas.imagem-dir}")
    private String imagemDir;

    public String getImagemDir() {
        return imagemDir;
    }
}

