<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	/**
	 * Verifica se algum parametro foi enviado para a tela.
	 * Caso a tenha sido enviado significa que a tela esta sendo usada como tela de consulta e sua grid
	 * estara com o click habilitado.
	 */
	function loadPage() {
		if (isLookup()) {
			onPageLoad();
			setDisabled('btnFechar',false);
	    } else {
		    setVisibility('btnFechar', false);
	    	coletasGridDef.onRowClickFunction="disableGridClick";
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.coleta.consultarColetasAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
    		xmit({serviceDataObjects:[sdo]});
	    }
	}
	
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function loadFilialUsuario_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad();
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		onPageLoad_cb(data,error);
		if(error){
			alert(error);
			return false;
		}
		if (isLookup()) {
	        lookupChange({e:document.getElementById("filialByIdFilialResponsavel.idFilial"),forceChange:true});
	       	if (getElementValue("filialByIdFilialResponsavel.sgFilial")!="") document.getElementById("filialByIdFilialResponsavel.pessoa.nmFantasia").masterLink = "true";
			var lookupOpenerElements = window.dialogArguments.window.document.forms[0].elements;
			var lookupElement = lookupOpenerElements["pedidoColeta.idPedidoColeta"];
			lookupFillCriteriaOnPopUp({e:lookupElement, opener:window.dialogArguments.window});
			setMasterLink(this.document,true);
		}else{
			setDisabled("filialByIdFilialResponsavel.idFilial", false);
			document.getElementById("filialByIdFilialResponsavel.sgFilial").disabled=false;
			document.getElementById("filialByIdFilialResponsavel.sgFilial").focus;
		}
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
</script>

<adsm:window service="lms.coleta.consultarColetasAction" onPageLoad="loadPage" onPageLoadCallBack="loadPage">

	<adsm:i18nLabels>
    	<adsm:include key="LMS-02085"/>
    </adsm:i18nLabels>
    
	<adsm:form action="/coleta/consultarColetas" height="133">
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>

		<adsm:lookup dataType="text" property="filialByIdFilialResponsavel" idProperty="idFilial" criteriaProperty="sgFilial" 
	   				 service="lms.coleta.consultarColetasAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="filial" size="3" maxLength="3" labelWidth="17%" width="83%" disabled="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="integer" label="numeroColeta" labelWidth="17%" width="83%" property="nrColeta" size="9" maxLength="8" mask="00000000"/>

		<adsm:range label="periodo" labelWidth="17%" width="83%" maxInterval="30">
			<adsm:textbox dataType="JTDateTimeZone" property="dhPedidoColetaInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhPedidoColetaFinal"/>
		</adsm:range>

		<adsm:label key="espacoBranco" width="17%" style="border:none;"/>
		<adsm:multicheckbox property="tpSCAberto|tpSCTransmitida|tpSCManifestada|tpSCExecutada|tpSCCancelada|" 
							texts="emAberto|transmitida|manifestada|executada|cancelada" 
							width="83%"cellStyle="vertical-align:bottom;" />				

		<adsm:multicheckbox property="tpSCAguardandoDescarga|tpSCEmDescarga|tpSCNoTerminal|tpSCFinalizada|tpSCNoManifesto|"  
							texts="aguardandoDescarga|emDescarga|coletaNoTerminal|finalizada|noManifesto" 
							label="status" labelWidth="17%" width="83%" cellStyle="vertical-align:bottom;"/>
		
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 service="lms.coleta.consultarColetasAction.findLookupRotaColetaEntrega" action="/municipios/manterRotaColetaEntrega"
					 onchange="return onChangeLookupRota()" onPopupSetValue="lookupRotaSetValue"
					 label="rota" labelWidth="17%" width="83%" size="3" maxLength="3">
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
        	<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" size="30" serializable="true"/>
        </adsm:lookup>	
		
		<adsm:combobox property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil" optionLabelProperty="dsRegiaoColetaEntregaFil" optionProperty="idRegiaoColetaEntregaFil" 
					   service="lms.coleta.consultarColetasAction.findRegiaoColetaEntregaFil" 
					   onchange="return onChangeRegiaoColeta()"
					   label="regiao" labelWidth="17%" width="83%" />
					   
		<adsm:combobox property="detalheColetas.servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico" 
					   service="lms.coleta.consultarColetasAction.findServico" 
					   label="servico" labelWidth="17%" width="83%"/>

		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.coleta.consultarColetasAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 onchange="return onChangeLookupCliente()" onPopupSetValue="lookupClienteSetValue"
					 label="cliente" size="20" maxLength="20" serializable="true" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:lookup
			property="usuario"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.coleta.consultarColetasAction.findLookupUsuarioFuncionario" 
			dataType="text" label="funcionarioSolicitante" size="10" maxLength="16" labelWidth="17%" width="83%"
			action="/configuracoes/consultarFuncionariosView">
			<adsm:hidden property="funcionario.codPessoa.codigo" />			
 			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.codigo" modelProperty="idUsuario"/>
 
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="50" disabled="true"/>
		</adsm:lookup> 
		
		<adsm:lookup dataType="text" property="detalheColetas.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.consultarColetasAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="destino" size="3" maxLength="3" labelWidth="17%" width="83%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="detalheColetas.filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="detalheColetas.filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" renderOptions="true"
					   label="tipoColeta" labelWidth="17%" width="35%" onDataLoadCallBack="loadTpPedidoColetaDefaultValue"/>
		
		<adsm:checkbox property="blSemVinculoDoctoServico" label="semVinculoDoctoServico" labelWidth="26%" width="15%" />		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="findButton" disabled="false" caption="consultar" onclick="return consultar(this.form)"/>
			<adsm:button caption="limpar" buttonType="resetButton" onclick="limpaTela()" disabled="false" id="btnLimpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid property="coletas" idProperty="idPedidoColeta" scrollBars="horizontal" rows="7" gridHeight="150"  
			   service="lms.coleta.consultarColetasAction.findPaginatedConsultarColetas" 
			   rowCountService="lms.coleta.consultarColetasAction.getRowCountConsultarColetas" selectionMode="none">
		<adsm:editColumn field="hidden" property="idFilial" title=""/>
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn property="sgFilial" dataType="text" title="coleta" width="30"/>
			<adsm:gridColumn property="nrColeta" dataType="integer" title="" width="60" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="nmPessoa" dataType="text" title="cliente" width="150" />
		<adsm:gridColumn property="tpPedidoColeta" isDomain="true" dataType="text" title="tipoColeta" width="150" />
		<adsm:gridColumn property="dhPedidoColeta" dataType="JTDateTimeZone" align="center" title="solicitacao" width="120"/>
		<adsm:gridColumn property="psTotalVerificado" title="peso" unit="kg" dataType="decimal" mask="###,###,##0.000" width="100" align="right" />
		<adsm:gridColumn property="qtTotalVolumesVerificado" title="volumes" width="100" align="right" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valor" width="30"/>
			<adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalVerificado" dataType="currency" title="" width="80"/>
		<adsm:gridColumn property="tpStatusColeta" isDomain="true" title="status" width="130" />
		<adsm:gridColumn property="dsCancelamento" dataType="text" title="obsCancelamento" width="150" />
		<adsm:gridColumn property="tpModoPedidoColeta" isDomain="true" title="modoColeta" width="100" />
		<adsm:gridColumn property="nmUsuario" title="usuario" width="200" />
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="dadosCliente" property="dadosCliente" image="/images/popup.gif" width="100" link="/coleta/consultarColetasDadosCliente.do?cmd=main" linkIdProperty="idCliente" align="center"/>
		<adsm:gridColumn title="eventosColeta" property="eventosColeta" image="/images/popup.gif" openPopup="true" link="coleta/consultarEventosColeta.do?cmd=pesq&popUp=true" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
	</adsm:grid>
	<adsm:buttonBar> 
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
	</adsm:buttonBar>
</adsm:window>

<script>

	function initWindow(eventObj) {
		setDisabled("findButton", false);
		setDisabled("btnLimpar", false);

		if (eventObj.name == "cleanButton_click") {
		    if(document.getElementById("filialByIdFilialResponsavel.idFilial").masterLink == false){
				setElementValue("filialByIdFilialResponsavel.sgFilial", "");
				setElementValue("filialByIdFilialResponsavel.idFilial", "");
				setElementValue("filialByIdFilialResponsavel.pessoa.nmFantasia", "");
				
				fillDataUsuario();
			
			}
			
			filialByIdFilialResponsavel_sgFilialOnChangeHandler();	
			
			setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
    		setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), false);
    		setDisabled(document.getElementById("cliente.idCliente"), false);
			
			setFocus(document.getElementById("filialByIdFilialResponsavel.sgFilial"));
		}
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filialByIdFilialResponsavel.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filialByIdFilialResponsavel.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filialByIdFilialResponsavel.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		}
	}
	
	/**
	 * Callback para o carregamento de dados da combo tpPedidoColeta
	 */
	function loadTpPedidoColetaDefaultValue_cb(data, error) {
		tpPedidoColeta_cb(data);
		document.getElementById("tpPedidoColeta").value='NO';
	}
	
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
	/**
	 * função para o tratamento da seguinte situação:
	 * caso informado o cliente, deve ser desabilitado a combo de região e a lookup rota
	 */
	function onChangeLookupCliente(){
	    var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();
	    
	    var nrRota = getElementValue("cliente.idCliente");
	    if(nrRota!="") {
	        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
	        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), true);
	    } else {
	        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
	        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), false);
	    }
	    return retorno;
	}
	
	function lookupClienteSetValue(data) {
	    if(data!=undefined) {
	        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), true);
	        setDisabled(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), true);
	    }
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
	    } else {
	        setDisabled(document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"), false);
	        setDisabled(document.getElementById("cliente.idCliente"), false);
	    }
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
	
	function limpaTela(){
		cleanButtonScript(this.document);
		fillDataUsuario();
	}
	
	function consultar(form){
		if (getElementValue("nrColeta")=="" 
			&& (getElementValue("dhPedidoColetaInicial")=="" || getElementValue("dhPedidoColetaFinal")=="")){
			alert(i18NLabel.getLabel("LMS-02085"));
			return false;
		}
		coletasGridDef.executeSearch(buildFormBeanFromForm(form), true);
	}
	
</script>