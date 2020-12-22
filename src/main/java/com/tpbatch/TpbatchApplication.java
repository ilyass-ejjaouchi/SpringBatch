package com.tpbatch;

import com.tpbatch.entities.Compte;
import com.tpbatch.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TpbatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpbatchApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CompteRepository compteRepository){
        return args -> {
            compteRepository.save(new Compte(0,60000));
            compteRepository.save(new Compte(0,70000));
            compteRepository.save(new Compte(0,80000));
            compteRepository.save(new Compte(0,90000));
        };
    }
}
