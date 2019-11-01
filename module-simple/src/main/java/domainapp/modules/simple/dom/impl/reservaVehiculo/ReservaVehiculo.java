package domainapp.modules.simple.dom.impl.reservaVehiculo;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
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
        schema = "reservaVehiculo",
        table = "ReservaVehiculo"
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
@Unique(name = "ReservaVehiculo_fechaReserva_UNQ", members = { "fechaReserva" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class ReservaVehiculo implements Comparable<ReservaVehiculo> {

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
