<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterBDMAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-36047"/>
		<adsm:include key="LMS-36076"/>
	</adsm:i18nLabels>

	<adsm:form action="/contasReceber/manterBDM"
			   idProperty="idBaixaDevMerc"
			   newService="lms.contasreceber.manterBDMAction.newMaster"
			   onDataLoadCallBack="myOnDataLoad"
	>

		<adsm:hidden property="filial.idFilialUsuario"/>
		<adsm:hidden property="filial.sgFilialUsuario"/>
		<adsm:hidden property="filial.pessoa.nmFantasiaUsuario"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialEmissora" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialOrigem" 
 					 picker="false"
					 size="3" 
					 required="true"
					 maxLength="3" 
					 width="35%"
					 disabled="true"
					 labelWidth="15%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialEmissora.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialEmissora.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>	
		
		<adsm:hidden property="siglaNomeFilial"/>	

        <adsm:textbox label="numero" property="nrBdm" dataType="integer" size="10" labelWidth="20%" width="30%" disabled="true" />

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialDestino" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialDestino" 
					 size="3" 
					 required="true"
					 disabled="true"
					 maxLength="3" 
					 width="35%"
					 labelWidth="15%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>		

		<adsm:textbox label="dataEmissao" labelWidth="20%" width="30%" property="dtEmissao" dataType="JTDate" disabled="true" required="true"/>

        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS_BDM" labelWidth="15%" width="35%" disabled="true" defaultValue="E" required="true"/>

		<adsm:hidden property="statusAtivo" value="A" />
		<adsm:lookup label="responsavelFrete"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="85%"
					 required="true"
					 disabled="true"
					 serializable="true"
					 labelWidth="15%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="50" maxLength="50" />
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:buttonBar> 
	        <adsm:button caption="emitir" id="btnEmitir" disabled="false" onclick="return emitir()"/>
	        <adsm:button caption="retransmitir" onclick="retransmitir()"/>

   			<adsm:button caption="salvar" id="storeButton" onclick="return myStore();" buttonType="storeButton"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="limpar(this)" disabled="false"/>
			<adsm:button caption="cancelar" onclick="cancelar()"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>

    function myOnDataLoad_cb(data,error){
        onDataLoad_cb(data, error);
        var filial = data.filialEmissora.sgFilial + " - " + data.filialEmissora.pessoa.nmFantasia;
        setElementValue("siglaNomeFilial",filial);
            
    }


	/** Store personalizado para que se trate o retorno
	* chamado: pelo botão salvar
	*/
	function myStore(){
		storeButtonScript('lms.contasreceber.manterBDMAction.store', 'myStore',document.forms[0]);
	}
	
	
	/** Retorno do store, seta o nrBdm na terceira aba
	*/
	function myStore_cb(d,e,c,x){
		store_cb(d,e,c,x);
		
		var tabGroup = getTabGroup(this.document);	
				
		var tabDoctoServico = tabGroup.getTab("documentosServico");
		var telaDoctoServico = tabDoctoServico.tabOwnerFrame.document;
		
		if( d != undefined ){
			setElementValue(telaDoctoServico.getElementById("_nrBdm"), d.nrBdm);
		}
	}
	
	
	
	
	/**
	 * Function que limpa os campos e seta o focus no primeiro campo que pode receber o focus
	 *
	 * chamado por: botão limpar
	 */
	function limpar(elem){
		newButtonScript(elem.document, true, {name:'newButton_click'});
		setElementValue("tpSituacao",'E');
		setFocusOnFirstFocusableField(); 
	}

	function emitir(){
		_serviceDataObjects = new Array(); 
		var data = new Object();
		
		data.nrBdm = getElementValue("nrBdm");
		data.filial = {idFilial: getElementValue("filialEmissora.idFilial"), pessoa: {nmFantasia: getElementValue("filialEmissora.pessoa.nmFantasia")}};
		data.sgFilial = getElementValue("filialEmissora.sgFilial");
		data.tpFormatoRelatorio = "pdf";
		addServiceDataObject(createServiceDataObject('lms.contasreceber.emitirBDMAction.execute', 'exibeRelatorio', data)); 
		xmit(false);	
	}
	
	function exibeRelatorio_cb(strFile, error){
		openReportWithLocator(strFile, error);
		if (error == null){
			setElementValue("tpSituacao", "E");
		}
	}

	function initWindow(eventObj){
		
		var desabilita = true;
		
		setDisabled('btnLimpar',false);
		
		setDisabled('storeButton',false);
		
		
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" || eventObj.name == "cleanButton_click" ){
			setElementValue("tpSituacao","E");
		}
		
		if( eventObj.name != 'gridRow_click' &&
				!(eventObj.name == "tab_click" &&
					 eventObj.src.tabGroup.oldSelectedTab.properties.id == "documentosServico") ) {
			
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.findDataAtual",
				"setDataAtual", 
				new Array()));

			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.findFilialUsuarioLogado",
				"setFilialUsuario", 
				new Array()));

	        xmit(false);
	        
	        desabilita = false;
		} else if ( eventObj.name != 'storeButton'  ){
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.findFilialUsuarioLogado",
				"setFilialHidden", 
				new Array()));

	        xmit(false);
		}
		
		if ( eventObj.name == "gridRow_click" || eventObj.name == 'storeButton' ){	
			setFocus('btnLimpar',true,true)
		}
		if (eventObj.name == "tab_click"){
			var botao = document.getElementById("btnEmitir");
			desabilita = !botao.disabled;
		}
        setDisabled("filialDestino.idFilial", desabilita);
        setDisabled("cliente.idCliente", desabilita);		
	}

	function setDataAtual_cb(data,error) {
		setElementValue("dtEmissao", data._value);
	}
	
	function setFilialHidden_cb(data, error) {
		if (data != null) {
			setElementValue("filial.idFilialUsuario", data.idFilial);
			setElementValue("filial.sgFilialUsuario", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasiaUsuario", data.pessoa.nmFantasia);
		}
	}
	
	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			setElementValue("filialEmissora.idFilial", data.idFilial);
			setElementValue("filialEmissora.sgFilial", data.sgFilial);
			setElementValue("filialEmissora.pessoa.nmFantasia", data.pessoa.nmFantasia);
			
			setElementValue("siglaNomeFilial", data.sgFilial + " - " + data.pessoa.nmFantasia);
			
			setElementValue("filial.idFilialUsuario", data.idFilial);
			setElementValue("filial.sgFilialUsuario", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasiaUsuario", data.pessoa.nmFantasia);
		}
	}
	
	/*
	
		Essa verificação foi retirada conforme solicitação da Rita  de Cássia no dia 26/07/2006
		
	
	function validaFilialDestino(data, error) {
		var fEmissora = getElementValue("filialEmissora.idFilial");
		var fDestino = data.idFilial;
		
		if (fDestino != null) {
			if (fEmissora == fDestino) {
				alert(i18NLabel.getLabel("LMS-36047"));
				resetValue("filialDestino.idFilial");
				document.getElementById("filialDestino.sgFilial").focus();
			}
		}
	}
	
	function validaFilialDestino_cb(data, error) {
		filialDestino_sgFilial_exactMatch_cb(data);
	
		if (data[0] != null) {
			var fEmissora = getElementValue("filialEmissora.idFilial");
			var fDestino = data[0].idFilial;
			
			if (fDestino != null) {
				if (fEmissora == fDestino) {
					alert(i18NLabel.getLabel("LMS-36047"));
					resetValue("filialDestino.idFilial");
					document.getElementById("filialDestino.sgFilial").focus();
				}
			}
		}
	}

*/
	function retransmitir() {
		_serviceDataObjects = new Array();
				
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.storeRetransmitir",
			"sucesso", 
			{idBaixaDevMerc:getElementValue("idBaixaDevMerc")}));
	
	    xmit(false);	
	}
	
	function cancelar() {
		if (confirm(i18NLabel.getLabel("LMS-36076"))) {
			_serviceDataObjects = new Array();
					
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.storeCancelar",
				"cancelar", 
				{idBaixaDevMerc:getElementValue("idBaixaDevMerc")}));
		
	        xmit(false);
        }
	}
	
	function sucesso_cb(data, error) {
		if (error != null) {
			alert(error);
	    } else {
			showSuccessMessage();
		}
	}
	
	function cancelar_cb(data, error) {
		if (error != null) {
			alert(error);
	    } else {
	    	setElementValue("tpSituacao", "C")
			showSuccessMessage();
		}
	}
	
	document.getElementById("siglaNomeFilial").masterLink = true;
</script>