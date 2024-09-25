<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario e dados para os objetos
	 */
	function loadDataObjects() {
		//da continuidade no fluxo de carregamento da tela
		onPageLoad();

		//busca os dados da URL
		var url = new URL(parent.location.href);
		var idNaoConformidade = url.parameters["ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"];
		setElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", idNaoConformidade);
	
		notifyElementListeners({e:document.getElementById("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade")});
			
		//aplica a mascara no campo
		format(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
	}
</script>

<adsm:window service="lms.rnc.registrarNegociacoesAction" onPageLoad="loadDataObjects">
	<adsm:form action="/rnc/registrarNegociacoes">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		
		<adsm:hidden property="idOcorrenciaNaoConformidadeLocMerc" serializable="false"/>
	
		<adsm:lookup dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.rnc.registrarNegociacoesAction.findFilialLookUp" action="/municipios/manterFiliais" onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrNaoConformidade"
					 label="naoConformidade" labelWidth="20%" width="30%" size="3" maxLength="3" picker="false" serializable="false" popupLabel="pesquisarFilial">
		        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
				<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:lookup dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade" idProperty="idNaoConformidade" criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" service="lms.rnc.registrarNegociacoesAction.findNaoConformidadeLookUp" 
						 onDataLoadCallBack="loadNrNaoConformidade" onPopupSetValue="enableNrNaoConformidade"
						 exactMatch="false" size="15" maxLength="8" required="true" mask="00000000" popupLabel="pesquisarNaoConformidade" disabled="true"> 
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" blankFill="false"/>						 
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>
					   
		<adsm:combobox property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" optionProperty="idOcorrenciaNaoConformidade" optionLabelProperty="nrOcorrenciaNc"
					   service="lms.rnc.registrarNegociacoesAction.findOcorrenciaNaoConformidade"
					   label="numeroOcorrencia" labelWidth="20%" width="80%" onDataLoadCallBack="loadOcorrencianNC">
	    	<adsm:propertyMapping modelProperty="naoConformidade.idNaoConformidade" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"/>
		</adsm:combobox>
					   
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="negociacoes"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="negociacoes" idProperty="idNegociacao" unique="true"  selectionMode="none" rows="12" scrollBars="horizontal"
			   gridHeight="240"
			   defaultOrder="ocorrenciaNaoConformidade_.nrOcorrenciaNc:asc, ocorrenciaNaoConformidade_motivoAberturaNc_.dsMotivoAbertura:asc, dhNegociacao:desc"
			   service="lms.rnc.registrarNegociacoesAction.findPaginatedCustom"
			   rowCountService="lms.rnc.registrarNegociacoesAction.getRowCountCustom"
			   >
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn property="ocorrenciaNaoConformidade.nrOcorrenciaNc" title="ocorrencia" width="40" dataType="integer" align="left"/>
			<adsm:gridColumn property="ocorrenciaNaoConformidade.motivoAberturaNc.dsMotivoAbertura" title="" width="120" align="left"/>
		</adsm:gridColumnGroup>  
		<adsm:gridColumn property="dsNegociacao" title="descricaoNegociacao" width="250" />
		<adsm:gridColumn property="usuario.nmUsuario" title="funcionario" width="150" />
				<adsm:gridColumnGroup separatorType="RNC">	   
			<adsm:gridColumn title="rncLegado" 			property="ocorrenciaNaoConformidade.filialByIdFilialLegado.sgFilial" width="30" />
			<adsm:gridColumn title="" 				 	property="ocorrenciaNaoConformidade.nrRncLegado" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn property="dhNegociacao" title="dataHora" dataType="JTDateTimeZone" align="center" width="150"/>
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>

<script> 

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			disableNrNaoConformidade(true)
		}
		else if (eventObj.name == "tab_load") {
			document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").required = "true";
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", false);
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", true);
			
		} else if (eventObj.name == "tab_click") {
			if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial")!=""){
				setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", false);
			}
		}
    }


	/**
	 * Controla o objeto de nao conformidade 
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial")=="") {
			disableNrNaoConformidade(true);
			resetValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade");
		} else {
			disableNrNaoConformidade(false);
		}
		return lookupChange({e:document.forms[0].elements["ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"]});
	}
	
	function disableNrNaoConformidade_cb(data, error) {
		if (data.length==0) {
			disableNrNaoConformidade(false);
			ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data);
		}
		return lookupExactMatch({e:document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"), data:data});
	}
	
	function loadNrNaoConformidade_cb(data, error) {
		ocorrenciaNaoConformidade_naoConformidade_nrNaoConformidade_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").value=data[0].filial.sgFilial;
		}
	}
	
	function disableNrNaoConformidade(valor) {
		if (valor == true && getElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade") == "") {
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", false);
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", true);
		}
		else
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", valor);
	}
	
	function enableNrNaoConformidade(data){
		if (data.nrNaoConformidade!=undefined) {
			disableNrNaoConformidade(false);
		} else {
			disableNrNaoConformidade(true);
		}
	}
	
	/**
	 * Controla o objeto de numero ocorrencia
	 */
	function loadOcorrencianNC_cb(data, error) {
		ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data);
		
		if (document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").isDisabled==true) {
		
			//busca os dados da URL
			var url = new URL(parent.location.href);
			var idOcorrenciaNaoConformidadeValue = url.parameters["ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"];
			if (idOcorrenciaNaoConformidadeValue!=undefined) {
				document.getElementById("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").value = idOcorrenciaNaoConformidadeValue;
			}
			
			document.getElementById("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").disabled=false;
		} else if (data.length==1) {
			document.getElementById("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").value=data[0].idOcorrenciaNaoConformidade;
		}
	}
</script>

