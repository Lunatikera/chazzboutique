package com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio;

import com.mycompany.chazzboutiquenegocio.dtos.ProveedorDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;

import java.util.List;

public interface IProveedorNegocio {
    List<ProveedorDTO> obtenerProveedores() throws NegocioException;
}
