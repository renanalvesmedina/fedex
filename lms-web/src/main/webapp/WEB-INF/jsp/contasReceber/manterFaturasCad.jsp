<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>
	/*
	 * Remove as criteriaProperty da lookup.
	 */
	function myOnPageLoad(){
		onPageLoad();
	}	
			
</script>

<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas" idProperty="idFatura" onDataLoadCallBack="myOnDataLoad" height="340">
	
		<adsm:hidden property="enableGerarDesconto"/>
		<adsm:hidden property="enableGerarFaturaExcel"/>
		<adsm:hidden property="enableImportarDesctoCSV"/>
		<adsm:hidden property="enableAdmButtons"/>
		<adsm:hidden property="enableZerarDesconto"/>
		<adsm:hidden property="disableBlGeraBoleto"/>
		<adsm:hidden property="cliente.tpSituacao" value="A"/>		
		<adsm:hidden property="nrFaturaFormatada"/>				
		<adsm:hidden property="filialByIdFilial.idFilial"/>
		<adsm:hidden property="idProcessoWorkflow"/>		
		<adsm:hidden property="faturaDsConcatenado" serializable="false"/>
		<adsm:hidden property="load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto" serializable="false"/>
		<adsm:hidden property="isDesconto"/>
		<adsm:hidden property="isQuestionamentoFatura"/>	
		<adsm:hidden property="reemitir.enableReemitir"/>

		<adsm:textbox dataType="text" property="filialByIdFilial.sgFilial" size="3" width="30%" label="filialFaturamento" labelWidth="19%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="filialByIdFilial.pessoa.nmFantasia" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>
		
		<adsm:textbox dataType="integer" label="numero" property="nrFatura" size="10" maxLength="10" disabled="true" labelWidth="17%" width="33%"
		mask="0000000000"/>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.manterFaturasAction.findLookupCliente" 
			size="20" 
			labelWidth="19%"
			width="80%"
			onPopupSetValue="popupCliente"
			onDataLoadCallBack="onDataLoadCliente"			
			onchange="return myClienteOnChange();"
			required="true">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
				

			<adsm:propertyMapping 
				criteriaProperty="cliente.tpSituacao" 
				modelProperty="tpSituacao"/>				
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
		</adsm:lookup>
		
		<adsm:hidden property="cliente.filialByIdFilialCobranca.idFilial"/>
		<adsm:textbox dataType="text" property="cliente.filialByIdFilialCobranca.sgFilial" size="3" width="5%" label="filialCobrancaCliente" labelWidth="19%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="cliente.filialByIdFilialCobranca.pessoa.nmFantasia" width="25%" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>	
		
		
		<adsm:hidden property="filialByIdFilialCobradora.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilialCobradora.sgFilial" size="3" width="5%" label="filialCobranca" labelWidth="15%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="filialByIdFilialCobradora.pessoa.nmFantasia" width="28%" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>			

		<adsm:textbox dataType="integer" label="numeroPreFatura" property="nrPreFatura" size="10" maxLength="10" disabled="true"			
			labelWidth="19%"
			width="30%"/>

		<adsm:combobox 
			service="lms.contasreceber.manterFaturasAction.findComboCedentesActive" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			onlyActiveValues="true"
			label="banco"
			boxWidth="180"
			labelWidth="15%"
			width="33%"
			required="true">
			<adsm:propertyMapping 
				criteriaProperty="idFatura" 
				modelProperty="faturas.idFatura"
			/>							 
		</adsm:combobox>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" labelWidth="19%"
			width="30%" required="true"
			onchange="getDataVencimento(); resetComboDivisao(); findComboDivisaoCliente();"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="15%"
			width="33%" required="true" onchange="onChangeTpAbrangencia(); getDataVencimento(); resetComboDivisao(); findComboDivisaoCliente();"/>	
		
		<adsm:hidden property="divisaoCliente.idDivisaoClienteTmp"/>
		<adsm:hidden property="divisaoCliente.idDivisaoClienteInativo"/>

        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.manterFaturasAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente"				
				labelWidth="19%"
				boxWidth="150"
				width="30%"
				autoLoad="false"
				onchange="onChangeComboDivisao();"
				optionProperty="idDivisaoCliente">	
			<adsm:propertyMapping 
				criteriaProperty="cliente.idCliente" 
				modelProperty="cliente.idCliente"
			/>
			<adsm:propertyMapping 
				criteriaProperty="divisaoCliente.idDivisaoClienteTmp" 
				modelProperty="divisaoCliente.idDivisaoCliente"
			/>
			<adsm:propertyMapping 
				criteriaProperty="divisaoCliente.idDivisaoClienteInativo" 
				modelProperty="divisaoCliente.idDivisaoCliente"
			/>
		</adsm:combobox>		
		
		<adsm:hidden property="agrupamentoCliente.idAgrupamentoClienteTmp"/>

		<adsm:combobox property="agrupamentoCliente.idAgrupamentoCliente" 
				label="formaAgrupamento" 
				labelWidth="15%"
				width="33%"
				boxWidth="150"
				autoLoad="false"
				onchange="onChangeComboAgrupamentoCliente();"
				service="lms.contasreceber.manterFaturasAction.findAgrupamentoCliente" 
				optionLabelProperty="formaAgrupamento.dsFormaAgrupamento" 
				optionProperty="idAgrupamentoCliente">			
		</adsm:combobox>
				
		<adsm:hidden property="tipoAgrupamento.idTipoAgrupamentoTmp"/>
				
		<adsm:combobox property="tipoAgrupamento.idTipoAgrupamento" 
			label="tipoAgrupamento" service="lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento" 
			optionLabelProperty="dsTipoAgrupamento" optionProperty="idTipoAgrupamento" labelWidth="19%"
			width="30%"
			autoLoad="false"
			boxWidth="150">
			<adsm:propertyMapping criteriaProperty="agrupamentoCliente.idAgrupamentoCliente" modelProperty="agrupamentoCliente.idAgrupamentoCliente"/>
		</adsm:combobox>

		<adsm:combobox property="tpSituacaoFatura" label="situacaoFatura" domain="DM_STATUS_ROMANEIO"
			labelWidth="15%"
			width="33%"
			boxWidth="150"
			autoLoad="false"
			onchange="disabledButtons(false);"
			required="true">
		</adsm:combobox>
			
		<adsm:textbox label="situacaoAprovacao" 
			width="30%"
			labelWidth="19%"
			dataType="text"
			property="tpSituacaoAprovacao" 
			size="20"
			disabled="true">
			<a href="#" onclick="showHistoricoAprovacaoPendenciaDesconto();"><img alt="Historico" src="../images/popup.gif" border="0" id="img2HistoricoAprovacao"/></a>
		</adsm:textbox>							

		<adsm:textbox label="cotacao" 
			width="7%"
			labelWidth="15%"
			dataType="text"
			property="simboloMoedaPais" 
			size="8"
			serializable="true"
			disabled="true" />							
			
		<adsm:textbox property="dtCotacaoMoeda" 
			dataType="JTDate" 
			style="width:67px;"
			disabled="true" 
			picker="false"
			serializable="true" width="9%"/>
		
		<adsm:lookup property="cotacaoMoeda" idProperty="idCotacaoMoeda" 
			criteriaProperty="vlCotacaoMoeda" 
			action="/configuracoes/manterCotacoesMoedas" dataType="currency"
			onchange="vlCotacaoMoedaOnChange(this);" onPopupSetValue="cotacaoMoedaSetValue"
			service="lms.contasreceber.manterFaturasAction.findLookupCotacaoMoeda" 
			serializable="true" mask="###,###,###,###,##0.0000"
			size="12" width="14%" style="width:67px">
			<adsm:propertyMapping  criteriaProperty="idPaisCotacao" modelProperty="moedaPais.pais.idPais" />
			<adsm:propertyMapping  criteriaProperty="nmPaisCotacao" modelProperty="moedaPais.pais.nmPais" />			
			<adsm:propertyMapping  relatedProperty="dtCotacaoMoeda" modelProperty="dtCotacaoMoeda" />
			<adsm:propertyMapping  relatedProperty="vlCotacaoMoedaTmp" modelProperty="vlCotacaoMoeda" />
			<a href="#" onclick="if (getElementValue('pendencia.idPendencia') != '') { showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendencia.idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;'); }"><img alt="Historico" src="../images/popup.gif" border="0" id="imgHistoricoAprovacao"/></a>
		</adsm:lookup>

		<adsm:hidden property="vlCotacaoMoeda" serializable="true"/>
		<adsm:hidden property="vlCotacaoMoedaTmp" serializable="false"/>	

		<adsm:hidden property="idPaisCotacao" serializable="false"/>
		<adsm:hidden property="nmPaisCotacao" serializable="false"/>

		<adsm:hidden property="servico.idServico" serializable="true"/>		
		<adsm:hidden property="tpFreteValue" serializable="true"/>			
		<adsm:textbox dataType="text" label="tipoFrete" property="tpFreteDescription" size="8" maxLength="3"
			labelWidth="19%"
			width="30%" disabled="true"/>
		<adsm:textbox dataType="text" label="servico" property="servico.dsServico" size="42" maxLength="60"
			labelWidth="15%"
			width="33%" disabled="true"/>						

		<adsm:textbox dataType="JTDate" label="dataEmissao" property="dtEmissao" size="8" maxLength="20"  
			labelWidth="19%" 
			width="30%" onchange="getDataVencimento();"/>
		<adsm:textbox dataType="JTDate" label="dataVencimento" property="dtVencimento" size="8" maxLength="20" required="true" 
			labelWidth="15%"
			width="33%"/>
	   <adsm:combobox property="tpSetorCausadorAbatimento" label="setorCausadorAbatimento" domain="DM_SETOR_CAUSADOR"
			labelWidth="19%" width="30%" boxWidth="210" onchange="onChangeSetorCausadorAbatimento();" />					   			
		<adsm:textarea label="acaoCorretiva" property="obAcaoCorretiva" maxLength="500" columns="42" rows="2" 
					   labelWidth="15%" width="30%" />
		<adsm:combobox label="motivoDesconto" width="30%" labelWidth="19%"
			optionLabelProperty="dsMotivoDesconto"
			optionProperty="idMotivoDesconto" boxWidth="210" disabled="false"
			property="devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto" />		
		
		<adsm:textarea label="observacao" property="obFatura" maxLength="500" columns="42" rows="2" 
					   labelWidth="15%" width="30%" />
		
				
		<adsm:lookup 
			width="30%" 
				property="filialByIdFilialDebitada" 
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				service="lms.contasreceber.manterFaturasAction.findLookupFilialDebitada"
				dataType="text" 
				label="filialDebitada" 
				size="3"
				action="/municipios/manterFiliais"  
				minLengthForAutoPopUpSearch="3" 
				exactMatch="false" 
				maxLength="3"
				labelWidth="19%">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialDebitada.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox 
				dataType="text"
				property="filialByIdFilialDebitada.pessoa.nmFantasia" 
				size="26" 
				disabled="true" />
		</adsm:lookup>
		
		<adsm:textbox label="origemFatura" 
			width="30%"
			labelWidth="15%"
			dataType="text"
			property="tpOrigemDescription" 
			serializable="true"
			disabled="true" />
			
		<adsm:checkbox label="gerarEDI" property="blGerarEdi" labelWidth="19%"
			width="30%"/>
		<adsm:checkbox label="gerarBoleto" property="blGerarBoleto" labelWidth="15%"
			width="33%"/>

		<adsm:section caption="informacoesComplementares" />

		<adsm:hidden property="boleto.idBoleto"/>
		<adsm:textbox labelWidth="19%" dataType="text" label="boleto" property="boleto.nrBoleto" size="13"
		maxLength="13" width="13%" disabled="true"/>
		
		<adsm:hidden property="redeco.idRedeco"/>
		<adsm:hidden property="redeco.filial.idFilial"/>
		<adsm:hidden property="redeco.filial.nmFantasia"/>
		<adsm:complement label="redeco" labelWidth="14%" width="19%" separator="branco">
			<adsm:textbox dataType="text" property="redeco.filial.sgFilial" size="2" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="integer" property="redeco.nrRedeco" size="9" width="9%" disabled="true" maxLength="10" mask="0000000000"/>
		</adsm:complement>
		
        <adsm:textbox labelWidth="13%" dataType="text" label="finalidade" property="redeco.tpFinalidade" size="20" maxLength="60" width="20%" disabled="true"/>		

		<adsm:textbox labelWidth="19%" dataType="JTDate" label="dataLiquidacao" property="dtLiquidacao" size="13" 
		maxLength="20" width="13%" disabled="true" picker="false"/>
		
		<adsm:hidden property="recibo.idRecibo"/>
		<adsm:complement label="recibo" labelWidth="14%" width="19%" separator="branco">
			<adsm:textbox dataType="text" property="recibo.filial.sgFilial" size="2" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="integer" property="recibo.nrRecibo" size="9" width="9%" disabled="true" maxLength="10" mask="0000000000"/>
		</adsm:complement>
		
		
		<adsm:hidden property="relacaoCobranca.idRelacaoCobranca"/>
		<adsm:complement label="relacaoCobranca" labelWidth="13%" width="20%" separator="branco">
			<adsm:textbox dataType="text" property="relacaoCobranca.filial.sgFilial" size="2" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="integer" property="relacaoCobranca.nrRelacaoCobrancaFilial" size="9" width="9%" disabled="true" maxLength="10"
			mask="0000000000"/>
		</adsm:complement>

		<adsm:textbox labelWidth="19%" dataType="JTDate" label="dataTransmissaoEDI" property="dtTransmissaoEdi" size="13" 
			maxLength="20" width="13%" disabled="true" picker="false"/>

		<adsm:textbox labelWidth="14%" dataType="JTDateTimeZone" label="dataHoraReemissao" property="dhReemissao" size="10" maxLength="20" width="19%" disabled="true" picker="false"/>
		
        <adsm:textbox labelWidth="13%" dataType="text" label="usuarioReemissor" property="usuario.nmUsuario" size="20" maxLength="60" width="20%" disabled="true"/>

		<adsm:checkbox label="itensResumo" property="blConhecimentoResumo" labelWidth="19%" width="13%" disabled="true"/>
		
		<adsm:textbox labelWidth="14%" dataType="JTDateTimeZone" label="dataNegativacaoSerasa" property="dhNegativacaoSerasa" size="13" 
			maxLength="20" width="19%" disabled="true" picker="false"/>
			
		<adsm:textbox labelWidth="13%" dataType="JTDateTimeZone" label="dataExclusaoSerasa" property="dhExclusaoSerasa" size="13" 
			maxLength="20" width="20%" disabled="true" picker="false"/>
			
			
		<adsm:textbox labelWidth="19%" dataType="JTDateTimeZone" label="dataEnvioCobTerceira" property="dhEnvioCobTerceira" size="13" 
			maxLength="20" width="13%" disabled="true" picker="false"/>
			
		<adsm:textbox labelWidth="14%" dataType="JTDateTimeZone" label="dataPagtoCobTerceira" property="dhPagtoCobTerceira" size="13" 
			maxLength="20" width="19%" disabled="true" picker="false"/>

		<adsm:textbox labelWidth="13%" dataType="JTDateTimeZone" label="dataDevolCobTerceira" property="dhDevolCobTerceira" size="13" 
			maxLength="20" width="20%" disabled="true" picker="false"/>
			
			
			
			
		<adsm:textbox labelWidth="19%" dataType="JTDate" label="dataPrefatura" property="dhPreFatura" size="13" 
			maxLength="20" width="13%" disabled="true" picker="false"/>
			
			
		<adsm:textbox labelWidth="14%" dataType="JTDate" label="dataImportacao" property="dhImportacao" size="13" 
			maxLength="20" width="19%" disabled="true" picker="false"/>
			
			
		<adsm:textbox labelWidth="13%" dataType="JTDate" label="dataEnvioAceite" property="dhEnvioAceite" size="13" 
			maxLength="20" width="20%" disabled="true" picker="false"/>
			
			
		<adsm:textbox labelWidth="19%" dataType="JTDate" label="dataRetornoAceite" property="dhRetornoAceite" size="13" 
			maxLength="20" width="13%" disabled="true" picker="false"/>
			
			
	
		<adsm:section caption="totaisImpostos" />

		<adsm:textbox labelWidth="19%" dataType="integer" label="qtdeTotalDocumentos" property="qtDocumentos" size="13" 
			maxLength="6" width="13%" disabled="true"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorTotalFrete" property="vlTotal" size="10" maxLength="18" width="48%" disabled="true"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorIVA" property="vlIva" size="13" maxLength="20" width="13%"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorTotalDesconto" property="vlDesconto" size="10" maxLength="18" width="14%" disabled="true"/>

		<adsm:textbox labelWidth="18%" dataType="currency" label="valorJurosCalc" property="vlJuroCalculado" size="10" maxLength="18" width="13%" disabled="true"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorCobrar" property="vlCobrar" size="13" maxLength="18" width="13%" disabled="true"/>
		
		<adsm:textbox labelWidth="19%" dataType="currency" label="valorJurosRecebidos" property="vlJuroRecebido" size="10" maxLength="18" width="14%" disabled="true"/>		

		<adsm:textbox labelWidth="18%" dataType="currency" label="valorTotalRecebido" property="vlTotalRecebido" size="10" maxLength="18" width="13%" disabled="true"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorRecebidoParcial" property="vlRecebidoParcial" size="10" maxLength="18" width="13%" disabled="true"/>
		
		<adsm:textbox labelWidth="19%" dataType="currency" label="valorSaldoDevedor" property="vlSaldoDevedor" size="10" maxLength="18" width="13%" disabled="true"/>

		<adsm:hidden property="blFaturaReemitida"/>
		<adsm:hidden property="blIndicadorImpressao"/>
		<adsm:hidden property="tpFatura" value="R"/>	
		<adsm:hidden property="tpOrigem"/>			
		<adsm:hidden property="moeda.idMoeda"/>		
		<adsm:hidden property="pendencia.idPendencia"/>	
		<adsm:hidden property="pendenciaDesconto.idPendencia"/>	
			
		<adsm:buttonBar lines="3">
			<adsm:button boxWidth="82" id="complemento" caption="complemento" action="/contasReceber/consultarComplementosRomaneios" 
			cmd="main" disabled="true">
				<adsm:linkProperty src="idFatura" target="idFatura"/>
				<adsm:linkProperty src="nrPreFatura" target="numeroPreFatura"/>
				<adsm:linkProperty src="nrFatura" target="nrFatura"/>
				<adsm:linkProperty src="filialByIdFilial.sgFilial" target="sgFilialFatura"/>
				
			</adsm:button>
			
			<adsm:button id="emitir" caption="emitir" disabled="true" onclick="onclick_emitir()"/>
		
			
			<adsm:button id="emitirDactes" caption="emitirDactes" disabled="true" onclick="onclick_emitirDactes()"/>
			<adsm:button boxWidth="40" caption="boleto" id="boletoButton" action="/contasReceber/manterBoletos" cmd="main" disabled="true">
				<adsm:linkProperty src="boleto.nrBoleto" target="nrBoleto"/>
				<adsm:linkProperty src="filialByIdFilialCobradora.idFilial" target="fatura.filialByIdFilialCobranca.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialCobradora.sgFilial" target="fatura.filialByIdFilialCobranca.sgFilial"/>
				<adsm:linkProperty src="filialByIdFilialCobradora.pessoa.nmFantasia" target="fatura.filialByIdFilialCobranca.pessoa.nmFantasia"/>			
			</adsm:button>
			<adsm:button boxWidth="70" caption="relCob" id="relacaoCobrancaButton" action="/contasReceber/pesquisarRelacoesCobranca" 
			disabled="true" cmd="main">
				<adsm:linkProperty src="relacaoCobranca.nrRelacaoCobrancaFilial" target="nrRelacaoCobrancaFilial"/>
				<adsm:linkProperty src="filialByIdFilial.sgFilial" target="filial.sgFilial"/>
				<adsm:linkProperty src="filialByIdFilial.idFilial" target="filial.idFilial"/>
				<adsm:linkProperty src="filialByIdFilial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia"/>
			</adsm:button>
			<adsm:button boxWidth="45" caption="redeco" id="redecoButton" action="/contasReceber/manterRedeco" cmd="main" disabled="true">
				<adsm:linkProperty src="redeco.nrRedeco" target="nrRedeco"/>			
				<adsm:linkProperty src="redeco.filial.sgFilial" target="filial.sgFilial"/>
				<adsm:linkProperty src="redeco.filial.idFilial" target="filial.idFilial"/>
				<adsm:linkProperty src="redeco.filial.nmFantasia" target="filial.pessoa.nmFantasia"/>
				
			</adsm:button>
			<adsm:button boxWidth="42" caption="recibo" id="recibo" onclick="return onClickRecibo(this.form);" disabled="true">
				<adsm:linkProperty src="recibo.nrRecibo" target="nrRecibo"/>			
			</adsm:button>
			<adsm:button caption="zerarDesconto" id="btnZerarDesconto" disabled="true" boxWidth="95" onclick="return zerarDescontoFatura();" />
			<adsm:button id="btnGerarDescontoTotalDevedor" caption="gerarDescontoFaturasAutomatico" onclick="return gerarDescontoTotalDevedorAutomatico();" disabled="true"/>
			<adsm:button id="btnGerarFaturaExcel" caption="gerarFaturaExcel" onclick="onclick_imprimeFaturas()"/>			
			<adsm:button caption="importarDesctoCsv"  id="btnImportarDesctoCSV"  disabled="true" onclick="importarCSVPopup();"/>
			<adsm:button caption="recebimentosParciais"  id="btnRecebimentosParciais"  disabled="true" onclick="recebimentosParciais();" />
            <adsm:button boxWidth="50" id="cancelar" caption="cancelar" onclick="return validateCancelaBloqueioFaturamento();" disabled="true"/>
			<adsm:button caption="enviarPorEmail" id="emailFatura" disabled="true" onclick="return sendByEmail();" />
			<adsm:button caption="reenviaredi" id="reemitirFaturaIntegracao" disabled="true" onclick="return emitirFaturaIntegracaoCliente()"/>
			<adsm:button caption="mensagens" id="mensagensPopup" disabled="true" onclick="openMensagensPopup();" />
			<adsm:button caption="pgtoLP" id="pgtoLPPopup" disabled="true" onclick="openPgtoLPPopup();" />
			<adsm:button caption="tratativasBTN" id="tratativaButton" onclick="openTratativaPopup();"/>
			<adsm:button caption="recebimentos" id="recebimentos" onclick="openRecebimentosPopup();" />
			<adsm:button caption="salvar" id="storeButton" onclick="return myStore();" buttonType="storeButton"/>
			<adsm:newButton id="newButton" onclick="disabledButtons();" />
			<adsm:removeButton id="removeButton" disabled="true"/>
		</adsm:buttonBar>
	<adsm:i18nLabels>
		<adsm:include key="LMS-36070"/>
		<adsm:include key="LMS-36283"/>
		<adsm:include key="LMS-36284"/>
		<adsm:include key="LMS-36291"/>
		<adsm:include key="observacao"/>
		<adsm:include key="LMS-36206"/>
		<adsm:include key="fluxoAprovacaoCotacao"/>
		<adsm:include key="fluxoAprovacao"/>
		<adsm:include key="LMS-36423"/>
	</adsm:i18nLabels>
	</adsm:form>

</adsm:window>
<script>

	function emitirFaturaIntegracaoCliente() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "cnpj", getElementValue("cliente.pessoa.nrIdentificacao"));
		setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
		var sdo = createServiceDataObject('lms.contasreceber.emitirFaturasNacionaisAction.enviaFaturaClienteIntegracao', '', data);
		xmit({serviceDataObjects:[sdo]});
		alert(i18NLabel.getLabel("LMS-36423")+'');	}

	function handleDisableReemitirFatura(enable) {
			setDisabled("reemitirFaturaIntegracao", enable);
	}

	function importarCSVPopup() {
		var url = '/contasReceber/manterFaturas.do?cmd=importcsv&idFatura=' + getElementValue("idFatura");
		showModalDialog(url, window, 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:630px;dialogHeight:320px;');
		updateCamposFatura();
	}
	
	function openRecebimentosPopup(){
		var url = '/contasReceber/manterFaturas.do?cmd=recebimentosPopup&idFatura=' + getElementValue("idFatura");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:530px;');
	}
	
	function updateCamposFatura() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
		
		var sdo = createServiceDataObject('lms.contasreceber.manterFaturasAction.updateCamposFatura', 'updateCamposFatura', data);
	 	xmit({serviceDataObjects:[sdo]});	
	}

	function updateCamposFatura_cb(data, errors){
		if (errors != undefined) {
			alert(errors + '');
			return;
		}
		
		if(data.vlDesconto != undefined){
			setElementValue("vlDesconto", setFormat(getElement("vlDesconto"), data.vlDesconto));
		}
		
		if(data.vlSaldoDevedor != undefined){
			setElementValue("vlSaldoDevedor", setFormat(getElement("vlSaldoDevedor"), data.vlSaldoDevedor));
		}
	}
	
	function onclick_imprimeFaturas() {
		executeReportWithCallback('lms.contasreceber.manterFaturasAction.execute', 'verificaEmissao', document.forms[0]);

	}

	function verificaEmissao_cb(strFile, error) {
		reportUrl = contextRoot+"/viewBatchReport?open=false&"+strFile._value;
		alert(i18NLabel.getLabel("LMS-36291"));
		location.href(reportUrl);
	}

	function onclick_emitir(form){
		var data = buildFormBeanFromForm(document.forms[0]);
		setNestedBeanPropertyValue(data, "cnpj", getElementValue("cliente.pessoa.nrIdentificacao"));
		var sdo = createServiceDataObject("lms.contasreceber.emitirFaturasNacionaisAction", "", data);
		var reportFormat = 'pdf';
		executeReportWindowed(sdo, reportFormat);
		
		if (getElementValue("tpSituacaoFatura") == "DI"){
			setElementValue("tpSituacaoFatura", "EM");
		}

	}

	function onclick_emitirDactes(){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
		
		var sdo = createServiceDataObject('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirCTEbyIdFatura', 'imprimeCte', data);
	 	xmit({serviceDataObjects:[sdo]});
	}
	
	function imprimeCte_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_CTE", true);
			}
		} else {
			alert(error);
			return;
		}
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
		
		var sdo = createServiceDataObject('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirNTEbyIdFatura', 'imprimeNte', data);
	 	xmit({serviceDataObjects:[sdo]});
	}
	
	function imprimeNte_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_NTE", true);
			}
		} else {
			alert(error+'');
		}
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
		
		var sdo = createServiceDataObject('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirNSEbyIdFatura', 'imprimeNse', data);
	 	xmit({serviceDataObjects:[sdo]});
	}
	
	function imprimeNse_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_NSE", true);
			}
		} else {
			alert(error+'');
		}
	}
	
	/**
	 * Gerar bloqueios de faturamento para os itens da fatura
	 */
	function validateCancelaBloqueioFaturamento() {
		if (confirm(i18NLabel.getLabel("LMS-36070")) == true) {
			var remoteCall = {serviceDataObjects: []};
			var dataCall = createServiceDataObject(
				"lms.contasreceber.manterFaturasAction.validateBloqueioFaturamento", 
				"validateCancelaBloqueioFaturamento", 
				{idFatura:getElementValue("idFatura"), origem: "cancelar"}
			);
		
			remoteCall.serviceDataObjects.push(dataCall);
			xmit(remoteCall);  
		}
	}
	
	function validateCancelaBloqueioFaturamento_cb(data, error, erromsg) {
		if (error != undefined) {
			alert(error + '');
			return;
		}
		
		if (data.showPopup > 0) {
			var url = '/contasReceber/manterFaturas.do?cmd=bloqueio&origem=cancelar&idFatura=' + getElementValue("idFatura");
			var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:180px;');
			if (data != undefined && data.hasExecuted)
				cancelFatura();
		} else {
			cancelFatura();
		}
	}
	
	function openPgtoLPPopup(){
		var url = '/contasReceber/manterFaturas.do?cmd=pgtoPBPopup&idFatura=' + getElementValue("idFatura");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
	}
	
	function openTratativaPopup(){
		var url = '/contasReceber/manterFaturas.do?cmd=tratativaPopup&idFatura=' + getElementValue("idFatura");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:600px;');
	}
	
	function openMensagensPopup(){
		var url = '/contasReceber/manterFaturas.do?cmd=mensagensPopup&idFatura=' + getElementValue("idFatura");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:900px;dialogHeight:200px;');
	}
	
	/* Chama POP UP de Recebimentos Parciais */
	function recebimentosParciais(){
		var url = '/contasReceber/manterFaturas.do?cmd=recebimentosParciais&idFatura=' + getElementValue("idFatura");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:200px;');
	}
	
	function validateSalvarBloqueioFaturamento() {
		var remoteCall = {serviceDataObjects: []};
		var dataCall = createServiceDataObject(
			"lms.contasreceber.manterFaturasAction.validateBloqueioFaturamento", 
			"validateSalvarBloqueioFaturamento", 
			{idFatura:getElementValue("idFatura"), origem: "salvar"}
		);
	
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);  
	}
	
	function validateSalvarBloqueioFaturamento_cb(data, error, erromsg) {
		if (error != undefined) {
			alert(error + '');
			return;
		}
		
		if (data.showPopup > 0) {
			var url = '/contasReceber/manterFaturas.do?cmd=bloqueio&origem=salvar&idFatura=' + getElementValue("idFatura");
			var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:180px;');
			if (data != undefined && data.hasExecuted)
				storeButtonScript("lms.contasreceber.manterFaturasAction.store", "myStore", document.forms[0]);
		} else {
			storeButtonScript("lms.contasreceber.manterFaturasAction.store", "myStore", document.forms[0]);
		}
	}

	/* Bot�o store */
	function myStore(){
		/* Se tem uma divis�o, � obrigat�rio. */
		if (document.getElementById("divisaoCliente.idDivisaoCliente").options.length > 1) {
			document.getElementById("divisaoCliente.idDivisaoCliente").required = "true";
		} else {
			document.getElementById("divisaoCliente.idDivisaoCliente").required = "false";
		}

		/* Se tem um agrupamento, � obrigat�rio. */
		if (document.getElementById("agrupamentoCliente.idAgrupamentoCliente").options.length > 1) {
			document.getElementById("agrupamentoCliente.idAgrupamentoCliente").required = "true";
		} else {
			document.getElementById("agrupamentoCliente.idAgrupamentoCliente").required = "false";
		}
		
		/* Se tem um tipo de agrupamento, � obrigat�rio. */
		if (document.getElementById("tipoAgrupamento.idTipoAgrupamento").options.length > 1) {
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "true";
		} else {
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "false";
		}			
		
		/* Se a situa��o da fatura for 'Em boleto', mostrar uma mensagem dizendo que ele n�o vai salvar os dados da fatura, s� dos itens */
		if (getElementValue("tpSituacaoFatura") == "BL"){
			alert(i18NLabel.getLabel("LMS-36206")+'');	
		}
		
		//Valida os campos "A��o Corretiva" e "Observa��o"
		var acc = getElementValue("obAcaoCorretiva").length;
		var obs = getElementValue("obFatura").length;
		
		//var len = obs.length;

		if (acc > 500) {
			alert(getMessage(
				"Ajuste o campo \"A��o Corretiva\". M�ximo: 500 caracteres, mas est� com $0.", [ acc ]));
				return false;
				
			} else if (obs > 500) {
				alert(getMessage("Ajuste o campo \"Observa��o\". M�ximo: 500 caracteres, mas est� com $0.", [ obs ]));
				return false;
			}

		validateSalvarBloqueioFaturamento();
		atualizaRequerimentoDescontos();
		deveZerarFilialDebitada();
	}
	
	function atualizaRequerimentoDescontos() {
		if ((getElementValue("vlDesconto") != "") && getElementValue("vlDesconto") > 0) {
			document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto").required = true;
			document.getElementById("tpSetorCausadorAbatimento").required = true;
			document.getElementById("obAcaoCorretiva").required = true;
			document.getElementById("obFatura").required = true;	
		}
	}

	function validateRequirementDesconto(){		
		//Se o valor � maior que zero
		
		if (!compareData(getElementValue("vlDesconto"), "0.00", "currency") && getElementValue("vlDesconto") != "" && getElementValue("tpSituacaoAprovacao") != "" ){
			document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto").required = true;
			document.getElementById("tpSetorCausadorAbatimento").required = true;
			document.getElementById("obAcaoCorretiva").required = true;
			document.getElementById("obFatura").required = true;	
		}	
	}
		
	function deveZerarFilialDebitada() {
		if (compareData(getElementValue("vlDesconto"), "0.00", "currency") && getElementValue("vlDesconto") != "") {
			resetValue("filialByIdFilialDebitada.idFilial");
		}
	}

	/* Buscar a data de vencimento baseado no cliente, divis�o, modal, abrangencia. */
	function getDataVencimento(){
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findDataVencimento", "getDataVencimento", buildFormBeanFromForm(this.document.forms[0])));
		xmit(false);		
	}
	
	/* Encher o campo dtVencimento e atualizar os dados do masterLink. */
	function getDataVencimento_cb(d,e,c,x){
		if (e != undefined) {
			alert(e+'');
		} else {
			setElementValue("dtVencimento", setFormat("dtVencimento", d._value));
			var tabGroup = getTabGroup(this.document);
			var tabItem = tabGroup.getTab("documentoServico");
			var telaItem = tabItem.tabOwnerFrame;	
			copyMasterLink(this.document, telaItem.document);
		}
	}
	
	/* Onchange do cliente */
	function myClienteOnChange(){
		cleanFilialCliente();
		resetComboDivisao();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}

	/* Busca a filial do cliente e o cedente do cliente, chamado no callback do cliente. */
	function mountFilialDivisaoCliente(varIdCliente, varIdFatura){	
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialCliente", "mountFilialCliente", {idCliente:varIdCliente, idFatura:varIdFatura}));
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findCedente", "mountCedente", {cliente:{idCliente:varIdCliente}}));		
		xmit(false);		
	}
	
	/* Bot�o cancelar. */
	function cancelFatura(){	
			_serviceDataObjects = new Array();	
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.cancelFatura", "myOnDataLoad", buildFormBeanFromForm(this.document.forms[0])));
			xmit(true);		
		}
	
	/* Enche o campo filial de cobran�a baseado no cliente selecionado. */
	function mountFilialCliente_cb(d,e,o,x){
		cleanFilialCliente();
		if (e == undefined) {
			setElementValue("cliente.filialByIdFilialCobranca.idFilial",d.idFilial);		
			setElementValue("cliente.filialByIdFilialCobranca.sgFilial",d.sgFilial);
			setElementValue("cliente.filialByIdFilialCobranca.pessoa.nmFantasia",d.nmFantasia);
		} else {
			alert(e);
			resetValue("cliente.idCliente");
		}
	}
	
	/* Seleciona o cedente padr�o do cliente. */
	function mountCedente_cb(d,e,o,x){
		if (e == undefined) {
			setElementValue("cedente.idCedente",d._value);		
		} else {
			alert(e);
		}
	}

	/* On change do tipo de abrangencia. */
	function onChangeTpAbrangencia(){
		if (getElementValue("tpAbrangencia") == "N"){
			resetValue("simboloMoedaPais");
			resetValue("dtCotacaoMoeda");
			resetValue("cotacaoMoeda.idCotacaoMoeda");
			resetValue("cotacaoMoeda.vlCotacaoMoeda");			
			resetValue("vlCotacaoMoeda");
			resetValue("vlCotacaoMoedaTmp");
			resetValue("vlIva");				
					
			setDisabled("cotacaoMoeda.idCotacaoMoeda",true);
			setDisabled("cotacaoMoeda.vlCotacaoMoeda",true);			
			setDisabled("vlCotacaoMoeda",true);
			setDisabled("vlCotacaoMoedaTmp",true);
			setDisabled("vlIva",true);	
		} else {
			setDisabled("cotacaoMoeda.idCotacaoMoeda",false);
			setDisabled("cotacaoMoeda.vlCotacaoMoeda",false);
			setDisabled("vlCotacaoMoeda",false);
			setDisabled("vlCotacaoMoedaTmp",false);
			setDisabled("vlIva",false);			
		}		
	}

	/* Limpa o campo filial de cobran�a. */
	function cleanFilialCliente(){
		setElementValue("cliente.filialByIdFilialCobranca.idFilial","");		
		setElementValue("cliente.filialByIdFilialCobranca.sgFilial","");
		setElementValue("cliente.filialByIdFilialCobranca.pessoa.nmFantasia","");	
	}
	
	/* Chamar o initPage depois do onPageLoad_cb. */
	function myOnPageLoad_cb(d,e,o,x){
		initPage();
		var url = new URL(parent.location.href);
		
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			onDataLoad(url.parameters.idProcessoWorkflow);		
		}
		onPageLoad_cb(d,e,o,x);
		
		// valida a filial do usu�rio logado.
		validateFilialUser();

		onChangeSetorCausadorAbatimento();
	}
	
	/* Lista de todas as situa��es da fatura. */
	var constStatusFaturaFull = new Array();
	
	/* Lista de situa��es da fatura na inser��o. */
	var constStatusFatura = new Array(2);

	var dataTmp;
	var errorTmp;
	
	/* Carregar a pagina com os dados da fatura. */
	function myOnDataLoad_cb(d,e,o,x){
		if (e != null){
			dataTmp = null;
			errorTmp = null;			
			return false;
		}

		loadSituacaoFatura();	
		onDataLoad_cb(d,e);
		
		try{
			setElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto", d.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto);
		} catch(err) {
			setElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto", "");
		}
		
		onChangeSetorCausadorAbatimento();

		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		
		var objDivisao = {idCliente: d.cliente.idCliente, tpSituacao: "A"};
		
		if (d.divisaoCliente != undefined && d.divisaoCliente.idDivisaoClienteInativo != undefined){
			objDivisao.idDivisao = d.divisaoCliente.idDivisaoClienteInativo;
		}		
		
		/* Buscar as filiais do cliente (inclusive a filial inativa da fatura se tem). */
		var sdoDivisao = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboDivisaoCliente","findComboDivisaoCliente", objDivisao);
		remoteCall.serviceDataObjects.push(sdoDivisao);
		
		/* Buscar a lista da agrupamento (inclusiva a inativa da fatura se tem). */
		if (d.divisaoCliente != undefined && d.divisaoCliente.idDivisaoClienteTmp != undefined) {
			var objAgrupamento = {divisaoCliente:{idDivisaoCliente:d.divisaoCliente.idDivisaoClienteTmp}};
			
			if (d.agrupamentoCliente != undefined && d.agrupamentoCliente.idAgrupamentoClienteTmp != null){
				objAgrupamento.idAgrupamentoCliente = d.agrupamentoCliente.idAgrupamentoClienteTmp;
			}
			
			var sdoAgrupamento = createServiceDataObject("lms.contasreceber.manterFaturasAction.findAgrupamentoCliente","findComboAgrupamentoCliente", objAgrupamento);
			remoteCall.serviceDataObjects.push(sdoAgrupamento);
		}
		
		/* Buscar a lista de tipo de agrupamento (inclusiva o inativo da fatura se tem). */
		if (d.agrupamentoCliente != undefined && d.agrupamentoCliente.idAgrupamentoClienteTmp != null) {
			var sdoForma = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento","findComboTipoAgrupamento", {agrupamentoCliente:{idAgrupamentoCliente: d.agrupamentoCliente.idAgrupamentoClienteTmp}});
			remoteCall.serviceDataObjects.push(sdoForma);
		}

		xmit(remoteCall);
		
		prepareTela();
		
		var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
		/* Se a fatura foi aberta a partir da tela de workflow. */
		if (idProcessoWorkflow != ""){
			showSuccessMessage();
			setDisabled(document, true);
		}
		
		// seta descri��o na aba de anexos
		/*
		setElementValue("faturaDsConcatenado", 
				getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") + " - " + 
				getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial") + " - " +
				getElementValue("devedorDocServFat.doctoServico.nrDoctoServico")
				 );
		 */		

		if(getElementValue("tpSetorCausadorAbatimento") == ""){
			getElement("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto").required = false;
			getElement("tpSetorCausadorAbatimento").required = false;
			getElement("obAcaoCorretiva").required = false;
			getElement("obFatura").required = false;				
			
	}
	}

	/* Callback do bot�o store. */
	function myStore_cb(d,e,c,x){
		if (e == undefined){
			loadSituacaoFatura();
		}
		store_cb(d,e,c,x);
		
		var tabGroup = getTabGroup(this.document);	
				
		var tabDoctoServico = tabGroup.getTab("documentoServico");
		var telaDoctoServico = tabDoctoServico.tabOwnerFrame.document;
		
		if( d != undefined ){
			setElementValue(telaDoctoServico.getElementById("_nrFaturaFormatada"), d.nrFaturaFormatada);
			setFocusOnNewButton(document);
		}
		
		if (e == undefined){
			prepareTela();
		}
		
		if(c =="LMS-36099"){
			document.getElementById("dtEmissao").focus();
			document.getElementById("dtEmissao").select();
		}
	}
	
	/* Habilita, desabilita e formata os campos. */
	function prepareTela() {
		setElementValue("vlCotacaoMoedaTmp",setFormat("cotacaoMoeda.vlCotacaoMoeda",getElementValue("cotacaoMoeda.vlCotacaoMoeda")));
		format("cotacaoMoeda.vlCotacaoMoeda");
		format("vlCotacaoMoedaTmp");

		if (getElementValue("vlCotacaoMoeda") != ""){
			setElementValue("cotacaoMoeda.vlCotacaoMoeda",setFormat("cotacaoMoeda.vlCotacaoMoeda",getElementValue("vlCotacaoMoeda")));
		}
		setDisabled("tpSituacaoFatura",true);
		
		setElementValue("nrFaturaFormatada", getElementValue("filialByIdFilial.sgFilial") + " " + setFormat("nrFatura", getElementValue("nrFatura")));
		
		disabledButtons();
		onChangeTpAbrangencia();			
	}
	
	/* Popup callback da lookup de cliente. */
	function popupCliente(data){
		if (data == undefined) {
			return;
		}
		findComboDivisaoCliente(data.idCliente);
		mountFilialDivisaoCliente(data.idCliente);
	}
	
	/* Callback da lookup de cliente. */
	function onDataLoadCliente_cb(data){
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		handleDisableReemitirFatura(true);
		if (data != undefined && data.length > 0) {
			findComboDivisaoCliente(getElementValue("cliente.idCliente"));
			mountFilialDivisaoCliente(data[0].idCliente);
			if(getElementValue("reemitir.enableReemitir") == 'true') {
				handleDisableReemitirFatura(false);
			}else{
				handleDisableReemitirFatura(true);
			}
		}
	}		
	
	
	function initWindow(eventObj) {	
    	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			initPage();
			 addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.clearSessionItens",
	                    null,
	                    null));
	    		xmit(false);
	    }
	    
	    if (eventObj.name == "tab_click") {
	    	if (eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq"){		    	
	    		disabledButtons(false);	    		
	    	} else {
				newButtonScript();	    	
	    	}
	    }
	    
	    var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
		/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
		if (idProcessoWorkflow != ""){
			setDisabled(document, true);
		}

		validateRequirementDesconto();
		deveZerarFilialDebitada();
	}

	/* Inicializa os dados da tela. */
	function initPage(){
		onChangeTpAbrangencia();	
		resetComboDivisao();
		
		//seta valor default para os campos
		cleanFields();
		
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findInitialValue", "initPage", null)); 
		
		var cedente_idCedenteDynamicCriteria = new Array();
		setNestedBeanPropertyValue(cedente_idCedenteDynamicCriteria, ":0._criteriaProperty", "idFatura");
		setNestedBeanPropertyValue(cedente_idCedenteDynamicCriteria, ":0._modelProperty", "faturas.idFatura");		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboCedentesActive", "cedente.idCedente", { _dynamicComboBoxCriteria:cedente_idCedenteDynamicCriteria, tpSituacao:"A" })); 
		
		xmit(false);
	}
	
	/* Monta as duas constantes que tem a lista de situa��o de fatura. */
	function initPage_cb(d,e,o,x){
		if (e == undefined) {	
			loadSituacaoFaturaNovo();
			onDataLoad_cb(d,e,o,x);
			
			disabledButtons(true);
			constStatusFaturaFull = d.tpSituacaoFatura;
			
			for (var i = 0; i < constStatusFaturaFull.length; i++) {
				if (constStatusFaturaFull[i].value == "DI"){
					constStatusFatura[0] = constStatusFaturaFull[i];
				} else if (constStatusFaturaFull[i].value == "IN"){
					constStatusFatura[1] = constStatusFaturaFull[i];
				}
			}			
		} else {
			alert(e+'');
		}
		onChangeTpAbrangencia();
		
	}	

	/* Monta a combo de fatura baseado no objeto constStatusFaturaFull, se o objeto n�o foi carregado,
	   chamar um timeout at� ele carregar o objeto. */
	function loadSituacaoFatura(){
		if (constStatusFaturaFull[0] != undefined){
			document.getElementById("tpSituacaoFatura").length = 0;
				
			for (var i = 0; i < constStatusFaturaFull.length; i++) {
				document.getElementById("tpSituacaoFatura")[i] = new Option(constStatusFaturaFull[i].description,constStatusFaturaFull[i].value);  		
			}
			setDisabled("tpSituacaoFatura",true);
		} else {	
			setTimeout("loadSituacaoFatura()",1000);
		}			
	}

	/* Monta a combo de fatura baseado no objeto constStatusFatura, se o objeto n�o foi carregado,
	   chamar um timeout at� ele carregar o objeto. */
	function loadSituacaoFaturaNovo(){	
		if (constStatusFatura[0] != undefined){
			document.getElementById("tpSituacaoFatura").length = 0;
			
			document.getElementById("tpSituacaoFatura")[0] = new Option(constStatusFatura[0].description,constStatusFatura[0].value);  
			document.getElementById("tpSituacaoFatura")[1] = new Option(constStatusFatura[1].description,constStatusFatura[1].value);  					
			setDisabled("tpSituacaoFatura",false);
		} else {	
			setTimeout("loadSituacaoFaturaNovo()",1000);
		}			
	}
	
	/* No click do bot�o Recibo. */
	function onClickRecibo(){
		if (getElementValue("tpAbrangencia") == "I" ){
			parent.parent.redirectPage('contasReceber/manterReciboOficial.do?cmd=main' + buildLinkPropertiesQueryString_recibo());
		} else {
			parent.parent.redirectPage('contasReceber/manterReciboBrasil.do?cmd=main' + buildLinkPropertiesQueryString_recibo());
		}		
	}
	
	/* Monta a queryString do bot�o Recibo. */
	function buildLinkPropertiesQueryString_recibo() {
		var qs = "";
		qs += "&idRecibo=" + getElementValue("recibo.idRecibo");
		qs += "&nrRecibo=" + getElementValue("recibo.nrRecibo", false);
		qs += "&filialByIdFilialEmissora.sgFilial=" + getElementValue("filialByIdFilial.sgFilial");
		qs += "&filialByIdFilialEmissora.idFilial=" + getElementValue("filialByIdFilial.idFilial");
		qs += "&filialByIdFilialEmissora.pessoa.nmFantasia=" + getElementValue("filialByIdFilial.pessoa.nmFantasia");		
		return qs;
	}	

	/* Habilita ou desabilita os bot�es. */
	function disabledButtons(blDisableDefault){
		var varTpSituacaoFatura = getElementValue("tpSituacaoFatura");
		setDisabled("storeButton",false);

		if (getElementValue("idFatura") == ""){
			setDisabled("emitir",true);
			setDisabled("emitirDactes",true);
			setDisabled("complemento",true);		
			setDisabled("removeButton",true);
			setDisabled("cancelar",true);
			setDisabled("removeButton",true);
		} else {
			setDisabled("emailFatura",false);
			setDisabled("mensagensPopup",false);
			setDisabled("emitir",false);
			setDisabled("emitirDactes",false);
			setDisabled("complemento",false);			
			setDisabled("removeButton",false);
			setDisabled("cancelar",false);
			setDisabled("removeButton",false);			
		}
		
		if (varTpSituacaoFatura != "DI" && varTpSituacaoFatura != "EM" && varTpSituacaoFatura != "BL" && varTpSituacaoFatura != "IN" && varTpSituacaoFatura != ""){
			setDisabled("storeButton",true);
			setDisabled("removeButton",true);			
		}
		
 		if (getElementValue("enableAdmButtons") == 'true' && getElementValue("enableGerarDesconto") == 'true'){
			setDisabled("btnGerarDescontoTotalDevedor",false);	
		} else {
			setDisabled("btnGerarDescontoTotalDevedor",true);	
		}
 		
 		if (getElementValue("enableAdmButtons") == 'true' && getElementValue("enableGerarFaturaExcel") == 'true'){
			setDisabled("btnGerarFaturaExcel", false);	
		} else {
			setDisabled("btnGerarFaturaExcel", true);	
		}
 		if (getElementValue("enableAdmButtons") == 'true' && getElementValue("enableImportarDesctoCSV") == 'true'){
			setDisabled("btnImportarDesctoCSV", false);	
		} else {
			setDisabled("btnImportarDesctoCSV", true);	
		}
		 
		if (varTpSituacaoFatura == "IN" && getElementValue("idFatura") != ""){
			setDisabled("storeButton",true);
			setDisabled("removeButton",true);				
		}				
		
		if (varTpSituacaoFatura == "LI" || varTpSituacaoFatura == "IN" || varTpSituacaoFatura == "CA"){
			setDisabled("cancelar",true);
		}				
		
		if (getElementValue("boleto.idBoleto") == ""){
			setDisabled("boletoButton",true);
		} else {
			setDisabled("boletoButton",false);
		}

		if (getElementValue("reemitir.enableReemitir") == 'true' && (varTpSituacaoFatura != "DI" && varTpSituacaoFatura != "CA")){
			setDisabled("reemitirFaturaIntegracao", false);
		} else {
			setDisabled("reemitirFaturaIntegracao", true);
		}

		if (getElementValue("relacaoCobranca.idRelacaoCobranca") == ""){
			setDisabled("relacaoCobrancaButton",true);
		} else {
			setDisabled("relacaoCobrancaButton",false);
		}
		
		if (getElementValue("redeco.idRedeco") == ""){
			setDisabled("redecoButton",true);
		} else {
			setDisabled("redecoButton",false); 
		}
		
		if (getElementValue("recibo.idRecibo") == ""){
			setDisabled("recibo",true);
		} else {
			setDisabled("recibo",false);		
		}

		// Caso a situa��o seja DI ou EM ou BL
		// Caso exista um desconto e o mesmo seja maior que zero
		// Caso o a filial do usu�rio logado seja MTZ ou a filial de origem da 
		// fatura seja a mesma do usu�rio logado, desabilita o bot�o de zerar descontos.
		if ((varTpSituacaoFatura == "DI" || varTpSituacaoFatura == "EM" || varTpSituacaoFatura == "BL") 
				&& (getElementValue("vlDesconto") != "" && getElementValue("vlDesconto") > 0) 
				&& (getElementValue("enableAdmButtons") == 'true')) {
						setDisabled("btnZerarDesconto", false);		
		} else {
			setDisabled("btnZerarDesconto", true);		
		} 
	
		if (getElementValue("idProcessoWorkflow") != ""){
			setDisabled("emitir",true);
			setDisabled("emitirDactes",true);
			setDisabled("complemento",true);		
			setDisabled("removeButton",true);
			setDisabled("cancelar",true);		
			setDisabled("storeButton",true);					
			setDisabled("cancelar",true);
			setDisabled("btnZerarDesconto",true);
			setDisabled("boletoButton",true);
			setDisabled("relacaoCobrancaButton",true);
			setDisabled("redecoButton",true);
			setDisabled("recibo",true);
			setDisabled("newButton",true);
		}	
	}
	

	/* N�o faz a busca quando alterar o valor, deixando apenas no clique da picker. */
	function vlCotacaoMoedaOnChange(eThis){
		if (getElementValue("vlCotacaoMoedaTmp") == getElementValue("cotacaoMoeda.vlCotacaoMoeda")){
			setElementValue("vlCotacaoMoeda","");
		} else {
			setElementValue("vlCotacaoMoeda",getElementValue("cotacaoMoeda.vlCotacaoMoeda"));
			format(document.getElementById("vlCotacaoMoeda"), event);
		}
		return true;
	}
	
	/* Concatena o simbolo da moeda com a sigla do pa�s */
	function getSimboloMoedaPais(moedaPais){
		if (moedaPais == undefined) return "";
		if (moedaPais.moeda != undefined && moedaPais.pais != undefined){
			return moedaPais.moeda.dsSimbolo + " - " + moedaPais.pais.sgPais;
		}
		return "";
	}
	
	/* Ao clicar na listagem da popUp (lookup) atribui o valor do simboloMoedaPais */
	function cotacaoMoedaSetValue(data){
		if (data == undefined) return;
		setElementValue( "simboloMoedaPais", getSimboloMoedaPais(data.moedaPais) );
	}
	
	
	/* Remove propertyMapping */
	function removePropertyMapByCriteria(e, criteriaName){

		var props = e.propertyMappings;
		var newProps = new Array();
		for (var i = 0; i < props.length; i++){
			var map = props[i];
			 // Nao copia a criteria informada
			if (map.criteriaProperty != criteriaName){
				newProps[newProps.length] = props[i];
			}
		}
		e.propertyMappings = newProps;
	}
	
	/* Busca as divis�es do cliente */
	function findComboDivisaoCliente(idCliente){
		if (idCliente == undefined){
			idCliente = getElementValue("cliente.idCliente");
		}
		
		if (getElementValue("tpModal") != "" && getElementValue("tpAbrangencia") != "" && idCliente != ""){
			var data = new Array();	   
			
			setNestedBeanPropertyValue(data, "idCliente", idCliente);
			setNestedBeanPropertyValue(data, "tpModal", getElementValue("tpModal"));
			setNestedBeanPropertyValue(data, "tpAbrangencia", getElementValue("tpAbrangencia"));
			setNestedBeanPropertyValue(data, "tpSituacao", "A");
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboDivisaoCliente","findComboDivisaoCliente", data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			resetComboDivisao();
		}
	}
	
	/* CallBack da fun��o findComboDivisaoCliente */
	function findComboDivisaoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		divisaoCliente_idDivisaoCliente_cb(data);
		
		if(getElement("divisaoCliente.idDivisaoCliente").options.length > 1){
			getElement("divisaoCliente.idDivisaoCliente").required = "true";
			setDisabled("divisaoCliente.idDivisaoCliente", false);
			
			if(getElement("divisaoCliente.idDivisaoCliente").options.length == 2){
				setElementValue("divisaoCliente.idDivisaoCliente", getElement("divisaoCliente.idDivisaoCliente").options[1]);
			}
		}else{
			resetComboDivisao();
		}

		if (getElementValue("divisaoCliente.idDivisaoClienteTmp") != ""){
			setElementValue("divisaoCliente.idDivisaoCliente", getElementValue("divisaoCliente.idDivisaoClienteTmp"));
		} else {
			getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		}
		
		if (getElementValue("divisaoCliente.idDivisaoCliente") != "" && getElementValue("agrupamentoCliente.idAgrupamentoClienteTmp") == ""){
			findComboAgrupamentoCliente(getElementValue("divisaoCliente.idDivisaoCliente"));
		}
		
		/**
		  * C�digo retirado da tela ITEM, fun��o myStoreItem_cb, por problemas de concorr�ncia 
		  * Estava buscando a data de vencimento da fatura sem ter populado a combo de divis�o
		  */		
		if( getElementValue("idFatura") == "" ){
			getDataVencimento();
		}
		
	}

	/* Limpa todos os campos ligados com a combo de divis�o. */
	function resetComboDivisao(){
		getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
		getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		setElementValue("divisaoCliente.idDivisaoClienteTmp", "");
		setElementValue("divisaoCliente.idDivisaoClienteInativo", "");
		
		resetComboAgrupamentoCliente();
		resetComboTipoAgrupamento();
	}

	/* Onchange da combo de divis�o. */
	function onChangeComboDivisao(){
		setElementValue("divisaoCliente.idDivisaoClienteTmp", "");
		resetComboAgrupamentoCliente()
		
		if (getElementValue("divisaoCliente.idDivisaoCliente") != ""){
			findComboAgrupamentoCliente(getElementValue("divisaoCliente.idDivisaoCliente"));
		}
		
		getDataVencimento();
	}

	/** Busca as divis�es do cliente */
	function findComboAgrupamentoCliente(idDivisao){
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findAgrupamentoCliente","agrupamentoCliente.idAgrupamentoCliente", {divisaoCliente:{idDivisaoCliente:idDivisao}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	/* CallBack da fun��o findComboDivisaoCliente */
	function findComboAgrupamentoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		agrupamentoCliente_idAgrupamentoCliente_cb(data);
		
		if(getElement("agrupamentoCliente.idAgrupamentoCliente").options.length > 1){
			getElement("agrupamentoCliente.idAgrupamentoCliente").required = "true";
			
			if(getElement("agrupamentoCliente.idAgrupamentoCliente").options.length == 2)
				getElement("agrupamentoCliente.idAgrupamentoCliente").options[1].selected = true;
			
		}else{
			resetComboAgrupamentoCliente();
		}
		
		if (getElementValue("agrupamentoCliente.idAgrupamentoClienteTmp") != ""){
			setElementValue("agrupamentoCliente.idAgrupamentoCliente", getElementValue("agrupamentoCliente.idAgrupamentoClienteTmp"));
		} else {
			getElement("agrupamentoCliente.idAgrupamentoCliente").options[0].selected = true;
		}
	}
	
	/* Reinicia a combo de agrupamento. */
	function resetComboAgrupamentoCliente(){
		getElement("agrupamentoCliente.idAgrupamentoCliente").options.length = 1;
		setElementValue("agrupamentoCliente.idAgrupamentoClienteTmp", "");
		getElement("agrupamentoCliente.idAgrupamentoCliente").required = "false";		
		resetComboTipoAgrupamento();
	}
	
	/* Onchange da combo de agrupamento. */
	function onChangeComboAgrupamentoCliente(){
		setElementValue("agrupamentoCliente.idAgrupamentoClienteTmp", "");
		resetComboTipoAgrupamento();
		
		if (getElementValue("agrupamentoCliente.idAgrupamentoCliente") != ""){
			findComboTipoAgrupamento(getElementValue("agrupamentoCliente.idAgrupamentoCliente"));
		}
	}	

	/** Busca as divis�es do cliente */
	function findComboTipoAgrupamento(idAgrupamento){
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento","findComboTipoAgrupamento", {agrupamentoCliente:{idAgrupamentoCliente:idAgrupamento}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	/* CallBack da fun��o findComboDivisaoCliente */
	function findComboTipoAgrupamento_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		tipoAgrupamento_idTipoAgrupamento_cb(data);
		
		if(getElement("tipoAgrupamento.idTipoAgrupamento").options.length > 1){
			getElement("tipoAgrupamento.idTipoAgrupamento").required = "true";
			
			if(getElement("tipoAgrupamento.idTipoAgrupamento").options.length == 2) {
				getElement("tipoAgrupamento.idTipoAgrupamento").options[1].selected = true;
			}
		}else{
			resetComboTipoAgrupamento();
		}

		if (getElementValue("tipoAgrupamento.idTipoAgrupamentoTmp") != ""){
			setElementValue("tipoAgrupamento.idTipoAgrupamento", getElementValue("tipoAgrupamento.idTipoAgrupamentoTmp"));
		} else {
			getElement("tipoAgrupamento.idTipoAgrupamento").options[0].selected = true;
		}
	}
	
	/* Reinicia a combo de tipo de agrupamento. */
	function resetComboTipoAgrupamento(){
		getElement("tipoAgrupamento.idTipoAgrupamento").options.length = 1;
		setElementValue("tipoAgrupamento.idTipoAgrupamentoTmp", "");
		getElement("tipoAgrupamento.idTipoAgrupamento").required = "false";		
	}	
	
	function zerarDescontoFatura(){
		_serviceDataObjects = new Array();
		var dados = new Array();
		
		setNestedBeanPropertyValue(dados, "idFatura", getElementValue("idFatura"));		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.zerarDescontoFatura", "zerarDescontoFatura", dados)); 
		
		xmit(true);
		
	}
	
	function zerarDescontoFatura_cb(data, error){
		myOnDataLoad_cb(data, error);
	}
	
	function gerarDescontoTotalDevedorAutomatico(){
		if (confirm(i18NLabel.getLabel("LMS-36284"))) {
			var dados = new Array();
				
			setNestedBeanPropertyValue(dados, "idFatura", getElementValue("idFatura"));		
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.gerarDescontoTotalDevedorAutomatico", "gerarDescontoTotalDevedorAutomatico", dados); 

			xmit({serviceDataObjects:[sdo]});
		}
	}

	function gerarDescontoTotalDevedorAutomatico_cb(data, error){
		alert(i18NLabel.getLabel("LMS-36283"));
		myOnDataLoad_cb(data, error);
	}
	
	/**
	  * Valida se a filial do usu�rio logado � uma sucursal.
	  */
	function validateFilialUser() {
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.validateFilialUser", "validateFilialUser", new Array())); 
		xmit(false);
	}
	
	/**
	  * Call back da function validateFilialUser.
	  * desabilita o campo blGerarBoleto caso a filial
	  * seja uma sucursal.
	  */
	function validateFilialUser_cb(data, error) {
		if (error != undefined) {
			alert(error);
		}
		
		// Caso a filial do usu�rio logado seja uma sucursal, 
		// desabilita o campo de gera��o de boleto.
		if (data.disableBlGeraBoleto == "true") {
			setDisabled("blGerarBoleto", true);
			setElementValue("blGerarBoleto", false);
		}
		
	}
	/**
	* Onchange do combo tpSetorCausadorAbatimento
	*/
	function onChangeSetorCausadorAbatimento(){
		var idSetorCausadorAbatimento = getElementValue("tpSetorCausadorAbatimento");
		findMotivoDescontoByTpMotivoDesconto(idSetorCausadorAbatimento)
	}
	function sendByEmail(){
		var idFatura = getElementValue("idFatura");
		var data = new Array();	   		
		setNestedBeanPropertyValue(data, "idFatura", idFatura);
		
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.registrarEventoMsg","sendByEmail", data);
		xmit({serviceDataObjects:[sdo]});
	}
	function sendByEmail_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
	}
	function findMotivoDescontoByTpMotivoDesconto(idSetorCausadorAbatimento){
		var data = new Array();	   		
		setNestedBeanPropertyValue(data, "idSetorCausadorAbatimento", idSetorCausadorAbatimento);
		var sdo = createServiceDataObject("lms.contasreceber.manterMotivosDescontosAction.findMotivoDescontoByTpMotivoDesconto","findMotivoDescontoByTpMotivoDesconto", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findMotivoDescontoByTpMotivoDesconto_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		comboboxLoadOptions({e:document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto"), data:data});

		if(getElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto") && getElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto") != ""){
			setElementValue("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto", getElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto"));
		} else {
			resetValue("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto");
		}
		setElementValue("load.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto", "");
	}

	function cleanFields(){
		getElement("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto").required = false;
		getElement("tpSetorCausadorAbatimento").required = false;
		getElement("obAcaoCorretiva").required = false;
		getElement("obFatura").required = false;
	}

	function showHistoricoAprovacaoPendenciaDesconto(){
		 if (getElementValue('pendenciaDesconto.idPendencia') != '') {
			if(getElementValue('isQuestionamentoFatura') == "true"){
				showModalDialog('questionamentoFaturas/consultarHistoricoQuestionamentoFaturas.do?cmd=list&idQuestionamentoFatura='+getElementValue('pendenciaDesconto.idPendencia'),
								window,
								'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
			} else {
				showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('pendenciaDesconto.idPendencia'),
						window,
						'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
			}
		}
	}
	
	document.getElementById("imgHistoricoAprovacao").alt = i18NLabel.getLabel("fluxoAprovacaoCotacao");
	
	
</script>