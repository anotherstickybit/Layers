package tech.itpark.repository;

import lombok.RequiredArgsConstructor;
import tech.itpark.entity.UserEntity;
import tech.itpark.exception.DataAccessException;
import tech.itpark.jdbc.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@RequiredArgsConstructor
public class UserRepositoryJDBCImpl implements UserRepository {
    private final DataSource ds;
    private final RowMapper<UserEntity> mapper = rs -> {
        try {
            return new UserEntity(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("secret"),
                    Set.of((String[]) rs.getArray("roles").getArray()),
                    rs.getBoolean("removed"),
                    rs.getDate("created").toLocalDate()
            );
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    };

    @Override
    public List<UserEntity> findAll() {
        try (
                final Connection connection = ds.getConnection();
                final Statement stmt = connection.createStatement();
                final ResultSet rs = stmt.executeQuery(
                        "select id, login, password, name, secret, roles," +
                                " extract(epoch from created), removed from users order by id"
                );
        ) {
            List<UserEntity> result = new LinkedList<>();
            while (rs.next()) {
                final UserEntity entity = mapper.map(rs);
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long aLong) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt = connection.prepareStatement("select id, " +
                        "login, password, name, secret, roles, extract(epoch from created), removed " +
                        "from users where id = ?");
        ) {
            stmt.setLong(1, aLong);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.of(null);
    }


    @Override
    public UserEntity save(UserEntity entity) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt = connection.prepareStatement("insert into users " +
                        "(login, password, name, secret, roles, removed, created) values (?, ?, ?, ?, ?, ?, ?)")
        ) {
            Array array = connection.createArrayOf("TEXT", entity.getRoles().toArray());
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getName());
            stmt.setString(4, entity.getSecret());
            stmt.setArray(5, array);
            stmt.setBoolean(6, entity.isRemoved());
            stmt.setDate(7, Date.valueOf(entity.getCreated()));

            stmt.execute();

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return entity;
    }

    @Override
    public boolean removeById(Long aLong) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt =
                        connection.prepareStatement("update users set removed = 'true' where id = ?")
        ) {
            stmt.setLong(1, aLong);
            return stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt = connection.prepareStatement("select id, " +
                        "login, password, name, secret, roles, extract(epoch from created), removed " +
                        "from users where login = ?");
        ) {
            stmt.setString(1, login);
            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return false;
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt = connection.prepareStatement("select id, " +
                        "login, password, name, secret, roles, created, removed " +
                        "from users where login = ?");
        ) {
            stmt.setString(1, login);
            try (final ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return Optional.ofNullable(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.of(null);
    }

    public UserEntity reset(UserEntity entity) {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement stmt = connection.prepareStatement("update users set " +
                        "password = ? where id = ?")
        ) {
            stmt.setString(1, entity.getPassword());
            stmt.setLong(2, entity.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return entity;
    }
}
