/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.model;

import javax.ejb.Local;
import java.util.*;
import cl.entities.*;

/**
 *
 * @author sistemas
 */
@Local
public interface ServicioLocal {
    
    void insertar(Object o);
    void sincronizar(Object o);
    
    //modulo categoria
    Categoria buscarCategoria(int codigo);
    void editarCategoria(int codigo,int estado);
    List<Categoria> getCategorias();
    
    // modulo usuario
    Usuario buscarUsuario(String rut);
    void editarUsuario(String rut,String clave);
    List<Usuario> getUsuarios();
    
    // moulo producto
    Producto buscarProducto(int codigo);
    void editarProducto(int codigo,int precio,int stock, int estado);
    List<Producto> getProductos();
    
    //inicar sesion
    Usuario iniciarSesion(String rut, String clave);
    
    
}
