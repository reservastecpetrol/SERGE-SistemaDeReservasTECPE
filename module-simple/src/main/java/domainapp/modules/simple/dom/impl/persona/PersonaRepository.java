package domainapp.modules.simple.dom.impl.persona;

import java.util.List;
import java.util.regex.Pattern;

import org.datanucleus.query.typesafe.TypesafeQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.impl.SimpleObjects;
import domainapp.modules.simple.dom.impl.enums.TipoJerarquia;
import lombok.AccessLevel;


@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "simple.PersonaMenu",
        repositoryFor = Persona.class
)
@DomainServiceLayout(
        named = "Personas",
        menuOrder = "10"
)

/**
 * Esta clase es el servicio de dominio de la clase Persona
 * que define los metodos
 * que van a aparecer en el menu del sistema
 *
 * @author Francisco Bellani
 *
 */
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
     * Este metodo lista todas las personas con jerarquia de Ejecutivos que hay cargados
     * en el sistema
     *
     * @return List<Persona>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Persona> listarPersonasEjecutivas() {

        return this.listarPersonasPorJerarquia(TipoJerarquia.Ejecutivos);
    }

    /**
     * Este metodo lista todas las personas con jerarquia de Supervisores que hay cargados
     * en el sistema
     *
     * @return List<Persona>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "3")
    public List<Persona> listarPersonasSupervisores() {

        return this.listarPersonasPorJerarquia(TipoJerarquia.Supervisores);
    }

    /**
     * Este metodo lista todas las personas con jerarquia de Operadores que hay cargados
     * en el sistema
     *
     * @return List<Persona>
     */
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public List<Persona> listarPersonasOperadores() {

        return this.listarPersonasPorJerarquia(TipoJerarquia.Operadores);
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
    @MemberOrder(sequence = "5")
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


    /**
     * Este metodo permite recuperar en una lista todas las Personas
     * dado una jerarquia en particular
     *
     * @param jerarquia
     * @return List<Persona>
     */
    @Programmatic
    public List<Persona> listarPersonasPorJerarquia(
            @ParameterLayout(named="Jerarquia")
            final TipoJerarquia jerarquia
    ) {
        TypesafeQuery<Persona> tq = isisJdoSupport.newTypesafeQuery(Persona.class);
        final QPersona cand = QPersona.candidate();

        List<Persona> personas = tq.filter(
                cand.jerarquia.eq(tq.stringParameter("jerarquia")))
                .setParameter("jerarquia",jerarquia).executeList();

        return personas;
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


    public static class CreateDomainEvent extends ActionDomainEvent<SimpleObjects> {}
    @Action(domainEvent = SimpleObjects.CreateDomainEvent.class)
    @MemberOrder(sequence = "6")
    /**
     * Este metodo permite crear la entidad de dominio Persona
     * con los datos que va a ingresar el usuario
     *
     *
     * @param nombre
     * @param apellido
     * @param direccion
     * @param telefono
     * @param email
     * @param dni
     * @param jerarquias
     *
     *
     * @return Persona
     */
    public void crearPersona(
            @Parameter(
                    regexPattern = "[A-Za-z ]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese dato correcto"
            )
            @ParameterLayout(named="Nombre") final String nombre,
            @Parameter(
                    regexPattern = "[A-Za-z ]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese dato correcto"
            )
            @ParameterLayout(named="Apellido")final String apellido,
            @Parameter(
                    regexPattern = "[A-Za-z ]+[0-9]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese dato correcto"
            )
            @ParameterLayout(named="Direccion")final String direccion,
            @Parameter(
                    regexPattern = "[0-9]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese dato correcto"
            )
            @ParameterLayout(named="Telefono") final String telefono,
            @Parameter(
                    regexPattern = "(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese una dirección de correo electrónico válida (contienen un símbolo '@') -"
            )
            @ParameterLayout(named="Email") final String email,
            @Parameter(
                    regexPattern = "[0-9]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese dato correcto"
            )
            @ParameterLayout(named="Dni") final String dni,
            @ParameterLayout(named="Jerarquia") TipoJerarquia jerarquia
    )
    {
        if (verificarUsuario(dni)==null) {
            repositoryService.persist(
                    new Persona(nombre.toUpperCase(), apellido.toUpperCase(), direccion.toUpperCase(), telefono, email,
                            dni, jerarquia));

        }else{
            String mensaje="Este Usuario ya se encuentra cargado en el sistema!";
            messageService.informUser(mensaje);
        }
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
