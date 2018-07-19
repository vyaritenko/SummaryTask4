package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
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

public class DeleteCommand extends Command {
    private static final Logger LOG = Logger.getLogger(DeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'deleteCommand' starts");
        DBManager manager = DBManager.getInstance();
        HttpSession httpSession = request.getSession(false);
        User user = (User)httpSession.getAttribute("user");
        LOG.trace("Get the session attribute 'user' --> " + user);
        String forward = Path.PAGE_ERROR_PAGE;
        if (user == null) {
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            Enumeration parameters = request.getParameterNames();
            List<Integer> listIdEdition = new ArrayList<>();
            while (parameters.hasMoreElements()) {
                String parameter = parameters.nextElement().toString();
                LOG.trace("Request parameter: parameter --> " + parameter);
                if (parameter.equals("command")) {
                    continue;
                } else {
                    listIdEdition.add(Integer.parseInt(parameter));
                }
            }
            LOG.trace("List idEdition for deleting");
            if (manager.deleteEdition(listIdEdition)) {
                forward = Path.COMMAND_ADMIN;
                LOG.info("Editions on such id --> " + listIdEdition + " have been removed");
            }
        }
        LOG.debug("Command 'deleteCommand' finished");
        return forward;
    }
}
