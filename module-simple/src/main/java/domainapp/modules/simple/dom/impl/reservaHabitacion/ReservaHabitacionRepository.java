package domainapp.modules.simple.dom.impl.reservaHabitacion;

import org.apache.isis.applib.annotation.*;

import javax.jdo.annotations.*;

import lombok.Getter;
import lombok.Setter;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ReservaHabitacion.class
)
public class ReservaHabitacionRepository {

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

    @Programmatic
    public ReservaHabitacion findOrCreate(
            final LocalDate fechaReserva
    ) {
        ReservaHabitacion reservaHabitacion = findByFechaReserva(fechaReserva);
        if (reservaHabitacion == null) {
            reservaHabitacion = create(fechaReserva);
        }
        return reservaHabitacion;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
