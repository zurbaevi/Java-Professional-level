package database;

import java.sql.*;

/**
 * Java Core. Professional level. Lesson 6
 *
 * @author Zurbaevi Nika
 */
public class SQLService {
    private static final String urlConnectionDatabase = "jdbc:sqlite:C:\\Users\\Zurbaevi Nika\\Desktop\\Java-Professional-level\\src\\ru\\geekbrains\\lesson6\\src\\main\\java\\database\\firstDatabase.db";
    private static Connection connection;
    private static SQLService instance;
    private static PreparedStatement psGetNick;
    private static PreparedStatement psReg;
    private static PreparedStatement psChangeNick;
    private static boolean isConnected;

    private SQLService() {
        try {
            connect();
            prepareAllStatements();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(urlConnectionDatabase);
            prepareAllStatements();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException {
        psGetNick = connection.prepareStatement("SELECT name FROM users WHERE name = ? AND password = ?;");
        psReg = connection.prepareStatement("INSERT INTO users (name, password) VALUES (?, ?);");
        psChangeNick = connection.prepareStatement("UPDATE users SET name = ? WHERE name = ?;");
    }

    public static String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psGetNick.setString(1, login);
            psGetNick.setString(2, password);
            ResultSet rs = psGetNick.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }

    public static boolean registration(String nickname, String password) {
        try {
            psReg.setString(1, nickname);
            psReg.setString(2, password);
            psReg.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changeNick(String oldNick, String newNick) {
        try {
            psChangeNick.setString(1, newNick);
            psChangeNick.setString(2, oldNick);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void disconnect() {
        try {
            psReg.close();
            psGetNick.close();
            psChangeNick.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLService getInstance() {
        if (instance == null) {
            synchronized (SQLService.class) {
                if (instance == null) {
                    instance = new SQLService();
                    isConnected = true;
                }
            }
        }
        return instance;
    }
}
