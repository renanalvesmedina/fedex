<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnDataLoad(){
		onDataLoad();
		//validateBotaoReceber();
	}
</script>
<adsm:window service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction">

	<adsm:form action="/contasReceber/efetivarRecebimentoTransferenciaDebito" idProperty="idTransferencia" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
		<adsm:textbox property="filialByIdFilialOrigem.sgFilial" label="filialOrigem" labelWidth="20%" width="5%"
				dataType="text" serializable="false" disabled="true" size="3"/>
		<adsm:textbox property="filialByIdFilialOrigem.pessoa.nmFantasia" dataType="text" serializable="false" 
				disabled="true" size="35" width="30%"/>

		<adsm:textbox dataType="integer" property="nrTransferencia" size="15" maxLength="15" label="numeroTransferencia" 
				labelWidth="20%" width="25%" disabled="true"/>		

		<adsm:hidden property="filialByIdFilialDestino.idFilial" />
		<adsm:textbox property="filialByIdFilialDestino.sgFilial" label="filialDestino" labelWidth="20%" width="5%"
				dataType="text" serializable="false" disabled="true" size="3"/>
		<adsm:textbox property="filialByIdFilialDestino.pessoa.nmFantasia" dataType="text" serializable="false" 
				disabled="true" size="35" width="30%"/>

        <adsm:textbox label="dataEmissao" property="dtEmissao" dataType="JTDate" labelWidth="20%" width="25%" 
        	disabled="true" picker="false"/>

		<adsm:textbox label="origemTransferencia" property="tpOrigem.description" 
			dataType="text" 
			serializable="false" disabled="true" size="35" 
			labelWidth="20%" width="35%"/>

		<adsm:textbox label="situacaoTransferencia" property="tpSituacaoTransferencia.description" 
			dataType="text" 
			serializable="false" disabled="true" size="35" 
			labelWidth="20%" width="25%"/>

        <adsm:textbox label="dataRecebimento" property="dtRecebimento" dataType="JTDate" disabled="true" 
       		labelWidth="20%" width="35%" picker="false"/>

        <adsm:textbox label="dataTransmissao" property="dhTransmissao" dataType="JTDateTimeZone" disabled="true" 
        	labelWidth="20%" width="25%" picker="false"/>

		<adsm:buttonBar>
			<adsm:button caption="receber" buttonType="storeButton" 
				service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.storeTransferenciaDebito"
				disabled="true" id="buttonReceber" callbackProperty="retornoStore"/>
			<adsm:button caption="retransmitir" buttonType="storeButton" 
				service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.storeRetransmitirTransferenciaDebito"
				disabled="true" id="btnRetransmitir" callbackProperty="retornoRetransmitir"/>	
			<adsm:button caption="cancelar" buttonType="storeButton" 
				service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.storeCancelarTransferenciaDebito"
				disabled="true" id="buttonCancelar" callbackProperty="retornoCancelar"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>
<script>

	/*  retorno do botao de retransmitir */
	function retornoRetransmitir_cb(data,error){
		if (error != undefined){
			alert( error );		
		}else{
			showSuccessMessage();
			setElementValue("dhTransmissao", "");
		}
	}
	
	/*  retorno do botao de receber */
	function retornoStore_cb(data,error) {
		if (error != undefined){
			alert( error );		
		}else{
			showSuccessMessage();
		}
	}

	/*  retorno do botao de cancelar */
	function retornoCancelar_cb(data,error) {
		if (error != undefined){
			alert( error );		
		}else{
			showSuccessMessage();
		}
	}

	function initWindow(eventObj){
		if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "proc"){	
			var sdo = createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findById",
				"findById", {id:getElementValue("idTransferencia")});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function findById_cb(data, erro){
		onDataLoad_cb(data,erro);
	}
	
	function myDataLoad_cb(data, erro){
		onDataLoad_cb(data,erro);
		tabSetDisabled("proc", false);
		validateBotoes();
	}
	
	function tabSetDisabled(tab, disable) {
		var tabGroup = getTabGroup(this.document);
 		tabGroup.setDisabledTab(tab, disable);	
	}

	function onClickReceber(){
	}
	
	function validateBotoes(){
		_serviceDataObjects = new Array();
		prepareXmitValidateBotaoCancelar();
		prepareXmitValidateBotaoReceber();
		prepareXmitValidateBotaoRetransmitir();
		xmit(false);
	}
	
	/**
	*	Valida se o botão Retransmitir deve ou não estar habilitado
	*   Somente estará habilitado se a filial da transferencia for igual a filial do usuário logado
	*/
	function prepareXmitValidateBotaoRetransmitir(){
		var dados = new Array();
		
		setNestedBeanPropertyValue(dados, "filialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(dados, "transferencia.idTransferencia", getElementValue("idTransferencia"));
		
        addServiceDataObject(createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.validateBotaoRetransmitir",
                                                     "validateBotaoRetransmitir",
                                                     dados));
	}
	
	function prepareXmitValidateBotaoCancelar(){
		var dados = new Array();
		
		setNestedBeanPropertyValue(dados, "filialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(dados, "transferencia.idTransferencia", getElementValue("idTransferencia"));
		
        addServiceDataObject(createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.validateBotaoCancelar",
                                                   "validateBotaoCancelar",
                                                   dados));
	}

	function prepareXmitValidateBotaoReceber(){

		var dados = new Array();
		//alert(getElementValue("filialByIdFilialDestino.idFilial"));
		//alert(getElementValue("filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(dados, "filialDestino.idFilial", getElementValue("filialByIdFilialDestino.idFilial"));
		setNestedBeanPropertyValue(dados, "transferencia.idTransferencia", getElementValue("idTransferencia"));
		
        addServiceDataObject(createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.validateBotaoReceber",
                                                   "validateBotaoReceber",
                                                   dados));
	}
	
	function validateBotaoCancelar_cb(data,error){	
		setDisabled("buttonCancelar", (data._value == "false") );
	}
	
	/**
	*	Habilita/Desabilita o botão Retransmitir
	*/
	function validateBotaoRetransmitir_cb(data,error){	
		
		setDisabled("btnRetransmitir", (data._value == "false") );
	}
	
	function validateBotaoReceber_cb(data, errors){
		//Habilita se o usuario logado e tpSituacaoTransferencia forem válidos
		setDisabled("buttonReceber", (data._value == "false") );
	}
	
	function onCadShow(evento){
		if (getElementValue("idTransferencia") != ""){
			validateBotoes();
		}
	}
</script>