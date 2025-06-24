package es.mde.servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para enviar correos electrónicos.
 * Proporciona funcionalidad para enviar mensajes utilizando {@link JavaMailSender}.
 * 
 * Este servicio utiliza las configuraciones definidas en las propiedades de Spring para enviar correos.
 * 
 * @author Manuel de Blas Pino
 * @version 1.0
 */
@Service
public class EmailSenderServicio {

  private static final Logger log = LoggerFactory.getLogger(EmailSenderServicio.class);

  private final JavaMailSender mailSender;

  /**
   * Dirección de correo electrónico del remitente.
   * Configurada a través de la propiedad `spring.mail.username`.
   */
  @Value("${spring.mail.username}")
  private String fromEmail;

  /**
   * Constructor que inyecta el servicio de JavaMailSender.
   * 
   * @param mailSender Instancia de {@link JavaMailSender} para enviar correos electrónicos.
   */
  @Autowired
  public EmailSenderServicio(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Envía un correo electrónico con un mensaje de texto simple.
   * 
   * @param destinatario Dirección de correo del destinatario.
   * @param asunto Asunto del correo.
   * @param mensaje Contenido del mensaje.
   */
  public void enviarEmail(String destinatario, String asunto, String mensaje) {
    try {
      SimpleMailMessage email = new SimpleMailMessage();
      email.setFrom(fromEmail);
      email.setTo(destinatario);
      email.setSubject(asunto);
      email.setText(mensaje);
      mailSender.send(email);
      log.info("Correo enviado correctamente a {}", destinatario);
    } catch (Exception e) {
      log.error("Error al enviar el correo a {}: {}", destinatario, e.getMessage());
    }
  }

}
