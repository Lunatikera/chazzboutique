package com.juvenr.mqc.chazzboutiqueapi.imagenes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final Path baseDir =
            Paths.get("imagenes").toAbsolutePath().normalize();

    @GetMapping("/{carpeta}/{filename:.+}")
    public ResponseEntity<Resource> get(
            @PathVariable String carpeta,
            @PathVariable String filename
    ) throws Exception {

        Path file = baseDir.resolve(carpeta).resolve(filename).normalize();

        // seguridad: evitar ../ traversal
        if (!file.startsWith(baseDir) || !Files.exists(file) || Files.isDirectory(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toUri());

        String contentType = Files.probeContentType(file);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);
    }
}
