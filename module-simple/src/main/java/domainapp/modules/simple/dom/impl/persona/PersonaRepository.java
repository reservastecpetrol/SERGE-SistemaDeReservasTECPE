package domainapp.modules.simple.dom.impl.persona;

import java.util.List;

import org.datanucleus.query.typesafe.TypesafeQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
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

    /**
     * Este metodo permite encontrar una Persona en particular
     * dado un nombre
     *
     * @param nombre
     * @return List<Persona>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Persona> buscarPersonaPorNombre(
            @ParameterLayout(named="Nombre")
            final String nombre
    ) {
        TypesafeQuery<Persona> q = isisJdoSupport.newTypesafeQuery(Persona.class);
        final QPersona cand = QPersona.candidate();
        q = q.filter(
                cand.nombre.indexOf(q.stringParameter("nombre")).ne(-1)
        );
        return q.setParameter("nombre", nombre.toUpperCase())
                .executeList();
    }

    @Programmatic
    public Persona verificarUsuario(String dni){

        TypesafeQuery<Persona> q = isisJdoSupport.newTypesafeQuery(Persona.class);
        final QPersona cand = QPersona.candidate();

        q= q.filter(
                cand.dni.eq(q.stringParameter("dniIngresado"))
        );
        return  q.setParameter("dniIngresado",dni)
                .executeUnique();
    }


    @Programmatic
    public Persona create(final String nombre) {
        final Persona persona = container.newTransientInstance(Persona.class);
        persona.setNombre(nombre);
        container.persistIfNotAlready(persona);
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
