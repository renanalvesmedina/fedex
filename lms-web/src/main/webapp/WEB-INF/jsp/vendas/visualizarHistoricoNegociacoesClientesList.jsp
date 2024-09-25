<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.vendas.visualizarHistoricoNegociacoesClientesAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30017"/>
	</adsm:i18nLabels>

	<adsm:form action="vendas/visualizarHistoricoNegociacoesClientes" height="140">
		<adsm:hidden property="_idUfOrigem" serializable="false"/>
		<adsm:hidden property="_idUfDestino" serializable="false"/>
		<adsm:hidden property="_idDivisaoCliente" serializable="false" />
		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio" />
		<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>

		<%-- CLIENTE LOOKUP --%>
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			size="18"
			maxLength="18"
			width="45%"
			required="true"
			labelWidth="16%"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" disabled="true" serializable="false" property="cliente.pessoa.nmPessoa" size="30" />
		</adsm:lookup>

		<%-- DIVISAO COMBO --%>
		<adsm:combobox
			label="divisao"
			property="divisaoCliente.idDivisaoCliente"
			optionProperty="idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findDivisaoCombo"
			width="29%"
			labelWidth="10%"
			boxWidth="140"
			onDataLoadCallBack="divisaoClienteDataLoad"
		>
			<adsm:propertyMapping modelProperty="cliente.idCliente" criteriaProperty="cliente.idCliente"/>
		</adsm:combobox>

		<%-- TABELA LOOKUP --%>		
		<adsm:lookup label="tabela" labelWidth="16%"
			property="tabelaPreco" idProperty="idTabelaPreco"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupTabelaPreco" 
			action="/tabelaPrecos/manterTabelasPreco" 
			criteriaProperty="tabelaPrecoString" 
			onclickPicker="onclickPickerLookupTabelaPreco()"
			dataType="text" 			
			size="8"
			maxLength="7"	
			width="40%">
		   			
			<adsm:propertyMapping modelProperty="dsDescricao" relatedProperty="tabelaPreco.dsDescricao"/>
		   	
            <adsm:textbox dataType="text" property="tabelaPreco.dsDescricao" 
            	size="30" maxLength="30" disabled="true"/>
            
        </adsm:lookup>

		<%-- COPY&PASTE FROM MANTER ROTAS - BEGIN --%>

		<adsm:section caption="origem"/>

		<%-- ZONA ORIGEM COMBO --%>
		<adsm:combobox
			label="zona"
			property="zonaOrigem.idZona"
			optionProperty="idZona"
			optionLabelProperty="dsZona"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findZona"
			width="19%"
			onlyActiveValues="true"
			onchange="changeZona('Origem')"
			boxWidth="130"
			labelWidth="16%"
		/>

		<%-- PAIS ORIGEM LOOKUP --%>
		<adsm:lookup
			label="pais"
			property="paisByIdPaisOrigem"
			idProperty="idPais"
			criteriaProperty="nmPais"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupPais"
			action="/municipios/manterPaises"
			minLengthForAutoPopUpSearch="3"
			exactMatch="false"
			onchange="return changePais('Origem');"
			dataType="text"
			labelWidth="6%"
			width="25%"
			size="25"
			maxLength="60"
		>
			<adsm:propertyMapping criteriaProperty="zonaOrigem.idZona" modelProperty="zona.idZona" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="zonaOrigem.idZona" modelProperty="zona.idZona"/>
		</adsm:lookup>

		<%-- UF ORIGEM COMBO --%>
		<adsm:combobox
			label="uf"
			property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			optionLabelProperty="siglaDescricao"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findUnidadeFederativaByPais"
			onchange="return changeUF('Origem');"
			boxWidth="150"
			onDataLoadCallBack="ufOrigemOnDataLoad"
			labelWidth="5%"
			width="29%"
		>
			<adsm:propertyMapping criteriaProperty="paisByIdPaisOrigem.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>

		<%-- FILIAL ORIGEM LOOKUP --%>
		<adsm:lookup
			label="filial"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onDataLoadCallBack="filialOrigem"
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"
			onchange="return changeFilial('Origem');"
			dataType="text"
			size="5"
			onPopupSetValue="changeFilialOrigemPopup"
			maxLength="3"
			width="37%"
			labelWidth="16%"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<!-- Seta dados do Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
			<!-- Seta dados da Zona automaticamente -->
			<adsm:propertyMapping relatedProperty="zonaOrigem.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />
			<%-- Seta o hidden para carregar a UF relacionada --%>
			<adsm:propertyMapping relatedProperty="_idUfOrigem" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:textbox dataType="text" serializable="false"
				property="filialByIdFilialOrigem.pessoa.nmFantasia"
				size="30" maxLength="30" disabled="true"/>
		</adsm:lookup>

		<%-- MUNICIPIO ORIGEM LOOKUP --%>
		<adsm:lookup property="municipioByIdMunicipioOrigem" label="municipio"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupMunicipioFilial"
			action="/municipios/manterMunicipiosAtendidos" serializable="false"
			idProperty="municipio.idMunicipio" minLengthForAutoPopUpSearch="2"
			criteriaProperty="municipio.nmMunicipio" size="30" labelWidth="10%"
			onchange="return municipioChange('Origem');" dataType="text"
			onDataLoadCallBack="municipioOrigem" maxLength="60" width="37%"
			onPopupSetValue="MunicipioOrigem_PopupSetValue" exactMatch="false"
		>
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialByIdFilialOrigem.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialByIdFilialOrigem.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>

			<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="filialByIdFilialOrigem.idFilial"/>
			<adsm:propertyMapping modelProperty="municipio.idMunicipio" relatedProperty="municipioByIdMunicipioOrigem.idMunicipio"/>
			<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="filialByIdFilialOrigem.sgFilial"/>
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<!-- Seta dados do Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
			<!-- Seta dados da Zona automaticamente -->
			<adsm:propertyMapping relatedProperty="zonaOrigem.idZona" modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping relatedProperty="_idUfOrigem" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
		</adsm:lookup>

		<%-- AEROPORTO ORIGEM LOOKUP --%>
		<adsm:lookup 
			idProperty="idAeroporto" 
			label="aeroporto" 
			dataType="text" 
			labelWidth="16%"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			criteriaProperty="sgAeroporto"
			property="aeroportoByIdAeroportoOrigem" 
			size="5" 
			maxLength="3"
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"
			onchange="return changeAeroporto('Origem');"
			onDataLoadCallBack="aeroportoOrigem" width="80%"
			onPopupSetValue="changeAeroportoOrigemPopup">

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping relatedProperty="zonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" />
				
			<adsm:textbox dataType="text" serializable="false" size="30"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				maxLength="30" disabled="true" />
		</adsm:lookup>
		
		<%-- TIPO LOCALIZACAO ORIGEM COMBO --%>
		<adsm:combobox optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao" labelWidth="16%" width="84%" boxWidth="200"
			onchange="return changeTpLocalizacao('Origem');"
			onlyActiveValues="true"
		/>

		<adsm:section caption="destino" />

		<%-- ZONA DESTINO COMBO --%>
		<adsm:combobox 
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findZona" 
			property="zonaDestino.idZona" optionLabelProperty="dsZona" 
			optionProperty="idZona" label="zona" onchange="changeZona('Destino')"
			onlyActiveValues="true" labelWidth="16%" width="19%" boxWidth="130" />

		<%-- PAIS DESTINO LOOKUP --%>
		<adsm:lookup dataType="text" labelWidth="6%" width="25%"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupPais" 
			action="/municipios/manterPaises" property="paisByIdPaisDestino" 
			idProperty="idPais" criteriaProperty="nmPais" label="pais"
			minLengthForAutoPopUpSearch="3" exactMatch="false"
			onchange="return changePais('Destino');" size="25" maxLength="60">
			
			<adsm:propertyMapping criteriaProperty="zonaDestino.idZona"
				modelProperty="zona.idZona" addChangeListener="false" />
			<adsm:propertyMapping relatedProperty="zonaDestino.idZona" 
				modelProperty="zona.idZona" />
		</adsm:lookup>

		<%-- UF DESTINO COMBO --%>
		<adsm:combobox labelWidth="5%" width="29%" boxWidth="150" label="uf" 
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			onDataLoadCallBack="ufDestinoOnDataLoad"
			onchange="return changeUF('Destino');">

			<adsm:propertyMapping criteriaProperty="paisByIdPaisDestino.idPais" 
				modelProperty="pais.idPais"/>
		</adsm:combobox>

		<%-- FILIAL DESTINO LOOKUP --%>
		<adsm:lookup minLengthForAutoPopUpSearch="3" label="filial" width="37%"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupFilial" 
			action="/municipios/manterFiliais" property="filialByIdFilialDestino" 
			idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false"
			onDataLoadCallBack="filialDestino" maxLength="3" labelWidth="16%"
			onchange="return changeFilial('Destino');" dataType="text" size="5"
			onPopupSetValue="changeFilialDestinoPopup">

			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<%-- Seta o Nome Pais automaticamente --%>
			<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
			<%-- Seta o ID Pais automaticamente --%>
			<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
			<%-- Seta a Zona automaticamente --%>
			<adsm:propertyMapping relatedProperty="zonaDestino.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona"/>
			<%-- Seta o hidden para carregar a UF relacionada --%>
			<adsm:propertyMapping relatedProperty="_idUfDestino" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa"/>

			<adsm:textbox 
				dataType="text"
				property="filialByIdFilialDestino.pessoa.nmFantasia"
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
		</adsm:lookup>

		<%-- MUNICIPIO DESTINO LOOKUP --%>
		<adsm:lookup 
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" maxLength="60"
			property="municipioByIdMunicipioDestino" label="municipio" 
			idProperty="municipio.idMunicipio" serializable="false" size="30"
			criteriaProperty="municipio.nmMunicipio" exactMatch="false"
			minLengthForAutoPopUpSearch="2" width="37%" labelWidth="10%"
			onchange="return municipioChange('Destino');"
			onDataLoadCallBack="municipioDestino" dataType="text"
			onPopupSetValue="MunicipioDestino_PopupSetValue">

			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" 
				modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" 
				modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
				modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" 
				modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioDestino.idMunicipio" 
				modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" 
				modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
				modelProperty="filial.pessoa.nmFantasia"/>
				
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" />
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping relatedProperty="zonaDestino.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping relatedProperty="_idUfDestino" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
		</adsm:lookup>

		<%-- AEROPORTO DESTINO LOOKUP --%>
		<adsm:lookup 
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			property="aeroportoByIdAeroportoDestino" idProperty="idAeroporto" 
			criteriaProperty="sgAeroporto" onDataLoadCallBack="aeroportoDestino"
			dataType="text" onPopupSetValue="changeAeroportoDestinoPopup"
			label="aeroporto" onchange="return changeAeroporto('Destino');"
			size="5" maxLength="3" width="34%" labelWidth="16%" >
			
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping relatedProperty="zonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />

			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" />

			<adsm:textbox dataType="text" serializable="false"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				size="30" maxLength="30" disabled="true" />
		</adsm:lookup>

		<%-- TIPO LOCALIZACAO DESTINO COMBO --%>
		<adsm:combobox labelWidth="16%" width="84%" boxWidth="200"
			service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" label="tipoLocalizacao" 
			onchange="return changeTpLocalizacao('Destino');"
			onlyActiveValues="true" />

		<%-- COPY&PASTE FROM MANTER ROTAS - END --%>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametrosCliente" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idParametroCliente" gridHeight="125" 
		property="parametrosCliente" unique="true" scrollBars="horizontal" 
		rows="6" onRowClick="rowClick();" selectionMode="none" 
		service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findPaginated"
		rowCountService="lms.vendas.visualizarHistoricoNegociacoesClientesAction.getRowCount">
	
		<adsm:gridColumn title="tabela" property="tabelaPrecoString" width="80" />
		<adsm:gridColumn title="divisao" property="dsDivisaoCliente" width="120" />
		<adsm:gridColumn title="servico" property="dsServico" width="230" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="atualizacaoAutomatica" property="blAtualizacaoAutomatica" width="100" renderMode="image-check"/>

		<adsm:gridColumn title="zonaOrigem" property="zonaByIdZonaOrigem.dsZona" width="150" />
		<adsm:gridColumn title="paisOrigem" property="paisByIdPaisOrigem.nmPais" width="150" />
		<adsm:gridColumn title="ufOrigem2" property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialOrigem" property="filialByIdFilialOrigem.sgFilial" width="50" />
		<adsm:gridColumn title="municipioOrigem" property="municipioByIdMunicipioOrigem.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoOrigem" property="aeroportoByIdAeroportoOrigem.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoOrigem" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" width="100" />
		
		<adsm:gridColumn title="zonaDestino" property="zonaByIdZonaDestino.dsZona" width="150" />
		<adsm:gridColumn title="paisDestino" property="paisByIdPaisDestino.nmPais" width="150" />
		<adsm:gridColumn title="ufDestino2" property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="50" />
		<adsm:gridColumn title="municipioDestino" property="municipioByIdMunicipioDestino.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoDestino" property="aeroportoByIdAeroportoDestino.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoDestino" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" width="100" />
		<adsm:buttonBar />
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--
document.getElementById("cliente.pessoa.nrIdentificacao").serializable="false";

var _origem;

function changeTabStatus(status) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("cad", status);
	tabGroup.setDisabledTab("gen", status);
	tabGroup.setDisabledTab("tax", status);
}

function rowClick() {
	// habilita as abas
	changeTabStatus(false);
}

function initWindow(event) {
	if(event.name == "tab_load" || event.name == "tab_click") {
		// desabilita as abas
		changeTabStatus(true);
		if(_origem != "") {
			_origem = "";
		}
	}
}

function myOnPageLoad_cb() {
	onPageLoad_cb();

	var idDivisaoCliente = getElementValue("_idDivisaoCliente");
	var idPaisOrigem = getElementValue("paisByIdPaisOrigem.idPais");
	var idPaisDestino = getElementValue("paisByIdPaisOrigem.idPais");
	var idUfOrigem = getElementValue("_idUfOrigem");
	var idUfDestino = getElementValue("_idUfDestino");

	if(idDivisaoCliente != null && idDivisaoCliente != "") {
		notifyElementListeners({e:document.getElementById("cliente.idCliente")});
	}

	if(idUfOrigem != "" || idPaisOrigem != "") {
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	}

	if(idUfDestino != "" || idPaisDestino != "") {
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}

	setDisabled("filialByIdFilialOrigem.pessoa.nmFantasia", true);
	setDisabled("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", true);
	setDisabled("filialByIdFilialDestino.pessoa.nmFantasia", true);
	setDisabled("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", true);
}

function setOrigem(origem) {
	_origem = origem;
}

/* CALLBACK DIVISAO COMBO */
function divisaoClienteDataLoad_cb(dados, erro) {
	divisaoCliente_idDivisaoCliente_cb(dados);
	var idDivisaoCliente = getElementValue("_idDivisaoCliente");
	if (idDivisaoCliente != null && idDivisaoCliente != "") {
		setElementValue("divisaoCliente.idDivisaoCliente", idDivisaoCliente);
		setElementValue("_idDivisaoCliente", "");
	}
}

/* COPY&PASTE FROM MANTER ROTAS - BEGIN */

/* ONCHANGE ZONA COMBO */
function changeZona(tipo) {
	if(_origem == "") {
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
}

/* ONCHANGE PAIS LOOKUP */
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

/* ONCHANGE UF COMBO */
function changeUF(tipo) {
	if(_origem == "") {
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}
}

/* CALLBACK UF ORIGEM COMBO */
function ufOrigemOnDataLoad_cb(dados, erro){
	unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(dados);
	var idUf = getElementValue("_idUfOrigem");
	if (idUf != null && idUf != ""){
		setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", idUf);
		setElementValue("_idUfOrigem", "");
	}
}

/* CALLBACK AEROPORTO DESTINO LOOKUP */
function aeroportoDestino_cb(data) {
	if (data!=undefined && data.length == 0){
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoDestino.idAeroporto"), data:data });
	notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	resetFilial("Destino");
	resetMunicipio("Destino");
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio", "");
}

/* CALLBACK UF DESTINO COMBO */
function ufDestinoOnDataLoad_cb(dados, erro){
	unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(dados);
	var idUf = getElementValue("_idUfDestino");
	if(idUf != null && idUf != "") {
		setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", idUf);
		setElementValue("_idUfDestino", "");
	}
}

/* ONCHANGE AEROPORTO LOOKUP */
function changeAeroporto(tipo){
	var r = true;
	if (getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto") != "") {
		eval("r = aeroportoByIdAeroporto" + tipo + "_sgAeroportoOnChangeHandler()");
	} else {
		resetAeroporto(tipo);
	}
	return r;
}

/* ONPOPUPSETVALUE AEROPORTO DESTINO LOOKUP */
function changeAeroportoDestinoPopup(data){
	changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Destino");
}

/* ONPOPUPSETVALUE AEROPORTO ORIGEM LOOKUP */
function changeAeroportoOrigemPopup(data){
	changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Origem");
}

/* CALLBACK AEROPORTO ORIGEM LOOKUP */
function aeroportoOrigem_cb(data) {
	if (data!=undefined && data.length == 0){
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoOrigem.idAeroporto"), data:data });
	notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	resetFilial("Origem");
	resetMunicipio("Origem");
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", "");
}

/* ONCHANGE TIPO LOCALIZACAO COMBO */
function changeTpLocalizacao(tipo) {
	if(_origem == "") {
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
	}
}

/* ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO ORIGEM */

/* ONCHANGE FILIAL LOOKUP */
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

/* ONPOPUPSETVALUE FILIAL ORIGEM LOOKUP */
function changeFilialOrigemPopup(data){
	changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Origem");
}

/* CALLBACK FILIAL ORIGEM LOOKUP */
function filialOrigem_cb(data) {
	if (data!=undefined && data.length == 0){
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data });
	notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
}


/* ONCHANGE MUNICIPIO LOOKUP */
function municipioChange(tipo) {
	var r = true;
	if (getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio") != ""){
		eval("r = municipioByIdMunicipio"+ tipo + "_municipio_nmMunicipioOnChangeHandler()");
	} else {
		resetMunicipio(tipo);
	}
	return r;
}

/* ONPOPUPSETVALUE MUNICIPIO ORIGEM LOOKUP */
function MunicipioOrigem_PopupSetValue(dados) {
	configEndereco(dados, "Origem");
	eventMunicipio("Origem");
}

/* CALLBACK MUNICIPIO ORIGEM LOOKUP */
function municipioOrigem_cb(data) 
{
	lookupExactMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeEndMatch'});
	if (data != undefined && data.length == 1) {
		eventMunicipio("Origem");
	}
}

function municipioOrigemLikeEndMatch_cb(data){
	lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data});
	if (data != undefined && data.length == 1) {
		eventMunicipio("Origem");
	}
}

/* ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO DESTINO */

/* CALLBACK FILIAL DESTINO LOOKUP */
function filialDestino_cb(data) {
	if (data!=undefined && data.length == 0){
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("filialByIdFilialDestino.idFilial"), data:data });
	notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
}

/* ONPOPUPSETVALUE FILIAL DESTINO LOOKUP */
function changeFilialDestinoPopup(data){
	changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Destino");		
}

/* ONPOPUPSETVALUE MUNICIPIO DESTINO LOOKUP */
function MunicipioDestino_PopupSetValue(dados) {
	configEndereco(dados, "Destino");
	eventMunicipio("Destino");
}

/* CALLBACK MUNICIPIO DESTINO LOOKUP */
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

/* FUNCOES AUXILIARES */
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
	var uf = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
	var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
	var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
	var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
	setElementValue("_idUf" + tipo, uf);
	setElementValue("paisByIdPais" + tipo + ".idPais", idPais);
	setElementValue("paisByIdPais" + tipo + ".nmPais", nmPais);
	setElementValue("zona" + tipo + ".idZona", idZona);
}

function findEndereco(idPessoa, tipo) {
	var sdo = createServiceDataObject("lms.vendas.visualizarHistoricoNegociacoesClientesAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
	xmit({serviceDataObjects:[sdo]});
}

function enderecoOrigem_cb(dados, erros) {
	configEndereco(dados, "Origem");
	notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
}

function enderecoDestino_cb(dados, erros) {
	configEndereco(dados, "Destino");
	notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
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

/* COPY&PASTE FROM MANTER ROTAS - END */

function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString","");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

	if(getElementValue("tabelaPreco.tabelaPrecoString") == "" && tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
	}
}
//-->
</script>