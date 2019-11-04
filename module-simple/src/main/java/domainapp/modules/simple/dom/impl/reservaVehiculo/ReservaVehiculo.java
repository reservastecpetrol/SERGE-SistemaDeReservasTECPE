package domainapp.modules.simple.dom.impl.reservaVehiculo;

import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;

import domainapp.modules.simple.dom.impl.enums.EstadoReserva;
import domainapp.modules.simple.dom.impl.enums.EstadoVehiculo;
import domainapp.modules.simple.dom.impl.persona.Persona;
import domainapp.modules.simple.dom.impl.vehiculo.Vehiculo;
import lombok.AccessLevel;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "simple",
        table = "ReservaVehiculo",
        detachable="true"
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
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "),
        @Query(
                name = "findByFechaReservaContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "
                        + "WHERE fechaReserva.indexOf(:fechaReserva) >= 0 "),
        @Query(
                name = "findByFechaReserva", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "
                        + "WHERE fechaReserva == :fechaReserva ")
})
//Se comenta de forma que permita realizar varias reservas en una misma fecha
//@Unique(name = "ReservaVehiculo_fechaReserva_UNQ", members = { "fechaReserva" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@lombok.Getter @lombok.Setter

/**
 * Esta clase define la entidad de dominio ReservaVehiculo
 * mediante la cual un usuario puede realizar la reserva de un
 * Vehiculo dada una fecha de Inicio
 * En la misma se definen todas sus propiedades y metodos.
 * Entre los cuales encontramos metodos Constructores,para eliminar un objeto
 * y metodos para modificar el estado de la reserva.
 *
 * @see vehiculo.Vehiculo
 * @see persona.Persona
 *
 * @author Cintia Millacura
 *
 */
public class ReservaVehiculo implements Comparable<ReservaVehiculo>, CalendarEventable {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Reserva";
    }

    //Definicion de las propiedades de la entidad ReservaVehiculo

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    @Title(prepend = "Reserva: ")
    private LocalDate fechaReserva; //esta variable hace referencia a la fecha en que se realiza la reserva

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private LocalDate fechaInicio; //esta variable hace referencia a la fecha de inicio en la cual se va a hacer uso de la reserva

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private LocalDate fechaFin; //esta variable hace referencia a la fecha en la que se va a dar fin a la reserva

    @javax.jdo.annotations.Column(allowsNull = "false",name="PERSONA_ID")
    @Persistent(defaultFetchGroup="true")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private Persona persona; //esta variable hace referencia al usuario que va a hacer uso de la reserva

    @javax.jdo.annotations.Column(allowsNull = "false",name="VEHICULO_ID")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    private Vehiculo vehiculo; //esta variable hace referencia al Vehiculo que va a ser reservado por el usuario

    @javax.jdo.annotations.Column(allowsNull = "false")
    @lombok.NonNull
    @Property(editing = Editing.ENABLED)
    // esta variable hace referencia al estado en el que se encuentra la reserva realizada por el usuario
    // los cuales pueden ser :
    //ACTIVA | CANCELADA | ARRIBADA
    private EstadoReserva estado;


    /**
     * Este es un metodo constructor por defecto
     *
     */
    public ReservaVehiculo(){}


    /**
     * Este es un metodo constructor con parametros
     *
     * @param fechaReserva -valor definido en el codigo
     * @param fechaInicio -valor ingresado por el usuario
     * @param fechaFin -valor ingresado por el usuario
     * @param persona -valor ingresado por el usuario
     * @param vehiculo -valor definido en el codigo
     * @param estado -valor definido en el codigo
     */
    public ReservaVehiculo(LocalDate fechaReserva,LocalDate fechaInicio,LocalDate fechaFin,
            Persona persona, Vehiculo vehiculo, EstadoReserva estado){
        this.fechaReserva=fechaReserva;
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
        this.persona=persona;
        this.vehiculo=vehiculo;
        this.estado=estado;
    }
    
    //Se implememento la interfaz CalendarEventable y se definen los metodos
    //que deben ser implementados para poder hacer uso del Calendario

    @Programmatic
    @Override
    public String getCalendarName() {

        return  getPersona().getJerarquia().name();
    }

    @Programmatic
    public String getNotes() {

        return persona.getNombre()+" "+ persona.getApellido();

    }

    @Programmatic
    @Override
    public CalendarEvent toCalendarEvent() {

        return new CalendarEvent(getFechaInicio().toDateTimeAtStartOfDay(), getCalendarName(), getNotes());
    }

    /**
     * Este metodo permite cancelar la ReservaVehiculo del sistema
     */
    @Action
    public void cancelar(){
        this.setEstado(EstadoReserva.CANCELADA);
    }

    /**
     * Este metodo permite eliminar la entidad de ReservaVehiculo del sistema
     */
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' FUE ELIMINADA LA RESERVA", title));
        this.vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
        repositoryService.remove(this);
    }

    //region > compareTo, toString
    @Override
    public int compareTo(final ReservaVehiculo other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "fechaReserva");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "fechaReserva");
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
