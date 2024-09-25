<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/indenizacoes/cancelarLiberacaoPagamentoRIM" height="105">

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		
		<adsm:hidden property="tpFormaPagamentoValue" serializable="false"/>
		
		<adsm:hidden property="idFilialLogada" serializable="false"/>
		<adsm:hidden property="blFilialIsMatriz" serializable="false"/>
		<adsm:hidden property="blFilialIsFilial" serializable="false"/>
		<adsm:hidden property="salarioMinimoVigente" serializable="false"/>
		<adsm:hidden property="diaProgramadoPagtoRIM" serializable="false"/>
		<adsm:hidden property="nrDiasCorridosValidacaoDtPagtoRIM" serializable="false"/>
		<adsm:hidden property="txtDiaSemana" serializable="false"/>
		<adsm:hidden property="tpIndenizacao.value" serializable="false"/>
		<adsm:hidden property="pessoaByIdBeneficiario.blMtzLiberaRIM" serializable="false"/>
		<adsm:hidden property="pessoaByIdFavorecido.blMtzLiberaRIM" serializable="false"/>
		
		<adsm:lookup label="numeroRIM" labelWidth="20%" width="32%" size="3" maxLength="3" 
						  picker="false" 
						  serializable="true"
    		              dataType="text"
    		              property="reciboIndenizacao.filial"
    		              idProperty="idFilial"
    		              popupLabel="pesquisarFilial"
    		              criteriaProperty="sgFilial"
					      service="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.findLookupFilial"
					      action="/municipios/manterFiliais"
					      onchange="return sgFilialOnChangeHandler();"
					      onDataLoadCallBack="disableNrRim">

			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      

			<adsm:lookup  dataType="integer"
			              property="reciboIndenizacao"
			              idProperty="idReciboIndenizacao"
			              serializable="true"
			              criteriaProperty="nrReciboIndenizacao"
						  service="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.findReciboIndenizacao"
						  action="/indenizacoes/manterReciboIndenizacao"
						  onPopupSetValue="onPopupCarregaDados"
						  maxLength="6"
						  size="6"
						  popupLabel="pesquisarRIM"
						  onDataLoadCallBack="populaForm"
						  onchange="return rimOnChangeHandler()"
						  mask="000000">
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="reciboIndenizacao.filial.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>		
		
		<adsm:textbox property="dtEmissao" label="dataEmissao" 
					  dataType="JTDate" width="31%" picker="false" 
					  disabled="true" serializable="false"/>

		<adsm:combobox label="motivoCancelamento" labelWidth="20%" width="80%" required="true"
					   autoLoad="false"
					   onlyActiveValues="true"
					   property="motivoCancelamento.idMotivoCancelamentoRim" 
					   optionLabelProperty="dsMotivoCancelamentoRim" 
					   optionProperty="idMotivoCancelamentoRim"
					   service="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.findComboMotivoCancelamento" 
					   />
					   
		<adsm:textbox property="tpStatusIndenizacao" label="status" labelWidth="20%" width="80%" 
					  disabled="true" dataType="text" serializable="false" size="25"/>

		<adsm:textbox property="tpIndenizacao" label="tipoIndenizacao" 
					  labelWidth="20%" width="80%" disabled="true" 
					  dataType="text" serializable="false"
					  size="25"/>

		<adsm:hidden property="tpStatusIndenizacaoValue" serializable="false"/>
		
		<adsm:textbox property="pessoaByIdBeneficiario.nrIdentificacaoFormatado" label="beneficiario" 
					  dataType="text" size="18" maxLength="18" labelWidth="20%" width="77%" 
					  disabled="true" serializable="false">
			<adsm:textbox property="pessoaByIdBeneficiario.nmPessoa" dataType="text" 
						  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="pessoaByIdFavorecido.nrIdentificacaoFormatado" label="favorecido" 
					  dataType="text" size="18" maxLength="18" labelWidth="20%" 
					  width="77%" disabled="true" serializable="false">
			<adsm:textbox property="pessoaByIdFavorecido.nmPessoa" dataType="text" 
						  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="moeda.dsSimbolo" 
					  label="valorIndenizacao" 
					  labelWidth="20%" width="80%" disabled="true" 
					  dataType="text" size="10" serializable="false">
			<adsm:textbox property="vlIndenizacao" dataType="currency"  
						  size="20" maxLength="20" 
						  disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="dtProgramadaPagamento" dataType="JTDate" 
					  label="dataProgramadaPagamento" labelWidth="20%" width="80%" 
					  picker="false" disabled="true" size="18" serializable="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="limpar" onclick="limpaTela()" id="limpar" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-21003"/>
			<adsm:include key="LMS-21004"/>
		</adsm:i18nLabels>
		
	</adsm:form>

	<adsm:grid idProperty="idDoctoServicoIndenizacao"  onDataLoadCallBack="verificaLiberacao"
			   property="doctosServicosIndenizacao" 
			   service="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.findDoctosServicosByRim"
			   rowCountService="lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.getRowCountDoctosServicosByRim"
			   selectionMode="none" 
			   onRowClick="disableRowClick"
			   gridHeight="200" unique="true"
			   rows="10">

		<adsm:gridColumn title="documentoServico" 	property="doctoServico.tpDocumentoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 	property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" 	property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="doctoServico.filialByIdFilialDestino.sgFilial" title="destino" width="15%" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" title="remetente" width="32%" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" title="destinatario" width="" />

		<adsm:buttonBar>
			<adsm:button caption="cancelarPagamento" id="cancelarPagamento" onclick="cancelaPagamento()"  />
			<adsm:button caption="cancelarLiberacao" id="cancelarLiberacao" onclick="cancelaLiberacao()"  />
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>

<script type="text/javascript">

function limpaTela() {
	cleanButtonScript(this.document);
	loadDataUsuario();
	limpaGrid();
}

function limpaGrid() {
	doctosServicosIndenizacaoGridDef.resetGrid();
}

function rimOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.nrReciboIndenizacao")=="") {
		var idFilial = getElementValue("reciboIndenizacao.filial.idFilial");
		var sgFilial = getElementValue("reciboIndenizacao.filial.sgFilial");
		
		resetValue(this.document);
		limpaGrid();
		
		
		setElementValue("reciboIndenizacao.filial.idFilial", idFilial);
		setElementValue("reciboIndenizacao.filial.sgFilial", sgFilial);
		
		resetValue("motivoCancelamento.idMotivoCancelamentoRim");
	}
	
	return reciboIndenizacao_nrReciboIndenizacaoOnChangeHandler();
}

function verificaLiberacao_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	var tpStatus = getElementValue("tpStatusIndenizacaoValue");
	var tpFormaPagto = getElementValue("tpFormaPagamentoValue");
	disableCancelButtons(tpStatus, tpFormaPagto);
}

function disableCancelButtons(tpStatus, tpFormaPagto) {
	var blFilialIsMatriz = getElementValue("blFilialIsMatriz");
	var isLiberaPagamento = validateLiberarPagamento();
	
	if(blFilialIsMatriz === "true" || isLiberaPagamento === true) {
		if(tpStatus=="L") {
			document.getElementById("cancelarLiberacao").disabled=false;
		} else {
			document.getElementById("cancelarLiberacao").disabled=true;
		}
	} else {
		document.getElementById("cancelarLiberacao").disabled=true;
	}
	
	if(blFilialIsMatriz === "true") {
		if(tpStatus=="P" && tpFormaPagto=="PU") {
			document.getElementById("cancelarPagamento").disabled=false;
		} else {
			document.getElementById("cancelarPagamento").disabled=true;
		}
	} else {
		document.getElementById("cancelarPagamento").disabled=true;
	}
}

function cancelaPagamento() {
	if (validateForm(document.forms[0])) {
		var msg = i18NLabel.getLabel("LMS-21003");
		
		if(confirm(msg) == true ) {
			var idReciboIndenizacao = getElementValue("reciboIndenizacao.idReciboIndenizacao");
			var idMotivoCancelamentoRim = getElementValue("motivoCancelamento.idMotivoCancelamentoRim");
			var map = new Array();
			setNestedBeanPropertyValue(map, "idReciboIndenizacao", idReciboIndenizacao);
			setNestedBeanPropertyValue(map, "idMotivoCancelamentoRim", idMotivoCancelamentoRim);
		    var sdo = createServiceDataObject("lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.executeCancelarPagamento", "resultadoCancelamento", map);
		    xmit({serviceDataObjects:[sdo]});
		}	
	}
}

function cancelaLiberacao() {
	if (validateForm(document.forms[0]))  {
		var msg = i18NLabel.getLabel("LMS-21004");
		
		if(confirm(msg) == true ) {
			var idReciboIndenizacao = getElementValue("reciboIndenizacao.idReciboIndenizacao");
			var idMotivoCancelamentoRim = getElementValue("motivoCancelamento.idMotivoCancelamentoRim");
			var map = new Array();
			setNestedBeanPropertyValue(map, "idReciboIndenizacao", idReciboIndenizacao);
			setNestedBeanPropertyValue(map, "idMotivoCancelamentoRim", idMotivoCancelamentoRim);
		    var sdo = createServiceDataObject("lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.executeCancelarLiberacao", "resultadoCancelamento", map);
		    xmit({serviceDataObjects:[sdo]});
		}
	}
}

function resultadoCancelamento_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	} else {
    	setElementValue('tpStatusIndenizacao', data.description);
    	setElementValue('tpStatusIndenizacaoValue', data.value);
    	showSuccessMessage();
		var tpStatus = getElementValue("tpStatusIndenizacaoValue");
		var tpFormaPagto = getElementValue("tpFormaPagamentoValue");
		disableCancelButtons(tpStatus, tpFormaPagto);
		if (tpStatus=="L"){
			carregaComboMotivoCancelamento();
		} else if (tpStatus=="G"){
			setDisabled(document.getElementById("motivoCancelamento.idMotivoCancelamentoRim"), true);
		}
    }
   	return false;    	
}

function disableRowClick() {
	return false;
}

function initWindow() {
	disableNrRim(true);
	document.getElementById("cancelarLiberacao").disabled=true;
	document.getElementById("cancelarPagamento").disabled=true;
}

/**
 * Controla o objeto RIM
 */	
function sgFilialOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.filial.sgFilial")=="") {
		disableNrRim(true);
		resetValue(this.document);
		limpaGrid();
	} else {
		disableNrRim(false);
	}
	return lookupChange({e:document.forms[0].elements["reciboIndenizacao.filial.idFilial"]});
}

function disableNrRim_cb(data, error) {
	if (data.length==0) {
	    disableNrRim(false);
	}
	return lookupExactMatch({e:document.getElementById("reciboIndenizacao.filial.idFilial"), data:data});
}

function disableNrRim(disable) {
	setDisabled(document.getElementById("reciboIndenizacao.nrReciboIndenizacao"), disable);
}

function populaForm_cb(data, error) {
	if(error!=undefined) {
		alert(error);
	}
	
	var retorno = reciboIndenizacao_nrReciboIndenizacao_exactMatch_cb(data);

	if (data.length > 0) {
		populateFields(data);
		loadDoctosServicosIndenizacaoGrid();
		carregaComboMotivoCancelamento();	
	}
	return retorno;
}

function carregaComboMotivoCancelamento() {
	var tpStatusIndenizacaoValue = getElementValue("tpStatusIndenizacaoValue");
	var map = new Array();
	setNestedBeanPropertyValue(map, "tpStatusIndenizacaoValue", tpStatusIndenizacaoValue);
	var sdo = createServiceDataObject("lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.findComboMotivoCancelamento", "motivoCancelamento.idMotivoCancelamentoRim", map);
	xmit({serviceDataObjects:[sdo]});
}

function loadDoctosServicosIndenizacaoGrid() {
	var filtros = buildFormBeanFromForm(this.document.forms[0]);
    doctosServicosIndenizacaoGridDef.executeSearch(filtros);
}

function populateFields(data) {
	if (data[0].idReciboIndenizacao!= undefined) {
	
		setElementValue('reciboIndenizacao.filial.sgFilial', data[0].filial.sgFilial);
		setElementValue('reciboIndenizacao.filial.idFilial', data[0].filial.idFilial);
	
		setElementValue('reciboIndenizacao.idReciboIndenizacao', setFormat(document.getElementById("reciboIndenizacao.idReciboIndenizacao"), data[0].idReciboIndenizacao));
		setElementValue('dtEmissao', setFormat(document.getElementById("dtEmissao"), data[0].dtEmissao));
		setElementValue('tpIndenizacao', setFormat(document.getElementById("tpIndenizacao"), data[0].tpIndenizacao.description));
		setElementValue('tpIndenizacao.value', data[0].tpIndenizacao.value);
		setElementValue('tpStatusIndenizacao', setFormat(document.getElementById("tpStatusIndenizacao"), data[0].tpStatusIndenizacao.description));	
		setElementValue('tpStatusIndenizacaoValue', data[0].tpStatusIndenizacao.value);
		setElementValue('tpFormaPagamentoValue', data[0].tpFormaPagamento.value);

		setElementValue('pessoaByIdBeneficiario.nrIdentificacaoFormatado', data[0].pessoaByIdBeneficiario.nrIdentificacaoFormatado);
		setElementValue('pessoaByIdBeneficiario.nmPessoa', setFormat(document.getElementById("pessoaByIdBeneficiario.nmPessoa"), data[0].pessoaByIdBeneficiario.nmPessoa));
		setElementValue('pessoaByIdBeneficiario.blMtzLiberaRIM', data[0].pessoaByIdBeneficiario.blMtzLiberaRIM);
		setElementValue('pessoaByIdFavorecido.blMtzLiberaRIM', data[0].pessoaByIdFavorecido.blMtzLiberaRIM);
	
		if (data[0].pessoaByIdFavorecido!=undefined) {
			setElementValue('pessoaByIdFavorecido.nrIdentificacaoFormatado', data[0].pessoaByIdFavorecido.nrIdentificacaoFormatado);
			setElementValue('pessoaByIdFavorecido.nmPessoa', setFormat(document.getElementById("pessoaByIdFavorecido.nmPessoa"), data[0].pessoaByIdFavorecido.nmPessoa));
		}
		setElementValue('vlIndenizacao', setFormat(document.getElementById("vlIndenizacao"), data[0].vlIndenizacao));
		setElementValue('dtProgramadaPagamento', setFormat(document.getElementById("dtProgramadaPagamento"), data[0].dtProgramadaPagamento));
		setElementValue('moeda.dsSimbolo', data[0].moeda.sgMoeda+" "+data[0].moeda.dsSimbolo);
	}
}

function onPopupCarregaDados(data) {
		setDisabled(document.getElementById("reciboIndenizacao.nrReciboIndenizacao"), false);
		var idReciboIndenizacao = data.idReciboIndenizacao;
		var map = new Array();
		setNestedBeanPropertyValue(map, "idReciboIndenizacao", idReciboIndenizacao);
	    var sdo = createServiceDataObject("lms.indenizacoes.liberarPagamentoIndenizacaoAction.findReciboIndenizacao", "populaForm", map);
	    xmit({serviceDataObjects:[sdo]});
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	loadDataUsuario();
}

function loadDataUsuario() {
	var sdo = createServiceDataObject("lms.indenizacoes.cancelarLiberacaoPagamentoRIMAction.getDataUsuario", "resultado_loadDataUsuario");
   	xmit({serviceDataObjects:[sdo]});
}

function resultado_loadDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("blFilialIsMatriz", getNestedBeanPropertyValue(data,"blFilialIsMatriz"));
	setElementValue("blFilialIsFilial", getNestedBeanPropertyValue(data,"blFilialIsFilial"));
	setElementValue("salarioMinimoVigente", getNestedBeanPropertyValue(data,"salarioMinimoVigente"));
	setElementValue("diaProgramadoPagtoRIM", getNestedBeanPropertyValue(data,"diaProgramadoPagtoRIM"));
	setElementValue("nrDiasCorridosValidacaoDtPagtoRIM", getNestedBeanPropertyValue(data,"nrDiasCorridosValidacaoDtPagtoRIM"));
	setElementValue("idFilialLogada", getNestedBeanPropertyValue(data,"idFilialLogada"));
	setElementValue("txtDiaSemana", getNestedBeanPropertyValue(data,"txtDiaSemana"));
}

function validateLiberarPagamento() {
	var blFilialIsFilial = getElementValue("blFilialIsFilial");
	var idFilialLogada = getElementValue("idFilialLogada");
	var idFilialReciboIndenizacao = getElementValue("reciboIndenizacao.filial.idFilial");
	var salarioMinimoVigente = parseFloat(getElementValue("salarioMinimoVigente"));
	var tpIndenizacao = getElementValue("tpIndenizacao.value");
	var tpFormaPagamento = getElementValue("tpFormaPagamentoValue");
	var vlIndenizacao = parseFloat(getElementValue("vlIndenizacao"));
	var blMtzLiberaRIMBeneficiario = getElementValue("pessoaByIdBeneficiario.blMtzLiberaRIM");
	var blMtzLiberaRIMFavorecido = getElementValue("pessoaByIdFavorecido.blMtzLiberaRIM");
	
	return (blFilialIsFilial === "true" && 
			idFilialLogada === idFilialReciboIndenizacao && 
			tpIndenizacao === "NC" && tpFormaPagamento === "DC" &&
			vlIndenizacao <= salarioMinimoVigente &&
			(blMtzLiberaRIMBeneficiario !== "true" || blMtzLiberaRIMFavorecido !== "true"));
}

</script>
