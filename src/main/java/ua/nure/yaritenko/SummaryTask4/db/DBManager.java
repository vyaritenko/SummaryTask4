package ua.nure.yaritenko.SummaryTask4.db;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.db.entity.Order;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.DBException;
import ua.nure.yaritenko.SummaryTask4.exception.Messages;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * DB manager. Works with MySQL DB. Only the required DAO methods are
 * defined!
 *
 * @author V.Yaritenko
 */

public final class DBManager {
    private static final Logger LOG = Logger.getLogger(DBManager.class);
    private static DBManager instance;

    private DBManager() {
        //no op
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    // //////////////////////////////////////////////////////////
    // SQL queries
    // //////////////////////////////////////////////////////////

    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String SQL_FIND_ALL_EDITION = "SELECT * FROM editions ORDER BY price";
    private static final String SQL_FIND_EDITION_BY_TOPIC = "SELECT * FROM editions e, categories c WHERE e.categories_id = c.id AND c.name = ?";
    private static final String SQL_FIND_EDITION_BY_SORTING_NAME_ASC = "SELECT * FROM editions ORDER BY name ASC";
    private static final String SQL_FIND_EDITION_BY_SORTING_NAME_DESC = "SELECT * FROM editions ORDER BY name DESC";
    private static final String SQL_FIND_EDITION_BY_SORTING_PRICE_ASC = "SELECT * FROM editions ORDER BY price ASC";
    private static final String SQL_FIND_EDITION_BY_SORTING_PRICE_DESC = "SELECT * FROM editions ORDER BY price DESC";
    private static final String SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_NAME_ASC = "SELECT * FROM editions e, categories c WHERE e.categories_id = c.id AND c.name = ? ORDER BY e.name ASC ";
    private static final String SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_NAME_DESC = "SELECT * FROM editions e, categories c WHERE e.categories_id = c.id AND c.name = ? ORDER BY e.name DESC ";
    private static final String SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_PRICE_ASC = "SELECT * FROM editions e, categories c WHERE e.categories_id = c.id AND c.name = ? ORDER BY e.price ASC ";
    private static final String SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_PRICE_DESC = "SELECT * FROM editions e, categories c WHERE e.categories_id = c.id AND c.name = ? ORDER BY e.price DESC ";
    private static final String SQL_FIND_EDITION_BY_SEARCH = "SELECT * FROM editions WHERE name LIKE ?";
    private static final String SQL_INSERT_USER = "INSERT INTO users(id, login, password, first_name, last_name, email, bill, roles_id, user_lock_id) VALUES(default, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_CATEGORY = "INSERT INTO categories(id, name) VALUES(default, ?)";
    private static final String SQL_INSERT_EDITION = "INSERT INTO editions(id, name, price, categories_id) VALUES(default, ?, ?, ?)";
    private static final String SQL_EXIST_CATEGORY = "SELECT * FROM categories WHERE name = ?";
    private static final String SQL_FIND_ALL_CATEGORIES = "SELECT * FROM categories";
    private static final String SQL_FIND_ID_CATEGORY = "SELECT id FROM categories WHERE name = ?";
    private static final String SQL_CHECK_LOGIN = "SELECT login FROM users WHERE login = ?";
    private static final String SQL_CREATE_ORDER = "INSERT INTO orders(id, bill, users_id, statuses_id) VALUES(default, ?, ?, ?)";
    private static final String SQL_INSERT_ORDER = "INSERT INTO orders_editions(orders_id, editions_id) VALUES(?, ?)";
    private static final String SQL_UPDATE_USER_BILL = "UPDATE users SET bill = ? WHERE id = ?";
    private static final String SQL_CHECK_SUBSCRIBE = "SELECT * FROM orders_editions WHERE editions_id = ? AND orders_id IN (SELECT id FROM orders WHERE users_id = ?)";
    private static final String SQL_FIND_ORDERS_BY_USER = "SELECT o.id, o.bill, o.users_id, o.statuses_id FROM orders o, users u WHERE o.users_id = u.id AND u.id = ?";
    private static final String SQL_FIND_EDITION_BY_ORDERS = "SELECT e.id, e.name, e.price, e.categories_id FROM editions e, orders_editions oe, orders o WHERE e.id = oe.editions_id AND oe.editions_id IN (SELECT oe.editions_id FROM orders_editions WHERE oe.orders_id = o.id AND o.id = ?)";
    private static final String SQL_UPDATE_USER_BY_LOGIN = "UPDATE users  SET password = ?, first_name = ?, last_name = ?, email = ? WHERE login = ?";
    private static final String SQL_FIND_ID_ORDER_BY_USER_AND_EDITION = "SELECT o.id, oe.editions_id FROM orders o JOIN orders_editions oe WHERE o.id= oe.orders_id AND o.users_id = ? AND oe.editions_id = ?";
    private static final String SQL_DELETE_SUBSCRIBE_BY_ORDER_AND_EDITION = "DELETE FROM orders_editions WHERE orders_id = ? AND editions_id = ?";
    private static final String SQL_DELETE_EDITION = "DELETE FROM editions WHERE id = ?";
    private static final String SQL_FIND_EDITION_BY_ID = "SELECT * FROM editions WHERE id = ?";
    private static final String SQL_UPDATE_EDITION = "UPDATE editions SET name = ?, price = ? WHERE id = ?";
    private static final String SQL_FIND_EXISTING_EDITION = "SELECT * FROM editions WHERE name = ? AND categories_id IN (SELECT categories_id FROM categories WHERE name = ?)";
    private static final String SQL_FIND_ALL_USER = "SELECT * FROM users ORDER BY login";
    private static final String SQL_LOCK_USER_BY_ID = "UPDATE users SET user_lock_id = 1 WHERE id = ?";
    private static final String SQL_UNLOCK_USER_BY_ID = "UPDATE users SET user_lock_id = 2 WHERE id = ?";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SQL_FIND_USER_BY_SEARCH = "SELECT * FROM users WHERE login LIKE ?";
    private static final String SQL_FIND_USERS_ID = "SELECT DISTINCT o.users_id  FROM orders o, orders_editions oe, editions e WHERE o.id = oe.orders_id AND oe.editions_id = e.id AND  e.id  IN (SELECT e.id FROM editions e WHERE e.price < 40)";

    /**
     * Returns a DB connection from the Pool Connections. Before using this
     * method you must configure the Date Source and the Connections Pool in
     * your %CATALINA_HOME%/context.xml file.
     *
     * @return DB connection.
     */
    public Connection getConnection() throws DBException {
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            // periodical - the name of data source
            DataSource datasource = (DataSource) initContext.lookup("java:comp/env/jdbc/periodical");
            LOG.trace("Data source ==> " + datasource);
            if (datasource != null) {
                con = datasource.getConnection();
            }
            return con;
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
        } catch (NamingException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
        }
    }
    /**
     * Find all categories.
     *
     * @return Map of all categories.
     * @throws DBException
     */
    public Map <Integer, Category> findCategories() throws DBException {
        Map <Integer, Category> categoriesMap = new TreeMap <>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        Category category;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_CATEGORIES);
            while (rs.next()) {
                category = extractCategory(rs);
                categoriesMap.put(category.getId(), category);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, ex);
        } finally {
            close(con, stmt, rs);
        }
        return categoriesMap;
    }
    /**
     * Find user with the given login.
     *
     * @param login User entity.
     * @return User entities.
     * @throws DBException
     */
    public User findUserByLogin(String login) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return user;
    }
    /**
     * Find user with the given search.
     *
     * @param search Field of search.
     * @return User entities.
     * @throws DBException
     */
    public User findUserBySearch(String search) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_SEARCH);
            pstmt.setString(1, search + '%');
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_SEARCH, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_SEARCH, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return user;
    }

    /**
     * Find user with the given user identifier.
     *
     * @param idUser User identifier.
     * @return User entities.
     * @throws DBException
     */
    public User findUserById(int idUser) throws DBException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
            pstmt.setInt(1, idUser);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return user;
    }

    /**
     * Lock user with the given user identifier .
     *
     * @param idUser User identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean lockUserById(int idUser) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_LOCK_USER_BY_ID);
            pstmt.setInt(1, idUser);
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_LOCK_USER_BY_ID, ex);
            throw new DBException(Messages.ERR_CANNOT_LOCK_USER_BY_ID, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Unlock user with the given user identifier .
     *
     * @param idUser User identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean unlockUserById(int idUser) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_UNLOCK_USER_BY_ID);
            pstmt.setInt(1, idUser);
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_UNLOCK_USER_BY_ID, ex);
            throw new DBException(Messages.ERR_CANNOT_UNLOCK_USER_BY_ID, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Find all users.
     *
     * @return List of users.
     * @throws DBException
     */
    public List <User> findAllUser() throws DBException {
        List <User> userList = new ArrayList <>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_USER);
            while (rs.next()) {
                userList.add(extractUser(rs));
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_USER, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_ALL_USER, ex);
        } finally {
            close(con, stmt, rs);
        }
        return userList;
    }

    /**
     * Find all edition.
     *
     * @return List of editions.
     * @throws DBException
     */
    public List <Edition> findAllEdition() throws DBException {
        List <Edition> editionList = new ArrayList <>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_EDITION);
            while (rs.next()) {
                editionList.add(extractEdition(rs));
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION, ex);
        } finally {
            close(con, stmt, rs);
        }
        return editionList;
    }

    /**
     * Find editions with the given sorting and topic.
     *
     * @param topic   Topic of edition.
     * @param sorting Field of sorting.
     * @return List of editions.
     * @throws DBException
     */
    public List <Edition> findEditionBySortingAndTopic(String topic, String sorting) throws DBException {
        List <Edition> editionList = new ArrayList <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            switch (sorting) {
                case "От дешовых":
                    pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_PRICE_ASC);
                    break;
                case "От догорих":
                    pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_PRICE_DESC);
                    break;
                case "По алфавиту":
                    pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_NAME_ASC);
                    break;
                case "Против алфавита":
                    pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_SORTING_AND_TOPIC_NAME_DESC);
                    break;
            }
            pstmt.setString(1, topic);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                editionList.add(extractEdition(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC_AND_SORTING, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC_AND_SORTING, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return editionList;
    }

    /**
     * Find editions with the given sorting.
     *
     * @param sorting Field of sorting.
     * @return List of editions.
     * @throws DBException
     */
    public List <Edition> findEditionBySorting(String sorting) throws DBException {
        List <Edition> editionList = new ArrayList <>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            switch (sorting) {
                case "От дешовых":
                    rs = stmt.executeQuery(SQL_FIND_EDITION_BY_SORTING_PRICE_ASC);
                    break;
                case "От догорих":
                    rs = stmt.executeQuery(SQL_FIND_EDITION_BY_SORTING_PRICE_DESC);
                    break;
                case "По алфавиту":
                    rs = stmt.executeQuery(SQL_FIND_EDITION_BY_SORTING_NAME_ASC);
                    break;
                case "Против алфавита":
                    rs = stmt.executeQuery(SQL_FIND_EDITION_BY_SORTING_NAME_DESC);
                    break;
            }
            while (rs.next()) {
                editionList.add(extractEdition(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_SORTING, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_SORTING, ex);
        } finally {
            close(con, stmt, rs);
        }
        return editionList;
    }

    /**
     * Find editions with the given topic.
     *
     * @param topic Topic of edition.
     * @return List of editions.
     * @throws DBException
     */
    public List <Edition> findEditionByTopic(String topic) throws DBException {
        List <Edition> editionList = new ArrayList <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_TOPIC);
            pstmt.setString(1, topic);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                editionList.add(extractEdition(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return editionList;
    }

    /**
     * Find editions with the given search.
     *
     * @param search Field of search.
     * @return List of editions.
     * @throws DBException
     */
    public List <Edition> findEditionBySearch(String search) throws DBException {
        List <Edition> editionList = new ArrayList <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_SEARCH);
            pstmt.setString(1, search + '%');
            rs = pstmt.executeQuery();
            while (rs.next()) {
                editionList.add(extractEdition(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_SEARCH, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_SEARCH, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return editionList;
    }

    /**
     * Insert edition.
     *
     * @param user User entities.
     * @return boolean.
     * @throws DBException
     */
    public boolean insertUser(User user) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_USER);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getEmail());
            pstmt.setInt(6, user.getBill());
            pstmt.setInt(7, user.getRoleId());
            pstmt.setInt(8, user.getUserLockId());
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CREATE_USER, ex);
            throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Insert edition.
     *
     * @param nameEdition Name of edition.
     * @param price       Price of edition.
     * @param idCategory  Category identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean insertEdition(String nameEdition, Integer price, Integer idCategory) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_EDITION);
            pstmt.setString(1, nameEdition);
            pstmt.setInt(2, price);
            pstmt.setInt(3, idCategory);
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CREATE_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_CREATE_EDITION, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Find identifier of category.
     *
     * @param category Name of category.
     * @return Category identifier.
     * @throws DBException
     */
    public Integer findIdCatagory(String category) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        Integer id = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ID_CATEGORY);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt(Fields.ENTITY_ID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ID_CATEGORY, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_ID_CATEGORY, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return id;
    }

    /**
     * Find existing category.
     *
     * @param category Name of category.
     * @return boolean.
     * @throws DBException
     */
    public boolean findExistingCategory(String category) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_EXIST_CATEGORY);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CREATE_CATEGORY, ex);
            throw new DBException(Messages.ERR_CANNOT_CREATE_CATEGORY, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return flag;
    }

    /**
     * Find existing edition.
     *
     * @param name     Name of edition.
     * @param category Name of category.
     * @return boolean.
     * @throws DBException
     */
    public boolean findExistingEdition(String name, String category) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_EXISTING_EDITION);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EXISTING_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EXISTING_EDITION, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return flag;


    }

    /**
     * Insert category.
     *
     * @param category Name of category.
     * @return boolean.
     * @throws DBException
     */
    public boolean insertCategory(String category) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_CATEGORY);
            pstmt.setString(1, category);
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CREATE_CATEGORY, ex);
            throw new DBException(Messages.ERR_CANNOT_CREATE_CATEGORY, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Cheack login on exists.
     *
     * @param login User login.
     * @return boolean.
     * @throws DBException
     */
    public boolean checkLogin(String login) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        boolean flag = true;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_CHECK_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                flag = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CHECK_LOGIN, ex);
            throw new DBException(Messages.ERR_CANNOT_CHECK_LOGIN, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return flag;
    }

    /**
     * Create a new order.
     *
     * @param orderPrice Order price.
     * @param user       User identifier.
     * @return Identifier of order.
     * @throws DBException
     */
    public int createOrder(int orderPrice, User user) throws DBException {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        int idOrder = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, orderPrice);
            pstmt.setInt(2, user.getId());
            pstmt.setInt(3, 2);
            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idOrder = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CREATE_ORDER, ex);
            throw new DBException(Messages.ERR_CANNOT_CREATE_ORDER, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return idOrder;
    }

    /**
     * Insert order.
     *
     * @param user           User identifier.
     * @param idOrder        Order identifier.
     * @param listIdEditions List of identifier of editions.
     * @param orderPrice     Order price.
     * @return boolean.
     * @throws DBException
     */
    public boolean insertOrder(User user, int idOrder, List <Integer> listIdEditions, int orderPrice) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        int newBill;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            pstmt = con.prepareStatement(SQL_INSERT_ORDER);
            Iterator <Integer> iterator = listIdEditions.iterator();
            while (iterator.hasNext()) {
                pstmt.setInt(1, idOrder);
                pstmt.setInt(2, iterator.next());
                pstmt.executeUpdate();
            }
            newBill = user.getBill() - orderPrice;
            if (updateUserBill(user, newBill)) {
                con.commit();
                flag = true;

            }
        } catch (SQLException ex) {
            rollback(con);
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_INSERT_ORDER, ex);
            throw new DBException(Messages.ERR_CANNOT_INSERT_ORDER, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Update user`s bill.
     *
     * @param user    User identifier.
     * @param newBill User`s a new bill.
     * @return boolean.
     * @throws DBException
     */
    public boolean updateUserBill(User user, int newBill) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_BILL);
            pstmt.setInt(1, newBill);
            pstmt.setInt(2, user.getId());
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_UPDATE_USER_BILL, ex);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_USER_BILL, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Check subscribes on exists.
     *
     * @param user           User identifier.
     * @param listIdEditions List identifier of editions.
     * @return boolean.
     * @throws DBException
     */
    public boolean checkSubscribe(User user, List <Integer> listIdEditions) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_CHECK_SUBSCRIBE);
            Iterator <Integer> iterator = listIdEditions.iterator();
            while (iterator.hasNext()) {
                pstmt.setInt(1, iterator.next());
                pstmt.setInt(2, user.getId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    flag = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_CHECK_SUBSCRIBE, ex);
            throw new DBException(Messages.ERR_CANNOT_CHECK_SUBSCRIBE, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return flag;
    }

    /**
     * Find orders with given user identifier.
     *
     * @param user User identifier.
     * @return List of order entities.
     * @throws DBException
     */
    public List <Order> findOrdersByUser(User user) throws DBException {
        List <Order> listOrders = new ArrayList <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        Order order;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ORDERS_BY_USER);
            pstmt.setInt(1, user.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                order = extractOrder(rs);
                listOrders.add(order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_USER, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_USER, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return listOrders;
    }

    /**
     * Find editions with given list of orders.
     *
     * @param listOrders Order entities.
     * @return List of edition entities.
     * @throws DBException
     */
    public List <Edition> findEditionByOrders(List <Order> listOrders) throws DBException {
        List <Edition> listEdition = new ArrayList <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            Iterator <Order> iterator = listOrders.iterator();
            pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_ORDERS);
            while (iterator.hasNext()) {
                pstmt.setInt(1, iterator.next().getId());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    listEdition.add(extractEdition(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_ORDERS, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_ORDERS, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return listEdition;
    }

    /**
     * Update user.
     *
     * @param user User identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean updateUserByLogin(User user) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_BY_LOGIN);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getLogin());
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_UPDATE_USER_BY_LOGIN, ex);
            throw new DBException(Messages.ERR_UPDATE_USER_BY_LOGIN, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Returns a map identifier of order with the given identifier of user and list of identifier of edition.
     *
     * @param idUser                  User identifier.
     * @param listIdSubdcribeEditions List identifier of edition
     * @return Map of identifier of orders.
     * @throws DBException
     */
    public Map <Integer, Integer> findIdOrderByUserAndEdition(int idUser, List <Integer> listIdSubdcribeEditions) throws DBException {
        Map <Integer, Integer> idOrderIdEdition = new TreeMap <>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        int idOrder;
        int idEdition;
        try {
            con = getConnection();
            Iterator <Integer> iterator = listIdSubdcribeEditions.iterator();
            pstmt = con.prepareStatement(SQL_FIND_ID_ORDER_BY_USER_AND_EDITION);
            while (iterator.hasNext()) {
                pstmt.setInt(1, idUser);
                pstmt.setInt(2, iterator.next());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    idOrder = rs.getInt(Fields.ENTITY_ID);
                    idEdition = rs.getInt(Fields.ORDERS_EDITIONS_EDITIONS_ID);
                    idOrderIdEdition.put(idEdition, idOrder);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ID_ORDER_BY_USER_AND_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_ID_ORDER_BY_USER_AND_EDITION, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return idOrderIdEdition;
    }

    /**
     * Delete subscribes.
     *
     * @param idOrderIdEdition Map of identifier of order and identifier of edition.
     * @return boolean.
     * @throws DBException
     */
    public boolean deleteSubscribe(Map <Integer, Integer> idOrderIdEdition) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        int statusUpdate = 0;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            pstmt = con.prepareStatement(SQL_DELETE_SUBSCRIBE_BY_ORDER_AND_EDITION);
            Set <Map.Entry <Integer, Integer>> entrySet = idOrderIdEdition.entrySet();
            Iterator <Map.Entry <Integer, Integer>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry <Integer, Integer> entry = iterator.next();
                pstmt.setInt(1, entry.getValue());
                pstmt.setInt(2, entry.getKey());
                statusUpdate = pstmt.executeUpdate();
            }
            if (statusUpdate > 0) {
                con.commit();
                flag = true;
            }
        } catch (SQLException ex) {
            rollback(con);
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_DELETE_SUBSCRIBE, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_SUBSCRIBE, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Delete edition.
     *
     * @param listIdEdition List identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean deleteEdition(List <Integer> listIdEdition) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        int idEdition;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            pstmt = con.prepareStatement(SQL_DELETE_EDITION);
            Iterator <Integer> iterator = listIdEdition.iterator();
            while (iterator.hasNext()) {
                idEdition = iterator.next();
                pstmt.setInt(1, idEdition);
                if (pstmt.executeUpdate() > 0) {
                    flag = true;
                }
            }
            if (flag) {
                con.commit();
            }
        } catch (SQLException ex) {
            rollback(con);
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_DELETE_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_EDITION, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    /**
     * Returns a editions with the given identifier.
     *
     * @param listIdEdition List identifier.
     * @return Map of identifiers and editions.
     * @throws DBException
     */
    public Map <Integer, Edition> findEditionById(List <Integer> listIdEdition) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        Map <Integer, Edition> editions = new TreeMap <>();
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_FIND_EDITION_BY_ID);
            Iterator <Integer> iterator = listIdEdition.iterator();
            while (iterator.hasNext()) {
                int idEdition = iterator.next();
                pstmt.setInt(1, idEdition);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    editions.put(idEdition, extractEdition(rs));
                }
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_ID, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_EDITION_BY_ID, ex);
        } finally {
            close(con, pstmt, rs);
        }
        return editions;
    }

    /**
     * Update edition.
     *
     * @param idEdition Edition identifier.
     * @return boolean.
     * @throws DBException
     */
    public boolean updateEdition(int idEdition, String name, int price) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean flag = false;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_EDITION);
            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, idEdition);
            if (pstmt.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_UPDATE_EDITION, ex);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_EDITION, ex);
        } finally {
            close(pstmt);
            close(con);
        }
        return flag;
    }

    public List<Integer> findUsersIdById() throws DBException {
        List <Integer> usersId = new ArrayList <>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_USERS_ID);
            while (rs.next()) {
                usersId.add(rs.getInt(Fields.ORDER_USER_ID));
            }
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_FIND_USERS_ID, ex);
            throw new DBException(Messages.ERR_CANNOT_FIND_USERS_ID, ex);
        } finally {
            close(con, stmt, rs);
        }
        return usersId;
    }

    // //////////////////////////////////////////////////////////
    // DB util methods
    // //////////////////////////////////////////////////////////

    /**
     * Closes a connection.
     *
     * @param con Connection to be closed.
     */
    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Closes a statement object.
     */
    private void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Closes a result set object.
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Closes resources.
     */
    private void close(Connection con, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(con);
    }

    /**
     * Rollbacks a connection.
     *
     * @param con Connection to be rollbacked.
     */
    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                LOG.error("Cannot rollback transaction", ex);
                ex.printStackTrace();
            }
        }
    }

    // //////////////////////////////////////////////////////////
    // Other methods
    // //////////////////////////////////////////////////////////


    /**
     * Extracts a user entity from the result set.
     *
     * @param rs Result set from which a user entity will be extracted.
     * @return User entity
     */

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(Fields.ENTITY_ID));
        user.setLogin(rs.getString(Fields.USER_LOGIN));
        user.setPassword(rs.getString(Fields.USER_PASSWORD));
        user.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
        user.setLastName(rs.getString(Fields.USER_LAST_NAME));
        user.setEmail(rs.getString(Fields.USER_EMAIL));
        user.setBill(rs.getInt(Fields.USER_BILL));
        user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
        user.setUserLockId(rs.getInt(Fields.USER_LOCK_ID));
        return user;
    }


    /**
     * Extracts a edition entity from the result set.
     *
     * @param rs Result set from which a edition entity will be extracted.
     * @return Edition entity
     */

    private Edition extractEdition(ResultSet rs) throws SQLException {
        Edition edition = new Edition();
        edition.setId(rs.getInt(Fields.ENTITY_ID));
        edition.setName(rs.getString(Fields.EDITION_NAME));
        edition.setPrice(rs.getInt(Fields.EDITION_PRICE));
        edition.setCategoryId(rs.getInt(Fields.EDITION_CATEGORY_ID));
        return edition;
    }

    /**
     * Extracts a category entity from the result set.
     *
     * @param rs Result set from which a category entity will be extracted.
     * @return Category entity
     */

    private Category extractCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt(Fields.ENTITY_ID));
        category.setName(rs.getString(Fields.CATEGORY_NAME));
        return category;
    }

    /**
     * Extracts a order entity from the result set.
     *
     * @param rs Result set from which a order entity will be extracted.
     * @return Order entity
     */

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt(Fields.ENTITY_ID));
        order.setBill(rs.getInt(Fields.ORDER_BILL));
        order.setUserId(rs.getInt(Fields.ORDER_USER_ID));
        order.setStatusId(rs.getInt(Fields.ORDER_STATUS_ID));
        return order;
    }
}
