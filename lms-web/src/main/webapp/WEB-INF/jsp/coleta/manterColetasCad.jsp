<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function carregaPagina_cb(data, error) {		
		onPageLoad_cb(data, error);
		setMasterLink(this.document, true);
		setDisabled("restricoesColeta", false);
		
		var objTextAreaEndereco = document.getElementById("endereco");
		objTextAreaEndereco.readOnly="true";

	    var sdo = createServiceDataObject("lms.coleta.manterColetasAction.getDadosSessao", "buscarDadosSessao");
		var sdo2 = createServiceDataObject("lms.coleta.manterColetasAction.newMaster", "stub");
		xmit({serviceDataObjects:[sdo, sdo2]});	
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		dadosSessao = data;
	}
	
	/**
	* Método vazio para corrigir problema na arquitetura quando se dispara dois ou mais
	* 'sdos' em um mesmo xmit quando um deles nao tem callback. Para solucionar o problema 
	* foi criado esse método vazio. NAO REMOVER.
	*/
	function stub_cb(){
	}
		
</script>
<adsm:window service="lms.coleta.manterColetasAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/coleta/manterColetas" idProperty="idPedidoColeta" height="390" onDataLoadCallBack="carregaDadosPagina">

		<adsm:hidden property="usuario.idUsuario" />
		<adsm:hidden property="filialByIdFilialSolicitante.idFilial" />
		<adsm:hidden property="cliente.tpCliente" />
		<adsm:hidden property="moeda.siglaSimbolo" />
		<adsm:hidden property="blClienteLiberadoManual" />
		<adsm:hidden property="blAlteradoPosProgramacao" />			
		<adsm:hidden property="moeda.idMoeda" />
		<adsm:hidden property="moeda.siglaSimbolo" serializable="false"/>
		<adsm:hidden property="dataAtual" serializable="false"/>
		<adsm:hidden property="siglaNumeroColeta" serializable="false" />
		<adsm:hidden property="origem" value="manterColetas"/>
		<adsm:hidden property="qtdeTotalDocumentos"/>
		<adsm:hidden property="vlPesoTotal"/>
		<adsm:hidden property="valorTotalDocumentos"/>

		<adsm:textbox property="sgFilialColeta" label="coleta" dataType="text" 
					  labelWidth="21%" width="29%" size="4" maxLength="3" disabled="true">				
			<adsm:textbox property="nrColeta" dataType="integer" mask="00000000"
						  disabled="true" size="12"/>
		</adsm:textbox>

		<adsm:combobox label="modo" property="tpModoPedidoColeta" labelWidth="15%" width="25%"
					   service="lms.coleta.cadastrarPedidoColetaAction.findModoPedidoColeta" renderOptions="true"
					   optionProperty="value" optionLabelProperty="description" disabled="true"/>

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="cliente.pessoa.idPessoa"/>
		<adsm:hidden property="cliente.pessoa.tpIdentificacao"/>
		<adsm:hidden property="cliente.pessoa.nrIdentificacao"/>
		<adsm:textbox label="cliente" dataType="text" property="cliente.pessoa.nrIdentificacaoFormatado" 
					  labelWidth="21%" width="79%" size="19" maxLength="18" disabled="true">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox label="contato" property="nmContatoCliente" dataType="text" 
					  labelWidth="21%" width="29%" size="19" maxLength="50"
					  onchange="return setaContatoClienteEmSolicitante()"/>
		<adsm:hidden property="nmSolicitante" />
		
 		<adsm:textbox label="telefone" property="nrDddCliente" dataType="integer" 
 					  size="5" maxLength="5" width="20%">
			<adsm:textbox property="nrTelefoneCliente" dataType="text" size="10" maxLength="10"/>	
		</adsm:textbox>		

  		<adsm:combobox label="tipo" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA"
  					   labelWidth="21%" width="29%" renderOptions="true" disabled="true" />

		<adsm:combobox label="status" property="tpStatusColeta" domain="DM_TIPO_STATUS_COLETA" disabled="true" renderOptions="true"/> 					   
				
		<adsm:textbox label="disponibilidadeColeta" property="dhColetaDisponivel" dataType="JTDateTimeZone" 
					  labelWidth="21%" width="29%" onchange="return verificaDataDisponibilidade_OnChange()"/>

		<adsm:textbox label="horarioLimite" property="hrLimiteColeta" dataType="JTTime" />

		<adsm:textbox label="horarioCorte" property="horarioCorte" dataType="JTTime" 
					  labelWidth="21%" width="29%" disabled="true" serializable="false"/>

		<adsm:textbox label="dataPrevColeta" property="dtPrevisaoColeta" dataType="JTDate"/>

		<adsm:hidden property="tipoEndereco" value="COL" serializable="false" />
		<adsm:hidden property="idEnderecoPessoa" />
		<adsm:hidden property="edColeta" />
		<adsm:hidden property="nrEndereco" />
		<adsm:hidden property="dsComplementoEndereco" />
		<adsm:hidden property="dsBairro" />
		<adsm:hidden property="nrCep" />
		<adsm:textarea  label="enderecosColeta" property="endereco" maxLength="300" 
						columns="90" rows="3" labelWidth="21%" width="79%" 
						disabled="true" serializable="false" >			

			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
						 idProperty="idEnderecoPessoa" 
						 property="enderecoPessoa" 
						 action="/coleta/cadastrarPedidoColeta"
						 cmd="selecionarEndereco"
						 service="lms.coleta.manterColetasAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" onPopupSetValue="chamaFuncoes" >
						 
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.idPessoa" modelProperty="pessoa.idPessoa" />	
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao" />
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />	
				<adsm:propertyMapping criteriaProperty="tipoEndereco" modelProperty="tipoEnderecoPessoas.tpEndereco" />
				
				<adsm:propertyMapping relatedProperty="idEnderecoPessoa" modelProperty="idEnderecoPessoa" />
				<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
				<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="edColeta" modelProperty="dsEndereco" />
				<adsm:propertyMapping relatedProperty="nrEndereco" modelProperty="nrEndereco" />
				<adsm:propertyMapping relatedProperty="dsComplementoEndereco" modelProperty="dsComplemento" />
				<adsm:propertyMapping relatedProperty="dsBairro" modelProperty="dsBairro" />
				<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep" />

			</adsm:lookup>
									
		</adsm:textarea>

		<adsm:hidden property="municipio.idMunicipio" />
		<adsm:textbox label="municipio" dataType="text" property="municipio.nmMunicipio" 
					  labelWidth="21%" width="29%" size="34" maxLength="34" disabled="true"/>

		<adsm:textbox label="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" 
					  size="3" disabled="true" serializable="false"/>

		<adsm:hidden property="filialByIdFilialResponsavel.idFilial" />
		<adsm:textbox label="filialParaColeta" property="filialByIdFilialResponsavel.sgFilial" dataType="text" 
					  size="3" maxLength="3" labelWidth="21%" width="79%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50" 
						  disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="rotaIntervaloCep.idRotaIntervaloCep" />
		<adsm:hidden property="rotaColetaEntrega.idRotaColetaEntrega" />
		<adsm:textbox label="rotaColetaEntrega" property="rotaColetaEntrega.nrRota" dataType="text" 
					  size="10" labelWidth="21%" width="79%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="20" 
						  disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:combobox label="valorTotalInformado" labelWidth="21%" width="12%" boxWidth="85"
					   property="moeda1" 
					   service="lms.coleta.manterColetasAction.findMoeda" 
					   optionProperty="idMoeda" renderOptions="true"
					   optionLabelProperty="siglaSimbolo"
					   disabled="true">
			<adsm:textbox property="vlTotalInformado" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" width="17%" 
					 	  disabled="true" />
		</adsm:combobox>
		
		<adsm:textbox label="volumesInformado" property="qtTotalVolumesInformado" dataType="integer" 
					  disabled="true" size="6" labelWidth="17%" width="8%" />		

		<adsm:textbox label="pesoInformado" property="psTotalInformado" dataType="weight" disabled="true" 
					  labelWidth="21%" width="29%" unit="kg" size="16" />
					  
		<adsm:textbox label="pesoAforadoInformado" property="psTotalAforadoInformado" dataType="weight" 
					  disabled="true" labelWidth="17%" width="33%" 
					  unit="kg" size="16" />

		<adsm:combobox label="valorTotalVerificado" labelWidth="21%" width="12%" boxWidth="85"
					   property="moeda2" renderOptions="true"
					   service="lms.coleta.manterColetasAction.findMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo"
					   disabled="true">
			<adsm:textbox property="vlTotalVerificado" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" width="17%" 
					 	  disabled="true"/>
		</adsm:combobox>					  

		<adsm:textbox label="volumesVerificado" property="qtTotalVolumesVerificado" dataType="integer" 
					  disabled="true" size="6" labelWidth="17%" width="8%" />

		<adsm:textbox label="pesoVerificado" property="psTotalVerificado" dataType="weight"  disabled="true"
					  labelWidth="21%" width="29%" unit="kg" size="16" />
					  
		<adsm:textbox label="pesoAforadoVerificado" property="psTotalAforadoVerificado" dataType="weight" 
					  disabled="true" labelWidth="17%" width="33%" unit="kg" size="16" />

		<adsm:hidden property="cotacao.idCotacao" />
		<adsm:textbox property="cotacao.filialByIdFilialOrigem.sgFilial" 
					  label="cotacao" dataType="text" size="5" labelWidth="21%" width="29%" disabled="true">
			<adsm:textbox property="cotacao.nrCotacao" dataType="integer" size="10" 
						  mask="00000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox label="infoColeta" property="dsInfColeta" dataType="text" 
					  labelWidth="17%" width="33%" 
					  size="20" maxLength="20" />

		<adsm:textarea label="observacoes" property="obPedidoColeta" maxLength="100" 
					   columns="90" rows="4" labelWidth="21%" width="79%"/>
					   
		<adsm:listbox label="servicosAdicionais" property="servicoAdicionalColetas" 
					  optionProperty="idServicoAdicionalColeta" 
					  labelWidth="21%" width="79%"
					  size="8" boxWidth="200">  
			<adsm:combobox property="servicoAdicional" renderOptions="true"
						   service="lms.coleta.manterColetasAction.findServicoAdicional" 
						   optionProperty="idServicoAdicional" 
						   optionLabelProperty="dsServicoAdicional" 
					   	   serializable="false" onlyActiveValues="true"/>
		</adsm:listbox>

 		<adsm:checkbox 
			property="blProdutoDiferenciado" 
			label="produtoDiferenciado"
			labelWidth="21%" 	
			width="79%" 
			serializable="true"			
			onclick="return habilitaProdutosDiferenciados(this);"/>  						
				
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>							
		<adsm:hidden property="tpFiltraProdutoPerigosoRisco" value="true" serializable="false"/>
		<adsm:listbox label="produtoDiferenciado" property="produtoDiferenciado" labelWidth="21%" 
					  optionProperty="idProdutoDiferenciado"
					  size="5" boxWidth="150" >			
				<adsm:lookup
						property="produto"
						idProperty="idProduto"
						criteriaProperty="dsProduto"
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
			<adsm:button id="restricoesColeta" caption="produtosProibidos" onclick="exibeRestricoes();" />								
			<adsm:reportViewerButton id="emitir" caption="emitirPedidoColeta" service="lms.coleta.relatorioPedidoColetaService"/>			
			<adsm:button caption="cancelarColeta" id="cancelarButton" action="/coleta/cancelarColeta" cmd="main">
				<adsm:linkProperty src="filialByIdFilialResponsavel.idFilial" target="filialPesquisa.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialResponsavel.sgFilial" target="filialPesquisa.sgFilial"/>
				<adsm:linkProperty src="filialByIdFilialResponsavel.pessoa.nmFantasia" target="filialPesquisa.pessoa.nmFantasia"/>				
				<adsm:linkProperty src="idPedidoColeta" target="pedidoColeta.idPedidoColeta"/>
				<adsm:linkProperty src="nrColeta" target="pedidoColeta.nrColeta"/>
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>
			<adsm:storeButton id="storeButton" callbackProperty="salvarRegistro" caption="salvar" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Função que carrega os dados para a tela.
	 */
	function carregaDadosPagina_cb(data, error) {
		onDataLoad_cb(data);
		
		setElementValue("moeda.siglaSimbolo", dadosSessao.siglaSimboloMoedaSessao);
		setElementValue("dataAtual", dadosSessao.dataAtual);
		
		var siglaNumeroColeta = getElementValue("sgFilialColeta") + " " + setFormat("nrColeta", getElementValue("nrColeta"));
		//Seta dados em campo hidden que será utilizado para preencher barra verde.
		setElementValue("siglaNumeroColeta", siglaNumeroColeta);

		//Seta dados da coleta na barra verde
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("detalheColeta");
		setElementValue(tabDet.getElementById("_siglaNumeroColeta"), siglaNumeroColeta);
			
		if (document.getElementById("blProdutoDiferenciado").checked == true) {
			setDisabled("produtoDiferenciado_produto.idProduto", false);
		} else {
			setDisabled("produtoDiferenciado_produto.idProduto", true);
		}
	
		desabilitaHabilitaCampos();	
		carregaDados();
	}

	
	function habilitaProdutosDiferenciados(blProdDiferenciados) {
		if(blProdDiferenciados.checked == true) {
			setDisabled("produtoDiferenciado_produto.idProduto", false);
		} else {
			setDisabled("produtoDiferenciado_produto.idProduto", true);
			resetValue("produtoDiferenciado");
		}
	}
	

	function lookup_produto_cb(dados, erros) {
		return lookupExactMatch({e:document.getElementById("produto.idProduto"), callBack:"lookup_produto_like", data:dados});
	}

	function lookup_produto_like_cb(dados, erros) {
		return lookupLikeEndMatch({e:document.getElementById("produto.idProduto"), data:dados});
	}

	
	/**
	 * Função que desabilita os campos de acordo com o status da coleta.
	 */
	function desabilitaHabilitaCampos() {
		var status = getElementValue("tpStatusColeta");

		setDisabled("nmContatoCliente", false);
		setDisabled("dsInfColeta", false);
		setDisabled("nrDddCliente", false);
		setDisabled("nrTelefoneCliente", false);
		//setDisabled("tpPedidoColeta", false);
		setDisabled("dhColetaDisponivel", false);
		setDisabled("hrLimiteColeta", false);
		setDisabled("dtPrevisaoColeta", false);
		setDisabled("enderecoPessoa.idEnderecoPessoa", false);
		setDisabled("obPedidoColeta", false);
		setDisabled("servicoAdicionalColetas", false);
		setDisabled("servicoAdicionalColetas_servicoAdicional", false);
		document.getElementById("nmContatoCliente").required = "false";
		document.getElementById("tpPedidoColeta").required = "false";
		document.getElementById("nrDddCliente").required = "false";
		document.getElementById("nrTelefoneCliente").required = "false";			
		document.getElementById("dhColetaDisponivel").required = "false";			
		document.getElementById("hrLimiteColeta").required = "false";
		document.getElementById("dtPrevisaoColeta").required = "false";			
		document.getElementById("enderecoPessoa.idEnderecoPessoa").required = "false";

		if(status == "AB"){
			setDisabled("vlTotalInformado", false);
			setDisabled("qtTotalVolumesInformado", false);
			setDisabled("psTotalInformado", false);
			setDisabled("psTotalAforadoInformado", false);
		}
		
		if(status == "AB" || status == "NM") {
			document.getElementById("nmContatoCliente").required = "true";
			//document.getElementById("tpPedidoColeta").required = "true";
			document.getElementById("nrDddCliente").required = "true";
			document.getElementById("nrTelefoneCliente").required = "true";			
			document.getElementById("dhColetaDisponivel").required = "true";			
			document.getElementById("hrLimiteColeta").required = "true";
			document.getElementById("dtPrevisaoColeta").required = "true";			
			document.getElementById("enderecoPessoa.idEnderecoPessoa").required = "true";
		}
		
		if(status == "MA" || status == "TR") {
			//setDisabled("tpPedidoColeta", true);
			setDisabled("enderecoPessoa.idEnderecoPessoa", true);
			document.getElementById("nmContatoCliente").required = "true";
			document.getElementById("nrDddCliente").required = "true";
			document.getElementById("nrTelefoneCliente").required = "true";			
			document.getElementById("dhColetaDisponivel").required = "true";			
			document.getElementById("hrLimiteColeta").required = "true";
			document.getElementById("dtPrevisaoColeta").required = "true";			
		}		
		
		if(status == "EX" || status == "AD" || status == "NT" || status == "ED") {
			setDisabled("nmContatoCliente", true);
			setDisabled("nrDddCliente", true);
			setDisabled("dsInfColeta", true);
			setDisabled("nrTelefoneCliente", true);
			//setDisabled("tpPedidoColeta", true);
			setDisabled("dhColetaDisponivel", true);
			setDisabled("hrLimiteColeta", true);
			setDisabled("dtPrevisaoColeta", true);
			setDisabled("enderecoPessoa.idEnderecoPessoa", true);
			setDisabled("servicoAdicionalColetas", true);
			setDisabled("servicoAdicionalColetas_servicoAdicional", true);
		}
		
		if(status == "CA" || status == "FI") {
			setDisabled("nmContatoCliente", true);
			setDisabled("nrDddCliente", true);
			setDisabled("nrTelefoneCliente", true);
			//setDisabled("tpPedidoColeta", true);
			setDisabled("dhColetaDisponivel", true);
			setDisabled("dsInfColeta", true);
			setDisabled("hrLimiteColeta", true);
			setDisabled("dtPrevisaoColeta", true);
			setDisabled("enderecoPessoa.idEnderecoPessoa", true);
			setDisabled("obPedidoColeta", true);
			setDisabled("servicoAdicionalColetas", true);
			setDisabled("servicoAdicionalColetas_servicoAdicional", true);
		}	
	}
	
	/**
	 * Carrega dados na tela
	 */
	function carregaDados() {		

		var data = new Array();
		data.nrCep = getElementValue("nrCep")
		data.dsBairro = getElementValue("dsBairro");
		data.dsEndereco = getElementValue("edColeta");
		data.nrEndereco = getElementValue("nrEndereco");
		data.dsComplemento = getElementValue("dsComplementoEndereco");
		formataEndereco(data);
		
		var comboBox1 = document.getElementById("moeda1");
		for(var i=0; i < comboBox1.length; i++) {
			if(comboBox1.options[i].text == getElementValue("moeda.siglaSimbolo")) {
				comboBox1.options[i].selected = "true";
			}
		}

		var comboBox2 = document.getElementById("moeda2");
		for(var i=0; i < comboBox2.length; i++) {
			if(comboBox2.options[i].text == getElementValue("moeda.siglaSimbolo")) {
				comboBox2.options[i].selected = "true";
			}
		}		
		
		setDisabled("storeButton", false);
		setDisabled("cancelarButton", false);
	}
	
	/**
	 * Chama funçoes para preencher endereço, horario de corte...
	 */
	function chamaFuncoes(data) {
		buscarDadosComplementares(data);
		formataEndereco(data);
		
		return true;
	}

	
	/**
	 * Função que busca dados complementares para coleta.
	 */
	function buscarDadosComplementares(data) {
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "idEnderecoPessoa", data.idEnderecoPessoa);
		setNestedBeanPropertyValue(mapCriteria, "nrCep", data.nrCep);
		setNestedBeanPropertyValue(mapCriteria, "idCliente", getElementValue("cliente.idCliente"));
	    setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", getElementValue("dhColetaDisponivel"));
		
		var sdo = createServiceDataObject("lms.coleta.manterColetasAction.getDadosComplementares", "buscarDadosComplementares", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	/**
	 * Retorno da pesquisa de Dados Complementares.
	 */
	function buscarDadosComplementares_cb(data, error) {
		if (!error){
			setElementValue("hrLimiteColeta", setFormat(document.getElementById("hrLimiteColeta"), data.hrCorteCliente));
	
			setElementValue("horarioCorte", setFormat(document.getElementById("horarioCorte"), data.horaCorteSolicitacao));
			setElementValue("rotaIntervaloCep.idRotaIntervaloCep", data.idRotaIntervaloCep);
			setElementValue("rotaColetaEntrega.idRotaColetaEntrega", data.idRotaColetaEntrega);
			setElementValue("rotaColetaEntrega.nrRota", data.numeroRota);
			setElementValue("rotaColetaEntrega.dsRota", data.descricaoRota);			
			
			setElementValue("filialByIdFilialResponsavel.idFilial", data.idFilial);	
			setElementValue("filialByIdFilialResponsavel.sgFilial", data.sgFilial);
			setElementValue("filialByIdFilialResponsavel.pessoa.nmFantasia", data.pessoa.nmFantasia);
			
			if (data.dhColetaDisponivel){
				var dataPrevisao = (data.dhColetaDisponivel).substring(0, (data.dhColetaDisponivel).indexOf(" "));
				setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dataPrevisao));
			}
		}else{
			alert(error);
		}	
	}	
	
	function formataEndereco(data) {
		var sdo = createServiceDataObject("lms.coleta.manterColetasAction.formataEndereco", "formataEndereco", data);
		xmit({serviceDataObjects:[sdo]});	
	}

	function formataEndereco_cb(data, error) {
		if (!error) {
			setEndereco(data.endereco);
		}
	}
	
	function setEndereco(endereco) {
		setElementValue("endereco", endereco);
	}

	/**
	 * Seta o nome do Contato do Cliente para o campo hidden Nome do Solicitante.
	 */
	function setaContatoClienteEmSolicitante() {
		setElementValue("nmSolicitante", getElementValue("nmContatoCliente"));
		
		return true;
	}
	
	/**
	 * Seta os dados do Endereço na tela pai após salvar o registro.
	 */
	function setaDadosEndereco(mapEndereco) {
		setElementValue("idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
		setElementValue("enderecoPessoa.idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
		setElementValue("edColeta", mapEndereco.dsEndereco);
		setElementValue("nrEndereco", mapEndereco.nrEndereco);
		setElementValue("dsComplementoEndereco", mapEndereco.dsComplemento);
		setElementValue("dsBairro", mapEndereco.dsBairro);
		setElementValue("nrCep", mapEndereco.nrCep);
		setElementValue("municipio.idMunicipio", mapEndereco.municipio.idMunicipio);
		setElementValue("municipio.nmMunicipio", mapEndereco.municipio.nmMunicipio);
		setElementValue("municipio.unidadeFederativa.sgUnidadeFederativa", mapEndereco.municipio.unidadeFederativa.sgUnidadeFederativa);
					
		chamaFuncoes(mapEndereco);
	}	
	
	/**
	 * Retorno do Salvamento do registro de Pedido Coleta
	 */
	function salvarRegistro_cb(data, erros, errorMsg, eventObj) {
		store_cb(data, erros, errorMsg, eventObj);
	
		if(!erros) {
			var store = "true";
			var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("detalheColeta");
			setDisabled(tabDet.getElementById("salvarDetalheButton"), true);
			
			setElementValue("vlTotalVerificado", setFormat(document.getElementById("vlTotalVerificado"), data.vlTotalVerificado));
			setElementValue("qtTotalVolumesVerificado", setFormat(document.getElementById("qtTotalVolumesVerificado"), data.qtTotalVolumesVerificado));
			setElementValue("psTotalVerificado", setFormat(document.getElementById("psTotalVerificado"), data.psTotalVerificado));
			setElementValue("psTotalAforadoVerificado", setFormat(document.getElementById("psTotalAforadoVerificado"), data.psTotalAforadoVerificado));
		}
	}
	
	/**
	 * Verifica se horario corte e maior que a hora atual (hora de disponibilidade para coleta)
	 */	
	function verificaDataDisponibilidade_OnChange() {
		var dhColetaDisponivel = getElementValue("dhColetaDisponivel");
		var horarioCorte = getElementValue("horarioCorte");
		
		if(horarioCorte != "") {
			var mapCriteria = new Array();    
		    setNestedBeanPropertyValue(mapCriteria, "dhColetaDisponivel", dhColetaDisponivel);
	    	setNestedBeanPropertyValue(mapCriteria, "horarioCorte", horarioCorte);
		
			var sdo = createServiceDataObject("lms.coleta.manterColetasAction.getDataPrevisaoColeta", "verificaDataPrevisaoColeta", mapCriteria);
			xmit({serviceDataObjects:[sdo]});	
		}
		
		return true;
	}	
	
	/**
	 * Retorno da verificação de horario de corte em getDataPrevisaoColeta()
	 */
	function verificaDataPrevisaoColeta_cb(data) {
		if (data._value){
			var dataPrevisao = (data._value).substring(0, (data._value).indexOf(" "));	
			setElementValue("dtPrevisaoColeta", setFormat(document.getElementById("dtPrevisaoColeta"), dataPrevisao));
		} else {
			resetValue("dtPrevisaoColeta");
		}
	}
	
	function exibeRestricoes() {
		showModalDialog('/coleta/cadastrarPedidoColeta.do?cmd=restricoesColeta',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}	

</script>