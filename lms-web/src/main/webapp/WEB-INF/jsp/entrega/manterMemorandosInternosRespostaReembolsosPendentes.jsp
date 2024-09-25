<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	
	function onPageLoadCustom() {
		var idMir = dialogArguments.document.getElementById("form_reem").document.getElementById("masterId").value;
		setElementValue("idMir",idMir);
		
		var tpMir = dialogArguments.document.getElementById("form_reem").document.getElementById("tpMir.value").value
		setElementValue("tpMir",tpMir);
		
		var idFilialOrigem = dialogArguments.document.getElementById("form_reem").document.getElementById("idFilialOrigem").value
		setElementValue("idFilialOrigem",idFilialOrigem);
		
		var idFilialDestino = dialogArguments.document.getElementById("form_reem").document.getElementById("idFilialDestino").value
		setElementValue("idFilialDestino",idFilialDestino);
				
		onPageLoad();
	}
	
//-->
</script>
<adsm:window title="branco" service="lms.entrega.manterMemorandosInternosRespostaAction"
		onPageLoad="onPageLoadCustom" >
	<adsm:form action="/entrega/manterMemorandosInternosResposta" >
		<adsm:section caption="listagemReembolsosPendentes"/>
		<adsm:label key="branco" width="100%" style="height:25px;border:none"/>
		
		
		<adsm:hidden property="idMir" />
		<adsm:hidden property="tpMir" />
		<adsm:hidden property="idFilialOrigem" />
		<adsm:hidden property="idFilialDestino" />
		
		
		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false" />
		
		<adsm:lookup dataType="text" property="clienteByIdClienteRemetente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupCliente" 
				label="remetente" size="17" maxLength="20" labelWidth="18%" width="82%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			
			<adsm:textbox dataType="text" property="clienteByIdClienteRemetente.pessoa.nmPessoa" size="30" disabled="true" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="clienteByIdClienteDestinatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupCliente" 
				label="destinatario" size="17" maxLength="20" labelWidth="18%" width="82%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			
			<adsm:textbox dataType="text" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" size="30" disabled="true" />
		</adsm:lookup>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="reciboReembolso" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="reciboReembolso" idProperty="idReciboReembolso"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findGridReembPendentes"
			showPagging="false" unique="true" scrollBars="both" gridHeight="272" onRowClick="gridClick" >
		
		
		<adsm:gridColumn title="documentoServico" 
					property="tpDocumentoServico" width="40" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="filial.sgFilial" width="60" />
			<adsm:gridColumn title="" property="nrDoctoServico"
					width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup> 

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico"
					property="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico" width="45" />
			<adsm:gridColumn title="" property="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial"
					width="45" />
			<adsm:gridColumn title="" property="doctoServicoByIdDoctoServReembolsado.nrDoctoServico"
					width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="identificacao" property="clienteByIdClienteRemetente.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" property="clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120" />
		<adsm:gridColumn title="remetente" property="clienteByIdClienteRemetente.pessoa.nmPessoa" width="140" />

		<adsm:gridColumn title="identificacao" property="clienteByIdClienteDestinatario.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" property="clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120" />
		<adsm:gridColumn title="destinatario" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" width="140" />

		<adsm:buttonBar >
			<adsm:button caption="confirmar" buttonType="removeButton" id="btnConfirmar" onclick="onConfirmarClick();" />
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>   
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--

	document.getElementById("idMir").masterLink = true;
	document.getElementById("tpMir").masterLink = true;
	document.getElementById("idFilialOrigem").masterLink = true;
	document.getElementById("idFilialDestino").masterLink = true;

	document.getElementById('btnFechar').property = ".closeButton";
	setDisabled('btnFechar',false);

	function onConfirmarClick() {
		var ids = reciboReembolsoGridDef.getSelectedIds();
		var idMir = getElementValue("idMir");
		
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",idMir);
		setNestedBeanPropertyValue(data,"reciboReembolso",ids);
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.confirmarPendenciasReem",
				"confirmarPendenciasReem",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function confirmarPendenciasReem_cb(data,error) {
		store_cb(data,error);
		dialogArguments.documentoMirGridDef.executeLastSearch(true);
		self.close();
	}
	
	function gridClick(){
		return false;
	}
	
//-->
</script>