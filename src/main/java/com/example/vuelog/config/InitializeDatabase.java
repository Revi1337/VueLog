package com.example.vuelog.config;

import com.example.vuelog.domain.Post;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
public class InitializeDatabase {

    private final InitMemberService initMemberService;

    public InitializeDatabase(InitMemberService initMemberService) {
        System.out.println("InitializeDatabase.InitializeDatabase() -2-");
        this.initMemberService = initMemberService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("InitializeDatabase.postConstruct() -3-");
        initMemberService.initialize();
    }

    @Component
    static class InitMemberService {

        private final EntityManager entityManager;

        public InitMemberService(EntityManager entityManager) {
            System.out.println("InitMemberService.InitMemberService() -1-");
            this.entityManager = entityManager;
        }

        @Transactional
        public void initialize() {
            System.out.println("InitMemberService.initialize()");
            for (int i = 0; i < 10; i++)
                entityManager.persist(
                        Post.builder()
                                .title("title" + i)
                                .content("content" + i)
                                .build()
                );
        }
    }

}

