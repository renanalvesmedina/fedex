<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.cancelarReciboRIMAction"
	onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-21051" />
	</adsm:i18nLabels>
	<adsm:form action="/indenizacoes/cancelarReciboRIM" height="212"
		idProperty="id">

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false" />
		<adsm:hidden property="reciboIndenizacao.filial.nmFilial" serializable="false"/>
		<adsm:hidden property="filialLogada.idFilial" serializable="false"/>
		<adsm:hidden property="blFilialUsuario" serializable="false"/>

		<adsm:lookup label="RIM" labelWidth="20%" width="32%" size="3"
			maxLength="3" picker="false" serializable="true" dataType="text"
			property="reciboIndenizacao.filial" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.indenizacoes.cancelarReciboRIMAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return sgFilialOnChangeHandler();"
			onDataLoadCallBack="disableNrRim" required="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" />
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping relatedProperty="reciboIndenizacao.filial.nmFilial" modelProperty="pessoa.nmFantasia"/>

			<adsm:lookup dataType="integer" property="reciboIndenizacao"
				idProperty="idReciboIndenizacao" serializable="true"
				criteriaProperty="nrReciboIndenizacao"
				service="lms.indenizacoes.cancelarReciboRIMAction.findReciboIndenizacao"
				action="/indenizacoes/manterReciboIndenizacao"
				onPopupSetValue="onRIMPopupSetValue" maxLength="8" size="8"
				onDataLoadCallBack="findReciboIndenizacao"
				onchange="return rimOnChangeHandler(this)" mask="00000000">
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="reciboIndenizacao.filial.idFilial" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="reciboIndenizacao.filial.sgFilial" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="reciboIndenizacao.filial.nmFilial" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox property="dtEmissao" label="dataEmissao"
			dataType="JTDate" width="31%" picker="false" disabled="true"
			serializable="false" />

		<adsm:hidden property="tpStatusIndenizacaoValue"/>
		<adsm:textbox property="tpStatusIndenizacao" label="situacao"
			width="80%" disabled="true" dataType="text" serializable="false" labelWidth="20%"
			size="25" />

		<adsm:combobox property="motivoCancelamento.idMotivoCancelamento"
			label="motivoCancelamento" optionProperty="idMotivoCancelamento"
			optionLabelProperty="dsMotivoCancelamento" onlyActiveValues="true" 
			service="lms.indenizacoes.cancelarReciboRIMAction.findComboMotivoCancelamento"
			labelWidth="20%" width="80%" serializable="true" required="true">
		</adsm:combobox>

		<adsm:textbox property="tpIndenizacao" label="tipoIndenizacao"
			labelWidth="20%" width="32%" disabled="true" dataType="text"
			serializable="false" />

		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="25%" width="75%" disabled="true" />
			<adsm:hidden property="idProcessoSinistro" />


		<adsm:textbox property="moeda.sgSimbolo" label="valorIndenizacao"
			labelWidth="20%" width="80%" disabled="true" dataType="text"
			size="10" serializable="false">
			<adsm:textbox property="vlIndenizacao" dataType="currency" size="20"
				maxLength="20" disabled="true" serializable="false" />
		</adsm:textbox>

		<%-- ============================================================ --%>
		<adsm:section caption="beneficiario" />
		<adsm:textbox label="tipoBeneficiario"
			property="tpBeneficiarioIndenizacao" dataType="text" labelWidth="20%"
			width="80%" disabled="true" />
		<adsm:lookup label="filial" labelWidth="20%" width="80%"
			property="filialBeneficiada" idProperty="idFilial" picker="false"
			criteriaProperty="sgFilial" action="/municipios/manterFiliais"
			service="lms.indenizacoes.consultarReciboIndenizacaoAction.findLookupFilial"
			dataType="text" size="3" maxLength="3" exactMatch="true"
			disabled="true" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping
				relatedProperty="filialBeneficiada.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialBeneficiada.pessoa.nmFantasia" serializable="false"
				size="67" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:lookup label="cliente" dataType="text"
			property="clienteBeneficiario" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao" picker="false"
			service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" exactMatch="false"
			size="20" maxLength="20" serializable="true" labelWidth="20%"
			width="80%" disabled="true">
			<adsm:propertyMapping
				relatedProperty="clienteBeneficiario.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text"
				property="clienteBeneficiario.pessoa.nmPessoa" size="50"
				maxLength="50" disabled="true" serializable="true" />
		</adsm:lookup>

		<adsm:complement label="terceiro" labelWidth="20%" width="80%"
			separator="branco" required="false">
			<adsm:textbox property="beneficiarioTerceiro.nrIdentificacao"
				dataType="text" disabled="true" size="20"/>
			<adsm:textbox property="beneficiarioTerceiro.nmPessoa"
				dataType="text" disabled="true" size="50" />
		</adsm:complement>
		<%-- ============================================================ --%>
		<adsm:section caption="favorecido" />
		<adsm:textbox label="tipoFavorecido" dataType="text"
			property="tpFavorecidoIndenizacao" labelWidth="20%" width="80%"
			disabled="true" />
		<adsm:lookup label="filial" labelWidth="20%" width="80%"
			property="filialFavorecida" picker="false" idProperty="idFilial"
			criteriaProperty="sgFilial" action="/municipios/manterFiliais"
			service="lms.indenizacoes.consultarReciboIndenizacaoAction.findLookupFilial"
			dataType="text" size="3" maxLength="3" exactMatch="true"
			disabled="true" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping
				relatedProperty="filialFavorecida.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialFavorecida.pessoa.nmFantasia" serializable="false"
				size="67" maxLength="50" disabled="true" />
		</adsm:lookup>
		<adsm:lookup label="cliente" dataType="text"
			property="clienteFavorecido" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao" disabled="true"
			picker="false"
			service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" exactMatch="false"
			size="20" maxLength="20" serializable="true" labelWidth="20%"
			width="80%">
			<adsm:propertyMapping
				relatedProperty="clienteFavorecido.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text"
				property="clienteFavorecido.pessoa.nmPessoa" size="50"
				maxLength="50" disabled="true" serializable="true" />
		</adsm:lookup>
		<adsm:complement label="terceiro" labelWidth="20%" width="80%"
			separator="branco" required="false">
			<adsm:textbox property="favorecidoTerceiro.nrIdentificacao"
				dataType="text" disabled="true" size="20"/>
			<adsm:textbox property="favorecidoTerceiro.nmPessoa" dataType="text"
				disabled="true" size="50" />
		</adsm:complement>
		<%-- ============================================================ --%>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="cancelarRIM" id="cancelarReciboButton"
				buttonType="storeButton" onclick="store();" disabled="false" />
			<adsm:button caption="limpar" onclick="limpaTela()" id="limpar"
				disabled="false" buttonType="resetButton" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idDoctoServicoIndenizacao"
		onDataLoadCallBack="verificaLiberacao"
		property="doctosServicosIndenizacao"
		service="lms.indenizacoes.cancelarReciboRIMAction.findDoctosServicosByRim"
		rowCountService="lms.indenizacoes.cancelarReciboRIMAction.getRowCountDoctosServicosByRim"
		selectionMode="none" onRowClick="disableRowClick" gridHeight="200"
		unique="true" rows="4">

		<adsm:gridColumn title="documentoServico"
			property="doctoServico.tpDocumentoServico" isDomain="true" width="45" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title=""
				property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" property="doctoServico.nrDoctoServico"
				width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn
			property="doctoServico.filialByIdFilialDestino.sgFilial"
			title="destino" width="115" />
		<adsm:gridColumn
			property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa"
			title="remetente" width="245" />
		<adsm:gridColumn
			property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"
			title="destinatario" width="245"  />

		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">

function pageLoad_cb() {
	onPageLoad_cb();
	setDisabled('cancelarReciboButton', true);disableNrRim(true);
	disableNrRim(true);
	document.getElementById('reciboIndenizacao.nrReciboIndenizacao').required = 'true';
}

function findByIdReciboIndenizacao() {
	var data = new Array();
	data.id = getElementValue('reciboIndenizacao.idReciboIndenizacao');
	var sdo = createServiceDataObject("lms.indenizacoes.cancelarReciboRIMAction.findByIdReciboIndenizacao", "findByIdReciboIndenizacao", data);
    xmit({serviceDataObjects:[sdo]});			
}

function findByIdReciboIndenizacao_cb(data, error) {
	if (error!=undefined) {
		alert(error);
		return;
	}
	
	onDataLoad_cb(data);
 	
	if (getElementValue('tpStatusIndenizacaoValue')=='G'
			&& (getElementValue('filialLogada.idFilial')== getElementValue('reciboIndenizacao.filial.idFilial') || getElementValue('blFilialUsuario') == 'true' ) )
		setDisabled('cancelarReciboButton', false);	
	else
		setDisabled('cancelarReciboButton', true);
	
}

function store() {

	var tab = getTab(document);
	var valid = tab.validate({name:"storeButton_click"});
	
	if (valid==false) {
		return false;
	}
	
	var result = confirm(i18NLabel.getLabel("LMS-21051"));
	
	if (result==false) {
		return false;
	}		

	var fb = buildFormBeanFromForm(this.document.forms[0]);    	
	var sdo = createServiceDataObject("lms.indenizacoes.cancelarReciboRIMAction.executeCancelarRIM", "storeCallback", fb);
    xmit({serviceDataObjects:[sdo]});		
}

function storeCallback_cb(data, error) {
	store_cb(data, error);
}

function limpaTela() {
	setDisabled('cancelarReciboButton', true);
	resetValue(this.document);	
	disableNrRim(true);	
	limpaGrid();	
	setFocusOnFirstFocusableField(document);
}

function limpaGrid() {
	doctosServicosIndenizacaoGridDef.resetGrid();
}

function rimOnChangeHandler(e) {
	if (e.value=="") {
		resetValue("reciboIndenizacao.idReciboIndenizacao");
		limpaGrid();
		var sgFilial = getElementValue('reciboIndenizacao.filial.sgFilial');
		var idFilial = getElementValue('reciboIndenizacao.filial.idFilial');
		var nmFilial = getElementValue('reciboIndenizacao.filial.nmFilial');
		resetValue(document);
		setElementValue('reciboIndenizacao.filial.sgFilial', sgFilial);
		setElementValue('reciboIndenizacao.filial.idFilial', idFilial);
		setElementValue('reciboIndenizacao.filial.nmFilial', nmFilial);
		setDisabled('cancelarReciboButton', true);						
	} 
	
	return reciboIndenizacao_nrReciboIndenizacaoOnChangeHandler();
}

function verificaLiberacao_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
}

function relatorioRim() {
	reportButtonScript('lms.indenizacoes.cancelarReciboRIMAction.execute', 'openPdf', document.forms[0]);
}


function disableRowClick() {
	return false;
}

/**
 * Controla o objeto RIM
 */	
function sgFilialOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.filial.sgFilial")=="") {
		disableNrRim(true);
		resetValue("reciboIndenizacao.idReciboIndenizacao");
		limpaGrid();
		resetValue(document);
		setDisabled('cancelarReciboButton', true);					
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

function findReciboIndenizacao_cb(data, error) {
	
	
	if(error!=undefined) {
		alert(error);
	}
	
	var retorno = reciboIndenizacao_nrReciboIndenizacao_exactMatch_cb(data);

	if (data.length > 0) {
		if (data[0].idReciboIndenizacao!= undefined) {
			setElementValue('reciboIndenizacao.filial.sgFilial', data[0].filial.sgFilial);
			setElementValue('reciboIndenizacao.filial.idFilial', data[0].filial.idFilial);
			setElementValue('reciboIndenizacao.idReciboIndenizacao', setFormat(document.getElementById("reciboIndenizacao.idReciboIndenizacao"), data[0].idReciboIndenizacao));
			setElementValue('dtEmissao', setFormat(document.getElementById("dtEmissao"), data[0].dtEmissao));
			setElementValue('tpIndenizacao', setFormat(document.getElementById("tpIndenizacao"), data[0].tpIndenizacao.description));
			
			setElementValue('tpStatusIndenizacao',      data[0].tpStatusIndenizacao.description);	
			setElementValue('tpStatusIndenizacaoValue', data[0].tpStatusIndenizacao.value);	
			setElementValue('filialLogada.idFilial', data[0].filialLogada.idFilial);
			setElementValue('blFilialUsuario', data[0].blFilialUsuario);
			
			setElementValue('vlIndenizacao', setFormat(document.getElementById("vlIndenizacao"), data[0].vlIndenizacao));
			setElementValue('moeda.sgSimbolo', data[0].moeda.sgMoeda+" "+data[0].moeda.dsSimbolo); 
			setElementValue('nrProcessoSinistro', data[0].nrProcessoSinistro); 

			findByIdReciboIndenizacao();				
		}

		var filtros = buildFormBeanFromForm(this.document.forms[0]);
	    doctosServicosIndenizacaoGridDef.executeSearch(filtros);
	    
	}
	return retorno;
}


function onRIMPopupSetValue(data) {
		setDisabled(document.getElementById("reciboIndenizacao.nrReciboIndenizacao"), false);
		var idReciboIndenizacao = data.idReciboIndenizacao;
		var map = new Array();
		setNestedBeanPropertyValue(map, "idReciboIndenizacao", idReciboIndenizacao);
	    var sdo = createServiceDataObject("lms.indenizacoes.cancelarReciboRIMAction.findReciboIndenizacao", "findReciboIndenizacao", map);
	    xmit({serviceDataObjects:[sdo]});
}

</script>
