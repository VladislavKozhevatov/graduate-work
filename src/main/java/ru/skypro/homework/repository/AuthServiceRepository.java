package ru.skypro.homework.repository;

import ru.skypro.homework.dto.Registration.PasswordDTO;
import ru.skypro.homework.dto.Registration.RegisterDTO;
import ru.skypro.homework.dto.User.UserDTO;

public interface AuthServiceRepository {
    boolean login(String userName, String password);
    UserDTO register(RegisterDTO register);
    void changePassword(String email, PasswordDTO passwordDto);
}
