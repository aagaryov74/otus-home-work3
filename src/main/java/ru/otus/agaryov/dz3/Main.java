package ru.otus.agaryov.dz3;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import ru.otus.agaryov.dz3.exam.ExamExecutorImpl;

import java.util.Properties;

@SpringBootApplication
@PropertySource("application.yaml")
@PropertySource("messages.yaml")
public class Main {
/*

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
*/

    @Bean(name = "quizProperties")
    public Properties yamlProperties() {
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ClassPathResource("messages.yaml"));
        return bean.getObject();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms
                = new ReloadableResourceBundleMessageSource();
        ms.setCommonMessages(yamlProperties());
        ms.setBasename("messages/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        ExamExecutorImpl executor = context.getBean(ExamExecutorImpl.class);
        executor.doExam();
    }
}
