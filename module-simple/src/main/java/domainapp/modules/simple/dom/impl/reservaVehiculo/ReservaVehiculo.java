package domainapp.modules.simple.dom.impl.reservaVehiculo;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "simple",
        table = "ReservaVehiculo",
        detachable="true"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "),
        @Query(
                name = "findByFechaReservaContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "
                        + "WHERE fechaReserva.indexOf(:fechaReserva) >= 0 "),
        @Query(
                name = "findByFechaReserva", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.reservaVehiculo.ReservaVehiculo "
                        + "WHERE fechaReserva == :fechaReserva ")
})
//Se comenta de forma que permita realizar varias reservas en una misma fecha
//@Unique(name = "ReservaVehiculo_fechaReserva_UNQ", members = { "fechaReserva" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@lombok.Getter @lombok.Setter

/**
 * Esta clase define la entidad de dominio ReservaVehiculo
 * mediante la cual un usuario puede realizar la reserva de un
 * Vehiculo dada una fecha de Inicio
 * En la misma se definen todas sus propiedades y metodos.
 * Entre los cuales encontramos metodos Constructores,para eliminar un objeto
 * y metodos para modificar el estado de la reserva.
 *
 * @see vehiculo.Vehiculo
 * @see persona.Persona
 *
 * @author Cintia Millacura
 *
 */
public class ReservaVehiculo implements Comparable<ReservaVehiculo> {

    /**
     * Identificacion del nombre del icono que aparecera en la UI
     *
     * @return String
     */
    public String iconName() {
        return "Reserva";
    }

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private LocalDate fechaReserva;

    //region > compareTo, toString
    @Override
    public int compareTo(final ReservaVehiculo other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "fechaReserva");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "fechaReserva");
    }
    //endregion

}
