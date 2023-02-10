/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */
package cs4347.jdbcGame.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcGame.dao.GameDAO;
import cs4347.jdbcGame.dao.impl.GameDAOImpl;
import cs4347.jdbcGame.entity.Game;
import cs4347.jdbcGame.services.GameService;
import cs4347.jdbcGame.util.DAOException;

public class GameServiceImpl implements GameService
{

    private DataSource dataSource;

    public GameServiceImpl(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public Game create(Game game) throws DAOException, SQLException
    {
    	if (game.getTitle() == null || game.getDescription() == null) {
            throw new DAOException("Game must have a title and description");
        }

        GameDAO gameDAO = new GameDAOImpl();

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            Game g1 = gameDAO.create(connection, game);
            connection.commit();
            return g1;
        } catch (Exception ex) {
            connection.rollback();
            throw ex;
        }
        finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public Game retrieve(long gameID) throws DAOException, SQLException
    {
        Connection connection = dataSource.getConnection();
        GameDAO gameDAO = new GameDAOImpl();
        Game game = gameDAO.retrieve(connection, gameID);
        connection.close();
        return game;
    }

    @Override
    public int update(Game game) throws DAOException, SQLException
    {
        GameDAO gameDAO = new GameDAOImpl();
        
        Connection connection = dataSource.getConnection();
        try {
        	connection.setAutoCommit(false);
        	int rowsAffected = gameDAO.update(connection, game);
        	connection.commit();
        	return rowsAffected;
        } catch (Exception ex) {
        	connection.rollback();
        	throw ex;
        }
        finally {
        	if (connection != null) {
        		connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
            	connection.close();
            }
        }        
    }

    @Override
    public int delete(long gameID) throws DAOException, SQLException
    {
        GameDAO gameDAO = new GameDAOImpl();
        
        Connection connection = dataSource.getConnection();
        try {
        	connection.setAutoCommit(false);
        	int rowsAffected = gameDAO.delete(connection, gameID);
        	connection.commit();
        	return rowsAffected;
        } catch (Exception ex) {
        	connection.rollback();
        	throw ex;
        }
        finally {
        	if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Override
    public int count() throws DAOException, SQLException
    {
        GameDAO gameDAO = new GameDAOImpl();
        Connection connection = dataSource.getConnection();
        int count = gameDAO.count(connection);
        return count;
    }

    @Override
    public List<Game> retrieveByTitle(String titlePattern) throws DAOException, SQLException
    {
    	Connection connection = dataSource.getConnection();
    	GameDAO gameDAO = new GameDAOImpl();
    	List<Game> game = gameDAO.retrieveByTitle(connection, titlePattern);
    	connection.close();
    	return game;
    }

    @Override
    public List<Game> retrieveByReleaseDate(Date start, Date end) throws DAOException, SQLException
    {
    	Connection connection = dataSource.getConnection();
    	GameDAO gameDAO = new GameDAOImpl();
    	List<Game> game = gameDAO.retrieveByReleaseDate(connection, start, end);
    	connection.close();
    	return game;
    }

}
