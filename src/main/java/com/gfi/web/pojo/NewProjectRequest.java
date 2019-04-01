/**
 * 
 */
package com.gfi.web.pojo;

import static com.gfi.web.util.AppConstants.sdf;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import com.bis.gfi.entities.Group;
import com.bis.gfi.entities.GroupBank;
import com.bis.gfi.entities.Project;
import com.bis.gfi.entities.ProjectBank;

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
public class NewProjectRequest implements Serializable {

	private static final long serialVersionUID = 6379320493526479541L;

	private Long projectId;
	
	private Long groupId;
	
	@NotBlank(message = "Project name is required")
	private String name;
	
	private String description;
	
	private String category;
	
	private String startDate;
	
	private String endDate;
		
	private String projectPrivacy;
	
	private String targetAmount;
	
	@NotBlank(message = "You need to select a bank")
	private String bankCode;
	
	@NotNull(message = "Account number is required")
	@Size(min = 10, max = 14, message = "Invalid account number")
	private String acctNo;
	
	@NotBlank(message = "Account name is required")
	private String acctName;
	
	private String paymentPolicy;
	
	private String chargeBearer;
	
	private boolean privateGroup;
	
	private List<MultipartFile> pics;
	
	public static NewProjectRequest fromGroup(Group group) {
		NewProjectRequest prjReq = new NewProjectRequest();
		prjReq.groupId = group.getId();
		prjReq.privateGroup = group.isPrivateGroup();
		prjReq.projectPrivacy = group.isPrivateGroup() ? "private" : "public";
		prjReq.targetAmount = group.getTargetAmount().toString();
		if (group.getGroupBanks() != null && !group.getGroupBanks().isEmpty()) {
			GroupBank grpBank = group.getGroupBanks().iterator().next();
			prjReq.bankCode = grpBank.getBank().getCode();
			prjReq.acctNo = grpBank.getAccountNumber();
		}
		prjReq.chargeBearer = group.getChargeBearer() != null ? group.getChargeBearer().name() : "";
		return prjReq;
	}
	
	public static NewProjectRequest fromProject(Project project) {
		NewProjectRequest prjReq = new NewProjectRequest();
		prjReq.projectId = project.getId();
		prjReq.groupId = project.getGroup().getId();
		prjReq.privateGroup = project.getGroup().isPrivateGroup();
		prjReq.name = project.getName();
		prjReq.description = project.getDescription();
		prjReq.category = project.getCategory().getId().toString();
		prjReq.startDate = sdf.format(project.getStartDate());
		prjReq.endDate = sdf.format(project.getEndDate());
		prjReq.projectPrivacy = project.isPrivateProject() ? "private" : "public";
		prjReq.targetAmount = project.getTargetAmount().toString();
		if (project.getProjectBanks() != null && !project.getProjectBanks().isEmpty()) {
			ProjectBank prjBank = project.getProjectBanks().iterator().next();
			prjReq.bankCode = prjBank.getBank().getCode();
			prjReq.acctNo = prjBank.getAccountNumber();
			prjReq.acctName = prjBank.getAccountName();
		}
		prjReq.chargeBearer = project.getChargeBearer() != null ? project.getChargeBearer().name() : "";
		
		return prjReq;
	}
	
}
