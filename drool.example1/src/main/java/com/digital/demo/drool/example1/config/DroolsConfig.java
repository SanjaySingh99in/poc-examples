package com.digital.demo.drool.example1.config;


import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/";
    private final KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieContainer kieContainer() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Load all DRL files from resources/rules directory
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "payment-validation.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "fraud-detection.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "limit-check.drl"));
//        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "status-determination.drl"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieRepository kieRepository = kieServices.getRepository();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    @Bean
    public KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession();
    }
}
