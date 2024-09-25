<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/indenizacoes/emitirReciboRIM" height="130">

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		
		<adsm:lookup label="numeroRIM" labelWidth="20%" width="32%" size="3" maxLength="3" 
						  picker="false" 
						  serializable="true"
    		              dataType="text"
    		              property="reciboIndenizacao.filial"
    		              idProperty="idFilial"
    		              criteriaProperty="sgFilial"
					      service="lms.indenizacoes.emitirReciboRIMAction.findLookupFilial"
					      action="/municipios/manterFiliais"
					      popupLabel="pesquisarFilial"
					      onchange="return sgFilialOnChangeHandler();"
					      onDataLoadCallBack="disableNrRim">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
						
			<adsm:lookup  dataType="integer"
			              property="reciboIndenizacao"
			              idProperty="idReciboIndenizacao"
			              serializable="true"
			              criteriaProperty="nrReciboIndenizacao"
			              popupLabel="pesquisarRIM"
						  service="lms.indenizacoes.emitirReciboRIMAction.findReciboIndenizacao"
						  action="/indenizacoes/manterReciboIndenizacao"
						  onPopupSetValue="onPopupCarregaDados"
						  maxLength="6"
						  size="6"
						  onDataLoadCallBack="populaForm"
						  onchange="return rimOnChangeHandler()"
						  mask="000000">
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="reciboIndenizacao.filial.idFilial" disable="true"/>
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="reciboIndenizacao.filial.sgFilial" disable="true" inlineQuery="false"/>					
				
			</adsm:lookup>
		</adsm:lookup>		
		
		<adsm:textbox property="dtEmissao" label="dataEmissao" 
					  dataType="JTDate" width="31%" picker="false" 
					  disabled="true" serializable="false"/>

		<adsm:textbox property="tpIndenizacao" label="tipoIndenizacao" 
					  labelWidth="20%" width="32%" disabled="true" 
					  dataType="text" serializable="false"/>
					  
		<adsm:textbox property="tpStatusIndenizacao" label="status" width="31%" 
					  disabled="true" dataType="text" serializable="false"
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
			<adsm:include key="LMS-21011"/>
		</adsm:i18nLabels>

	</adsm:form>

	<adsm:grid idProperty="idDoctoServicoIndenizacao"  onDataLoadCallBack="verificaLiberacao"
			   property="doctosServicosIndenizacao" 
			   service="lms.indenizacoes.emitirReciboRIMAction.findDoctosServicosByRim"
			   rowCountService="lms.indenizacoes.emitirReciboRIMAction.getRowCountDoctosServicosByRim"
			   selectionMode="none" 
			   onRowClick="disableRowClick"
			   gridHeight="160" unique="true"
			   rows="7">

		<adsm:gridColumn title="documentoServico" 	property="doctoServico.tpDocumentoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 	property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" 	property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="doctoServico.filialByIdFilialDestino.sgFilial" title="destino" width="15%" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" title="remetente" width="32%" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" title="destinatario" width="" />

		<adsm:buttonBar>
			<adsm:button caption="emitirRecibo" id="emitirRecibo" onclick="emiteRecibo()" disabled="true"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">

function limpaTela() {
	cleanButtonScript(this.document);
	limpaGrid();
}

function limpaGrid() {
	doctosServicosIndenizacaoGridDef.resetGrid();
}

function rimOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.nrReciboIndenizacao")=="") {
		var sgFilial = getElementValue('reciboIndenizacao.filial.sgFilial');
		var idFilial = getElementValue('reciboIndenizacao.filial.idFilial');
		resetValue(document);
		setElementValue('reciboIndenizacao.filial.sgFilial', sgFilial);
		setElementValue('reciboIndenizacao.filial.idFilial', idFilial);
		limpaGrid();
		setDisabled("emitirRecibo", true);
	}
	
	return reciboIndenizacao_nrReciboIndenizacaoOnChangeHandler();
}

function verificaLiberacao_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	var tpStatus = getElementValue("tpStatusIndenizacaoValue");

	if(tpStatus!="P") {
		setDisabled("emitirRecibo", false);
	}else {
		setDisabled("emitirRecibo", true);
	}
}

function emiteRecibo() {
	var msg = i18NLabel.getLabel("LMS-21011");
	if(confirm(msg) == true ) {
		var fields = buildFormBeanFromForm(this.document.forms[0]);
	    var sdo = createServiceDataObject("lms.indenizacoes.emitirReciboRIMAction.executeEmitirReciboRIM", "resultadoExecuteEmitirRecibo", fields);
	    xmit({serviceDataObjects:[sdo]});
	}
}

function resultadoExecuteEmitirRecibo_cb(data, error){
	if(error!=undefined) {
		alert(error);
	} else {
    	setElementValue('tpStatusIndenizacao', data.description);
    	setElementValue('tpStatusIndenizacaoValue', data.value);
    	showSuccessMessage();
    	relatorioRim();
    }
   	return false;    	
}

function relatorioRim() {
	reportButtonScript('lms.indenizacoes.emitirReciboRIMAction.execute', 'openPdf', document.forms[0]);
}

function disableRowClick() {
	return false;
}

function initWindow() {
	disableNrRim(true);
}

/**
 * Controla o objeto RIM
 */	
function sgFilialOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.filial.sgFilial")=="") {
		disableNrRim(true);
		resetValue(document);
		limpaGrid();
		setDisabled("emitirRecibo", true);
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
	}
	return retorno;
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
		setElementValue('tpStatusIndenizacao', setFormat(document.getElementById("tpStatusIndenizacao"), data[0].tpStatusIndenizacao.description));	
		setElementValue('tpStatusIndenizacaoValue', data[0].tpStatusIndenizacao.value);		
	
		setElementValue('pessoaByIdBeneficiario.nrIdentificacaoFormatado', data[0].pessoaByIdBeneficiario.nrIdentificacaoFormatado);
		setElementValue('pessoaByIdBeneficiario.nmPessoa', setFormat(document.getElementById("pessoaByIdBeneficiario.nmPessoa"), data[0].pessoaByIdBeneficiario.nmPessoa));
	
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

</script>
