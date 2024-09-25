<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterAcertoFilialRegionalClienteAction">
	
	<adsm:form action="/vendas/manterAcertoFilialRegionalCliente">

		<adsm:hidden property="tpAcesso" value="F" serializable="true"/>
		<adsm:hidden serializable="false" property="tpSituacaoPesquisa" value="A"/>

		<adsm:checkbox property="blAcertoFilialMunicipio" labelWidth="20%" label="lbAcertoFilialMunicipio" width="60*" onclick="onClickCheck(this);"/>
		

		<adsm:lookup property="municipio" criteriaProperty="nmMunicipio" idProperty="idMunicipio" dataType="text" 
					 service="lms.municipios.municipioService.findLookup" labelWidth="20%"
					 label="municipio" size="41" action="/municipios/manterMunicipios" disabled="true"
					 exactMatch="false" minLengthForAutoPopUpSearch="3" maxLength="60">

				<adsm:propertyMapping criteriaProperty="tpSituacaoPesquisa" modelProperty="tpSituacao"/>	 				
		 </adsm:lookup>

		<adsm:lookup
			label="filialAnterior"
			property="filialAnterior"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text" disabled="true"
			size="3"
			maxLength="3"
			labelWidth="20%"
			width="71%">
			<adsm:propertyMapping criteriaProperty="tpAcesso" relatedProperty="tpAcesso"/>
			<adsm:propertyMapping relatedProperty="filialAnterior.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialAnterior.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="filialAtual"
			property="filialAtual"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text" disabled="true"
			size="3"
			maxLength="3"
			labelWidth="20%"
			width="71%">
			<adsm:propertyMapping criteriaProperty="tpAcesso" relatedProperty="tpAcesso"/>
			<adsm:propertyMapping relatedProperty="filialAtual.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				property="filialAtual.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:checkbox property="blAcertoRegionalFilial" labelWidth="20%" label="lbAcertoRegionalFilial" onclick="onClickCheck(this);"/>

		<adsm:lookup
			label="filial"
			property="filial"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			size="3"
			maxLength="3" disabled="true"
			labelWidth="20%"
			width="71%">
			<adsm:propertyMapping criteriaProperty="tpAcesso" relatedProperty="tpAcesso"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="regionalAnterior"
			property="regionalAnterior"
			service="lms.vendas.manterClienteAction.findLookupRegional"
			action="/municipios/manterRegionais"
			idProperty="idRegional"
			criteriaProperty="sgRegional"
			dataType="text"
			size="3" disabled="true"
			maxLength="3"
			labelWidth="20%"
			width="71%">

			<adsm:propertyMapping relatedProperty="regionalAnterior.dsRegional" modelProperty="dsRegional"/>
			<adsm:textbox
				dataType="text"
				property="regionalAnterior.dsRegional"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="regionalAtual"
			property="regionalAtual"
			service="lms.vendas.manterClienteAction.findLookupRegional"
			action="/municipios/manterRegionais"
			idProperty="idRegional"
			criteriaProperty="sgRegional"
			dataType="text"
			size="3" disabled="true"
			maxLength="3"
			labelWidth="20%"
			width="71%">

			<adsm:propertyMapping relatedProperty="regionalAtual.dsRegional" modelProperty="dsRegional"/>

			<adsm:textbox
				dataType="text"
				property="regionalAtual.dsRegional"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button id="btnProcessar" caption="processar" onclick="processar();" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>
<script>


	function processar(){

		if(!getElement("blAcertoFilialMunicipio").checked 
				&& !getElement("blAcertoRegionalFilial").checked ){
			alert("Selecione Acerto filial por município ou Acerto de filial por regional");
			return false;	
		}

		var info = " é obrigatório";
		if(getElement("blAcertoFilialMunicipio").checked){

			if(isBlank(getElementValue("municipio.idMunicipio"))){
				alert(getElement("municipio.idMunicipio").label+info);
				return false;
			}
			if(isBlank(getElementValue("filialAnterior.idFilial"))){
				alert(getElement("filialAnterior.idFilial").label+info);
				return false;
			}
			if(isBlank(getElementValue("municipio.idMunicipio"))){
				alert(getElement("municipio.idMunicipio").label+info);
				return false;
			}
				
		}else{

			if(isBlank(getElementValue("filial.idFilial"))){
				alert(getElement("filial.idFilial").label+info);
				return false;
			}
			if(isBlank(getElementValue("regionalAnterior.idRegional"))){
				alert(getElement("regionalAnterior.idRegional").label+info);
				return false;
			}
			if(isBlank(getElementValue("regionalAtual.idRegional"))){
				alert(getElement("regionalAtual.idRegional").label+info);
				return false;
			}
			
		}

		var formData = buildFormBeanFromForm(document.forms[0]);
		var idFilial = getElementValue("filial.idFilial");
		var service = "lms.vendas.manterAcertoFilialRegionalClienteAction.executarRotina";
		var sdo = createServiceDataObject(service, "processar", formData);
		xmit({serviceDataObjects:[sdo]});				
	}

	function processar_cb(data, exception){
		if(exception != undefined){
			alert(exception);
			return false;
		}	
		alert("Total de clientes atualizados: "+data.total);	
	}
	
	function onClickCheck(element){
		
		var enable = (element.name == "blAcertoFilialMunicipio"); 
		if(element.checked){
			enableOrDisableFields(enable);
			getElement("btnProcessar").disabled = false;
		}else{
			/*if(enable){
				resetAcertoFilial();
				disableAcertoFilial(true);
			}else{
				resetAcertoRegional();
				disableAcertoRegional(true);
			}*/
				
			resetAcertoFilial();
			disableAcertoFilial(true);
			resetAcertoRegional();
			disableAcertoRegional(true);		
			getElement("btnProcessar").disabled = true;
		}				
	}

	function enableOrDisableFields(enable){
		
		resetAcertoRegional();		
		disableAcertoRegional(!enable);
		getElement("blAcertoRegionalFilial").checked = !enable;
		
		resetAcertoFilial();
		disableAcertoFilial(enable);		
		getElement("blAcertoFilialMunicipio").checked = enable;
	}

	function disableAcertoFilial(enable){		
		setDisabled("filial.idFilial",enable);
		setDisabled("regionalAnterior.idRegional",enable);
		setDisabled("regionalAtual.idRegional",enable);			
	}

	function disableAcertoRegional(enable){						
		setDisabled("municipio.idMunicipio",enable);
		setDisabled("filialAnterior.idFilial",enable);
		setDisabled("filialAtual.idFilial",enable);		
	}
	
	function resetAcertoFilial(){		
		resetValue("municipio.idMunicipio");
		resetValue("filialAnterior.idFilial");
		resetValue("filialAtual.idFilial");
	}
	
	function resetAcertoRegional(){		
		resetValue("filial.idFilial");
		resetValue("regionalAnterior.idRegional");
		resetValue("regionalAtual.idRegional");
	}
	
</script>