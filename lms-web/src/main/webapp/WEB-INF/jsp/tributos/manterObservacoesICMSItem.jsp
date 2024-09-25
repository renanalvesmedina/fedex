<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterObservacoesICMSAction">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-30040" />
	</adsm:i18nLabels>
	
	<adsm:form action="/tributos/manterObservacoesICMS" idProperty="idObservacaoICMS"
		service="lms.tributos.manterObservacoesICMSAction.findByIdItem">
		<adsm:masterLink showSaveAll="true" idProperty="idDescricaoTributacaoIcms">
			<adsm:masterLinkItem property="unidadeFederativa.idUnidadeFederativa" label="uf"/>
			<adsm:masterLinkItem property="tipoTributacaoIcms.idTipoTributacaoIcms" label="tipoTributacao"/>			
		</adsm:masterLink>	
	
	
		<adsm:combobox label="tipo" property="tpObservacaoICMS" domain="DM_TIPO_OBSERVACAO_ICMS" required="true"/>	
        <adsm:range label="vigencia" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:textbox width="85%" maxLength="85" size="80" property="obObservacaoICMS" label="observacao" required="true" dataType="text"/>
        <adsm:textbox label="ordem" property="nrOrdemImpressao" dataType="integer" maxLength="99" mask="00" required="true"/>

		<adsm:textbox dataType="text" label="cdEmbLegalMasterSaf" disabled="true"
			property="cdEmbLegalMastersaf" size="20" maxLength="10"  width="80%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarObservacao" callbackProperty="retornoSalvarObservacoes" service="lms.tributos.manterObservacoesICMSAction.saveItem"/>		
			<adsm:button caption="limpar" onclick="limpar(this)" disabled="false" id="btnLimpar"/>	
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idObservacaoICMS" property="observacaoICMS" unique="true"
			autoSearch="false" rows="8" showGotoBox="true" showPagging="true" detailFrameName="item"
			service="lms.tributos.manterObservacoesICMSAction.findPaginatedItem"
			rowCountService="lms.tributos.manterObservacoesICMSAction.getRowCountItem" onRowClick="desabilitaTodosCampos">
		<adsm:gridColumn title="tipo" property="tpObservacaoICMS" isDomain="true" width="150" />
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" width="60" dataType="JTDate"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" width="60" dataType="JTDate"/>		
		<adsm:gridColumn title="observacao" property="obObservacaoICMS" width="400" dataType="text"/>
		<adsm:gridColumn title="ordem" property="nrOrdemImpressao" dataType="integer" mask="00"/>		
		<adsm:buttonBar> 
			<adsm:removeButton caption="excluirObservacao"
			service="lms.tributos.manterObservacoesICMSAction.removeByIdsItem"
			/>
		</adsm:buttonBar>
	</adsm:grid>		
</adsm:window>

<script>


	/**
	*	Se houver exception lança a excessão, caso contrário habilita os campos
	*/
	function retornoSalvarObservacoes_cb(data, erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocusOnFirstFocusableField();
			return false;
		}
		newButtonScript();
		showSuccessMessage();
		desabilitaTodosCampos(false);
		setFocusOnFirstFocusableField();
		observacaoICMSGridDef.executeSearch(null, true);
	}
	
	
	/**Executado quando a tela é iniciada
	**/
	function initWindow(event){
		
		if ( getElementValue("idObservacaoICMS") != "" ){
			newButtonScript();
		}
		setDisabled('btnLimpar', false);
		desabilitaTodosCampos(false);
		setFocusOnFirstFocusableField();
		
	}

	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/
	function desabilitaTodosCampos(val){
		if (val == undefined || (val != true  && val != false)){ 
			val = true;	
		}
		setDisabled("tpObservacaoICMS",val);
		setDisabled("dtVigenciaInicial",val);
		
		var   documento = getCurrentDocument("dtVigenciaInicial", null);
        var obj = getElement("dtVigenciaInicial", documento);
        setDisabledCalendar(obj, val , documento);
		
		setDisabled("obObservacaoICMS",val);
		setDisabled("nrOrdemImpressao",val);
	}

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(elem){	    
		newButtonScript();
		desabilitaTodosCampos(false);
		setFocusOnFirstFocusableField();
	}
	
</script>