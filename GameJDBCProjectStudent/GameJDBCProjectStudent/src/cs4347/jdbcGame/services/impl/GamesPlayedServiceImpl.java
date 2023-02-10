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

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

import javax.sql.DataSource;

import cs4347.jdbcGame.dao.GamesPlayedDAO;
import cs4347.jdbcGame.dao.impl.GamesPlayedDAOImpl;
import cs4347.jdbcGame.entity.GamesPlayed;
import cs4347.jdbcGame.services.GamesPlayedService;
import cs4347.jdbcGame.util.DAOException;

public class GamesPlayedServiceImpl implements GamesPlayedService
{
    private DataSource dataSource;

    public GamesPlayedServiceImpl(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public GamesPlayed create(GamesPlayed gamesPlayed) throws DAOException, SQLException
    {
    	if(gamesPlayed.getPlayerID() == null || gamesPlayed.getGameID() == null) {
    		throw new DAOException("GamesPlayed must have a player and game ID");
    	}
    	
    	GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
    	Connection con = dataSource.getConnection();
    	
    	try {
            con.setAutoCommit(false);
            GamesPlayed gp = gpDAO.create(con, gamesPlayed);
            con.commit();
            return gp;
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        }
        finally {
            if (con != null) {
                con.setAutoCommit(true);
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }

    @Override
    public GamesPlayed retrieveByID(long gamePlayedID) throws DAOException, SQLException
    {
    	Connection con = dataSource.getConnection();
        GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
		GamesPlayed gp = gpDAO.retrieveID(con, gamePlayedID);
        con.close();
        return gp;
    }

    @Override
    public List<GamesPlayed> retrieveByPlayerGameID(long playerID, long gameID) throws DAOException, SQLException
    {
    	Connection con = dataSource.getConnection();
        GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
		List<GamesPlayed> gp = gpDAO.retrieveByPlayerGameID(con, playerID, gameID);
        con.close();
        return gp;
    }

    @Override
    public List<GamesPlayed> retrieveByGame(long gameID) throws DAOException, SQLException
    {
    	Connection con = dataSource.getConnection();
        GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
		List<GamesPlayed> gp = gpDAO.retrieveByGame(con, gameID);
        con.close();
        return gp;
    }

    @Override
    public List<GamesPlayed> retrieveByPlayer(long playerID) throws DAOException, SQLException
    {
    	Connection con = dataSource.getConnection();
        GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
		List<GamesPlayed> gp = gpDAO.retrieveByPlayer(con, playerID);
        con.close();
        return gp;
    }

    @Override
    public int update(GamesPlayed gamesPlayed) throws DAOException, SQLException
    {
    	GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            int rowsAffected = gpDAO.update(con, gamesPlayed);
            con.commit();
            return rowsAffected;
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        }
        finally {
            if (con != null) {
                con.setAutoCommit(true);
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }

    @Override
    public int delete(long gamePlayedID) throws DAOException, SQLException
    {
    	 GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();

         Connection con = dataSource.getConnection();
         try {
             con.setAutoCommit(false);
             int rowsAffected = gpDAO.delete(con, gamePlayedID);
             con.commit();
             return rowsAffected;
         } catch (Exception ex) {
             con.rollback();
             throw ex;
         }
         finally {
             if (con != null) {
                 con.setAutoCommit(true);
             }
             if (con != null && !con.isClosed()) {
                 con.close();
             }
         }
    }

    @Override
    public int count() throws DAOException, SQLException
    {
    	GamesPlayedDAO gpDAO = new GamesPlayedDAOImpl();
    	Connection con = dataSource.getConnection();
    	int count = gpDAO.count(con);
    	return count;
    }

}
