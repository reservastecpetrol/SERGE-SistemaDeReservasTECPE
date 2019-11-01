package domainapp.modules.simple.dom.impl.persona;

import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;

import domainapp.modules.simple.dom.impl.enums.TipoJerarquia;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "simple",
        table = "Persona"
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
                        + "FROM domainapp.modules.simple.dom.impl.persona.Persona "),
        @Query(
                name = "findByNombreContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.persona.Persona "
                        + "WHERE nombre.indexOf(:nombre) >= 0 "),
        @Query(
                name = "findByNombre", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.persona.Persona "
                        + "WHERE nombre == :nombre ")
})
//Se comenta de forma que se pueda ingresar indefinidamente el mismo nombre de un Usuario del sistema
//@Unique(name = "Persona_nombre_UNQ", members = { "nombre" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@lombok.Getter @lombok.Setter
/**
 * Esta clase define la entidad de dominio Persona que hace referencia al Usuario del sistema definiendo asi
 * todas sus propiedades asi tambien sus metodos constructores,de validacion de variables,actualizacion de variables
 * y ademas poder relizar la accion de eliminar una entidad Persona.
 *
 * @author Francisco Bellani
 *
 */

public class Persona implements Comparable<Persona> {

    //Se defininen todas las propiedades de la entidad de dominio Persona

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title(prepend = "Persona: ")
    private String nombre; //esta variable hace referencia al nombre de la entidad Persona

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property() // editing disabled by default, see isis.properties
    @Title()
    private String apellido; //esta variable hace referencia al apellido de la entidad Persona

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String direccion; //esta variable hace referencia a la dirección de la entidad Persona

    @javax.jdo.annotations.Column(allowsNull = "false", length =10)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String telefono; //esta variable hace referencia al telefono de la entidad Persona

    @javax.jdo.annotations.Column()
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String email; //esta variable hace referencia a la direccion de email de la entidad Persona

    @javax.jdo.annotations.Column(allowsNull ="false", length = 8)
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private String dni; //esta variable hace referencia al numero de documento (DNI) de la entidad Persona

    //listado de Jerarquias dropdown menu
    @javax.jdo.annotations.Column(allowsNull="false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private TipoJerarquia jerarquia; //esta variable hace referencia al tipo de jerarquia de la entidad Persona

    public Persona(){}

    /**
     * Este es un metodo constructor
     *
     * @param nombre
     * @param apellido
     * @param direccion
     * @param telefono
     * @param email
     * @param dni
     * @param jerarquia
     */
    public Persona(String nombre,String apellido,String direccion,String telefono,String email,String dni,TipoJerarquia jerarquia){
        this.nombre=nombre;
        this.apellido=apellido;
        this.direccion=direccion;
        this.telefono=telefono;
        this.email=email;
        this.dni=dni;
        this.jerarquia=jerarquia;
    }
    //region > compareTo, toString
    @Override
    public int compareTo(final Persona other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "nombre");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "nombre");
    }
    //endregion

}
