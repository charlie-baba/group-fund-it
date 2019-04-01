/**
 * 
 */
package com.gfi.web.pojo;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.GroupBank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Obi
 *
 */
@Getter
@Setter
@ToString
public class NewGroupRequest implements Serializable {
	
	private static final long serialVersionUID = 9111429926553625003L;

	private Long groupId;
	
	@NotBlank(message = "Group name is required")
	private String name;
	
	private String description;
	
	private String category;
	
	@Range(min = 0, max = 24, message = "Fund should be available between 1 to 24 months") 
	private Integer fundDuration;
	
	private String groupPrivacy;
	
	private String targetAmount;
	
	private String bankCode;
	
	@Size(min = 0, max = 14, message = "Invalid account number")
	private String acctNo;
	
	private String paymentPolicy;
	
	private boolean allowAnonymous;
	
	private String chargeBearer;
	
	private MultipartFile logo;
	
	private String base64Image;
	
	public static NewGroupRequest fromGroup(Group group) {
		NewGroupRequest grpReq = new NewGroupRequest();
		grpReq.groupId = group.getId();
		grpReq.name = group.getName();
		grpReq.description = group.getDescription();
		grpReq.category = group.getCategory() != null ? group.getCategory().name() : "";
		grpReq.fundDuration = group.getFundAvailabilityDuration();
		grpReq.groupPrivacy = group.isPrivateGroup() ? "private" : "public";
		grpReq.targetAmount = group.getTargetAmount().toString();
		if (group.getGroupBanks() != null && !group.getGroupBanks().isEmpty()) {
			GroupBank grpBank = group.getGroupBanks().iterator().next();
			grpReq.bankCode = grpBank.getBank().getCode();
			grpReq.acctNo = grpBank.getAccountNumber();
		}
		grpReq.allowAnonymous = group.isAllowAnonymousDonations();
		grpReq.chargeBearer = group.getChargeBearer() != null ? group.getChargeBearer().name() : "";
		grpReq.base64Image = group.getBase64Image();
		return grpReq;
	}
	
}
