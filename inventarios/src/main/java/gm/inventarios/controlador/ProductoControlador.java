package gm.inventarios.controlador;

import gm.inventarios.excepecion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//http://localhost:8080/inventario-app
@RequestMapping("inventario-app")
//peticiones desde el front-end de angular agregamos esta notación, que es el puerto y dirección default de angular.
@CrossOrigin(value = "http://localhost:4200")
public class ProductoControlador {

    //importante el Logger debe de ser de slf4j y del LoggerFactory de slf4j igual.
    //cuando mandemos información a la consola se va a especificar que es apartir de esa clase y utilizaremos
    //la variable logger para mandar información a la consola, en este caso el login de nuestra aplicación.
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    //ahora hacemos un autowired, para obtener toda la información de la base de datos mediante en el servicio.
    @Autowired
    private ProductoServicio productoServicio;

    //la url es: http://localhost:8080/inventario-app/productos
    @GetMapping("/productos")
    public List<Producto> obtenerProductos(){
        List<Producto> productos = this.productoServicio.listarProductos();

        //imprimimos para verificar que tod0 este bien en el logger, en consola.
        logger.info("Productos obtenidos:");
        productos.forEach((producto -> logger.info(producto.toString())));

        return productos;
    }
    
    @PostMapping("/productos")
    //se hace de requestbody ya que vamos a recibir la información desde un formulario de angular
    public Producto agregarProducto(@RequestBody Producto producto){
        logger.info("Producto a agregar: " + producto);
        return this.productoServicio.guardarProducto(producto);
    }

    @GetMapping("/productos/{id}")
    //regresar información de manera sencilla, api rest de spring
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable int id){
        Producto producto = this.productoServicio.buscarProductoPorId(id);

        //validamos que regresa algo, que lo haya encontrado
        if (producto != null){
            return ResponseEntity.ok(producto);
        }else{
            //la arrojamos para que la puede procesar el cliente
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        }

    }

    //el put es para actualizar un recurso ya existente
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable int id,
                                                       @RequestBody Producto productoRecibido){
        //buscamos el producto por id
        Producto producto = this.productoServicio.buscarProductoPorId(id);

        if(producto == null){
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: "+ id);
        }

        //actualizamos los datos con el producto encontrado con los datos que se cambiaron
        producto.setDescripcion(productoRecibido.getDescripcion());
        producto.setPrecio(productoRecibido.getPrecio());
        producto.setExistencia(productoRecibido.getExistencia());

        //lo guardamos
        this.productoServicio.guardarProducto(producto);

        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarProducto(@PathVariable int id){
        Producto producto = productoServicio.buscarProductoPorId(id);
        if (producto == null){
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        }else {
            this.productoServicio.eliminarProducto(producto.getIdProducto());
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("Eliminado", Boolean.TRUE);

            return ResponseEntity.ok(respuesta);
        }
    }
}
