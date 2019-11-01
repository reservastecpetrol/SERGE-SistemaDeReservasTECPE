package domainapp.modules.simple.dom.impl.reservaVehiculo;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ReservaVehiculo.class
)
public class ReservaVehiculoRepository {

    @Programmatic
    public java.util.List<ReservaVehiculo> listAll() {
        return container.allInstances(ReservaVehiculo.class);
    }

    @Programmatic
    public ReservaVehiculo findByFechaReserva(
            final String fechaReserva
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ReservaVehiculo.class,
                        "findByFechaReserva",
                        "fechaReserva", fechaReserva));
    }

    @Programmatic
    public java.util.List<ReservaVehiculo> findByFechaReservaContains(
            final LocalDate fechaReserva
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        ReservaVehiculo.class,
                        "findByFechaReservaContains",
                        "fechaReserva", fechaReserva));
    }

    @Programmatic
    public ReservaVehiculo create(final LocalDate fechaReserva) {
        final ReservaVehiculo reservaVehiculo = container.newTransientInstance(ReservaVehiculo.class);
        reservaVehiculo.setFechaReserva(fechaReserva);
        container.persistIfNotAlready(reservaVehiculo);
        return reservaVehiculo;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}