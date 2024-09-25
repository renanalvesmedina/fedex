<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.ocorrenciaClienteService">
	<adsm:form action="/vendas/manterOcorrenciasCliente" idProperty="idOcorrenciaCliente" onDataLoadCallBack="myDataLoad">
	    <adsm:hidden property="idFilial" serializable="false"/>
	    <adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
	    <adsm:hidden property="cliente.idCliente"/>
	    <adsm:textbox dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" labelWidth="15%" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
	     <adsm:lookup service="lms.entrega.ocorrenciaEntregaService.findLookup" 
	        dataType="integer" 
	 		property="ocorrenciaEntrega" 
	 		criteriaProperty="cdOcorrenciaEntrega"
	 		idProperty="idOcorrenciaEntrega"
	 		label="ocorrencia" 
	 		required="true"
	 		exactMatch="true" minLengthForAutoPopUpSearch="3"
	 		size="3" maxLength="3" 
	 		action="/entrega/manterOcorrenciasEntrega" labelWidth="15%" width="8%" >
			<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo"/>						
			<adsm:textbox dataType="text" size="70" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega" disabled="true" width="77%" />
		</adsm:lookup>
		<adsm:textbox property="dsOcorrenciaCliente" label="descricao" maxLength="60" dataType="text" size="70" required="true" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
	    	<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

</script>
