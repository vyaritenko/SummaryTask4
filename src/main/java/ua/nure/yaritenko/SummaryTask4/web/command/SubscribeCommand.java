package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.Role;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SubscribeCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SubscribeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'subscribeCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the session attribute 'user' --> " + user);
        String forward = Path.PAGE_ERROR_PAGE;
        if (user == null) {
            forward = Path.OPEN_PAGE_LOGIN;
        } else {
            if (httpSession.getAttribute("subscribe") != null) {
                httpSession.removeAttribute("subscribe");
            }

            DBManager manager = DBManager.getInstance();
            Enumeration parameters = request.getParameterNames();
            int orderPrice = 0;
            List<Integer> listIdEditions = new ArrayList<>();
            Role userRole = (Role) httpSession.getAttribute("userRole");
            LOG.trace("Get the session attribute 'userRole' --> " + userRole);
            while (parameters.hasMoreElements()) {
                String parameter = parameters.nextElement().toString();
                LOG.trace("Request parameter: parameter --> " + parameter);
                if (parameter.equals("command")) {
                    continue;
                } else {
                    listIdEditions.add(Integer.parseInt(parameter));
                    orderPrice += Integer.parseInt(request.getParameter(parameter));
                }
            }
            LOG.trace("Price of order --> " + orderPrice);
            if (orderPrice > user.getBill()) {
                if (userRole == Role.CLIENT) {
                    request.setAttribute("backToUser", Path.PAGE_USER);
                }
                if (userRole == Role.ADMIN) {
                    request.setAttribute("backToUser", Path.PAGE_ADMIN);
                }
                throw new AppException("Not enough money in a personal account. Fund your account, please!");
            }
            if (manager.checkSubscribe(user, listIdEditions)) {
                if (userRole == Role.CLIENT) {
                    request.setAttribute("backToUser", Path.PAGE_USER);
                }
                if (userRole == Role.ADMIN) {
                    request.setAttribute("backToUser", Path.PAGE_ADMIN);
                }
                throw new AppException("Subscription to one of the selected edition(s) has already been completed!");
            }
            int idOrder = manager.createOrder(orderPrice, user);
            if (manager.insertOrder(user, idOrder, listIdEditions, orderPrice)) {
                LOG.trace("Order created for user " + user.getLogin() + " idOrder = " + idOrder + " listIdEdition " + listIdEditions + " price order " + orderPrice);
                if (userRole == Role.CLIENT) {
                    forward = Path.COMMAND_USER;
                }
                if (userRole == Role.ADMIN) {
                    forward = Path.COMMAND_ADMIN;
                }

            }
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'subscribeCommand' finished");
        return forward;
    }
}
