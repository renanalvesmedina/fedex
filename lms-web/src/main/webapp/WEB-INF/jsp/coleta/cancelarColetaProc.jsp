<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function loadDataOnPageLoad() {
	onPageLoad();
	carregaDadosUsuarioLogado();
}

function carregaDadosUsuarioLogado() {
   	var data = new Array();
	var sdo = createServiceDataObject("lms.coleta.cancelarColetaAction.getDadosUsuarioLogado", "carregaDadosUsuarioLogado", data);
   	xmit({serviceDataObjects:[sdo]});

}
	
function carregaDadosUsuarioLogado_cb(data, error) {
	if (error!=undefined) {
		alert(error);
		return false;
	}
	if(getElementValue("origem") != "manterColetas") {
		setElementValue("filialPesquisa.idFilial", data.filial.idFilial);
		setElementValue("filialPesquisa.sgFilial", data.filial.sgFilial);
		setElementValue("filialPesquisa.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
		//setElementValue("empresa.idEmpresa", data.empresa.idEmpresa);
	
		disableNrPedidoColeta(false);		
	}
	setFocus(document.getElementById('pedidoColeta.nrColeta'));
}

</script>

<adsm:window service="lms.coleta.cancelarColetaAction" onPageLoad="loadDataOnPageLoad">
	<adsm:form action="/coleta/cancelarColeta">
		<adsm:hidden property="origem" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpAcesso" value="F"/>
<%-- 	<adsm:hidden property="empresa.idEmpresa" serializable="false"/> --%>
		
		<adsm:hidden property="filialPesquisa.pessoa.nmFantasia" serializable="false"/>
    	<adsm:lookup label="coleta" labelWidth="19%" width="36%" required="true"
    		              dataType="text" 
    		              property="filialPesquisa"  
    		              idProperty="idFilial" 
    		              criteriaProperty="sgFilial" 
					      service="lms.coleta.cancelarColetaAction.findFilialBySgFilial" 
					      action="/municipios/manterFiliais" 
					      onchange="return sgFilialOnChangeHandler()"
					      size="3" maxLength="3" picker="false" serializable="false" disabled="true">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>					      
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping relatedProperty="filialPesquisa.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			

			<adsm:lookup size="8" maxLength="8" dataType="integer"

						 onDataLoadCallBack="carregaDadosPedidoColeta"
						 idProperty="idPedidoColeta"
						 property="pedidoColeta"
						 action="/coleta/consultarColetas" cmd="pesq"
						 service="lms.coleta.cancelarColetaAction.findLookupComEnderecoCompleto"
						 criteriaProperty="nrColeta"
						 mask="00000000"
						 onPopupSetValue="buscaPedidoColeta"
						 onchange="return coletaOnChangeHandler();"
						 >
    
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.idFilial" 	 	   modelProperty="filialByIdFilialResponsavel.idFilial" disable="true" />
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.sgFilial" 	 	   modelProperty="filialByIdFilialResponsavel.sgFilial" disable="true"/>
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.pessoa.nmFantasia" modelProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" disable="true"/>
						 
						 <adsm:propertyMapping relatedProperty="filialPesquisa.sgFilial" blankFill="false" modelProperty="filialByIdFilialResponsavel.sgFilial"/>
						 <adsm:propertyMapping relatedProperty="pedidoColeta.nrColeta" 	 	   modelProperty="nrColeta"/>
						 
						 <adsm:propertyMapping relatedProperty="tpStatusColeta.description"  modelProperty="tpStatusColeta.description"/>
						 <adsm:propertyMapping relatedProperty="tpStatusColeta.value" 		 modelProperty="tpStatusColeta.value"/>
						 <adsm:propertyMapping relatedProperty="filialByIdFilialSolicitante.sgFilial"		 modelProperty="filialByIdFilialSolicitante.sgFilial"/>
						 <adsm:propertyMapping relatedProperty="filialByIdFilialSolicitante.pessoa.nmFantasia" modelProperty="filialByIdFilialSolicitante.pessoa.nmFantasia"/>
			
						 <adsm:propertyMapping relatedProperty="filialByIdFilialResponsavel.sgFilial"		 modelProperty="filialByIdFilialResponsavel.sgFilial"/>
						 <adsm:propertyMapping relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" modelProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
						 <adsm:propertyMapping relatedProperty="dhPedidoColeta" 				modelProperty="dhPedidoColeta"/>
						 <adsm:propertyMapping relatedProperty="usuario.idUsuario" 				modelProperty="usuario.idUsuario"/>
						 <adsm:propertyMapping relatedProperty="usuario.nmUsuario" 				modelProperty="usuario.nmUsuario"/>
						 <adsm:propertyMapping relatedProperty="cliente.pessoa.nrIdentificacaoFormatado" modelProperty="cliente.pessoa.nrIdentificacaoFormatado"/>
						 <adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" 		modelProperty="cliente.pessoa.nmPessoa"/>
						 <adsm:propertyMapping relatedProperty="enderecoCompleto" 				modelProperty="enderecoCompleto"/>
						 <adsm:propertyMapping relatedProperty="municipio.nmMunicipio" 			modelProperty="municipio.nmMunicipio"/>
						 <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
						 <adsm:propertyMapping relatedProperty="nmContatoCliente" 				modelProperty="nmContatoCliente"/>
						 <adsm:propertyMapping relatedProperty="nrTelefoneCliente"				modelProperty="nrTelefoneCliente"/>
						 <adsm:propertyMapping relatedProperty="qtTotalVolumesVerificado" 		modelProperty="qtTotalVolumesVerificado"/>
						 <adsm:propertyMapping relatedProperty="psTotalVerificado"				modelProperty="psTotalVerificado"/>
						 <adsm:propertyMapping relatedProperty="vlTotalVerificado"				modelProperty="vlTotalVerificado"/>
						 <adsm:propertyMapping relatedProperty="psTotalAforadoVerificado"		modelProperty="psTotalAforadoVerificado"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:hidden property="tpStatusColeta.value" serializable="false"/>
		
		<adsm:textbox label="status" width="30%" disabled="true" serializable="false"
					  property="tpStatusColeta.description" 
					  size="16"
					  dataType="text"/>

		<adsm:textbox label="filialAtendimento" labelWidth="19%" width="81%" disabled="true" serializable="false"
					  property="filialByIdFilialResponsavel.sgFilial"
					  dataType="text"
					  size="3" maxLength="3">
			<adsm:textbox property="filialByIdFilialResponsavel.pessoa.nmFantasia" dataType="text" size="35" maxLength="35" disabled="true" serializable="false"/>
		</adsm:textbox>


		<adsm:textbox label="filialSolicitacao" labelWidth="19%" width="36%" disabled="true" serializable="false" 
					  property="filialByIdFilialSolicitante.sgFilial"
					  dataType="text" 
					  size="3" maxLength="3">
			<adsm:textbox dataType="text" property="filialByIdFilialSolicitante.pessoa.nmFantasia" size="35" maxLength="35" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox label="solicitacao" width="30%" disabled="true" serializable="false" 
					  property="dhPedidoColeta"
					  dataType="JTDateTimeZone" 
					  picker="false"/>

		<adsm:textbox label="registradaPor" labelWidth="19%" width="81%" disabled="true" 
					  property="usuario.idUsuario" serializable="false"
					  dataType="text"
					  size="18" maxLength="20">
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="30" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox label="cliente" labelWidth="19%" width="81%" disabled="true" serializable="false"
					  property="cliente.pessoa.nrIdentificacaoFormatado"
					  dataType="text" 
					  size="18" maxLength="20">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" maxLength="30" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textarea label="endereco" labelWidth="19%" width="81%" disabled="true" serializable="false"
					   property="enderecoCompleto" 
					   maxLength="300" 
					   columns="80" rows="3" />

		<adsm:textbox label="municipio" labelWidth="19%" width="36%" disabled="true" serializable="false"
					  property="municipio.nmMunicipio" 
					  dataType="text" 
					  size="40" maxLength="60" />
		<adsm:textbox label="uf" 
					  dataType="text" 
					  property="municipio.unidadeFederativa.sgUnidadeFederativa" 
					  width="30%" disabled="true" serializable="false"
					  size="10"
					  />

		<adsm:textbox label="contato" labelWidth="19%" width="36%" disabled="true" serializable="false"
					  property="nmContatoCliente" size="40" maxLength="50"
					  dataType="text" />
		<adsm:textbox label="telefone" width="6%" disabled="true" serializable="false"
					  property="nrTelefoneCliente" 
					  dataType="phone"
					  size="16"
					  />

		<adsm:textbox label="totalVolumes" labelWidth="19%" width="36%" disabled="true" serializable="false"
					  property="qtTotalVolumesVerificado"
					  dataType="integer"
					  size="18" maxLength="18" />
		<adsm:textbox label="totalPeso" unit="kg"  width="30%" disabled="true" serializable="false"
					  property="psTotalVerificado"
					  dataType="weight" 
					  size="16"
					  />

		<adsm:textbox label="valorTotalMercadoria" labelWidth="19%" width="36%" disabled="true" serializable="false"
					  property="vlTotalVerificado"
					  dataType="decimal" 
					  size="18%" maxLength="18" />

		<adsm:textbox label="totalPesoAforado" serializable="false" width="30%"
					  property="psTotalAforadoVerificado" 
					  dataType="weight"
					  unit="kg"
					  size="16"
					  disabled="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="cancelaColeta" caption="cancelarColetaBotao" disabled="false" onclick="cancelarColeta();"/>
			<adsm:button caption="limpar" onclick="limpaTela()" id="limpar" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>

		<script>
			var msgLMS02001 = "<adsm:label key="LMS-02001" />";
		</script>
			
	</adsm:form>
	<adsm:grid property="detalheColetas" scrollBars="horizontal" selectionMode="none" 
			   gridHeight="40" rows="2" onRowClick="disableRowClick"
			   idProperty="idDetalheColeta" 
			   service="lms.coleta.cancelarColetaAction.findPaginatedDetalheColeta" 
			   rowCountService="lms.coleta.cancelarColetaAction.getRowCountDetalheColeta">
		<adsm:gridColumn title="servico" 			property="servico.dsServico" width="200"/>
		<adsm:gridColumn title="frete" 				property="tpFrete" width="50" isDomain="true"/>
		<adsm:gridColumn title="naturezaMercadoria" property="naturezaProduto.dsNaturezaProduto" width="170"/>
		<adsm:gridColumn title="notaFiscal" 		property="notaFiscalColetas" image="/images/popup.gif" openPopup="true" link="/coleta/cadastrarPedidoColeta.do?cmd=listaNota" popupDimension="380,240" width="80" align="center" linkIdProperty="idDetalheColeta"/>
		<adsm:gridColumn title="preAwbAwb" property="awb" width="140" align="left"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="documentoServico" property="doctoServico.tpDoctoSgFilial" width="60" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="entregaDireta" property="blEntregaDireta" width="110" renderMode="image-check"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor"			property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title=""				property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title=""					property="vlMercadoria" width="100" align="right" dataType="decimal"/>
		<adsm:gridColumn title="pesoKG" 			property="psMercadoria" width="105" align="right" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumn title="pesoAforadoKG" 		property="psAforado" width="105" align="right" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumn title="destinatario" 		property="cliente.pessoa.nmPessoa" width="300"/>
		<adsm:gridColumn title="municipioDestino" 	property="municipio.nmMunicipio" width="155"/>
		<adsm:gridColumn title="localidadeEspecial" property="localidadeEspecial.dsLocalidade" width="200"/>
		<adsm:gridColumn title="filialDestino" 		property="localidadeEspecial.filial.sgFilial" width="80"/>
		
		<adsm:gridColumn title="cotacao" 		property="cotacao" width="80"/>
		<adsm:gridColumn title="crt" 		property="ctr" width="80"/>
	</adsm:grid>
	<adsm:buttonBar freeLayout="false">
	</adsm:buttonBar>
</adsm:window>

<script language="javascript">

	// Se a tela for chamada pelo botão emitir de Abrir MDA.
	var u = new URL(parent.location.href);
   	var objOrigem = document.getElementById("origem");
   	setElementValue(objOrigem, u.parameters["origem"]);
   	objOrigem.masterLink = "true";

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		document.getElementById("pedidoColeta.nrColeta").required = "true";
		setDisabled(document.getElementById("cancelaColeta"), false);
		if(getElementValue("origem") == "manterColetas") {
			setElementValue("filialPesquisa.idFilial", u.parameters["filialPesquisa.idFilial"]);
			setElementValue(document.getElementById("filialPesquisa.sgFilial"), u.parameters["filialPesquisa.sgFilial"]);
			setElementValue("filialPesquisa.pessoa.nmFantasia", u.parameters["filialPesquisa.pessoa.nmFantasia"]);
			setElementValue("pedidoColeta.idPedidoColeta", u.parameters["pedidoColeta.idPedidoColeta"]);
			setElementValue(document.getElementById("pedidoColeta.nrColeta"), u.parameters["pedidoColeta.nrColeta"]);			
			lookupChange({e:document.getElementById("pedidoColeta.idPedidoColeta"), forceChange:true});
		}
		
	}

	function buscaPedidoColeta(data) {
		var criteria = new Array();
	    // Monta um map
	    setNestedBeanPropertyValue(criteria, "idPedidoColeta", data.idPedidoColeta);
	
		// temporario
		//setElementValue(document.getElementById("filialPesquisa.sgFilial"), data.filialByIdFilialResponsavel.sgFilial);
		
	    var sdo = createServiceDataObject("lms.coleta.cancelarColetaAction.findLookupComEnderecoCompleto", "carregaDadosPedidoColeta", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	
	/**
	 * Função que ao informar o numero da coleta, carrega a grid
	 * de detalhe coleta
	 */
	function carregaDadosPedidoColeta_cb(data, error) {
		var retornoLookup = pedidoColeta_nrColeta_exactMatch_cb(data);
		
		if (!retornoLookup)
		   return false;
		
		if(retornoLookup) {
			disableNrPedidoColeta(false);	
			detalheColetasGridDef.executeSearch(buildFormBeanFromForm(this.document.forms[0]));
		}
		return retornoLookup;
	}
	
	/**
	 * Limpa todos os campos da grid
	 */
	function limpaGrid() {
		detalheColetasGridDef.resetGrid();
	}
	
	/**
	  * Função que abre a popup de cancelamento de coleta.
	  */
	function cancelarColeta() {
	  	
	  	var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("proc");
		
	  	if (!tab.validate()){
	  		return false;
	  	}
	  
		var tpStatusColeta = getElementValue("tpStatusColeta.value");
		if (tpStatusColeta == "AB") {
			var returnModalValue = showModalDialog('coleta/cancelarColeta.do?cmd=cancel',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:140px;');
			
			if(returnModalValue != undefined) {
				var str = returnModalValue._value+"";
				var dataArray = str.split(",");
				
				setElementValue("tpStatusColeta.description", dataArray[0]);
				setElementValue("tpStatusColeta.value", dataArray[0]);
				
				showSuccessMessage();
				window.event.cancelBubble = true;
			}
			
		} else {
			alert(msgLMS02001);
			setFocusOnFirstFocusableField();
			return false;
		}
	}

	/**
	 * Função para não chamar nada no onclick da row da grid
	 */
	function disableRowClick() {
		return false;
	}
	
	/**
	 * Controla o objeto coleta
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("filialPesquisa.sgFilial")=="") {
			disableNrPedidoColeta(true);
			resetValue("filialPesquisa.idFilial");
			limpaGrid();
		} else {
			disableNrPedidoColeta(false);
		}
		return lookupChange({e:document.forms[0].elements["filialPesquisa.idFilial"]});
	}
	
	function disableNrPedidoColeta(disable) {
		setDisabled(document.getElementById("pedidoColeta.nrColeta"), disable);
	}
	
	
	function coletaOnChangeHandler() {
		if (getElementValue("pedidoColeta.nrColeta")=="") {
			resetValue("pedidoColeta.idPedidoColeta");
			limpaGrid();
		}
		
		return pedidoColeta_nrColetaOnChangeHandler();
	}
	
	function limpaTela() {
		cleanButtonScript(this.document);
		detalheColetasGridDef.resetGrid();
		carregaDadosUsuarioLogado();
	}

</script>