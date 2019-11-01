package domainapp.modules.simple.dom.impl.habitacion;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Habitacion.class
)
public class HabitacionRepository {

    @Programmatic
    public java.util.List<Habitacion> listAll() {
        return container.allInstances(Habitacion.class);
    }

    @Programmatic
    public Habitacion findByNombre(
            final String nombre
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Habitacion.class,
                        "findByNombre",
                        "nombre", nombre));
    }

    @Programmatic
    public java.util.List<Habitacion> findByNombreContains(
            final String nombre
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Habitacion.class,
                        "findByNombreContains",
                        "nombre", nombre));
    }

    @Programmatic
    public Habitacion create(final String nombre) {
        final Habitacion habitacion = container.newTransientInstance(Habitacion.class);
        habitacion.setNombre(nombre);
        container.persistIfNotAlready(habitacion);
        return habitacion;
    }

    @Programmatic
    public Habitacion findOrCreate(
            final String nombre
    ) {
        Habitacion habitacion = findByNombre(nombre);
        if (habitacion == null) {
            habitacion = create(nombre);
        }
        return habitacion;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
