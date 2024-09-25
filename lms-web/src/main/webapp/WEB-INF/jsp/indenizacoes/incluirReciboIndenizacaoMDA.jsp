<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" idProperty="idMdaSalvadoIndenizacao" service="lms.indenizacoes.incluirReciboIndenizacaoAction.findMdaById">

	    <adsm:masterLink idProperty="idReciboIndenizacao" >
		    <adsm:masterLinkItem property="reciboIndenizacao" label="RIM" itemWidth="50" />
	    </adsm:masterLink>

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupFilialByDocumentoServico" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="20%" width="80%" 
					 size="3" maxLength="3" picker="false" serializable="false">			
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupMDA" 
						 action="/pendencia/consultarMDA"
						 onDataLoadCallBack="carregaDadosMda"
						 onchange="return checkValueMda(this.value)"						 
						 afterPopupSetValue="onMdaAfterPopupSetValue"
						 dataType="integer" maxLength="10" size="10" mask="00000000" 
						 disabled="false">
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
				<adsm:propertyMapping relatedProperty="dhInclusao" modelProperty="dhInclusao" />
			</adsm:lookup>					 
		</adsm:lookup>			
		
		
		<adsm:textbox label="dataInclusao" property="dhInclusao" dataType="JTDateTimeZone" labelWidth="20%" width="80%" disabled="true" serializable="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarMDA" service="lms.indenizacoes.incluirReciboIndenizacaoAction.saveMDA" callbackProperty="storeCallback"/>
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="mdaSalvadoIndenizacao" 
				idProperty="idMdaSalvadoIndenizacao" 
				selectionMode="check" unique="true" 
				service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedMda" 
				rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountMda" 
				detailFrameName="mda"
				rows="12"
				defaultOrder="mda_filialByIdFilialOrigem_.sgFilial, mda_.nrDoctoServico"
				>
		<adsm:gridColumnGroup separatorType="MDA">
			<adsm:gridColumn property="sgFilial" title="mda" width="30" />
			<adsm:gridColumn property="nrMda"    title=""    align="left" mask="00000000" dataType="integer" width="340"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn property="dhInclusao" title="dataInclusao" dataType="JTDateTimeZone" align="center" width="350"/>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirMDA" service="lms.indenizacoes.incluirReciboIndenizacaoAction.removeMdaByIds"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>


   	var tabGroup = getTabGroup(this.document);
	var abaRim = tabGroup.getTab("rim");
	
	function initWindow(e) {	
		if (e.name=='newButton_click') {
			setDisabled('mda.nrDoctoServico', true);
		}
	}
	
	function pageLoad_cb() {
		setDisabled('mda.nrDoctoServico', true);
	}
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		data.masterId = getElementValue('masterId');
		mdaSalvadoIndenizacaoGridDef.executeSearch(data);
	}	
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) {
			resetValue(document);
			executeSearch();
		}
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
		executeSearch();
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
		} else {
			setElementValue('filialByIdFilialOrigem.sgFilial', data[0].filialByIdFilialOrigem.sgFilial);
			setDisabled('mda.nrDoctoServico', false);
			setFocus('mda.nrDoctoServico');
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