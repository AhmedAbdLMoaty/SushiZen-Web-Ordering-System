package ism.lab02_ism.controller;

import ism.lab02_ism.api.UsersApi;
import ism.lab02_ism.model.LoginDTO;
import ism.lab02_ism.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController implements UsersApi {
    
    
    private final Map<String, UserDTO> users = new HashMap<>();
    
    public UserController() {
        
        UserDTO user = new UserDTO();
        user.setUserID("1");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setPhoneNumber("123-456-7890");
        user.setRole(UserDTO.RoleEnum.CUSTOMER);
        users.put(user.getUserID(), user);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    @Override
    public ResponseEntity<Void> registerUser(UserDTO userDTO) {
        users.put(userDTO.getUserID(), userDTO);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<UserDTO> loginUser(LoginDTO loginDTO) {
        for (UserDTO user : users.values()) {
            if (user.getEmail().equals(loginDTO.getEmail()) && 
                user.getPassword().equals(loginDTO.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<UserDTO> getUserProfile(String userId) {
        UserDTO user = users.get(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> updateUserProfile(String userId, UserDTO userDTO) {
        if (!users.containsKey(userId)) {
            return ResponseEntity.notFound().build();
        }
        users.put(userId, userDTO);
        return ResponseEntity.ok().build();
    }
}