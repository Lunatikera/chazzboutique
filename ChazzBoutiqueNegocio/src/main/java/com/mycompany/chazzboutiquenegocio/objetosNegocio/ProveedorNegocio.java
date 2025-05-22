package com.mycompany.chazzboutiquenegocio.objetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.ProveedorDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IProveedorNegocio;
import com.mycompany.chazzboutiquepersistencia.dominio.Proveedor;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import com.mycompany.chazzboutiquepersistencia.interfacesDAO.IProveedorDAO;

import java.util.List;
import java.util.stream.Collectors;

public class ProveedorNegocio implements IProveedorNegocio {

    private final IProveedorDAO proveedorDAO;

    public ProveedorNegocio(IProveedorDAO proveedorDAO) {
        this.proveedorDAO = proveedorDAO;
    }

    @Override
    public List<ProveedorDTO> obtenerProveedores() throws NegocioException {
        try {
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            return proveedores.stream()
                    .map(p -> new ProveedorDTO(p.getId(), p.getNombre()))
                    .collect(Collectors.toList());
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener proveedores", e);
        }
    }
}
