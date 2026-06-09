/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.juvenr.mqc.chazzboutiqueapi.categorias;

import com.juvenr.mqc.chazzboutiqueapi.categorias.dto.CategoriaResponse;
import com.mycompany.chazzboutiquenegocio.dtos.CategoriaDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.ICategoriaNegocio;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author carli
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final ICategoriaNegocio categoriaNegocio;

    public CategoriaController(ICategoriaNegocio categoriaNegocio) {
        this.categoriaNegocio = categoriaNegocio;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        try {
            List<CategoriaDTO> cats = categoriaNegocio.obtenerCategorias();

            return cats.stream().map(c -> new CategoriaResponse(
                    c.getId(),
                    c.getNombreCategoria(),
                    "/api/imagenes/" + c.getImagenCategoria()  // asumiendo que guardas el nombre del archivo
            )).toList();

        } catch (NegocioException e) {
            return List.of();
        }
    }
}