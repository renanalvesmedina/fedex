<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaClientePageLoad() {
	onPageLoad();
	setMasterLink(this.document, true);
	mostraEscondeBotaoFechar();
	carregaDadosCliente();
}
</script>


<adsm:window title="dadosColeta" service="lms.coleta.consultaClientesColetaAction" onPageLoad="carregaClientePageLoad">
	<adsm:form action="/coleta/consultarColetasDadosCliente">
		<adsm:section caption="dadosCliente" />
				<adsm:textbox label="cliente" labelWidth="18%" width="82%" disabled="true" serializable="false"
					  property="cliente.pessoa.nrIdentificacao"
					  dataType="text" 
					  size="20" maxLength="20">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:hidden property="idCliente"/>
		<adsm:hidden property="cliente.idCliente"/>

		<adsm:textbox property="dhPedidoColeta" label="dataUltimaColeta" dataType="JTDateTimeZone" picker="false" labelWidth="18%" width="82%" disabled="true" />
		<adsm:textbox property="filial.sgFilial" size="3" label="numeroUltimaColeta" dataType="text" labelWidth="18%" width="82%" disabled="true">
		    <adsm:textbox property="nrColeta" dataType="integer" disabled="true" size="8" mask="00000000"/>
        </adsm:textbox>
	</adsm:form>
	
	
	<adsm:grid idProperty="idEnderecoPessoa" 
	           property="enderecosColeta" 
	           selectionMode="none" 
	           rows="4" 
	           scrollBars="horizontal"
	           service="lms.coleta.consultaClientesColetaAction.findPaginatedEnderecosColeta"
	           rowCountService="lms.coleta.consultaClientesColetaAction.getRowCountEnderecosColeta"
	           onRowClick="fillGridColetasAutomaticas"
	           gridHeight="80" unique="false" title="enderecosColeta">
		<adsm:gridColumn title="endereco"    property="dsEndereco"     width="200"/>
		<adsm:gridColumn title="numero"      property="nrEndereco"     width="70" align="right"/>
		<adsm:gridColumn title="complemento" property="dsComplemento"  width="100"/>
		<adsm:gridColumn title="bairro"      property="dsBairro"       width="120"/>
		<adsm:gridColumn title="cep"         property="nrCep"          width="80" align="right" />
		<adsm:gridColumn title="municipio"   property="nmMunicipio"    width="150" />
		<adsm:gridColumn title="uf"          property="sgUnidadeFederativa" width="70" />
		<adsm:gridColumn title="rota"        property="dsRota"         width="120" />
		<adsm:gridColumn title="regiao"      property="dsRegiao"       width="120" />

		<%--adsm:gridColumn title="telefones"   property="telefones"      width="100" openPopup="true" link="/coleta/cadastrarPedidoColeta.do?cmd=selecionarTelefone" linkIdProperty="idEnderecoPessoa" image="/images/popup.gif" align="center" /--%>
		<adsm:gridColumn title="telefones"   property="telefones"      width="100" openPopup="true" link="javascript:exibirTelefones" image="/images/popup.gif" align="center" />		
	</adsm:grid>

	<adsm:grid idProperty="idColetaAutomatica" 
	           property="coletasAutomaticas" 
	           service="lms.coleta.consultaClientesColetaAction.findPaginatedColetasAutomatica"
	           rowCountService=""
	           showPagging="false"
	           selectionMode="none" 
	           onRowClick="disableRowClick"
	           scrollBars="vertical"
	           gridHeight="120" unique="false" title="coletaAutomatica"
	>
		<adsm:gridColumn title="dia"         property="tpDiaSemana" width="40%" isDomain="true"/>
		<adsm:gridColumn title="horaInicial" property="hrChegada"   width="30%" align="center" dataType="JTTime"/>
		<adsm:gridColumn title="horaFinal"   property="hrSaida"     width="30%" align="center" dataType="JTTime"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
	</adsm:buttonBar>
</adsm:window>

<script>
function exibirTelefones(id){
	var data = enderecosColetaGridDef.getDataRowById(id);
	var nrIdentificacaoCliente = getElementValue('cliente.pessoa.nrIdentificacao');
	showModalDialog('coleta/cadastrarPedidoColeta.do?cmd=selecionarTelefone&idEnderecoPessoa='+id+'&nrIdentificacaoCliente='+nrIdentificacaoCliente+'&rowClick=false' ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:522px;');
}

function carregaDadosCliente() {
	var idCliente = getElementValue("idCliente");
	
	if(idCliente==undefined || idCliente=="") {
	   idCliente = getElementValue("cliente.idCliente");
	   setElementValue("idCliente", idCliente);
	}
	
	var map = new Array();
	setNestedBeanPropertyValue(map, "cliente.idCliente", idCliente);
	setNestedBeanPropertyValue(map, "_currentPage", "1");
	setNestedBeanPropertyValue(map, "_pageSize", "10");
	
    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.findPaginatedClientesColeta", "resultadoCarregaDadosCliente", map);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Carrega o cliente que foi selecionado na tela anterior
 */
 function resultadoCarregaDadosCliente_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	
	if(data.list.length==1) {
    	setElementValue('cliente.pessoa.nrIdentificacao', data.list[0].nrIdentificacaoFormatado);
    	setElementValue('cliente.pessoa.nmPessoa', data.list[0].nmPessoaCliente);
        // para aplicar a mascara no campo
    	setElementValue('dhPedidoColeta', setFormat(document.getElementById("dhPedidoColeta"), data.list[0].dhPedidoColeta));
    	
    	setElementValue('filial.sgFilial', data.list[0].sgFilial);
    	setElementValue('nrColeta', setFormat(document.getElementById("nrColeta"), data.list[0].nrColeta));

    	carregaGridEnderecosColesta();
    	
    }	
}

/**
 * Mostra ou esconde o botão Fechar caso seja uma lookup ou nao.
 */
function mostraEscondeBotaoFechar(){
	var isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}	
}

function carregaGridEnderecosColesta() {
    var idCliente = getElementValue("idCliente");
    var filtros = new Array();
    setNestedBeanPropertyValue(filtros,"idCliente",idCliente);
    enderecosColetaGridDef.executeSearch(filtros);
}

/**
 * Função para preencher a grid de coletas automaticas
 */
function fillGridColetasAutomaticas(idEnderecoPessoa) {
    var idCliente = getElementValue("idCliente");
    var filtros = new Array();
    
    setNestedBeanPropertyValue(filtros,"cliente.idCliente",idCliente);
    setNestedBeanPropertyValue(filtros,"enderecoPessoa.idEnderecoPessoa",idEnderecoPessoa);
    setNestedBeanPropertyValue(filtros, "_pageSize", "10");
	setNestedBeanPropertyValue(filtros, "_currentPage", "1");
    coletasAutomaticasGridDef.executeSearch(filtros);
	return false;
}

function disableRowClick() {
    return false;
}
</script>