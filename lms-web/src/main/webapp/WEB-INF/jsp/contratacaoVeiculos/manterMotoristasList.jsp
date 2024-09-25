<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function pageLoadMotorista() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});
	} 
</script>
<adsm:window title="consultarMotoristas" service="lms.contratacaoveiculos.manterMotoristasAction" onPageLoadCallBack="myOnPageLoad" onPageLoad="pageLoadMotorista">
	<adsm:form action="/contratacaoVeiculos/manterMotoristas" idProperty="idMotorista">
	
		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="identificacao"/>
                <adsm:include key="tipoVinculo"/>
                <adsm:include key="nome"/>
                <adsm:include key="proprietario"/>
    	</adsm:i18nLabels>
	
		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupFilial" dataType="text" 
					idProperty="idFilial" property="filial" criteriaProperty="sgFilial" 
					label="filial" size="3" maxLength="3" labelWidth="17%" width="33%" action="/municipios/manterFiliais">
	         <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
	         <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="28" maxLength="60" disabled="true"/>
	    </adsm:lookup>
		
		<adsm:combobox property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_MOTORISTA"/>
		
		<adsm:hidden property="pessoa.tpPessoa" value="F"/>
		<adsm:complement labelWidth="17%" width="33%" label="identificacao">
        	<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>		
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
        </adsm:complement>
         
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" size="30" maxLength="60" width="35%"/>
		
		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupProprietario" idProperty="idProprietario" 
                     property="proprietario" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
                     label="proprietario" size="21" maxLength="20" 
					 width="75%" labelWidth="17%" action="/contratacaoVeiculos/manterProprietarios" dataType="text">
	         <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	         <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" maxLength="60" disabled="true"/>
	    </adsm:lookup>
    	<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

	    <adsm:combobox property="blBloqueado" label="bloqueado" domain="DM_SIM_NAO" labelWidth="17%" width="33%" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="17%" width="33%" />
		
		<adsm:range label="vencimentoCNH" labelWidth="17%" width="33%"> 
			<adsm:textbox dataType="JTDate" property="dtVencimentoHabilitacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVencimentoHabilitacaoFinal"/>
		</adsm:range>	 
		
		<adsm:range label="periodoAtualizacao" labelWidth="17%"  width="33%"> 
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoFinal"/>
		</adsm:range>
		
		<adsm:lookup service="lms.contratacaoveiculos.manterMotoristasAction.findLookupRotasViagem" dataType="integer" 
					 property="rotaIdaVolta" idProperty="idRotaIdaVolta"
					 criteriaProperty="nrRota"
					 size="4" label="rotaViagem" 
					 maxLength="4" exactMatch="false"					 
					 labelWidth="17%"
					 width="33%" mask="0000"
					 action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" >
			<adsm:propertyMapping relatedProperty="rotaIdaVolta.dsRota" modelProperty="rota.dsRota"/>
			<adsm:propertyMapping relatedProperty="idRotaViagem" modelProperty="rotaViagem.idRotaViagem"/>
			<adsm:textbox dataType="text" property="rotaIdaVolta.dsRota" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:hidden property="sim" value="S" serializable="false"/>
		<adsm:hidden property="idRotaViagem" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motorista"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		 
	</adsm:form>
	<adsm:grid idProperty="idMotorista" property="motorista"  gridHeight="161" unique="true" rows="9"
			service="lms.contratacaoveiculos.manterMotoristasAction.findPaginatedCustom"
			rowCountService="lms.contratacaoveiculos.manterMotoristasAction.getRowCountCustom">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="75" />
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="75" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="50" align="left" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="150" />
		<adsm:gridColumn title="matricula" property="usuarioMotorista.nrMatricula" width="100" align="right" />
		<adsm:gridColumn title="tipoVinculo" property="tpVinculo" isDomain="true" width="90" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 
<script>

	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
    
	function initWindow(evt){
		var event = evt.name;
		
		if(event == "tab_click"){
			disableTabAnexos(true);
		}
		
		if (event == "cleanButton_click" || event == "tab_load"){
			setFocusOnFirstFocusableField();
		}		
	}

	
	function myOnPageLoad_cb(data){
       onPageLoad_cb(data);
       changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
       if (window.dialogArguments &&
       		window.dialogArguments.window.document.getElementById("motorista.pessoa.nrIdentificacao") &&
       		getElementValue(window.dialogArguments.window.document.getElementById("motorista.pessoa.nrIdentificacao")) != ""){
       			setElementValue("pessoa.nrIdentificacao",getElementValue(window.dialogArguments.window.document.getElementById("motorista.pessoa.nrIdentificacao")));    	   
       }
      
	}

	function validateTab() {	
		if (validateTabScript(document.forms)) {
			
			if ( (getElementValue("tpVinculo") == null || getElementValue("tpVinculo") == "") && 
				 (getElementValue("pessoa.nmPessoa") == null || getElementValue("pessoa.nmPessoa") == "") && 
				 (getElementValue("proprietario.idProprietario") == null || getElementValue("proprietario.idProprietario") == "") &&
				 (getElementValue("pessoa.nrIdentificacao") == null || getElementValue("pessoa.nrIdentificacao") == "") ) {
				 
				 alert(i18NLabel.getLabel("LMS-00013")
								+ i18NLabel.getLabel("tipoVinculo") + " ou "
								+ i18NLabel.getLabel("identificacao")+ " ou "
								+ i18NLabel.getLabel("nome")+ " ou "
								+ i18NLabel.getLabel("proprietario")+ "." );
		    	 return false;
			}
			
		}
	}	
	
	function validateLookupForm() {
      	return validateTab();
	}
	
</script>
