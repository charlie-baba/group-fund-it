/**
 * 
 */
package com.gfi.web.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bis.gfi.entities.PaystackAccount;
import com.bis.gfi.entities.Project;
import com.bis.gfi.entities.Transaction;
import com.bis.gfi.enums.TransactionStatus;
import com.bis.gfi.enums.TransactionType;
import com.gfi.web.dao.PaystackAccountDao;
import com.gfi.web.dao.SettingsDao;
import com.gfi.web.dao.TransactionDao;
import com.gfi.web.enums.ResponseCode;
import com.gfi.web.enums.SettingsEnum;
import com.gfi.web.pojo.GenericResponse;
import com.gfi.web.pojo.OTPPojo;
import com.gfi.web.pojo.paystack.BaseResponse;
import com.gfi.web.pojo.paystack.CreateRecipientRequest;
import com.gfi.web.pojo.paystack.CreateRecipientResponse;
import com.gfi.web.pojo.paystack.FinalizeTransferRequest;
import com.gfi.web.pojo.paystack.InitiateTransferData;
import com.gfi.web.pojo.paystack.InitiateTransferRequest;
import com.gfi.web.pojo.paystack.InitiateTransferResponse;
import com.gfi.web.pojo.paystack.PaystackInitializeRequest;
import com.gfi.web.pojo.paystack.PaystackInitializeResponse;
import com.gfi.web.pojo.paystack.TransInitializeReponse;
import com.gfi.web.pojo.paystack.VerifyTransactionResponse;
import com.gfi.web.util.AppConstants;
import com.gfi.web.util.PayCalculator;
import com.gfi.web.util.RestUtil;
import com.gfi.web.util.Utility;
import com.sun.jersey.api.client.ClientResponse;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Service
public class PaystackService {
	
	@Autowired
	SettingsDao settingsDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	PayCalculator payCalculator;

	@Autowired
	PaystackAccountDao paystackAccountDao;

	@Transactional
	public TransInitializeReponse initializePayment(double amount, String email, String rootUrl, Project project) {
		TransInitializeReponse response = new TransInitializeReponse();
		Double transCharge = payCalculator.getChargeAmount(amount);
		Double totalAMount = amount + transCharge;
		long chargeAmount = Math.round(100 * totalAMount);
		String ref = Utility.getRequestId();
		String callbackUrl = rootUrl + ref;
		
		PaystackInitializeRequest req = new PaystackInitializeRequest();
		req.setAmount(chargeAmount);
		req.setCallback_url(callbackUrl);
		req.setEmail(email);
		req.setReference(ref);		
		PaystackInitializeResponse resp = getInitializationDetails(req);
		
		if (resp == null || !resp.isStatus()) {
            response.setCode(ResponseCode.SYSTEM_ERROR.getCode());
            response.setDescription(resp.getMessage());
            return response;
        }
		
		createTransaction(totalAMount, transCharge, req.getEmail(), req.getReference(), 
				resp.getData().getAccess_code(), project, TransactionType.DONATION, TransactionStatus.PENDING);
		response.assignResponseCode(ResponseCode.SUCCESS);
        response.setAuthUrl(resp.getData().getAuthorization_url());
        return response;
	}
		
	@Transactional
	private PaystackInitializeResponse getInitializationDetails(PaystackInitializeRequest req) {
		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_INITIALIZE_PATH;
		
		RestUtil<PaystackInitializeResponse> rest = new RestUtil<>();
		rest.setClazz(PaystackInitializeResponse.class);
		PaystackInitializeResponse resp = rest.postJson(url, req, getPaystackKey());
		return resp;
	}

	@Transactional
	public void createTransaction(Double totalAmount, Double transCharge, String email, String transRef, 
			String accessCode, Project project, TransactionType transType, TransactionStatus status) {
		Transaction transaction = new Transaction();
		transaction.setAmount(totalAmount - transCharge);
		transaction.setTransactionCharge(transCharge);
		transaction.setTotalAmount(totalAmount);
		transaction.setEmail(email);
		transaction.setProject(project);
		transaction.setStatus(status);
		transaction.setTransactionRef(transRef);
		transaction.setTransactionType(transType);
		transaction.setAccessCode(accessCode);
		transaction.setDate(new Date());
		transactionDao.create(transaction);
	}	

	@Transactional
	public void updateTransaction(Transaction trans, TransactionStatus status) {
		trans.setStatus(status);
		transactionDao.update(trans);		
	}
	
	@Transactional
	public GenericResponse verifyPayment(Transaction trans) {
		GenericResponse response = new GenericResponse();
		
		VerifyTransactionResponse resp = getTransactionDetails(trans.getTransactionRef());
		if (resp == null) {
			response.assignResponseCode(ResponseCode.SERVICE_NOT_AVAILABLE);
			return response;
		}
		
		if(resp.getData() != null && "success".equals(resp.getData().getStatus())) {
			updateTransaction(trans, TransactionStatus.SUCCESSFUL);
			response.assignResponseCode(ResponseCode.SUCCESS);			
		} else {
			updateTransaction(trans, TransactionStatus.FAILED);
			response.assignResponseCode(ResponseCode.TRANSACTION_FAILED);
			response.setDescription(resp.getMessage());
		}
		return response;
	}

	@Transactional
	private VerifyTransactionResponse getTransactionDetails(String transRef) {
		if (StringUtils.isBlank(transRef)) {
			return null;
		}
		
		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_VERIFY_PATH + transRef;
		RestUtil<VerifyTransactionResponse> rest = new RestUtil<>();
		rest.setClazz(VerifyTransactionResponse.class);
		VerifyTransactionResponse resp = rest.getJson(url, getPaystackKey());
		return resp;
	}
	
	@Transactional
	public BaseResponse toggleOTP(boolean enable) {
		String url = AppConstants.PAYSTACK_BASE_URL + (enable ? AppConstants.PAYSTACK_ENABLE_OTP_PATH : AppConstants.PAYSTACK_DISABLE_OTP_PATH);
		RestUtil<BaseResponse> rest = new RestUtil<>();
		rest.setClazz(BaseResponse.class);
		BaseResponse resp = rest.postJson(url, getPaystackKey());
		return resp;
	}
	
	public BaseResponse finalizeDisbleOTP(OTPPojo otp) {
		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_FINALIZE_DISABLE_OTP_PATH;
		RestUtil<BaseResponse> rest = new RestUtil<>();
		rest.setClazz(BaseResponse.class);
		BaseResponse resp = rest.postJson(url, otp, getPaystackKey());
		return resp;
	}
	
	public CreateRecipientResponse createTransferRecipient(String acctName, String acctNo, String bankCode) {
		CreateRecipientRequest req = new CreateRecipientRequest();
		req.setAccount_number(acctNo);
		req.setBank_code(bankCode);
		req.setDescription(acctName);
		req.setName(acctName);

		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_TRANSFER_RECIPIENT_PATH;
		RestUtil<CreateRecipientResponse> rest = new RestUtil<>();
		rest.setClazz(CreateRecipientResponse.class);
		CreateRecipientResponse resp = rest.postJson(url, req, getPaystackKey());
		return resp;
	}
	
	public InitiateTransferResponse initiateTransfer(String recipientCode, double amount) {
		InitiateTransferRequest req = new InitiateTransferRequest();
		req.setAmount(Math.round(100 * amount));
		req.setReason("Project Payout");
		req.setRecipient(recipientCode);
		
		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_INITIATE_TRANSFER_PATH;
		RestUtil<InitiateTransferResponse> rest = new RestUtil<>();
		rest.setClazz(InitiateTransferResponse.class);
		InitiateTransferResponse resp = rest.postJson(url, req, getPaystackKey());
		return resp;
	}
	
	public ClientResponse finalizeTransfer(String transferCode, String otp) {
		FinalizeTransferRequest req = new FinalizeTransferRequest();
		req.setOtp(otp);
		req.setTransfer_code(transferCode);

		String url = AppConstants.PAYSTACK_BASE_URL + AppConstants.PAYSTACK_FINALIZE_TRANSFER_PATH;
		RestUtil<ClientResponse> rest = new RestUtil<>();
		rest.setClazz(ClientResponse.class);
		ClientResponse resp = rest.postJson(url, req, getPaystackKey());
		return resp;
	}
	
	@Transactional
	public PaystackAccount createPaystackAccount(CreateRecipientResponse resp) {
		log.info("creating paystack account");
		InitiateTransferData data = resp.getData();
		PaystackAccount acct = new PaystackAccount();
		acct.setAccountNumber(data.getDetails().getAccount_number());
		acct.setBankCode(data.getDetails().getBank_code());
		acct.setBankName(data.getDetails().getBank_name());
		acct.setCurrency(data.getCurrency());
		acct.setDescription(data.getDescription());
		acct.setName(data.getName());
		acct.setRecipientCode(data.getRecipient_code());
		acct.setPaystackId(data.getId());
		paystackAccountDao.create(acct);
		return acct;
	}

	private String getPaystackKey() {
        String mode = settingsDao.getSetting(SettingsEnum.DEPLOYMENT_MODE);
        
        String apiKey;
        if ("TEST".equals(mode)) {
            apiKey = "Bearer " + settingsDao.getSetting(SettingsEnum.PAYSTACK_TEST_SECRET_KEY);
        } else {
            apiKey = "Bearer " + settingsDao.getSetting(SettingsEnum.PAYSTACK_LIVE_SECRET_KEY);
        }
        return apiKey;
    }

}
