package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;

import java.util.List;
import java.util.Optional;

public class UserApplication {
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public UserApplication(UserRepository userRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public UserId createUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new InvalidUsernameException("Username already taken");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidEmailException("Email already in use");
        }

        User user = new User(username, password, email);
        userRepository.create(user);

        Workspace workspace = Workspace.create("Default", user);
        workspaceRepository.create(user.getId(), workspace);

        return user.getId();
    }

    public User getUser(UserId userId) {
        return userRepository.get(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(UserId userId, Role role, Boolean verified) {
        User user = userRepository.get(userId);
        user.setRole(role);
        user.setVerified(verified);

        userRepository.update(user);

        return user;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public List<Workspace> getWorkspaces(UserId userId) {
        return workspaceRepository.getWorkspaces(userId);
    }
}
