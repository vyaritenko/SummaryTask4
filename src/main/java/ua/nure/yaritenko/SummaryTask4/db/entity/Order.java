package ua.nure.yaritenko.SummaryTask4.db.entity;
/**
 * Order entity.
 *
 * @author V.Yaritenko
 *
 */
public class Order extends Entity {
    private Integer bill;
    private Integer userId;
    private Integer statusId;

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
        this.bill = bill;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "Order [id="+getId() + ", bill=" + bill + ", userId=" + userId + ", statusId="
                + statusId + "]";
    }
}
