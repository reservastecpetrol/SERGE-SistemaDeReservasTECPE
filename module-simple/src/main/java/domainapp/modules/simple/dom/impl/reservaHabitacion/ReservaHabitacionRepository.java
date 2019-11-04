package domainapp.modules.simple.dom.impl.reservaHabitacion;

import java.util.List;

import org.datanucleus.query.typesafe.TypesafeQuery;
import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.impl.enums.EstadoReserva;
import domainapp.modules.simple.dom.impl.habitacion.HabitacionRepository;
import domainapp.modules.simple.dom.impl.persona.Persona;
import domainapp.modules.simple.dom.impl.persona.PersonaRepository;
import lombok.AccessLevel;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.ReservaHabitacionMenu",
        repositoryFor = ReservaHabitacion.class
)
@DomainServiceLayout(
        named = "Reservas Habitacion",
        menuOrder = "10"
)

/**
 * Esta clase es el servicio de dominio de la clase ReservaHabitacion
 * que define los metodos
 * que van a aparecer en el menu del sistema
 *
 * @author Francisco Bellani
 */
public class ReservaHabitacionRepository {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Reserva";
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    /**
     * Este metodo lista todas las reservas de habitaciones que hay cargados
     * en el sistema
     *
     * @return List<ReservaHabitacion>
     */
    public java.util.List<ReservaHabitacion> listarReservasDeHabitaciones() {
        return container.allInstances(ReservaHabitacion.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    /**
     * Este metodo lista todos las Reservas Activas que hay cargados
     * en el sistema
     *
     * @return List<ReservaHabitacion>
     */
    public List<ReservaHabitacion> listarReservasDeHabitacionesActivas() {
        return this.listarReservasPorEstado(EstadoReserva.ACTIVA);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "3")
    /**
     * Este metodo lista todos las Reservas Canceladas que hay cargados
     * en el sistema
     *
     * @return List<ReservaHabitacion>
     */
    public List<ReservaHabitacion> listarReservasDeHabitacionesCanceladas() {
        return this.listarReservasPorEstado(EstadoReserva.CANCELADA);
    }

    /**
     * Este metodo permite recuperar en una lista todos las reservas realizadas
     * dado un estado en particular
     *
     * @param estado
     * @return List<ReservaHabitacion>
     */
    @Programmatic
    public List<ReservaHabitacion> listarReservasPorEstado(
            @ParameterLayout(named="Estado")
            final EstadoReserva estado
    ) {
        TypesafeQuery<ReservaHabitacion> tq = isisJdoSupport.newTypesafeQuery(ReservaHabitacion.class);
        final QReservaHabitacion cand = QReservaHabitacion.candidate();

        List<ReservaHabitacion> reservas = tq.filter(
                cand.estado.eq(tq.stringParameter("estado")))
                .setParameter("estado",estado).executeList();

        return reservas;
    }

    @Programmatic
    /**
     * Este metodo lista todos los usuarios que hay en el sistema de
     * forma que el administrador seleccione a uno en especifico
     *
     * @return Collection<Persona>
     *
     */
    public List<Persona> choices0ListarReservasDeHabitacionesPorPersona() {
        return personaRepository.listarPersonas();
    }

    /**
     * Este metodo permite encontrar todas las reservas
     * realizadas por un usuario en particular
     *
     * @param persona
     * @return List<ReservaHabitacion>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public List<ReservaHabitacion> listarReservasDeHabitacionesPorPersona(
            @ParameterLayout(named="Persona")
            final Persona persona
    ) {

        List<ReservaHabitacion> reservas;

        TypesafeQuery<ReservaHabitacion> q = isisJdoSupport.newTypesafeQuery(ReservaHabitacion.class);

        final QReservaHabitacion cand = QReservaHabitacion.candidate();

        reservas= q.filter(
                cand.persona.dni.eq(q.stringParameter("dniIngresado")))
                .setParameter("dniIngresado",persona.getDni())
                .executeList();

        return reservas;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "5")
    /**
     * Este metodo lista todas las reservas de habitaciones que hay cargados
     * en el sistema en el dia de la fecha
     *
     * @return List<ReservaHabitacion>
     */
    public List<ReservaHabitacion> listarReservasQueInicianHoy() {

        List<ReservaHabitacion> reservas;

        TypesafeQuery<ReservaHabitacion> q = isisJdoSupport.newTypesafeQuery(ReservaHabitacion.class);

        final QReservaHabitacion cand = QReservaHabitacion.candidate();

        reservas = q.filter(
                cand.fechaInicio.eq(LocalDate.now()))
                .executeList();
        return reservas;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "6")
    /**
     * Este metodo permite listar todas las reservas de habitaciones
     * dada una fecha de reserva
     *
     * @param fechaReseva
     * @return List<ReservaHabitacion>
     */
    public List<ReservaHabitacion> buscarReservasPorFechaDeReserva(
            @ParameterLayout(named="Fecha Reserva")
            final LocalDate fechaReserva
    ) {
        List<ReservaHabitacion> reservas;

        TypesafeQuery<ReservaHabitacion> q = isisJdoSupport.newTypesafeQuery(ReservaHabitacion.class);

        final QReservaHabitacion cand = QReservaHabitacion.candidate();

        reservas = q.filter(
                cand.fechaReserva.eq(q.stringParameter("fecha")))
                .setParameter("fecha",fechaReserva)
                .executeList();
        return reservas;
    }


    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;

    @javax.inject.Inject
    PersonaRepository personaRepository;

    @javax.inject.Inject
    HabitacionRepository habitacionRepository;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;
}
