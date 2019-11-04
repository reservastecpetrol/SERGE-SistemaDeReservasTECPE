package domainapp.modules.simple.dom.impl.reservaVehiculo;

import java.util.List;

import org.datanucleus.query.typesafe.TypesafeQuery;

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
import domainapp.modules.simple.dom.impl.persona.PersonaRepository;
import domainapp.modules.simple.dom.impl.vehiculo.VehiculoRepository;
import lombok.AccessLevel;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.ReservaVehiculoMenu",
        repositoryFor = ReservaVehiculo.class
)
@DomainServiceLayout(
        named = "Reserva Vehiculos",
        menuOrder = "10"
)

/**
 * Esta clase es el servicio de dominio de la clase ReservaVehiculo
 * que define los metodos
 * que van a aparecer en el menu del sistema
 *
 * @author Cintia Millacura
 */
public class ReservaVehiculoRepository {

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
     * Este metodo lista todas las reservas de vehiculos que hay cargados
     * en el sistema
     *
     * @return List<ReservaVehiculo>
     */
    public java.util.List<ReservaVehiculo> listarReservasDeVehiculosTotales() {
        return container.allInstances(ReservaVehiculo.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    /**
     * Este metodo lista todos las Reservas Activas que hay cargados
     * en el sistema
     *
     * @return List<ReservaVehiculo>
     */
    public List<ReservaVehiculo> listarReservasDeVehiculosActivas() {
        return this.listarReservasPorEstado(EstadoReserva.ACTIVA);
    }


    /**
     * Este metodo permite recuperar en una lista todos las reservas realizadas
     * dado un estado en particular
     *
     * @param estado
     * @return List<ReservaVehiculo>
     */
    @Programmatic
    public List<ReservaVehiculo> listarReservasPorEstado(
            @ParameterLayout(named="Estado")
            final EstadoReserva estado
    ) {
        TypesafeQuery<ReservaVehiculo> tq = isisJdoSupport.newTypesafeQuery(ReservaVehiculo.class);
        final QReservaVehiculo cand = QReservaVehiculo.candidate();

        List<ReservaVehiculo> reservas = tq.filter(
                cand.estado.eq(tq.stringParameter("estado")))
                .setParameter("estado",estado).executeList();

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
    VehiculoRepository vehiculoRepository;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

}
