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

import es.mde.entidades.CostePorDiaConId;
import es.mde.repositorios.CostePorDiaDAO;

/**
 * Inicializa los datos de coste por día al arrancar la aplicación. Carga los
 * datos desde un archivo JSON si la base de datos está vacía.
 */
@Component
public class CostesPorDiaInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(CostesPorDiaInitializer.class);

  private final CostePorDiaDAO costePorDiaDAO;
  private final ObjectMapper objectMapper;

  // Ruta del archivo JSON con los datos iniciales, configurada desde
  // application.properties o application.yml
  @Value("${costes-dia-iniciales-json}")
  private String costesIniciales;

  /**
   * Constructor con inyección de dependencias.
   *
   * @param costePorDiaDAO DAO para persistencia de costes por día
   * @param objectMapper   lector JSON de Jackson
   */
  public CostesPorDiaInitializer(CostePorDiaDAO costePorDiaDAO, ObjectMapper objectMapper) {
    this.costePorDiaDAO = costePorDiaDAO;
    this.objectMapper = objectMapper;
  }

  /**
   * Método ejecutado automáticamente al arrancar la aplicación. Carga los datos
   * desde el archivo JSON si no hay registros en la base de datos.
   *
   * @param args argumentos de línea de comandos
   * @throws Exception si ocurre algún error al leer el archivo JSON
   */
  @Override
  public void run(String... args) throws Exception {
    if (costePorDiaDAO.count() == 0) {
      InputStream inputStream = getClass().getResourceAsStream(costesIniciales);
      List<CostePorDiaConId> costes = Arrays.asList(objectMapper.readValue(inputStream, CostePorDiaConId[].class));
      costePorDiaDAO.saveAll(costes);
      log.info("Costes por día inicializados correctamente.");
    }
  }

}
