package domainapp.modules.simple.dom.impl.habitacion;

import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;

import domainapp.modules.simple.dom.impl.enums.EstadoHabitacion;
import domainapp.modules.simple.dom.impl.enums.TipoHabitacion;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "simple",
        table = "Habitacion"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "),
        @Query(
                name = "findByNombreContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "
                        + "WHERE nombre.indexOf(:nombre) >= 0 "),
        @Query(
                name = "findByNombre", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "
                        + "WHERE nombre == :nombre ")
})
@Unique(name = "Habitacion_nombre_UNQ", members = { "nombre" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@lombok.Getter @lombok.Setter

/**
 * Esta clase define la entidad de dominio Habitacion
 * con todas sus propiedades.
 * Ademas de metodos que realizan
 * Validacion de propiedades
 * Actualizacion de propiedades
 * Eliminar una entidad de Habitacion
 * Un metodo Constructor
 *Metodos para modificar los estados de la entidad Habitacion
 *
 *
 * @author Francisco Bellani
 *
 */
public class Habitacion implements Comparable<Habitacion> {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Habitacion";
    }
    
    //Definicion de las propiedades de la entidad Habitacion

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Habitacion: ")
    private String nombre;  //esta variable hace referencia al numero que identifica a la entidad Habitacion

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String ubicacion;   //esta variable hace referencia a la ubicacion en que se encuentra la entidad Habitacion

    @javax.jdo.annotations.Column(allowsNull = "true", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    // esta variable hace referencia al estado en el que se encuentra la entidad Habiatcion
    // los cuales pueden ser :
    // DISPONIBLE|| OCUPADA || MANTENIMIENTO|| INACTIVA
    private EstadoHabitacion estado;


    // esta variable hace referencia al tipo de categoria a la que pertenece la entidad Habitacion
    // Ejecutivas || Estandar || Simple
    @javax.jdo.annotations.Column(allowsNull="true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private TipoHabitacion categoria;

    public Habitacion(){}

    /**
     * Este es un metodo constructor
     *
     *
     * @param nombre -valor ingresado por el usuario
     * @param ubicacion -valor ingresado por el usuario
     * @param categoria -valor ingresado por el usuario
     * @param estado  -valor definido en el codigo
     */
    Habitacion(String nombre,String ubicacion, TipoHabitacion categoria,EstadoHabitacion estado){
        this.nombre=nombre;
        this.ubicacion=ubicacion;
        this.categoria=categoria;
        this.estado=estado;
    }


    //region > compareTo, toString
    @Override
    public int compareTo(final Habitacion other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "nombre");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "nombre");
    }
    //endregion

}
