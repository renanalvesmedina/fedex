<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function carregaPagina_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		onPageLoad_cb(data, error);
		setMasterLink(this.document, true);
		setFocus(document.getElementById("cliente.pessoa.nrIdentificacao"));

		var objTextAreaEndereco = document.getElementById("endereco");
		objTextAreaEndereco.readOnly="true";

		document.getElementById("nrDddCliente").required = "true";
		document.getElementById("telefoneEndereco.idTelefoneEndereco").required = "true";

	    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDadosSessao", "buscarDadosSessao");
		var sdo2 = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.newMaster", "stub");
		xmit({serviceDataObjects:[sdo, sdo2]});	
	}

	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		if(error){
			alert(error);
			// Desabilita Pedido de Coleta
			setDisabledDocument(this.document, true);
			setDisabled("gerarCotacoes", false);
			setDisabled("storeButton", false);
			setDisabled("newButton", false);
			setElementValue("origem", "cotacao");
			return false;
		}	
		dadosSessao = data;			
		carregaDados();
		setaDadosSessao();
	}

	/**
	* Método vazio para corrigir problema na arquitetura quando se dispara dois ou mais
	* 'sdos' em um mesmo xmit quando um deles nao tem callback. Para solucionar o problema 
	* foi criado esse método vazio. NAO REMOVER.
	*/
	function stub_cb(){
	}

	/**
	 * Armazena as filiais da combo filial responsavel
	 */
	var filiaisResponsavel = [];
</script>
<adsm:window service="lms.coleta.cadastrarPedidoColetaAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/coleta/cadastrarPedidoColeta" idProperty="idPedidoColeta" height="390">
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>

		<adsm:i18nLabels>
			<adsm:include key="LMS-02093" />
			<adsm:include key="LMS-02104"/>
		</adsm:i18nLabels>

		<!-- Dados para Pop-up de Liberar Bloqueio de Crédito para Coleta -->
		<adsm:hidden property="eventoColeta.usuario.idUsuario"/>
		<adsm:hidden property="eventoColeta.ocorrenciaColeta.idOcorrenciaColeta"/>
		<adsm:hidden property="eventoColeta.dsDescricao"/>
		<adsm:hidden property="buttonBloqueioCredito" value="false"/>
		<adsm:hidden property="AlertaProdutoDiferenciado" value="true"/>

		<adsm:hidden property="origem"/>
		<adsm:hidden property="usuario.idUsuario" />
		<adsm:hidden property="filialByIdFilialSolicitante.idFilial" />			
		<adsm:hidden property="tpStatusColeta" value="AB"/>
		<adsm:hidden property="blClienteLiberadoManual" value="N"/>
		<adsm:hidden property="blAlteradoPosProgramacao" value="N"/>
		<adsm:hidden property="siglaNumeroColeta" serializable="false" />		
		<adsm:hidden property="dataAtual" serializable="false"/>
		<adsm:hidden property="idServicoDefault" serializable="false"/>
		<adsm:hidden property="qtdeTotalDocumentos"/>
		<adsm:hidden property="vlPesoTotal"/>
		<adsm:hidden property="valorTotalDocumentos"/>
		
		<adsm:hidden property="idServicoAereo" serializable="false"/>

		<adsm:textbox property="sgFilialColeta" label="coleta" dataType="text" 
					  labelWidth="21%" width="29%" size="4" maxLength="3" disabled="true">				
			<adsm:textbox property="nrColeta" dataType="integer" mask="00000000"
						  disabled="true" size="12"/>
		</adsm:textbox>

		<adsm:combobox label="modo" property="tpModoPedidoColeta" labelWidth="15%" width="25%" renderOptions="true"
					   service="lms.coleta.cadastrarPedidoColetaAction.findModoPedidoColeta" 
					   optionProperty="value" optionLabelProperty="description" defaultValue="TE" 
					   required="true"
					   onchange="tpModoColeta_onChange()"
					   />

		<adsm:hidden property="cliente.tpSituacao" value="A" />
		<adsm:hidden property="idClientePessoa" serializable="false" />
		<adsm:hidden property="tipoIdentificacao" serializable="false" />
		<adsm:hidden property="numeroIdentificacao" serializable="false" />
		<adsm:hidden property="tipoCliente" serializable="false" />		
 		<adsm:lookup label="cliente" dataType="text" property="cliente" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.coleta.cadastrarPedidoColetaAction.findLookupCliente" 
					 action="/coleta/cadastrarPedidoColeta" 
					 cmd="consultarClientes"				 
					 size="19" maxLength="18" labelWidth="21%" width="79%" 
					 required="true" serializable="true" 
					 onPopupSetValue="carregaDadosCliente" onDataLoadCallBack="carregaDadosCliente"
					 onchange="return limpaCamposCliente();">

			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />			 
			<adsm:propertyMapping relatedProperty="tipoIdentificacao" modelProperty="pessoa.tpIdentificacao.value" />
			<adsm:propertyMapping relatedProperty="numeroIdentificacao" modelProperty="pessoa.nrIdentificacao" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="idClientePessoa" modelProperty="idCliente" />
			<adsm:propertyMapping relatedProperty="tipoCliente" modelProperty="tpCliente.value" />

			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" 
						  size="53" maxLength="53" disabled="true" 
						  serializable="false" />
		</adsm:lookup>

		<adsm:textbox label="contato" property="nmContatoCliente" dataType="text" 
					  required="true" labelWidth="21%" width="29%" size="19" maxLength="50"
					  onchange="return setaContatoClienteEmSolicitante()" />
		<adsm:hidden property="nmSolicitante" />

 		<adsm:textbox label="telefone" property="nrDddCliente" dataType="integer" 
 					  size="5" maxLength="5" width="20%" onchange="return limpaCamposDddTelefone();">
			<adsm:lookup property="telefoneEndereco"
						 idProperty="idTelefoneEndereco" 
						 dataType="text" size="10" maxLength="10"
						 action="/coleta/cadastrarPedidoColeta" minLength="4"
						 cmd="selecionarTelefone"
						 service="lms.coleta.cadastrarPedidoColetaAction.findLookupTelefoneEndereco" 
						 criteriaProperty="nrTelefone" criteriaSerializable="true"
						 serializable="true"
						 onPopupSetValue="carregaDadosTelefone"
						 onDataLoadCallBack="carregaDadosTelefone"
						 onchange="return limpaCamposTelefone();">	

				<adsm:propertyMapping criteriaProperty="idClientePessoa" modelProperty="pessoa.idPessoa" />
				<adsm:propertyMapping criteriaProperty="idClientePessoa" modelProperty="cliente.idCliente" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nrIdentificacao" modelProperty="cliente.pessoa.nrIdentificacao" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa" modelProperty="cliente.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="endereco" modelProperty="endereco" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="idEnderecoPessoa" modelProperty="enderecoPessoa.idEnderecoPessoa" />
				<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="enderecoPessoa.municipio.idMunicipio" />
				<adsm:propertyMapping criteriaProperty="municipio.nmMunicipio" modelProperty="enderecoPessoa.municipio.nmMunicipio" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="uf" modelProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="nrDddCliente" modelProperty="nrDdd" disable="false" />
				<adsm:propertyMapping relatedProperty="nrDddCliente" modelProperty="nrDdd" />
				<adsm:propertyMapping criteriaProperty="telefoneEndereco.nrTelefone" modelProperty="nrTelefone" />
				<adsm:propertyMapping relatedProperty="telefoneEndereco.nrTelefone" modelProperty="nrTelefone" />

			</adsm:lookup>
		</adsm:textbox>

  		<adsm:combobox label="tipo" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA"
  					   labelWidth="21%" width="29%" required="true" defaultValue="NO" renderOptions="true" onchange="cboTipoOnChange();"/>

		<adsm:textbox label="novoTelefone" property="nrDddTelefoneNovo" size="5" maxLength="5" width="20%" dataType="integer" disabled="true" serializable="true" onchange="novoTelefoneHandler();" >
			<adsm:textbox property="nrTelefoneNovo" serializable="true" dataType="integer" size="10" maxLength="10" disabled="true" onchange="novoTelefoneHandler();"/>
		</adsm:textbox>  					   

		<adsm:textbox label="disponibilidadeColeta" property="dhColetaDisponivel" dataType="JTDateTimeZone" onchange="return verificaDataDisponibilidade_OnChange()"
					  required="true" labelWidth="21%" width="29%"  />
		
		<adsm:textbox label="horarioLimite" property="hrLimiteColeta" dataType="JTTime" 
					  required="true"/>

		<adsm:textbox label="horarioCorte" property="horarioCorte" dataType="JTTime" 
					  labelWidth="21%" width="29%" disabled="true" serializable="false"/>

		<adsm:textbox label="dataPrevColeta" property="dtPrevisaoColeta" dataType="JTDate" 
					  disabled="false" required="true" />

		<adsm:hidden property="tipoEndereco" value="COL" serializable="false" />
		<adsm:hidden property="idEnderecoPessoa" />
		<adsm:hidden property="edColeta" />
		<adsm:hidden property="nrEndereco" />
		<adsm:hidden property="dsComplementoEndereco" />
		<adsm:hidden property="dsTipoLogradouro" />
		<adsm:hidden property="dsBairro" />
		<adsm:hidden property="nrCep" />
		<adsm:textarea  label="enderecosColeta" property="endereco" maxLength="300" 
						columns="90" rows="3" labelWidth="21%" width="79%" 
						required="true" serializable="false" >			

			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
						 idProperty="idEnderecoPessoa" 
						 property="enderecoPessoa" 
						 action="/coleta/cadastrarPedidoColeta"
						 cmd="selecionarEndereco"
						 service="lms.coleta.cadastrarPedidoColetaAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" onPopupSetValue="enderecoPopSetValue" afterPopupSetValue="buscaTelefoneEndereco" >

				<adsm:propertyMapping criteriaProperty="idClientePessoa" modelProperty="pessoa.idPessoa" />	
				<adsm:propertyMapping criteriaProperty="tipoIdentificacao" modelProperty="pessoa.tpIdentificacao" />				
				<adsm:propertyMapping criteriaProperty="numeroIdentificacao" modelProperty="pessoa.nrIdentificacao" />	
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />	
				<adsm:propertyMapping criteriaProperty="tipoEndereco" modelProperty="tipoEnderecoPessoas.tpEndereco" />

				<adsm:propertyMapping relatedProperty="idEnderecoPessoa" modelProperty="idEnderecoPessoa" />
				<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
				<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="uf" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="edColeta" modelProperty="dsEndereco" />
				<adsm:propertyMapping relatedProperty="nrEndereco" modelProperty="nrEndereco" />
				<adsm:propertyMapping relatedProperty="dsTipoLogradouro" modelProperty="dsTipoLogradouro"/>
				<adsm:propertyMapping relatedProperty="dsComplementoEndereco" modelProperty="dsComplemento" />
				<adsm:propertyMapping relatedProperty="dsBairro" modelProperty="dsBairro" />
				<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep" />

			</adsm:lookup>

		</adsm:textarea>

		<adsm:hidden property="municipio.idMunicipio" />
		<adsm:textbox label="municipio" property="municipio.nmMunicipio" dataType="text" 
					 size="34" labelWidth="21%" width="29%" disabled="true" serializable="false"/>

		<adsm:textbox label="uf" property="uf" dataType="text" 
					  size="3" disabled="true" serializable="false"/>

		<adsm:hidden property="filialByIdFilialResponsavel.idFilial" />
  		<adsm:combobox label="filialParaColeta" property="filialSigla"
  					   labelWidth="21%" width="79%" required="true" renderOptions="true" 
   					   onchange="cboFilialSiglaChange();"
  					   service="lms.coleta.cadastrarPedidoColetaAction.findSiglaFilial" 
					   optionProperty="idFilial" optionLabelProperty="sgFilial"
					   disabled="true" boxWidth="85">
			<adsm:textbox dataType="text" property="filialNome" size="50" disabled="true" serializable="false"/>
  		</adsm:combobox>

		<adsm:hidden property="rotaIntervaloCep.idRotaIntervaloCep" />
		<adsm:hidden property="rotaColetaEntrega.idRotaColetaEntrega" />
		<adsm:textbox label="rotaColetaEntrega" property="numeroRotaColetaEntrega" dataType="text" 
					  size="10" labelWidth="21%" width="79%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="descricaoRotaColetaEntrega" size="20" 
						  disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="moeda.siglaSimbolo"/>
		<adsm:hidden property="vlTotalVerificado" />
		<adsm:combobox label="valorTotal" labelWidth="21%" width="12%"
					   property="moeda.idMoeda" 
					   service="lms.coleta.cadastrarPedidoColetaAction.findMoeda" 
					   optionProperty="idMoeda" renderOptions="true"
					   optionLabelProperty="siglaSimbolo"
					   disabled="true" boxWidth="85">
			<adsm:textbox property="vlTotalInformado" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" width="17%" 
					 	  disabled="true" />
		</adsm:combobox>

		<adsm:textbox label="volumes" property="qtTotalVolumesInformado" dataType="integer" 
					  disabled="true" size="6" />
		<adsm:hidden property="qtTotalVolumesVerificado" />

		<adsm:textbox label="peso" property="psTotalInformado" dataType="weight" 
					  disabled="true" labelWidth="21%" width="29%" unit="kg" size="16" maxLength="18" />
		<adsm:textbox label="pesoAforado" property="psTotalAforadoInformado" dataType="weight" 
					  disabled="true" labelWidth="15%" width="35%" 
					  unit="kg" size="16" maxLength="18" />
		<adsm:hidden property="psTotalVerificado" />
		<adsm:hidden property="psTotalAforadoVerificado" />

		<adsm:hidden property="cotacao.idCotacao" />
		<adsm:lookup property="cotacao.filialByIdFilialOrigem" idProperty="idFilial"
					 criteriaProperty="sgFilial" popupLabel="pesquisarFilial"
				 	 service="lms.coleta.cadastrarPedidoColetaAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" onchange="return sgFilialOnChangeHandler()"
					 label="cotacao" dataType="text" size="5" maxLength="3" 
					 labelWidth="21%" width="29%" picker="false">
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>					 
			<adsm:textbox property="cotacao.nrCotacao" dataType="integer" mask="00000000" disabled="true"
					  	  size="10" maxLength="8" onchange="validaCotacao()"/>
		</adsm:lookup>
		
		<adsm:textbox label="infoColeta" property="dsInfColeta" dataType="text" 
					  labelWidth="15%" width="35%" 
					  size="20" maxLength="20" />

		<adsm:textarea label="observacoes" property="obPedidoColeta" maxLength="100" 
					   columns="90" rows="4" labelWidth="21%" width="79%"/>

		<adsm:listbox label="servicosAdicionais" property="servicoAdicionalColetas" 
					  optionProperty="idServicoAdicionalColeta"
					  labelWidth="21%" width="79%"
					  size="8" boxWidth="200">  
			<adsm:combobox property="servicoAdicional" renderOptions="true"
						   service="lms.coleta.cadastrarPedidoColetaAction.findServicoAdicional" 
						   optionProperty="idServicoAdicional" 
						   optionLabelProperty="dsServicoAdicional" 
					   	   serializable="false" onlyActiveValues="true"/>			
		</adsm:listbox>
			
		<adsm:combobox label="produtoDiferenciado" property="blProdutoDiferenciado" domain="DM_SIM_NAO"
  					   labelWidth="21%" width="29%" required="true" defaultValue="NO" renderOptions="true" onchange="habilitaProdutosDiferenciados()"/>  						
				
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>							
		<adsm:hidden property="tpFiltraProdutoPerigosoRisco" value="true" serializable="false"/>
		
		<adsm:listbox property="produtoDiferenciado" labelWidth="21%" 
					  optionProperty="idProdutoDiferenciado"
					  size="5" boxWidth="150" label="produto">			
				<adsm:lookup
						property="nomeProduto"
						idProperty="idNomeProduto"
						criteriaProperty="dsNomeProduto"
						service="lms.expedicao.produtoService.findLookup"
						action="/expedicao/manterProdutos"
						dataType="text"
						exactMatch="false"
						minLengthForAutoPopUpSearch="1"
						maxLength="80"
						disabled="true"
						onDataLoadCallBack="lookup_produto">					  	
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>	
				<adsm:propertyMapping criteriaProperty="tpFiltraProdutoPerigosoRisco" modelProperty="tpFiltraProdutoPerigosoRisco"/>
			</adsm:lookup>			
		</adsm:listbox>
		
		<adsm:combobox onlyActiveValues="true" 
			label="situacaoAprovacao" 
			property="situacaoAprovacao" 
			domain="DM_STATUS_WORKFLOW"
			labelWidth="21%" 
			width="79%" 
			disabled="true"
			serializable="true"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="gerarCotacoes" id="gerarCotacoes" action="/vendas/gerarCotacoes" cmd="main"/>
			<adsm:reportViewerButton id="emitir" caption="emitirPedidoColeta" service="lms.coleta.relatorioPedidoColetaService"/>
			<adsm:storeButton id="storeButton" callbackProperty="salvarRegistro" caption="salvar" />
			<adsm:button id="newButton" caption="novo" onclick="novoRegistro()"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">

	// Se a tela for chamada pelo módulo de gerar cotações
	var u = new URL(parent.location.href);
   	var origem = u.parameters["origem"];
   	var objOrigem = document.getElementById("origem");
   	setElementValue(objOrigem, origem);
   	objOrigem.masterLink = "true";

	function initWindow(eventObj) {
		setDisabled("gerarCotacoes", false);				
		setDisabled("newButton", false);
		if(getElementValue("idPedidoColeta") == "") {
			setDisabled("storeButton", false);
			setDisabled("emitir", true);
		} else {
			setDisabled("storeButton", true);
			setDisabled("emitir", false);
		}	

	}

	function habilitaProdutosDiferenciados() {
		if(getElementValue("blProdutoDiferenciado") == "S") {
			setDisabled("produtoDiferenciado_nomeProduto.idNomeProduto", false);
			getElement("produtoDiferenciado").required = true;
		} else {
			resetValue("produtoDiferenciado");
			setDisabled("produtoDiferenciado_nomeProduto.idNomeProduto", true);
			getElement("produtoDiferenciado").required = false;			
		}
	}

	function lookup_produto_cb(dados, erros) {
		return lookupExactMatch({e:document.getElementById("nomeProduto.idNomeProduto"), callBack:"lookup_produto_like", data:dados});
	}

	function lookup_produto_like_cb(dados, erros) {
		return lookupLikeEndMatch({e:document.getElementById("nomeProduto.idNomeProduto"), data:dados});
	}
	
	/**
	 * Carrega dados na tela
	 */
	function carregaDados() {
		setDisabled("enderecoPessoa.idEnderecoPessoa", true);		

		if(getElementValue("origem") == "cotacao") {
			populaPedidoColetaByCotacao();
		} else if(getElementValue("origem") == "consultarAWB") {
			populaPedidoColetaByConsultarAWB();		
		}
	}

	/**
	 * Responsável por habilitar/desabilitar uma tab
	 */
	function desabilitaTab(aba, disabled) {
		var tabGroup = getTabGroup(this.document);	
		tabGroup.setDisabledTab(aba, disabled);
	}

	/**
	 * Seta dados da sessão.
	 */
	function setaDadosSessao() {
		setElementValue("usuario.idUsuario", dadosSessao.idUsuarioSessao);
		setElementValue("filialByIdFilialSolicitante.idFilial", dadosSessao.idFilialSessao);
		var comboBox = document.getElementById("moeda.idMoeda");
		for(var i=0; i < comboBox.length; i++) {
			if(comboBox.options[i].text == dadosSessao.siglaSimboloMoedaSessao) {
				setElementValue("moeda.siglaSimbolo", dadosSessao.siglaSimboloMoedaSessao)
				comboBox.options[i].selected = "true";
			}		
		}	
		setElementValue("dhColetaDisponivel", setFormat(document.getElementById("dhColetaDisponivel"), dadosSessao.dataHoraAtual));
		setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dadosSessao.dataAtual));				
		setElementValue("dataAtual", dadosSessao.dataAtual);
		setElementValue("idServicoDefault", dadosSessao.idServicoDefault);
		setElementValue("idServicoAereo", dadosSessao.idServicoAereo);
	}

	/**
	 * Limpa a tela e carrega os dados necessarios novamente.
	 */
	function novoRegistro() {
		
		if(getElementValue("idPedidoColeta") != "" || getElementValue("origem") == "cotacao") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.document.parentWindow.navigate(window.location.pathname + "?cmd=main");
		} else {
			
			// Verifica se foi feita alguma alteração na tela.
			var tab = getTab(this.document);
			if (tab.properties.masterTabId == undefined) {
				if (tab.hasChanged()) {
					var ret = confirm(erDiscardChanges);
					if ((ret == false) || (ret == undefined)) {
						return ret;
					}
					
					var obj = getElement("filialSigla");
					setDisabled(obj, true);
					resetListBoxValue(obj);
					resetValue("filialNome");
				}
			}

			newButtonScript();
			setDisabled("cotacao.nrCotacao", true);
			setDisabled("gerarCotacoes", false);
			setDisabled("storeButton", false);
			setDisabled("newButton", false);

			var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.newMaster", "stub");
			xmit({serviceDataObjects:[sdo]});	

			desabilitaTab("detalheColeta", true);
			carregaDados();
			setaDadosSessao();
			novoTelefone(false);
			setFocus(document.getElementById("cliente.pessoa.nrIdentificacao"));
		}
	}

	/**
	 * Retorno do Salvamento do registro de Pedido Coleta
	 */
	function salvarRegistro_cb(data, erros, errorMsg, eventObj) {
		if(erros){
			alert(erros);
			return false;
		}	
		if(data.AlertaProdutoDiferenciado == "false"){
			if(confirm(i18NLabel.getLabel("LMS-02093"))) {
				setElementValue("AlertaProdutoDiferenciado", false);
				storeButtonScript("lms.coleta.cadastrarPedidoColetaAction.store","salvarRegistro", document.forms[0])
			}else{
				setElementValue("AlertaProdutoDiferenciado", true);
			}
		}else{
			store_cb(data, erros, errorMsg, eventObj);
			setDisabled("emitir", false);
			if(data.numerosPedidoColeta.length > 1) {
				//Cadê a internacionalização?
				var msg = "Foram gerados os seguintes Pedidos de Coleta:\r";
				for(var i=0; i<data.numerosPedidoColeta.length; i++) {
					msg = msg + data.numerosPedidoColeta[i] + "\r";
				}
				alert(msg);
			} 

			setElementValue("idPedidoColeta", data.idPedidoColeta);
			setElementValue("sgFilialColeta", data.filialByIdFilialResponsavel.sgFilial);
			setElementValue("nrColeta", data.nrColeta);
			setElementValue("vlTotalInformado", setFormat(document.getElementById("vlTotalInformado"), data.vlTotalInformado));
			setElementValue("qtTotalVolumesInformado", data.qtTotalVolumesInformado);
			setElementValue("psTotalInformado", setFormat(document.getElementById("psTotalInformado"), data.psTotalInformado));
			setElementValue("psTotalAforadoInformado", setFormat(document.getElementById("psTotalAforadoInformado"), data.psTotalAforadoInformado));
			setElementValue("vlTotalVerificado", setFormat(document.getElementById("vlTotalVerificado"), data.vlTotalVerificado));
			setElementValue("qtTotalVolumesVerificado", data.qtTotalVolumesVerificado);
			setElementValue("psTotalVerificado", setFormat(document.getElementById("psTotalVerificado"), data.psTotalVerificado));
			setElementValue("psTotalAforadoVerificado", setFormat(document.getElementById("psTotalAforadoVerificado"), data.psTotalAforadoVerificado));
			// Desabilita Pedido de Coleta
			setDisabledDocument(this.document, true);
	
	
			var siglaNumeroColeta = getElementValue("sgFilialColeta") + " " + setFormat("nrColeta", getElementValue("nrColeta"));
			//Seta dados em campo hidden que será utilizado para preencher barra verde.
			setElementValue("siglaNumeroColeta", siglaNumeroColeta);
	
			// Desabilita Detalhe de Coleta			
			var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("detalheColeta");			
			setDisabledDocument(tabDet.getDocument(), true);			
			
			//Seta dados da coleta na barra verde
			setElementValue(tabDet.getElementById("_siglaNumeroColeta"), siglaNumeroColeta);
	
			setDisabled("gerarCotacoes", false);
			setDisabled("emitir", false);
			setDisabled("newButton", false);
			
			setFocus(document.getElementById("newButton"), true, true);
		}
	}

	/**
	 * Verifica se horario corte e maior que a hora atual (hora de disponibilidade para coleta)
	 */	
	function verificaDataDisponibilidade_OnChange() {
		var objDhColetaDisponivel = document.getElementById("dhColetaDisponivel");
		result = isDateTime(objDhColetaDisponivel.value, objDhColetaDisponivel.mask);
		if (result == true){
			var horarioCorte = getElementValue("horarioCorte");
			var dhColetaDisponivel = getElementValue("dhColetaDisponivel");
			if(dhColetaDisponivel != "" && horarioCorte != "") {
				var mapCriteria = new Array();
			    setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", dhColetaDisponivel);
		    	setNestedBeanPropertyValue(mapCriteria, "horarioCorte", horarioCorte);
	
				var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDataPrevisaoColeta", "verificaDataPrevisaoColeta", mapCriteria);
				xmit({serviceDataObjects:[sdo]});	
			}
		}
		return result;
	}

	/**
	 * Retorno da verificação de horario de corte em getDataPrevisaoColeta()
	 */
	function verificaDataPrevisaoColeta_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		var dataPrevisao = (data._value).substring(0, (data._value).indexOf(" "));	
		setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dataPrevisao));
	}	

	function carregaDadosCliente(data) {
		buscarDadosCliente(data);			
		desabilitaTab("detalheColeta", false);

		if(getElementValue("origem") == "cotacao") {
			populaDetalheColetaByCotacao();
		} else if(getElementValue("origem") == "consultarAWB") {
			populaDetalheColetaByConsultarAWB();
		}
		//Gera a validacao do pce
		validatePCE(data.idCliente);
	}

	function carregaDadosCliente_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);

		if(data[0] != undefined) {
			buscarDadosCliente(data[0]);			
			desabilitaTab("detalheColeta", false);
			if(getElementValue("origem") == "cotacao") {
				populaDetalheColetaByCotacao();
			} else if(getElementValue("origem") == "consultarAWB") {
				populaDetalheColetaByConsultarAWB();
			}
			//Gera a validacao do pce
			validatePCE(data[0].idCliente);
		}
	}

	function cboTipoOnChange() {
		var tpModoPedidoColeta = getElementValue("tpModoPedidoColeta");
		var tpPedidoColeta = getElementValue("tpPedidoColeta");
		
		if(tpModoPedidoColeta == 'BA' && tpPedidoColeta == 'AE'){
			var msg = i18NLabel.getLabel('LMS-02104');
			alert('LMS-02104 - ' + msg);
			setFocus("tpPedidoColeta");
			setElementValue("tpPedidoColeta", tpPedidoColetaAnteriorGlobal);
		} else {
			validateGridItens();
			comboboxChange({e:this});
			validatePCE(getElementValue("cliente.idCliente"));
		}
		
		tpPedidoColetaAnteriorGlobal = getElementValue("tpPedidoColeta");
	}
	
	var tpPedidoColetaAnteriorGlobal;
	function validateGridItens(){
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("detalheColeta");
		var abaDetalheColeta = tabDet.tabOwnerFrame.window;
		
		abaDetalheColeta.document.getElementById("newButton").click();
		
		if(abaDetalheColeta.detalheColetaGridDef.gridState.rowCount > 0){
			var tpPedidoColeta = getElementValue("tpPedidoColeta");
			var ret = novoRegistro();
			
			if(ret == false) {
				setElementValue("tpPedidoColeta", tpPedidoColetaAnteriorGlobal);
			} else {
				setElementValue("tpPedidoColeta", tpPedidoColeta);
				abaDetalheColeta.detalheColetaGridDef.resetGrid();
			}
			
		}
		
		tpPedidoColetaAnteriorGlobal = getElementValue("tpPedidoColeta");
	}
	

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################

	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE(idCliente) {	
		var data = new Object();
		data.idCliente = idCliente;
		data.tpPedidoColeta = getElementValue("tpPedidoColeta");
		if (data.idCliente!="") {
			var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.validatePCE", "validatePCE", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception!=undefined) {
			alert(error);
			return false;	
		} 

		if (data.codigo!=undefined) {
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		}
	}

	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Empty...
	}	

	//#####################################################
	// Fim da validacao do pce
	//#####################################################

	/**
	 * Limpa os campos do form caso não exista cliente
	 */
	function limpaCamposCliente() {				
		if(getElementValue("cliente.pessoa.nrIdentificacao") == "" && 
		   getElementValue("cliente.idCliente") != "") {
			novoRegistro();
		} else { // Limpa o id do endereço antigo, caso seja alterado o nrIdentificacao, para no método buscarEnderecoPessoa(), buscar o novo endereço.
			setElementValue("enderecoPessoa.idEnderecoPessoa", "");			
		}
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();;
	}

	/**
	 * Retorno da verificação de Contato no pedido coleta em getContatoPedidoColeta()
	 */
	function resultado_verificaContatoPedidoColeta_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		setElementValue("nmContatoCliente", data._value);
		setElementValue("nmSolicitante", data._value);
	}	

	/**
	 * Função que pega dados Contato e Telefone do Cliente.
	 * Chama funçoes para verificar se o cliente possui contato em um pedido de coleta já 
	 * cadastrado para o mesmo e se possui bloqueio, caso possua, chamar tela de Liberar 
	 * Bloqueio de crédito para Coleta.
	 */
	function buscarDadosCliente(data) {
		setDisabled("enderecoPessoa.idEnderecoPessoa", false);

		var mapCriteria = new Array();    
	    setNestedBeanPropertyValue(mapCriteria, "idCliente", data.idCliente);
	    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDadosCliente", "resultado_buscarDadosCliente", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Retorno da verificação de bloqueio de cliente em getDadosCliente()
	 */
	function resultado_buscarDadosCliente_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}

		if (data.nomeContato){
			setElementValue("nmContatoCliente", data.nomeContato);
			setElementValue("nmSolicitante", data.nomeContato);
		}
		if (data.nrDdd)	setElementValue("nrDddCliente", data.nrDdd);
		if (data.idTelefoneEndereco) setElementValue("telefoneEndereco.idTelefoneEndereco", data.idTelefoneEndereco);
		if (data.nrTelefone) setElementValue("telefoneEndereco.nrTelefone", data.nrTelefone);

		var mapCriteria = new Array();    
	    setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("idClientePessoa"));
	    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getBloqueioCliente", "resultado_verificaBloqueioCliente", mapCriteria);
		xmit({serviceDataObjects:[sdo]});		
	}

	/**
	 * Retorno da verificação de bloqueio de cliente em getBloqueioCliente()
	 */
	function resultado_verificaBloqueioCliente_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		if(data._value == "true") {
			window.showModalDialog('coleta/liberarBloqueioCreditoColeta.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:760px;dialogHeight:490px;');		
			if(getElementValue("buttonBloqueioCredito") == "false") {
				if(getElementValue("origem") == "") {
					novoRegistro();
				} else {
					var tabGroup = getTabGroup(this.document);
					tabGroup.document.parentWindow.navigate(window.location.pathname + "?cmd=main");
				}
			} else {
				buscarEnderecoPessoa();
				setElementValue("buttonBloqueioCredito", "false");
			}			
		} else {
			buscarEnderecoPessoa();
		}
	}	

	/**
	 * Chama funçoes para preencher endereço, horario de corte...
	 */
	function enderecoPopSetValue(data) {
		buscarDadosComplementares(data);
		formataEndereco(data);
		return true;
	}

	function formataEndereco(data) {
		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.formataEndereco", "formataEndereco", data);
		xmit({serviceDataObjects:[sdo]});	
	}

	function formataEndereco_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		setEndereco(data.endereco);
	}

	function setEndereco(endereco) {
		setElementValue("endereco", endereco);
	}
	
	/**
	 * Função chamada ao selecionar um endereço pela popup.
	 */
	function buscaTelefoneEndereco(data) {
		var mapCriteria = new Array();
    	setNestedBeanPropertyValue(mapCriteria, "idEnderecoPessoa", data.idEnderecoPessoa);
    	var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getTelefoneEnderecoPedidoColeta", "buscaTelefoneEndereco", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
	}

	/**
	 * Callback da função buscaTelefoneEndereco()
	 */
	function buscaTelefoneEndereco_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		
		limpaTelefoneNovo();
		
		if (data.nrDdd)	setElementValue("nrDddCliente", data.nrDdd);
		if (data.idTelefoneEndereco) setElementValue("telefoneEndereco.idTelefoneEndereco", data.idTelefoneEndereco);
		if (data.nrTelefone) setElementValue("telefoneEndereco.nrTelefone", data.nrTelefone);
	}
		
	<%-- --%>


	/**
	 * Função que busca dados complementares para coleta.
	 */
	function buscarDadosComplementares(data) {
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "idEnderecoPessoa", data.idEnderecoPessoa);
		setNestedBeanPropertyValue(mapCriteria, "nrCep", data.nrCep);
		setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("idClientePessoa"));
	    setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", getElementValue("dhColetaDisponivel"));
	    setNestedBeanPropertyValue(mapCriteria, "tpModoPedidoColeta", getElementValue("tpModoPedidoColeta"));

		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDadosComplementares", "buscarDadosComplementares", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Retorno da pesquisa de Dados Complementares.
	 */
	function buscarDadosComplementares_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		setElementValue("hrLimiteColeta", setFormat(document.getElementById("hrLimiteColeta"), data.hrCorteCliente));
		setElementValue("horarioCorte", setFormat(document.getElementById("horarioCorte"), data.horaCorteSolicitacao));
		setElementValue("rotaIntervaloCep.idRotaIntervaloCep", data.idRotaIntervaloCep);
		setElementValue("rotaColetaEntrega.idRotaColetaEntrega", data.idRotaColetaEntrega);
		setElementValue("numeroRotaColetaEntrega", data.numeroRota);
		setElementValue("descricaoRotaColetaEntrega", data.descricaoRota);

		filiaisResponsavel = data.filiais;
		if (filiaisResponsavel != null && filiaisResponsavel.length > 0) {
			var obj = getElement("filialSigla");
			resetListBoxValue(obj);

			for (var i = 0; i < filiaisResponsavel.length; i++) {
				setComboBoxElementValue(obj, filiaisResponsavel[i].idFilial, filiaisResponsavel[i].idFilial, filiaisResponsavel[i].sgFilial);
			}

			if (filiaisResponsavel.length == 1) {
				setDisabled(obj, true);
			} else {
				setDisabled(obj, false);
			}
			cboFilialSiglaChange();
		}

		if (data.dhColetaDisponivel) {
			var dataPrevisao = (data.dhColetaDisponivel).substring(0, (data.dhColetaDisponivel).indexOf(" "));
			setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dataPrevisao));
		}
	}

	function cboFilialSiglaChange() {
		var obj = getElement("filialSigla");
		var filial = filiaisResponsavel[obj.selectedIndex];
		setElementValue("filialByIdFilialResponsavel.idFilial", filial.idFilial);
		setElementValue("filialNome", filial.nmFantasia);
		
		// LMS-5316
		if (getElementValue("tpModoPedidoColeta") != "BA") {
			findRotaColetaEntrega();
		}
	}

	// LMS-5316
	function findRotaColetaEntrega() {
		var mapCriteria = new Array();
		var obj = getElement("filialSigla");
		var filial = filiaisResponsavel[obj.selectedIndex];
		setNestedBeanPropertyValue(mapCriteria, "idEnderecoPessoa", getElementValue("idEnderecoPessoa"));
		setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("idClientePessoa"));
		setNestedBeanPropertyValue(mapCriteria, "nrCep", getElementValue("nrCep"));
		setNestedBeanPropertyValue(mapCriteria, "idFilial", filial.idFilial);
		setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", getElementValue("dhColetaDisponivel"));
		
		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.findRotaColetaEntrega", "findRotaColetaEntrega", mapCriteria);
        xmit({serviceDataObjects:[sdo]});
	}
	
	// LMS-5316
	function findRotaColetaEntrega_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		
		setElementValue("rotaIntervaloCep.idRotaIntervaloCep", data.idRotaIntervaloCep);
		setElementValue("rotaColetaEntrega.idRotaColetaEntrega", data.idRotaColetaEntrega);
		setElementValue("numeroRotaColetaEntrega", data.numeroRota);
		setElementValue("descricaoRotaColetaEntrega", data.descricaoRota);
		setElementValue("hrLimiteColeta", setFormat(document.getElementById("hrLimiteColeta"), data.hrCorteCliente));
		setElementValue("horarioCorte", setFormat(document.getElementById("horarioCorte"), data.horaCorteSolicitacao));
		
		if (data.dhColetaDisponivel) {
			var dataPrevisao = (data.dhColetaDisponivel).substring(0, (data.dhColetaDisponivel).indexOf(" "));
			setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dataPrevisao));
		}
	}

	/**
	 * Busca EnderecoPessoa de acordo com o ID co Cliente
	 */
	function buscarEnderecoPessoa() {
		if (getElementValue("enderecoPessoa.idEnderecoPessoa") == ""
			|| getElementValue("enderecoPessoa.idEnderecoPessoa") == undefined) {
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("idClientePessoa"));
	    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getEnderecoPessoa", "resultado_buscarEnderecoPessoa", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
	}
	}

	/**
	 * Retorno da pesquisa de EnderecoPessoa em getEnderecoPessoa().
	 */
	function resultado_buscarEnderecoPessoa_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		if(data.idEnderecoPessoa != undefined) {				
			setElementValue("idEnderecoPessoa", data.idEnderecoPessoa);
			setElementValue("enderecoPessoa.idEnderecoPessoa", data.idEnderecoPessoa);
			setElementValue("edColeta", data.dsEndereco);
			setElementValue("dsTipoLogradouro", data.dsTipoLogradouro);
			setElementValue("nrEndereco", data.nrEndereco);
			setElementValue("dsComplementoEndereco", data.dsComplemento);
			setElementValue("dsBairro", data.dsBairro);
			setElementValue("nrCep", data.nrCep);
			setElementValue("municipio.idMunicipio", data.municipio.idMunicipio);
			setElementValue("municipio.nmMunicipio", data.municipio.nmMunicipio);
			setElementValue("uf", data.municipio.unidadeFederativa.sgUnidadeFederativa);

			setElementValue("nrDddCliente", data.nrDdd);
			setElementValue("telefoneEndereco.nrTelefone", data.nrTelefone);
			setElementValue("telefoneEndereco.idTelefoneEndereco", data.idTelefoneEndereco);

			enderecoPopSetValue(data);
		} else {
			lookupClickPicker({e:document.forms[0].elements['enderecoPessoa.idEnderecoPessoa']});
		}
	}

	/**
	 * Seta o nome do Contato do Cliente para o campo hidden Nome do Solicitante.
	 */
	function setaContatoClienteEmSolicitante() {
		setElementValue("nmSolicitante", getElementValue("nmContatoCliente"));

		return true;
	}

	/**
	 * Seta o Número Identificação vindo do Cadastro de Cliente.
	 */
	function setaNumeroIdentificacao(nrIdentificacao) {
		var identificacao = document.getElementById("cliente.pessoa.nrIdentificacao");
		identificacao.value = nrIdentificacao;		
		cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}

	/**
	 * Seta os dados do Endereço na tela pai após salvar o registro.
	 */
	function setaDadosEndereco(mapEndereco) {
		setElementValue("nrDddCliente", mapEndereco.nrDdd);
		setElementValue("telefoneEndereco.nrTelefone", mapEndereco.nrTelefone);
		setElementValue("idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
		setElementValue("enderecoPessoa.idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
		setElementValue("edColeta", mapEndereco.dsEndereco);
		setElementValue("dsTipoLogradouro", mapEndereco.dsTipoLogradouro);
		setElementValue("nrEndereco", mapEndereco.nrEndereco);
		setElementValue("dsComplementoEndereco", mapEndereco.dsComplemento);
		setElementValue("dsBairro", mapEndereco.dsBairro);
		setElementValue("nrCep", mapEndereco.nrCep);
		setElementValue("municipio.idMunicipio", mapEndereco.municipio.idMunicipio);
		setElementValue("municipio.nmMunicipio", mapEndereco.municipio.nmMunicipio);
		setElementValue("uf", mapEndereco.municipio.unidadeFederativa.sgUnidadeFederativa);

		limpaTelefoneNovo();
		
		enderecoPopSetValue(mapEndereco);
	}


	function limpaTelefoneNovo() {
 		resetValue("nrDddTelefoneNovo");
		resetValue("nrTelefoneNovo");
		
		novoTelefone(false);
	}
	 
	/**
	 * Seta os dados da Pop-up de Liberar Bloqueio de Crédito para Coleta.
	 */
	function setaDadosPopupLiberarBloqueioCreditoColeta(mapBloqueioCredito) {
		setElementValue("eventoColeta.usuario.idUsuario", mapBloqueioCredito.idUsuario);	
		setElementValue("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta", mapBloqueioCredito.idOcorrenciaColeta);
		setElementValue("eventoColeta.dsDescricao", mapBloqueioCredito.dsDescricao);	
		setElementValue("buttonBloqueioCredito", "true");
	}	

	/**
	 * Função que popula a aba de Pedido de Coleta com os dados vindos da tela de Cotação.
	 */
	 function populaPedidoColetaByCotacao() {
	 	var idCliente = u.parameters["cliente.idCliente"];	 
	 	if(idCliente != undefined && idCliente != "") {	 		
	 		document.getElementById("cliente.pessoa.nrIdentificacao").value = u.parameters["cliente.pessoa.nrIdentificacao"];
	   		lookupChange({e:document.getElementById("cliente.idCliente"), forceChange:true});
	 	} else { 	 		
	 		document.getElementById("cliente.idCliente").disabled = false;
	 		setDisabled("cliente.idCliente", false);
	 	}
   		setElementValue("nmContatoCliente", u.parameters["nmContatoCliente"]);
   		setElementValue("nmSolicitante", u.parameters["nmSolicitante"]);
   		setElementValue("cotacao.idCotacao", u.parameters["cotacao.idCotacao"]);

   		// Pesquisa os dados da cotação.
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "idCotacao", getElementValue("cotacao.idCotacao"));
		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDadosCotacao", "dadosCotacao", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	 }

	 /**
	  * Retorno da pesquisa de dados da cotação.
	  */
	 function dadosCotacao_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	 	
	 	setElementValue("cotacao.filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
	 	setElementValue("cotacao.filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
	 	setElementValue("cotacao.nrCotacao", data.nrCotacao);
	 	setElementValue("cotacao.nrCotacao", setFormat("cotacao.nrCotacao", getElementValue("cotacao.nrCotacao")));
	 	setDisabled("cotacao.filialByIdFilialOrigem.idFilial", true);
	 	setDisabled("cotacao.nrCotacao", true);	 	
	 }

	/**
	 * Função que popula a aba de Detalhes de Coleta com os dados vindos da tela de Cotação
	 */	 
	 function populaDetalheColetaByCotacao() {
		// Pega a aba 'detalheColeta' para setar os valores nos properties
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("detalheColeta");
		var abaDetalheColeta = tabDet.tabOwnerFrame.window;

		setFormPropertyValue(tabDet.tabOwnerFrame, "origem", getElementValue("origem"));		
		setFormPropertyValue(tabDet.tabOwnerFrame, "cotacao.idCotacao", getElementValue("cotacao.idCotacao"));

		var comboBoxServico = abaDetalheColeta.document.getElementById("servico.idServico");		
		setElementValue(comboBoxServico, u.parameters["servico.idServico"]);
		abaDetalheColeta.setDisabled("servico.idServico", true);

		if(u.parameters["naturezaProduto.idNaturezaProduto"] && u.parameters["naturezaProduto.idNaturezaProduto"] != "") {
			var comboBoxNaturezaProduto = abaDetalheColeta.document.getElementById("naturezaProduto.idNaturezaProduto");		
			setElementValue(comboBoxNaturezaProduto, u.parameters["naturezaProduto.idNaturezaProduto"]);
			abaDetalheColeta.setDisabled("naturezaProduto.idNaturezaProduto", true);
		}

		if (u.parameters["municipioDestino.idMunicipio"] && u.parameters["municipioDestino.idMunicipio"] != ""){
			abaDetalheColeta.document.getElementById("municipioFilial.municipio.nmMunicipio").value = u.parameters["municipioDestino.nmMunicipio"];
			abaDetalheColeta.document.getElementById("municipio.idMunicipio").value = u.parameters["municipioDestino.idMunicipio"];
			abaDetalheColeta.setDisabled("municipioFilial.idMunicipioFilial", true);
			abaDetalheColeta.municipioFilial_municipio_nmMunicipioOnChangeHandler();
		}

		abaDetalheColeta.setDisabled("destino", true);
		abaDetalheColeta.setDisabled("localidadeEspecial.idLocalidadeEspecial", true);

		if (u.parameters["clienteDestino.idCliente"] && u.parameters["clienteDestino.idCliente"] != ""){
			abaDetalheColeta.document.getElementById("cliente.idCliente").value = u.parameters["clienteDestino.idCliente"];
			abaDetalheColeta.document.getElementById("cliente.pessoa.nrIdentificacao").value = u.parameters["clienteDestino.pessoa.nrIdentificacao"];
			abaDetalheColeta.cliente_pessoa_nrIdentificacaoOnChangeHandler();
		} else if (u.parameters["clienteDestino.pessoa.nmPessoa"] && u.parameters["clienteDestino.pessoa.nmPessoa"] != ""){
			abaDetalheColeta.document.getElementById("nmDestinatario").value = u.parameters["clienteDestino.pessoa.nmPessoa"];
		}
		abaDetalheColeta.setDisabled("cliente.idCliente", true);
		abaDetalheColeta.setDisabled("nmDestinatario", true);

		var docDet = tabDet.tabOwnerFrame.window.document;
		setFormPropertyValue(tabDet.tabOwnerFrame, "idMoedaCotacao", u.parameters["moeda.idMoeda"]);
		setFormPropertyValue(tabDet.tabOwnerFrame, "vlMercadoria", u.parameters["vlTotalInformado"]);
		setFormPropertyValue(tabDet.tabOwnerFrame, "psMercadoria", u.parameters["psTotalInformado"]);
		setFormPropertyValue(tabDet.tabOwnerFrame, "psAforado", u.parameters["pesoAforado"]);
		if(u.parameters["pesoAforado"] && u.parameters["pesoAforado"] != "") {
			abaDetalheColeta.document.getElementById("aforado").checked = true;
			abaDetalheColeta.document.getElementById("psAforado").disabled = "true";
			abaDetalheColeta.document.getElementById("aforado").disabled = "true";			
			setElementValue("psTotalAforadoInformado", setFormat(document.getElementById("psTotalAforadoInformado"), u.parameters["pesoAforado"]));
		}

		abaDetalheColeta.setDisabled("moeda.idMoeda", true);
		abaDetalheColeta.setDisabled("vlMercadoria", true);
		abaDetalheColeta.setDisabled("psMercadoria", true);

		var comboBoxTpFrete = abaDetalheColeta.document.getElementById("tpFrete");		
		setElementValue(comboBoxTpFrete, u.parameters["tpFrete"]);
		abaDetalheColeta.setDisabled("tpFrete", true);
	 }

	function carregaDadosTelefone(data) {
		if(data.pessoa.nrIdentificacao != undefined) {	
			setaNumeroIdentificacao(data.pessoa.nrIdentificacao);
		}
	}

	function carregaDadosTelefone_cb(data, error) {
		if (error){
			if (data._exception){
				if (data._exception._key=="LMS-02081"){
					var confirmacaoNovoTelefone = confirm(data._exception._message);
					if (confirmacaoNovoTelefone) {
						copiaTelefoneParaNovoTelefone();
				} else {
						setFocus("telefoneEndereco.nrTelefone");
					}			
				} else {
					alert(error);
					
				}
			}
			return false;
		}
		telefoneEndereco_nrTelefone_likeEndMatch_cb(data);
		if(data.length == 1) {
			if(data[0].pessoa.nrIdentificacao != undefined) {	
				setaNumeroIdentificacao(data[0].pessoa.nrIdentificacao);
			}
		}
	}

	/**
	 * Limpa os campos de telefone
	 */
	function limpaCamposTelefone() {				
		var x = telefoneEndereco_nrTelefoneOnChangeHandler();
		if(getElementValue("telefoneEndereco.nrTelefone") == "" && 
		   getElementValue("telefoneEndereco.idTelefoneEndereco") != "") {
			resetValue("telefoneEndereco.idTelefoneEndereco");
		}
		return x;
	}	

	/**
	 * Limpa os campos de telefone
	 */
	function limpaCamposDddTelefone() {
		var x = telefoneEndereco_nrTelefoneOnChangeHandler();	
		if(getElementValue("telefoneEndereco.idTelefoneEndereco") != "") {
			resetValue("telefoneEndereco.idTelefoneEndereco");
			resetValue("telefoneEndereco.nrTelefone");
		}
		return x;
	}

	/**
	 * Controla o objeto coleta
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("cotacao.filialByIdFilialOrigem.sgFilial")=="") {
			resetValue("cotacao.filialByIdFilialOrigem.idFilial");
			resetValue("cotacao.idCotacao");
			setDisabled("cotacao.nrCotacao", true);
		} else {
			setDisabled("cotacao.nrCotacao", false);
		}
		return lookupChange({e:document.forms[0].elements["cotacao.filialByIdFilialOrigem.idFilial"]});
	}

	/**
	 * Função que valida a cotação.
	 */
	function validaCotacao() {
		if(getElementValue("cotacao.nrCotacao") != "") {
			var mapCriteria = new Array();	   
			setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("cliente.idCliente"));
			setNestedBeanPropertyValue(mapCriteria, "idMunicipio", getElementValue("municipio.idMunicipio"));
		   	setNestedBeanPropertyValue(mapCriteria, "idFilialOrigem", getElementValue("cotacao.filialByIdFilialOrigem.idFilial"));
		   	setNestedBeanPropertyValue(mapCriteria, "nrCotacao", getElementValue("cotacao.nrCotacao"));

		    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.validaCotacao", "validaCotacao", mapCriteria);
			xmit({serviceDataObjects:[sdo]});
		} else {
			resetValue("cotacao.idCotacao");
		}
	}

	/**
	 * Retorno da validação de cotação em validaCotacao().
	 */
	function validaCotacao_cb(data, error) {
		if (error){
			alert(error);
			resetValue("cotacao.idCotacao");
			resetValue("cotacao.nrCotacao");
			setFocus("cotacao.nrCotacao");
			return false;
		}
		// Caso um cliente não tenha sido informado, preenche a coleta com o cliente da cotação.
		if(getElementValue("cliente.idCliente") == "") {
			if(data.clienteByIdClienteSolicitou){
				setaNumeroIdentificacao(data.clienteByIdClienteSolicitou.pessoa.nrIdentificacao);
			}
		}		

		setElementValue("cotacao.idCotacao", data.idCotacao);

		// Pega a aba 'detalheColeta' para setar os valores nos properties
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("detalheColeta");
		var abaDetalheColeta = tabDet.tabOwnerFrame.window;

		setFormPropertyValue(tabDet.tabOwnerFrame, "origem", "cotacao");
		setFormPropertyValue(tabDet.tabOwnerFrame, "cotacao.idCotacao", data.idCotacao);

		var comboBoxServico = abaDetalheColeta.document.getElementById("servico.idServico");		
		setElementValue(comboBoxServico, data.servico.idServico);
		abaDetalheColeta.habilitaCampos();
		abaDetalheColeta.setDisabled("servico.idServico", true);

		if(data.naturezaProduto) {
			var comboBoxNaturezaProduto = abaDetalheColeta.document.getElementById("naturezaProduto.idNaturezaProduto");		
			setElementValue(comboBoxNaturezaProduto, data.naturezaProduto.idNaturezaProduto);
			abaDetalheColeta.setDisabled("naturezaProduto.idNaturezaProduto", true);				
		}

		if (data.municipioByIdMunicipioDestino){
			abaDetalheColeta.document.getElementById("municipioFilial.municipio.nmMunicipio").value = data.municipioByIdMunicipioDestino.nmMunicipio;
			abaDetalheColeta.document.getElementById("municipio.idMunicipio").value = data.municipioByIdMunicipioDestino.idMunicipio;
			abaDetalheColeta.setDisabled("municipioFilial.idMunicipioFilial", true);
			abaDetalheColeta.municipioFilial_municipio_nmMunicipioOnChangeHandler();
		}

		if (data.clienteByIdClienteDestino){
			abaDetalheColeta.document.getElementById("cliente.pessoa.nrIdentificacao").value = data.clienteByIdClienteDestino.pessoa.nrIdentificacao;
			abaDetalheColeta.cliente_pessoa_nrIdentificacaoOnChangeHandler();
		} else if (data.nmClienteDestino && data.nmClienteDestino!=""){
			abaDetalheColeta.document.getElementById("nmDestinatario").value = data.nmClienteDestino;
		}
		abaDetalheColeta.setDisabled("cliente.idCliente", true);
		abaDetalheColeta.setDisabled("nmDestinatario", true);

		abaDetalheColeta.setDisabled("destino", true);
		abaDetalheColeta.setDisabled("municipio.idMunicipio", true);
		abaDetalheColeta.setDisabled("localidadeEspecial.idLocalidadeEspecial", true);

		setFormPropertyValue(tabDet.tabOwnerFrame, "idMoedaCotacao", data.moeda.idMoeda);

		var docDet = tabDet.tabOwnerFrame.window.document;
		setFormPropertyValue(tabDet.tabOwnerFrame, "vlMercadoria", setFormat(docDet.getElementById("vlMercadoria"), data.vlMercadoria));
		setFormPropertyValue(tabDet.tabOwnerFrame, "psMercadoria", setFormat(docDet.getElementById("psMercadoria"), data.psReal));

		if(data.psAforado && data.psAforado!="") {
			abaDetalheColeta.document.getElementById("aforado").checked = true;
			abaDetalheColeta.document.getElementById("aforado").disabled = "true";			
			abaDetalheColeta.document.getElementById("psAforado").disabled = "true";
			setElementValue("psTotalAforadoInformado", setFormat(document.getElementById("psTotalAforadoInformado"), data.psAforado));
			setFormPropertyValue(tabDet.tabOwnerFrame, "psAforado", setFormat(docDet.getElementById("psAforado"), data.psAforado));
		}

		if(data.vlMercadoria && data.vlMercadoria!="") {
			setElementValue("vlTotalInformado", setFormat(document.getElementById("vlTotalInformado"), data.vlMercadoria));
		}

		if(data.psReal && data.psReal!="") {
			setElementValue("psTotalInformado", setFormat(document.getElementById("psTotalInformado"), data.psReal));
		}

		if(data.dsContato && data.dsContato!=""){
			setElementValue("nmContatoCliente", data.dsContato);
			setElementValue("nmSolicitante", data.dsContato);
		}

		abaDetalheColeta.setDisabled("moeda.idMoeda", true);
		abaDetalheColeta.setDisabled("vlMercadoria", true);
		abaDetalheColeta.setDisabled("psMercadoria", true);

		var comboBoxTpFrete = abaDetalheColeta.document.getElementById("tpFrete");
		setElementValue(comboBoxTpFrete, data.tpFrete.value);
		abaDetalheColeta.setDisabled("tpFrete", true);			

		doTab(document.getElementById("cotacao.nrCotacao"), 1);
	}
	 
	function populaPedidoColetaByConsultarAWB() {
		lookupChange({e:document.getElementById("cliente.idCliente"), forceChange:true});
	}
	
	 function populaDetalheColetaByConsultarAWB() {
		 	/** Valores Formatados */
			setElementValue("vlTotalInformado", setFormat(document.getElementById("vlTotalInformado"), u.parameters["vlTotalInformado"]));
			setElementValue("psTotalInformado", setFormat(document.getElementById("psTotalInformado"), u.parameters["psTotalInformado"]));

			//*** Busca aba 'detalheColeta' para setar os valores nos properties
			var tabDet = getTabGroup(this.document).getTab("detalheColeta");

			setFormPropertyValue(tabDet.tabOwnerFrame, "filial.idFilial", u.parameters["filial.idFilial"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "filial.sgFilial", u.parameters["filial.sgFilial"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "filial.pessoa.nmFantasia", u.parameters["filial.pessoa.nmFantasia"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "moeda.idMoeda", u.parameters["moeda.idMoeda"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "idMoedaCotacao", u.parameters["moeda.idMoeda"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "moeda.siglaSimbolo", u.parameters["siglaSimbolo"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "municipioFilial.municipio.nmMunicipio", u.parameters["municipioPessoa.nmMunicipio"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "municipio.idMunicipio", u.parameters["municipioPessoa.idMunicipio"]);
			tabDet.tabOwnerFrame.window.municipioFilial_municipio_nmMunicipioOnChangeHandler();

			/** Valores Formatados */
			var vlMercadoria = tabDet.tabOwnerFrame.document.getElementById("vlMercadoria");
			var psMercadoria = tabDet.tabOwnerFrame.document.getElementById("psMercadoria");
			setElementValue(vlMercadoria, getFormattedValue(vlMercadoria.dataType, u.parameters["vlTotalInformado"], vlMercadoria.mask, true));
			setElementValue(psMercadoria, getFormattedValue(psMercadoria.dataType, u.parameters["psTotalInformado"], psMercadoria.mask, true));
			setElementValue(tabDet.tabOwnerFrame.document.getElementById("qtVolumes"), u.parameters["qtTotalVolumesInformado"]);
			
			setFormPropertyValue(tabDet.tabOwnerFrame, "origem", u.parameters["origem"]);
			setFormPropertyValue(tabDet.tabOwnerFrame, "servico.idServico", u.parameters["servico.idServico"]);
			
			if(u.parameters["naturezaProduto.idNaturezaProduto"] && u.parameters["naturezaProduto.idNaturezaProduto"] != "") {
				var comboBoxNaturezaProduto = tabDet.tabOwnerFrame.document.getElementById("naturezaProduto.idNaturezaProduto");
				setElementValue(comboBoxNaturezaProduto, u.parameters["naturezaProduto.idNaturezaProduto"]);
				setDisabled(comboBoxNaturezaProduto, false);
			}
				
			setFormPropertyValue(tabDet.tabOwnerFrame, "ciaFilialMercurio.empresa.idEmpresa", u.parameters["awb.ciaFilialMercurio.empresa.idEmpresa"]);

			/** ListBox */
			var optionData = new Object();
			setNestedBeanPropertyValue(optionData, "_uniqueId", 0);
			setNestedBeanPropertyValue(optionData, "idAwb", u.parameters["awb.idAwb"]);
			setNestedBeanPropertyValue(optionData, "nrAwb", u.parameters["awb.nrAwb"]);
			var awbColetas = tabDet.tabOwnerFrame.document.getElementById("awbColetas");
			awbColetas.options.add(new Option(u.parameters["awb.nrAwb"], u.parameters["awb.idAwb"]));
			awbColetas.options[0].data = optionData;
		 }

	 // Função que limpa os campos de telefone caso seja informado um "Novo telefone"
	 function novoTelefone(value){
	 	if (value==true){
	 		resetValue("nrDddCliente");
			resetValue("telefoneEndereco.nrTelefone");
			resetValue("telefoneEndereco.idTelefoneEndereco");
			setDisabled("nrDddCliente", true);
			setDisabled("telefoneEndereco.idTelefoneEndereco", true);
			setDisabled("nrDddTelefoneNovo", false);
			setDisabled("nrTelefoneNovo", false);				
			document.getElementById("nrDddCliente").required = "false";
			document.getElementById("telefoneEndereco.idTelefoneEndereco").required = "false";
			document.getElementById("nrDddTelefoneNovo").required = "true";
			document.getElementById("nrTelefoneNovo").required = "true";
	 	} else {
			setDisabled("nrDddCliente", false);
			setDisabled("telefoneEndereco.idTelefoneEndereco", false);
			setDisabled("nrDddTelefoneNovo", true);
			setDisabled("nrTelefoneNovo", true);			
			document.getElementById("nrDddCliente").required = "true";
			document.getElementById("telefoneEndereco.idTelefoneEndereco").required = "true";
			document.getElementById("nrDddTelefoneNovo").required = "false";
			document.getElementById("nrTelefoneNovo").required = "false";
	 	}
	 }

	 function copiaTelefoneParaNovoTelefone(){
		setElementValue("nrDddTelefoneNovo", getElementValue("nrDddCliente"));
		setElementValue("nrTelefoneNovo", getElementValue("telefoneEndereco.nrTelefone"));
		novoTelefone(true);
	 }

	 function novoTelefoneHandler(){
	 	var nrDddNovoTelefone = getElementValue("nrDddTelefoneNovo");
	 	var nrNovoTelefone = getElementValue("nrTelefoneNovo");
		if (nrDddNovoTelefone == "" && nrNovoTelefone == ""){
			novoTelefone(false);
		}
	 }
	 
	function exibeRestricoes() {
		showModalDialog('/coleta/cadastrarPedidoColeta.do?cmd=restricoesColeta',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	
	var tpModoPedidoGlobal = getElementValue("tpModoPedidoColeta");
	function tpModoColeta_onChange(){		
		var tpModoPedidoColeta = getElementValue("tpModoPedidoColeta");
		var tpPedidoColeta = getElementValue("tpPedidoColeta");
		
		if(tpModoPedidoColeta == 'BA' && tpPedidoColeta == 'AE'){
			alert('LMS-02104 - ' + getI18nMessage('LMS-02104'));
			setFocus("tpModoPedidoColeta");
			setElementValue("tpModoPedidoColeta", tpModoPedidoGlobal);
		} else {
		   if (getElementValue("idEnderecoPessoa")!= null && getElementValue("idEnderecoPessoa").length >0){
		        var mapCriteria = new Array(); 
		        setNestedBeanPropertyValue(mapCriteria, "idEnderecoPessoa", getElementValue("idEnderecoPessoa"));
	            setNestedBeanPropertyValue(mapCriteria, "nrCep", getElementValue("nrCep"));
	            setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("idClientePessoa"));
	            setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", getElementValue("dhColetaDisponivel"));
	            setNestedBeanPropertyValue(mapCriteria, "tpModoPedidoColeta", getElementValue("tpModoPedidoColeta"));
		        var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaAction.getDadosComplementares", "buscarDadosComplementares", mapCriteria);
	            xmit({serviceDataObjects:[sdo]});
		   }			
		}
		tpModoPedidoGlobal = getElementValue("tpModoPedidoColeta");
	}
</script>