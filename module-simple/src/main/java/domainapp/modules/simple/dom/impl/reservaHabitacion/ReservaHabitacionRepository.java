package domainapp.modules.simple.dom.impl.reservaHabitacion;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ReservaHabitacion.class
)
public class ReservaHabitacionRepository {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Reserva";
    }
    
    @Programmatic
    public java.util.List<ReservaHabitacion> listAll() {
        return container.allInstances(ReservaHabitacion.class);
    }

    @Programmatic
    public ReservaHabitacion findByFechaReserva(
            final String fechaReserva
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ReservaHabitacion.class,
                        "findByFechaReserva",
                        "fechaReserva", fechaReserva));
    }

    @Programmatic
    public java.util.List<ReservaHabitacion> findByFechaReservaContains(
            final LocalDate fechaReserva
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ReservaHabitacion.class,
                        "findByFechaReservaContains",
                        "fechaReserva", fechaReserva));
    }

    @Programmatic
    public ReservaHabitacion create(final LocalDate fechaReserva) {
        final ReservaHabitacion reservaHabitacion = container.newTransientInstance(ReservaHabitacion.class);
        reservaHabitacion.setFechaReserva(fechaReserva);
        container.persistIfNotAlready(reservaHabitacion);
        return reservaHabitacion;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
