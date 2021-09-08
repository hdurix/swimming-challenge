package fr.hippo.swimmingchallenge.service;

import fr.hippo.swimmingchallenge.config.JHipsterProperties;
import fr.hippo.swimmingchallenge.domain.Timeslot;
import fr.hippo.swimmingchallenge.domain.User;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Value("${application.url}")
    private String applicationUrl;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", applicationUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", applicationUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", applicationUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendSocialRegistrationValidationEmail(User user, String provider) {
        log.debug("Sending social registration validation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("provider", WordUtils.capitalize(provider));
        String content = templateEngine.process("socialRegistrationValidationEmail", context);
        String subject = messageSource.getMessage("email.social.registration.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPayedEmail(User user, Integer price, List<Timeslot> timeslots) {
        log.debug("Sending payed e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("applicationUrl", applicationUrl);
        context.setVariable("price", price);
        context.setVariable("timeslots", timeslots);
        String content = templateEngine.process("payedEmail", context);
        String subject = "Confirmation d'inscription journée Défi \"Tout ce qui roule\" (payement reçu)";
        if (user.getEmail() == null) {
            log.error("L'utilisateur {} ne possède pas d'email, impossible de lui envoyer un mail de confirmation.",
                user.getLogin());
        } else {
            sendEmail(user.getEmail(), subject, content, false, true);
        }
    }

    @Async
    public void sendReminderEmail(User user, Integer price, List<Timeslot> timeslots) {
        log.debug("Sending reminder e-mail to '{}'", user.getEmail());
        Locale locale = Locale.FRANCE;
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("applicationUrl", applicationUrl);
        context.setVariable("price", price);
        context.setVariable("timeslots", timeslots);
        String content = templateEngine.process("reminderEmail", context);
        String subject = "Inscription Défi \"Tout ce qui roule\" en attente";
        if (user.getEmail() == null) {
            log.error("L'utilisateur {} ne possède pas d'email, impossible de lui envoyer un mail de rappel.",
                user.getLogin());
        } else {
            sendEmail(user.getEmail(), subject, content, false, true);
        }
    }
}
