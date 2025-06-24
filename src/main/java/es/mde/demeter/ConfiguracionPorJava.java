package es.mde.demeter;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.mde.rest.ConfiguracionRest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * Clase de configuración principal para la aplicación Demeter. Configura los
 * beans necesarios para la gestión de JPA, transacciones y otros componentes.
 * 
 * Carga las propiedades desde varios archivos de configuración y define los
 * beans para el EntityManager, EntityManagerFactory y ObjectMapper.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@Import(ConfiguracionRest.class)
@EnableJpaRepositories("${es.mde.demeter.repositorios}")
@ComponentScan({ "${paquetes-con-componentes}" })
@PropertySource({ "classpath:config/rest.properties", "classpath:config/jackson.properties",
    "classpath:config/email.properties", "classpath:config/DB.properties",
 //"classpath:config/passwordsBD.properties","classpath:config/passwords-email.properties"
})
public class ConfiguracionPorJava {

  /**
   * Paquete donde se encuentran las entidades JPA.
   */
  @Value("${es.mde.demeter.entidades}")
  private String entidades;

  /**
   * Recursos XML adicionales para la configuración de JPA.
   */
  @Value("${es.mde.demeter.jpa-resources}")
  private String[] xmlsJpa;

  /**
   * Configura el bean de EntityManagerFactory.
   * 
   * @param dataSource    Fuente de datos configurada.
   * @param env           Entorno de Spring para acceder a las propiedades.
   * @param vendorAdapter Adaptador del proveedor JPA.
   * @return Bean configurado de tipo
   *         {@link LocalContainerEntityManagerFactoryBean}.
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env,
      JpaVendorAdapter vendorAdapter) {

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setJpaVendorAdapter(vendorAdapter);
    em.setPackagesToScan(entidades);
    em.setMappingResources(xmlsJpa);

    Properties jpaProperties = new Properties();
    Arrays.asList("dialect", "show_sql", "hbm2ddl.auto", "enable_lazy_load_no_trans").stream()
        .map(s -> "hibernate." + s).map(p -> new AbstractMap.SimpleEntry<String, String>(p, env.getProperty(p)))
        .filter(e -> e.getValue() != null).forEach(e -> jpaProperties.put(e.getKey(), e.getValue()));
    em.setJpaProperties(jpaProperties);

    return em;
  }

  /**
   * Configura el bean de EntityManager.
   * 
   * @param emf EntityManagerFactory configurado.
   * @return Bean configurado de tipo {@link EntityManager}.
   */
  @Bean
  @Primary
  public EntityManager entityManager(EntityManagerFactory emf) {
    System.err.println("--- LAS ENTIDADES MAPEADAS SON ---");
    emf.getMetamodel().getEntities().forEach(System.err::println);
    System.err.println("----------------------------------");

    return emf.createEntityManager();
  }

  /**
   * Configura el bean de ObjectMapper para la serialización y deserialización de
   * JSON.
   * 
   * @return Bean configurado de tipo {@link ObjectMapper}.
   */
  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    return mapper;
  }

}
