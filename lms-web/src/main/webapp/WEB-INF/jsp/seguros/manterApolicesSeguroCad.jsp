<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterApolicesSeguroAction">
	<adsm:form action="/seguros/manterApolicesSeguro" idProperty="idApoliceSeguro" onDataLoadCallBack="loadDataReguladora">
	
		<!-- LMS-6146 -->
        <adsm:lookup size="20" maxLength="30" width="85%"
					 idProperty="idSegurado"
					 property="segurado" 
					 criteriaProperty="nrIdentificacao"
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 action="/configuracoes/manterPessoas" 
					 service="lms.seguros.manterApolicesSeguroAction.findLookupPessoa" 
					 dataType="text" 
					 exactMatch="true"
					 label="segurado"
					 required="true">
			<adsm:propertyMapping relatedProperty="segurado.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:propertyMapping relatedProperty="segurado.idPessoa" modelProperty="idPessoa"/>
			<adsm:textbox size="50" maxLength="50" disabled="true" dataType="text" property="segurado.nmPessoa"/>
		</adsm:lookup>
		<adsm:hidden property="segurado.idPessoa" serializable="true"/>
	
		<adsm:textbox dataType="text" property="nrApolice" label="numeroApolice" size="60" width="85%" maxLength="60" required="true"/>
		
		<adsm:hidden property="reguladoraSeguro.pessoa.nmPessoa" />
		<adsm:combobox property="reguladoraSeguro.idReguladora" optionProperty="idReguladora" optionLabelProperty="pessoa.nmPessoa" 
					   service="lms.seguros.manterApolicesSeguroAction.findReguladoraOrderByNmPessoa"  onchange="checkValueReguladoraOnChange()" onlyActiveValues="true"
					   label="reguladora" width="85%" required="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>
		</adsm:combobox>
					   
		<adsm:combobox property="seguradora.idSeguradora" optionProperty="seguradora.idSeguradora" optionLabelProperty="seguradora.pessoa.nmPessoa" 
					   service="lms.seguros.manterApolicesSeguroAction.findReguladoraSeguradoraOrderByNmPessoa" onlyActiveValues="true"
					   label="seguradora" width="85%" required="true">	
			<adsm:propertyMapping modelProperty="reguladoraSeguro.idReguladora" criteriaProperty="reguladoraSeguro.idReguladora"/>
		</adsm:combobox>

		<adsm:range label="vigencia" width="35%" required="true">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="tipoSeguro.sgTipo" />
		<adsm:combobox property="tipoSeguro.idTipoSeguro" optionProperty="idTipoSeguro" optionLabelProperty="sgTipo"
					   service="lms.seguros.manterApolicesSeguroAction.findTipoSeguroOrderBySgTipo" onlyActiveValues="true"
					   label="tipoSeguro" width="35%" required="true" >
			<adsm:propertyMapping modelProperty="sgTipo" relatedProperty="tipoSeguro.sgTipo"/>
		</adsm:combobox>
		   
		<adsm:complement label="limiteValor" width="35%" required="true" separator="branco" > 
			<adsm:combobox property="moeda.idMoeda" 
						   service="lms.seguros.manterApolicesSeguroAction.findMoeda" 
						   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
						   boxWidth="100" onlyActiveValues="true"/>
				<adsm:textbox dataType="currency" 
							  property="vlLimiteApolice" 
							  mask="###,###,###,###,##0.00" minValue="0.01"
							  size="18" maxLength="15"/>
		</adsm:complement>
		
		<adsm:textbox dataType="currency" property="vlFranquia" label="vlFranquia" size="30" mask="###,###,###,###,##0.00" width="35%" maxLength="60" required="true" minValue="0.00"/>
		<adsm:textbox dataType="currency" property="vlPremio" label="valorPremio" size="39" mask="###,###,###,###,##0.00" width="35%" maxLength="60" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlPremioVencer" label="vlPremioVencer" size="30" mask="###,###,###,###,##0.00" width="35%" maxLength="60" disabled="true"/>
		
		<adsm:textarea 
			label="coberturas" 
			property="dsCobertura" 
			maxLength="1500" 
			columns="120" 
			rows="5" 
			labelWidth="15%" 
			width="87%" 
		/>
		
		<adsm:textarea 
			label="limites" 
			property="dsLimite" 
			maxLength="1500" 
			columns="120" 
			rows="5" 
			labelWidth="15%" 
			width="87%" 
		/>
		
		<adsm:textarea 
			label="dsFranquias" 
			property="dsFranquia" 
			maxLength="1500" 
			columns="120" 
			rows="5" 
			labelWidth="15%" 
			width="87%" 
		/>
		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" />
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		
		if (eventObj.name != "gridRow_click") {
			checkValueReguladora();
			
			if(getElementValue('idApoliceSeguro') !=''){
				findValoresDetalhamento(false);
			}
		} 
    }

	var oldData;
	var oldError;
	function loadDataReguladora_cb(data, error) {
		oldData = data;
		oldError = error;
		
		var beanValue = getNestedBeanPropertyValue(data, "reguladoraSeguro.idReguladora");
		
		var sdo = createServiceDataObject("lms.seguros.manterApolicesSeguroAction.findReguladoraSeguradoraOrderByNmPessoa", "loadDataSeguradora", {reguladoraSeguro:{idReguladora:beanValue}});
    	xmit({serviceDataObjects:[sdo]});

    }

	function loadDataSeguradora_cb(data, error){
		seguradora_idSeguradora_cb(data);
		onDataLoad_cb(oldData, oldError);		
		checkValueReguladora();
    	
    	findValoresDetalhamento(true);
	}

	function checkValueReguladoraOnChange() {
		//Permite que o elemento nao tenha o seu comporto default do 'onchange' sobre escrito.
		e = document.getElementById('reguladoraSeguro.idReguladora');
		comboboxChange({e:e});
		
		checkValueReguladora();
	}
	
	function checkValueReguladora(){
		if ( getElementValue('reguladoraSeguro.idReguladora') == "") {
			resetValue('seguradora.idSeguradora');
			setDisabled('seguradora.idSeguradora', true);
		} else {
			setDisabled('seguradora.idSeguradora', false);
		}
	}
	
	function findValoresDetalhamento(isGridClick){
		var data = new Array();
		data.idApoliceSeguro = getElementValue('idApoliceSeguro');
		data.isGridClick = isGridClick;
		
        var sdo = createServiceDataObject("lms.seguros.manterApolicesSeguroAction.findCalculaValorPremio","calculaValorPremio", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function calculaValorPremio_cb(data){
		setElementValue("vlPremio", getNestedBeanPropertyValue(data,"valorPremio"));
		setElementValue("vlPremioVencer", getNestedBeanPropertyValue(data,"vlPremioVencer"));
	}

</script>