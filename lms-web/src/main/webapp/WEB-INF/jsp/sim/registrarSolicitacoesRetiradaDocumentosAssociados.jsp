<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>

	function popupPageLoad_cb(){
		onPageLoad_cb();
	
		copyValuesFromCad();
	}
	
	function initWindow(evt){
		if (evt.name == 'cleanButton_click') {
			copyValuesFromCad();
		}
	}
	
	function copyValuesFromCad() {
		setDisabled("btnFechar", false);
	
		var campoIdCliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteDestinatario.idCliente");		
		var campoNmcliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa");		
		var campoNrIdentificacao = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		
		if (campoIdCliente.value != ''){
			setElementValue("destinatario.idCliente", campoIdCliente.value);
			setElementValue("destinatario.pessoa.nrIdentificacao", campoNrIdentificacao.value);
			setElementValue("destinatario.pessoa.nmPessoa", campoNmcliente.value);
			
			document.getElementById("destinatario.pessoa.nrIdentificacao").masterLink = "true";
			document.getElementById("destinatario.pessoa.nmPessoa").masterLink = "true";
			
			setDisabled("destinatario.idCliente", true);
		}

		campoIdCliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteConsignatario.idCliente");		
		campoNmcliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa");		
		campoNrIdentificacao = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao");
		
		if (campoIdCliente.value != ''){
			setElementValue("consignatario.idCliente", campoIdCliente.value);
			setElementValue("consignatario.pessoa.nrIdentificacao", campoNrIdentificacao.value);
			setElementValue("consignatario.pessoa.nmPessoa", campoNmcliente.value);
			setDisabled("consignatario.idCliente", true);
			
			document.getElementById("consignatario.pessoa.nrIdentificacao").masterLink = "true";
			document.getElementById("consignatario.pessoa.nmPessoa").masterLink = "true";
		}
				
		campoIdCliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteRemetente.idCliente");		
		campoNmcliente = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa");		
		campoNrIdentificacao = dialogArguments.document.getElementById("doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao");
		
		if (campoIdCliente.value != ''){
			setElementValue("remetente.idCliente", campoIdCliente.value);
			setElementValue("remetente.pessoa.nrIdentificacao", campoNrIdentificacao.value);
			setElementValue("remetente.pessoa.nmPessoa", campoNmcliente.value);
			
			document.getElementById("remetente.pessoa.nrIdentificacao").masterLink = "true";
			document.getElementById("remetente.pessoa.nmPessoa").masterLink = "true";
			
			setDisabled("remetente.idCliente", true);
		}
		
		setElementValue("filialRetirada.idFilial", dialogArguments.document.getElementById("idFilialRetirada").value);
		
		var idSolicitacaoRetirada = dialogArguments.document.getElementById("form_doc").document.getElementById("masterId").value;
		setElementValue("idSolicitacaoRetirada",idSolicitacaoRetirada);
	}
	
</script> 
<adsm:window title="branco" service="lms.sim.registrarSolicitacoesRetiradaAction" onPageLoadCallBack="popupPageLoad">
	
	<adsm:form action="/sim/registrarSolicitacoesRetirada">
	
		<adsm:section caption="adicionarDocumentosServicoTitulo"/>
		
		<adsm:label key="branco" width="100%" style="height:25px;border:none"/>
		
		<adsm:hidden property="filialRetirada.idFilial" />
		<adsm:hidden property="idSolicitacaoRetirada" />
		
		<adsm:lookup dataType="text" property="remetente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="remetente" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	
               
		<adsm:lookup dataType="text" property="destinatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="destinatario" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true">
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	 

		<adsm:lookup dataType="text" property="consignatario" idProperty="idCliente"  
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="consignatario" size="17" maxLength="20" labelWidth="20%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true"	>
			<adsm:propertyMapping relatedProperty="consignatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="consignatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>	
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="doctoServico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idDoctoServico" property="doctoServico" unique="true" scrollBars="vertical" gridHeight="250"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findPaginatedDoctoServico"	showPagging="false" onRowClick="gridClick">
				
		<adsm:gridColumn width="100" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="00000000"/>
		
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40" isDomain="true"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="70" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>		

		<adsm:gridColumn width="50" title="identificacao" property="tpIdentificacao" />
		<adsm:gridColumn width="120" title="" property="nrIdentificacao" align="right"/>
		<adsm:gridColumn width="150" title="remetente" property="nmPessoa" />
		<adsm:gridColumn width="100" title="priorizado" renderMode="image-check" property="blPrioridadeCarregamento" />
		
		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="onConfirmarClick()"/>
			<adsm:button caption="fechar" onclick="self.close()" id="btnFechar" />
		</adsm:buttonBar>    
		
	</adsm:grid>
	
</adsm:window>

<script>
	
	function gridClick(){
		return false;
	}
	
	function onConfirmarClick() {
		var data = doctoServicoGridDef.getSelectedIds();
		var idSolicitacaoRetirada = getElementValue("idSolicitacaoRetirada");
		
	//	var data = new Array();
		setNestedBeanPropertyValue(data,"idSolicitacaoRetirada",idSolicitacaoRetirada);
	//	setNestedBeanPropertyValue(data,"list",ids);
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesRetiradaAction.confirmarDocumentos",
				"confirmarDocumentos",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function confirmarDocumentos_cb(data,error) {
		
		dialogArguments.atualizaGrid();
		self.close();		
	}
</script>
