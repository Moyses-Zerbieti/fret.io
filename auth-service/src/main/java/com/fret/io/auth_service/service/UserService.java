package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.RegisterRequest;
import com.fret.io.auth_service.exception.DocInvalidException;
import com.fret.io.auth_service.model.*;
import com.fret.io.auth_service.repository.UserRepository;
import com.fret.io.auth_service.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserRoleRepository roleRepository;


    public UserService (UserRepository repository, PasswordEncoder encoder, UserRoleRepository roleRepository){
        this.repository = repository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }



    public User registerUser(RegisterRequest userDto){
            String docNumbers = userDto.getDocument();
            UserRoles userRole = new UserRoles();
            User user = userDto.toModel();

            if (docNumbers.length() == 14) {
                if (!isValidCPF(docNumbers)) throw new DocInvalidException("CPF inválido");
                user.setDocumentType(DocumentType.CPF);
                userRole.setRole(Role.MOTORISTA);

            } else if (docNumbers.length() == 18) {
                if (!isValidCNPJ(docNumbers)) throw new DocInvalidException("CNPJ inválido");
                user.setDocumentType(DocumentType.CNPJ);
                userRole.setRole(Role.EMBARCADORA);
            } else {
                throw new DocInvalidException("Documento inválido");
            }

            user.setUserStatus(UserStatus.ACTIVE);
            user.setPasswordHash(encoder.encode(userDto.getPassword()));

            User savedUser = repository.save(user);
            userRole.setUserId(savedUser);
            roleRepository.save(userRole);

            return savedUser;
    }

    private boolean isValidCPF(String doc){
        return doc.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidCNPJ(String doc){
        return doc.matches("\\d{2}\\.\\d{3}\\.d{3}/\\d{4}-\\d{2}");
    }
}
