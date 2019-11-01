package domainapp.modules.simple.dom.impl.persona;

import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.dom.impl.enums.TipoJerarquia;
import lombok.AccessLevel;
import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

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

    /**
     * Este metodo realiza la actualizacion de la variable nombre de la entidad Persona
     *
     * @param nombre
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "nombre")
    public Persona updateNombre(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Nombre")
            final String nombre) {
        setNombre(nombre);
        return this;
    }

    /**
     * Este metodo realiza la actualizacion de la variable apellido de la entidad Persona
     *
     * @param apellido
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "apellido")
    public Persona updateApellido(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Apellido")
            final String apellido) {
        setApellido(apellido);
        return this;
    }

    /**
     * Este metodo realiza la actualizacion de la variable direccion de la entidad Persona
     *
     * @param direccion
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "direccion")
    public Persona updateDireccion(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Direccion")
            final String direccion) {
        setDireccion(direccion);
        return this;
    }

    /**
     * Este metodo realiza la actualizacion de la variable telefono de la entidad Persona
     *
     * @param telefono
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "telefono")
    public Persona updateTelefono(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Telefono")
            final String telefono) {
        setTelefono(telefono);
        return this;
    }

    /**
     * Este metodo realiza la actualizacion de la variable email de la entidad Persona
     *
     * @param email
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "email")
    public Persona updateEmail(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Email")
            final String email) {
        setEmail(email);
        return this;
    }


    /**
     * Este metodo realiza la actualizacion de la variable dni de la entidad Persona
     *
     * @param dni
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "dni")
    public Persona updateDni(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Dni")
            final String dni) {
        setEmail(dni);
        return this;
    }

    /**
     * Este metodo realiza la actualizacion de la variable jerarquia de la entidad Persona
     *
     * @param jerarquia
     * @return Persona
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "jerarquia")
    public TipoJerarquia updateJerarquia(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Jerarquias")
            final TipoJerarquia jerarquia) {
        setJerarquia(jerarquia);
        return jerarquia;
    }

    public String default0UpdateNombre() {
        return getNombre();
    }


    /**
     * Este metodo realiza la validacion de la variable nombre
     * de forma que el usuario no ingrese un signo de exclamacion
     *
     * @param nombre
     * @return TranslatableString
     */
    public TranslatableString validate0UpdateNombre(final String nombre) {
        return nombre != null && nombre.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }


    public String default0UpdateApellido() {
        return getApellido();
    }


    /**
     * Este metodo realiza la validacion de la variable apellido
     * de forma que el usuario no ingrese un signo de exclamacion
     *
     * @param apellido
     * @return TranslatableString
     */
    public TranslatableString validate0UpdateApellido(final String apellido) {
        return apellido != null && apellido.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }


    /**
     * Este metodo realiza la validacion de la variable direccion
     * de forma que el usuario no ingrese un signo de exclamacion
     *
     * @param direccion
     * @return TranslatableString
     */
    public TranslatableString validate0UpdateDireccion(final String direccion) {
        return direccion != null && direccion.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    /**
     * Este metodo realiza la validacion de la variable telefono
     * de forma que el usuario no ingrese un signo de exclamacion
     *
     * @param telefono
     * @return TranslatableString
     */
    public TranslatableString validate0UpdateTelefono(final String telefono) {
        return telefono != null && telefono.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    /**
     * Este metodo permite eliminar la entidad de Persona
     */
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' FUE ELIMINADA ESTA PERSONA", title));
        repositoryService.remove(this);
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


    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}
