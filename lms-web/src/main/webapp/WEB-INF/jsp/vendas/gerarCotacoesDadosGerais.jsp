<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.gerarCotacoesAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="LMS-01099"/>
		<adsm:include key="LMS-01263"/>
		<adsm:include key="LMS-04021"/>
		<adsm:include key="LMS-04063"/>
		<adsm:include key="LMS-04066"/>
		<adsm:include key="LMS-01195"/>
		<adsm:include key="LMS-01321"/>
		<adsm:include key="LMS-01322"/>
		<adsm:include key="requiredField"/>
		<adsm:include key="remetente"/>
		<adsm:include key="destinatario"/>
		<adsm:include key="responsavelFrete"/>
		<adsm:include key="municipioOrigem"/>
		<adsm:include key="municipioDestino"/>
		<adsm:include key="valorMercadoria"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/gerarCotacoes"
		idProperty="idCotacao"
		height="370"
		newService="lms.vendas.gerarCotacoesAction.criaCotacao"
		service="lms.vendas.gerarCotacoesAction.findCotacaoById"
		onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="divisaoCliente"/>
		<adsm:hidden property="obCotacao"/>
		<adsm:hidden property="origem" value="cotacao"/>
		<adsm:hidden property="dsMotivo"/>
		<adsm:hidden property="tabelaPreco.dsTabelaPreco"/>
		<adsm:hidden property="pendencia_idPendencia"/>
		<adsm:hidden property="pendenciaDimensoes_idPendencia"/>
		<adsm:hidden property="pendenciaDimensoes_tpSituacaoPendencia_value"/>

		<adsm:textbox
			property="nrCotacao"
			label="numeroCotacao"
			dataType="integer"
			size="8"
			maxLength="8"
			labelWidth="16%"
			width="22%"
			disabled="true"/>
		<adsm:checkbox
			property="blFrete"
			label="frete"
			labelWidth="12%"
			width="10%"/>
		<adsm:checkbox
			property="blServicosAdicionais"
			label="servicosAdicionais"
			onclick="habilitaServicoAdicional(this);"
			labelWidth="15%"
			width="25%"/>

		<adsm:hidden property="servico.tpModal"/>
		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:combobox
			property="servico.idServico"
			label="servico"
			boxWidth="280"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			onDataLoadCallBack="servico"
			service="lms.vendas.gerarCotacoesAction.findServico"
			required="true"
			labelWidth="16%"
			width="44%"
			onchange="return servicoOnChange(this);">
			<adsm:propertyMapping
				relatedProperty="servico.tpModal"
				modelProperty="tpModal"/>
			<adsm:propertyMapping
				relatedProperty="servico.tpAbrangencia"
				modelProperty="tpAbrangencia"/>
		</adsm:combobox>

		<adsm:combobox
			property="tpFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"
			onchange="return changeTpFrete();"
			required="true"
			labelWidth="15%"
			width="25%"/>

		<adsm:textbox
			property="tpDocumentoCotacao"
			label="tipoDocumento"
			labelWidth="16%"
			dataType="text"
			width="44%"
			size="22"
			disabled="true"/>

		<adsm:combobox
			property="tpCalculo"
			label="tipoCalculo"
			domain="DM_TIPO_CALCULO_COTACAO"
			labelWidth="15%"
			width="25%"
			boxWidth="87"
			required="true"/>

		<adsm:section caption="dadosIntegrantes"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			label="remetente"
			idProperty="idCliente"
			service="lms.vendas.gerarCotacoesAction.findCliente"
			dataType="text"
			property="clienteByIdClienteSolicitou"
			criteriaProperty="pessoa.nrIdentificacao"
			allowInvalidCriteriaValue="true"
			size="20"
			maxLength="20"
			required="true"
			labelWidth="16%"
			width="63%"
			onDataLoadCallBack="clienteSolicitou"
			onPopupSetValue="clienteSolicitouPopup"
			onchange="return clienteSolicitouChange();">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteSolicitou.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.tpPessoa.value"
				relatedProperty="clienteByIdClienteSolicitou.pessoa.tpPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteSolicitou.tpCliente"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.description"
				relatedProperty="clienteByIdClienteSolicitou.tpCliente.description"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteSolicitou.pessoa.nmPessoa"
				size="60"
				maxLength="40"
				required="true"
				onchange="return clienteSolicitouNameChange();"
				/>
		</adsm:lookup>

		<adsm:hidden property="clienteByIdClienteSolicitou.pessoa.tpPessoa"/>
		<adsm:hidden property="clienteByIdClienteSolicitou.tpCliente"/>
		<adsm:textbox
			property="clienteByIdClienteSolicitou.tpCliente.description"
			label="tipoCliente"
			dataType="text"
			size="10"
			serializable="false"
			labelWidth="10%"
			width="10%"
			disabled="true"/>

		<adsm:combobox
			property="clienteByIdClienteSolicitou.tpSituacaoTributaria"
			label="situacaoTributaria"
			domain="DM_SIT_TRIBUTARIA_CLIENTE"
			boxWidth="140"
			labelWidth="16%"
			width="20%"
			required="true"/>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteSolicitou.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			onchange="return changeInscricaoEstadual(this)"
			service=""
			boxWidth="110"
			labelWidth="3%"
			width="16%">
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteSolicitou.tpSituacaoTributaria"
				modelProperty="tpSituacaoTributaria"/>
		</adsm:combobox>

		<adsm:lookup
			label="municipio"
			property="municipioByIdMunicipioOrigem"
			action="/municipios/manterMunicipios"
			service="lms.vendas.gerarCotacoesAction.findMunicipio"
			dataType="text"
			criteriaProperty="nmMunicipio"
			idProperty="idMunicipio"
			exactMatch="false"
			onDataLoadCallBack="municipioOrigem"
			onchange="return municipioOrigemChange();"
			onPopupSetValue="municipioOrigemPopup"
			size="20"
			maxLength="50"
			labelWidth="8%"
			minLengthForAutoPopUpSearch="3"
			width="20%"
			required="true"/>

		<adsm:hidden property="filialByIdFilialOrigem.nmFilial" serializable="false"/>
		<adsm:hidden property="idTipoLocalizacaoOrigem"/>
		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="origem"
			onDataLoadCallBack="filialOrigemLoad"
			onchange="return filialOrigemChange();"
			onPopupSetValue="filialOrigemPopup"
			dataType="text"
			size="5"
			maxLength="3"
			labelWidth="6%"
			width="10%">
		</adsm:lookup>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.gerarCotacoesAction.findCliente"
			dataType="text"
			property="clienteByIdClienteDestino"
			idProperty="idCliente"
			onDataLoadCallBack="clienteDestino"
			criteriaProperty="pessoa.nrIdentificacao"
			allowInvalidCriteriaValue="true"
			label="destinatario"
			size="20"
			maxLength="20"
			onPopupSetValue="clienteDestinoPopup"
			required="true"
			labelWidth="16%"
			width="63%"
			onchange="return clienteDestinoChange();">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestino.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.tpPessoa.value"
				relatedProperty="clienteByIdClienteDestino.pessoa.tpPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteDestino.tpCliente"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.description"
				relatedProperty="clienteByIdClienteDestino.tpCliente.description"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestino.pessoa.nmPessoa"
				size="60"
				maxLength="40"
				required="true"
				onchange="return clienteDestinoNameChange();"
				/>
		</adsm:lookup>

		<adsm:hidden property="clienteByIdClienteDestino.pessoa.tpPessoa"/>
		<adsm:hidden property="clienteByIdClienteDestino.tpCliente"/>
		<adsm:textbox
			property="clienteByIdClienteDestino.tpCliente.description"
			label="tipoCliente"
			dataType="text"
			size="10"
			serializable="false"
			labelWidth="10%"
			width="10%"
			disabled="true"/>

		<adsm:combobox
			property="clienteByIdClienteDestino.tpSituacaoTributaria"
			label="situacaoTributaria"
			domain="DM_SIT_TRIBUTARIA_CLIENTE"
			onchange="return validarFOB()"
			boxWidth="140"
			labelWidth="16%"
			width="20%"
			required="true"/>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteDestino.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			onchange="return changeInscricaoEstadual(this)"
			service=""
			boxWidth="110"
			labelWidth="3%"
			width="16%">
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteDestino.tpSituacaoTributaria"
				modelProperty="tpSituacaoTributaria"/>
		</adsm:combobox>

		<adsm:lookup
			label="municipio"
			property="municipioByIdMunicipioDestino"
			idProperty="idMunicipio"
			action="/municipios/manterMunicipios"
			service="lms.vendas.gerarCotacoesAction.findMunicipio"
			onchange="return municipioDestinoChange();"
			onPopupSetValue="municipioDestinoPopup"
			criteriaProperty="nmMunicipio"
			onDataLoadCallBack="municipioDestino"
			dataType="text"
			size="20"
			maxLength="50"
			labelWidth="8%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			width="20%"
			required="true"/>

		<adsm:hidden property="idTipoLocalizacaoDestino"/>
		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="destino"
			onDataLoadCallBack="filialDestinoLoad"
			onchange="return filialDestinoChange();"
			onPopupSetValue="filialDestinoPopup"
			dataType="text"
			size="5"
			maxLength="3"
			labelWidth="6%"
			width="10%"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.gerarCotacoesAction.findClienteResponsavel"
			dataType="text"
			property="clienteByIdCliente"
			idProperty="idCliente"
			allowInvalidCriteriaValue="true"
			criteriaProperty="pessoa.nrIdentificacao"
			onDataLoadCallBack="cliente"
			label="responsavelFrete"
			size="20"
			maxLength="20"
			onPopupSetValue="clienteResponsavelOnPopup"
			required="true"
			labelWidth="16%"
			width="63%"
			onchange="return clienteChange();">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdCliente.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdCliente.tpCliente"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.description"
				relatedProperty="clienteByIdCliente.tpCliente.description"/>
			<adsm:propertyMapping
				modelProperty="pessoa.tpPessoa"
				relatedProperty="tpDevedorFrete"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdCliente.pessoa.nmPessoa"
				size="60"
				maxLength="40"
				required="true"
				onchange="return clienteNameChange();"
				/>
		</adsm:lookup>

		<adsm:hidden property="clienteByIdCliente.tpCliente"/>
		<adsm:textbox
			property="clienteByIdCliente.tpCliente.description"
			label="tipoCliente"
			dataType="text"
			size="10"
			serializable="false"
			labelWidth="10%"
			width="10%"
			disabled="true"/>

		<adsm:combobox
			property="clienteByIdCliente.tpSituacaoTributaria"
			label="situacaoTributaria"
			domain="DM_SIT_TRIBUTARIA_CLIENTE"
			boxWidth="140"
			labelWidth="16%"
			width="20%"
			required="true"/>

		<adsm:combobox
			label="ie"
			property="clienteByIdCliente.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="3%"
			width="16%">
			<adsm:propertyMapping
				relatedProperty="clienteByIdCliente.tpSituacaoTributaria"
				modelProperty="tpSituacaoTributaria"/>
		</adsm:combobox>

		<adsm:lookup
			label="municipio"
			property="municipioByIdMunicipioResponsavel"
			idProperty="idMunicipio"
			action="/municipios/manterMunicipios"
			service="lms.vendas.gerarCotacoesAction.findMunicipio"
			onchange="return municipioResponsavelChange();"
			onPopupSetValue="municipioResponsavelPopup"
			criteriaProperty="nmMunicipio"
			onDataLoadCallBack="municipioResponsavel"
			dataType="text"
			size="20"
			maxLength="50"
			labelWidth="8%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			width="20%"
			required="true"/>

		<adsm:lookup
			service="lms.vendas.gerarCotacoesAction.findFilial"
			action="/municipios/manterFiliais"
			property="filialByIdFilialResponsavel"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filial"
			onDataLoadCallBack="filialResponsavelLoad"
			onchange="return filialResponsavelChange();"
			onPopupSetValue="filialResponsavelPopup"
			dataType="text"
			size="5"
			maxLength="3"
			labelWidth="6%"
			width="10%"/>

		<adsm:combobox
			property="tpDevedorFrete"
			label="tipoPessoa"
			domain="DM_TIPO_PESSOA_COTACAO"
			labelWidth="16%"
			width="19%"
			required="true"
			onchange="return validaTipoPessoa();"
			/>

		<adsm:textbox
			property="nrPpe"
			label="ppe"
			dataType="text"
			size="5"
			disabled="true"
			maxLength="3"
			labelWidth="6%"
			width="16%"
			unit="dias"
			serializable="false"/>

		<adsm:section caption="dadosContato"/>
		<adsm:textbox
			property="dsContato"
			label="nome"
			dataType="text"
			size="60"
			maxLength="60"
			required="true"
			labelWidth="16%"
			width="49%"/>

		<adsm:textbox
			property="nrTelefone"
			label="telefone"
			dataType="integer"
			size="12"
			maxLength="12"
			labelWidth="15%"
			required="true"
			width="20%"/>

		<adsm:textbox
			property="dsEmail"
			label="email"
			dataType="email"
			size="60"
			maxLength="60"
			required="true"
			labelWidth="16%"
			width="49%"/>

		<adsm:textbox
			property="nrFax"
			label="fax"
			dataType="integer"
			size="12"
			maxLength="12"
			labelWidth="15%"
			width="20%"/>

		<adsm:section caption="dadosNacinaisCotacao"/>
		<adsm:textbox
			property="nrNotaFiscal"
			label="notaFiscal"
			dataType="integer"
			labelWidth="16%"
			width="20%"
			maxLength="6"/>

		<adsm:combobox
			property="moeda.idMoeda"
			label="valorMercadoria"
			optionProperty="idMoeda"
			optionLabelProperty="dsSimbolo"
			onDataLoadCallBack="moedaPais"
			service="lms.vendas.gerarCotacoesAction.findMoeda"
			labelWidth="18%"
			width="20%"
			boxWidth="60">

			<adsm:textbox
				property="vlMercadoria"
				dataType="currency"
				required="true"
				size="9"/>
		</adsm:combobox>

		<adsm:textbox
			property="qtVolumes"
			label="volumes"
			dataType="integer"
			labelWidth="8%"
			width="10%"
			maxLength="6"
			size="10"/>

		<adsm:textbox
			property="psReal"
			label="pesoReal"
			dataType="weight"
			unit="kg"
			required="true"
			size="10"
			onchange="return validatePsReal();"
			labelWidth="16%"
			width="21%"/>

		<adsm:textbox
			property="psCubado"
			label="pesoCubado"
			dataType="weight"
			unit="kg"
			size="10"
			labelWidth="12%"
			width="13%"
			required="true"/>

		<adsm:checkbox
			property="blMercadoriaExportacao"
			label="mercadoriaExportacao"
			labelWidth="20%"
			width="13%"/>

		<adsm:combobox
			label="naturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			optionProperty="idNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			service="lms.vendas.gerarCotacoesAction.findNaturezaProduto"
			labelWidth="16%"
			width="46%"
			required="true"
			boxWidth="140"/>

		<adsm:textbox
			property="dtValidade"
			label="dataValidade"
			dataType="JTDate"
			labelWidth="18%"
			width="20%"
			disabled="true"
			serializable="false"/>

		<adsm:hidden property="filial.idFilial" serializable="false"/>
		<adsm:hidden property="usuarioByIdUsuarioRealizou.idUsuario" serializable="false"/>
		<adsm:complement label="funcionarioCotacao" labelWidth="16%" width="46%">
			<adsm:textbox
				dataType="text"
				property="usuarioByIdUsuarioRealizou.funcionario.nrMatricula"
				size="15"
				serializable="false"
				disabled="true"/>
			<adsm:textbox
				dataType="text"
				property="usuarioByIdUsuarioRealizou.funcionario.nmUsuario"
				size="30"
				serializable="false"
				disabled="true"/>
		</adsm:complement>

		<adsm:textbox
			property="dtGeracaoCotacao"
			label="dataCotacao"
			dataType="JTDate"
			labelWidth="18%"
			width="20%"
			disabled="true"
			serializable="false"/>

		<adsm:complement label="autorizadoPor" labelWidth="16%" width="46%">
			<adsm:textbox
				dataType="text"
				property="usuarioByIdUsuarioAprovou.funcionario.nrMatricula"
				size="15"
				serializable="false"
				disabled="true"/>
			<adsm:textbox
				dataType="text"
				property="usuarioByIdUsuarioAprovou.funcionario.nmUsuario"
				size="30"
				serializable="false"
				disabled="true"/>
		</adsm:complement>

		<adsm:textbox
			property="tpSituacao.description"
			label="situacao"
			dataType="text"
			size="15"
			labelWidth="18%"
			width="20%"
			serializable="false"
			disabled="true"/>
		<adsm:hidden property="tpSituacao"/>

		<adsm:textbox
			property="dtEfetivacao"
			label="dataEfetivacao"
			dataType="JTDateTimeZone"
			labelWidth="16%"
			width="46%"
			serializable="false"
			size="18"
			disabled="true"/>

		<adsm:textbox
			property="nrDocumentoCotacao"
			label="documentoServico"
			dataType="text"
			labelWidth="18%"
			width="20%"
			size="15"
			serializable="false"
			disabled="true"/>

		<adsm:label key="branco" width="62%"/>
		<adsm:textbox
			property="pedidoColeta.nrColeta"
			label="numeroColeta"
			dataType="text"
			labelWidth="18%"
			width="20%"
			maxLength="12"
			size="15"
			serializable="false"
			disabled="true"/>

		<adsm:section caption="dadosFreteAereo"/>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.vendas.gerarCotacoesAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeOrigem"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="34%"
			disabled="true">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
				serializable="false"
				size="30"
				maxLength="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.vendas.gerarCotacoesAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="34%"
			disabled="true">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"
				serializable="false"
				size="30"
				maxLength="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:checkbox
			property="blColetaEmergencia"
			disabled="true"
			label="coletaEmergencia"
			labelWidth="16%"
			width="8%"/>

		<adsm:checkbox
			property="blEntregaEmergencia"
			label="entregaEmergencia"
			labelWidth="16%"
			width="10%"
			disabled="true"/>

		<adsm:combobox
			property="produtoEspecifico.idProdutoEspecifico"
			label="produtoEspecifico"
			disabled="true"
			optionLabelProperty="nmTarifaEspecifica"
			optionProperty="idProdutoEspecifico"
			service="lms.vendas.gerarCotacoesAction.findProdutoEspecifico"
			labelWidth="16%"
			width="34%"
			boxWidth="240"/>

		<adsm:buttonBar lines="2">
			<adsm:button
				caption="observacoes"
				id="observacaoButton"
				boxWidth="90"
				onclick="openObservacoes();"/>
			<adsm:button
				caption="dimensoes"
				id="dimensaoButton"
				boxWidth="80"
				onclick="showDimensoes()"/>
			<adsm:button
				caption="servicosAdicionais"
				id="servicoButton"
				boxWidth="125"
				onclick="openServicosAdicionaisTpDocumento(getElementValue('idCotacao'));"/>
			<adsm:storeButton
				caption="calcularCotacao"
				service="lms.vendas.gerarCotacoesAction.validateCotacaoPrimeiraFase"
				callbackProperty="validateCotacao"
				id="calcularButton"/>

			<adsm:button
				caption="pedidoColeta"
				boxWidth="115"
				id="pedidoButton"
				action="/coleta/cadastrarPedidoColeta"
				cmd="main">
				<adsm:linkProperty src="usuarioByIdUsuarioRealizou.idUsuario" target="usuario.idUsuario" disabled="true"/>
				<adsm:linkProperty src="filial.idFilial" target="filialByIdFilialSolicitante.idFilial" disabled="true"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialResponsavel.idFilial" disabled="true"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialSigla" disabled="true"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.nmFilial" target="filialNome" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteSolicitou.idCliente" target="cliente.idCliente" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteSolicitou.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteSolicitou.pessoa.nrIdentificacao" target="cliente.pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteDestino.idCliente" target="clienteDestino.idCliente" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteDestino.pessoa.nmPessoa" target="clienteDestino.pessoa.nmPessoa" disabled="true"/>
				<adsm:linkProperty src="clienteByIdClienteDestino.pessoa.nrIdentificacao" target="clienteDestino.pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="municipioByIdMunicipioOrigem.idMunicipio" target="municipioOrigem.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioByIdMunicipioOrigem.nmMunicipio" target="municipioOrigem.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioByIdMunicipioDestino.idMunicipio" target="municipioDestino.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioByIdMunicipioDestino.nmMunicipio" target="municipioDestino.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="moeda.idMoeda" target="moeda.idMoeda" disabled="true"/>
				<adsm:linkProperty src="dsContato" target="nmContatoCliente" disabled="true"/>
				<adsm:linkProperty src="dsContato" target="nmSolicitante" disabled="true"/>
				<adsm:linkProperty src="vlMercadoria" target="vlTotalInformado" disabled="true"/>
				<adsm:linkProperty src="psReal" target="psTotalInformado" disabled="true"/>
				<adsm:linkProperty src="psCubado" target="pesoAforado" />
				<adsm:linkProperty src="tpFrete" target="tpFrete" disabled="true"/>
				<adsm:linkProperty src="idCotacao" target="cotacao.idCotacao" disabled="true"/>
				<adsm:linkProperty src="servico.idServico" target="servico.idServico" disabled="true"/>
				<adsm:linkProperty src="naturezaProduto.idNaturezaProduto" target="naturezaProduto.idNaturezaProduto" disabled="true"/>
				<adsm:linkProperty src="origem" target="origem" disabled="true"/>
			</adsm:button>

			<adsm:button
				caption="reprovar"
				boxWidth="72"
				id="reprovarButton"
				onclick="openReprovar();"/>
			<adsm:button
				id="imprimirButton"
				caption="imprimir"
				onclick="imprime(getElementValue('idCotacao'));"/>
			<adsm:button caption="historicoAprovacao" id="btnFluxoAprovacao"
				onclick="historicoAprovacao()"/>
			<adsm:newButton
				id="cancelarButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript" src="../lib/digitarDadosCTRC.js"></script>
<script language="javascript" type="text/javascript" src="../lib/digitarCotacao.js"></script>
