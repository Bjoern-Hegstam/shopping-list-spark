package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(UserMapper.class)
public interface JdbiUserRepository extends UserRepository {
    @Override
    default UserId create(User user) {
        int id = createUser(
                user.getUsername(),
                user.getEmail(),
                user.getHashedPassword(),
                user.getSalt(),
                user.isVerified(),
                mapUserRole(user.getRole())
        );
        return UserId.from(id);
    }

    default String mapUserRole(Role role) {
        switch (role) {
            case USER:
                return "USER";
            case ADMIN:
                return "ADMIN";
            default:
                throw new IllegalArgumentException("Unmapped role: " + role);
        }
    }

    @SqlUpdate("insert into application_user(username, email, hashed_password, salt, verified, role) values (:username, :email, :hashedPassword, :salt, :verified, :role)")
    @GetGeneratedKeys
    int createUser(
            @Bind("username") String username,
            @Bind("email") String email,
            @Bind("hashedPassword") String hashedPassword,
            @Bind("salt") String salt,
            @Bind("verified") boolean verified,
            @Bind("role") String role
    );


    @Override
    @SqlQuery("select * from application_user where id = :userId.id")
    User get(@BindBean("userId") UserId userId);

    @Override
    @SqlQuery("select * from application_user where username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @Override
    @SqlQuery("select * from application_user where email = :email")
    Optional<User> findByEmail(@Bind("email") String email);

    @Override
    @SqlQuery("select * from application_user")
    List<User> getUsers();

    @Override
    default void update(User user) {
        updateUser(
                user.getId().getId(),
                user.getUsername(),
                user.getEmail(),
                user.isVerified(),
                mapUserRole(user.getRole())
        );
    }

    @SqlUpdate("update application_user set username = :username, email = :email, verified = :verified, role = :role where id = :id")
    void updateUser(
            @Bind("id") int id,
            @Bind("username") String username,
            @Bind("email") String email,
            @Bind("verified") boolean verified,
            @Bind("role") String role
    );
}
