<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.registrarSolicitacoesRetiradaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/municipios/manterRegionais" idProperty="idSolicitacaoRetirada" 
			   service="lms.sim.registrarSolicitacoesRetiradaAction.findByIdCustom" height="380" 
			   newService="lms.sim.registrarSolicitacoesRetiradaAction.newMaster" onDataLoadCallBack="solicitacaoRetiradaDataLoad">
	
	
		<adsm:hidden property="idProcessoWorkflow"/>
	
		<!-- DADOS LIBERACAO -->
		<adsm:section caption="dadosLiberacao" />
		
		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupFilial" dataType="text" label="filialSolicitante" size="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="35%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px; " cellStyle="vertical-align: bottom;" disabled="true" >
				
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" cellStyle="vertical-align: bottom;" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
		
		<adsm:textbox dataType="integer" property="nrSolicitacaoRetirada" mask="00000000" cellStyle="vertical-align: bottom;" label="numeroSolicitacaoRetirada" disabled="true" maxLength="8" size="10" labelWidth="20%" width="25%"/>
		
		
		<adsm:hidden property="idFilialRetiradaOld"/>
        <adsm:hidden property="idRemetenteOld"/>
        <adsm:hidden property="idDestinatarioOld"/>
        
		<adsm:hidden property="cliente.tpSituacao" value="A"/>
		<adsm:lookup dataType="text" property="remetente" idProperty="idCliente"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="remetente" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" 
				onDataLoadCallBack="remetenteDataLoad" onPopupSetValue="remetentePopup" onchange="return remetenteOnChange(this)">

			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />			
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>
               
		<adsm:lookup dataType="text" property="destinatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="destinatario" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" 
				onDataLoadCallBack="destinatarioDataLoad" onPopupSetValue="destinatarioPopup" onchange="return destinatarioOnChange(this)">
				
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	       
        
		<adsm:lookup dataType="text" property="consignatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="consignatario" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" 
				onDataLoadCallBack="consignatarioDataLoad" onPopupSetValue="consignatarioPopup" onchange="return consignatarioOnChange(this)">
				
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="consignatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="consignatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>	
		
		<adsm:lookup property="filialRetirada" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3" required="true" 
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupFilial" dataType="text" label="filialRetirada" size="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false" 
				onDataLoadCallBack="filialDataLoad" onPopupSetValue="filialPopup" onchange="return filialChange(this)">
				
			<adsm:propertyMapping relatedProperty="filialRetirada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialRetirada.pessoa.nmFantasia" size="30" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:combobox label="liberadoPor" property="tpRegistroLiberacao" onchange="liberadoPorOnChange(this)" domain="DM_TIPO_LIBERACAO_SOLICITACAO_RETIRADA" labelWidth="20%" required="true" width="80%"/>
				
		<adsm:lookup label="nomeResponsavelLiberacao" size="16" cellStyle="vertical-align: bottom;" maxLength="16" labelWidth="20%" width="80%" required="true"
					 dataType="text" property="usuarioAutorizacao" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView" serializable="true">
				     
			<adsm:propertyMapping relatedProperty="nmResponsavelAutorizacao" modelProperty="nmUsuario" />
			<adsm:propertyMapping relatedProperty="dsFuncaoResponsavelAutoriza" modelProperty="dsFuncao" />
	
			<adsm:textbox dataType="text" property="nmResponsavelAutorizacao" cellStyle="vertical-align: bottom;" size="30" maxLength="60" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="dsFuncaoResponsavelAutoriza" label="funcao" maxLength="60" size="30" labelWidth="20%" width="30%"  />
		
		
		<adsm:complement label="telefone" labelWidth="20%" width="30%" >
        	<adsm:textbox dataType="text"  property="nrDDDAutorizador" size="5" maxLength="5" width="10%"/>
			<adsm:textbox dataType="text" property="nrTelefoneAutorizador" maxLength="20" size="15" />
		 </adsm:complement>
		 
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_SITUACAO_SOLICITACAO_RETIRADA" labelWidth="20%" width="80%" required="true" disabled="true"/>


		<!-- DADOS RETIRANTE -->
		<adsm:section caption="dadosRetirante" />
		
		<adsm:textbox dataType="text" property="nmRetirante" label="nome" maxLength="60" size="30" labelWidth="20%" width="80%" required="true"/>
		
		<adsm:complement labelWidth="20%" width="30%" label="identificacao" required="true">
        	<adsm:combobox property="tpIdentificacao" width="12%" domain="DM_TIPO_IDENTIFICACAO_PESSOA" onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:this, numberElement:document.getElementById('nrIdentificacao'), tabCmd:'cad'});" onchangeAfterValueChanged="true"/>
        	<adsm:textbox property="nrIdentificacao" size="18" minLength="5" maxLength="20" dataType="text" depends="tpIdentificacao"/>
        </adsm:complement>

		<adsm:textbox dataType="text" property="nrRg" label="rg" maxLength="12" size="30" labelWidth="20%" width="30%" required="true"/>
	
		<adsm:complement label="telefone" labelWidth="20%" width="30%" required="true">
             <adsm:textbox dataType="text"  property="nrDdd" size="5" maxLength="5" width="10%"/>
             <adsm:textbox dataType="text"  property="nrTelefone" size="10" maxLength="10" width="20%"/>
        </adsm:complement>
	
		<adsm:textbox dataType="text" property="nrPlaca" label="placa" maxLength="7" size="30" 
				labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"
				onchange="return onChangeMeioTransporte(this);" />
		
		<adsm:textbox dataType="text" property="nrPlacaSemiReboque" label="semiReboque" maxLength="7" size="30"
				labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"
				onchange="return onChangeMeioTransporte(this);" />

		<adsm:textbox dataType="JTDateTimeZone" size="15" property="dhPrevistaRetirada" label="dataHoraPrevistaRetirada" labelWidth="20%" width="80%" required="true" cellStyle="vertical-align:bottom;"/>
		
		<adsm:textbox dataType="JTDateTimeZone" size="15" property="dhSolicitacao" label="dataHoraSolicitacao" labelWidth="20%" width="30%" disabled="true"/>
				
		<adsm:hidden property="usuarioCriacao.idUsuario"/>
		<adsm:textbox dataType="text" property="usuarioCriacao.nmUsuario" size="30" width="30%" label="usuario" disabled="true"  labelWidth="20%"/>
	
		<!-- DADOS RETIRADA -->
		<adsm:section caption="dadosRetirada"/>
		
		<adsm:hidden property="manifestoEntrega.idManifestoEntrega"/>
		<adsm:textbox dataType="text" property="manifesto.filial.sgFilial" label="preManifesto" size="3" labelWidth="20%" width="30%" disabled="true">
			<adsm:textbox dataType="integer" property="manifesto.nrPreManifesto" size="20"  disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="manifestoEntrega.filial.sgFilial" label="manifestoEntrega" size="3" labelWidth="20%" width="30%" disabled="true">
			<adsm:textbox dataType="integer" property="manifestoEntrega.nrManifestoEntrega" size="20" mask="00000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="JTDateTimeZone" disabled="true" property="manifesto.dhEmissaoManifesto" label="dataHoraDeEmissao" maxLength="8" size="30" labelWidth="20%" width="30%"/>
		
		<adsm:textbox dataType="JTDateTimeZone" disabled="true" property="manifestoEntrega.dhFechamento" label="dataHoraFechamento" maxLength="8" size="30" labelWidth="20%" width="30%"/>
	
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" />
			<adsm:newButton id="newButton"/>			
			<adsm:button id="cancelarButton" caption="cancelar" onclick="cancelar()"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>   
<script>

	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	
	document.getElementById("filialRetirada.sgFilial").serializable = "true";
	
	function setaMasterLink(){
		var url = new URL(parent.location.href);
		
		if(url.parameters["remetenteIdCliente"]!= undefined){
		
	 		setElementValue("remetente.idCliente",url.parameters["remetenteIdCliente"]);
	 		setElementValue("remetente.pessoa.nrIdentificacao",url.parameters["remetentePessoaNrIdentificacao"]);
	 	    setElementValue("remetente.pessoa.nmPessoa",url.parameters["remetentePessoaNmPessoa"]);
	 	    document.getElementById("remetente.idCliente").masterLink= "true";
	 	    setDisabled("remetente.idCliente",true);
	 	}
	 	if(url.parameters["destinatarioIdCliente"]!= undefined){
	 		setElementValue("destinatario.idCliente",url.parameters["destinatarioIdCliente"]);
	 		setElementValue("destinatario.pessoa.nrIdentificacao",url.parameters["destinatarioPessoaNrIdentificacao"]);
	 	    setElementValue("destinatario.pessoa.nmPessoa",url.parameters["destinatarioPessoaNmPessoa"]);
	 	    document.getElementById("destinatario.idCliente").masterLink= "true";
	 	    setDisabled("destinatario.idCliente",true);
	 	}
	 	
	 		
	 	
	}
	
	function initWindow(eventObj) {		
		
		setElementValue("idFilialRetiradaOld",getElementValue("filialRetirada.idFilial"));
		setElementValue("idRemetenteOld",getElementValue("remetente.idCliente"));
		setElementValue("idDestinatarioOld",getElementValue("destinatario.idCliente"));
				
		if (eventObj.name == "newButton_click" || (eventObj.name == "tab_click" 
			&& eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq")) {	
					
			estadoNovo();	
			setaMasterLink();	
							
		} else if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "doc"){
		
			if (getElementValue("idProcessoWorkflow") != '') {
				setDisabled(document, true);
				
			} else if (getElementValue("idSolicitacaoRetirada") != '')  {
				desabilitaTela();    		
    		}
		}		
		
		if (eventObj.name == "newButton_click") {
			setDisabledTabCustom("doc", true);
			setFocusOnFirstFocusableField();
		}
    }    
    
	function disableClientes(disable){
		setDisabled("remetente.idCliente", disable);
		setDisabled("destinatario.idCliente", disable);
		setDisabled("consignatario.idCliente", disable);
		setDisabled("filialRetirada.idFilial",disable);
	}
    
    function pageLoad_cb(){
    	onPageLoad_cb();
    
	    initPessoaWidget({tpIdentificacaoElement:document.getElementById("tpIdentificacao")
      			   		 ,numberElement:document.getElementById("nrIdentificacao")});
        
    	if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
		}
	}
    
    function afterStore_cb(data,error,key) {

		store_cb(data,error,key);
		var tabGroup = getTabGroup(this.document);
		
		if(error != undefined) {		
			var tabGroup = getTabGroup(this.document);
			if (key == "LMS-10023")
				tabGroup.selectTab('doc',{name:'tab_click'});
			else
				tabGroup.selectTab('cad',{name:'tab_click'});			
		} else {
			desabilitaTela();
			desabilitaTabDoc();
		}
	}
	
    function cancelar(){
    	var data = new Array();
    	data.idSolicitacaoRetirada = getElementValue("idSolicitacaoRetirada");
    	var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesRetiradaAction.executeCancelar", "cancelar", data);
		xmit({serviceDataObjects:[sdo]});
    }
    	
    function cancelar_cb(data, error){
    	if (error == undefined){
    		setElementValue("tpSituacao", "C");
    		setDisabled(document, true);
    		setDisabled("newButton", false);
    		setFocusOnNewButton();
    	} else {
    		alert(error);
    	}    	    	
    }
    
    function tpIdentificacaoOnchange(obj){
    	if (obj.value != ''){    	
    		setDisabled("nrIdentificacao", false);
    		document.getElementById("nrIdentificacao").dataType = obj.value;    		
    	} else {    	
    		setDisabled("nrIdentificacao", true);    	
    	}
    	
    	resetValue("nrIdentificacao");
    }
    
    function solicitacaoRetiradaDataLoad_cb(data, error){
    	onDataLoad_cb(data, error);

    	setDisabledTabCustom("doc", false);   	
		
		desabilitaTela(); 		
    	
    }
    
    
    function desabilitaTela(){
    	setDisabled(document, true);
    	setDisabled("storeButton", true); 
   		setDisabled("newButton", false);   
   		 
   		if	(!(getElementValue("tpSituacao") == 'R' 
   				|| getElementValue("tpSituacao") == 'C' 
   				|| getElementValue("manifestoEntrega.nrManifestoEntrega") != ""))
	   		setDisabled("cancelarButton", false);
   	
   		setFocusOnNewButton();		
    }
    
    function desabilitaTabDoc(){
	    var tabGroup = getTabGroup(this.document);
    	var tab = tabGroup.getTab("doc");			
		var doc = tab.tabOwnerFrame.document;	
		tab.tabOwnerFrame.setDisabled(doc, true);
    }
    
    function setDisabledTabCustom(tabName,disabled) {
        var tabGroup = getTabGroup(this.document);
        tabGroup.setDisabledTab(tabName, disabled);
    }
    
    function camposEstadoNovo(){
     	setDisabled("usuarioAutorizacao.idUsuario", true);
  	    setDisabled("dsFuncaoResponsavelAutoriza", true);
  	    setDisabled("nrTelefoneAutorizador", false);
  	    setDisabled("nrDDDAutorizador", false);
		//setDisabled("filial.idFilial", false);	
		setDisabled("nrSolicitacaoRetirada", true);
		setDisabled("remetente.idCliente", false);
		setDisabled("destinatario.idCliente", false);
		setDisabled("consignatario.idCliente", false);
		setDisabled("filialRetirada.idFilial", false);
		setDisabled("tpRegistroLiberacao", false);
		setDisabled("tpSituacao", true);
		setDisabled("nmRetirante", false);
		setDisabled("tpIdentificacao", false);
		setDisabled("nrIdentificacao", true);
		setDisabled("nrRg", false);
		setDisabled("nrDdd", false);
		setDisabled("nrTelefone", false);
		setDisabled("nrPlacaSemiReboque", false);
		setDisabled("nrPlaca", false);
		setDisabled("dhPrevistaRetirada", false);
		setDisabled("newButton", false);
		setDisabled("storeButton", false);	
    }
    
	function validateTab() {
		getElement("nmResponsavelAutorizacao").label =  getElement("usuarioAutorizacao.idUsuario").label;
		return validateTabScript(document.forms);
	}
    
    function estadoNovo() {

    	if (getElementValue("idProcessoWorkflow") == ''){
		    camposEstadoNovo();
			verificaTipoLiberacao();
		}
		
		// Carrega filial do usuário logado.
		// Se já foi consultado uma vez não é necessário chamar a service novamente.
		if (idFilialLogado != undefined && idFilialLogado != "") {
			setaValoresFilial();
		} else {
			getFilialUsuario();
		}

	}
	
	function setaValoresFilial() {
		if (!document.getElementById("filial.idFilial").masterLink) {
			setElementValue("filial.idFilial", idFilialLogado);
			setElementValue("filial.sgFilial", sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
		}
	}
	
	
	function getFilialUsuario() {

		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("pesq");
		var pesq = tab.tabOwnerFrame.document;
		
		if (tab != undefined && tab != null){
			idFilialLogado = pesq.getElementById("idFilialLogado").value;
			sgFilialLogado = pesq.getElementById("sgFilialLogado").value;
			nmFilialLogado = pesq.getElementById("nmFilialLogado").value;
			setaValoresFilial();
		}

	}
	
	function liberadoPorOnChange(obj){
		verificaTipoLiberacao(obj.value, true);
	}
	
	function verificaTipoLiberacao(tpLiberacao, resetValues){
		if (tpLiberacao == 'G'){
			setDisabled("usuarioAutorizacao.idUsuario", false);
			
			document.getElementById("usuarioAutorizacao.nrMatricula").required = "true";
			document.getElementById("nmResponsavelAutorizacao").required = "false";
			
			setDisabled("nmResponsavelAutorizacao", true);			
  	   		setDisabled("dsFuncaoResponsavelAutoriza", true);  	   		
  	   		setDisabled("nrTelefoneAutorizador", false);  	   		
  	   		setDisabled("nrDDDAutorizador", false);  	   		  	   			
  	   		
  	   		if (resetValues){
  	   			resetValue("nmResponsavelAutorizacao");
  	   			resetValue("dsFuncaoResponsavelAutoriza");
  	   			resetValue("nrDDDAutorizador");
  	   			resetValue("nrTelefoneAutorizador");
  	   		}
  	   		
		} else if (tpLiberacao == 'C'){
			setDisabled("usuarioAutorizacao.idUsuario", true);
				
			
			document.getElementById("usuarioAutorizacao.nrMatricula").required = "false";
			document.getElementById("nmResponsavelAutorizacao").required = "true";
			
			setDisabled("nmResponsavelAutorizacao", false);			
  	   		setDisabled("dsFuncaoResponsavelAutoriza", false);  
  	   		setDisabled("nrDDDAutorizador", false);	   		
  	   		setDisabled("nrTelefoneAutorizador", false);	
  	   		
  	   		if (resetValues){
   				resetValue("usuarioAutorizacao.idUsuario");
				resetValue("usuarioAutorizacao.nrMatricula");	
				resetValue("nmResponsavelAutorizacao");
				resetValue("dsFuncaoResponsavelAutoriza");
  	   		}
  	   			
		} else {
			setDisabled("usuarioAutorizacao.idUsuario", true);
			setElementValue("usuarioAutorizacao.idUsuario", "");
			setElementValue("usuarioAutorizacao.nrMatricula", "");	
			
  	   		setDisabled("dsFuncaoResponsavelAutoriza", true);
  	   		setElementValue("dsFuncaoResponsavelAutoriza", "");
  	   		
  	   		setDisabled("nrDDDAutorizador", true);
  	   		setElementValue("nrDDDAutorizador", "");
  	   		
  	   		setDisabled("nrTelefoneAutorizador", true);
  	   		setElementValue("nrTelefoneAutorizador", "");
  	   		
  	   		setDisabled("nmResponsavelAutorizacao", true);
  	   		setElementValue("nmResponsavelAutorizacao", "");
  	   		  	   		
		}
	}

	function filialDataLoad_cb(data){
		filialRetirada_sgFilial_exactMatch_cb(data);
		
		if (data != undefined){
			if (getElementValue("consignatario.idCliente") != '' || getElementValue("destinatario.idCliente") != '')
				setEstadoTabDoc(data, "1");
		}
	}
	
	function filialPopup(data){
		if (data != undefined){
			if (getElementValue("consignatario.idCliente") != '' || getElementValue("destinatario.idCliente") != '')
				setEstadoTabDoc(data, "1");
		}
		
		return true;
	}
	
	function filialChange(obj){
		var r = filialRetirada_sgFilialOnChangeHandler();
		
		if (obj.value == '')
			setDisabledTabCustom("doc", true);
		
		return r;
	}

	// CONSIGNATARIO
	function consignatarioDataLoad_cb(data){
		var retorno = consignatario_pessoa_nrIdentificacao_exactMatch_cb(data);		
		setEstadoTabDoc(data, getElementValue("filialRetirada.idFilial"));
	
		return retorno;
	}

	function consignatarioPopup(data){
		setEstadoTabDoc(data, getElementValue("filialRetirada.idFilial"));				
		return true;
	}
	
	function consignatarioOnChange(obj){
		var retorno = consignatario_pessoa_nrIdentificacaoOnChangeHandler();
		if (obj.value == '' && getElementValue("destinatario.idCliente") == ''){
			setDisabledTabCustom("doc", true);
		}
		return retorno;
	}
	//-------------------------------------	
	// DESTINATARIO
	function destinatarioDataLoad_cb(data){
		var retorno = destinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
		setEstadoTabDoc(data, getElementValue("filialRetirada.idFilial"));		
	
		return retorno;
	}
	
	function destinatarioPopup(data){
		setEstadoTabDoc(data, getElementValue("filialRetirada.idFilial"));			
		return true;
	}

	function destinatarioOnChange(obj){
		var retorno = destinatario_pessoa_nrIdentificacaoOnChangeHandler();		
		if (obj.value == '' && getElementValue("consignatario.idCliente") == ''){
			setDisabledTabCustom("doc", true);
		}
		return retorno;
	}
	//-------------------------------------
	
	// REMETENTE
	function remetenteDataLoad_cb(data){
		var retorno = remetente_pessoa_nrIdentificacao_exactMatch_cb(data);
		return retorno;
	}
			
	function remetentePopup(data){
				
		return true;
	}	
		
	function remetenteOnChange(obj){
		var retorno = remetente_pessoa_nrIdentificacaoOnChangeHandler();		
		
		return retorno;
	}
	//-------------------------------------
		
	function setEstadoTabDoc(data, idFilial){
	
		if (data != undefined && idFilial != '')
			setDisabledTabCustom('doc', false);
	}
	
	function onChangeMeioTransporte(elem) {
		elem.value = elem.value.toUpperCase();
		return validate(elem);
	}
	
</script>