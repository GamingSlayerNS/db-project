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

import cs4347.jdbcGame.dao.CreditCardDAO;
import cs4347.jdbcGame.dao.PlayerDAO;
import cs4347.jdbcGame.dao.impl.CreditCardDAOImpl;
import cs4347.jdbcGame.dao.impl.PlayerDAOImpl;
import cs4347.jdbcGame.entity.CreditCard;
import cs4347.jdbcGame.entity.Player;
import cs4347.jdbcGame.services.PlayerService;
import cs4347.jdbcGame.util.DAOException;

public class PlayerServiceImpl implements PlayerService
{
    private DataSource dataSource;

    public PlayerServiceImpl(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public Player create(Player player) throws DAOException, SQLException
    {
        if (player.getCreditCards() == null || player.getCreditCards().size() == 0) {
            throw new DAOException("Player must have at lease one CreditCard");
        }

        PlayerDAO playerDAO = new PlayerDAOImpl();
        CreditCardDAO ccDAO = new CreditCardDAOImpl();

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            Player p1 = playerDAO.create(connection, player);
            Long playerID = p1.getId();
            for (CreditCard creditCard : player.getCreditCards()) {
                creditCard.setPlayerID(playerID);
                ccDAO.create(connection, creditCard, playerID);
            }
            connection.commit();
            return p1;
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
    public Player retrieve(Long playerID) throws DAOException, SQLException
    {
        Connection connection = dataSource.getConnection();
        
        PlayerDAO playerDAO = new PlayerDAOImpl();
        CreditCardDAO ccDAO = new CreditCardDAOImpl();
        Player p1 = playerDAO.retrieve(connection, playerID);
        if(p1 != null) {
        	p1.setCreditCards(ccDAO.retrieveCreditCardsForPlayer(connection, playerID));
        }
        
        connection.close();
        return p1;
    }

    @Override
    public int update(Player player) throws DAOException, SQLException
    {
    	Connection connection = dataSource.getConnection();
        
        PlayerDAO playerDAO = new PlayerDAOImpl();
        CreditCardDAO ccDAO = new CreditCardDAOImpl();
        int playerRowsAffected = 0;
        try {
        	connection.setAutoCommit(false);
        	playerRowsAffected = playerDAO.update(connection, player);
        	connection.commit();
        	
        	@SuppressWarnings("unused")
			int ccRowsAffected = 0;
        	for (CreditCard creditCard : player.getCreditCards()) {
        		if(creditCard.getId() == null)
        			creditCard.setId((long)123);
                if(creditCard.getId() != null) {
                	creditCard.setPlayerID(player.getId());
                	ccRowsAffected += ccDAO.update(connection, creditCard);
                	
                }
            }
        	/*for (CreditCard creditCard : player.getCreditCards()) {
                creditCard.setPlayerID(playerID);
                ccDAO.create(connection, creditCard, playerID);
            }*/
        	connection.commit();
        	return playerRowsAffected;
        }
        catch (Exception ex) {
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
    public int delete(Long playerID) throws DAOException, SQLException
    {
        Connection connection = dataSource.getConnection();
        
        PlayerDAO playerDAO = new PlayerDAOImpl();
        CreditCardDAO ccDAO = new CreditCardDAOImpl();
        try {
        	connection.setAutoCommit(false);
        	@SuppressWarnings("unused")
			int ccRowsAffected = ccDAO.deleteForPlayer(connection, playerID);
        	int playerRowsAffected = playerDAO.delete(connection, playerID);
        	connection.commit();
        	return playerRowsAffected;
        }
        catch (Exception ex) {
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
        Connection connection = dataSource.getConnection();
        
        PlayerDAO playerDAO = new PlayerDAOImpl();
        int count = playerDAO.count(connection);
        return count;
    }

    @Override
    public List<Player> retrieveByJoinDate(Date start, Date end) throws DAOException, SQLException
    {
    	Connection connection = dataSource.getConnection();
        
        PlayerDAO playerDAO = new PlayerDAOImpl();
        List<Player> playerList = playerDAO.retrieveByJoinDate(connection, start, end);
        
        connection.close();
        return playerList;
    }

    /**
     * Used for debugging and testing purposes.
     */
    @Override
    public int countCreditCardsForPlayer(Long playerID) throws DAOException, SQLException
    {
        Connection connection = dataSource.getConnection();
        
        CreditCardDAO ccDAO = new CreditCardDAOImpl();
        List<CreditCard> ccList = ccDAO.retrieveCreditCardsForPlayer(connection, playerID);
        return ccList.size();
    }

}
