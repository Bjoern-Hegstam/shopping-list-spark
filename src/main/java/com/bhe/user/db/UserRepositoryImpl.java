package com.bhe.user.db;

import com.bhe.db.DatabaseConnectionFactory;
import com.bhe.jooq.enums.UserRole;
import com.bhe.jooq.tables.records.ApplicationUserRecord;
import com.bhe.user.Role;
import com.bhe.user.User;
import com.bhe.user.UserRepository;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bhe.jooq.Tables.APPLICATION_USER;
import static com.bhe.util.CustomCollectors.onlyOptionalElement;

public class UserRepositoryImpl implements UserRepository {
    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public UserRepositoryImpl(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public boolean create(User user) {
        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .insertInto(APPLICATION_USER)
                        .set(APPLICATION_USER.USERNAME, user.getUsername())
                        .set(APPLICATION_USER.EMAIL, user.getEmail())
                        .set(APPLICATION_USER.HASHED_PASSWORD, user.getHashedPassword())
                        .set(APPLICATION_USER.SALT, user.getSalt())
                        .set(APPLICATION_USER.VERIFIED, user.isVerified())
                        .set(APPLICATION_USER.ROLE, user.getRole() == Role.USER ? UserRole.USER : UserRole.ADMIN)
                        .execute()
                );

        return true;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findUsersWhere(APPLICATION_USER.USERNAME.eq(username))
                .stream()
                .collect(onlyOptionalElement());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findUsersWhere(APPLICATION_USER.EMAIL.eq(email))
                .stream()
                .collect(onlyOptionalElement());
    }

    @Override
    public List<User> getUsers() {
        return findUsersWhere();
    }

    private List<User> findUsersWhere(Condition... conditions) {
        List<User> users = new ArrayList<>();

        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .selectFrom(APPLICATION_USER)
                        .where(conditions)
                        .fetch()
                        .stream()
                        .map(this::mapRecordToUser)
                        .forEach(users::add)
                );

        return users;
    }

    private User mapRecordToUser(ApplicationUserRecord r) {
        return new User(
                r.getUsername(),
                r.getEmail(),
                r.getHashedPassword(),
                r.getSalt(),
                r.getVerified(),
                r.getRole() == UserRole.USER ?
                        Role.USER :
                        Role.ADMIN
        );
    }
}
