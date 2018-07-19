package ua.nure.yaritenko.SummaryTask4.db.entity;
/**
 * Category entity.
 *
 * @author V.Yaritenko
 *
 */
public class Category extends Entity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category [name=" + name + ", Id=" + getId() + "]";
    }

}
