package domainapp.modules.simple.dom.impl.vehiculo;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;

import lombok.AccessLevel;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.VehiculoMenu",
        repositoryFor = Vehiculo.class
)
@DomainServiceLayout(
        named = "Vehiculos",
        menuOrder = "10"
)

/**
 *
 * Esta clase es el servicio de dominio de la clase Vehiculo
 * que define los metodos
 * que van a aparecer en el menu del sistema
 *
 *@author Cintia Millacura
 *
 */
public class VehiculoRepository {

    /**
     * Este metodo lista todos los Vehiculos que hay cargados
     * en el sistema
     *
     * @return List<Vehiculo>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Vehiculo> listarVehiculos() {
        return repositoryService.allInstances(Vehiculo.class);
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
