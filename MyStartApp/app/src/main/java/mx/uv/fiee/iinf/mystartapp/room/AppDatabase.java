package mx.uv.fiee.iinf.mystartapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Clase abstracta que hereda de la clase RoomDatabase y permite
 * obtener la referencia al objeto DAO que se usará para las operaciones CRUD
 */
@Database (entities = {Formulario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FormularioDAO formularioDAO ();
}
