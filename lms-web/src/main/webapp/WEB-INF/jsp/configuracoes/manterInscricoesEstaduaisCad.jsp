<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterInscricoesEstaduaisAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterInscricoesEstaduais" idProperty="idInscricaoEstadual" id="formInscricoesEstaduais" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-27064"/>
			<adsm:include key="LMS-27096"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="aviso" serializable="false"/>
		<adsm:hidden property="tpPessoa"/>
		<adsm:hidden property="pessoa.idPessoa"/>
	    <adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="permiteManutencaoUF" serializable="false"/>
 		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		
        <adsm:textbox dataType="text" property="pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
		<adsm:lookup property="unidadeFederativa"  
		             idProperty="idUnidadeFederativa" 
		             criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" 
					 required="true"					
					 width="35%" 
					 label="uf" 
					 size="3" 
					 maxLength="3"
					 action="/municipios/manterUnidadesFederativas" 
					 minLengthForAutoPopUpSearch="2"
					 exactMatch="true" 
					 disabled="false"
					 onchange="return onChange_uf();">
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox property="unidadeFederativa.nmUnidadeFederativa" dataType="text" disabled="true"/>
		</adsm:lookup>        
		
		<adsm:checkbox label="indicadorPadrao" property="blIndicadorPadrao" width="35%" />
		
		<adsm:textbox label="numero" dataType="text" property="nrInscricaoEstadual" onchange="return validateIE();" size="20" maxLength="20" required="true" width="35%"/>
		
		<adsm:checkbox label="isentoInscricao" property="blIsentoInscricao" width="35%" onclick="insentoInscricaoOnClick()"/>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true" width="35%" />

	    <adsm:buttonBar>
            <adsm:button caption="observacoesIcms" action="configuracoes/manterDescricaoTributacaoICMSPessoa.do" cmd="main">
			   <adsm:linkProperty src="idInscricaoEstadual"    target="inscricaoEstadual.idInscricaoEstadual" />
			   <adsm:linkProperty src="pessoa.nrIdentificacao" target="inscricaoEstadual.pessoa.nrIdentificacao" />
			   <adsm:linkProperty src="nrInscricaoEstadual"    target="inscricaoEstadual.nrInscricaoEstadual" />
			   <adsm:linkProperty src="pessoa.nmPessoa"        target="inscricaoEstadual.pessoa.nmPessoa" />
			   <adsm:linkProperty src="labelPessoaTemp"        target="labelPessoaTemp" />
		    </adsm:button>
            <adsm:button caption="tiposTributacao"  action="configuracoes/manterTipoTributacaoIE.do" cmd="main" id="tiposTributacao">
			   <adsm:linkProperty src="idInscricaoEstadual"    target="inscricaoEstadual.idInscricaoEstadual" />
			   <adsm:linkProperty src="nrInscricaoEstadual"    target="inscricaoEstadual.nrInscricaoEstadual" />
			   <adsm:linkProperty src="pessoa.idPessoa"		   target="inscricaoEstadual.pessoa.idPessoa" />
			   <adsm:linkProperty src="pessoa.nrIdentificacao" target="inscricaoEstadual.pessoa.nrIdentificacao" />
			   <adsm:linkProperty src="pessoa.nmPessoa"        target="inscricaoEstadual.pessoa.nmPessoa" />
			   <adsm:linkProperty src="labelPessoaTemp"        target="labelPessoaTemp" />
		    </adsm:button>
		    <adsm:button caption="salvar" id="btnSalvar" buttonType="storeButton" disabled="false"
		                 service="lms.configuracoes.manterInscricoesEstaduaisAction.store"
		                 callbackProperty="store"/>
  		    <adsm:button caption="limpar" buttonType="newButton" onclick="myCleanButtonClick();" disabled="false"/>
		    <adsm:removeButton/> 
		</adsm:buttonBar>   
		
	</adsm:form>
	
</adsm:window>
<script>

	document.getElementById('pessoa.nrIdentificacao').masterLink = 'true';
	document.getElementById('pessoa.nmPessoa').masterLink = 'true';		

	function myCleanButtonClick(){
		validateBlAtualizacaoCountasse();		
	}
	
	function validateBlAtualizacaoCountasse(){
		var sdo = createServiceDataObject("lms.configuracoes.manterInscricoesEstaduaisAction.validateBlAtualizacaoCountasse", 
		                                  "validateBlAtualizacaoCountasse", 
		                                  {idPessoa:getElementValue("pessoa.idPessoa")});
		xmit({serviceDataObjects:[sdo]}); 		
	}
	
	function validateBlAtualizacaoCountasse_cb(data, error){
		if (error != undefined) {
			alert(i18NLabel.getLabel("LMS-27096"));
		}
		newButtonScript();
	}
	
    /**
	 * Tratar comportamento de alguns campos quando for inserido um novo 
	 * registro. 
	 */
	function initWindow(eventObj) {
	   // Ao incluir um novo registro
	   //    campo "Inscrição padrão" deve vir ligado (Sim)
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			findDadosIniciais();
			setDisabled("unidadeFederativa.idUnidadeFederativa", false);
			setDisabled("nrInscricaoEstadual", false);
			setFocusOnFirstFocusableField();
			setDisabled("tpSituacao", true);
		} else if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton") {
			setDisabled("unidadeFederativa.idUnidadeFederativa", true);
			setDisabled("nrInscricaoEstadual", true);
			setDisabled("blIsentoInscricao", true);
			setFocusOnFirstFocusableField();
			setDisabled("tpSituacao", false);
		} 
		
		if( eventObj.name == 'storeButton' && getElementValue('idInscricaoEstadual') != '' ){
			alert(getElementValue('aviso'));			
			setFocus('tiposTributacao',true,true);
			setDisabled("tpSituacao", false);
		}
		
	}
	
	function onChange_uf(){
		if (document.getElementById("unidadeFederativa.sgUnidadeFederativa").value == document.getElementById("unidadeFederativa.sgUnidadeFederativa").previousValue) {		
			return true;
		} else {
			if(getElementValue('permiteManutencaoUF') != "true"){
				resetValue('nrInscricaoEstadual');
				resetValue('blIsentoInscricao');
			}			
		}
		
		return unidadeFederativa_sgUnidadeFederativaOnChangeHandler();	
	}
	
	function validateIE(){
		var retorno = true;
		if( getElementValue('nrInscricaoEstadual') != '' ){
			retorno = validateInscricaoEstadual(getElementValue('unidadeFederativa.sgUnidadeFederativa'), getElementValue('nrInscricaoEstadual'));		
		}
		return retorno;
	}
	
	function findDadosIniciais(){
		var sdo = createServiceDataObject("lms.configuracoes.manterInscricoesEstaduaisAction.getInitialValue", 
		                                  "getInitialValue", 
		                                  buildFormBeanFromForm(document.getElementById("formInscricoesEstaduais")));
		xmit({serviceDataObjects:[sdo]}); 		
	}
	
   /**
    *  	Preenche a Unidade Federativa caso a pessoa possua um endereço comercial
    *	Seta o indicador Padrão caso a pessoa não possua inscrição estadual marcada como padrão
    */	
	function getInitialValue_cb(data,erro){

		if (data!=undefined) {
		
			resetValue("unidadeFederativa.idUnidadeFederativa");
			resetValue("unidadeFederativa.nmUnidadeFederativa");
			resetValue("unidadeFederativa.sgUnidadeFederativa");			
			resetValue('nrInscricaoEstadual');
			
	    	fillFormWithFormBeanData(0, data);
	    	
	    	if( data.endereco != undefined && data.endereco.idUnidadeFederativa != undefined ){
		    	setElementValue("unidadeFederativa.idUnidadeFederativa",data.endereco.idUnidadeFederativa);
				setElementValue("unidadeFederativa.nmUnidadeFederativa",data.endereco.nmUnidadeFederativa);
				setElementValue("unidadeFederativa.sgUnidadeFederativa",data.endereco.sgUnidadeFederativa);			
			}
			
	        setDisabled("unidadeFederativa.idUnidadeFederativa",(getElementValue("unidadeFederativa.idUnidadeFederativa") != ""));
        	setDisabled("blIndicadorPadrao",!(data.enableBlIndicadorPadrao == "true"));
        	
        	if( data.tpPessoa == 'F' && !(isProdutorRural(data.idRamoAtividade)) ){
        		setElementValue('nrInscricaoEstadual','ISENTO');
        		setElementValue('blIsentoInscricao',true);
				setDisabled("nrInscricaoEstadual", true);
				setDisabled("blIsentoInscricao", true);
				document.getElementById("nrInscricaoEstadual").masterLink = "true";
				document.getElementById("blIsentoInscricao").masterLink = "true";
        	} else {
        		document.getElementById("nrInscricaoEstadual").masterLink = "false";
				document.getElementById("blIsentoInscricao").masterLink = "false";
				setDisabled("nrInscricaoEstadual", false);
				setDisabled("blIsentoInscricao", false);				
        	}
        	
        	setElementValue("blIndicadorPadrao",(data.enableBlIndicadorPadrao == "true"));

	    } else {
	    	alert(erro);
	        setDisabled("unidadeFederativa.idUnidadeFederativa",false);	    	        	    
	    }
	    
	    setFocusOnFirstFocusableField();

    }


    function isProdutorRural(idRamoAtividade) {
    	var idRamoAtividadeProdutorRural = '6';
    	
    	return idRamoAtividade == idRamoAtividadeProdutorRural
    }
    
   /**
	*  Tratar o label dinâmico
	*
	*/
    function myOnPageLoad_cb(data, erro){
	   onPageLoad_cb();
   	   document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
   	   setFocusOnFirstFocusableField(document);	    	          
    }
    
    function myOnDataLoad_cb(data, erro){
		onDataLoad_cb(data, erro);
		setDisabled("nrInscricaoEstadual", true);
		if (data.enableBlIndicadorPadrao == "false"){
			setDisabled("blIndicadorPadrao",true);
		} else {
			setDisabled("blIndicadorPadrao",false);	        
		}    
		
		var nrInscricaoEstadual = getElementValue('nrInscricaoEstadual');		
		
		if( nrInscricaoEstadual == 'ISENTO' ){
			setElementValue("blIsentoInscricao",true);
		} else {
			setElementValue("blIsentoInscricao",false);
		}

		var permiteManutencaoUF = data.permiteManutencaoUF;
		if(permiteManutencaoUF != undefined && permiteManutencaoUF == "true"){
			setDisabled("unidadeFederativa.idUnidadeFederativa",false);	
			setElementValue("permiteManutencaoUF","true");				
		}else{
			setDisabled("unidadeFederativa.idUnidadeFederativa",true);
			setElementValue("permiteManutencaoUF","false");
		}
	}    
	
	/**
	*	Ao selecionar Isento de Inscrição desabilita o número de Inscrição Estadual e preenche com ISENTO
	*/
	function insentoInscricaoOnClick(){
		
		var isentoInscricao = getElementValue('blIsentoInscricao');
		
		if( isentoInscricao == true ){
			setDisabled("nrInscricaoEstadual",true);  
			setElementValue("nrInscricaoEstadual","ISENTO");
		} else {
			setDisabled("nrInscricaoEstadual",false);  
			resetValue('nrInscricaoEstadual');
		}
		
	}		
	
</script>
