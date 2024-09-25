<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarFilialEmbarcadoraLogAction">
	<adsm:form action="/vendas/consultarFilialEmbarcadoraLog" >
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="cliente"
			labelWidth="10%"
			maxLength="20"
			property="cliente"
			service="lms.vendas.consultarFilialEmbarcadoraLogAction.findCliente"
			size="20"
			width="50%"
			required="true"
			onDataLoadCallBack="clienteDataLoad"
			onchange="return clienteChange()"
			onPopupSetValue="fillComboFiliais"
			>
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30" 
			/>
		</adsm:lookup>
		
		<adsm:combobox property="idFilialEmbarcadora"
			optionLabelProperty="sgFilial"
			optionProperty="idFilialEmbarcadora"
			autoLoad="false"
			label="filial"
			labelWidth="10%"
			width="30%"
			required="true"
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idFilialEmbarcadoraLog"
			selectionMode="none"
			width="100%"
			scrollBars="horizontal"
			onRowClick="rowClick"
			gridHeight="300"
			rows="14"
  	>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="nmPessoa" title="cliente" dataType="text" width="180"/>
		<adsm:gridColumn property="sgFilial" title="filial" dataType="text"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script>
function rowClick(){
	return false;
}

function clienteDataLoad_cb(data,error){
	if (error != undefined){
		alert(error);
	}else{
		if (cliente_pessoa_nrIdentificacao_exactMatch_cb(data)){
			fillComboFiliais(data[0])
		}		
	}
}

function fillComboFiliais(data){
	var sdo = createServiceDataObject("lms.vendas.consultarFilialEmbarcadoraLogAction.findComboFiliais", "findComboFiliais", data)
	xmit({serviceDataObjects:[sdo]});
}

function findComboFiliais_cb(data,error){
	if (error != undefined){
		alert(error);
	}else{
		idFilialEmbarcadora_cb(data);
	}
}

function clienteChange(){
	var change = cliente_pessoa_nrIdentificacaoOnChangeHandler();
	if (getElementValue("cliente.pessoa.nrIdentificacao")==""){
		document.getElementById("idFilialEmbarcadora").options.length = 1;
	}
	return change;
}
</script>