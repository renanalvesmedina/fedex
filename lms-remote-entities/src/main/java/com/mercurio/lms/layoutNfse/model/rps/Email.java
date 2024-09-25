package com.mercurio.lms.layoutNfse.model.rps;

import java.util.List;


public class Email {
	
	private List<ReceiverToList> ReceiverToList;
	private ReceiverCcList ReceiverCcList;
	
	public List<ReceiverToList> getReceiverToList() {
		return ReceiverToList;
	}
	
	public void setReceiverToList(List<ReceiverToList> receiverToList) {
		ReceiverToList = receiverToList;
	}
	
	public ReceiverCcList getReceiverCcList() {
		return ReceiverCcList;
	}
	public void setReceiverCcList(ReceiverCcList receiverCcList) {
		ReceiverCcList = receiverCcList;
	}
	
}
