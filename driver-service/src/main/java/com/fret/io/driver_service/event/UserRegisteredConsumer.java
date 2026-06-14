package com.fret.io.driver_service.event;

import com.fret.io.driver_service.dto.UserRegisteredEvent;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserRegisteredConsumer {

    private final DriverRepository driverRepository;


    public UserRegisteredConsumer(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @RabbitListener(queues = "user.registered")
    public void consume(UserRegisteredEvent event){

        System.out.println("EVENTO RECEBIDO");
        System.out.println(event);

        try{
            if (!"CPF".equals(event.getDocumentType())){
                return;
            }

            Driver driver = new Driver();
            driver.setUserId(event.getIdUser());
            driver.setCpf(event.getDocument());
            driver.setAvgRating(BigDecimal.ZERO);

            driverRepository.save(driver);

        }catch (Exception e){
            System.err.println(
                    "Erro ao processar user.registered" + e.getMessage()
            );
        }
    }
}
