<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.liberarPagamentoIndenizacaoAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/indenizacoes/liberarPagamentoIndenizacao" height="105">

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="blFilialIsMatriz" serializable="false"/>
		<adsm:hidden property="blFilialIsFilial" serializable="false"/>
		<adsm:hidden property="idFilialLogada" serializable="false"/>
		<adsm:hidden property="salarioMinimoVigente" serializable="false"/>
		<adsm:hidden property="diaProgramadoPagtoRIM" serializable="false" />
		<adsm:hidden property="nrDiasCorridosValidacaoDtPagtoRIM" serializable="false" />
		<adsm:hidden property="txtDiaSemana" serializable="false" />
		
		<adsm:lookup label="numeroRIM" labelWidth="20%" width="32%" size="3" maxLength="3" 
						  picker="false" 
						  serializable="true"
    		              dataType="text"
    		              property="reciboIndenizacao.filial"
    		              idProperty="idFilial"
    		              criteriaProperty="sgFilial"
					      service="lms.indenizacoes.liberarPagamentoIndenizacaoAction.findLookupFilial"
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
						  service="lms.indenizacoes.liberarPagamentoIndenizacaoAction.findReciboIndenizacao"
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

		<adsm:textbox property="tpIndenizacao" label="tipoIndenizacao" 
					  labelWidth="20%" width="32%" disabled="true" 
					  dataType="text" serializable="false"/>
					  
		<adsm:textbox property="tpStatusIndenizacao" label="status" width="31%" 
					  disabled="true" dataType="text" serializable="false"
					  size="25"/>

		<adsm:hidden property="tpStatusIndenizacaoValue" serializable="false"/>
		<adsm:hidden property="tpFormaPagamentoValue" serializable="false"/>
		<adsm:hidden property="tpSituacaoWorkflow" serializable="false"/>
		
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
					  required="true" size="18" serializable="false"
					  onchange="return dtProgramadaPagamento_change(this)" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="limpar" onclick="limpaTela()" id="limpar" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-21001"/>
			<adsm:include key="LMS-21060"/>
			<adsm:include key="LMS-21063"/>
			<adsm:include key="LMS-21091"/>
			<adsm:include key="LMS-21093"/>
		</adsm:i18nLabels>

	</adsm:form>

	<adsm:grid idProperty="idDoctoServicoIndenizacao"  onDataLoadCallBack="verificaLiberacao"
			   property="doctosServicosIndenizacao" 
			   service="lms.indenizacoes.liberarPagamentoIndenizacaoAction.findDoctosServicosByRim"
			   rowCountService="lms.indenizacoes.liberarPagamentoIndenizacaoAction.getRowCountDoctosServicosByRim"
			   selectionMode="none" 
			   onRowClick="disableRowClick"
			   gridHeight="200" unique="true"
			   rows="9">

		<adsm:gridColumn title="documentoServico" 	property="doctoServico.tpDocumentoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 	property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" 	property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="doctoServico.filialByIdFilialDestino.sgFilial" title="destino" width="115" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" title="remetente" width="230" />
		<adsm:gridColumn property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" title="destinatario" width="" />

		<adsm:buttonBar>
			<adsm:button caption="enviarJDE" id="enviarJDE" onclick="enviarRimJDE();" disabled="false" />
			<adsm:button caption="liberarPagamento" id="liberarPagamento" onclick="liberaPagamento()" disabled="true"/>
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>

<script type="text/javascript">

function enviarRimJDE(){

	var msg = i18NLabel.getLabel("LMS-21060");
	if(confirm(msg)){

		var sdo = createServiceDataObject("lms.indenizacoes.liberarPagamentoIndenizacaoAction.executeEnvioJDE", "enviarRimJDE");
	   	xmit({serviceDataObjects:[sdo]});
		
	}
}

function enviarRimJDE_cb(data, error){
	if(error!=undefined) {
		alert(error);
	} else {
		var msg = i18NLabel.getLabel("LMS-21063");
    	alert(msg);
    }
   	return false;	
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	loadDataUsuario();
}

function loadDataUsuario() {
	var sdo = createServiceDataObject("lms.indenizacoes.liberarPagamentoIndenizacaoAction.getDataUsuario", "resultado_loadDataUsuario");
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
	disabledBtnEnvioJde();
}

function disabledBtnEnvioJde() {
	var isFilialMatriz = getElementValue("blFilialIsMatriz");
	setDisabled("enviarJDE", (isFilialMatriz==="false"));
}

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
		resetValue("reciboIndenizacao.idReciboIndenizacao");
		resetaCamposByRim()
		limpaGrid();
		setDisabled("liberarPagamento", true);
	}
	return reciboIndenizacao_nrReciboIndenizacaoOnChangeHandler();
}


var rim = null;
function verificaLiberacao_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	
	var isDisabledLiberarPagamento = true;
	var tpStatusIndenizacao = getElementValue("tpStatusIndenizacaoValue");
	var tpSituacaoWorkflow = getElementValue("tpSituacaoWorkflow");
	var tpFormaPagamento = getElementValue("tpFormaPagamentoValue");
	var blFilialIsMatriz =  getElementValue("blFilialIsMatriz");
	
	if(data[0] !== undefined) {
		rim = data[0];
	}
	
	var isLiberarPagamento = validateLiberarPagamento();
	
	if(blFilialIsMatriz === "true" || isLiberarPagamento === true) {
		if((tpStatusIndenizacao == "G" ||
			tpStatusIndenizacao == "T" ||
			tpStatusIndenizacao == "E") &&
			tpSituacaoWorkflow == "A" &&
			tpFormaPagamento != "PU"){
			isDisabledLiberarPagamento = false;
		}
	}
	
	setDisabled("liberarPagamento", isDisabledLiberarPagamento);
	disabledBtnEnvioJde();
}

function validateLiberarPagamento() {
	var blFilialIsFilial = getElementValue("blFilialIsFilial");
	var idFilialLogada = getElementValue("idFilialLogada");
	var salarioMinimoVigente = parseFloat(getElementValue("salarioMinimoVigente"));
	
	return (blFilialIsFilial === "true" && 
			(rim !== null && rim !== undefined) &&
			idFilialLogada === rim.filial.idFilial && 
			rim.tpIndenizacao.value == "NC" && rim.tpFormaPagamento.value == "DC" &&
			parseFloat(rim.vlIndenizacao) <= salarioMinimoVigente &&
			(rim.pessoaByIdBeneficiario.blMtzLiberaRIM !== "true" || rim.pessoaByIdFavorecido.blMtzLiberaRIM !== "true"));
}

function liberaPagamento() {
	
	var tab = getTab(document);
	var valid = tab.validate({name:"storeButton_click"});
	
	if (valid == false) {
		return false;
	}
	
	var validDtProgramadaPagto = dtProgramadaPagamento_change(getElement("dtProgramadaPagamento"));	

	if(validDtProgramadaPagto == true) { 
		var msg = i18NLabel.getLabel("LMS-21001");
		if(confirm(msg) == true ) {
			var idReciboIndenizacao = getElementValue("reciboIndenizacao.idReciboIndenizacao");
			var dtProgramadaPagamento = getElementValue("dtProgramadaPagamento"); 
			var map = new Array();
			setNestedBeanPropertyValue(map, "idReciboIndenizacao", idReciboIndenizacao);
			setNestedBeanPropertyValue(map, "dtProgramadaPagamento", dtProgramadaPagamento);
		    var sdo = createServiceDataObject("lms.indenizacoes.liberarPagamentoIndenizacaoAction.executeLiberaPagamento", "resultadoExecuteLiberaPagamento", map);
		    xmit({serviceDataObjects:[sdo]});
		}
	}
}

function resultadoExecuteLiberaPagamento_cb(data, error){
	if(error!=undefined) {
		alert(error);
	} else {
    	setElementValue('tpStatusIndenizacao', data.tpStatusIndenizacao);
    	setElementValue('tpStatusIndenizacaoValue', data.tpStatusIndenizacaoValue);
    	setElementValue('tpFormaPagamentoValue', data.tpFormaPagamentoValue);
    	setElementValue('dtProgramadaPagamento', setFormat(document.getElementById("dtProgramadaPagamento"), data.dtProgramadaPagamento));
    	
        if (data.tpSituacaoWorkflow) {
	    	setElementValue('tpSituacaoWorkflow', data.tpSituacaoWorkflow);
	    }
    	showSuccessMessage();
    }
   	return false;    	
}

function disableRowClick() {
	return false;
}

function initWindow() {
	disabledBtnEnvioJde();
	disableNrRim(true);
}

/**
 * Controla o objeto RIM
 */	
function sgFilialOnChangeHandler() {
	if (getElementValue("reciboIndenizacao.filial.sgFilial")=="") {
		disableNrRim(true);
		resetValue("reciboIndenizacao.idReciboIndenizacao");
		resetaCamposByRim()
		limpaGrid();
		setDisabled("liberarPagamento", true);
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
		resetaCamposByRim();
		populateFields(data);
		loadDoctosServicosIndenizacaoGrid();
		
		verificaLiberacao_cb(data, error);		
	}else{
				
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
		setElementValue('tpFormaPagamentoValue', data[0].tpFormaPagamento.value);	
		setElementValue('pessoaByIdBeneficiario.nrIdentificacaoFormatado', data[0].pessoaByIdBeneficiario.nrIdentificacaoFormatado);
		setElementValue('pessoaByIdBeneficiario.nmPessoa', setFormat(document.getElementById("pessoaByIdBeneficiario.nmPessoa"), data[0].pessoaByIdBeneficiario.nmPessoa));
		setElementValue('vlIndenizacao', setFormat(document.getElementById("vlIndenizacao"), data[0].vlIndenizacao));
		setElementValue('dtProgramadaPagamento', setFormat(document.getElementById("dtProgramadaPagamento"), data[0].dtProgramadaPagamento));
		setElementValue('moeda.dsSimbolo', data[0].moeda.sgMoeda+" "+data[0].moeda.dsSimbolo);
		if (data[0].tpSituacaoWorkflow) {
			setElementValue('tpSituacaoWorkflow', data[0].tpSituacaoWorkflow.value);
		}
		if (data[0].pessoaByIdFavorecido!=undefined) {
			setElementValue('pessoaByIdFavorecido.nrIdentificacaoFormatado', data[0].pessoaByIdFavorecido.nrIdentificacaoFormatado);
			setElementValue('pessoaByIdFavorecido.nmPessoa', setFormat(document.getElementById("pessoaByIdFavorecido.nmPessoa"), data[0].pessoaByIdFavorecido.nmPessoa));
		}
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

function resetaCamposByRim() {
	resetValue("dtEmissao");
	resetValue("tpIndenizacao");
	resetValue("tpStatusIndenizacao");
	resetValue("tpStatusIndenizacaoValue");
	resetValue("tpFormaPagamentoValue");
	resetValue("tpSituacaoWorkflow");
	resetValue("pessoaByIdBeneficiario.nrIdentificacaoFormatado");
	resetValue("pessoaByIdBeneficiario.nmPessoa");
	resetValue("pessoaByIdFavorecido.nrIdentificacaoFormatado");
	resetValue("pessoaByIdFavorecido.nmPessoa");
	resetValue("moeda.dsSimbolo");
	resetValue("vlIndenizacao");
	resetValue("dtProgramadaPagamento");
}

function dtProgramadaPagamento_change(obj) {
	if (obj.value != "") {
		var dtProgramadaPagamento = stringToDate(obj.value, obj.mask);
		if (dtProgramadaPagamento != "0") {
			var currentDate = new Date();
			var time1 = LZ(dtProgramadaPagamento.getYear())+LZ(dtProgramadaPagamento.getMonth())+LZ(dtProgramadaPagamento.getDate());
			var time2 = LZ(currentDate.getFullYear())+LZ(currentDate.getMonth())+LZ(currentDate.getDate());
			
			var dateProgramada = new Date(dtProgramadaPagamento.getYear(),dtProgramadaPagamento.getMonth(),dtProgramadaPagamento.getDate());
			var intervalo = getDiferencaEntreDatas(currentDate, dateProgramada); 
	
			if (time1 < time2) {
				alert("LMS-21091 - " + getI18nMessage("LMS-21091"))
				obj.value = "";
				return false;
			}
			
			var isLiberarPagamento = validateLiberarPagamento();
			var diaProgramadoPagtoRIM = parseInt(getElementValue("diaProgramadoPagtoRIM"));
			var nrDiasCorridosValidacaoDtPagtoRIM = parseInt(getElementValue("nrDiasCorridosValidacaoDtPagtoRIM"));
			var txtDiaSemana = getElementValue("txtDiaSemana");
			var blFilialIsMatriz =  getElementValue("blFilialIsMatriz");
			
			
			if(blFilialIsMatriz === "false" &&
					(isLiberarPagamento === true && 
					diaProgramadoPagtoRIM !== dateProgramada.getUTCDay() ||
					(diaProgramadoPagtoRIM === dateProgramada.getUTCDay() && intervalo < nrDiasCorridosValidacaoDtPagtoRIM ))) {
				
				alert("LMS-21093 - " + getI18nMessage("LMS-21093", new Array(nrDiasCorridosValidacaoDtPagtoRIM, txtDiaSemana), false));
				
				return false;
			}
			
		}
	}
	return true;
}

function getDiferencaEntreDatas(currentDate, dateFinal) {
	var one_day=1000*60*60*24;
	return Math.ceil((dateFinal.getTime()-currentDate.getTime())/(one_day));
}

</script>