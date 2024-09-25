<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.ocorrenciaClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterOcorrenciasCliente">
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
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
	 		size="3" maxLength="3" 
	 		exactMatch="true" minLengthForAutoPopUpSearch="3"
	 		action="/entrega/manterOcorrenciasEntrega" labelWidth="15%" width="8%" >
			<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
			<adsm:textbox dataType="text" size="70" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega"  disabled="true" width="77%" />
		</adsm:lookup>                                                                         
		<adsm:textbox property="dsOcorrenciaCliente" label="descricao" maxLength="60" dataType="text" size="70" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ocorrencias"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idOcorrenciaCliente" property="ocorrencias" defaultOrder="ocorrenciaEntrega_.cdOcorrenciaEntrega,ocorrenciaEntrega_.dsOcorrenciaEntrega" selectionMode="check" gridHeight="200" unique="true" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow">
		<adsm:gridColumn title="ocorrencia" property="codigoDescricaoEntrega" width="40%" />
		<adsm:gridColumn title="descricao" property="dsOcorrenciaCliente" width="50%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.municipios.ocorrenciaClienteService.validatePermissao","validarPermissoes",data);
		xmit({serviceDataObjects:[sdo]});
	}                                      
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
	
	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}

</script>
