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

public class AddEditionCommand extends Command {
    private static final Logger LOG = Logger.getLogger(AddEditionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'addEditionCommand' starts");
       HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        String forward = null;
        System.out.println("user" + user);
        if(user == null){
            httpSession.setAttribute("user", user);
            forward = Path.PAGE_ACCOUNT;
        }else  {
            DBManager manager = DBManager.getInstance();
            String nameEdition = request.getParameter("name");
            LOG.trace("Request parameter: nameEdition --> " + nameEdition);
            String price = request.getParameter("price");
            LOG.trace("Request parameter: price --> " + price);
            String category = request.getParameter("category");
            LOG.trace("Request parameter: category --> " + category);

            if ((nameEdition == null) && (price == null) && (category == null)) {
                LOG.trace("Go to on page 'add edition'");
                forward = Path.OPEN_PAGE_ADD_EDITION;
            } else {
                if (manager.findExistingEdition(nameEdition, category)) {
                    throw new AppException("This edition have already existed!");
                }

                if (!manager.findExistingCategory(category)) {
                    LOG.trace("Adding new category");
                    manager.insertCategory(category);
                    LOG.trace("Category is added");
                }
                Integer idCategory = manager.findIdCatagory(category);
                LOG.trace("idCategory --> " + idCategory);
                if (manager.insertEdition(nameEdition, Integer.parseInt(price), idCategory)) {
                    LOG.info("Edition with name " + nameEdition + ", price " + price + " and category " + idCategory + " is added");
                    forward = Path.COMMAND_ADD_EDITION;
                }
            }
            LOG.debug("Command 'addEdition' finished");
        }
        return forward;
    }
}
