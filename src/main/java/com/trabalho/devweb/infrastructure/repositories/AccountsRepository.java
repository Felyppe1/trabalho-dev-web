// package com.trabalho.devweb.infrastructure.repositories;

// import com.trabalho.devweb.application.interfaces.IUsersRepository;
// import com.trabalho.devweb.domain.User;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;

// public class UsersRepository implements IUsersRepository {
//     private Connection connection;

//     public UsersRepository(Connection connection) {
//         this.connection = connection;
//     }

//     @Override
//     public void save(User user) throws SQLException {
//         String sql = "INSERT INTO usuarios (id, name, email) VALUES (?, ?, ?)";
//         try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//             stmt.setObject(1, user.getId()); // insere o UUID diretamente
//             stmt.setString(2, user.getName());
//             stmt.setString(3, user.getEmail());
//             stmt.executeUpdate();
//         }
//     }
// }