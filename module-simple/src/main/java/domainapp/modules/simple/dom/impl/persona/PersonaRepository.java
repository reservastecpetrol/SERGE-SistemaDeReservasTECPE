package domainapp.modules.simple.dom.impl.persona;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Persona.class
)
public class PersonaRepository {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Persona";
    }

    @Programmatic
    public java.util.List<Persona> listAll() {
        return container.allInstances(Persona.class);
    }

    @Programmatic
    public Persona findByNombre(
            final String nombre
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Persona.class,
                        "findByNombre",
                        "nombre", nombre));
    }

    @Programmatic
    public java.util.List<Persona> findByNombreContains(
            final String nombre
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Persona.class,
                        "findByNombreContains",
                        "nombre", nombre));
    }

    @Programmatic
    public Persona create(final String nombre) {
        final Persona persona = container.newTransientInstance(Persona.class);
        persona.setNombre(nombre);
        container.persistIfNotAlready(persona);
        return persona;
    }

    @Programmatic
    public Persona findOrCreate(
            final String nombre
    ) {
        Persona persona = findByNombre(nombre);
        if (persona == null) {
            persona = create(nombre);
        }
        return persona;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
