<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	function pageLoadCustom_cb(data,error) {		
		onPageLoad_cb(data,error);		
		findFilialUsuarioLogado();
	}
	 
	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.emitirMIRSemRegistroRecebimentoAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	// variaveis armazenarão temporiariamente informações da filial do usuário logado.
	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;
	
	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		populateFilial();
	}

	function populateFilial() {
		setElementValue("filialByIdFilialDestino.idFilial",idFilial);
		setElementValue("filialByIdFilialDestino.sgFilial",sgFilial);
		setElementValue("filialByIdFilialDestino.pessoa.nmFantasia",nmFilial);
	}
</script>	
<adsm:window onPageLoadCallBack="pageLoadCustom">
	<adsm:form action="/entrega/emitirMIRSemRegistroRecebimento">
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="filialByIdFilialOrigem.empresa.tpEmpresa" value="M"></adsm:hidden>
		
		<adsm:lookup dataType="text" property="filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupFilial"
    			action="/municipios/manterFiliais" onchange="return filialOrigemChange(this);"
    			label="filialOrigem" size="3" maxLength="3" labelWidth="20%" width="80%" exactMatch="true" onPopupSetValue="filialOrigemSetValue">

   			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />

   			<adsm:propertyMapping relatedProperty="mir.filial.idFilial" modelProperty="idFilial" />
    		<adsm:propertyMapping relatedProperty="mir.filial.sgFilial" modelProperty="sgFilial" />
         	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="true" />
	    </adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialByIdFilialDestino"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupFilial"
    			action="/municipios/manterFiliais"
    			label="filialDestino" size="3" maxLength="3" labelWidth="20%" width="80%" exactMatch="true" >

   			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />

         	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="true" />
	    </adsm:lookup>
		
		<adsm:combobox property="tpMir" domain="DM_TIPO_MIR" 
				label="tipoMir" labelWidth="20%" width="80%" />
		
		
		<adsm:lookup dataType="text" 
				property="mir.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupFilial"
				label="mir" size="3" maxLength="3" width="33%" labelWidth="20%" picker="false" serializable="false"
				action="/municipios/manterFiliais" onchange="return filialChange(this);" popupLabel="pesquisarFilial" >
				<adsm:propertyMapping relatedProperty="nmfantasia" modelProperty="pessoa.nmFantasia" />
				
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" 
	    				modelProperty="idFilial" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" 
						modelProperty="sgFilial" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 
						modelProperty="pessoa.nmFantasia" />
				
			<adsm:lookup dataType="integer" property="mir"
					idProperty="idMir" criteriaProperty="nrMir"
	    			service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupMir"
	    			action="/entrega/manterMemorandosInternosResposta"
	    			size="8" maxLength="8" exactMatch="true" mask="00000000" disabled="true" >
	    		<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" 
	    				modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
					
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" 
	    				modelProperty="filialByIdFilialDestino.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" 
						modelProperty="filialByIdFilialDestino.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
						modelProperty="filialByIdFilialDestino.pessoa.nmFantasia" inlineQuery="false" />
					
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" blankFill="false"
	    				modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" blankFill="false"
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" blankFill="false"
	    				modelProperty="filialByIdFilialDestino.idFilial" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" blankFill="false"
						modelProperty="filialByIdFilialDestino.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" blankFill="false"
						modelProperty="filialByIdFilialDestino.pessoa.nmFantasia" inlineQuery="false" />	
	    	<adsm:hidden property="nmfantasia"></adsm:hidden>		
		    </adsm:lookup>
	    </adsm:lookup>
	            
        
        <adsm:lookup dataType="text" property="clienteOrigem" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupCliente" 
				label="remetente" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="clienteOrigem.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="clienteOrigem.pessoa.nrItentificacaoV" modelProperty="pessoa.nrIdentificacao" />
			
			<adsm:textbox dataType="text" property="clienteOrigem.pessoa.nmPessoa" 
					size="30" disabled="true" serializable="true" />
		</adsm:lookup>
		<adsm:hidden property="clienteOrigem.pessoa.nrItentificacaoV" serializable="true" />
		
		<adsm:lookup dataType="text" property="clienteDestino" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupCliente" 
				label="destinatario" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="clienteDestino.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="clienteDestino.pessoa.nrItentificacaoV" modelProperty="pessoa.nrIdentificacao" />
			
			<adsm:textbox dataType="text" property="clienteDestino.pessoa.nmPessoa" 
					size="30" disabled="true" serializable="true" />
		</adsm:lookup>
		<adsm:hidden property="clienteDestino.pessoa.nrItentificacaoV" serializable="true" />
		
        
		<adsm:lookup dataType="text" property="usuarioByIdUsuarioCriacao" idProperty="idUsuario" criteriaProperty="nrMatricula"
			     service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupUsuarioFuncionario" 
			     action="/configuracoes/consultarFuncionariosView"
			     label="funcionarioOrigem" size="16" maxLength="16" labelWidth="20%" width="80%" >
						
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" 
					modelProperty="filial.sgFilial" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
			
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioCriacao.nmUsuario" modelProperty="nmUsuario"/>				
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioCriacao.nmUsuario" 
					size="30" maxLength="45" disabled="true" serializable="true"/>
		</adsm:lookup>
        
        <adsm:lookup dataType="text" property="usuarioByIdUsuarioRecebimento" idProperty="idUsuario" criteriaProperty="nrMatricula"
			     service="lms.entrega.emitirMIRSemRegistroRecebimentoAction.findLookupUsuarioFuncionario" 
			     action="/configuracoes/consultarFuncionariosView"
			     label="funcionarioDestino" size="16" maxLength="16" labelWidth="20%" width="80%" >
						
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" 
					modelProperty="filial.sgFilial" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
			
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioRecebimento.nmUsuario" modelProperty="nmUsuario"/>				
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioRecebimento.nmUsuario" 
					size="30" maxLength="45" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" 
				domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"
				labelWidth="20%" width="80%" />

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirMIRSemRegistroRecebimentoAction" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	
	document.getElementById("filialByIdFilialOrigem.sgFilial").serializable = true;
	document.getElementById("filialByIdFilialDestino.sgFilial").serializable = true;
	document.getElementById("mir.nrMir").serializable = true;
	
	
	function filialChange(elem) {
		setDisabled("mir.idMir",elem.value == "");
		resetValue("mir.idMir");
		return mir_filial_sgFilialOnChangeHandler();
	}
	
	function filialOrigemChange(elem) {
		setDisabled("mir.idMir",elem.value == "");
		resetValue("mir.idMir");
		return filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	function filialOrigemSetValue() {
		setDisabled("mir.idMir",false);
		resetValue("mir.idMir");
	}
	
	function validateTab() {
		if (getElementValue("filialByIdFilialOrigem.idFilial") == "" &&
				getElementValue("filialByIdFilialDestino.idFilial") == "" &&
				getElementValue("mir.idMir") == "") {
			alert(i18NLabel.getLabel("LMS-00013") + " " +
					document.getElementById("filialByIdFilialOrigem.idFilial").label + ", " +
					document.getElementById("filialByIdFilialDestino.idFilial").label + ", " +
					document.getElementById("mir.idMir").label + ".");
			return false;
		}
		
		return true;
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateFilial();
			setDisabled("mir.idMir",true);
		}
	}
	
//-->
</script>