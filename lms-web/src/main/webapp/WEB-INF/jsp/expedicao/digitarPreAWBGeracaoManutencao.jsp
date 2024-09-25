<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreAWBAction">
	<adsm:form 
		action="/expedicao/digitarPreAWB"
		height="390" 
		idProperty="idAwb"
		onDataLoadCallBack="formOnDataLoadCallBack">

		<adsm:i18nLabels>
			<adsm:include key="LMS-04161"/>
			<adsm:include key="LMS-04100"/>
			<adsm:include key="LMS-04101"/>
			<adsm:include key="LMS-04162"/>
			<adsm:include key="LMS-04442"/>
			<adsm:include key="LMS-04512"/>
			<adsm:include key="expedidor"/>
			<adsm:include key="destinatario"/>
			<adsm:include key="requiredField"/>
		</adsm:i18nLabels>

		<adsm:hidden property="clienteByIdClienteRemetente.ie.id" serializable="false"/>
		<adsm:hidden property="clienteByIdClienteRemetente.endereco.idMunicipio"/>
		<adsm:hidden property="clienteByIdClienteRemetente.endereco.idUnidadeFederativa"/>
		
		<adsm:hidden property="clienteByIdClienteTomador.ie.id" serializable="false"/>
		<adsm:hidden property="clienteByIdClienteTomador.endereco.idMunicipio"/>
		<adsm:hidden property="clienteByIdClienteTomador.endereco.idUnidadeFederativa"/>

		<adsm:hidden property="tarifaSpot.idTarifaSpot" />
		<adsm:hidden property="aeroportoByIdAeroportoEscala.idAeroporto" />
		<adsm:hidden property="aeroportoByIdAeroportoEscala.sgAeroporto" />
		<adsm:hidden property="aeroportoByIdAeroportoEscala.pessoa.nmPessoa" />

		<adsm:hidden property="statusAtivo" value="A"/>
		<adsm:hidden property="tpStatusAwb"/>
		<adsm:hidden property="idApoliceSeguro"/>

		<adsm:textbox 
			dataType="text" 
			label="numeroPreAWB" 
			property="awb.idAwb" 
			disabled="true" 
			size="13" 
			maxLength="10" 
			labelWidth="14%" 
			width="36%" 
			required="true"
			serializable="true" />

		<adsm:textbox 
			dataType="JTDateTimeZone" 
			label="dataHoraDigitacao" 
			property="dhDigitacao" 
			disabled="true" 
			labelWidth="21%"
			width="29%" 
			picker="false"
			required="true"/>

		<%-- DADOS DO EXPEDIDOR --%>
		<adsm:section caption="dadosExpedidor" />

		<adsm:lookup
			label="expedidor"
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarPreAWBAction.findDadosRemetente" 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			size="20"
			maxLength="20"
			onchange="return remetenteChange();"
			width="44%"
			labelWidth="11%"
			allowInvalidCriteriaValue="true"
			required="true"
			onDataLoadCallBack="remetente"
			onPopupSetValue="remetentePopup">
			<adsm:propertyMapping 
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteRemetente.nrIdentificacao"/>
			<adsm:hidden
				property="clienteByIdClienteRemetente.nrIdentificacao"
				serializable="false"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="30"
				maxLength="50" 
				serializable="false"
				disabled="true"/>
		</adsm:lookup>

		<td colspan="11">
			<adsm:button 
				style="FmLbSection" 
				id="clienteByIdClienteRemetenteButton" 
				caption="incluir" 
				onclick="WindowUtils.showCadastroCliente('clienteByIdClienteRemetente', 'remetente');" 
				disabled="true"/>
		</td>
		
		<adsm:combobox
			label="ie"
			property="clienteByIdClienteRemetente.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="16%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>

		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteRemetente.endereco.dsEndereco"
			label="endereco"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteRemetente.endereco.nrEndereco"
			label="numero"
			size="5" 
			maxLength="5"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteRemetente.endereco.dsComplemento"
			label="complemento"
			size="21"
			maxLength="60"
			labelWidth="10%"
			width="21%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteRemetente.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			property="clienteByIdClienteRemetente.endereco.sgUnidadeFederativa"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="8"
			property="clienteByIdClienteRemetente.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="21%"
			size="12"
			disabled="true"/>

		<%-- DADOS DO DESTINATARIO --%>
		<adsm:section caption="dadosDestinatario"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.endereco.idMunicipio"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.endereco.idUnidadeFederativa"/>

		<adsm:lookup
			label="destinatario"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarPreAWBAction.findDadosDestinatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			allowInvalidCriteriaValue="true"
			onchange="return destinatarioChange();"
			onDataLoadCallBack="destinatario"
			size="20"
			maxLength="20"
			onPopupSetValue="destinatarioPopup"
			required="true"
			width="44%"
			labelWidth="11%"
		>
			<adsm:propertyMapping
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteDestinatario.nrIdentificacao"/>
			<adsm:hidden
				property="clienteByIdClienteDestinatario.nrIdentificacao"
				serializable="false"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="30"
				maxLength="50"
				serializable="false"
				disabled="true"/>
		</adsm:lookup>

		<td colspan="11">
			<adsm:button
				style="FmLbSection"
				id="clienteByIdClienteDestinatarioButton"
				caption="incluir"
				onclick="WindowUtils.showCadastroCliente('clienteByIdClienteDestinatario', 'destinatario');"
				disabled="true"/>
		</td>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteDestinatario.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="16%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>

		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteDestinatario.endereco.dsEndereco"
			label="endereco" 
			maxLength="100"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteDestinatario.endereco.nrEndereco"
			label="numero"
			size="5" 
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteDestinatario.endereco.dsComplemento"
			label="complemento"
			size="21" 
			maxLength="40"
			labelWidth="10%"
			width="21%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteDestinatario.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false" />

		<adsm:textbox
			property="clienteByIdClienteDestinatario.endereco.sgUnidadeFederativa"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="12"
			property="clienteByIdClienteDestinatario.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="21%"
			size="12"
			disabled="true"/>

		<%-- DADOS AWB --%>
		<adsm:section caption="dadosAWB"/>

		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarPreAWBAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeOrigem"
			onDataLoadCallBack="onDataLoadCallBackAeroportoOrigem"
			onchange="return onChangeAeroporto('Origem');"
			size="3"
			required="true"
			maxLength="3"
			labelWidth="17%"
			width="33%">

			<adsm:propertyMapping 
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao" />

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="30"
				maxLength="45"
				disabled="true"/>

		</adsm:lookup>

		<adsm:lookup action="/municipios/manterAeroportos" 
			service="lms.expedicao.digitarPreAWBAction.findAeroporto" 
			dataType="text"
			required="true"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto" 
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			onDataLoadCallBack="onDataLoadCallBackAeroportoDestino"
			onchange="return onChangeAeroporto('Destino');"
			size="3"
			maxLength="3" 
			labelWidth="17%"
			width="33%">

			<adsm:hidden
				property="aeroportoByIdAeroportoDestino.filial.idFilial"/>
			<adsm:propertyMapping 
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="idFilial"
				relatedProperty="aeroportoByIdAeroportoDestino.filial.idFilial"/>
			<adsm:textbox 
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="30"
				maxLength="45"
				disabled="true"/>
		</adsm:lookup>

		<adsm:combobox 
			label="embalagem" 
			property="embalagem.idEmbalagem" 
			optionLabelProperty="dsEmbalagem" 
			optionProperty="idEmbalagem" 
			service="lms.expedicao.digitarPreAWBAction.findEmbalagem" 
			onlyActiveValues="true"
			required="true" 
			labelWidth="17%" 
			width="33%"
			boxWidth="228"/>

		<adsm:combobox 
			label="naturezaProduto" 
			property="naturezaProduto.idNaturezaProduto" 
			optionLabelProperty="dsNaturezaProduto" 
			optionProperty="idNaturezaProduto" 
			service="lms.expedicao.digitarPreAWBAction.findNaturezaProduto" 
			onlyActiveValues="true"
			required="true" 
			labelWidth="17%" 
			width="32%"
			boxWidth="230"/>

		<adsm:combobox 
			label="produtoEspecifico" 
			property="produtoEspecifico.idProdutoEspecifico" 
			optionLabelProperty="nmTarifaEspecifica" 
			optionProperty="idProdutoEspecifico" 
			service="lms.expedicao.digitarPreAWBAction.findProdutoEspecifico" 
			onlyActiveValues="true"
			labelWidth="17%"
			width="33%"
			boxWidth="228"/>

		<adsm:textbox 
			dataType="integer" 
			property="tarifaSpot.dsSenha" 
			onchange="onChangeTarifaSpot();"
			label="codigoTarifaSpot" 
			labelWidth="17%" 
			width="32%" 
			size="10" 
			maxLength="8" />

		<adsm:textbox 
			dataType="integer" 
			property="qtVolumes" 
			label="qtdeVolumes" 
			labelWidth="17%"
			width="20%" 
			size="7" 
			required="true" 
			maxLength="5" 
			minValue="1"/>

		<adsm:textbox 
			label="pesoReal"
			property="psReal"
			dataType="weight"
			unit="kg"
			labelWidth="14%"
			width="18%"
			size="11"
			maxLength="11"
			required="true"
			minValue="0,001"/>

		<adsm:textbox
			label="pesoCubado"
			property="psCubado"
			dataType="weight"
			unit="kg"
			labelWidth="17%"
			width="14%"
			size="10"
			maxLength="11"
			minValue="0,001"/>

		<adsm:combobox 
			label="tipoFrete" 
			property="tpFrete"
			domain="DM_TIPO_FRETE"
			width="20%" 
			labelWidth="17%"/>
			
		<adsm:textarea label="observacao" property="obAwb" maxLength="500" rows="4" columns="116" 
					   labelWidth="17%" width="86%" />

		<%-- DADOS DO VOO --%>
		<adsm:section caption="dadosVoo"/>	

		<adsm:textbox 
			dataType="text"
			property="ciaFilialMercurio.empresa.pessoa.nmPessoa" 
			label="ciaAerea" 
			size="25" 
			disabled="true" 
			labelWidth="17%" 
			width="33%"
			/>

		<adsm:textbox 
			dataType="currency" 
			property="vlFrete" 
			label="valorFreteReais" 
			size="12" 
			disabled="true" 
			labelWidth="20%" 
			width="30%"/>

		<adsm:textbox 
			dataType="text" 
			property="dsVooPrevisto" 
			label="numeroVooPrevisto" 
			size="10" 
			disabled="true" 
			labelWidth="17%" 
			width="33%"/>

		<adsm:textbox 
			dataType="JTDateTimeZone" 
			property="dhPrevistaSaida" 
			label="dataHoraDeSaida" 
			disabled="true" 
			picker="false"
			labelWidth="20%" 
			width="30%"/>

		<adsm:textbox
			dataType="text"
			property="aeroportoByIdAeroportoEscala.siglaDescricao"
			label="via"
			disabled="true"
			labelWidth="17%"
			width="33%"
			size="25"/>

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhPrevistaChegada"
			label="dataHoraDeChegada"
			disabled="true"
			picker="false"
			labelWidth="20%"
			width="30%"/>

		<%-- DADOS DO SEGURO --%>
		<adsm:section caption="dadosSeguro"/>
		
		<adsm:textbox 
			dataType="text"
			property="nrLvSeguro"
			label="numeroLvSeguro" 
			size="12" 
			disabled="true" 
			labelWidth="10%" 
			width="15%"/>
		
		<adsm:textbox 
			dataType="text"
			property="dsSeguradora"
			label="seguradora" 
			size="15" 
			disabled="true" 
			labelWidth="10%" 
			width="15%"/>

		<adsm:textbox 
			dataType="text" 
			property="dsApolice" 
			label="apolice" 
			size="10" 
			disabled="true" 
			labelWidth="8%" 
			width="15%"/>

		<adsm:textbox 
			dataType="text"  
			property="dsResponsavel" 
			label="responsavel" 
			size="15" 
			disabled="true" 
			labelWidth="10%" 
			width="15%"/>

		<%-- DADOS DO TOMADOR DO SERVICO --%>
		<adsm:section caption="dadosTomadorServico" />
		<adsm:hidden property="clienteByIdClienteTomador.endereco.idMunicipio"/>
		<adsm:hidden property="clienteByIdClienteTomador.endereco.idUnidadeFederativa"/>

		<adsm:lookup
			label="tomador"
			property="clienteByIdClienteTomador"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarPreAWBAction.findDadosTomador" 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			size="20"
			maxLength="20"
			onchange="return tomadorChange();"
			width="44%"
			labelWidth="11%"
			allowInvalidCriteriaValue="true"
			disabled="true"

			onDataLoadCallBack="tomador"
			onPopupSetValue="tomadorPopup">
			<adsm:propertyMapping 
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteTomador.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteTomador.nrIdentificacao"/>
			<adsm:hidden
				property="clienteByIdClienteTomador.nrIdentificacao"
				serializable="false"/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteTomador.pessoa.nmPessoa"
				size="30"
				maxLength="50" 
				serializable="false"
				disabled="true"/>
		</adsm:lookup>

		<td colspan="11">
			<adsm:button 
				style="FmLbSection" 
				id="clienteByIdClienteTomadorButton" 
				caption="incluir" 
				onclick="WindowUtils.showCadastroCliente('clienteByIdClienteTomador', 'tomador');" 
				disabled="false"/>
		</td>
		
		<adsm:combobox
			label="ie"
			property="clienteByIdClienteTomador.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			boxWidth="110"
			labelWidth="10%"
			width="16%"
			disabled="true"
			autoLoad="false">
		</adsm:combobox>

		<adsm:textbox
			dataType="text"
			size="40"
			property="clienteByIdClienteTomador.endereco.dsEndereco"
			label="endereco"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteTomador.endereco.nrEndereco"
			label="numero"
			size="5" 
			maxLength="5"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="clienteByIdClienteTomador.endereco.dsComplemento"
			label="complemento"
			size="21"
			maxLength="60"
			labelWidth="10%"
			width="21%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="50"
			property="clienteByIdClienteTomador.endereco.nmMunicipio" 
			label="municipio"
			width="30%"
			labelWidth="11%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			property="clienteByIdClienteTomador.endereco.sgUnidadeFederativa"
			label="uf"
			size="5" 
			dataType="text"
			labelWidth="10%"
			width="15%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			maxLength="8"
			property="clienteByIdClienteTomador.endereco.nrCep" 
			label="cep"
			labelWidth="10%"
			width="21%"
			size="12"
			disabled="true"/>
			
		<adsm:label key="espacoBranco" width="56%" />
			
		<adsm:textbox
			dataType="text"
			maxLength="8"
			property="nrCcTomadorServico" 
			label="contaCiaAerea"
			labelWidth="20%"
			width="15%"
			size="12" 
			disabled="true" />	

		<adsm:buttonBar freeLayout="false">
			<adsm:button
				id="botao.emitirMinuta"
				caption="emitirMinuta" 
				buttonType="reportViewerButton"
				disabled="true"
				onclick="imprimeRelatorio();" />
				
			<adsm:button
				id="botao.incluirExcluirCTRC"
				caption="incluirExcluirCTRC"
				disabled="false"
				onclick="WindowUtils.showIncluirExcluirCTRC();"/>
			<adsm:button
				id="botao.dimensoes"
				caption="dimensoes"
				disabled="false"
				onclick="digitarDimensoes();"/>
			<adsm:storeButton
				caption="calcularFrete"
				service="lms.expedicao.digitarPreAWBAction.calcularAwb"
				callbackProperty="validateAwb"
				disabled="true"
				id="calcularAwbButton"/>
			
			<adsm:newButton
				caption="cancelar"/>
				<adsm:resetButton id="limpar" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script src="../lib/expedicao.js" type="text/javascript"></script>
<script src="../lib/digitarDadosCTRC.js" type="text/javascript"></script>
<script type="text/javascript">
var tpDocumento = "AWB";

//Armazena uma referencia para o aeroporto da filial do usuario logado
var _aeroportoFilialLogado;
//Armazena informacoes para saber quem remeteu informacoes para esta tela.
var _origem;
//Armazena uma referencia para o endereco do usuario logado.
var _expedidorFilialLogado;
//Armazena uma referencia para o endereco do usuario logado. - tomador do serviço
var _tomadorFilialLogado;

//Sera utilizada pela tela de consolidacao
//de cargas para passagem de dados via javascript
function saveDataFromConsolidacaoCargas(data) {
	setAcumuladores(data);
	if(data.aeroporto.idAeroporto != "") {
		setElementValue("aeroportoByIdAeroportoDestino.idAeroporto", data.aeroporto.idAeroporto);
		setElementValue("aeroportoByIdAeroportoDestino.sgAeroporto", data.aeroporto.sgAeroporto);
		setElementValue("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", data.aeroporto.pessoa.nmPessoa);
	}
	setOrigem("consolidacao");
}

function initWindow(eventObj) {
	var event = eventObj.name;
	if(event == "newButton_click" || event == "cleanButton_click") {
		novoPreAwb();
	}

	setDisabled("botao.incluirExcluirCTRC", false);
	setDisabled("botao.dimensoes", false);
	setDisabled("calcularAwbButton", false);
	setDisabled("botao.emitirMinuta", false);
	

	getElement("awb.idAwb").required = 'false';
	getElement("dhDigitacao").required = 'false';
	getElement("clienteByIdClienteDestinatario.idCliente").required = 'true';
	getElement("clienteByIdClienteDestinatario.pessoa.nrIdentificacao").required = 'false';
	getElement("clienteByIdClienteRemetente.idCliente").required = 'true';
	getElement("clienteByIdClienteRemetente.pessoa.nrIdentificacao").required = 'false';
	// getElement("clienteByIdClienteTomador.idCliente").required = 'true';
	getElement("clienteByIdClienteTomador.pessoa.nrIdentificacao").required = 'false';

	doOrigemRules();
}

function getTomador(){
	//INSERIR VALIDAÇÃO PARA QUANDO FOR CONSOLIDAÇÃO DE CARGA 
	var sdo = createServiceDataObject("lms.expedicao.digitarPreAWBAction.verificaCNPJFilialUsuarioLogado", "getTomador");
	xmit({serviceDataObjects:[sdo]});
}

function validateTab() {
	setLabelRequiredFields();

	var idClienteRementente = getElementValue("clienteByIdClienteRemetente.idCliente");
	if(idClienteRementente == "") {
		alertRequiredField("clienteByIdClienteRemetente.idCliente");
		setFocus("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
		return false;
	}
	var idClienteDestinatario = getElementValue("clienteByIdClienteDestinatario.idCliente");
	if(idClienteDestinatario == "") {
		alertRequiredField("clienteByIdClienteDestinatario.idCliente");
		setFocus("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		return false;
	}

	return validateTabScript(document.forms);
}

function setLabelRequiredFields() {
	getElement("clienteByIdClienteRemetente.idCliente").label = getI18nMessage("expedidor");
	getElement("clienteByIdClienteDestinatario.idCliente").label = getI18nMessage("destinatario");
}

function novoPreAwb() {
	setOrigem("");
	changeConsolidacaoCargasTabStatus(false);
	clearIes();
	setFocus("clienteByIdClienteRemetente.pessoa.nrIdentificacao", false);
	
	var service = "lms.expedicao.digitarPreAWBAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
}

function onChangeTarifaSpot() {
	var dsSenha = getElementValue("tarifaSpot.dsSenha");
	if(dsSenha != "") {
		var data = {
			dsSenha : dsSenha,
			aeroportoOrigem : {
				idAeroporto : getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto")
			},
			aeroportoDestino : {
				idAeroporto : getElementValue("aeroportoByIdAeroportoDestino.idAeroporto")
			}
		}
		
		var service = "lms.expedicao.digitarPreAWBAction.verificaTarifaSpot";
		var sdo = createServiceDataObject(service, "onChangeTarifaSpot", data);
		xmit({serviceDataObjects:[sdo]});
	} else {
		setElementValue("tarifaSpot.idTarifaSpot", "");
	}
}

function onChangeTarifaSpot_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	if(data != undefined) {
		if(data.error == "true") {
			setElementValue("tarifaSpot.dsSenha", "");
			alert(i18NLabel.getLabel('LMS-04161'));
		} else {
			setElementValue("tarifaSpot.idTarifaSpot", data.idTarifaSpot);
		}
	}
}

function onDataLoadCallBackAeroportoOrigem_cb(data) {
	aeroportoByIdAeroportoOrigem_sgAeroporto_exactMatch_cb(data);
	updateTarifaSpotStatus();
}

function onDataLoadCallBackAeroportoDestino_cb(data) {
	aeroportoByIdAeroportoDestino_sgAeroporto_exactMatch_cb(data)
	updateTarifaSpotStatus();
}

function onChangeAeroporto(tipo) {
	if (tipo == "Origem") {
		aeroportoByIdAeroportoOrigem_sgAeroportoOnChangeHandler();
	} else if (tipo == "Destino") {
		aeroportoByIdAeroportoDestino_sgAeroportoOnChangeHandler();
	}
	
	var sgAeroporto = getElementValue("aeroportoByIdAeroporto"+tipo+".sgAeroporto");
	if (sgAeroporto == "" || sgAeroporto == null) {
		updateTarifaSpotStatus();
		return true;
	}
	return false;
}

function getAcumuladores_cb(data, error) {
	if(error != undefined) {
		alert(error);
	}
	if(data != undefined) {
		setAcumuladores(data);
	}
}

function validateAwb_cb(data, error) {
	var erro = null;
	if (error != undefined) {
		erro = error.substr(0, 9);
		if (erro == 'LMS-04442') {
			alert("LMS-04442 - " + i18NLabel.getLabel("LMS-04442"));
			setDisabled(getElement("nrLvSeguro"), false);
			setFocus(getElement("nrLvSeguro"), true);
			return;
		}
		alert(error);
		return;
	}
	
	var calculoResult = WindowUtils.showCalculoFreteAereo();
	findDadosCliente(getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao"), "Tomador");
	return calculoResult;
}

function onClickEmitir() {
	/* No caso de Reemissao */
	if (getElementValue("tpStatusAwb") == "E" && !window.confirm(i18NLabel.getLabel("LMS-04162"))) {
		return;
	}
	var service = "lms.expedicao.digitarPreAWBAction.emitirAWB";
	var sdo = createServiceDataObject(service, "onClickEmitir", {idAwb: getElementValue("awb.idAwb")});
	xmit({serviceDataObjects:[sdo]});
}

function onClickEmitir_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	//Verifica se houve Exceção no envio do e-mail do Pre-Alerta para AWBs Emitidos
	if (data.exception != undefined) {
		alert(data.exception);
	}
	//Atualiza Status
	setElementValue("tpStatusAwb", data.tpStatusAwb);
	//Imprime Awb
	printAWB(data.awb);
}

//Envia documento para impressora
function printAWB(data) {
	if((data == undefined) || (data == "")) {
		alert(i18NLabel.getLabel("LMS-04100"));
		return;
	}

	var printer = window.top[0].document.getElementById("printer");
	printer.print(data);
	alertI18nMessage("LMS-04101", "1", false);
}

function digitarDimensoes() {
	if(openDimensoes() == true) {
		executeCalculoDimensoes();
	}
}

function executeCalculoDimensoes() {
	var data = {psCubado : getElementValue("psCubado")};
	var service = "lms.expedicao.digitarPreAWBDimensoesAction.executeCalculoPesoCubado";
	var sdo = createServiceDataObject(service, "executeCalculoPesoCubado", data);
	xmit({serviceDataObjects:[sdo]});
}

function executeCalculoPesoCubado_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	setElementValue("psCubado", setFormat("psCubado", data.psCubado));
}

function formOnDataLoadCallBack_cb(data, error) {
	if(!error) {
		addOption("clienteByIdClienteRemetente.idInscricaoEstadual", data.clienteByIdClienteRemetente.idInscricaoEstadual, data.clienteByIdClienteRemetente.nrInscricaoEstadual);
		addOption("clienteByIdClienteTomador.idInscricaoEstadual", data.clienteByIdClienteTomador.idInscricaoEstadual, data.clienteByIdClienteTomador.nrInscricaoEstadual);
		addOption("clienteByIdClienteDestinatario.idInscricaoEstadual", data.clienteByIdClienteDestinatario.idInscricaoEstadual, data.clienteByIdClienteDestinatario.nrInscricaoEstadual);
	}
	onDataLoad_cb(data, error);
	
	if(_origem == "list") {
		setDisabled("clienteByIdClienteRemetenteButton", true);
		setDisabled("clienteByIdClienteTomadorButton", true);
		setDisabled("clienteByIdClienteDestinatarioButton", true);

		setDisabled("clienteByIdClienteRemetente.pessoa.nrIdentificacao", true);
		setDisabled("clienteByIdClienteTomador.pessoa.nrIdentificacao", true);
		setDisabled("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", true);		
		setDadosSeguroPorAwb(data);
	}
	
	setOrigem();
	updateTarifaSpotStatus();
	
}

function changeFields() {
	setDisabled("nrCcTomadorServico", false);
}

function addOption(controlId, value, text, status) {
	var control = document.getElementById(controlId);
	if (control == null) {
		alert(getMessage(listbox_elementNotFound, new Array(controlId)));
		return;
	}

	var opt = new Option(text, value);
	control.options.add(opt);
}

///onHide tab geracao manutencao
function hide() {
	tab_onHide();
	return true;
}

function changeConsolidacaoCargasTabStatus(status) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("consolidacaoCargas", status);
}

function setOrigem(valor) {
	_origem = valor;
}

function changeStatusBotaoCalcularFrete(status) {
	setDisabled("calcularAwbButton", status);
}

function setAeroportoFilialLogado() {
	// busca o aeroporto da sessao apenas uma vez, armazenando na variavel,
	// evitando a consulta ao servidor toda a vez que o usuario vier da tela
	// de consolidacao de cargas
	if(!_aeroportoFilialLogado) {
		findAeroportoOrigem();
	} else {
		setAeroporto(_aeroportoFilialLogado, "aeroportoByIdAeroportoOrigem");
	}
}

/**
 * Realiza as funcoes de acordo com quem chamou esta tela.
 * _origem == null || _origem == "" -> Modo inclusao
 * _origem == "list" -> Modo consulta
 * _origem == "consolidacao" -> Tela de consolidacao de cargas
 */
function doOrigemRules() {
	if(_origem == "consolidacao" || _origem == "" || _origem == null) {
		setAeroportoFilialLogado();
		setElementValue("tpFrete", "C");
		setExpedidorFilialLogado();
		
		// setTomadorFilialLoagado();
		setDadosSeguroPorTipoSeguro();
		setResponsavelSeguroAereo();
		setDisabled("nrLvSeguro", false);
	}
	updateTarifaSpotStatus();
	/* Controle de alteração de dados na pagina  */
	var tab = getTab(document);
	tab.setChanged(false);
}
 
function setResponsavelSeguroAereo(){
	 var service = "lms.expedicao.digitarPreAWBAction.findByNomeParametro";
	 var sdo = createServiceDataObject(service, "findByNomeParametro");
	 xmit({serviceDataObjects:[sdo]});
} 

function findByNomeParametro_cb(data, error){
	setElementValue("dsResponsavel", data.dsConteudo);
}

function setDadosSeguroPorTipoSeguro(){
	 var service = "lms.expedicao.digitarPreAWBAction.findSeguroPorTipo";
	 var sdo = createServiceDataObject(service, "findSeguroPorTipo");
	 xmit({serviceDataObjects:[sdo]});
}

function findSeguroPorTipo_cb(data, error){	
	setElementValue("dsSeguradora", data.nmSeguradora);
	setElementValue("dsApolice", data.nrApolice);
	setElementValue("idApoliceSeguro", data.idApoliceSeguro);
}

function setDadosSeguroPorAwb(data){
	var parameters = {dsApolice:data.dsApolice};
	var service = "lms.expedicao.digitarPreAWBAction.findSeguroPorAwb";
	var sdo = createServiceDataObject(service, "findSeguroPorAwb", parameters);
	xmit({serviceDataObjects:[sdo]}); 

}
 
function findSeguroPorAwb_cb(data, error){	
	if(error == undefined) {
		setElementValue("dsSeguradora", data.nmSeguradora);
		setElementValue("dsApolice", data.nrApolice);		
		setElementValue("idApoliceSeguro", data.idApoliceSeguro);
		setResponsavelSeguroAereo();
		setDisabled("nrLvSeguro", true);
	}else{
		alert(error);
	}
}

function setExpedidorFilialLogado() {
	if(_expedidorFilialLogado) {
		remetente_cb(_expedidorFilialLogado);
	} else {
		var service = "lms.expedicao.digitarPreAWBAction.findClienteLogado";
		var sdo = createServiceDataObject(service, "findExpedidorLogado");
		xmit({serviceDataObjects:[sdo]});
	}
}

function setTomadorFilialLoagado() {
	if(_tomadorFilialLogado) {
		tomador_cb(_tomadorFilialLogado);
	} else {
		var service = "lms.expedicao.digitarPreAWBAction.findClienteLogado";
		var sdo = createServiceDataObject(service, "findTomadorLogado");
		xmit({serviceDataObjects:[sdo]});
	}
}

function findExpedidorLogado_cb(data, error) {
	_expedidorFilialLogado = data;
	remetente_cb(_expedidorFilialLogado, true);
	/* Controle de alteração de dados na pagina  */
	var tab = getTab(document);
	tab.setChanged(false);
}

function findTomadorLogado_cb(data, error) {
	_tomadorFilialLogado = data;
	tomador_cb(_tomadorFilialLogado, true);
	/* Controle de alteração de dados na pagina  */
	var tab = getTab(document);
	tab.setChanged(false);
}

function updateTarifaSpotStatus() {
	var idAeroportoOrigem = getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto");
	var idAeroportoDestino = getElementValue("aeroportoByIdAeroportoDestino.idAeroporto");
	if (idAeroportoOrigem != "" && idAeroportoDestino != "") {
		setDisabled("tarifaSpot.dsSenha", false);
	} else {
		setDisabled("tarifaSpot.dsSenha", true);
	}
}

function setAcumuladores(data) {
	var qtVolumesTotal = data.qtVolumesTotal;
	if(parseInt(qtVolumesTotal) <= 0) {
		qtVolumesTotal = "";
	}
	var psRealTotal = data.psRealTotal;
	if(parseFloat(psRealTotal) <= 0.00000) {
		psRealTotal = "";
	}
	var psCubadoTotal = data.psCubadoTotal;
	if(parseFloat(psCubadoTotal) <= 0.00000) {
		psCubadoTotal = "";
	}
	setElementValue("qtVolumes", setFormat("qtVolumes", qtVolumesTotal));
	setElementValue("psReal", setFormat("psReal", psRealTotal));
	setElementValue("psCubado", setFormat("psCubado", psCubadoTotal));
}

function firePopupChangeModel(data, caller) {
	var criteria = {
		pessoa: {
			nrIdentificacao : data
		}
	}
	var service = "lms.expedicao.digitarPreAWBAction.findDados" + caller;
	var sdo = createServiceDataObject(service, "firePopupChange" + caller + "Model", criteria);
	xmit({serviceDataObjects:[sdo]});
}

function tomadorPopup(data) {
	if(data == undefined) {
		resetValue("clienteByIdClienteTomador.idCliente");
		setDisabled("clienteByIdClienteTomadorButton", false);
		clearIeTomador();
		setFocus(getElement("clienteByIdClienteTomadorButton"), false);
	} else {
		//setTimeout("firePopupChangeModel('" + data.pessoa.nrIdentificacao + "', 'Remetente')", 500);
		findDadosCliente(data.pessoa.nrIdentificacao, "Tomador");
		setDisabled("clienteByIdClienteTomadorButton", true);
		var objNotas = document.getElementById("quantidadeNotasFiscais");
	}
	resetValue("clienteByIdClienteTomador.ie.id");
	setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true);

	return true;
}

//Funcoes remetente
function remetentePopup(data) {
	if(data == undefined) {
		resetValue("clienteByIdClienteRemetente.idCliente");
		setDisabled("clienteByIdClienteRemetenteButton", false);
		clearIeRemetente();
		setFocus(getElement("clienteByIdClienteRemetenteButton"), false);
	} else {
		//setTimeout("firePopupChangeModel('" + data.pessoa.nrIdentificacao + "', 'Remetente')", 500);
		findDadosCliente(data.pessoa.nrIdentificacao, "Remetente");
		setDisabled("clienteByIdClienteRemetenteButton", true);
		var objNotas = document.getElementById("quantidadeNotasFiscais");
	}
	resetValue("clienteByIdClienteRemetente.ie.id");
	setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);

	return true;
}

function firePopupChangeRemetenteModel_cb(data){
	if(data != undefined && data.length > 0){
		if(data[0].ie != undefined) {
			setElementValue("clienteByIdClienteRemetente.idInscricaoEstadual", data[0].ie.idInscricaoEstadual);
		}
	}
}

function enderecoRemetente_cb(data, erros) {
	if(erros) {
		alert(erros);
		return false;
	}
	setEndereco(data, "clienteByIdClienteRemetente");
}

function setEndereco(dados, tipo) {
	var nmTarget = tipo + ".endereco";
	if(dados && dados.pessoa.endereco) {
		var objEnd = dados.pessoa.endereco;
		setElementValue(nmTarget + ".nrCep", objEnd.nrCep);
		setElementValue(nmTarget + ".nmMunicipio", objEnd.nmMunicipio);
		setElementValue(nmTarget + ".idMunicipio", objEnd.idMunicipio);
		setElementValue(nmTarget + ".idUnidadeFederativa", objEnd.idUnidadeFederativa);
		setElementValue(nmTarget + ".sgUnidadeFederativa", objEnd.sgUnidadeFederativa);
		setElementValue(nmTarget + ".dsComplemento", objEnd.dsComplemento);
		setElementValue(nmTarget + ".nrEndereco", objEnd.nrEndereco);
		setElementValue(nmTarget + ".dsEndereco", objEnd.dsEndereco);
		if(tipo == "clienteByIdClienteDestinatario") {
			findAeroportoDestino(objEnd.idMunicipio, objEnd.nrCep, dados.idCliente);
		}
	} else {
		setElementValue(nmTarget + ".nrCep", "");
		setElementValue(nmTarget + ".nmMunicipio", "");
		setElementValue(nmTarget + ".idMunicipio", "");
		setElementValue(nmTarget + ".idUnidadeFederativa", "");
		setElementValue(nmTarget + ".sgUnidadeFederativa", "");
		setElementValue(nmTarget + ".dsComplemento", "");
		setElementValue(nmTarget + ".nrEndereco", "");
		setElementValue(nmTarget + ".dsEndereco", "");
		if(tipo == "clienteByIdClienteDestinatario") {
			resetAeroporto(tipo);
		}
	}
}

function resetAeroporto(tipo) {
	if(tipo == "clienteByIdClienteRemetente")
		resetValue("aeroportoByIdAeroportoOrigem.idAeroporto");
	else resetValue("aeroportoByIdAeroportoDestino.idAeroporto");
}

function findAeroportoOrigem(){
	if(getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto") == "") {
		var service = "lms.expedicao.digitarPreAWBAction.findAeroportoFilialSessao";
		var sdo = createServiceDataObject(service, "findAeroportoOrigem");
		xmit({serviceDataObjects:[sdo]});	
	}
}

function findAeroportoOrigem_cb(data, error) {
	_aeroportoFilialLogado = data;
	setAeroporto(data, "aeroportoByIdAeroportoOrigem");
}

function setAeroporto(data, tipo) {
	setElementValue(tipo + ".idAeroporto", data.idAeroporto);
	setElementValue(tipo + ".sgAeroporto", data.sgAeroporto);
	setElementValue(tipo + ".pessoa.nmPessoa", data.nmAeroporto);
	updateTarifaSpotStatus();
}

function tomadorChange() {
	var nrId = getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao");
	if(nrId == "") {
		setEndereco(undefined, "clienteByIdClienteTomador");
		resetValue("clienteByIdClienteTomador.idCliente"); 
		setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true); 
		setIe("Tomador");
	}
	if(nrId == getElementValue("clienteByIdClienteTomador.nrIdentificacao")){
		setDisabled("clienteByIdClienteTomadorButton", true);
		return true;
	}
	return clienteByIdClienteTomador_pessoa_nrIdentificacaoOnChangeHandler();
}

function remetenteChange(){
	var nrId = getElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
	if(nrId == "") {
		setEndereco(undefined, "clienteByIdClienteRemetente");
		resetValue("clienteByIdClienteRemetente.idCliente"); 
		setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true); 
		setIe("Remetente");
	}
	if(nrId == getElementValue("clienteByIdClienteRemetente.nrIdentificacao")){
		setDisabled("clienteByIdClienteRemetenteButton", true);
		return true;
	}
	return clienteByIdClienteRemetente_pessoa_nrIdentificacaoOnChangeHandler();
}

function remetente_cb(data) {
	if(data == undefined || data.length == 0){
		var nrId = getElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
		resetValue("clienteByIdClienteRemetente.idCliente");
		setElementValue("clienteByIdClienteRemetente.pessoa.nrIdentificacao", nrId);
		setDisabled("clienteByIdClienteRemetenteButton", false);
		clearIeRemetente();
		setFocus(document.getElementById("clienteByIdClienteRemetenteButton"), false);
	} else {
		setDisabled("clienteByIdClienteRemetenteButton", true);
		//setInscricaoEstadual("Remetente", data[0].ie.inscricaoEstadual);
		setIe("Remetente", data);
	}
	setEndereco(data[0], "clienteByIdClienteRemetente");
	//setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);
	resetValue("clienteByIdClienteRemetente.ie.id");
	return clienteByIdClienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);
}

function tomador_cb(data) {
	if(data == undefined || data.length == 0){
		var nrId = getElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao");
		resetValue("clienteByIdClienteTomador.idCliente");
		setElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao", nrId);
		setDisabled("clienteByIdClienteTomadorButton", false);
		clearIeTomador();
		setFocus(document.getElementById("clienteByIdClienteTomadorButton"), false);
	} else {
		setDisabled("clienteByIdClienteTomadorButton", true);
		//setInscricaoEstadual("Remetente", data[0].ie.inscricaoEstadual);
		setIe("Tomador", data);
		//setElementValue("nrCcTomadorServico", data[0].nrCcTomadorServico);
	}
	setEndereco(data[0], "clienteByIdClienteTomador");
	
	//setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);
	resetValue("clienteByIdClienteTomador.ie.id");
	return clienteByIdClienteTomador_pessoa_nrIdentificacao_exactMatch_cb(data);
}

//Funcoes destinatario
function destinatarioChange() {
	var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
	if(nrId == "") {
		setEndereco(undefined, "clienteByIdClienteDestinatario");
		resetValue("clienteByIdClienteDestinatario.idCliente");
		setIe("Destinatario");
	}
	if(nrId == getElementValue("clienteByIdClienteDestinatario.nrIdentificacao")){
		setDisabled("clienteByIdClienteDestinatarioButton", true);
		return true;
	}
	//setInscricaoEstadual("Destinatario");
	return clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
}

function destinatarioPopup(data) {
	if(data == undefined){
		var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		resetValue("clienteByIdClienteDestinatario.idCliente");
		setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", nrId);
		setDisabled("clienteByIdClienteDestinatarioButton", false);
		clearIeDestinatario();
		setFocus("clienteByIdClienteDestinatarioButton", true);
	} else {
		setDisabled("clienteByIdClienteDestinatarioButton", true);
		findDadosCliente(data.pessoa.nrIdentificacao, "Destinatario");
	}
	return true;
}

function findDadosCliente(nrIdentificacao, tipo) {
	var sdo = createServiceDataObject("lms.expedicao.digitarPreAWBAction.findDados" + tipo, tipo.toLowerCase(), {pessoa:{nrIdentificacao:nrIdentificacao}});
	xmit({serviceDataObjects:[sdo]});
}

function enderecoDestinatario_cb(data, erros) {
	if(erros) {
		alert(erros);
		return false;
	}
	setEndereco(data, "clienteByIdClienteDestinatario");
}

function findAeroportoDestino(idMunicipio, nrCep, idCliente) {
	var parameters = {idMunicipio:idMunicipio, nrCep:nrCep, idCliente:idCliente};
	var service = "lms.expedicao.digitarPreAWBAction.findAeroportoDestino";
	var sdo = createServiceDataObject(service, "findAeroportoDestino", parameters);
	xmit({serviceDataObjects:[sdo]}); 
}

function findAeroportoDestino_cb(data, error) {
	setAeroporto(data, "aeroportoByIdAeroportoDestino");
}

function destinatario_cb(data) {
	if(data == undefined || data.length == 0){
		var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		resetValue("clienteByIdClienteDestinatario.idCliente");
		setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", nrId);
		setDisabled("clienteByIdClienteDestinatarioButton", false);
		clearIeDestinatario();
		setFocus(document.getElementById("clienteByIdClienteDestinatarioButton"), false);
	} else {
		setDisabled("clienteByIdClienteDestinatarioButton", true);
		setIe("Destinatario", data);
	}
	var r = clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
	setEndereco(data[0], "clienteByIdClienteDestinatario");
//	setInscricaoEstadual("Destinatario", data[0].ie.inscricaoEstadual);
	return r;
}

function setIe(tipo, data) {
	if (data) {
		if (data[0].ie != undefined) {
			eval("clienteByIdCliente"+tipo+"_idInscricaoEstadual_cb(data[0].ie)");
			var idInscricaoEstadualPadrao = getIdInscricaoEstadualPadrao(data[0].ie);
			setElementValue("clienteByIdCliente"+tipo+".idInscricaoEstadual", idInscricaoEstadualPadrao);
			setDisabled("clienteByIdCliente"+tipo+".idInscricaoEstadual", !(data[0].ie.length > 1));
		} else {
			eval("clearIe"+tipo+"()");
		}
	} else {
		eval("clearIe"+tipo+"()");
	}
}

function getIdInscricaoEstadualPadrao(ies) {
	for (var i = 0; i < ies.length; i++) {
		var inscricaoEstadual = ies[i].inscricaoEstadual;
		if (inscricaoEstadual.blIndicadorPadrao == "true") {
			return inscricaoEstadual.idInscricaoEstadual;
		}
	}
	return 0;
}

function clearIes() {
	clearIeRemetente();
	clearIeDestinatario();
	clearIeTomador();
}

function clearIeRemetente() {
	clearElement("clienteByIdClienteRemetente.idInscricaoEstadual");
	setDisabled("clienteByIdClienteRemetente.idInscricaoEstadual", true);
}

function clearIeTomador() {
	clearElement("clienteByIdClienteTomador.idInscricaoEstadual");
	setDisabled("clienteByIdClienteTomador.idInscricaoEstadual", true);
}

function clearIeDestinatario() {	
	clearElement("clienteByIdClienteDestinatario.idInscricaoEstadual");
	setDisabled("clienteByIdClienteDestinatario.idInscricaoEstadual", true);
}

var WindowUtils = {
	Options : {
		unardorned : 'no'
		,scroll : 'auto'
		,resizable : 'no'
		,status : 'no'
		,center : 'yes'
		,help : 'no'
		,dialogWidth : '700px'
		,dialogHeight : '350px'
		,toStr : function(){
			var str = '';
			for(var k in this){
				if(typeof(this[k]) == 'string') str += k + ':' + this[k] + ';';
			}
			return str;
		}
	}
	,showModalParams : function(url, params, options){
		return showModalDialog(url, params, options);
	}
	,showModal : function(url){
		return this.showModalParams(url, window, this.Options.toStr());
	}
	,showModalBySize : function(url, window, width, height){
		var opts = ObjectUtils.extend(this.Options, {});
		opts.dialogWidth = width + 'px';
		opts.dialogHeight = height + 'px';
		return this.showModalParams(url, window, opts.toStr());
	}
	,showIncluirExcluirCTRC : function(){
		var retorno = this.showModal('expedicao/digitarPreAWBCTRCs.do?cmd=main&millis=' + new Date().getTime());
		
		var service = "lms.expedicao.digitarPreAWBCTRCsAction.getAcumuladores";
		var sdo = createServiceDataObject(service, "getAcumuladores");
		xmit({serviceDataObjects:[sdo]});
		
		return retorno;
	}
	,showCadastroCliente : function(tipo, callBack) {
		var data = this.showModalBySize('expedicao/cadastrarClientes.do?cmd=main&origem=exp', window, 750, 300);
		if(data) {
			setDisabled(tipo+"Button", true);
			resetValue(tipo + ".idCliente");
			setElementValue(tipo + ".idCliente", data.idCliente);

			var service = "lms.expedicao.digitarPreAWBAction.findDadosCliente";
			var sdo = createServiceDataObject(service, callBack, {idCliente:data.idCliente});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	,showCalculoFreteAereo : function() {
		return this.showModalBySize('expedicao/digitarPreAWBCalculoFreteAereo.do?cmd=main', window, 665, 330);
	}
}

var ObjectUtils = {
	extend : function(from, to){
		if(!from || !to) return null;
		for(var k in from) to[k] = from[k];
		return to;
	}
	,objToString : function (o){
		if(!o) return '';

		var saida = '';
		var reg = /function (.*?)\(/m;
		var c = reg.test("" + o.constructor) ? RegExp.$1 : '';

		for(var a in o) saida += c + '.' + a + '='+ o[a] + '\n';

		return saida;
	}
}

function imprimeRelatorio() {
	var nrPreAwb = getElementValue("awb.idAwb");
	
	if(nrPreAwb != null && nrPreAwb != '') {
	    executeReportWithCallback('lms.expedicao.digitarPreAWBAction', 'verificaEmissao', document.forms[0]);
	} else {
		alert("LMS-04512 - " + i18NLabel.getLabel("LMS-04512"));
	}
	
}

function verificaEmissao_cb(strFile, error) {
	if (error){
		alert(error);
		return false;
	} 
    openReportWithLocator(strFile._value, error);
}
</script>
