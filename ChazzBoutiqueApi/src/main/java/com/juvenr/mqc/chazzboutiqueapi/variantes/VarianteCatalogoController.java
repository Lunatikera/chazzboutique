package com.juvenr.mqc.chazzboutiqueapi.variantes;

import com.juvenr.mqc.chazzboutiqueapi.home.dto.PagedResponse;
import com.juvenr.mqc.chazzboutiqueapi.home.dto.VarianteCardResponse;
import com.mycompany.chazzboutiquenegocio.dtos.VarianteProductoDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IVarianteProductoNegocio;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/variantes")
public class VarianteCatalogoController {

    private final IVarianteProductoNegocio varianteNegocio;

    public VarianteCatalogoController(IVarianteProductoNegocio varianteNegocio) {
        this.varianteNegocio = varianteNegocio;
    }

    @GetMapping
    public PagedResponse<VarianteCardResponse> listar(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "12") int pageSize,
            @RequestParam(value = "categoriaId", required = false) Integer categoriaId
    ) {
        String filtro = (search == null) ? "" : search.trim();
        int p = Math.max(1, page);
        int s = Math.max(1, Math.min(pageSize, 50));

        try {
            boolean filtrarPorCategoria = (categoriaId != null && categoriaId > 0);

            List<VarianteProductoDTO> itemsNegocio;
            long total;

            if (filtrarPorCategoria) {
                itemsNegocio = varianteNegocio.buscarVariantesPorNombreProductoYCategoria(filtro, categoriaId, p, s);
                total = varianteNegocio.contarVariantesPorNombreProductoYCategoria(filtro, categoriaId);
            } else {
                itemsNegocio = varianteNegocio.buscarVariantesPorNombreProducto(filtro, p, s);
                total = varianteNegocio.contarVariantesPorNombreProducto(filtro);
            }

            boolean hasNext = (long) p * s < total;

            List<VarianteCardResponse> items = itemsNegocio.stream().map(v -> {
                VarianteCardResponse r = new VarianteCardResponse();
                r.setId(v.getId());
                r.setNombreProducto(v.getNombreProducto());
                r.setTalla(v.getTalla());
                r.setColorHex(v.getColor());
                r.setPrecioVenta(v.getPrecioVenta());
                r.setStock(v.getStock());

                String urlImg = v.getUrlImagen();
                if (urlImg == null || urlImg.trim().isEmpty()) {
                    r.setImagenUrl(null);
                } else {
                    r.setImagenUrl("/api/imagenes/" + urlImg);
                }

                return r;
            }).toList();

            PagedResponse<VarianteCardResponse> res = new PagedResponse<>();
            res.setPage(p);
            res.setPageSize(s);
            res.setTotal(total);
            res.setHasNext(hasNext);
            res.setItems(items);
            return res;

        } catch (NegocioException e) {
            PagedResponse<VarianteCardResponse> res = new PagedResponse<>();
            res.setPage(p);
            res.setPageSize(s);
            res.setTotal(0);
            res.setHasNext(false);
            res.setItems(List.of());
            return res;
        }
    }
}
