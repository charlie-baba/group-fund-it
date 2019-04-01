/**
 * 
 */
package com.gfi.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.gfi.web.enums.EmailType;

import freemarker.template.Configuration;
import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Service
public class EmailService {

	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private Configuration freemarkerConfig;
	
	@Value("${mail.username}")
    private String fromEmailAddress;
		
	public void sendMail(final String messageContent, final String subject, final String toEmail) {
        sendMail(messageContent, subject, toEmail, EmailType.GENERAL_TEMPLATE);
    }
	
	public void sendMail(final String messageContent, final String subject, final String toEmail, EmailType template) {
        log.info(">>> sending email to " + toEmail);
        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("message", messageContent);
        
        final JavaMailSender eSender = mailSender;
        ExecutorService mileageThreadExecutor = Executors.newSingleThreadExecutor();
        mileageThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MimeMessagePreparator preparator = new MimeMessagePreparator() {
                        @Override
                        public void prepare(MimeMessage mimeMessage) throws Exception {
                            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
                            message.setTo(toEmail);
                            message.setFrom(new InternetAddress(fromEmailAddress));
                            message.setSubject(subject);
                            String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(template.getTemplateName(), "UTF-8"), mailModel);
                            message.setText(text, true);
                        }
                    };
                    eSender.send(preparator);
                } catch (Exception ex) {
                    log.error("Could not send email: ", ex);
                }
            }
        });
    }
}
