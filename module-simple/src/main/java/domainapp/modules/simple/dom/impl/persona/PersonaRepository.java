package domainapp.modules.simple.dom.impl.persona;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;

import lombok.AccessLevel;

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

    /**
     * Este metodo lista todos las personas que hay registradas
     * en el sistema
     *
     * @return List<Persona>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Persona> listarPersonas() {
        return repositoryService.allInstances(Persona.class);
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
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
