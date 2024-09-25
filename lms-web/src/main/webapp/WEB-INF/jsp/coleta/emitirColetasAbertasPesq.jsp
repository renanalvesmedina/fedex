<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirColetasAbertasAction" onPageLoadCallBack="emitirColetas" >
	<adsm:form action="/coleta/emitirColetasAbertas">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" required="true"
					 property="filial" exactMatch="true" onPopupSetValue="validaAcessoFilial"
					 service="lms.coleta.emitirColetasAbertasAction.findLookupFiliaisPorUsuario" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial"
                     serializable="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
                     
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="rotaColetaEntrega" labelWidth="18%" width="82%" maxLength="3" size="3"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.emitirColetasAbertasAction.findLookupRotaColetaEntrega" 
					 action="/municipios/manterRotaColetaEntrega">
			 <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
        	<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" maxLength="50" size="50"/>
        </adsm:lookup>

		<adsm:combobox label="servico" labelWidth="18%" width="82%" onchange="onChangeComboServico()"
					   property="servico.idServico" 
					   service="lms.coleta.emitirColetasAbertasAction.findComboServico" 
					   optionLabelProperty="dsServico" 
					   optionProperty="idServico"/>

		<adsm:hidden property="dsServicoHidden" />
		
		<adsm:range label="dataPrevistaColeta" labelWidth="18%" width="82%">
			<adsm:textbox dataType="JTDate" property="dataInicial" />
			<adsm:textbox dataType="JTDate" property="dataFinal" />
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:button id="btnGerarExcel" caption="visualizar" onclick="onclick_imprimeCsv()"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript">

document.getElementById("filial.sgFilial").serializable="true";
document.getElementById("rotaColetaEntrega.dsRota").serializable="true";

function onChangeComboServico() {
	var comboServico= document.getElementById("servico.idServico");
	setElementValue('dsServicoHidden', comboServico.options[comboServico.selectedIndex].text);
}

function onclick_imprimeCsv() {
	executeReportWithCallback('lms.coleta.emitirColetasAbertasAction.executeExportacaoCsv', 'verificaEmissao', document.forms[0]);
}

function verificaEmissao_cb(strFile, error) {
	reportUrl = contextRoot+"/viewBatchReport?open=false&"+strFile._value;
	location.href(reportUrl);
}

/**
 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
 * Função necessária pois quando é selecionado um item na popup não é chamado
 * o serviço definido na lookup.
 */
 function validaAcessoFilial(data) {
	var criteria = new Array();
    // Monta um map
    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
	
    var sdo = createServiceDataObject("lms.coleta.emitirColetasAbertasAction.findLookupFiliaisPorUsuario", "resultadoLookup", criteria);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Função que trata o retorno da função validaAcessoFilial.
 */
function resultadoLookup_cb(data, error) {

	if (error != undefined) {
		alert(error);
	    setFocus(document.getElementById("filial.sgFilial"));
		return false;
	} else {
		filial_sgFilial_exactMatch_cb(data, error);
	}
}

/**
 * Verifica se o usuario tem acesso a mais de uma filial. Caso tenha acesso
 * a apenas uma, a lookup deve vir preenchida.
 */
 function initWindow(eventObj) {
	
	setDisabled("btnGerarExcel", false);	
	
	var criteria = new Array();
    var sdo = createServiceDataObject("lms.coleta.emitirColetasAbertasAction.verificaAcessoFilial", "resultadoBusca", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBusca_cb(data, error) {
	if(data.idFilial==undefined || data.idFilial=="") {
		return false;
	} else {
		setElementValue('filial.idFilial', data.filial.idFilial);
		setElementValue('filial.sgFilial', data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);		
	}
}

function emitirColetas_cb() {
   onPageLoad_cb();
   setMasterLink(document, true);
}
</script>
