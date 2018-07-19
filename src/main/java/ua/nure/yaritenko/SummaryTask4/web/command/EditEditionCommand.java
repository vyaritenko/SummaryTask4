package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class EditEditionCommand extends Command {
    private static final Logger LOG = Logger.getLogger(EditEditionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'editEditionCommand' starts");
        HttpSession httpSession = request.getSession();
        String forward;
        if (httpSession.getAttribute("user") == null) {
            forward = Path.OPEN_PAGE_LOGIN;
            LOG.trace("Go to on page 'login'");
        } else if (httpSession.getAttribute("repeat") != null) {
            httpSession.removeAttribute("repeat");
            forward = Path.OPEN_PAGE_EDIT_EDITION;
            LOG.trace("Go to on page 'editEdition'");
        } else {
            DBManager manager = DBManager.getInstance();
            List <Integer> idEditEdition = new ArrayList <>();
            Enumeration parameters = request.getParameterNames();
            String parameter;
            while (parameters.hasMoreElements()) {
                parameter = parameters.nextElement().toString();
                LOG.trace("Request parameter: parameter --> " + parameter);
                if (parameter.equals("command")) {
                    continue;
                } else {
                    idEditEdition.add(Integer.parseInt(parameter));
                }
            }
            LOG.trace("List idEdition for editing");
            if (idEditEdition.isEmpty()) {
                throw new AppException("Нou have not selected any editions to edit");
            }
            Map <Integer, Edition> editions = manager.findEditionById(idEditEdition);
            httpSession.setAttribute("editions", editions);
            LOG.trace("Set the session attribute: editions --> " + editions);
            forward = Path.OPEN_PAGE_EDIT_EDITION;
            LOG.info("Editions for editing --> " + editions);
        }
        LOG.debug("Command 'editEditionCommand' finished");
        return forward;
    }
}
