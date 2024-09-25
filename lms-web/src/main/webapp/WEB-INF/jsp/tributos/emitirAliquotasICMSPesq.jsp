<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.emitirAliquotasICMSAction">
	<adsm:form action="/tributos/emitirAliquotasICMS">

		<adsm:lookup labelWidth="23%" width="28%" label="ufOrigem" property="unidadeFederativaOrigem" idProperty="idUnidadeFederativa" 
	 		dataType="text" criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup" size="3" maxLength="10" 
			minLengthForAutoPopUpSearch="3"	action="/municipios/manterUnidadesFederativas" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativaOrigem.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox property="unidadeFederativaOrigem.nmUnidadeFederativa" dataType="text" serializable="false" size="20" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup labelWidth="10%" width="25%" label="ufDestino" property="unidadeFederativaDestino" idProperty="idUnidadeFederativa" 
	 		dataType="text" criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup" size="3" maxLength="10" 
			minLengthForAutoPopUpSearch="3"	action="/municipios/manterUnidadesFederativas" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativaDestino.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox property="unidadeFederativaDestino.nmUnidadeFederativa" dataType="text" serializable="false" size="20" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox 
			property="regiaoGeografica.idRegiaoGeografica" 
			label="regiaoGeograficaDestino" 
			service="lms.municipios.regiaoGeograficaService.find" 
			optionLabelProperty="dsRegiaoGeografica" 
			optionProperty="idRegiaoGeografica" 
			labelWidth="23%" width="80%" boxWidth="470" onlyActiveValues="false"/>

		<adsm:combobox labelWidth="23%" width="80%" property="tpSituacaoTribRemetente" boxWidth="470"
		label="situacaoTributariaRemetente" domain="DM_SITUACAO_TRIBUTARIA" />

		<adsm:combobox labelWidth="23%" width="80%" property="tpSituacaoTribDestinatario"  boxWidth="470"
		label="situacaoTributariaDestinatario" domain="DM_SITUACAO_TRIBUTARIA"/>
		
        <adsm:range label="vigencia" labelWidth="23%" width="28%">
			<adsm:textbox dataType="JTDate" property="dtVigencia"/>
		</adsm:range>
		
        <adsm:combobox labelWidth="20%" width="28%" property="tpTipoFrete" domain="DM_TIPO_FRETE" label="tipoFrete"/>
                       
        <adsm:combobox label="tipoTributacao" 
					   property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					   service="lms.tributos.emitirAliquotasICMSAction.findComboTipoTributacao" 
					   onlyActiveValues="true" 
					   optionLabelProperty="dsTipoTributacaoIcms" 
					   optionProperty="idTipoTributacaoIcms" 
					   labelWidth="23%" 
					   width="28%"
					   onchange="myOnChangeTipoTributacao(this)"
					   boxWidth="200"/>               
					   
		<adsm:range 
			label="vigenciaFinal" 
			labelWidth="23%" >

	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal" />

	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"/>

        </adsm:range>

					   
		<adsm:hidden property="dsTipoTributacaoIcms" serializable="true"/>
        
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" labelWidth="23%" width="28%" required="true" defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" onclick="emitirRelatorio();" buttonType="reportButton" id="btnVisualizar" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function myOnChangeTipoTributacao(elem){
		
		var retorno = comboboxChange({e:elem});
	
		var tpTributacao = elem;
		
		if( tpTributacao.selectedIndex != 0 ){
			setElementValue('dsTipoTributacaoIcms',tpTributacao.options[tpTributacao.selectedIndex].text);
		}
		
		return retorno;
	}

	function emitirRelatorio(){
		var dtVigenciaInicial = getElement("dtVigenciaInicial")
		var dtVigenciaFinal   = getElement("dtVigenciaFinal")
		if(dtVigenciaInicial.value != "" &&  dtVigenciaFinal.value == ""){
			alert("Campo 'Vigência final' data final obrigatório!");
			setFocus('dtVigenciaFinal');			
			return;
		}
		if(dtVigenciaInicial.value == "" &&  dtVigenciaFinal.value != ""){
			alert("Campo 'Vigência final' data inicial obrigatório!");
			setFocus('dtVigenciaInicial');
			return;
		}		
		reportButtonScript('lms.tributos.emitirAliquotasICMSAction', 'openPdf', document.forms[0]);
	}

	
</script>