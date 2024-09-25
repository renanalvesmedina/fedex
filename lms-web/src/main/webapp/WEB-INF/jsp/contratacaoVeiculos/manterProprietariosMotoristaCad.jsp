<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<SCRIPT>
	function proprietarioMotoristaDataLoad_cb(data,exception){
		 onDataLoad_cb(data,exception);
		 var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		 validaAcaoVigencia(acaoVigenciaAtual, null);
		 
	}
	
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		setDisabled("proprietario.idProprietario", true);
		setDisabled("motorista.idMotorista", true);
		
		//Futuro
		if (acaoVigenciaAtual == 0){
			novo();
			if(tipoEvento == "" ||  tipoEvento == null) 
		     	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
		//Atual       
		} else if (acaoVigenciaAtual == 1){
			setDisabled("dtVigenciaInicial", true);		
			setDisabled("removeButton",true);	
			if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
		//Passado       
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("newButton", false);
			setFocusOnNewButton(document);
		}
	}
	
	function afterStore_cb(data,exception,key){
		store_cb(data,exception,key);
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
  			validaAcaoVigencia(acaoVigenciaAtual, store);
		}	
	} 
	
</SCRIPT>
<adsm:window title="manterMotoristasProprietario" service="lms.contratacaoveiculos.proprietarioMotoristaService">

	<adsm:form action="/contratacaoVeiculos/manterProprietariosMotorista" service="lms.contratacaoveiculos.proprietarioMotoristaService.findByIdDetalhamento"  idProperty="idProprietarioMotorista" onDataLoadCallBack="proprietarioMotoristaDataLoad">
	
		<adsm:lookup property="proprietario" 
					 idProperty="idProprietario"  
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 dataType="text" label="proprietario" size="21" maxLength="20" 
					 service="lms.contratacaoveiculos.proprietarioService.findLookup" 
					 action="/contratacaoVeiculos/manterProprietarios" width="19%">
         	<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
         	<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
	    </adsm:lookup>
	    <adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="50" maxLength="50" width="50%" disabled="true" required="true"/>
		
		<adsm:lookup property="motorista" 
					 idProperty="idMotorista" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 dataType="text"  label="motorista" size="21" maxLength="20"
		             service="lms.contratacaoveiculos.motoristaService.findLookup" 
					 action="/contratacaoVeiculos/manterMotoristas" width="19%">
		 	<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		 	<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/> 
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" required="true" size="30" maxLength="50" width="50%" disabled="true"  />
	
		<adsm:range label="vigencia" width="85%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
    	
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore"
					service="lms.contratacaoveiculos.proprietarioMotoristaService.storeMap" />
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>
<script>
	
	function initWindow(event){
		if (event.name != 'grid_rowClick' && event.name != 'storeButton'){					
			 novo();						
			setFocusOnFirstFocusableField();
		}
	}

	function novo(){
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("proprietario.idProprietario", document.getElementById("proprietario.idProprietario").masterLink == "true");
		setDisabled("motorista.idMotorista", document.getElementById("motorista.idMotorista").masterLink == "true");
		setFocusOnFirstFocusableField();
	}
	
</script>