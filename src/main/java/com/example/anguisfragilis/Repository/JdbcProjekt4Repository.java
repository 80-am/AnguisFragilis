package com.example.anguisfragilis.Repository;

import com.example.anguisfragilis.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class JdbcProjekt4Repository implements Projekt4Repository {

    @Autowired
    private DataSource dataSource;

    @Override
    public void addUser(User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"UserID"})){
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
        }catch(SQLException e){
        }
    }

    @Override
    public User checkUser(User user){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Username, Password, UserID FROM Users WHERE Username = ?")){
            ps.setString(1, user.getUserName());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    if (rs.getString("Username").equals(user.getUserName()) && rs.getString("Password").equals(user.getPassword())) {
                        user.setUserId(rs.getInt("UserID"));
                        System.out.println("Success!");
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
