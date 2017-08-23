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
    public String addUser(User user){
        user = addUserIdIfUserExists(user);
        if(user.getUserId() != 0){
            return "User already exists!";
        }else {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"UserID"})) {
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();
            } catch (SQLException e) {
            }
        }
        return "Username added";
    }

    @Override
    public User addUserIdIfUserExists(User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Username, Password, UserID FROM Users WHERE Username = ?")){
            ps.setString(1, user.getUserName());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    if (rs.getString("Username").equals(user.getUserName()) && rs.getString("Password").equals(user.getPassword())) {
                        user.setUserId(rs.getInt("UserID"));
                    }
                }
            }catch(SQLException e){
                throw new Projekt4RepositoryException("Wrong in if-sats" + " SQLmessage: " + e.getMessage());
            }
        }catch(SQLException e) {
           throw new Projekt4RepositoryException("Connection failed!");
        }
        return user;
    }

    @Override
    public void addScore(int score, User user){
        System.out.println("Repo addScore: " + score + " User: " + user.getUserName() + " ID:" + user.getUserId());
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

    /*public void getUser(int userId){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"id"})){
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.executeUpdate();
        }catch(SQLException e){
        }
    }
    public void addScore(String userName, String password){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"id"})){
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.executeUpdate();
        }catch(SQLException e){
        }
    }*/

}
