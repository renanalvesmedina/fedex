<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window
		service="lms.vendas.manterPropostasClienteAction"
		onPageLoad="myOnPageLoad"
		onPageLoadCallBack="myOnPageLoad">

	<adsm:form
			action="/vendas/manterPropostasClienteParam"
			onDataLoadCallBack="formDataLoadCallBack"
			idProperty="idParametroCliente">
		<adsm:i18nLabels>
			<adsm:include key="LMS-30017"/>
			<adsm:include key="LMS-02067"/>
		</adsm:i18nLabels>

		<adsm:hidden property="_idUfOrigem" serializable="false"/>
		<adsm:hidden property="_idUfDestino" serializable="false"/>

		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio" />
		<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>

		<!-- LMS-6166 - trazer somente munic�pios vigentes -->
		<adsm:hidden property="municipioByIdMunicipioOrigem.vigentes" value="S" serializable="false" />
		<adsm:hidden property="municipioByIdMunicipioDestino.vigentes" value="S" serializable="false" />

		<adsm:hidden property="_idTabelaDivisaoCliente" serializable="false" />
		<adsm:hidden property="_idDivisaoCliente" serializable="false" />

		<adsm:hidden property="blEfetivada" />

		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="simulacao.tpGeracaoProposta" />
		<adsm:hidden property="parametroCliente.idParametroCliente" />
		<adsm:hidden property="filial.idFilial" />
		<adsm:hidden property="tabelaPreco.moeda.dsMoeda" />
		<adsm:hidden property="simulacao.dtTabelaVigenciaInicial" />

		<adsm:hidden property="servico.idServico" />
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="tabelaPreco.idTabelaPreco" />
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		<adsm:hidden property="cliente.idCliente" />

		<adsm:hidden property="geral.tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:complement
				label="cliente"
				labelWidth="14%"
				width="45%"
				required="true">

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="cliente.pessoa.nrIdentificacao"
					serializable="false"
					size="20"
					maxLength="11" />

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="cliente.pessoa.nmPessoa"
					serializable="false"
					size="34" />

		</adsm:complement>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				label="divisao"
				labelWidth="100"
				property="divisaoCliente.dsDivisaoCliente"
				serializable="false"
				width="18%" />

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:complement
				label="tabela"
				labelWidth="14%"
				width="45%"
				required="true">

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.tabelaPrecoString"
					serializable="false"
					size="10"
					maxLength="9"/>

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.dsDescricao"
					serializable="false"
					size="44" />

		</adsm:complement>

		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				label="servico"
				labelWidth="100"
				size="30"
				property="servico.dsServico"
				serializable="false"
				width="25%"
				required="true"/>

		<%------------------------------------------%>
		<%-- COPY&PASTE FROM MANTER ROTAS - BEGIN --%>
		<%------------------------------------------%>

		<adsm:section caption="origem"/>

		<%-----------------------%>
		<%-- ZONA ORIGEM COMBO --%>
		<%-----------------------%>
		<adsm:combobox
				property="zonaOrigem.idZona"
				label="zona"
				service="lms.vendas.manterPropostasClienteAction.findZona"
				optionLabelProperty="dsZona"
				optionProperty="idZona"
				width="19%"
				onlyActiveValues="true"
				onchange="changeZona('Origem')"
				boxWidth="130"
				labelWidth="16%" />

		<%------------------------%>
		<%-- PAIS ORIGEM LOOKUP --%>
		<%------------------------%>
		<adsm:lookup
				service="lms.vendas.manterPropostasClienteAction.findLookupPais"
				action="/municipios/manterPaises"
				property="paisByIdPaisOrigem"
				idProperty="idPais"
				criteriaProperty="nmPais"
				label="pais"
				minLengthForAutoPopUpSearch="3"
				exactMatch="false"
				onchange="return changePais('Origem');"
				dataType="text"
				labelWidth="6%"
				width="25%"
				size="25"
				maxLength="60">

			<adsm:propertyMapping
					criteriaProperty="geral.tpSituacao"
					modelProperty="tpSituacao" />

			<adsm:propertyMapping
					addChangeListener="false"
					criteriaProperty="zonaOrigem.idZona"
					modelProperty="zona.idZona" />

			<adsm:propertyMapping
					relatedProperty="zonaOrigem.idZona"
					modelProperty="zona.idZona" />

		</adsm:lookup>

		<%---------------------%>
		<%-- UF ORIGEM COMBO --%>
		<%---------------------%>
		<adsm:combobox
				service="lms.vendas.manterPropostasClienteAction.findUnidadeFederativaByPais"
				property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"
				optionLabelProperty="siglaDescricao"
				optionProperty="idUnidadeFederativa"
				onlyActiveValues="true"
				label="uf"
				onchange="return changeUF('Origem');"
				boxWidth="150"
				onDataLoadCallBack="ufOrigemOnDataLoad"
				labelWidth="5%"
				width="29%">

			<adsm:propertyMapping
					criteriaProperty="paisByIdPaisOrigem.idPais"
					modelProperty="pais.idPais" />

		</adsm:combobox>

		<%--------------------------%>
		<%-- FILIAL ORIGEM LOOKUP --%>
		<%--------------------------%>
		<adsm:lookup
				service="lms.vendas.manterPropostasClienteAction.findLookupFilial"
				action="/municipios/manterFiliais"
				onDataLoadCallBack="filialOrigem"
				property="filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				exactMatch="false"
				label="filial"
				allowInvalidCriteriaValue="false"
				minLengthForAutoPopUpSearch="3"
				onchange="return changeFilial('Origem');"
				dataType="text"
				size="5"
				onPopupSetValue="changeFilialOrigemPopup"
				maxLength="3"
				width="37%"
				labelWidth="16%">

			<adsm:propertyMapping
					modelProperty="pessoa.nmFantasia"
					relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.nmPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.idPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping
					relatedProperty="zonaOrigem.idZona"
					modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />

			<%-- Seta o hidden para carregar a UF relacionada --%>
			<adsm:propertyMapping
					relatedProperty="_idUfOrigem"
					modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:textbox
					dataType="text"
					serializable="false"
					property="filialByIdFilialOrigem.pessoa.nmFantasia"
					size="30"
					maxLength="30"
					disabled="true"/>

		</adsm:lookup>

		<%-----------------------------%>
		<%-- MUNICIPIO ORIGEM LOOKUP --%>
		<%-----------------------------%>
		<adsm:lookup
				property="municipioByIdMunicipioOrigem"
				label="municipio"
				service="lms.vendas.manterPropostasClienteAction.findLookupMunicipioFilial"
				action="/municipios/manterMunicipiosAtendidos"
				serializable="false"
				idProperty="municipio.idMunicipio"
				minLengthForAutoPopUpSearch="2"
				criteriaProperty="municipio.nmMunicipio"
				size="30"
				labelWidth="10%"
				onchange="return municipioChange('Origem');"
				dataType="text"
				onDataLoadCallBack="municipioOrigem"
				maxLength="60"
				width="37%"
				onPopupSetValue="MunicipioOrigem_PopupSetValue"
				exactMatch="false">

			<adsm:propertyMapping
					modelProperty="filial.idFilial"
					criteriaProperty="filialByIdFilialOrigem.idFilial"
					addChangeListener="false"/>

			<adsm:propertyMapping
					modelProperty="filial.sgFilial"
					criteriaProperty="filialByIdFilialOrigem.sgFilial"
					addChangeListener="false" inlineQuery="false"/>

			<adsm:propertyMapping
					modelProperty="filial.pessoa.nmFantasia"
					criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
					addChangeListener="false"
					inlineQuery="false"/>

			<adsm:propertyMapping
					modelProperty="filial.idFilial"
					relatedProperty="filialByIdFilialOrigem.idFilial"/>

			<adsm:propertyMapping
					modelProperty="municipio.idMunicipio"
					relatedProperty="municipioByIdMunicipioOrigem.idMunicipio"/>

			<!-- LMS-6166 - trazer somente munic�pios vigentes -->
			<adsm:propertyMapping
					modelProperty="vigentes"
					criteriaProperty="municipioByIdMunicipioOrigem.vigentes" />

			<adsm:propertyMapping
					modelProperty="filial.sgFilial"
					relatedProperty="filialByIdFilialOrigem.sgFilial"/>

			<adsm:propertyMapping
					modelProperty="filial.pessoa.nmFantasia"
					relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.nmPais"
					modelProperty="municipio.unidadeFederativa.pais.nmPais" />

			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.idPais"
					modelProperty="municipio.unidadeFederativa.pais.idPais" />

			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping
					relatedProperty="zonaOrigem.idZona"
					modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping
					relatedProperty="_idUfOrigem"
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />

		</adsm:lookup>

		<%-----------------------------%>
		<%-- AEROPORTO ORIGEM LOOKUP --%>
		<%-----------------------------%>
		<adsm:lookup
				idProperty="idAeroporto"
				dataType="text"
				labelWidth="16%"
				service="lms.vendas.manterPropostasClienteAction.findLookupAeroporto"
				action="/municipios/manterAeroportos"
				criteriaProperty="sgAeroporto"
				property="aeroportoByIdAeroportoOrigem"
				size="5"
				maxLength="3"
				label="aeroporto"
				onchange="return changeAeroporto('Origem');"
				onDataLoadCallBack="aeroportoOrigem"
				width="80%"
				onPopupSetValue="changeAeroportoOrigemPopup">

			<adsm:propertyMapping
					criteriaProperty="geral.tpSituacao"
					modelProperty="tpSituacao" />

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.nmPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />

			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisOrigem.idPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />

			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping
					relatedProperty="zonaOrigem.idZona"
					modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping
					relatedProperty="_idUfOrigem"
					modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:propertyMapping
					relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />

			<adsm:textbox
					dataType="text"
					serializable="false"
					size="30"
					property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
					maxLength="30"
					disabled="true" />

		</adsm:lookup>

		<%-----------------------------------%>
		<%-- TIPO LOCALIZACAO ORIGEM COMBO --%>
		<%-----------------------------------%>
		<adsm:combobox
				optionLabelProperty="dsTipoLocalizacaoMunicipio"
				service="lms.vendas.manterPropostasClienteAction.findTipoLocalizacaoOperacional"
				property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"
				optionProperty="idTipoLocalizacaoMunicipio"
				label="tipoLocalizacao"
				labelWidth="16%"
				width="37%"
				boxWidth="200"
				onchange="return changeTpLocalizacao('Origem');"
				onlyActiveValues="true"/>


		<adsm:hidden property="grupoRegiaoOrigem.dsGrupoRegiao"/>
		<adsm:combobox
				label="grupoRegiao"
				property="grupoRegiaoOrigem.idGrupoRegiao"
				optionLabelProperty="dsGrupoRegiao"
				optionProperty="idGrupoRegiao"
				onchange="changeGrupoRegiao('Origem');"
				service="lms.vendas.manterPropostasClienteAction.findByUfGrupoRegiao"
				onDataLoadCallBack="grupoRegiaoOrigemOnDataLoad"
				onlyActiveValues="true"
				labelWidth="10%"
				width="37%"
				boxWidth="200">
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" modelProperty="unidadeFederativaByIdUf.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="tabelaPreco.idTabelaPreco" modelProperty="tabelaPreco.idTabelaPreco"/>
			<adsm:propertyMapping relatedProperty="grupoRegiaoOrigem.dsGrupoRegiao" modelProperty="dsGrupoRegiao"/>
		</adsm:combobox>



		<adsm:section caption="destino" />

		<%------------------------%>
		<%-- ZONA DESTINO COMBO --%>
		<%------------------------%>
		<adsm:combobox
				service="lms.vendas.manterPropostasClienteAction.findZona"
				property="zonaDestino.idZona"
				optionLabelProperty="dsZona"
				optionProperty="idZona"
				label="zona"
				onchange="changeZona('Destino')"
				onlyActiveValues="true"
				labelWidth="16%"
				width="19%"
				boxWidth="130" />

		<%-------------------------%>
		<%-- PAIS DESTINO LOOKUP --%>
		<%-------------------------%>
		<adsm:lookup
				dataType="text"
				labelWidth="6%"
				width="25%"
				service="lms.vendas.manterPropostasClienteAction.findLookupPais"
				action="/municipios/manterPaises"
				property="paisByIdPaisDestino"
				idProperty="idPais"
				criteriaProperty="nmPais"
				label="pais"
				minLengthForAutoPopUpSearch="3"
				exactMatch="false"
				onchange="return changePais('Destino');"
				size="25"
				maxLength="60">

			<adsm:propertyMapping
					criteriaProperty="geral.tpSituacao"
					modelProperty="tpSituacao" />

			<adsm:propertyMapping
					criteriaProperty="zonaDestino.idZona"
					modelProperty="zona.idZona"
					addChangeListener="false" />

			<adsm:propertyMapping
					relatedProperty="zonaDestino.idZona"
					modelProperty="zona.idZona" />

		</adsm:lookup>

		<%----------------------%>
		<%-- UF DESTINO COMBO --%>
		<%----------------------%>
		<adsm:combobox
				labelWidth="5%"
				width="29%"
				boxWidth="150"
				label="uf"
				service="lms.vendas.manterPropostasClienteAction.findUnidadeFederativaByPais"
				property="unidadeFederativaByIdUfDestino.idUnidadeFederativa"
				onlyActiveValues="true"
				optionLabelProperty="siglaDescricao"
				optionProperty="idUnidadeFederativa"
				onDataLoadCallBack="ufDestinoOnDataLoad"
				onchange="return changeUF('Destino');">

			<adsm:propertyMapping
					criteriaProperty="paisByIdPaisDestino.idPais"
					modelProperty="pais.idPais"/>

		</adsm:combobox>

		<%---------------------------%>
		<%-- FILIAL DESTINO LOOKUP --%>
		<%---------------------------%>
		<adsm:lookup
				minLengthForAutoPopUpSearch="3"
				label="filial"
				width="37%"
				service="lms.vendas.manterPropostasClienteAction.findLookupFilial"
				action="/municipios/manterFiliais"
				property="filialByIdFilialDestino"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				exactMatch="false"
				onDataLoadCallBack="filialDestino"
				maxLength="3"
				labelWidth="16%"
				onchange="return changeFilial('Destino');"
				dataType="text"
				size="5"
				onPopupSetValue="changeFilialDestinoPopup">

			<adsm:propertyMapping
					modelProperty="pessoa.nmFantasia"
					relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>

			<%-- Seta o Nome Pais automaticamente --%>
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.nmPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"
			/>
			<%-- Seta o ID Pais automaticamente --%>
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.idPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />

			<%-- Seta a Zona automaticamente --%>
			<adsm:propertyMapping
					relatedProperty="zonaDestino.idZona"
					modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />

			<%-- Seta o hidden para carregar a UF relacionada --%>
			<adsm:propertyMapping
					relatedProperty="_idUfDestino"
					modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:textbox
					dataType="text"
					property="filialByIdFilialDestino.pessoa.nmFantasia"
					serializable="false"
					size="30"
					maxLength="30"
					disabled="true" />

		</adsm:lookup>

		<%------------------------------%>
		<%-- MUNICIPIO DESTINO LOOKUP --%>
		<%------------------------------%>
		<adsm:lookup
				service="lms.vendas.manterPropostasClienteAction.findLookupMunicipioFilial"
				action="/municipios/manterMunicipiosAtendidos"
				maxLength="60"
				property="municipioByIdMunicipioDestino"
				label="municipio"
				idProperty="municipio.idMunicipio"
				serializable="false"
				size="30"
				criteriaProperty="municipio.nmMunicipio"
				exactMatch="false"
				minLengthForAutoPopUpSearch="2"
				width="37%"
				labelWidth="10%"
				onchange="return municipioChange('Destino');"
				onDataLoadCallBack="municipioDestino"
				dataType="text"
				onPopupSetValue="MunicipioDestino_PopupSetValue">

			<adsm:propertyMapping
					criteriaProperty="filialByIdFilialDestino.idFilial"
					modelProperty="filial.idFilial"
					addChangeListener="false"/>

			<adsm:propertyMapping
					criteriaProperty="filialByIdFilialDestino.sgFilial"
					modelProperty="filial.sgFilial"
					addChangeListener="false"
					inlineQuery="false"/>

			<adsm:propertyMapping
					criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia"
					modelProperty="filial.pessoa.nmFantasia"
					addChangeListener="false"
					inlineQuery="false"/>

			<adsm:propertyMapping
					relatedProperty="filialByIdFilialDestino.idFilial"
					modelProperty="filial.idFilial"/>

			<adsm:propertyMapping
					relatedProperty="municipioByIdMunicipioDestino.idMunicipio"
					modelProperty="municipio.idMunicipio"/>

			<!-- LMS-6166 - trazer somente munic�pios vigentes -->
			<adsm:propertyMapping
					modelProperty="vigentes"
					criteriaProperty="municipioByIdMunicipioDestino.vigentes" />

			<adsm:propertyMapping
					relatedProperty="filialByIdFilialDestino.sgFilial"
					modelProperty="filial.sgFilial"/>

			<adsm:propertyMapping
					relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"
					modelProperty="filial.pessoa.nmFantasia"/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.nmPais"
					modelProperty="municipio.unidadeFederativa.pais.nmPais"
			/>

			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.idPais"
					modelProperty="municipio.unidadeFederativa.pais.idPais" />

			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping
					relatedProperty="zonaDestino.idZona"
					modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping
					relatedProperty="_idUfDestino"
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />

		</adsm:lookup>

		<%------------------------------%>
		<%-- AEROPORTO DESTINO LOOKUP --%>
		<%------------------------------%>
		<adsm:lookup
				service="lms.vendas.manterPropostasClienteAction.findLookupAeroporto"
				action="/municipios/manterAeroportos"
				property="aeroportoByIdAeroportoDestino"
				idProperty="idAeroporto"
				criteriaProperty="sgAeroporto"
				onDataLoadCallBack="aeroportoDestino"
				dataType="text"
				onPopupSetValue="changeAeroportoDestinoPopup"
				label="aeroporto"
				onchange="return changeAeroporto('Destino');"
				size="5"
				maxLength="3"
				width="34%"
				labelWidth="16%" >

			<adsm:propertyMapping
					criteriaProperty="geral.tpSituacao"
					modelProperty="tpSituacao" />

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.nmPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />

			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping
					relatedProperty="paisByIdPaisDestino.idPais"
					modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />

			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping
					relatedProperty="zonaDestino.idZona"
					modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping
					relatedProperty="_idUfDestino"
					modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:propertyMapping
					modelProperty="pessoa.nmPessoa"
					relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" />

			<adsm:textbox
					dataType="text"
					serializable="false"
					property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"
					size="30"
					maxLength="30"
					disabled="true" />

		</adsm:lookup>

		<%------------------------------------%>
		<%-- TIPO LOCALIZACAO DESTINO COMBO --%>
		<%------------------------------------%>
		<adsm:combobox
				labelWidth="16%"
				width="37%"
				boxWidth="200"
				service="lms.vendas.manterPropostasClienteAction.findTipoLocalizacaoOperacional"
				property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"
				optionLabelProperty="dsTipoLocalizacaoMunicipio"
				optionProperty="idTipoLocalizacaoMunicipio"
				label="tipoLocalizacao"
				onchange="return changeTpLocalizacao('Destino');"
				onlyActiveValues="true" />

		<adsm:hidden property="grupoRegiaoDestino.dsGrupoRegiao"/>
		<adsm:combobox
				label="grupoRegiao"
				property="grupoRegiaoDestino.idGrupoRegiao"
				optionLabelProperty="dsGrupoRegiao"
				optionProperty="idGrupoRegiao"
				onchange="changeGrupoRegiao('Destino');"
				service="lms.vendas.manterPropostasClienteAction.findByUfGrupoRegiao"
				onDataLoadCallBack="grupoRegiaoDestinoOnDataLoad"
				onlyActiveValues="true"
				labelWidth="10%"
				width="37%"
				boxWidth="200">
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="unidadeFederativaByIdUf.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="tabelaPreco.idTabelaPreco" modelProperty="tabelaPreco.idTabelaPreco"/>
			<adsm:propertyMapping relatedProperty="grupoRegiaoDestino.dsGrupoRegiao" modelProperty="dsGrupoRegiao"/>
		</adsm:combobox>

		<%----------------------------------------%>
		<%-- COPY&PASTE FROM MANTER ROTAS - END --%>
		<%----------------------------------------%>

		<adsm:buttonBar>
			<adsm:button
					caption="copiarRotaParametros"
					id="btnCopiar"
					buttonType="copiar"
					onclick="copiarRotaParametros()"
					disabled="false" />
			<adsm:button
					id="btnContinuar"
					caption="continuar"
					onclick="onClickContinuar();"
					disabled="false"/>
			<adsm:newButton
					id="btnLimpar"/>
			<adsm:button
					id="btnExcluir"
					onclick="return onClickExcluir();"
					caption="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/rotaPreco.js"></script>
<script type="text/javascript">
	<!--

	getElement("servico.idServico").masterLink = "true";
	getElement("servico.dsServico").masterLink = "true";
	getElement("divisaoCliente.idDivisaoCliente").masterLink = "true";
	getElement("divisaoCliente.dsDivisaoCliente").masterLink = "true";

	var _dadosSessao;
	var _disableAll = false;
	var _copia = false;

	function initWindow(event) {
		if(event.name == "tab_load" || event.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var tabId = tabGroup.oldSelectedTab.properties.id;
			if (tabId == "pesq") {
				_copia = false;
			}
			if (getElementValue("parametroCliente.idParametroCliente") == "" && !_copia) {
				ajustaDadosSessao();
			}
			if (getElementValue("simulacao.idSimulacao") != "") {
				if (_disableAll) {
					disableAll();
				} else {
					if (getElementValue("parametroCliente.idParametroCliente") != "") {
						setDisabled("btnExcluir", false);
						setDisabled("btnCopiar", false);
					} else {
						setDisabled("btnExcluir", true);
						setDisabled("btnCopiar", true);
					}
					validateTipoGeracaoProposta();
				}
			} else {
				changeAbasStatus(true);
				enableFields();
				setDisabled("btnExcluir", true);
				setDisabled("btnCopiar", true);

				top._consulta = true;
				top._consultaFormalidades = true;
			}
			findDadosDefault();
		}
		if (event.name == "newButton_click") {
			changeAbasStatus(true);
			ajustaDadosSessao();
			enableFields();
			setDisabled("btnExcluir", true);
			setDisabled("btnCopiar", true);

			top._consulta = true;
			top._consultaFormalidades = true;
			_copia = false;
		}
		if (event.name == "gridRow_click") {
			findDadosDefault();
		}
		if (!_disableAll) {
			validateTipoGeracaoProposta();
			setDisabled("btnContinuar", false);
		}
		disableWorkflow();
	}

	function formDataLoadCallBack_cb(data, errorMessage, errorCode, eventObj) {
		if (errorMessage != undefined) {
			alert(errorMessage);
			return false;
		}
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);

		if (data.idSimulacao != "") {
			changeAbasStatus(false);
			setDisabled("btnExcluir", false);
		}

		if (data.blEfetivada == "S" || data.filial.idFilial != _dadosSessao.filial.idFilial) {
			disableAll();
			_disableAll = true;
		} else {
			enableFields();
			_disableAll = false;
		}
	}

	function myOnPageLoad() {
		onPageLoad();
		carregaDadosSessao();
	}

	function myOnPageLoad_cb() {
		onPageLoad_cb();
		disableWorkflow();
	}

	function disableWorkflow() {
		if (getElementValue("idProcessoWorkflow") != "") {
			disableAll();
		}
	}

	function onClickExcluir() {
		if (window.confirm(i18NLabel.getLabel('LMS-02067'))) {
			var idParametroCliente = getElementValue("parametroCliente.idParametroCliente");
			var service = "lms.vendas.manterPropostasClienteAction.removeById";
			var sdo = createServiceDataObject(service, "excluir", {id:idParametroCliente});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function excluir_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		newButtonScript();
	}

	function disableAll() {
		setDisabled(document, true);
	}
	function validateTipoGeracaoProposta() {
		if("C" == getElementValue("simulacao.tpGeracaoProposta")) {
			disableAll();
			setDisabled("paisByIdPaisOrigem.idPais", false);
			setDisabled("zonaOrigem.idZona", false);

			setDisabled("paisByIdPaisDestino.idPais", false);
			setDisabled("zonaDestino.idZona", false);
			setDisabled("btnContinuar", false);
			return false;
		}
		return true;
	}

	function enableFields() {
		if(validateTipoGeracaoProposta()) {
			setDisabled(document, false);
			setDisabled("cliente.pessoa.nmPessoa", true);
			setDisabled("tabelaPreco.dsDescricao", true);
			setDisabled("filialByIdFilialOrigem.pessoa.nmFantasia", true);
			setDisabled("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", true);
			setDisabled("filialByIdFilialDestino.pessoa.nmFantasia", true);
			setDisabled("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", true);
			setDisabled("divisaoCliente.dsDivisaoCliente", true);
			setDisabled("servico.dsServico", true);
		}
		disableWorkflow();
	}

	function carregaDadosSessao() {
		if (_dadosSessao == undefined) {
			var service = "lms.vendas.manterPropostasClienteAction.findDadosSessao";
			var sdo = createServiceDataObject(service, "carregaDadosSessao");
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function carregaDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		_dadosSessao = data;
	}

	function ajustaDadosSessao() {
		setElementValue("paisByIdPaisOrigem.idPais", _dadosSessao.pais.idPais);
		setElementValue("paisByIdPaisOrigem.nmPais", _dadosSessao.pais.nmPais);
		setElementValue("zonaOrigem.idZona", _dadosSessao.zona.idZona);
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});

		setElementValue("paisByIdPaisDestino.idPais", _dadosSessao.pais.idPais);
		setElementValue("paisByIdPaisDestino.nmPais", _dadosSessao.pais.nmPais);
		setElementValue("zonaDestino.idZona", _dadosSessao.zona.idZona);
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}

	function findDadosDefault() {
		var service = "lms.vendas.manterPropostasClienteAction.findDescricaoDados";
		var data = {
			servico : {
				idServico : getElementValue("servico.idServico")
			},
			divisaoCliente : {
				idDivisaoCliente : getElementValue("divisaoCliente.idDivisaoCliente")
			}
		}
		var sdo = createServiceDataObject(service, "findDadosDefault", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDadosDefault_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data.servico != undefined) {
			setElementValue("servico.dsServico", data.servico.dsServico);
		}
		if (data.divisaoCliente != undefined) {
			setElementValue("divisaoCliente.dsDivisaoCliente", data.divisaoCliente.dsDivisaoCliente);
		}
	}

	function changeAbasStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != undefined) {
			tabGroup.setDisabledTab("param", status);
			tabGroup.setDisabledTab("tax", status);
			tabGroup.setDisabledTab("gen", status);
		}
	}

	function onClickContinuar() {
		var service = "lms.vendas.manterPropostasClienteAction.continuarParametros";
		var sdo = createServiceDataObject(service, "continuarParametros", buildFormBeanFromForm(document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
	}

	function continuarParametros_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		setMoeda(data.tabelaPreco.moeda.dsMoeda);

		if (validateTabScript(document.forms[0])) {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("param", false);
			tabGroup.selectTab("param", {name:"tab_click"});
		}
	}

	function copiarRotaParametros() {
		populaParametros();

		_copia = true;
		setIdParametroCliente('');
		setParametroCliente('');
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("gen", true);
		tabGroup.setDisabledTab("tax", true);
		setDisabled("btnCopiar", true);
		setDisabled("btnExcluir", true);
	}

	function populaParametros() {
		var idParametroCliente = getElementValue("parametroCliente.idParametroCliente");
		var param = parent.document.frames["param_iframe"];
		param.populaParametros(idParametroCliente);
		top._consulta = false;
	}

	function setIdParametroCliente(idParametroCliente) {
		setElementValue("idParametroCliente",idParametroCliente);
	}

	function setParametroCliente(idParametroCliente) {
		setElementValue("parametroCliente.idParametroCliente", idParametroCliente);
	}

	function setSimulacao(idSimulacao) {
		setElementValue("simulacao.idSimulacao", idSimulacao);
	}

	function setMoeda(dsMoeda) {
		setElementValue("tabelaPreco.moeda.dsMoeda", dsMoeda);
	}

	function setVigenciaInicial(dtVigenciaInicial) {
		setElementValue("simulacao.dtTabelaVigenciaInicial", dtVigenciaInicial);
	}

	function getDadosFromRota() {
		var data = {
			simulacao : {
				idSimulacao : getElementValue("simulacao.idSimulacao"),
				tpGeracaoProposta : getElementValue("simulacao.tpGeracaoProposta"),
				blEfetivada : getElementValue("blEfetivada"),
				filial : {
					ifFilial : getElementValue("filial.idFilial")
				},
				dtTabelaVigenciaInicial : getElementValue("simulacao.dtTabelaVigenciaInicial")
			},
			filialSessao : {
				idFilial : _dadosSessao.filial.idFilial
			},
			disableAll : _disableAll,
			parametroCliente : {
				idParametroCliente : getElementValue("parametroCliente.idParametroCliente")
			},
			cliente : {
				idCliente : getElementValue("cliente.idCliente"),
				pessoa : {
					nmPessoa : getElementValue("cliente.pessoa.nmPessoa"),
					nrIdentificacao : getElementValue("cliente.pessoa.nrIdentificacao")
				}
			},
			divisaoCliente : {
				idDivisaoCliente : getElementValue("divisaoCliente.idDivisaoCliente"),
				dsDivisaoCliente : getElementValue("divisaoCliente.dsDivisaoCliente")
			},
			tabelaDivisaoCliente : {
				tabelaPreco : {
					tipoTabelaPreco : {
						tpTipoTabelaPreco : getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco")
					},
					subtipoTabelaPreco : {
						idSubtipoTabelaPreco : getElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco")
					}
				}
			},
			servico : {
				idServico : getElementValue("servico.idServico"),
				dsServico : getElementValue("servico.dsServico")
			},
			tabelaPreco : {
				idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco"),
				tabelaPrecoString : getElementValue("tabelaPreco.tabelaPrecoString"),
				dsDescricao : getElementValue("tabelaPreco.dsDescricao"),
				moeda : {
					dsMoeda : getElementValue("tabelaPreco.moeda.dsMoeda")
				}
			},
			rotaPreco : {
				origemString : montaRota("Origem"),
				destinoString : montaRota("Destino")
			},
			municipioByIdMunicipioOrigem : {
				idMunicipio : getElementValue("municipioByIdMunicipioOrigem.idMunicipio")
			},
			municipioByIdMunicipioDestino : {
				idMunicipio : getElementValue("municipioByIdMunicipioDestino.idMunicipio")
			},
			filialByIdFilialOrigem : {
				idFilial : getElementValue("filialByIdFilialOrigem.idFilial")
			},
			filialByIdFilialDestino : {
				idFilial : getElementValue("filialByIdFilialDestino.idFilial")
			},
			zonaByIdZonaOrigem : {
				idZona : getElementValue("zonaOrigem.idZona")
			},
			zonaByIdZonaDestino : {
				idZona : getElementValue("zonaDestino.idZona")
			},
			paisByIdPaisOrigem : {
				idPais : getElementValue("paisByIdPaisOrigem.idPais")
			},
			paisByIdPaisDestino : {
				idPais : getElementValue("paisByIdPaisDestino.idPais")
			},
			tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem : {
				idTipoLocalizacaoMunicipio : getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio")
			},
			tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino : {
				idTipoLocalizacaoMunicipio : getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio")
			},
			grupoRegiaoOrigem : {
				idGrupoRegiaoOrigem : getElementValue("grupoRegiaoOrigem.idGrupoRegiao")
			},
			grupoRegiaoDestino : {
				idGrupoRegiaoDestino : getElementValue("grupoRegiaoDestino.idGrupoRegiao")
			},
			unidadeFederativaByIdUfOrigem : {
				idUnidadeFederativa : getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa")
			},
			unidadeFederativaByIdUfDestino : {
				idUnidadeFederativa : getElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa")
			},
			aeroportoByIdAeroportoOrigem : {
				idAeroporto : getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto")
			},
			aeroportoByIdAeroportoDestino : {
				idAeroporto : getElementValue("aeroportoByIdAeroportoDestino.idAeroporto")
			}
		}

		return data;
	}

	function montaRota(tipo) {
		var rota = criaRotaString(tipo);

		if(rota != "") {
			var i = rota.lastIndexOf(" - ");
			rota = rota.substring(0, i);
		}

		return rota;
	}

	function criaRotaString(tipo) {
		var rota = "";
		var value = "";

		var dsCombo = "";
		var combo = getElement("zona"+tipo+".idZona");
		if((combo.options.length > 0) && (combo.selectedIndex > 0) ) {
			dsCombo = combo.options[combo.selectedIndex].text;
		}
		value = dsCombo;
		if(value!="") {
			rota += value+" - ";
		}

		value = getElementValue("paisByIdPais"+tipo+".nmPais");
		if(value!="") {
			rota += value+" - ";
		}

		dsCombo = "";
		combo = getElement("unidadeFederativaByIdUf"+tipo+".idUnidadeFederativa");
		if((combo.options.length > 0) && (combo.selectedIndex > 0) ) {
			dsCombo = combo.options[combo.selectedIndex].text;
		}
		value = dsCombo;
		if(value!="") {
			rota += value+" - ";
		}

		value = getElementValue("filialByIdFilial"+tipo+".sgFilial");
		if(value!="") {
			rota += value+" - ";
		}

		value = getElementValue("municipioByIdMunicipio"+tipo+".municipio.nmMunicipio");
		if(value!="") {
			rota += value+" - ";
		}

		value = getElementValue("aeroportoByIdAeroporto"+tipo+".sgAeroporto");
		if(value!="") {
			rota += value+" - ";
		}

		dsCombo = "";
		combo = getElement("tipoLocalizacaoMunicipioByIdTipoLocalizacao"+tipo+".idTipoLocalizacaoMunicipio");
		if((combo.options.length > 0) && (combo.selectedIndex > 0) ) {
			dsCombo = combo.options[combo.selectedIndex].text;
		}

		value = dsCombo;
		if(value!="") {
			rota += value+" - ";
		}

		dsCombo = "";
		combo = getElement("grupoRegiao"+tipo+".idGrupoRegiao");
		if((combo.options.length > 0) && (combo.selectedIndex > 0) ) {
			dsCombo = combo.options[combo.selectedIndex].text;
		}

		value = dsCombo;
		if(value!="") {
			rota += value+" - ";
		}

		return rota;
	}

	/***************************************/
	/* ON CLICK PICKER TABELA PRECO LOOKUP */
	/***************************************/
	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	/*****************************************************************************/
	/*               COPY&PASTE FROM MANTER ROTAS - BEGIN                        */
	/*****************************************************************************/

	/***********************/
	/* ONCHANGE ZONA COMBO */
	/***********************/
	function changeZona(tipo) {
		setElementValue("paisByIdPais" + tipo + ".idPais", "");
		setElementValue("paisByIdPais" + tipo + ".nmPais", "");
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", "");

		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("_idUf" + tipo, "");
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
	}

	/************************/
	/* ONCHANGE PAIS LOOKUP */
	/************************/
	function changePais(tipo) {
		var idZona = getElementValue("zona" + tipo + ".idZona");
		var r = true;
		eval("r = paisByIdPais" + tipo + "_nmPaisOnChangeHandler();");
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", "");
		setElementValue("_idUf" + tipo, "");
		changeUF(tipo);
		setElementValue("zona" + tipo + ".idZona", idZona);
		return r;
	}


	/****************************/
	/* CALLBACK UF ORIGEM COMBO */
	/****************************/
	function ufOrigemOnDataLoad_cb(dados, erro){
		unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfOrigem");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", idUf);
			setElementValue("_idUfOrigem", "");
		}
	}

	/*************************************/
	/* CALLBACK AEROPORTO DESTINO LOOKUP */
	/*************************************/
	function aeroportoDestino_cb(data) {
		if (data!=undefined && data.length == 0) {
			alert(i18NLabel.getLabel('LMS-30017'));
			return;
		}
		lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoDestino.idAeroporto"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
		resetFilial("Destino");
		resetMunicipio("Destino");
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio", "");
	}

	/*****************************/
	/* CALLBACK UF DESTINO COMBO */
	/*****************************/
	function ufDestinoOnDataLoad_cb(dados, erro){
		unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfDestino");
		if(idUf != null && idUf != "") {
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", idUf);
			setElementValue("_idUfDestino", "");
		}
	}

	/*****************************/
	/* ONCHANGE AEROPORTO LOOKUP */
	/*****************************/
	function changeAeroporto(tipo){
		var r = true;
		if (getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto") != "") {
			eval("r = aeroportoByIdAeroporto" + tipo + "_sgAeroportoOnChangeHandler()");
		} else {
			resetAeroporto(tipo);
		}
		return r;
	}

	/********************************************/
	/* ONPOPUPSETVALUE AEROPORTO DESTINO LOOKUP */
	/********************************************/
	function changeAeroportoDestinoPopup(data){
		changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Destino");
	}

	/*******************************************/
	/* ONPOPUPSETVALUE AEROPORTO ORIGEM LOOKUP */
	/*******************************************/
	function changeAeroportoOrigemPopup(data){
		changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Origem");
	}

	/************************************/
	/* CALLBACK AEROPORTO ORIGEM LOOKUP */
	/************************************/
	function aeroportoOrigem_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel('LMS-30017'));
			return;
		}
		lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoOrigem.idAeroporto"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
		resetFilial("Origem");
		resetMunicipio("Origem");
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", "");
	}


	/*****************************************************************************/
	/*    ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO ORIGEM         */
	/*****************************************************************************/

	/**************************/
	/* ONCHANGE FILIAL LOOKUP */
	/**************************/
	function changeFilial(tipo){
		var r = true;
		if (getElementValue("filialByIdFilial" + tipo + ".sgFilial") == "") {
			resetFilial(tipo);
		} else {
			eval("r = filialByIdFilial" + tipo + "_sgFilialOnChangeHandler()");
		}
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
		return r;
	}

	/****************************************/
	/* ONPOPUPSETVALUE FILIAL ORIGEM LOOKUP */
	/****************************************/
	function changeFilialOrigemPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Origem");
	}

	/*********************************/
	/* CALLBACK FILIAL ORIGEM LOOKUP */
	/*********************************/
	function filialOrigem_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel('LMS-30017'));
			return;
		}
		lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	}


	/*****************************/
	/* ONCHANGE MUNICIPIO LOOKUP */
	/*****************************/
	function municipioChange(tipo) {
		var r = true;
		if (getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio") != ""){
			eval("r = municipioByIdMunicipio"+ tipo + "_municipio_nmMunicipioOnChangeHandler()");
		} else {
			resetMunicipio(tipo);
		}
		return r;
	}

	/*******************************************/
	/* ONPOPUPSETVALUE MUNICIPIO ORIGEM LOOKUP */
	/*******************************************/
	function MunicipioOrigem_PopupSetValue(dados) {
		configEndereco(dados, "Origem");
		eventMunicipio("Origem");
	}

	/************************************/
	/* CALLBACK MUNICIPIO ORIGEM LOOKUP */
	/************************************/
	function municipioOrigem_cb(data)
	{
		lookupExactMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeEndMatch'});
		if (data != undefined && data.length == 1) 	{
			eventMunicipio("Origem");
		}
	}

	function municipioOrigemLikeEndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Origem");
		}
	}

	/*****************************************************************************/
	/*    ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO DESTINO        */
	/*****************************************************************************/

	/**********************************/
	/* CALLBACK FILIAL DESTINO LOOKUP */
	/**********************************/
	function filialDestino_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel('LMS-30017'));
			return;
		}
		lookupExactMatch({e:document.getElementById("filialByIdFilialDestino.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}

	/*****************************************/
	/* ONPOPUPSETVALUE FILIAL DESTINO LOOKUP */
	/*****************************************/
	function changeFilialDestinoPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Destino");
	}

	/********************************************/
	/* ONPOPUPSETVALUE MUNICIPIO DESTINO LOOKUP */
	/********************************************/
	function MunicipioDestino_PopupSetValue(dados) {
		configEndereco(dados, "Destino");
		eventMunicipio("Destino");
	}

	/*************************************/
	/* CALLBACK MUNICIPIO DESTINO LOOKUP */
	/*************************************/
	function municipioDestino_cb(data) {
		lookupExactMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data, callBack:'municipioDestinoLikeEndMatch'});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Destino");
		}
	}

	function municipioDestinoLikeEndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Destino");
		}
	}

	/**********************/
	/* FUNCOES AUXILIARES */
	/**********************/
	function eventMunicipio(tipo){
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}

	function changeAeroportoPopup(idPessoa, tipo) {
		if (idPessoa != "") {
			findEndereco(idPessoa, tipo);
		}
		resetFilial(tipo);
		resetMunicipio(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}

	function changeFilialPopup(idFilial, tipo) {
		if (idFilial != "") {
			findEndereco(idFilial, tipo);
		}
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}

	function configEndereco(dados, tipo) {
		var idFilialUF = getNestedBeanPropertyValue(dados, "filial.pessoa.enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa");
		setNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa", idFilialUF);
		var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
		var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
		var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
		setElementValue("_idUf" + tipo, idFilialUF);
		setElementValue("paisByIdPais" + tipo + ".idPais", idPais);
		setElementValue("paisByIdPais" + tipo + ".nmPais", nmPais);
		setElementValue("zona" + tipo + ".idZona", idZona);
	}

	function findEndereco(idPessoa, tipo) {
		var sdo = createServiceDataObject("lms.vendas.visualizarHistoricoNegociacoesClientesAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
		xmit({serviceDataObjects:[sdo]});
	}

	function resetMunicipio(tipo) {
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio", "");
	}

	function resetFilial(tipo) {
		setElementValue("filialByIdFilial" + tipo + ".idFilial", "");
		setElementValue("filialByIdFilial" + tipo + ".pessoa.nmFantasia", "");
		setElementValue("filialByIdFilial" + tipo + ".sgFilial", "");
	}

	function resetAeroporto(tipo) {
		setElementValue("aeroportoByIdAeroporto" + tipo + ".idAeroporto", "");
		setElementValue("aeroportoByIdAeroporto" + tipo + ".pessoa.nmPessoa", "");
		setElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto", "");
	}

	/*****************************************************************************/
	/*               COPY&PASTE FROM MANTER ROTAS - END                          */
	/*****************************************************************************/
	//-->
</script>