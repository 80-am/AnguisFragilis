package com.example.anguisfragilis.Repository;

import com.example.anguisfragilis.Domain.HighScore;
import com.example.anguisfragilis.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class JdbcProjekt4Repository implements Projekt4Repository {

    @Autowired
    private DataSource dataSource;

    @Override
    public boolean addUser(User user){
        if(getUserByUserName(user)){
            return true;
        }else {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"UserID"})) {
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();
            } catch (SQLException e) {
            }
        }
        return false;
    }

    @Override
    public User checkForLogin(User user){
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT Username, Password, UserID FROM Users WHERE Username = ?")) {
                ps.setString(1, user.getUserName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("Password").equals(user.getPassword())) {
                            user.setUserId(rs.getInt("UserID"));
                        } else {
                        }
                    }
                } catch (SQLException e) {
                    throw new Projekt4RepositoryException("Wrong in if-sats" + " SQLmessage: " + e.getMessage());
                }
            } catch (SQLException e) {
                throw new Projekt4RepositoryException("Connection failed!");
            }
        return user;
    }

    public boolean getUserByUserName (User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Username, Password, UserID FROM Users WHERE Username = ?")){
            ps.setString(1,user.getUserName());
           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                return true;
               }
           }catch(SQLException e){
           }
        }catch(SQLException e){
                throw new Projekt4RepositoryException("Connection in getUserByUserName failed!");
        }
        return false;
    }

    @Override
    public void addScore(int score, User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Score(Score, UserID, EntryDate) VALUES(?,?, ?)", new String[]{"ScoreID"})){
            ps.setInt(1, score);
            ps.setInt(2, user.getUserId());
            ps.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<HighScore> getHighScores(){
        List<HighScore> highScores = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 10 Score, Username FROM Users JOIN Score ON Users.UserID = Score.UserID  ORDER BY Score DESC, Username")) {
            while(rs.next()){
                highScores.add(rsHighScore(rs));
            }
        }catch(SQLException e) {
            throw new Projekt4RepositoryException("Connection failed!");
        }
        return highScores;
    }
    private HighScore rsHighScore(ResultSet rs) throws SQLException {
        return new HighScore(rs.getInt("Score"), rs.getString("Username"));
    }

    @Override
    public int getUserHighScore(User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT TOP 1 Score FROM Score WHERE UserID=? ORDER BY Score DESC")) {
            ps.setInt(1,user.getUserId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int userHighScore = rs.getInt("Score");
                return userHighScore;
            }
        }catch(SQLException e) {
            throw new Projekt4RepositoryException("Connection failed!");
        }
        return 0;
    }

}
