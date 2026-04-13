package demo.meetingsmain.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class BaseEntityUUID {
    private String id;

    protected BaseEntityUUID() {}

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(name = "uuid-string", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", columnDefinition = "VARCHAR(36)")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
