<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	function novo(){	
		setDisabled("filialCiaAerea.idFilialCiaAerea", 
			document.getElementById("filialCiaAerea.idFilialCiaAerea").masterLink == "true");
		setDisabled("blDomingo", false);		
		setDisabled("blSegunda", false);
		setDisabled("blTerca", false);
		setDisabled("blQuarta", false);
		setDisabled("blQuinta", false);
		setDisabled("blSexta", false);
		setDisabled("blSabado", false);		
		setDisabled("hrAtendimentoInicial", false);
		setDisabled("hrAtendimentoFinal", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("__buttonBar:0.storeButton", false);
		setDisabled("botaoLimpar", false);
		setFocusOnFirstFocusableField();
	}
	
	function vigencia_cb(data, erros){
		onDataLoad_cb(data,erros);
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0){
			novo();						
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("dtVigenciaFinal", false);
			setDisabled("botaoLimpar", false);
			setDisabled("__buttonBar:0.storeButton", false);			
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2){
			setDisabled(document, true);
			setDisabled("botaoLimpar", false);
			setFocus(document.getElementById("botaoLimpar"),false);
		}
		
					
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click") {			
			novo();
		}
    }
 
    function afterStore_cb(data,exception,key) {
		store_cb(data,exception,key);
		if(exception == undefined){
			vigencia_cb(data,exception);
			setFocusOnNewButton();		
		}
		
		
	}
	
</script>

<adsm:window service="lms.municipios.atendimFilialCiaAereaService">
	<adsm:form idProperty="idAtendimFilialCiaAerea" action="/municipios/manterHorariosAtendimentoFilialCiaAerea" 
				service="lms.municipios.atendimFilialCiaAereaService.findByIdMap" onDataLoadCallBack="vigencia">
	
		<adsm:lookup service="lms.municipios.filialCiaAereaService.findLookup" dataType="text" property="filialCiaAerea" idProperty="idFilialCiaAerea"
					criteriaProperty="pessoa.nrIdentificacao" label="filialCiaAerea2" size="20" maxLength="20" width="47%" 
					relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					action="/municipios/manterFiliaisCiaAerea" labelWidth="13%" required="true">
	         <adsm:propertyMapping relatedProperty="filialCiaAerea.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
   	         <adsm:propertyMapping relatedProperty="filialCiaAerea.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" />
	         <adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="aeroporto.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="filialCiaAerea.pessoa.nmPessoa" size="30" maxLength="50" disabled="true" serializable="false"/>		
	    </adsm:lookup>
    
		<adsm:textbox property="filialCiaAerea.empresa.pessoa.nmPessoa" dataType="text" label="ciaAerea" labelWidth="13%" width="23%" disabled="true" required="false" size="30"/>
		
		<adsm:textbox property="filialCiaAerea.aeroporto.pessoa.nmPessoa" dataType="text" label="aeroporto" labelWidth="13%" size="59" width="87%" disabled="true" required="false" />

		<adsm:multicheckbox 
			texts="dom|seg|ter|qua|qui|sex|sab|"
			property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|" 
			align="top" label="dias2"  labelWidth="13%" cellStyle="vertical-align:bottom"/>
			
		<adsm:range label="horario" labelWidth="13%" width="87%" required="true" >
			<adsm:textbox dataType="JTTime" property="hrAtendimentoInicial" />
			<adsm:textbox dataType="JTTime" property="hrAtendimentoFinal"/>
		</adsm:range> 

		<adsm:range label="vigencia" labelWidth="13%" width="87%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

 		<adsm:buttonBar>
 		    <adsm:storeButton callbackProperty="afterStore" service="lms.municipios.atendimFilialCiaAereaService.storeMap" />
			<adsm:newButton id="botaoLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
