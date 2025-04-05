package ism.lab02_ism.service;

import ism.lab02_ism.entity.User;
import ism.lab02_ism.model.LoginDTO;
import ism.lab02_ism.model.UserDTO;
import ism.lab02_ism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = convertToEntity(userDTO);

        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID().toString());
        }

        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO authenticateUser(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginDTO.getPassword())) {
            return convertToDTO(userOptional.get());
        }

        return null;
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        return userOptional.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setName(userDTO.getName());
            user.setPhoneNumber(userDTO.getPhoneNumber());

            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                user.setEmail(userDTO.getEmail());
            }

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(userDTO.getPassword());
            }

            user = userRepository.save(user);
            return convertToDTO(user);
        }

        return null;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserID(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPassword(user.getPassword());

        if (user.getRole() != null) {
            switch (user.getRole()) {
                case CUSTOMER:
                    dto.setRole(UserDTO.RoleEnum.CUSTOMER);
                    break;
                case ADMIN:
                    dto.setRole(UserDTO.RoleEnum.ADMIN);
                    break;
                case KITCHEN_STAFF:
                    dto.setRole(UserDTO.RoleEnum.KITCHEN_STAFF);
                    break;
            }
        }

        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setUserId(dto.getUserID());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            switch (dto.getRole()) {
                case CUSTOMER:
                    user.setRole(User.UserRole.CUSTOMER);
                    break;
                case ADMIN:
                    user.setRole(User.UserRole.ADMIN);
                    break;
                case KITCHEN_STAFF:
                    user.setRole(User.UserRole.KITCHEN_STAFF);
                    break;
            }
        }

        return user;
    }
}