package com.fret.io.driver_service.event;

import com.fret.io.driver_service.dto.UserRegisteredEvent;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegisteredConsumerTest {
    @InjectMocks
    UserRegisteredConsumer userRegisteredConsumer;
    @Mock
    DriverRepository driverRepository;

    private UserRegisteredEvent createUserRegistered(){
        UserRegisteredEvent userRegistered = new UserRegisteredEvent();
        userRegistered.setIdUser(UUID.randomUUID());
        userRegistered.setEmail("test@gmail.com");
        userRegistered.setDocument("123.456.789-01");
        userRegistered.setDocumentType("CPF");

        return userRegistered;
    }

    @Test
    void shouldConsumeQueueWhenExistsUserRegisteredWithCpfTest(){
        UserRegisteredEvent event = createUserRegistered();

        ArgumentCaptor<Driver> driverCaptor =
                ArgumentCaptor.forClass(Driver.class);

        userRegisteredConsumer.consume(event);

        verify(driverRepository).save(driverCaptor.capture());

        Driver savedDriver = driverCaptor.getValue();

        assertThat(savedDriver).isNotNull();

        assertEquals(event.getIdUser(), savedDriver.getUserId());
        assertEquals(event.getEmail(), savedDriver.getEmail());
        assertEquals(event.getDocument(), savedDriver.getCpf());
        assertEquals(BigDecimal.ZERO, savedDriver.getAvgRating());
    }

    @Test
    void shouldNotSaveDriverWhenDocumentTypeIsNotCpfTest(){
        UserRegisteredEvent event = createUserRegistered();
        event.setDocumentType("CNPJ");

        userRegisteredConsumer.consume(event);

        verify(driverRepository, never()).save(Mockito.any());
    }

    @Test
    void shouldThrowExceptionWhenRepositorySavesDriver(){
        UserRegisteredEvent event = createUserRegistered();

        when(driverRepository.save(any(Driver.class)))
                .thenThrow(new RuntimeException());

        assertDoesNotThrow(()->{
            userRegisteredConsumer.consume(event);
        });
    }
}
