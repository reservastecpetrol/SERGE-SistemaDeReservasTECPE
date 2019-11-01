package domainapp.modules.simple.dom.impl.vehiculo;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Vehiculo.class
)
public class VehiculoRepository {

    @Programmatic
    public java.util.List<Vehiculo> listAll() {
        return container.allInstances(Vehiculo.class);
    }

    @Programmatic
    public Vehiculo findByMatricula(
            final String matricula
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Vehiculo.class,
                        "findByMatricula",
                        "matricula", matricula));
    }

    @Programmatic
    public java.util.List<Vehiculo> findByMatriculaContains(
            final String matricula
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Vehiculo.class,
                        "findByMatriculaContains",
                        "matricula", matricula));
    }

    @Programmatic
    public Vehiculo create(final String matricula) {
        final Vehiculo vehiculo = container.newTransientInstance(Vehiculo.class);
        vehiculo.setMatricula(matricula);
        container.persistIfNotAlready(vehiculo);
        return vehiculo;
    }

    @Programmatic
    public Vehiculo findOrCreate(
            final String matricula
    ) {
        Vehiculo vehiculo = findByMatricula(matricula);
        if (vehiculo == null) {
            vehiculo = create(matricula);
        }
        return vehiculo;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
