<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contratacaoveiculos.manterCheckListAction" onPageLoadCallBack="carregaDados" onPageLoad="pageLoadCheckList">
	<adsm:form action="/contratacaoVeiculos/manterCheckList" idProperty="idChecklistMeioTransporte"
			onDataLoadCallBack="desabilitaTodosCampos"
			service="lms.contratacaoveiculos.manterCheckListAction.findByIdCustom" >
		<adsm:hidden property="isDataAtual"/>
        <adsm:lookup
	        action="/municipios/manterFiliais" onchange="return changeFilial();"
	        dataType="text" 
	        property="filial" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.contratacaoveiculos.manterCheckListAction.findLookupFilial" 
	        label="filial" labelWidth="19%" size="3" width="81%" required="true" maxLength="3" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
		

		<adsm:textbox maxLength="10" dataType="integer" property="nrChecklist" label="numero" size="20" width="81%" labelWidth="19%" disabled="true" mask="0000000000"/>

        <adsm:hidden property="situacaoSolicitacao" value="AP"/>
        
        <%--*******************************lookup de solicitação de contratação****************************--%>
        <adsm:lookup 
		service="lms.contratacaoveiculos.manterCheckListAction.findLookupFilial" 
		onchange="return sgFilialOnChangeHandler();"
		onDataLoadCallBack="disableNrSolicitacao"
		dataType="text" 
		property="solicitacaoContratacao.filial" 
		idProperty="idFilial"
		criteriaProperty="sgFilial" 
		label="solicitacaoContratacao" size="3"  
		action="/municipios/manterFiliais" 
		labelWidth="19%" width="31%" disabled="true" maxLength="3"  picker="false" >
		<adsm:lookup 
		onPopupSetValue="verifyChecklistByPopPup"
		onDataLoadCallBack="verifyChecklistByIdSolicitacao"
		service="lms.contratacaoveiculos.manterCheckListAction.findLookupSolicitacaoContratacao" 
		dataType="integer" 
		property="solicitacaoContratacao" 
		idProperty="idSolicitacaoContratacao"
		criteriaProperty="nrSolicitacaoContratacao" 
		size="10" 
		action="/contratacaoVeiculos/manterSolicitacoesContratacao" 
		required="true" disabled="false" maxLength="10" picker="true" mask="0000000000">
		  <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		  <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
		  <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
		  	  
		  <adsm:propertyMapping criteriaProperty="situacaoSolicitacao" modelProperty="tpSituacaoContratacao"/>
		  
		  <adsm:propertyMapping relatedProperty="tpSolicitacaoContratacao.description" modelProperty="tpSolicitacaoContratacao.description"/>
		  <adsm:propertyMapping relatedProperty="rota.dsRota" modelProperty="rota.dsRota"  />
		  <adsm:propertyMapping relatedProperty="tipoMeioTransporte.tpMeioTransporte.description" modelProperty="tipoMeioTransporte.tpMeioTransporte.description"/>
		  <adsm:propertyMapping relatedProperty="tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="tipoMeioTransporte.dsTipoMeioTransporte"/>
		  <adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="tipoMeioTransporte.idTipoMeioTransporte"/>
		  <adsm:propertyMapping relatedProperty="solicitacaoContratacao.nrIdentificacaoMeioTransp" modelProperty="nrIdentificacaoMeioTransp"/>
		  <adsm:propertyMapping relatedProperty="solicitacaoContratacao.nrIdentificacaoSemiReboque" modelProperty="nrIdentificacaoSemiReboque"/>
		  
		  <adsm:propertyMapping blankFill="false" relatedProperty="solicitacaoContratacao.idSolicitacaoContratacao" modelProperty="idSolicitacaoContratacao"/>
		  
		  <adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
		  <adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
		  <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
		  <adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
		  
		  <adsm:propertyMapping relatedProperty="idTipoMeioTransporteSemiReboque" modelProperty="tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>	
		  
		</adsm:lookup>
		<adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia"/>
		</adsm:lookup>
		
		
		<%--fim lookup --%>
		
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte"/>
		<adsm:hidden property="idTipoMeioTransporteSemiReboque"/>
		
		<adsm:textbox 
			dataType="text" 
			property="tpSolicitacaoContratacao.description" 
			label="tipoSolicitacao" size="20" width="31%" labelWidth="19%" disabled="true" serializable="false"/>

		<adsm:textbox 
			dataType="text" 
			property="rota.dsRota" 
			label="rota" size="20" width="78%" labelWidth="19%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" 
			property="tipoMeioTransporte.tpMeioTransporte.description" 
			label="modalidade" size="20" width="31%" labelWidth="19%" disabled="true" serializable="false"/>
		
		<adsm:textbox 
			dataType="text" 
			property="tipoMeioTransporte.dsTipoMeioTransporte" 
			label="tipoMeioTransporte" size="20" width="31%" labelWidth="19%" disabled="true" serializable="false"/>

		
		<adsm:textbox 
		dataType="text" 
		property="nrFrotaMT" 
		label="meioTransporte" 
		size="6" labelWidth="19%" disabled="true" width="31%" cellStyle="vertical-align=bottom;" serializable="false">
		<adsm:textbox 
		dataType="text" 
		property="solicitacaoContratacao.nrIdentificacaoMeioTransp" 
		size="9" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:textbox 
		dataType="text" 
		property="nrFrotaSR" 
		label="semiReboque" size="6" labelWidth="19%" disabled="true" width="31%" cellStyle="vertical-align=bottom;" serializable="false">
		<adsm:textbox 
		dataType="text" 
		property="solicitacaoContratacao.nrIdentificacaoSemiReboque" 
		size="9" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		
		 
		
		<adsm:section caption="primeiroMotorista"/>
		
			<adsm:complement label="identificacao" labelWidth="19%" width="31%" required="true">
			   
				<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false" property="pessoaByIdPrimeiroMotorista.tpIdentificacao" onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:document.getElementById('pessoaByIdPrimeiroMotorista.tpIdentificacao'), numberElement:document.getElementById('pessoaByIdPrimeiroMotorista.idPessoa'), tabCmd:'cad'});"/>
							
				
		        <adsm:lookup definition="IDENTIFICACAO_PESSOA" required="false" disabled="false" property="pessoaByIdPrimeiroMotorista">
		        	<adsm:propertyMapping relatedProperty="pessoaByIdPrimeiroMotorista.nmPessoa" modelProperty="nmPessoa"/>
		        	<adsm:propertyMapping criteriaProperty="pessoaByIdPrimeiroMotorista.tpIdentificacao" modelProperty="tpIdentificacao"/>
		        </adsm:lookup>
		        
				<adsm:hidden property="pessoaByIdPrimeiroMotorista.tpPessoa" value="F" />
				<adsm:hidden property="pessoaByIdPrimeiroMotorista.dsEmail" serializable="false"/>
			</adsm:complement>
			<adsm:textbox required="true" dataType="text" property="pessoaByIdPrimeiroMotorista.nmPessoa" label="motorista" size="30" labelWidth="19%" width="28%" maxLength="50"/>
			
		<adsm:section caption="segundoMotorista"/>
		
			<adsm:complement label="identificacao" labelWidth="19%" width="31%" >
				 
				<adsm:combobox  definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false" 
				property="pessoaByIdSegundoMotorista.tpIdentificacao" 
				onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:document.getElementById('pessoaByIdSegundoMotorista.tpIdentificacao'), numberElement:document.getElementById('pessoaByIdSegundoMotorista.idPessoa'), tabCmd:'cad'});"/>		
		        
		        <adsm:lookup definition="IDENTIFICACAO_PESSOA" required="false" property="pessoaByIdSegundoMotorista" disabled="false" >
		        		<adsm:propertyMapping relatedProperty="pessoaByIdSegundoMotorista.nmPessoa" modelProperty="nmPessoa"/>
		        		<adsm:propertyMapping criteriaProperty="pessoaByIdSegundoMotorista.tpIdentificacao" modelProperty="tpIdentificacao"/>
		        </adsm:lookup>
		        
		       
		       <adsm:hidden property="pessoaByIdSegundoMotorista.tpPessoa" value="F" serializable="false" />
			   <adsm:hidden property="pessoaByIdSegundoMotorista.dsEmail" serializable="false"/>
		        	
		    </adsm:complement>
		    <adsm:hidden property="labelPessoa"/>
			<adsm:textbox dataType="text" property="pessoaByIdSegundoMotorista.nmPessoa" label="motorista" size="30" labelWidth="19%" width="31%" maxLength="50"/>
			
		<adsm:section caption="informacoesComplementares"/>
		<adsm:hidden property="usuario.idUsuario"/>	
		<adsm:textbox dataType="text" property="usuario.vfuncionario.nrMatricula" label="registradoPor" size="9" labelWidth="19%" disabled="true" width="9%"/>
		<adsm:textbox dataType="text" property="usuario.vfuncionario.nmFuncionario" size="29" disabled="true" width="71%"/>
		
		<adsm:textbox dataType="text" property="tpSituacao.description" label="aprovadoCheckList" labelWidth="19%" width="31%" disabled="true"/>
		
		<adsm:textbox dataType="JTDate" property="dtRealizacao" label="dataRealizacao" size="20" labelWidth="19%" disabled="true" width="31%" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="checklistItemMeioTransporte" id="checklistItemMeioTransporte"  onclick="showModalDialog('contratacaoVeiculos/manterCheckList.do?cmd=meioTransporte',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:520px;');" />
			<adsm:button caption="checklistItemMotorista" id="checklistItemMotorista" onclick="showModalDialog('contratacaoVeiculos/manterCheckList.do?cmd=motorista',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:500px;');" />
			<adsm:storeButton service="lms.contratacaoveiculos.manterCheckListAction.store" callbackProperty="verificaSituacaoChecklist" id="botaoSalvar"/>
			<adsm:newButton id="botaoLimpar"/>
			<adsm:button caption="excluir" buttonType="removeButton" id="botaoExcluir" onclick="removeButtonScript('lms.contratacaoveiculos.manterCheckListAction.removeById', 'removeById', 'idChecklistMeioTransporte', this.document)"/>
		</adsm:buttonBar>
		<script>
			var lms26053 = "<adsm:label key="LMS-26053"/>";
		</script>
	</adsm:form>
</adsm:window>
<script>

function changeFilial(){
	return filial_sgFilialOnChangeHandler();
 }

 
function carregaDados_cb(data, exception){
	onPageLoad_cb(data,exception);
	findInfUsuarioLogado();
	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoaByIdPrimeiroMotorista.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoaByIdPrimeiroMotorista.tpIdentificacao'), numberElement:document.getElementById('pessoaByIdPrimeiroMotorista.nrIdentificacao'), tabCmd:'cad'})
	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoaByIdSegundoMotorista.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoaByIdSegundoMotorista.tpIdentificacao'), numberElement:document.getElementById('pessoaByIdSegundoMotorista.nrIdentificacao'), tabCmd:'cad'})
	
}

function verifyChecklistByPopPup(data){
	if(data != undefined){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findCheckListByIdSolicitacao", "resultVerifyChecklistPopPup", {idSolicitacao:getNestedBeanPropertyValue(data,"idSolicitacaoContratacao")}));
	  	xmit();
	  	
  	}
}

function updateStatus(status){
	setElementValue("tpSituacao.description", status);
	
}

function removeById_cb(data,exception){
    if(exception != null){
    	alert(exception); 
    	return;
    }else{
    	limpar();
	}
}

function desabilitaTodosCampos_cb(data,exception){
	onDataLoad_cb(data,exception);
	var nrFrotaMT = getNestedBeanPropertyValue(data,"nrFrotaMT");
	if(nrFrotaMT != null){
		setElementValue("nrFrotaMT",nrFrotaMT);
	}
	var nrFrotaSR = getNestedBeanPropertyValue(data,"nrFrotaSR");
	if(nrFrotaSR != null){
		setElementValue("nrFrotaSR",nrFrotaSR);
	}
	
	var idPM = getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.idPessoa");
 	var nmPM = getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.nmPessoa");
 	var nrIdentificacaoPM =getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.nrIdentificacao");
 	var tpIdentificacaoPM =getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.tpIdentificacao");
 	if (idPM != null){
 		setElementValue("pessoaByIdPrimeiroMotorista.tpIdentificacao",tpIdentificacaoPM);
 		setElementValue("pessoaByIdPrimeiroMotorista.idPessoa",idPM);
 		setElementValue("pessoaByIdPrimeiroMotorista.nmPessoa",nmPM);
 		setElementValue("pessoaByIdPrimeiroMotorista.nrIdentificacao",nrIdentificacaoPM);
 		
 	}
 	
 	var idSM = getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.idPessoa");
 	var nmSM = getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.nmPessoa");
 	var nrIdentificacaoSM =getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.nrIdentificacao");
 	var tpIdentificacaoSM =getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.tpIdentificacao");
 	if (idSM != null){
 		setElementValue("pessoaByIdSegundoMotorista.tpIdentificacao",tpIdentificacaoSM);
 		setElementValue("pessoaByIdSegundoMotorista.idPessoa",idSM);
 		setElementValue("pessoaByIdSegundoMotorista.nmPessoa",nmSM);
 		setElementValue("pessoaByIdSegundoMotorista.nrIdentificacao",nrIdentificacaoSM);
 			
 	}
	desabilitaCampos();
}
 
 document.getElementById("pessoaByIdPrimeiroMotorista.nrIdentificacao").serializable= true;
 document.getElementById("pessoaByIdSegundoMotorista.nrIdentificacao").serializable= true;
 
 function initWindow(eventObj){
 	if(eventObj.name == 'tab_click'){
 		habilitaCampos();
		findInfUsuarioLogado();
		disableNrSolicitacao(false);
		
 	}
 	if(eventObj.name == 'newButton_click'){
 		habilitaCampos();
 		findInfUsuarioLogado();
 		disableNrSolicitacao(false);
 		setDisabled("checklistItemMeioTransporte",true);
		setDisabled("checklistItemMotorista",true);
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao",false);
 		
 	}	
 }
 
function desabilitaCampos(){

	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao",true);
	setDisabled("solicitacaoContratacao.idSolicitacaoContratacao",true);
	
	setDisabled("pessoaByIdPrimeiroMotorista.tpIdentificacao",true);
	setDisabled("pessoaByIdPrimeiroMotorista.idPessoa",true);
	setDisabled("pessoaByIdPrimeiroMotorista.nmPessoa",true);
	
	setDisabled("pessoaByIdSegundoMotorista.tpIdentificacao",true);
	setDisabled("pessoaByIdSegundoMotorista.idPessoa",true);
	setDisabled("pessoaByIdSegundoMotorista.nmPessoa",true);
	
	setDisabled("botaoSalvar",true);
	setFocus("botaoLimpar",false);
}
 
 function habilitaCampos(){
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao",true);
	setDisabled("pessoaByIdPrimeiroMotorista.tpIdentificacao",false);
	//setDisabled("pessoaByIdPrimeiroMotorista.idPessoa",false);
	setDisabled("pessoaByIdPrimeiroMotorista.nmPessoa",false);
	setDisabled("pessoaByIdSegundoMotorista.tpIdentificacao",false);
	//setDisabled("pessoaByIdSegundoMotorista.idPessoa",false);
	setDisabled("pessoaByIdSegundoMotorista.nmPessoa",false);
	setDisabled("botaoSalvar",false);
	setDisabled("botaoLimpar",false);
	setDisabled("checklistItemMeioTransporte",true);
	setDisabled("checklistItemMotorista",true);
 }
 
 function limpar(){
 	newButtonScript();
 	habilitaCampos();
	findInfUsuarioLogado();
	setDisabled("checklistItemMeioTransporte",true);
	setDisabled("checklistItemMotorista",true);
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao",false);
}

 function findInfUsuarioLogado(){
 	 	_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findInformacoesUsuarioLogado", "setaInformacoesUsuarioLogado", ""));
	  	xmit();
	  	setFocus(document.getElementById("filial.sgFilial"));
 }
 
 function setaInformacoesUsuarioLogado_cb(data,exception){
  		setElementValue("filial.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));
 		setElementValue("filial.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));
 		setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
 		
 		if(document.getElementById("solicitacaoContratacao.filial.idFilial").masterLink == undefined){
	 		setElementValue("solicitacaoContratacao.filial.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));
	 		setElementValue("solicitacaoContratacao.filial.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));
	 		setElementValue("solicitacaoContratacao.filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
 		}
 		
 		setElementValue("dtRealizacao",getNestedBeanPropertyValue(data,"dtRealizacao"));
 		
 		setElementValue("usuario.vfuncionario.nrMatricula",getNestedBeanPropertyValue(data,"usuario.nrMatricula"));
 		setElementValue("usuario.vfuncionario.nmFuncionario",getNestedBeanPropertyValue(data,"usuario.nmFuncionario"));
 		setElementValue("usuario.idUsuario",getNestedBeanPropertyValue(data,"usuario.idUsuario"));
 		
 }
 
 function verifyChecklistByIdSolicitacao_cb(data){
 	 solicitacaoContratacao_nrSolicitacaoContratacao_exactMatch_cb(data);
 	 if(data != undefined){
 	 
	 	_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findCheckListByIdSolicitacao", "resultVerifyChecklist", {idSolicitacao:getElementValue("solicitacaoContratacao.idSolicitacaoContratacao")}));
	  	xmit();
	    
  	}
 }
 
  function verifyChecklistByIdSolicitacaoFilial_cb(data){
 	 solicitacaoContratacao_filial_sgFilial_exactMatch_cb(data);
 	 if(data != undefined){
 	 		if(data.length == 1){
	 	 	 	_serviceDataObjects = new Array();
			   	addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findCheckListByIdSolicitacao", "resultVerifyChecklist", {idSolicitacao:getElementValue("solicitacaoContratacao.idSolicitacaoContratacao")}));
			  	xmit();
	  	    }
  	}
 }
 
 function resultVerifyChecklist_cb(data, exception){
 	if(exception != undefined){
 		alert(exception);
 		deletaCamposSolicitacaoContratacao();
 		setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
 	}else{
 			var data = new Array();
 			var idSolicitacaoContratacao = getElementValue("solicitacaoContratacao.idSolicitacaoContratacao");
 			var nrIdentificacaoMT = getElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp");
 			var nrIdentificacaoSR = getElementValue("solicitacaoContratacao.nrIdentificacaoSemiReboque");
 			
 			setNestedBeanPropertyValue(data,"idSolicitacaoContratacao",idSolicitacaoContratacao);
 			setNestedBeanPropertyValue(data,"nrIdentificacaoMT",nrIdentificacaoMT);
 			setNestedBeanPropertyValue(data,"nrIdentificacaoSR",nrIdentificacaoSR);
 			
 			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findRotaByIdSolicitacaoContratacao", "onDataLoadRota", data));
			xmit();
		
 	}
 }
 
 function resultVerifyChecklistPopPup_cb(data, exception){
 	if(exception != undefined){
 		alert(exception);
 		deletaCamposSolicitacaoContratacao();
 		setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
 	}else{
 			var data = new Array();
 			var idSolicitacaoContratacao = getElementValue("solicitacaoContratacao.idSolicitacaoContratacao");
 			var nrIdentificacaoMT = getElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp");
 			var nrIdentificacaoSR = getElementValue("solicitacaoContratacao.nrIdentificacaoSemiReboque");
 			
 			setNestedBeanPropertyValue(data,"idSolicitacaoContratacao",idSolicitacaoContratacao);
 			setNestedBeanPropertyValue(data,"nrIdentificacaoMT",nrIdentificacaoMT);
 			setNestedBeanPropertyValue(data,"nrIdentificacaoSR",nrIdentificacaoSR);
 			
 			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findRotaByIdSolicitacaoContratacao", "onDataLoadRotaPopPup", data));
			xmit();
		
 	}
 }
 
 function onDataLoadRotaPopPup_cb(data,exception){
 	if(data!= undefined){
 		if(getNestedBeanPropertyValue(data,"dsRota")!= "")
 			setElementValue("rota.dsRota", getNestedBeanPropertyValue(data,"_value"));
 		if(getNestedBeanPropertyValue(data,"nrFrotaMT")!= "")
 			setElementValue("nrFrotaMT", getNestedBeanPropertyValue(data,"nrFrotaMT"));
 		if(getNestedBeanPropertyValue(data,"nrFrotaSR")!= "")
 			setElementValue("nrFrotaSR", getNestedBeanPropertyValue(data,"nrFrotaSR"));	
 	}		
 }
 
 function onDataLoadRota_cb(data,exception){
 	if(data!= undefined)
 		if(getNestedBeanPropertyValue(data,"nrFrotaMT")!= "")
 			setElementValue("nrFrotaMT", getNestedBeanPropertyValue(data,"nrFrotaMT"));
 		if(getNestedBeanPropertyValue(data,"nrFrotaSR")!= "")
 			setElementValue("nrFrotaSR", getNestedBeanPropertyValue(data,"nrFrotaSR"));		
 }
 
 function deletaCamposSolicitacaoContratacao(){
 		setElementValue("solicitacaoContratacao.idSolicitacaoContratacao","");
 		setElementValue("solicitacaoContratacao.nrSolicitacaoContratacao","");
 		setElementValue("tpSolicitacaoContratacao.description","");
 		setElementValue("rota.dsRota","");
 		setElementValue("tipoMeioTransporte.tpMeioTransporte.description","");
 		setElementValue("tipoMeioTransporte.dsTipoMeioTransporte","");
 		setElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp","");
 		setElementValue("solicitacaoContratacao.nrIdentificacaoSemiReboque","");
 		
 }
 
 function verificaSituacaoChecklist_cb(data, exception, key){
 	store_cb(data,exception,key);
 	if(exception == undefined)
 		desabilitaCampos();
 			
 	var tpSituacao = getNestedBeanPropertyValue(data,"tpSituacao.value");
 	
 	var idPM = getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.idPessoa");
 	var nmPM = getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.nmPessoa");
 	var nrIdentificacaoPM =getNestedBeanPropertyValue(data,"pessoaByIdPrimeiroMotorista.nrIdentificacaoFormatado");
 	if (idPM != null){
 		setElementValue("pessoaByIdPrimeiroMotorista.idPessoa",idPM);
 		setElementValue("pessoaByIdPrimeiroMotorista.nmPessoa",nmPM);
 		setElementValue("pessoaByIdPrimeiroMotorista.nrIdentificacao",nrIdentificacaoPM);
 	}
 	var idSM = getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.idPessoa");
 	var nmSM = getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.nmPessoa");
 	var nrIdentificacaoSM =getNestedBeanPropertyValue(data,"pessoaByIdSegundoMotorista.nrIdentificacaoFormatado");
 	if (idSM != null){
 		setElementValue("pessoaByIdSegundoMotorista.idPessoa",idSM);
 		setElementValue("pessoaByIdSegundoMotorista.nmPessoa",nmSM);
 		setElementValue("pessoaByIdSegundoMotorista.nrIdentificacao",nrIdentificacaoSM);
 	}
 	
 	setElementValue("tpSituacao.description",getNestedBeanPropertyValue(data,"tpSituacao.description"));
 	
 	if(tpSituacao == "I"){
 		setElementValue("isDataAtual","true");
 		alert(lms26053);
 		setFocus(document.getElementById("botaoLimpar"),false);
 		
 	}	
}

function changeSolicitacaoFilial(){
	if(getElementValue("solicitacaoContratacao.filial.sgFilial") == ''){
		deletaCamposSolicitacaoContratacao();
	}
	return solicitacaoContratacao_filial_sgFilialOnChangeHandler();
}
 
function deletaCamposSolicitacaoContratacao(){
		setElementValue("solicitacaoContratacao.idSolicitacaoContratacao","");
 		setElementValue("solicitacaoContratacao.nrSolicitacaoContratacao","");
 		setElementValue("tpSolicitacaoContratacao.description","");
 		setElementValue("rota.dsRota","");
 		setElementValue("tipoMeioTransporte.dsTipoMeioTransporte","");
 		setElementValue("tipoMeioTransporte.idTipoMeioTransporte","");
 		setElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp","");
 		setElementValue("solicitacaoContratacao.nrIdentificacaoSemiReboque","");
 		setElementValue("tipoMeioTransporte.tpMeioTransporte.description","");
 		setElementValue("solicitacaoContratacao.idSolicitacaoContratacao","");
} 

/**************************************************************************************************************************
	 * Funções referentes ao comportamento da lookup de controle de carga
	 **************************************************************************************************************************/	
	function sgFilialOnChangeHandler() {
		if (getElementValue("solicitacaoContratacao.filial.sgFilial")=="") {
			disableNrSolicitacao(true);
			deletaCamposSolicitacaoContratacao();
		} else {
			disableNrSolicitacao(false);
		}
		return lookupChange({e:document.forms[0].elements["solicitacaoContratacao.filial.idFilial"]});
	}

	function disableNrSolicitacao(disable) {
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", disable);
	}
	
	function disableNrSolicitacao_cb(data, error) {
		if (data.length==0) disableNrSolicitacao(false);
		return lookupExactMatch({e:document.getElementById("solicitacaoContratacao.filial.idFilial"), data:data});
	}
	
	function pageLoadCheckList() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoaByIdPrimeiroMotorista.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoaByIdPrimeiroMotorista.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoaByIdPrimeiroMotorista.idPessoa")});
      			   
      	initPessoaWidget({tpTipoElement:document.getElementById("pessoaByIdSegundoMotorista.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoaByIdSegundoMotorista.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoaByIdSegundoMotorista.idPessoa")});		   
	}

</script>













