package gm.inventarios.excepecion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//clase para decir el error en el lado del cliente si hay algun error
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoExcepcion extends  RuntimeException{
    public RecursoNoEncontradoExcepcion(String mensaje ){
        super(mensaje);
    }
}
