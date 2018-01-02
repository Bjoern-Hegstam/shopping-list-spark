package com.bhegstam.user.db;

import com.bhe.jooq.enums.UserRole;
import com.bhe.jooq.tables.records.ApplicationUserRecord;
import com.bhegstam.db.DatabaseConnectionFactory;
import com.bhegstam.user.domain.Role;
import com.bhegstam.user.domain.User;
import com.bhegstam.user.domain.UserId;
import com.bhegstam.user.domain.UserRepository;
import com.bhegstam.util.QueryUtil;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bhe.jooq.Tables.APPLICATION_USER;
import static com.bhegstam.webutil.CustomCollectors.onlyElement;
import static com.bhegstam.webutil.CustomCollectors.onlyOptionalElement;

public class JdbcUserRepository implements UserRepository {
    private final DatabaseConnectionFactory connectionFactory;
    private final QueryUtil queryUtil;

    @Inject
    public JdbcUserRepository(DatabaseConnectionFactory connectionFactory, QueryUtil queryUtil) {
        this.connectionFactory = connectionFactory;
        this.queryUtil = queryUtil;
    }

    @Override
    public UserId create(User user) {
        List<UserId> userIdContainer = new ArrayList<>();

        connectionFactory
                .withConnection(conn -> {
                            Result<ApplicationUserRecord> newUser = DSL
                                    .using(conn)
                                    .insertInto(APPLICATION_USER)
                                    .set(APPLICATION_USER.USERNAME, user.getUsername())
                                    .set(APPLICATION_USER.EMAIL, user.getEmail())
                                    .set(APPLICATION_USER.HASHED_PASSWORD, user.getHashedPassword())
                                    .set(APPLICATION_USER.SALT, user.getSalt())
                                    .set(APPLICATION_USER.VERIFIED, user.isVerified())
                                    .set(APPLICATION_USER.ROLE, getUserRole(user))
                                    .returning(APPLICATION_USER.ID)
                                    .fetch();

                            userIdContainer.add(UserId.from(newUser.get(0).getId()));
                        }
                );

        return userIdContainer.get(0);
    }

    private UserRole getUserRole(User user) {
        return user.getRole() == Role.USER ? UserRole.USER : UserRole.ADMIN;
    }

    @Override
    public User get(UserId userId) {
        return findUsersWhere(APPLICATION_USER.ID.eq(userId.getId()))
                .stream()
                .collect(onlyElement());
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
        return queryUtil.selectObjects(
                dsl -> dsl
                        .selectFrom(APPLICATION_USER)
                        .where(conditions)
                        .orderBy(APPLICATION_USER.USERNAME)
                        .fetch(this::mapRecordToUser)
        );
    }

    private User mapRecordToUser(ApplicationUserRecord r) {
        return new User(
                UserId.from(r.getId()),
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

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Cannot update user that does not have an id");
        }

        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .update(APPLICATION_USER)
                        .set(APPLICATION_USER.USERNAME, user.getUsername())
                        .set(APPLICATION_USER.EMAIL, user.getEmail())
                        .set(APPLICATION_USER.VERIFIED, user.isVerified())
                        .set(APPLICATION_USER.ROLE, getUserRole(user))
                        .where(APPLICATION_USER.ID.eq(user.getId().getId()))
                        .execute()
                );
    }
}
