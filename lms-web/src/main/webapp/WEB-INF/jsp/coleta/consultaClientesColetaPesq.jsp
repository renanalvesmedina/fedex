<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function carregaDados() {
    onPageLoad();
    carregaDadosFilial();
}
</script>   
<adsm:window service="lms.coleta.consultaClientesColetaAction" onPageLoad="carregaDados">
	<adsm:form action="/coleta/consultaClientesColeta" height="120" >
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>

		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" width="85%" required="true"
					 property="filial" exactMatch="true" 
					 service="lms.coleta.consultaClientesColetaAction.findLookupFiliais" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial"
                     serializable="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="cliente" width="85%" size="20" maxLength="20"
				     property="cliente" 
				     idProperty="idCliente"
				     action="/vendas/manterDadosIdentificacao"
				     service="lms.coleta.consultaClientesColetaAction.findLookupCliente" 
				     dataType="text"
				     criteriaProperty="pessoa.nrIdentificacao"
				     exactMatch="false"
				     onchange="return onChangeCliente()" 
				     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 onDataLoadCallBack="desabilitaCampos"
				     onPopupSetValue="lookupClienteSetValue"
				     >

			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>
		
		<adsm:lookup label="rota" width="85%" size="3" maxLength="3" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.consultaClientesColetaAction.findLookupRotaColetaEntrega"
					 action="/municipios/manterRotaColetaEntrega"
					 onchange="return onChangeLookupRota()"
					 onPopupSetValue="lookupRotaSetValue">
 		    <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
 		    <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" inlineQuery="false"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" size="30" serializable="false"/>
        </adsm:lookup>
        
		<adsm:combobox label="regiaoColeta" width="85%"
                       onchange="return onChangeRegiaoColeta()"
					   property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"
					   optionLabelProperty="dsRegiaoColetaEntregaFil"
					   optionProperty="idRegiaoColetaEntregaFil"
					   service="lms.coleta.consultaClientesColetaAction.findComboRegiao">
					   
			   <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
		</adsm:combobox> 


		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="clientes" id="btnConsultar"/>
			<adsm:button caption="limpar" onclick="limpaTela()" id="botaoLimpar" buttonType="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="clientes" idProperty="idCliente" gridHeight="200" onRowClick="disableRowClick" selectionMode="none"
	           service="lms.coleta.consultaClientesColetaAction.findPaginatedClientesColeta"
	           rowCountService="lms.coleta.consultaClientesColetaAction.getRowCountClientesColeta" rows="9">
		<adsm:gridColumn title="identificacao" property="tpIdentificacaoCliente" width="60" align="left" isDomain="true"/>
		<adsm:gridColumn title="" 			   property="nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="nome" 		   property="nmPessoaCliente" />
		<adsm:gridColumn title="ultimaColeta"  property="dhPedidoColeta" width="25%" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="coletas" 	   property="coletas" 	   image="images/popup.gif" width="10%" link="/coleta/consultaClientesColetaDadosColeta.do?cmd=pesq" openPopup="true" linkIdProperty="idCliente" align="center"/>
		<adsm:gridColumn title="dadosCliente" property="dadosCliente" image="/images/popup.gif" width="12%" link="/coleta/consultarColetasDadosCliente.do?cmd=main" linkIdProperty="idCliente" align="center"/>
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>
<script type="text/javascript">

function limpaTela() {
    cleanButtonScript(this.document);
    setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
    setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), false);
    setDisabled(document.getElementById("cliente.idCliente"), false);
    
}

function initWindow(eventObj) {
    if(eventObj.name=="cleanButton_click") {
        carregaDadosFilial();
    }
}

function lookupClienteSetValue(data) {
    if(data!=undefined) {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), true);
    }
}

function desabilitaCampos_cb(data,error){
	var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	if (retorno == true) onChangeCliente();
	return retorno;
}


function lookupRotaSetValue(data) {
    if(data!=undefined) {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
        setDisabled(document.getElementById("cliente.idCliente"), true);
    }
}


/**
 * função para o tratamento da seguinte situação:
 * caso informado o cliente, deve ser desabilitado a combo de região e a lookup rota
 */
function onChangeCliente(){
    var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();
    var idCliente = getElementValue("cliente.idCliente");
    if(idCliente!="") {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), true);
    } else {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), false);
    }
	return retorno;
}


/**
 * função para o tratamento da seguinte situação:
 * caso informado a rota, deve ser desabilitado a combo de região e a lookup cliente
 */
function onChangeLookupRota(){
    var retorno = rotaColetaEntrega_nrRotaOnChangeHandler();
    
    var nrRota = getElementValue("rotaColetaEntrega.nrRota");
    if(nrRota!="") {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
        setDisabled(document.getElementById("cliente.idCliente"), true);
//      setFocus(document.getElementById("btnConsultar"), true, true);
    } else {
        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
        setDisabled(document.getElementById("cliente.idCliente"), false);
    }
    return retorno;
}
/**
 * função para o tratamento da seguinte situação:
 * caso informado a regiao, deve ser desabilitado a lookup de rota e de cliente
 */
function onChangeRegiaoColeta() {
    
    var regiao = getElementValue("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil");
    if(regiao!="") {
        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), true);
        setDisabled(document.getElementById("cliente.idCliente"), true);
    } else {
        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), false);
        setDisabled(document.getElementById("cliente.idCliente"), false);
    }
}


/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}

/**
 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
 * Função necessária pois quando é selecionado um item na popup não é chamado
 * o serviço definido na lookup.
 */
 /* Deixado essa função pois a regra pode voltar
 function validaAcessoFilial(data) {
	var criteria = new Array();
    // Monta um map
    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
	
    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.findLookupFiliaisPorUsuario", "resultadoLookup", criteria);
    xmit({serviceDataObjects:[sdo]});
}
*/
/**
 * Função que trata o retorno da função validaAcessoFilial.
 */
 /* Deixado essa função pois a regra pode voltar
 function resultadoLookup_cb(data, error) {

	if (error != undefined) {
		alert(error);
	    setFocus(document.getElementById("filial.sgFilial"));
		return false;
	} else {
		filial_sgFilial_exactMatch_cb(data, error);
	}
}
*/
/**
 * Carrega a filial que o usuario está logado
 * 
 */
 function carregaDadosFilial() {
	var criteria = new Array();
	
    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.getFilialUsuarioLogado", "resultadoBusca", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBusca_cb(data, error) {
   
	if(data.idFilial==undefined || data.idFilial=="") {
		return false;
	} else {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
         var filtro = new Array();
        carregaComboRegiaoColeta(data.idFilial);
	}
}

/**
 * carrega combo de regiao coleta entrega
 */
 function carregaComboRegiaoColeta(idFilial){
    var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "filial.idFilial", idFilial);
    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.findComboRegiao", "regiaoColetaEntregaFil_idRegiaoColetaEntregaFil", filtro);
    xmit({serviceDataObjects:[sdo]});
}
</script>