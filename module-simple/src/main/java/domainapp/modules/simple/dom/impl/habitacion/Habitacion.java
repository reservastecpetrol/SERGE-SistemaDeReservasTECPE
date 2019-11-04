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

import domainapp.modules.simple.dom.impl.enums.EstadoHabitacion;
import domainapp.modules.simple.dom.impl.enums.TipoHabitacion;
import lombok.AccessLevel;
import static org.apache.isis.applib.annotation.CommandReification.ENABLED;
import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

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
 * Ademas de metodos que realizan la validacion de propiedades,
 * actualizacion de las propiedades y la funcion que permite
 * eliminar una entidad de Habitacion
 * Tambien estan definidos los metodos Constructores
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

    /**
     * Este es el metodo constructor por defecto
     */
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

    /**
     * Este metodo realiza la actualizacion de la variable nombre
     *
     * @param nombre
     * @return Habitacion
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "nombre")
    public Habitacion updateNombre(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "nombre")
            final String nombre) {
        setNombre(nombre);
        return this;
    }


    /**
     * Este metodo realiza la actualizacion de la variable ubicacion
     *
     * @param ubicacion
     * @return Habitacion
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith ="ubicacion")
    public Habitacion updateUbicacion(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Ubicacion")
            final String ubicacion) {
        setUbicacion(ubicacion);
        return this;
    }


    /**
     * Este metodo realiza la actualizacion de la variable categoria
     *
     * @param categoria
     * @return Habitacion
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith = "categoria")
    public TipoHabitacion updateCategoria(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "Categorias")
            final TipoHabitacion categoria) {
        setCategoria(categoria);
        return categoria;
    }


    /**
     * Este metodo realiza la actualizacion de la variable estado
     *
     * @param estado
     * @return Habitacion
     */
    @Action(semantics = IDEMPOTENT, command = ENABLED, publishing = Publishing.ENABLED, associateWith ="estado")
    public Habitacion updateEstado(
            @Parameter(maxLength = 40)
            @ParameterLayout(named = "estado")
            final EstadoHabitacion estado) {
        setEstado(estado);
        return this;
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

    public String default0UpdateUbicacion() {
        return getUbicacion();
    }


    /**
     * Este metodo realiza la validacion de la variable ubicacion
     * de forma que el usuario no ingrese un signo de exclamacion
     *
     * @param ubicacion
     * @return TranslatableString
     */
    public TranslatableString validate0UpdateUbicacion(final String ubicacion) {
        return ubicacion != null && ubicacion.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    /**
     * Este metodo permite eliminar la entidad de Habitacion
     */
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' FUE ELIMINADA", title));
        repositoryService.remove(this);
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
