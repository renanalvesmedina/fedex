<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:i18nLabels>
	<adsm:include key="parametro"/>
	<adsm:include key="valor"/>
	<adsm:include key="valorCheio"/>
	<adsm:include key="pcDesconto"/>
</adsm:i18nLabels>
<adsm:window service="lms.vendas.manterPropostasClienteProcAction" >
	<adsm:form action="/vendas/manterPropostasCliente" height="380" >

		<adsm:hidden property="simulacao.idSimulacao" serializable="true"/>
		<adsm:hidden property="pendencia.idPendencia" />
		<adsm:hidden property="isGenerate" value="false" />
		<adsm:hidden property="tpSituacaoAprovacao.value" />

		<%----------------------%>
		<%-- TELA DETALHAMENTO --%>
		<%----------------------%>

		<%----------Numero da proposta------------%>
		<adsm:textbox dataType="text" label="numeroProposta" property="nrProposta" size="10"
					  maxLength="10" labelWidth="13%" width="70%" disabled="true" />

		<%----------Cliente------------%>
		<adsm:lookup action="/vendas/manterDadosIdentificacao" dataType="text" criteriaProperty="pessoa.nrIdentificacao"
					 exactMatch="true" idProperty="idCliente" property="cliente" label="cliente" size="18"
					 maxLength="20" width="40%" labelWidth="13%" disabled="true"
					 service="lms.vendas.manterPropostasClienteAction.findClienteLookup" >

			<adsm:textbox dataType="text" disabled="true" serializable="false"
						  property="cliente.pessoa.nmPessoa" size="27" />
		</adsm:lookup>
		<%----------Divis�o cliente------------%>
		<adsm:combobox optionLabelProperty="dsDivisaoCliente" width="21%"
					   optionProperty="idDivisaoCliente" labelWidth="22%" boxWidth="150"
					   property="divisaoCliente.idDivisaoCliente" disabled="true"
					   service="lms.vendas.manterPropostasClienteAction.findDivisaoCombo"
					   label="divisao">
		</adsm:combobox>

		<%----------Tabela------------%>
		<adsm:lookup label="tabela" property="tabelaPreco"
					 service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
					 action="/tabelaPrecos/manterTabelasPreco" idProperty="idTabelaPreco"
					 criteriaProperty="tabelaPrecoString"
					 onclickPicker="onclickPickerLookupTabelaPreco()"
					 onDataLoadCallBack="dataLoadTabelaPreco"
					 afterPopupSetValue="aferPopupTabelaPreco"
					 onPopupSetValue="validateSubtipoTabelaPreco"
					 onchange="return onChangeTabelaPreco();" dataType="text" size="10" disabled="true"
					 maxLength="9" width="40%" labelWidth="13%">

			<adsm:textbox dataType="text" property="tabelaPreco.dsDescricao"
						  size="30" maxLength="30" disabled="true" />
		</adsm:lookup>
		<%----------Servi�o------------%>
		<adsm:combobox boxWidth="150" label="servico" onlyActiveValues="true"
					   optionLabelProperty="dsServico" optionProperty="idServico"
					   property="servico.idServico" disabled="true"
					   service="lms.vendas.manterPropostasClienteAction.findServicoCombo" width="21%"
					   labelWidth="22%" autoLoad="false">
		</adsm:combobox>

		<%----------Paga frete tonelada------------%>
		<adsm:checkbox label="pagaFreteTonelada"
					   property="blPagaFreteTonelada" labelWidth="13%" width="40%" disabled="true"/>
		<%----------Emite tabela com carga completa------------%>
		<adsm:checkbox label="emiteCargaCompleta" disabled="true"
					   property="blEmiteCargaCompleta" labelWidth="25%" width="6%" />

		<%----------Tabela FOB------------%>
		<adsm:lookup
				action="/tabelaPrecos/manterTabelasPreco"
				criteriaProperty="tabelaPrecoString"
				dataType="text"
				exactMatch="true"
				idProperty="idTabelaPreco"
				label="tabelaFob"
				maxLength="9"
				property="tabelaPrecoFob"
				service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
				onclickPicker="onclickPickerLookupTabelaPrecoFob()"
				onPopupSetValue="validateSubtipoTabelaPrecoFob"
				afterPopupSetValue="afterPopupTabelaPrecoFob"
				onDataLoadCallBack="lookupTabelaPrecoFob"
				onchange="return changeTabelaPrecoFob();"
				size="10" disabled="true"
				labelWidth="13%"
				width="40%">

			<adsm:textbox dataType="text" disabled="true"
						  property="tabelaPrecoFob.dsDescricao" size="30" />
		</adsm:lookup>
		<%----------Tipo de gera��o------------%>
		<adsm:combobox
				label="tipoGeracao"
				property="tpGeracaoProposta"
				onlyActiveValues="true"
				domain="DM_TIPO_GERACAO_PROPOSTA"
				labelWidth="22%"
				width="21%"
				boxWidth="150"
				autoLoad="false" disabled="true"
				renderOptions="true"/>

		<%----------------------%>
		<%-- FIM TELA DETALHAMENTO --%>
		<%----------------------%>

		<adsm:section caption="formalidades"/>



		<%----------------------%>
		<%-- TELA FORMALIDADES --%>
		<%----------------------%>

		<%----------Periodicidade de faturamento------------%>
		<adsm:combobox
				property="tpPeriodicidadeFaturamento"
				labelWidth="22%"
				width="31%"
				domain="DM_PERIODICIDADE_FATURAMENTO"
				label="periodicidadeFaturamento"
				disabled="true"/>
		<%----------Prazo para pagamento------------%>
		<adsm:textbox
				dataType="integer"
				property="nrDiasPrazoPagamento"
				label="prazoPagamento"
				minValue="0"
				size="3"
				labelWidth="22%"
				width="10%"
				unit="dias"
				disabled="true"/>

		<%----------Data de validade de proposta------------%>
		<adsm:textbox
				dataType="JTDate"
				property="dtValidadeProposta"
				label="dataValidadeProposta"
				labelWidth="22%"
				width="31%" disabled="true"
				picker="true" />
		<%----------Data de inicio de vig�ncia------------%>
		<adsm:textbox
				dataType="JTDate"
				property="dtTabelaVigenciaInicial"
				label="dataInicioVigencia"
				labelWidth="22%"
				width="18%" disabled="true"
				picker="true"/>

		<%----------Data de aceita��o do cliente------------%>
		<adsm:textbox
				dataType="JTDate"
				property="dtAceiteCliente"
				label="dataAceitacaoCliente"
				labelWidth="22%"
				width="31%" disabled="true"
				picker="true" />
		<%----------Data de aprova��o------------%>
		<adsm:textbox
				dataType="JTDate"
				property="dtAprovacao"
				label="dataAprovacao"
				labelWidth="22%"
				width="18%"
				disabled="true"
				picker="false" />

		<%----------Funcionario aprovador------------%>
		<adsm:textbox
				dataType="text"
				property="usuarioByIdUsuarioAprovou.dsDescricao"
				label="funcionarioAprovador"
				size="60"
				labelWidth="22%"
				width="83%"
				disabled="true" />

		<%----------Data de emiss�o da tabela------------%>
		<adsm:textbox
				dataType="JTDate"
				property="dtEmissaoTabela"
				label="dataEmissaoTabela"
				labelWidth="22%"
				width="31%"
				disabled="true"
				picker="false" />
		<%----------Situa��o de aprova��o------------%>
		<adsm:textbox
				dataType="text"
				property="tpSituacaoAprovacao"
				labelWidth="22%"
				width="18%"
				label="situacaoAprovacao"
				disabled="true"/>

		<%----------Observa��o------------%>
		<adsm:textarea
				label="observacao"
				maxLength="500"
				property="obProposta"
				labelWidth="13%"
				width="83%"
				rows="5" disabled="true"
				columns="80" />

		<%----------Efetivada------------%>
		<adsm:checkbox
				property="blEfetivada"
				labelWidth="13%"
				width="83%"
				label="efetivada"
				disabled="true"/>

		<%----------------------%>
		<%-- FIM TELA FORMALIDADES --%>
		<%----------------------%>

		<%----------------------%>
		<%-- RESUMO CLIENTE   --%>
		<%----------------------%>

		<adsm:section caption="resumo"/>

		<%----------Segmento de mercado------------%>
		<adsm:combobox property="segmentoMercado.idSegmentoMercado" onlyActiveValues="true"
					   optionLabelProperty="dsSegmentoMercado"
					   optionProperty="idSegmentoMercado" disabled="true"
					   service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findComboSegmentoMercado"
					   label="tipoCarga" labelWidth="16%" width="83%" boxWidth="200" >
			<adsm:propertyMapping relatedProperty="segmentoMercado.dsSegmentoMercado" modelProperty="dsSegmentoMercado"/>
			<adsm:hidden property="segmentoMercado.dsSegmentoMercado" serializable="true"/>
		</adsm:combobox>

		<%----------Valor faturamento previsto------------%>
		<adsm:combobox
				label="faturamentoPrevisto"
				onlyActiveValues="true"
				property="moedaByIdMoedaFatPrev.idMoeda"
				optionLabelProperty="siglaSimbolo"
				optionProperty="idMoeda"
				service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
				labelWidth="16%"
				width="83%" disabled="true"
				boxWidth="80"
				autoLoad="false"
				renderOptions="true">

			<adsm:textbox
					dataType="currency"
					property="vlFaturamentoPrevisto"
					size="9" disabled="true"
					maxLength="18"/>
		</adsm:combobox>


		<%----------Vendedor------------%>
		<adsm:lookup
				action="/configuracoes/consultarFuncionariosView"
				service="lms.vendas.manterPipelineClienteAction.findLookupFuncionario"
				dataType="text"
				required="false"
				disabled="true"
				property="usuarioByIdUsuario"
				idProperty="idUsuario"
				criteriaProperty="nrMatricula"
				label="vendedor"
				size="17"
				maxLength="10"
				exactMatch="true"
				width="83%"
				labelWidth="16%"
				onDataLoadCallBack="funcCallBack"
				onPopupSetValue="funcCallBack">

			<adsm:propertyMapping
					relatedProperty="usuarioByIdUsuario.nmUsuario"
					modelProperty="nmUsuario"/>
			<adsm:textbox
					dataType="text"
					property="usuarioByIdUsuario.nmUsuario"
					size="35"
					maxLength="50"
					disabled="true"
					serializable="true"/>
		</adsm:lookup>

		<%----------Produro------------%>
		<adsm:combobox
				property="produto"
				labelWidth="16%"
				width="40%"
				label="produto"
				disabled="true"/>

		<%----------Valor medio por Kg------------%>
		<adsm:combobox
				label="valorMedioKg"
				property="moeda.idMoeda"
				optionLabelProperty="dsSimbolo"
				optionProperty="idMoeda"
				service="lms.configuracoes.moedaService.findMoedasAtivasByPaisUsuario"
				labelWidth="15%"
				onDataLoadCallBack="carregaMoedaPadrao"
				disabled="true"
				width="25%">
			<adsm:textbox
					dataType="currency"
					property="vlMedioProdutoKilo"
					mask="###,###,###,###,##0.00" disabled="true"
					size="13"/>
		</adsm:combobox>


		<%----------------------%>
		<%-- FIM RESUMO CLIENTE   --%>
		<%----------------------%>

		<adsm:section  caption="fretePeso"/>

		<%----------------------%>
		<%--    FRETE PESO    --%>
		<%----------------------%>

		<%----------Minimo frete peso------------%>
		<adsm:hidden property="minimoFretePesoFlag" value="false"/>
		<adsm:combobox
				domain="DM_INDICADOR_FRETE_MINIMO_PROPOSTA"
				label="minimoFretePeso"
				labelWidth="20%"
				property="tpIndicadorMinFretePeso" disabled="true"
				width="36%">
			<adsm:textbox
					dataType="decimal"
					property="vlMinFretePeso"
					mask="###,###,###,###,##0.00" disabled="true"
					size="18"/>
		</adsm:combobox>

		<%-- Percentual minimo combo --%>
		<adsm:combobox
				domain="DM_ACRESCIMO_DESCONTO"
				label="percentualMinimo"
				labelWidth="15%"
				property="tpIndicadorFreteMinimo" disabled="true"
				width="25%">
			<adsm:textbox
					dataType="decimal" disabled="true"
					property="vlFreteMinimo"
					mask="###,##0.00"
					size="13"/>
		</adsm:combobox>

		<%-- Frete peso combo --%>
		<adsm:combobox
				domain="DM_ACRESCIMO_DESCONTO"
				label="fretePeso"
				labelWidth="20%" disabled="true"
				property="tpIndicadorFretePeso"
				width="36%">
			<adsm:textbox
					dataType="decimal"
					property="vlFretePeso" disabled="true"
					mask="###,###,###,###,##0.00000"
					size="18"/>
		</adsm:combobox>

		<%----------Paga peso excedente------------%>
		<adsm:checkbox
				property="blPagaPesoExcedente"
				label="pagaPesoExcedente"
				labelWidth="15%" disabled="true"
				width="18%"/>

		<%-- Diferenca Capital/Interior --%>
		<adsm:textbox
				dataType="decimal"
				property="pcDiferencaFretePeso"
				label="diferencaCapitalInterior"
				mask="##0.00"
				minValue="0.00"
				maxValue="100.00"
				labelWidth="20%" disabled="true"
				width="83%"
				size="18"/>

		<%----------------------%>
		<%-- FIM FRETE PESO    --%>
		<%----------------------%>

		<adsm:section caption="freteValor"/>

		<%----------------------%>
		<%-- FRETE VALOR      --%>
		<%----------------------%>

		<%-- Advalorem1 combo --%>
		<adsm:combobox
				domain="DM_INDICADOR_ADVALOREM"
				label="adValorem"
				labelWidth="16%" disabled="true"
				property="tpIndicadorAdvalorem"
				width="40%">
			<adsm:textbox
					dataType="decimal"
					property="vlAdvalorem"
					maxLength="15" disabled="true"
					size="18"/>
		</adsm:combobox>

		<%-- Diferen�a capital interior --%>
		<adsm:textbox
				dataType="decimal"
				property="pcDiferencaAdvalorem"
				label="diferencaCapitalInterior"
				mask="##0.00"
				minValue="0.00"
				maxValue="100.00"
				labelWidth="20%"
				width="18%" disabled="true"
				size="18"/>

		<%----------------------%>
		<%-- FIM FRETE VALOR  --%>
		<%----------------------%>

		<%----------------------%>
		<%-- ESPECIFICA��ES   --%>
		<%----------------------%>

		<adsm:section caption="especificacoes"/>

		<%-- Ufs de origem --%>
		<adsm:combobox
				label="origem"
				service="lms.vendas.gerarParametrosPropostaAction.findUnidadeFederativaFromBrasil"
				property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"
				optionLabelProperty="siglaDescricao"
				optionProperty="idUnidadeFederativa"
				onlyActiveValues="true"
				labelWidth="16%"
				width="40%"
				boxWidth="150"
				disabled="true"/>

		<%-- Localizacao de origem --%>
		<adsm:combobox
				label="tipoLocalizacao"
				service="lms.vendas.gerarParametrosPropostaAction.findTipoLocalizacaoOperacional"
				property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"
				optionLabelProperty="dsTipoLocalizacaoMunicipio"
				optionProperty="idTipoLocalizacaoMunicipio"
				onlyActiveValues="true"
				labelWidth="20%"
				width="20%" disabled="true"
				boxWidth="150"/>

		<%-- Gera expedicao check --%>
		<adsm:checkbox
				property="blFreteExpedido"
				labelWidth="16%"
				width="40%" disabled="true"
				label="geraExpedicao"/>

		<%-- Gera recepcao check --%>
		<adsm:checkbox
				property="blFreteRecebido"
				labelWidth="20%"
				width="18%" disabled="true"
				label="geraRecepcao"/>

		<%----------------------%>
		<%-- FIM ESPECIFICA��ES   --%>
		<%----------------------%>

		<adsm:section caption="destinos"/>
		<%----------------------%>
		<%--  GRID DESTINOS   --%>
		<%----------------------%>
		<adsm:grid
				property="destinoProposta"
				idProperty="idDestinoProposta"
				scrollBars="innerBoth"
				gridHeight="200"
				showGotoBox="false"
				selectionMode="none"
				showPagging="false"
				showTotalPageCount="false"
				autoSearch="false"
				service="lms.vendas.gerarParametrosPropostaAction.generateDestinosPropostaResumo"
				onDataLoadCallBack="onDataLoadCallBack"
				onPopulateRow="onPopulateRow"
				onRowClick="onRowClick">


			<adsm:editColumn
					property="siglaDescricao"
					title="destino"
					field="textbox"
					width="70"
					disabled="true"/>

			<%-- FRETE MINIMO --%>
			<adsm:editColumn
					property="tpIndicadorFreteMinimo"
					domain="DM_ACRESCIMO_DESCONTO"
					title="freteMinimo"
					field="combobox"
					width="90"
					disabled="true"/>
			<adsm:editColumn
					property="vlParametroFreteMinimo"
					dataType="decimal"
					mask="###,##0.00"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlFreteMinimo"
					dataType="decimal"
					mask="###,##0.00"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlCheioFreteMinimo"
					dataType="decimal"
					mask="###,##0.00"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>

			<%-- FRETE PESO --%>
			<adsm:editColumn
					property="tpIndicadorFretePeso"
					domain="DM_ACRESCIMO_DESCONTO"
					title="fretePeso"
					field="combobox"
					width="90"
					disabled="true"/>
			<adsm:editColumn
					property="vlParametroFretePeso"
					dataType="decimal"
					mask="###,###,###,###,##0.00000"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlFretePeso"
					dataType="decimal"
					mask="###,###,###,###,##0.00000"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlCheioFretePeso"
					dataType="decimal"
					mask="###,###,###,###,##0.00000"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>

			<%-- DIFEREN�A CAPITAL INTERIOR --%>
			<adsm:editColumn
					property="pcDiferencaFretePeso"
					dataType="decimal"
					mask="##0.00"
					minValue="0"
					maxValue="100"
					title="diferencaCapitalInterior"
					field="textbox"
					width="95"
					maxLength="18"
					disabled="true"/>

			<%-- AD VALOREM --%>
			<adsm:editColumn
					property="tpIndicadorAdvalorem"
					domain="DM_INDICADOR_ADVALOREM"
					title="adValorem"
					field="combobox"
					width="90"
					disabled="true"/>
			<adsm:editColumn
					property="vlParametroAdvalorem"
					dataType="decimal"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlAdvalorem"
					dataType="decimal"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="vlCheioAdvalorem"
					dataType="decimal"
					title=""
					field="textbox"
					width="60"
					maxLength="18"
					disabled="true"/>
			<adsm:editColumn
					property="pcDescontoAdvalorem"
					dataType="decimal"
					title=""
					field="textbox"
					width="70"
					maxLength="18"
					disabled="true"/>

			<%-- DIFEREN�A CAPITAL INTERIOR --%>
			<adsm:editColumn
					property="tpDiferencaAdvalorem"
					domain="DM_TIPO_DIFERENCA"
					title="diferencaCapitalInterior"
					field="combobox"
					width="80"
					disabled="true"/>
			<adsm:editColumn
					property="pcDiferencaAdvalorem"
					dataType="decimal"
					mask="##0.00"
					minValue="0"
					maxValue="100"
					title=""
					field="textbox"
					maxLength="18"
					width="95"
					disabled="true"/>
		</adsm:grid>


		<%----------------------%>
		<%--  FIM  DESTINOS   --%>
		<%----------------------%>

		<%----------------------%>
		<%--   PARAMETROS     --%>
		<%----------------------%>

		<adsm:section caption="gris"/>

		<%-- Percentual gris combo --%>
		<adsm:combobox
				property="tpIndicadorPercentualGris"
				domain="DM_INDICADOR_ADVALOREM"
				label="percentualGris"
				labelWidth="13%" disabled="true"
				width="83%">
			<adsm:textbox
					dataType="decimal" disabled="true"
					property="vlPercentualGris"
					mask="###,###,###,###,##0.00"
					size="18"/>
		</adsm:combobox>

		<%-- Minimo gris combo --%>
		<adsm:combobox
				domain="DM_INDICADOR_PARAMETRO_CLIENTE"
				label="minimoGris"
				labelWidth="13%" disabled="true"
				property="tpIndicadorMinimoGris"
				width="83%">
			<adsm:textbox
					dataType="decimal" disabled="true"
					property="vlMinimoGris"
					mask="###,###,###,###,##0.00"
					size="18"/>
		</adsm:combobox>


		<%-- Pedagio section--%>
		<adsm:section caption="pedagio"/>


		<adsm:combobox
				domain="DM_INDICADOR_PEDAGIO"
				label="valorPedagio"
				labelWidth="13%" disabled="true"
				property="tpIndicadorPedagio"
				width="83%">

			<adsm:textbox
					dataType="decimal"
					property="vlPedagio" disabled="true"
					mask="###,###,###,###,##0.00000"
					size="18"/>
		</adsm:combobox>

		<%-- TRT section --%>
		<adsm:section caption="trt"/>

		<%-- PERCENTUAL TRT COMBO --%>
		<adsm:combobox
				property="tpIndicadorPercentualTrt"
				domain="DM_INDICADOR_ADVALOREM"
				label="percentualTrt"
				labelWidth="13%" disabled="true"
				width="83%">
			<adsm:textbox
					dataType="decimal"
					property="vlPercentualTrt" disabled="true"
					mask="###,###,###,###,##0.00"
					size="18"/>
		</adsm:combobox>

		<%-- Minimo trt combo --%>
		<adsm:combobox
				domain="DM_INDICADOR_PARAMETRO_CLIENTE"
				label="minimoTrt"
				labelWidth="13%" disabled="true"
				property="tpIndicadorMinimoTrt"
				width="83%">
			<adsm:textbox
					dataType="decimal"
					property="vlMinimoTrt" disabled="true"
					mask="###,###,###,###,##0.00"
					size="18"/>
		</adsm:combobox>

		<%-- TDE SECTION --%>
		<adsm:section caption="tde"/>

		<%-- Percentual tde combo --%>
		<adsm:combobox
				property="tpIndicadorPercentualTde"
				domain="DM_INDICADOR_ADVALOREM"
				label="percentualTde"
				labelWidth="13%" disabled="true"
				defaultValue="T"
				width="83%">

			<adsm:textbox
					dataType="decimal"
					property="vlPercentualTde" disabled="true"
					mask="###,###,###,###,##0.00"
					size="18"/>

		</adsm:combobox>

		<%-- Minimo tde combo --%>
		<adsm:combobox
				domain="DM_INDICADOR_PARAMETRO_CLIENTE"
				label="minimoTde"
				labelWidth="13%" disabled="true"
				property="tpIndicadorMinimoTde"
				defaultValue="T"
				width="83%">

			<adsm:textbox
					dataType="decimal"
					property="vlMinimoTde" disabled="true"
					mask="###,###,###,###,##0.00"
					size="18"/>
		</adsm:combobox>

		<%----------------------%>
		<%--   PARAMETROS     --%>
		<%----------------------%>

		<%----------------------%>
		<%--  GENERALIDADES   --%>
		<%----------------------%>

		<adsm:section caption="generalidades"/>

		<adsm:grid
				idProperty="idGeneralidadeCliente"
				property="generalidadesList"
				autoSearch="false"
				gridHeight="100"
				width="755"
				unique="true"
				service="lms.vendas.manterPropostasClienteGeneralidadesAction.findPaginated"
				rowCountService="lms.vendas.manterPropostasClienteGeneralidadesAction.getRowCount"
				rows="8"
				showGotoBox="false"
				showPagging="false"
				selectionMode="none"
				onRowClick="onRowClick">

			<adsm:gridColumn
					title="generalidade"
					property="parcelaPreco.nmParcelaPreco"
					width="40%" />

			<adsm:gridColumn
					title="indicador"
					property="tpIndicador"
					isDomain="true"
					width="30%" />

			<adsm:gridColumn
					title="valorIndicador"
					property="vlGeneralidade"
					align="right"
					width="30%" />

		</adsm:grid>

		<%----------------------%>
		<%--  GENERALIDADES   --%>
		<%----------------------%>

		<adsm:buttonBar>
			<adsm:button
					id="btnHistoricoAprovacao"
					caption="historicoAprovacao"
					disabled="true"
					onclick="return showHistoricoAprovacao();" />
			<adsm:reportViewerButton
					id="btnImprimirProposta"
					caption="imprimirProposta" service="lms.vendas.emitirTabelaPropostaAction" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/*Fun��o chamada ao carregar a tela*/
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			findDadosSessao();
		}
		goHorseMain();
	}

	/*Obtem os  dados da sess�o*/
	function findDadosSessao() {
		var service = "lms.vendas.manterPropostasClienteProcAction.findResumoProposta";
		var sdo = createServiceDataObject(service, "findDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}

	/*Callback obtem os dados da sess�o*/
	function findDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		/*Seta os valores na tela*/
		onDataLoad_cb(data, error);
		setUsuario(data);
		setTpSituacaoAprovacao(data);

		/*Grid generalidades*/
		if(data.idParametroCliente != undefined){
			populaGeneralidades(data.idParametroCliente);
		}

		/*Popula grid de destinos*/
		populateGridDestinos(false);

		controlarBotoes();
	}

	/*Popula a lista de generalidades*/
	function populaGeneralidades(idParametroCliente) {

		var arguments = {
			_currentPage : '1' , _pageSize : '1',
			parametroCliente : {
				idParametroCliente : idParametroCliente
			}
		}
		generalidadesListGridDef.executeSearch(arguments, true);
	}

	function showHistoricoAprovacao() {
		var idPendencia = getElementValue("pendencia.idPendencia");
		if (idPendencia == "") {
			idPendencia = 0;
		}
		showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+idPendencia,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
	}

	function controlarBotoes(){
		var tpSituacaoAprovacao = getElementValue("tpSituacaoAprovacao.value");
		if (tpSituacaoAprovacao == "A") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", false);
		} else if (tpSituacaoAprovacao == "C") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", true);
		} else if (tpSituacaoAprovacao == "E" || tpSituacaoAprovacao == "R") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", (tpSituacaoAprovacao == "R"));
		} else if (tpSituacaoAprovacao == "") {
			setDisabled("btnImprimirProposta", true);
			setDisabled("btnHistoricoAprovacao", true);
		}
	}

	function setTpSituacaoAprovacao(data) {
		if (data.tpSituacaoAprovacao != undefined) {
			setElementValue("tpSituacaoAprovacao", data.tpSituacaoAprovacao.description);
			setElementValue("tpSituacaoAprovacao.value", data.tpSituacaoAprovacao.value);
		} else {
			setElementValue("tpSituacaoAprovacao", "");
			setElementValue("tpSituacaoAprovacao.value", "");
		}
	}
	function setUsuario(data) {
		if (data.usuarioByIdUsuarioAprovou != undefined) {
			var dsDescricao = "";
			var first = true;
			if (data.usuarioByIdUsuarioAprovou.nrMatricula != "" &&
					data.usuarioByIdUsuarioAprovou.nrMatricula != undefined) {
				first = false;
				dsDescricao += data.usuarioByIdUsuarioAprovou.nrMatricula;
			}
			if (data.usuarioByIdUsuarioAprovou.nmUsuario != "" &&
					data.usuarioByIdUsuarioAprovou.nmUsuario != undefined) {
				if (first) {
					dsDescricao += " ";
				}
				dsDescricao += " - " + data.usuarioByIdUsuarioAprovou.nmUsuario;
			}
			setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", dsDescricao);
		} else {
			setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", "");
		}
	}
	function populateGridDestinos(isGenerate) {
		setElementValue("isGenerate", isGenerate);
		var formPrincipal = buildFormBeanFromForm(document.forms[0]);
		destinoPropostaGridDef.executeSearch(formPrincipal, true);
	}

	/*Grid*/
	function onDataLoadCallBack_cb(data, error) {
		for (rowIndex=0; rowIndex < data.length; rowIndex++) {
			disableRows(rowIndex, true);
		}
	}

	function onPopulateRow(tr, data) {
		tr.style.backgroundColor = "#E7E7E7";
	}

	function disableRows(rowIndex, disabled) {
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"siglaDescricao"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorFreteMinimo"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlFreteMinimo"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorFretePeso"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlFretePeso"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"pcDiferencaFretePeso"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorAdvalorem"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlAdvalorem"), true);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"pcDiferencaAdvalorem"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"tpDiferencaAdvalorem"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlCheioFreteMinimo"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlCheioFretePeso"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlCheioAdvalorem"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"pcDescontoAdvalorem"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlParametroFreteMinimo"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlParametroFretePeso"), disabled);
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlParametroAdvalorem"), disabled);
	}

	/*N�o faz nada ao pressionar as linhas da tabela*/
	function onRowClick() {
		return false;
	}

	/*Carrega as moedas*/
	function carregaMoedaPadrao_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return false;
		}

		if( data != undefined ){
			var dados = getNestedBeanPropertyValue(data[0],"listaMoedas");
			var moedaPadrao = getNestedBeanPropertyValue(data[0],"moedaPadrao");

			if( dados != undefined ){
				comboboxLoadOptions({e:document.getElementById("moeda.idMoeda"), data: dados});

				if( moedaPadrao != undefined ){
					setElementValue(document.getElementById("moeda.idMoeda"),moedaPadrao);
				}
			}
		}
	}

	// Fun��o totalmente desaconselhada para manipular os dados na tela
	// afim de atender as necessidades do usuario no prazo que a arquitetura nao suporta :s
	function goHorseMain() {
		var headerTable = getElement("destinoProposta.headerTable");
		if(headerTable.children[0].children.length > 1) {
			return;
		}

		//Ajusta a descri��o da td de formalidades 
		goHorseShiftTdFormalidades();

		//Cria uma custom HeaderRow
		var gridHTML = new Array();
		gridHTML.push("<TR>");
		gridHTML.push('<td class="FmSep"></td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" colspan="2">'+getI18nMessage("parametro")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;">'+getI18nMessage("valor")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>'+getI18nMessage("valorCheio")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" colspan="2">'+getI18nMessage("parametro")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;">'+getI18nMessage("valor")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>'+getI18nMessage("valorCheio")+'</td>');
		gridHTML.push('<td class="FmSep"></td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" colspan="2">'+getI18nMessage("parametro")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;">'+getI18nMessage("valor")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>'+getI18nMessage("valorCheio")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>'+getI18nMessage("pcDesconto")+'</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>&nbsp;</td>');
		gridHTML.push('<td class="FmSep" style="text-align:center;" nowrap>&nbsp;</td>');
		gridHTML.push('<td class="FmSep"></td>');
		gridHTML.push("</TR>" );

		var fakeDiv = document.createElement("<DIV></DIV>");
		fakeDiv.innerHTML = "<table>" + gridHTML.join('') + "</table>";
		var tr = fakeDiv.children[0].children[0].children[0];

		headerTable.children[0].appendChild(tr);

		document.getElementById("destinoProposta.innerBoth").style.width= '755px';
		document.getElementById("destinoProposta.innerBoth").style.height= '275px';
		document.getElementById("destinoProposta.innerBoth").style.overflow= 'auto';
	}

	// Ajusta a descri��o da td de formalidades :s 
	function goHorseShiftTdFormalidades() {
		var tdFormalidades = getElement("anonymousForm").children[0].children[0].children[1].children[7].children[0];
		var value = tdFormalidades.innerHTML;
		tdFormalidades.innerHTML = value;
	}
</script>