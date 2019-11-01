package domainapp.modules.simple.dom.impl.habitacion;

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

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "habitacion",
        table = "Habitacion"
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
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "),
        @Query(
                name = "findByNombreContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "
                        + "WHERE nombre.indexOf(:nombre) >= 0 "),
        @Query(
                name = "findByNombre", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.habitacion.Habitacion "
                        + "WHERE nombre == :nombre ")
})
@Unique(name = "Habitacion_nombre_UNQ", members = { "nombre" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Habitacion implements Comparable<Habitacion> {

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String nombre;

    //region > compareTo, toString
    @Override
    public int compareTo(final Habitacion other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "nombre");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "nombre");
    }
    //endregion

}
