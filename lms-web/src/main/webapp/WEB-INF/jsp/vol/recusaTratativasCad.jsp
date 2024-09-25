<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<script type="text/javascript">
 function myOnPageLoad(){
	onPageLoad();
	var url = new URL(parent.location.href);
	var documento = getFormattedValue("integer", url.parameters["documento"], "0000000000", true);
	var idRecusa = url.parameters["idRecusa"];
	var destinatario = url.parameters["destinatario"];
	var status = url.parameters["status"];
	
    setElementValue('documento',documento);
    setElementValue('idRecusa',idRecusa); 
    setElementValue('destinatario',destinatario); 
    
    findContato(idRecusa);
   	findAcao(); 
   				
   	/*se o status for de resolução esconde os campos de contatoDestinatario, emailContato e acao */	
     if(status != "C"){
    	document.getElementById("acao").required = "false";
    	setRowVisibility("contatoDestinatario", false, this.document);
    	setRowVisibility("emailContato", false, this.document);
    	setRowVisibility("acao", false, this.document);
    } 
}
</script>


<adsm:window service="lms.vol.recusaTratativasAction" onPageLoad="myOnPageLoad">
	<adsm:form action="/vol/recusaTratativas" idProperty="idTratativa" >	 
		<adsm:hidden property="idRecusa" />
		<adsm:hidden property="status"/>
		<adsm:textbox property="documento" dataType="text" label="ctrc"	maxLength="10" size="10" width="70%" disabled="true" />
		<adsm:textbox property="destinatario" label="destinatario" dataType="text" size="60" width="70%" disabled="true"/>
		<adsm:textbox property="contatoDestinatario" label="contatoDestinatario" dataType="text" maxLength="60" size="60" width="70%"/>
		<adsm:combobox property="emailContato" optionProperty="idContato" optionLabelProperty="nmContato" label="emailContatoDestinatario"
			width="70%"	cellStyle="vertical-align:bottom;" boxWidth="200"/>	
		<adsm:combobox property="acao" label="acao" optionProperty="valueAcao" optionLabelProperty="dsAcao" required="true"
			width="70%" boxWidth="200"/>
		<adsm:textarea property="observacoes" columns="40" rows="4" width="85%" maxLength="255" label="observacoes" required="true" />
		<adsm:buttonBar>
			<adsm:storeButton service="lms.vol.recusaTratativasAction.store" callbackProperty="returnParent"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/**
	* retorna para tela de Consultar Recusa
	*/
	function returnParent_cb(data, error){
		if (data._exception==undefined) {
			returnToParent();
		 } else {
			alert(data._exception._message);
		}
	}

	function findContato(idRecusa){
		var data = new Object();
		data.idRecusa = idRecusa;
	
		var sdo = createServiceDataObject("lms.vol.recusaTratativasAction.findContato", "populaComboContato" , data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function populaComboContato_cb(data){	
		emailContato_cb(data);
	}
	
	function findAcao(){
		var data = new Object();
		var sdo = createServiceDataObject("lms.vol.recusaTratativasAction.findComboAcao", "populaComboAcao" , data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function populaComboAcao_cb(data){
		acao_cb(data);
	}
	
	/**
	 * Fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
</script>