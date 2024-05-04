package org.example.capestone_group_02;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.capestone_group_02.gabes_code.DBAdapter;
import org.example.capestone_group_02.gabes_code.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@WebServlet (name = "loginServlet", value = "/login_servlet")
public class Login extends HttpServlet {

    DBAdapter db;
    ResultSet resultSet;
    RequestDispatcher rd;

    @Override
    public void init() throws ServletException {
        this.db = new DBAdapter();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String passwd = req.getParameter("passwd");

        this.resultSet = this.db.readTable("CALL User_Login(?,?)", username, passwd);

        try{
            // if all goes well here then a new user object will be passed to the user_page JSP file
            Object[] objects = sortUserCredentials(this.resultSet);

            int id = (int)objects[0];
            String user = (String)objects[1];
            String email = (String)objects[2];
            String pass = (String)objects[3];

            req.setAttribute("user", new User(id, user, email, pass));

            this.rd = req.getRequestDispatcher("user_page.jsp");
            this.rd.forward(req, resp);
        }catch (SQLException e){
            this.db.sqlErrorHandle(e);
            req.setAttribute("fail", "true");
            this.rd = req.getRequestDispatcher("login.jsp");
            this.rd.forward(req, resp);
        }
    }

    // this method will sort out the user credentials if a result was returned
    private Object[] sortUserCredentials(ResultSet resultSet)throws SQLException {
        Object[] objects = new Object[4];
        resultSet.next();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for(int i = 1; i <= metaData.getColumnCount(); i++){
            if(i == 1){
                objects[i-1] = resultSet.getInt(i);
            }else{
                objects[i-1] = resultSet.getString(i);
            }
        }
        return objects;
    }
}
