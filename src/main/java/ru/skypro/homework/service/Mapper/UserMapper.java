package ru.skypro.homework.service.Mapper;


import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Registration.RegisterDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;


@Component
public class UserMapper {

    public UserEntity toEntity(RegisterDTO registerDTO) {
        UserEntity entity = new UserEntity();
        entity.setEmail(registerDTO.getUsername());
        entity.setPassword(registerDTO.getPassword());
        entity.setFirstName(registerDTO.getFirstName());
        entity.setLastName(registerDTO.getLastName());
        entity.setPhone(registerDTO.getPhone());
        entity.setRole(registerDTO.getRole());
        return entity;
    }

    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setImage(entity.getImage());
        return dto;
    }

    public void updateEntityFromDTO(UpdateUserDTO updateDTO, UserEntity entity) {
        if (updateDTO.getFirstName() != null)
            entity.setFirstName(updateDTO.getFirstName());
        if (updateDTO.getLastName() != null)
            entity.setLastName(updateDTO.getLastName());
        if (updateDTO.getPhone() != null)
            entity.setPhone(updateDTO.getPhone());
    }
}