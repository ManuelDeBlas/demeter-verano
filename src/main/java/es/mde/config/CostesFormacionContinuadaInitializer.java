package es.mde.config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.mde.entidades.CosteFormacionContinuadaConId;
import es.mde.repositorios.CosteFormacionContinuadaDAO;

/**
 * Inicializa los datos de costes de formación continuada al arrancar la
 * aplicación, cargándolos desde un archivo JSON si la base de datos está vacía.
 */
@Component
public class CostesFormacionContinuadaInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(CostesFormacionContinuadaInitializer.class);

  private final CosteFormacionContinuadaDAO costeFormacionContinuadaDAO;
  private final ObjectMapper objectMapper;

  // Ruta del archivo JSON con los datos iniciales, leída desde
  // application.properties o application.yml
  @Value("${costes-fc-iniciales-json}")
  private String costesIniciales;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param costeFormacionContinuadaDAO DAO para persistencia de datos
   * @param objectMapper                objeto Jackson para leer JSON
   */
  public CostesFormacionContinuadaInitializer(CosteFormacionContinuadaDAO costeFormacionContinuadaDAO,
      ObjectMapper objectMapper) {
    this.costeFormacionContinuadaDAO = costeFormacionContinuadaDAO;
    this.objectMapper = objectMapper;
  }

  /**
   * Método que se ejecuta automáticamente al iniciar la aplicación. Si no hay
   * datos en la tabla correspondiente, carga los definidos en el archivo JSON.
   *
   * @param args argumentos de línea de comandos
   * @throws Exception si hay problemas al leer o procesar el archivo JSON
   */
  @Override
  public void run(String... args) throws Exception {
    if (costeFormacionContinuadaDAO.count() == 0) {
      InputStream inputStream = getClass().getResourceAsStream(costesIniciales);
      List<CosteFormacionContinuadaConId> costes = Arrays
          .asList(objectMapper.readValue(inputStream, CosteFormacionContinuadaConId[].class));
      costeFormacionContinuadaDAO.saveAll(costes);
      log.info("Costes de formación continuada inicializados correctamente.");
    }
  }

}
