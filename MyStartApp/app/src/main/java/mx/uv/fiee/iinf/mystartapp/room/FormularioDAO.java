package mx.uv.fiee.iinf.mystartapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interface que representa al objeto DAO y contiene las acciones
 * disponibles a realizar la base de datos.
 */
@Dao
public interface FormularioDAO {
    @Query("SELECT * FROM formulario")
    List<Formulario> getAll ();

    @Query ("Select * from formulario where id = :id")
    Formulario getFormulario (int id);

    @Insert
    void inserAll (Formulario...formularios);

    @Delete
    void delete (Formulario formulario);
}
