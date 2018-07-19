package ua.nure.yaritenko.SummaryTask4.tags.tld;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Map;

/**
 * Create own tag.
 *
 * @author V.Yaritenko
 */
public class CategoryTag extends TagSupport {
    private static final Logger LOG = Logger.getLogger(CategoryTag.class);
    private int id;

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    @Override
    public int doStartTag() throws JspException {
        LOG.debug("Method doStartTag() starts");
        HttpSession httpSession = pageContext.getSession();
        Map <Integer, Category> categoryMap = (Map <Integer, Category>) httpSession.getAttribute("categoryList");
        Category category = categoryMap.get(id);
        try {
            pageContext.getOut().write(category.getName());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.equals("Cannot create own tag " + e.getMessage());
        }
        LOG.debug("Method doStartTag() finished");
        return SKIP_BODY;
    }
}
