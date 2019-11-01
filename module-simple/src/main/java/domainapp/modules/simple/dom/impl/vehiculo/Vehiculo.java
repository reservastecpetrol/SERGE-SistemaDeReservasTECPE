package domainapp.modules.simple.dom.impl.vehiculo;

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

import domainapp.modules.simple.dom.impl.enums.EstadoVehiculo;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "simple",
        table = "Vehiculo"
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
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "),
        @Query(
                name = "findByMatriculaContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "
                        + "WHERE matricula.indexOf(:matricula) >= 0 "),
        @Query(
                name = "findByMatricula", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "
                        + "WHERE matricula == :matricula ")
})
@Unique(name = "Vehiculo_matricula_UNQ", members = { "matricula" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@lombok.Getter @lombok.Setter
/**
 * Esta clase define la entidad de dominio Vehiculo
 * con todas sus propiedades.
 * Ademas de metodos que realizan
 * Validacion de propiedades
 * Actualizacion de propiedades
 * Eliminar una entidad de Vehiculo
 * Un metodo Constructor
 *Metodos para modificar los estados de la entidad Vehiculo
 *
 *
 * @author Cintia Millacura
 *
 */
public class Vehiculo implements Comparable<Vehiculo> {

    //Definicion de las propiedades de la entidad Vehiculo

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Vehiculo: ")
    private String matricula; //esta variable hace referencia a la matricula de la entidad Vehiculo

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String marca; // esta variable hace referencia a la marca de la entidad Vehiculo

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String color; // esta variable hace referencia al color de la entidad Vehiculo

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String modelo; // esta variable hace referencia al modelo de la entidad Vehiculo

    @javax.jdo.annotations.Column()
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private boolean combustible; // esta variable booleana hace referencia a si la entidad Vehiculo cuenta con combustible

    @javax.jdo.annotations.Column()
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private boolean seguro; // esta variable booleana hace referencia a si la entidad Vehiculo cuenta con seguro


    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String ubicacion; //esta variable hace referencia a la ubicacion de la entidad Vehiculo


    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    // esta variable hace referencia al estado en el que se encuentra la entidad Vehiculo
    // los cuales pueden ser :
    // DISPONIBLE|| OCUPADO || REPARACION || INACTIVO
    private EstadoVehiculo estado;

    /**
     *Este es un metodo constructor
     *
     *@param matricula -valor ingresado por el usuario
     *@param marca -valor ingresado por el usuario
     *@param color  -valor ingresado por el usuario
     *@param modelo  -valor ingresado por el usuario
     *@param combustible -valor ingresado por el usuario
     *@param seguro -valor ingresado por el usuario
     *@param ubicacion -valor ingresado por el usuario
     *@param estado  -valor definido en el codigo
     *
     */
    public Vehiculo(String matricula,String marca,String color,String modelo,boolean combustible,boolean seguro,String ubicacion,EstadoVehiculo estado){
        this.matricula=matricula;
        this.marca=marca;
        this.color=color;
        this.modelo=modelo;
        this.combustible=combustible;
        this.seguro=seguro;
        this.ubicacion=ubicacion;
        this.estado=estado;
    }


    //region > compareTo, toString
    @Override
    public int compareTo(final Vehiculo other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "matricula");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "matricula");
    }
    //endregion

}
