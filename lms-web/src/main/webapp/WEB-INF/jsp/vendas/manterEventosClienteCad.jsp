<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.eventoClienteService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/vendas/manterEventosCliente" idProperty="idEventoCliente" onDataLoadCallBack="myDataLoad">
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	   
		<adsm:hidden property="evento.idEvento"/>                                                                   
		<adsm:combobox property="idEvento" boxWidth="445" optionLabelProperty="comboText" 
				optionProperty="idEvento" serializable="false" service="lms.vendas.eventoClienteService.findEventoComboAtivo" 
				label="evento" required="true" autoLoad="false" width="85%" onchange="eventoOnChange();"/>
		<adsm:textbox maxLength="60" size="70" dataType="text" property="dsEventoCliente" label="descricao" required="true" width="85%" />		
		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao" required="true"/>		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function eventoOnChange(){
		setElementValue("evento.idEvento",getElementValue("idEvento"));
	}

	function myOnPageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		preencheComboEvento();
	}

	function preencheComboEvento(){
		var sdo = createServiceDataObject("lms.vendas.eventoClienteService.findEventoComboAtivo", "preencheComboEvento",{idEvento:getElementValue("evento.idEvento")});			
		xmit({serviceDataObjects:[sdo], onXmitDone:"preencheComboEvento"});		
	}

	function preencheComboEvento_cb(data,error){
		if (data != undefined) {
			comboboxLoadOptions({data:data, e:document.getElementById("idEvento")});
			setElementValue("idEvento",getElementValue("evento.idEvento"));
		}
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
		if (data != undefined) {
			setElementValue("idEvento",data.evento.idEvento);
		}
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(eventObj){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

	function myOnShow(){
		initWindow();
		setFocusOnFirstFocusableField();//ninguem merece
		return false;
	}	
</script>