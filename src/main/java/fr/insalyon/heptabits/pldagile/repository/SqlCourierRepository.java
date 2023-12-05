package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlCourierRepository implements CourierRepository {

    final private SQLiteConnection connection;

    public SqlCourierRepository(SQLiteConnection connection) {
        this.connection = connection;
    }
    @Override
    public Courier create(String firstName, String lastName, String email, String phoneNumber) {
        try {
            // Connexion à la base de données
            try (Connection dbConnection = connection.connect()) {

                // Insertion de données
                String insertQuery = "INSERT INTO couriers (firstName, lastName, phoneNumber, email) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, phoneNumber);
                    preparedStatement.setString(4, email);
                    preparedStatement.executeUpdate();

                    // Récupération de l'ID du dernier enregistrement inséré
                    String selectLastInsertIdQuery = "SELECT last_insert_rowid() AS id";
                    try (PreparedStatement selectStatement = dbConnection.prepareStatement(selectLastInsertIdQuery);
                         ResultSet resultSet = selectStatement.executeQuery()) {

                        if (resultSet.next()) {
                            long courierId = resultSet.getLong("id");

                            // Création et retour de l'objet Client
                            System.out.println("Client bien créé avec l'ID : " + courierId);
                            return new Courier(courierId, firstName, lastName, email, phoneNumber);
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Courier findById(Long id) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Sélection d'un livreur par ID
            String selectQuery = "SELECT * FROM couriers WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Livreur trouvé, création de l'objet Livreur
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String email = resultSet.getString("email");
                        String phoneNumber = resultSet.getString("phoneNumber");

                        return new Courier(id, firstName, lastName, email, phoneNumber);
                    } else {
                        // Aucun livreur trouvé avec cet ID
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Courier> findAll() {
        List<Courier> couriers = new ArrayList<>();

        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Sélection de toutes les lignes dans la table livreurs
            String selectQuery = "SELECT * FROM couriers";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phoneNumber");

                    // Création de l'objet Livreur et ajout à la liste
                    couriers.add(new Courier(id, firstName, lastName, email, phoneNumber));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return couriers;
    }

    @Override
    public Courier update(Courier courier) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Exemple de mise à jour des données d'un livreur
            String updateQuery = "UPDATE couriers SET firstName = ?, lastName = ?, email = ?, phoneNumber = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, courier.getFirstName());
                preparedStatement.setString(2, courier.getLastName());
                preparedStatement.setString(3, courier.getPhoneNumber());
                preparedStatement.setString(4, courier.getEmail());
                preparedStatement.setLong(5, courier.getId());

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    // La mise à jour a réussi, retournez le livreur mis à jour
                    return courier;
                } else {
                    // Aucune ligne mise à jour, le livreur n'a peut-être pas été trouvé dans la base de données
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Suppression d'un livreur
            String deleteQuery = "DELETE FROM couriers WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteQuery)) {
                preparedStatement.setLong(1, id);

                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    // La suppression a réussi
                    System.out.println("Livreur supprimé avec succès.");
                } else {
                    // Aucune ligne supprimée, le livreur n'a peut-être pas été trouvé dans la base de données
                    System.out.println("Aucun livreur trouvé avec l'ID " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
