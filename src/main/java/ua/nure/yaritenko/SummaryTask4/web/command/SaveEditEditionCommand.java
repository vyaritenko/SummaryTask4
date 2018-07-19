package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import sun.security.krb5.internal.APOptions;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaveEditEditionCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SaveEditEditionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'saveEditEditionCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        String forward = null;
        if (user == null) {
            forward = Path.OPEN_PAGE_LOGIN;
        } else {
            DBManager manager = DBManager.getInstance();
            String name = request.getParameter("name");
            LOG.trace("Request parameter: name --> " + name);
            int price = Integer.parseInt(request.getParameter("price"));
            LOG.trace("Request parameter: price --> " + price);
            if (request.getParameter("id") == null) {
                throw new AppException("You have not selected any editions");
            }
            int idEdition = Integer.parseInt(request.getParameter("id"));
            LOG.trace("Request parameter: id --> " + idEdition);
            Map<Integer, Edition> editions = (Map<Integer, Edition>) httpSession.getAttribute("editions");
            LOG.trace("Get the session attribute 'user' --> " + editions);
            Edition edition = editions.get(idEdition);
            LOG.trace("Edition --> " + edition);
            if (price == 0) {
                price = edition.getPrice();
            }
            if ((name == null) || (name.isEmpty())) {
                name = edition.getName();
            }
            if (manager.updateEdition(idEdition, name, price)) {
                LOG.trace("Edition with id " + idEdition + " name " + name + " price " + price + " updated");
                editions.remove(idEdition);
                LOG.trace("Edition with id " + idEdition + " deleted from list 'editions'");
                if (editions.isEmpty()) {
                    httpSession.removeAttribute("editions");
                    LOG.trace("Remove the session attribute 'editions'");
                    forward = Path.COMMAND_ADMIN;
                } else {
                    httpSession.setAttribute("editions", editions);
                    LOG.trace("Set the session attribute: editions --> " + editions);
                    httpSession.setAttribute("repeat", 1);
                    LOG.trace("Set the session attribute: repeat --> " + 1);
                    forward = Path.COMMAND_EDIT_EDITION;
                }
            }
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'saveEditEditionCommand' finished");
        return forward;
    }
}
