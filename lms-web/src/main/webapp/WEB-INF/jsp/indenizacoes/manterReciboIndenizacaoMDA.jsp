<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" >

	<adsm:form action="/indenizacoes/manterReciboIndenizacao" idProperty="idMdaSalvadoIndenizacao" service="lms.indenizacoes.manterReciboIndenizacaoAction.findByIdMda" onDataLoadCallBack="retornoFindById">

	    <adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
		    <adsm:masterLinkItem property="nrReciboComposto"   label="numeroRIM" itemWidth="50" />
	    </adsm:masterLink>

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilialByDocumentoServico" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrDoctoServico"
					 label="mda" dataType="text" labelWidth="20%" width="80%" 
					 size="3" maxLength="3" picker="false" serializable="false" 
					 >
			<adsm:lookup property="mda" idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupMDA" 
						 action="/pendencia/consultarMDA"
						 onDataLoadCallBack="carregaDadosMda" required="true"
						 onchange="return checkValueMda(this.value)"						 
						 afterPopupSetValue="onMdaAfterPopupSetValue"
						 dataType="integer" maxLength="10" size="10" mask="00000000" 
						 disabled="false">
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
				<adsm:propertyMapping relatedProperty="dhInclusao" modelProperty="dhInclusao"/>
			</adsm:lookup>					 
		</adsm:lookup>			
		
		
		<adsm:textbox label="dataInclusao" property="dhInclusao" dataType="JTDateTimeZone" labelWidth="20%" width="80%" picker="false" disabled="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="botaoStore" caption="salvarMDA" onclick="salvar_onClick(this.form);"/>
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="mdaSalvadoIndenizacao" 
				idProperty="idMdaSalvadoIndenizacao" 
				selectionMode="check" unique="true" 
				service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedMda" 
				rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountMda" 
				detailFrameName="mda" 
				rows="10"
				defaultOrder="mda_filialByIdFilialOrigem_.sgFilial, mda_.nrDoctoServico"
				>
		<adsm:gridColumnGroup separatorType="MDA">
			<adsm:gridColumn property="sgFilial" title="mda" width="30" />
			<adsm:gridColumn property="nrMda"    title=""    align="left" mask="00000000" dataType="integer" width="250"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn property="dhInclusao" title="dataInclusao" dataType="JTDateTimeZone" align="center" width="300"/>
		<adsm:editColumn title="hidden" property="idMda" dataType="integer" field="hidden" width="" />
		<adsm:gridColumn property="visualizar" title="visualizar" image="/images/printer.gif" width="100" link="javascript:visualizarMda" align="center" />

		<adsm:buttonBar>
			<adsm:removeButton caption="excluirMDA" service="lms.indenizacoes.manterReciboIndenizacaoAction.removeMdaByIds" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'masterId', getElementValue('masterId'));
		mdaSalvadoIndenizacaoGridDef.executeSearch(data);
	}	
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) {
			executeSearch();
		}
		setDisabled('mda.nrDoctoServico', false);
	}

	function onMdaPopupSetValue(data) {
		setElementValue('filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, 'filialByIdFilialOrigem.sgFilial'));		
	} 
	
	function onMdaAfterPopupSetValue(data) {
		setDisabled('mda.nrDoctoServico', false);
	    findDhInclusaoDocumento(data.idDoctoServico);
	    setElementValue('filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);
  	    setElementValue('filialByIdFilialOrigem.idFilial', data.filialByIdFilialOrigem.idFilial);
	    return false;
	}
	
	function findDhInclusaoDocumento(idDoctoServico) {
		var data = new Array(); 
		data.idDoctoServico = idDoctoServico;
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findDhInclusaoDocumento", "findDhInclusaoDocumento", data);
    	xmit({serviceDataObjects:[sdo]});	
	}
	
	function findDhInclusaoDocumento_cb(data, error) {
		if (error==undefined) {
			 setElementValue('dhInclusao', setFormat('dhInclusao', data.dhInclusao));
		}		
	}

	function onTabShow(fromTab) {
		executeSearch();
	}
	
	
	/**
	 * Funções referentes à filial da MDA
	 */
	function sgFilialOnChangeHandler() {	
		if (getElementValue("filialByIdFilialOrigem.sgFilial")=='') {
			setDisabled('mda.nrDoctoServico', true);
			resetValue("mda.idDoctoServico");
		} else {
			setDisabled('mda.nrDoctoServico', false);
		}
		return lookupChange({e:document.forms[0].elements["filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrDoctoServico_cb(data, error) {
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
       	    resetValue(document);
           	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		}		
		return true;
	}
	
	/**
	 * Funcao que faz a chamada do relatorio...
	 *
	 * @param idMda
	 */
	function visualizarMda(rowId) {
		event.cancelBubble = true;
		var data = mdaSalvadoIndenizacaoGridDef.getDataRowById(rowId);
		var dataObject = new Object();
		dataObject.idMda = data.idMda;
		
		var sdo = createServiceDataObject('lms.indenizacoes.manterReciboIndenizacaoAction','openPdf', dataObject); 
		executeReportWindowed(sdo, 'pdf');
		return false;
	}
	
	function initWindow(e) {
		setDisabled("botaoStore", false);
		document.getElementById("mda.nrDoctoServico").disabled = true;
	}
	
	function retornoFindById_cb(data, error){
		setElementValue("mda.nrDoctoServico", setFormat(document.getElementById("mda.nrDoctoServico") ,data.mda.nrDoctoServico));
		setElementValue("mda.idDoctoServico", data.mda.idDoctoServico);
		setElementValue("idMdaSalvadoIndenizacao", data.idMdaSalvadoIndenizacao);
		setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
		setElementValue("dhInclusao", setFormat(document.getElementById("dhInclusao"), data.dhInclusao));
		setDisabled('mda.nrDoctoServico', false);
	}

	function salvar_onClick(form) {
		if (!validateForm(form)) {
			setFocusOnFirstFocusableField();
			return false;
		}
		storeButtonScript('lms.indenizacoes.manterReciboIndenizacaoAction.saveMDA', 'storeCallback', form);
	}
</script>