package ua.nure.yaritenko.SummaryTask4.db;

import ua.nure.yaritenko.SummaryTask4.db.entity.Order;
/**
 * Status entity.
 *
 * @author V.Yaritenko
 *
 */
public enum Status {
    UNPAID, PAID;
    public static Status getStatus(Order order) {
        int statusId = order.getStatusId();
        return Status.values()[statusId - 1];
    }
    public String getName() {
        return name().toLowerCase();
    }
}
