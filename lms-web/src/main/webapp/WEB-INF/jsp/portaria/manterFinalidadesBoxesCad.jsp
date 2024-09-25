<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterFinalidadesBoxesAction">
	<adsm:form action="/portaria/manterFinalidadesBoxes" idProperty="idBoxFinalidade" onDataLoadCallBack="finalidadesBoxesDataLoad" service="lms.portaria.manterFinalidadesBoxesAction.findById">	
		<adsm:textbox dataType="text" property="box.doca.terminal.filial.sgFilial" size="3" label="filial" labelWidth="22%" width="38%" disabled="true">
			<adsm:textbox dataType="text"property="box.doca.terminal.filial.pessoa.nmFantasia" size="27" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="box.doca.terminal.pessoa.nmPessoa" label="terminal" labelWidth="22%" width="28%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.doca.numeroDescricaoDoca" serializable="false" label="doca" labelWidth="22%" width="28%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.nrBox" label="box" labelWidth="22%" width="28%" disabled="true" />
		<adsm:hidden property="box.idBox"/>
				
	    <adsm:combobox property="finalidade.idFinalidade" optionLabelProperty="dsFinalidade" optionProperty="idFinalidade" required="true" boxWidth="200"
	   					service="lms.portaria.manterFinalidadesBoxesAction.findFinalidade" label="finalidade" labelWidth="22%" width="68%" onlyActiveValues="true" />
	    
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200"
				service="lms.portaria.manterFinalidadesBoxesAction.findServico" label="servico" labelWidth="22%" width="68%"
				onlyActiveValues="true" />
		
		<adsm:range label="intervaloAtendimento" required="true" labelWidth="22%" width="68%">
             <adsm:textbox dataType="JTTime" property="hrInicial" required="true" mask="HH:mm" picker="true" />
             <adsm:textbox dataType="JTTime" property="hrFinal"  mask="HH:mm" picker="true"/>
        </adsm:range>

		<adsm:range label="vigencia" labelWidth="22%" width="68%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
        
	<adsm:buttonBar>
			<adsm:storeButton id="store" callbackProperty="storeFinalidadesBoxes" service="lms.portaria.manterFinalidadesBoxesAction.storeMap" />
			<adsm:newButton id="novo"/>
			<adsm:removeButton id="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	function initWindow(evt){
		if(evt.name != "gridRow_click" && evt.name != 'storeButton'){
			novo();
			setFocusOnFirstFocusableField();
		}
	}

	function storeFinalidadesBoxes_cb(data, error, key){		
		store_cb(data, error, key);
		if (error == undefined && data != undefined){
			var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
			validaAcaoVigencia(acaoVigencia, store);
			
		}			
	}
	
	function novo(){
		setDisabled("box.doca.terminal.filial.sgFilial",true);
		setDisabled("box.doca.terminal.filial.pessoa.nmFantasia",true);
		setDisabled("box.doca.terminal.pessoa.nmPessoa",true);
		setDisabled("box.doca.numeroDescricaoDoca",true);
		setDisabled("box.nrBox",true);
		setDisabled("finalidade.idFinalidade",false);
		setDisabled("servico.idServico",false);
		setDisabled("hrInicial",false);
		setDisabled("hrFinal",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setDisabled("store",false);
		setDisabled("novo",false);
	}

	function finalidadesBoxesDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		validaAcaoVigencia(acaoVigencia, null);
		
	}
	
	function validaAcaoVigencia(acaoVigencia, tipoEvento){
		if (acaoVigencia == 0){
			novo();	
			if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocusOnFirstFocusableField(document);
		    else
		        setFocusOnNewButton(document);		
		} else if (acaoVigencia == 1){
			setDisabled(document, true);
			setDisabled("dtVigenciaFinal",false);
			setDisabled("novo",false);
			setDisabled("store",false);
			if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocusOnFirstFocusableField(document);
		    else
		        setFocusOnNewButton(document);		
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("novo", false);		
			setFocusOnNewButton(document);		
		}
	}

</script>