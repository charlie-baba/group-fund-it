/**
 * 
 */
package com.gfi.web.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;

import com.bis.gfi.entities.GroupMembers;
import com.bis.gfi.entities.UserProfile;
import com.gfi.web.enums.EmailType;
import com.gfi.web.service.EmailService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Component
public class EmailUtil {

	@Autowired
    EmailService emailService;
	
	@Autowired
    Configuration freemarkerConfig;
	
	@Value("${gfi.support.email}")
    private String supportEmail;
	
	@Value("${gfi.url}")
    private String url;
	
		
	public void sendPasswordReset(String password, UserProfile user) throws Exception {
        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("firstname", user.getFirstName());
        mailModel.put("password", password);
        mailModel.put("supportEmail", supportEmail);
        String subject = "Password Reset Successful"; 
        String toEmail = user.getEmail();

        EmailType emailType = EmailType.PASSWORD_RESET;
        String message = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(emailType.getTemplateName(), "UTF-8"), mailModel);
        emailService.sendMail(message, subject, toEmail);
    }
	
	public void sendGroupInvitation(String groupName, String email) throws Exception {
		Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("groupName", groupName);
        mailModel.put("url", url);
        mailModel.put("supportEmail", supportEmail);
        String subject = "Invitation to join group on GroupWallet";
        String toEmail = email;
        
        EmailType emailType = EmailType.GROUP_INVITE;
        String message = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(emailType.getTemplateName(), "UTF-8"), mailModel);
        emailService.sendMail(message, subject, toEmail);
	}
	
	public void sendPublishProject(String projectName, String projectLink, List<GroupMembers> members) {
		if (CollectionUtils.isEmpty(members)) {
			return;
		}
		
		Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("projectName", projectName);
        mailModel.put("projectLink", projectLink);
        mailModel.put("supportEmail", supportEmail);
        String subject = "A new Project has been published on GroupWallet";
        EmailType emailType = EmailType.PUBLISH_PROJECT;
        String message;
		try {
			message = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(emailType.getTemplateName(), "UTF-8"), mailModel);
			for (GroupMembers member : members) {
		        emailService.sendMail(message, subject, member.getEmail());
	        }
		} catch (IOException | TemplateException e) {
			log.error("Unable to send publish project mail", e);
		}
	}
	
	public void sendDonationNotification(String email, String firstName, Double amount, String projectName) {
		Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("firstName", firstName);
        mailModel.put("amount", amount);
        mailModel.put("projectName", projectName);
        mailModel.put("supportEmail", supportEmail);
        
        String subject = "Successful Donation";
        String toEmail = email;

        EmailType emailType = EmailType.DONATION;
		try {
			String message = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(emailType.getTemplateName(), "UTF-8"), mailModel);
			emailService.sendMail(message, subject, toEmail);
		} catch (IOException | TemplateException e) {
			log.error("Unable to send donation email: ", e);
		}
	}
	
	public void sendProjectPayout(String email, String adminFirstName, Double amount, String projectName) {
		Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("adminFirstName", adminFirstName);
        mailModel.put("amount", amount);
        mailModel.put("projectName", projectName);
        mailModel.put("supportEmail", supportEmail);
        
        String subject = "Project Payout";
        String toEmail = email;

        EmailType emailType = EmailType.PAYOUT;
		try {
			String message = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate(emailType.getTemplateName(), "UTF-8"), mailModel);
			emailService.sendMail(message, subject, toEmail);
		} catch (IOException | TemplateException e) {
			log.error("Unable to send donation email: ", e);
		}
	}
	
}
