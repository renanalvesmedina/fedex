<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
</script>
<adsm:window service="lms.contratacaoveiculos.manterPostoConveniadoAction" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-27053" />
		<adsm:include key="pessoa" />
		<adsm:include key="postoConveniado" />
	</adsm:i18nLabels>
	<adsm:form action="/contratacaoVeiculos/manterPostoConveniado" idProperty="idPostoConveniado" onDataLoadCallBack="myOnDataLoad" >	
		<adsm:hidden property="pessoa.idPessoa"/>	
		
		<adsm:combobox property="pessoa.tpPessoa" onlyActiveValues="true" labelWidth="15%" width="35%" label="tipoPessoa" domain="DM_TIPO_PESSOA" definition="TIPO_PESSOA.list" required="true" defaultValue="J" disabled="true" />
        
		<adsm:complement label="identificacao" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" onchange="return tpIdentificacaoOnChange()" required="true" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.configuracoes.manterPessoasAction.findPessoa"
					onDataLoadCallBack="pessoaCallback"
					onchange="return nrIdentificacaoOnChange();" required="true"/>
		</adsm:complement>
		
		<adsm:hidden property="labelPessoa" />		 
		
		<adsm:textbox dataType="text" size="95%" labelWidth="15%" width="82%" property="pessoa.nmPessoa" label="nome" maxLength="50" required="true" />
		<adsm:textbox dataType="JTDateTimeZone" picker="false" serializable="false" labelWidth="15%" width="82%" property="pessoa.dhInclusao" label="dataInclusao" disabled="true"/>
		<adsm:textbox dataType="text" property="pessoa.dsEmail" label="email" size="60" maxLength="60" labelWidth="15%" width="85%" />
		
		<adsm:buttonBar>
			<adsm:button caption="telefones" action="/configuracoes/manterTelefonesPessoa" cmd="main">
				<adsm:linkProperty src="idPostoConveniado" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idPostoConveniado" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idPostoConveniado" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>							
			</adsm:button>			
			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="idPostoConveniado" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>			
			<adsm:button caption="dadosBancarios" action="/configuracoes/manterDadosBancariosPessoa" cmd="main">
				<adsm:linkProperty src="idPostoConveniado" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>						
			<adsm:storeButton/>
			<adsm:newButton />
			<adsm:removeButton  />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
        
	document.getElementById("labelPessoa").masterLink = "true";
	
	function pessoaCallback_cb(data,erro) {
		if (data == undefined || erro != undefined) {
			return pessoa_nrIdentificacao_exactMatch_cb(data);				
		}

		// Se Pessoa cadastrada
		if (data.idPessoa != undefined){
			setElementValue("pessoa.tpPessoa",data.tpPessoa);
			setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
			
			setElementValue("pessoa.idPessoa",data.idPessoa);
			
			setElementValue("pessoa.nmPessoa",data.nmPessoa);
			
			setElementValue("pessoa.dsEmail",data.dsEmail);
			
			setElementValue("pessoa.nrIdentificacao",data.nrIdentificacaoFormatado);
		} else {
			setElementValue("pessoa.idPessoa", "");
		}

	}
	
	function tpIdentificacaoOnChange(){
		var tpIdentificacaoElement = document.getElementById("pessoa.tpIdentificacao");
		var numberElement = document.getElementById("pessoa.nrIdentificacao");
		
		var tpIdentificacao = getElementValue(tpIdentificacaoElement);


		changeIdentificationTypePessoaWidget({tpIdentificacaoElement:tpIdentificacaoElement, numberElement:numberElement, tabCmd:'cad'});
		
		setElementValue("pessoa.nrIdentificacao", "");
		setElementValue("pessoa.idPessoa", "");

		setDisabled(numberElement, tpIdentificacao == "");
		
		if (numberElement.disabled == false) {
		   var numberInputElement;
		   if ("lookup" == numberElement.widgetType)
		      numberInputElement = document.getElementById(numberElement.property + "." + numberElement.criteriaProperty);
		   else
		      numberInputElement = numberElement;
			
		   if (tpIdentificacao == "")
		      numberInputElement.dataType = "text";
		   else
		      numberInputElement.dataType = tpIdentificacao;
		}
		
		return true;	
	}
	
	function nrIdentificacaoOnChange(){
		if (getElementValue("pessoa.nrIdentificacao") != ""){
			return pessoa_nrIdentificacaoOnChangeHandler();
		}
	}

    function myOnDataLoad_cb(d,e){  
    	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});	            	
    	onDataLoad_cb(d,e);
    	if (e == undefined){
    		setElementValue("pessoa.idPessoa", d.pessoa.idPessoa);
	 		setElementValue("pessoa.tpPessoa", d.pessoa.tpPessoa);
	 		setElementValue("pessoa.tpIdentificacao", d.pessoa.tpIdentificacao);
	 		tpIdentificacaoOnChange();
	 		setElementValue("pessoa.nrIdentificacao", d.pessoa.nrIdentificacao);
		    setDisabled("pessoa.nrIdentificacao", true);
		    setDisabled("pessoa.tpIdentificacao", true);		    	    
 		}

    	onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
	    //habilitaDesabilitaRg();
    }


	function initWindow(eventObj) {
    	if (eventObj.name == "newButton_click" || 
    	    eventObj.name == "removeButton" || 
    	    eventObj.name == "tab_click") {

			setDisabled("pessoa.tpIdentificacao", false);
			setDisabled("pessoa.nrIdentificacao", false);
		    
	    } else if( eventObj.name == 'storeButton' ){
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
	    }
    	setElementValue(document.getElementById("labelPessoa"),i18NLabel.getLabel("postoConveniado"));
	}
	
</script>