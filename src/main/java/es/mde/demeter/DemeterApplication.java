package es.mde.demeter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Clase principal de la aplicación Demeter. Configura y ejecuta la aplicación
 * Spring Boot.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@SpringBootApplication
public class DemeterApplication {

  private static final Logger log = LoggerFactory.getLogger(DemeterApplication.class);

  /**
   * Método principal que inicia la aplicación.
   * 
   * @param args Argumentos de línea de comandos.
   */
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DemeterApplication.class, args);
    log.debug("La aplicación está funcionando");
  }

}
