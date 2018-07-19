package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.db.entity.Order;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AccountCommand extends Command {
    private static final Logger LOG = Logger.getLogger(AccountCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'accountCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        String forward;
        if (user == null) {
            forward = Path.PAGE_ACCOUNT;
        } else {
            LOG.trace("Get the session attribute user --> " + user);
            DBManager manager = DBManager.getInstance();
            List<Order> listOrders = manager.findOrdersByUser(user);
            LOG.trace("List of orders for user " + user.getLogin() + " --> " + listOrders);
            List<Edition> listSubscribeEdition = null;
            if (listOrders != null) {
                listSubscribeEdition = manager.findEditionByOrders(listOrders);
            }
            httpSession.setAttribute("listSubscribeEdition", listSubscribeEdition);
            forward = Path.PAGE_ACCOUNT;
            LOG.trace("Set the session attribute listSubscribeEdition -->" + listSubscribeEdition);
            LOG.info("User " + user.getLogin() + "have such subscribs --> " + listSubscribeEdition);
            LOG.debug("Command 'accountCommand' finished");
        }
        return forward;
    }
}
