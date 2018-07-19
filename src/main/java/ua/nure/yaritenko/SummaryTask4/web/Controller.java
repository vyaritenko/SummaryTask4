package ua.nure.yaritenko.SummaryTask4.web;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;
import ua.nure.yaritenko.SummaryTask4.web.command.Command;
import ua.nure.yaritenko.SummaryTask4.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Main servlet controller.
 *
 * @author V.Yaritenko
 *
 */
public class Controller extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Controller.class);
    private String forward;
    private boolean flagQuery;
    private boolean flagResponse;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!flagQuery) {
            process(request, response);
        }
        flagQuery = false;
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
        if (!flagResponse) {
            flagQuery = true;
            response.sendRedirect(forward);
        }
    }
    /**
     * Main method of this controller.
     */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("Controller starts");
        // extract command name from the request
        String commandName = request.getParameter("command");
        LOG.trace("Request parameter: command --> " + commandName);
        LOG.trace("Request parameter: command --> " + "commandName");
        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        LOG.trace("Obtained command --> " + command);
        // execute command and get forward address
        forward = Path.PAGE_ERROR_PAGE;
        try {
            forward = command.execute(request, response);
            flagResponse = false;
        } catch (AppException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            flagResponse = true;
            request.getRequestDispatcher(forward).forward(request, response);
        }
        LOG.trace("Forward address --> " + forward);

        LOG.debug("Controller finished, now go to forward address --> " + forward);
    }
}
