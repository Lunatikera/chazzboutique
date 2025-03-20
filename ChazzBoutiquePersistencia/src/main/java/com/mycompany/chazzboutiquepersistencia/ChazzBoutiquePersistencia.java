/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chazzboutiquepersistencia;

import com.mycompany.chazzboutiquepersistencia.dominio.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author carli
 */
public class ChazzBoutiquePersistencia {

    public static void main(String[] args) {
    // Crear una fábrica de administradores de entidades
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ChazzBoutique");
        EntityManager em = emf.createEntityManager();

        try {
            // Crear una nueva instancia de AcademicUnityEntity
            Usuario usuario = new Usuario("Yalam", "12345", "Admin", Boolean.TRUE, LocalDate.now());

            // Iniciar una transacción
            em.getTransaction().begin();

            // Persistir la entidad en la base de datos
            em.persist(usuario);

            // Confirmar la transacción
            em.getTransaction().commit();

            // Imprimir la entidad persistida con su ID asignado
            System.out.println("Entidad persistida: " + usuario);

        } catch (Exception e) {
            System.out.println("111");
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            // Cerrar el EntityManager y la EntityManagerFactory
            em.close();
            emf.close();
        }
    }
}
