package com.mycompany.chazzboutiquepersistencia.interfacesDAO;

import com.mycompany.chazzboutiquepersistencia.dominio.Proveedor;
import com.mycompany.chazzboutiquepersistencia.excepciones.PersistenciaException;
import java.util.List;

public interface IProveedorDAO {
    List<Proveedor> obtenerTodos() throws PersistenciaException;
}
