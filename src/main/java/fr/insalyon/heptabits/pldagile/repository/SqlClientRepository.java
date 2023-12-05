package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Client;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlClientRepository implements ClientRepository {

    final private SQLiteConnection connection;
    public SqlClientRepository(SQLiteConnection connection) {
        this.connection = connection;
    }

    @Override
    public Client create(String firstName, String lastName, String phoneNumber) {
        try {
            // Connexion à la base de données
            try (Connection dbConnection = connection.connect()) {

                // Insertion de données
                String insertQuery = "INSERT INTO clients (firstName, lastName, phoneNumber) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, phoneNumber);
                    preparedStatement.executeUpdate();

                    // Récupération de l'ID du dernier enregistrement inséré
                    String selectLastInsertIdQuery = "SELECT last_insert_rowid() AS id";
                    try (PreparedStatement selectStatement = dbConnection.prepareStatement(selectLastInsertIdQuery);
                         ResultSet resultSet = selectStatement.executeQuery()) {

                        if (resultSet.next()) {
                            long clientId = resultSet.getLong("id");

                            // Création et retour de l'objet Client
                            System.out.println("Client bien créé avec l'ID : " + clientId);
                            return new Client(clientId, firstName, lastName, phoneNumber);
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
    public Client findById(long id) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Sélection d'un client par ID
            String selectQuery = "SELECT * FROM clients WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Client trouvé, création de l'objet Client
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String phoneNumber = resultSet.getString("phoneNumber");

                        return new Client(id, firstName, lastName, phoneNumber);
                    } else {
                        // Aucun client trouvé avec cet ID
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
    public List<Client> findByName(String firstName, String lastName) {
        List<Client> clients = new ArrayList<>();

        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Construction de la requête SQL
            String selectQuery;
            List<Object> parameters = new ArrayList<>();

            if (firstName != null && lastName != null) {
                selectQuery = "SELECT * FROM clients WHERE firstName LIKE ? AND lastName LIKE ?";
                parameters.add("%" + firstName + "%");
                parameters.add("%" + lastName + "%");
            } else if (firstName != null) {
                selectQuery = "SELECT * FROM clients WHERE firstName LIKE ?";
                parameters.add("%" + firstName + "%");
            } else if (lastName != null) {
                selectQuery = "SELECT * FROM clients WHERE lastName LIKE ?";
                parameters.add("%" + lastName + "%");
            } else {
                // Aucun des deux n'est spécifié, renvoyer une liste vide ou tous les clients selon votre logique
                return clients;
            }

            // Préparation de la requête
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery)) {
                // Définir les paramètres de la requête
                for (int i = 0; i < parameters.size(); i++) {
                    preparedStatement.setObject(i + 1, parameters.get(i));
                }

                // Exécution de la requête
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String clientFirstName = resultSet.getString("firstName");
                        String clientLastName = resultSet.getString("lastName");
                        String phoneNumber = resultSet.getString("phoneNumber");

                        // Création de l'objet Client et ajout à la liste
                        clients.add(new Client(id, clientFirstName, clientLastName, phoneNumber));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();

        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Sélection de toutes les lignes dans la table clients
            String selectQuery = "SELECT * FROM clients";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String phoneNumber = resultSet.getString("phoneNumber");

                    // Création de l'objet Client et ajout à la liste
                    clients.add(new Client(id, firstName, lastName, phoneNumber));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public Client update(Client client) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Exemple de mise à jour des données d'un client
            String updateQuery = "UPDATE clients SET firstName = ?, lastName = ?, phoneNumber = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, client.getFirstName());
                preparedStatement.setString(2, client.getLastName());
                preparedStatement.setString(3, client.getPhoneNumber());
                preparedStatement.setLong(4, client.getId());

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    // La mise à jour a réussi, retournez le client mis à jour
                    return client;
                } else {
                    // Aucune ligne mise à jour, le client n'a peut-être pas été trouvé dans la base de données
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Client client) {
        try {
            // Connexion à la base de données
            Connection dbConnection = connection.connect();

            // Suppression d'un client
            String deleteQuery = "DELETE FROM clients WHERE id = ?";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteQuery)) {
                preparedStatement.setLong(1, client.getId());

                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    // La suppression a réussi
                    System.out.println("Client supprimé avec succès.");
                } else {
                    // Aucune ligne supprimée, le client n'a peut-être pas été trouvé dans la base de données
                    System.out.println("Aucun client trouvé avec l'ID " + client.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            connection.close();
        }
    }
}
