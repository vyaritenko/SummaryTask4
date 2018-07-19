package ua.nure.yaritenko.SummaryTask4.db.entity;

import java.io.Serializable;
/**
 * Root of all entities which have identifier field.
 *
 * @author V.Yaritenko
 *
 */
public abstract class Entity implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
