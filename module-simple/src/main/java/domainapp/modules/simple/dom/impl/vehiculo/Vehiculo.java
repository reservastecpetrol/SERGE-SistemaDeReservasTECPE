package domainapp.modules.simple.dom.impl.vehiculo;

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
        schema = "vehiculo",
        table = "Vehiculo"
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
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "),
        @Query(
                name = "findByMatriculaContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "
                        + "WHERE matricula.indexOf(:matricula) >= 0 "),
        @Query(
                name = "findByMatricula", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.modules.simple.dom.impl.vehiculo.Vehiculo "
                        + "WHERE matricula == :matricula ")
})
@Unique(name = "Vehiculo_matricula_UNQ", members = { "matricula" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Vehiculo implements Comparable<Vehiculo> {

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String matricula;

    //region > compareTo, toString
    @Override
    public int compareTo(final Vehiculo other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "matricula");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "matricula");
    }
    //endregion

}
