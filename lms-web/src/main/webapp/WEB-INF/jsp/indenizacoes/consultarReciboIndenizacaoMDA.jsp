<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" idProperty="idMdaSalvadoIndenizacao" service="lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdMda">
		
		<adsm:hidden property="reciboIndenizacao.idReciboIndenizacao"/>
		<adsm:complement label="numeroRIM" labelWidth="20%" width="80%" separator="branco">
		<adsm:textbox property="reciboIndenizacao.filial.sgFilial" dataType="text"        disabled="true" size="3"/>
		<adsm:textbox property="reciboIndenizacao.nrReciboIndenizacao" dataType="integer" disabled="true" size="8" mask="00000000"/>
		</adsm:complement>

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.indenizacoes.consultarReciboIndenizacaoAction.findLookupFilialByDocumentoServico" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" disabled="true"
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="20%" width="80%" 
					 size="3" maxLength="3" picker="false" serializable="false">			
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="lms.indenizacoes.consultarReciboIndenizacaoAction.findLookupMDA" 
						 action="/pendencia/consultarMDA" picker="false"
						 onDataLoadCallBack="carregaDadosMda"
						 onchange="return checkValueMda(this.value)"						 
						 afterPopupSetValue="onMdaAfterPopupSetValue"
						 dataType="integer" maxLength="10" size="10" mask="00000000" 
						 disabled="true">
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
				<adsm:propertyMapping relatedProperty="dhInclusao" modelProperty="dhInclusao"/>
			</adsm:lookup>					 
		</adsm:lookup>			
		
		
		<adsm:textbox label="dataInclusao" property="dhInclusao" dataType="JTDateTimeZone" labelWidth="20%" width="80%" disabled="true" picker="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="mdaSalvadoIndenizacao" 
				idProperty="idMdaSalvadoIndenizacao" 
				selectionMode="none" unique="true" 
				service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedMda" 
				rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountMda" 
				detailFrameName="mda"
				rows="11"				
				defaultOrder="mda_filialByIdFilialOrigem_.sgFilial, mda_.nrDoctoServico"
				>
		<adsm:gridColumnGroup separatorType="MDA">
			<adsm:gridColumn property="sgFilial" title="mda" width="30" />
			<adsm:gridColumn property="nrMda"    title=""    align="left" mask="00000000" dataType="integer" width="300"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn property="dhInclusao" title="dataInclusao" dataType="JTDateTimeZone" align="center" width="300"/>
		<adsm:editColumn title="hidden" property="mda.idDoctoServico" dataType="integer" field="hidden" width="" />
		<adsm:gridColumn property="visualizar" title="visualizar" image="/images/printer.gif" width="100" reportName="lms.pendencia.emitirMDAAction" linkIdProperty="mda.idDoctoServico" align="center" />

		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");

	document.getElementById('reciboIndenizacao.idReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.nrReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.filial.sgFilial').masterLink='true';	
	
	function initWindow(e) {	
		if (e.name=='newButton_click') {
			setDisabled('mda.nrDoctoServico', true);
		}
	}
	
	function pageLoad_cb() {
		document.getElementById('reciboIndenizacao.idReciboIndenizacao').masterLink='true';
		document.getElementById('reciboIndenizacao.nrReciboIndenizacao').masterLink='true';
		setDisabled('mda.nrDoctoServico', true);
	}
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'reciboIndenizacao.idReciboIndenizacao', getElementValue('reciboIndenizacao.idReciboIndenizacao'));
		mdaSalvadoIndenizacaoGridDef.executeSearch(data);
	}	
	
	function onMdaPopupSetValue(data) {
		setElementValue('filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, 'filialByIdFilialOrigem.sgFilial'));
		setDisabled('mda.nrDoctoServico', false);
		
	} 
	
	function onMdaAfterPopupSetValue(data) {
		var criteria = new Array();
	    setNestedBeanPropertyValue(criteria, "mda.idDoctoServico", getNestedBeanPropertyValue(data, 'idDoctoServico'));
	    document.getElementById("mda.nrDoctoServico").previousValue = undefined;
	    lookupChange({e:document.getElementById("mda.idDoctoServico"), forceChange:true});
	    return false;
	}

	function onTabShow(fromTab) {
		resetValue(document);
		setMasterLinkProperties();
		executeSearch();
	}
	
	// seta os valores masterLink
	function setMasterLinkProperties() {
		setElementValue('reciboIndenizacao.idReciboIndenizacao', abaDetalhamento.getFormProperty("idReciboIndenizacao"));
		setElementValue('reciboIndenizacao.filial.sgFilial',     abaDetalhamento.getFormProperty("sgFilialRecibo"));
		setElementValue('reciboIndenizacao.nrReciboIndenizacao', abaDetalhamento.getFormProperty("nrReciboIndenizacao"));
	}
	
	
	/**
	 * Funções referentes à filial da MDA
	 */
	function sgFilialOnChangeHandler() {	
		if (getElementValue("filialByIdFilialOrigem.sgFilial")=='') {
			setDisabled("mda.nrDoctoServico", true);
			resetValue("mda.idDoctoServico");
		} else {
			setDisabled("mda.nrDoctoServico", false);
		}
		return lookupChange({e:document.forms[0].elements["filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrDoctoServico_cb(data, error) {
		if (data.length == 0) {
			setDisabled("mda.nrDoctoServico", false);
		}
		return lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data});
	}

	/**
	 * Funções referentes aos dados da MDA
	 */
	function carregaDadosMda_cb(data) {
		mda_nrDoctoServico_exactMatch_cb(data);		
		if(data[0] == undefined) {
			resetValue("mda.nrDoctoServico");
			setFocus(document.getElementById("mda.nrDoctoServico"));
		}
	}

	function checkValueMda(valor) {		
		mda_nrDoctoServicoOnChangeHandler();
		if (valor == "") {			
			var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
   	        var sgFilial = getElementValue("filialByIdFilialOrigem.sgFilial");
       	    resetValue(this.document);
           	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		}		
		return true;
	}
	
</script>