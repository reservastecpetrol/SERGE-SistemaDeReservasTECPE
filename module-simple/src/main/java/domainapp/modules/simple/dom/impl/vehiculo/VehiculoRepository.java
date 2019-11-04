package domainapp.modules.simple.dom.impl.vehiculo;

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

import domainapp.modules.simple.dom.impl.enums.EstadoVehiculo;
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
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Vehiculo";
    }

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

    /**
     * Este metodo lista todos los Vehiculos Disponibles que hay cargados
     * en el sistema
     *
     * @return List<Vehiculo>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Vehiculo> listarVehiculosDisponibles() {

        return this.listarVehiculosPorEstado(EstadoVehiculo.DISPONIBLE);
    }

    /**
     * Este metodo permite recuperar en una lista todos los Vehiculos
     * dado un estado en particular
     *
     * @param estado
     * @return List<Vehiculo>
     */
    @Programmatic
    public List<Vehiculo> listarVehiculosPorEstado(
            @ParameterLayout(named="Estado")
            final EstadoVehiculo estado
    ) {
        TypesafeQuery<Vehiculo> tq = isisJdoSupport.newTypesafeQuery(Vehiculo.class);
        final QVehiculo cand = QVehiculo.candidate();

        List<Vehiculo> vehiculos = tq.filter(
                cand.estado.eq(tq.stringParameter("estado")))
                .setParameter("estado",estado).executeList();

        return vehiculos;
    }

    /**
     * Este metodo permite encontrar un Vehiculo en particular
     * dada una matricula que es la que identifica de manera
     * unica a cada Vehiculo
     *
     * @param matricula
     * @return List<Vehiculo>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public List<Vehiculo> buscarVehiculoPorMatricula(
            @ParameterLayout(named="Matricula")
            final String matricula
    ) {
        TypesafeQuery<Vehiculo> q = isisJdoSupport.newTypesafeQuery(Vehiculo.class);
        final QVehiculo cand = QVehiculo.candidate();
        q = q.filter(
                cand.matricula.indexOf(q.stringParameter("matricula")).ne(-1)
        );
        return q.setParameter("matricula", matricula)
                .executeList();
    }

    @Programmatic
    public Vehiculo verificarVehiculo(String matricula){

        TypesafeQuery<Vehiculo> q = isisJdoSupport.newTypesafeQuery(Vehiculo.class);
        final QVehiculo cand = QVehiculo.candidate();

        q= q.filter(
                cand.matricula.eq(q.stringParameter("matriculaIngresada"))
        );
        return  q.setParameter("matriculaIngresada",matricula)
                .executeUnique();
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
