package mx.uv.fiee.iinf.mystartapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase que modela las columnas que componen la tabla correspondiente de la bd.
 */
@Entity
public class Formulario {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo (name = "name")
    public String name;

    @ColumnInfo (name = "last_name")
    public String lastName;

    @ColumnInfo (name = "radio_selected")
    public int radioSelected;

    @ColumnInfo (name = "is_checked")
    public boolean isChecked;

    @ColumnInfo (name = "spinner_selected_item")
    public long spinnerSelectedItem;

    @ColumnInfo (name = "image")
    public String image;
}
