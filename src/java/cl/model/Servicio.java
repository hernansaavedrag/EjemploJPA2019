/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.model;

import cl.entities.Categoria;
import cl.entities.Producto;
import cl.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sistemas
 */
@Stateless
public class Servicio implements ServicioLocal {

    @PersistenceContext(unitName = "EjemploJPA2019PU")
    private EntityManager em;

    @Override
    public void insertar(Object o) {
        em.persist(o);
    }

    @Override
    public void sincronizar(Object o) {
        em.merge(o);
        em.flush();
    }

    @Override
    public Categoria buscarCategoria(int codigo) {
        return em.find(Categoria.class, codigo);
    }

    @Override
    public void editarCategoria(int codigo, int estado) {
        Categoria cat = buscarCategoria(codigo);
        cat.setEstado(estado);
        em.merge(cat);
        em.flush();
        em.refresh(cat);
    }

    @Override
    public List<Categoria> getCategorias() {
        return em.createQuery("select c from Categoria c").getResultList();
    }

    @Override
    public Usuario buscarUsuario(String rut) {
        return em.find(Usuario.class, rut);
                
    }

    @Override
    public void editarUsuario(String rut, String clave) {
        Usuario user = buscarUsuario(rut);
        user.setClave(clave);
        em.merge(user);
        em.flush();
        em.refresh(user);
    }

    @Override
    public List<Usuario> getUsuarios() {
        return em.createQuery("select u from Usuario u").getResultList();
    }

    @Override
    public Producto buscarProducto(int codigo) {
       return em.find(Producto.class, codigo);
    }

    @Override
    public void editarProducto(int codigo, int precio, int stock, int estado) {
        Producto p = buscarProducto(codigo);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setEstado(estado);
        em.merge(p);
        em.flush();
        em.refresh(p);
        
    }
    @Override
    public List<Producto> getProductos() {
        return em.createQuery("select p from Producto p").getResultList();
    }

    @Override
    public Usuario iniciarSesion(String rut, String clave) {
        Usuario user = buscarUsuario(rut);
        
        return (user != null && user.getClave().equals(clave))?user:null;
    }

    
    
    
}
