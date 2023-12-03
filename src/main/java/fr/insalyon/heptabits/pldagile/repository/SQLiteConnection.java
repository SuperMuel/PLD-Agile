package fr.insalyon.heptabits.pldagile.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    final private String databaseUrl;

    public SQLiteConnection(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public Connection connect() {
        try {
            // Chargement du pilote JDBC SQLite
            Class.forName("org.sqlite.JDBC");

            // Création de la connexion à la base de données
            return DriverManager.getConnection("jdbc:sqlite:" + databaseUrl);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        // Fermeture de la connexion à la base de données
        try {
            DriverManager.getConnection("jdbc:sqlite:" + databaseUrl).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
