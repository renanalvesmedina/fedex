<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function geraColunas() {
	colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
	for (i = 0 ; i < 33 ; i++) {
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
		colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
	}
	colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
	return colunas;
}
</script>
<adsm:window service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction" onPageLoad="myPageLoad" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00065"/>
		<adsm:include key="LMS-26075"/>
		<adsm:include key="LMS-26080"/>
		<adsm:include key="LMS-26081"/>
		<adsm:include key="LMS-26092"/>
		<adsm:include key="LMS-26097"/>
	</adsm:i18nLabels>

	<adsm:form action="/contratacaoVeiculos/manterSolicitacoesContratacao" idProperty="idSolicitacaoContratacao" height="390" onDataLoadCallBack="dataLoad">
		<adsm:hidden property="reconsultarFilho" serializable="false"/>
		<adsm:hidden property="reconsultarFluxoContratacao" serializable="false"/>
		<adsm:hidden property="idPendencia"/>
		<adsm:hidden property="tpSituacaoPendencia"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="empresaRota.tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="empresaRota.flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="empresaRota.flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="empresaRota.tpAcesso" value="" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="idAcao" serializable="true"/>
		<adsm:hidden property="warning" serializable="false"/>
		<adsm:hidden property="behavior" serializable="false"/>
		<adsm:hidden property="dtAtual" serializable="false"/>
		<adsm:hidden property="controleCarga.tpControleCarga" value="V" serializable="false" />
		<adsm:hidden property="filial.dtImplantacaoLMS" serializable="true" />
		<adsm:hidden property="emailInvalido" serializable="false"/>
		<adsm:hidden property="isAnoConfirmado" value="N" />
		<adsm:hidden property="tpCalculo" value="" />
		
		<adsm:lookup
			label="filialSolicitante"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupFilial"
			action="/municipios/manterFiliais"
			required="true"
			labelWidth="23%"
			onDataLoadCallBack="filialMigrada"
			afterPopupSetValue="filialMigrada_cb"
			dataType="text"
			size="3"
			maxLength="3"
			width="77%" 
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>

		<adsm:combobox property="tpSolicitacaoContratacao" label="tipoSolicitacao" domain="DM_TIPO_SOLICITACAO_CONTRATACAO" labelWidth="23%" width="32%"
			onchange="changeTpSolicitacaoContratacao(this)" boxWidth="150" required="true" onlyActiveValues="true"/>

		<adsm:combobox
			label="abrangencia"
			property="tpAbrangencia"
			domain="DM_ABRANGENCIA_TOTAL"
			labelWidth="23%"
			width="37%"
			boxWidth="150"
			required="true"
			onlyActiveValues="true"
			onchange="return changeTpAbrangencia();"
		/>

		<adsm:combobox
			label="tipoVinculo"
			property="tpVinculoContratacao"
			optionProperty="value"
			optionLabelProperty="description"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findTpVeiculo"
			onchange="comboboxChange({e:this});changeTpSolicitacaoContratacao(false);"
			labelWidth="15%"
			width="25%"
			boxWidth="150"
			required="true"
			onDataLoadCallBack="tipoVinculo"
		>
			<adsm:propertyMapping modelProperty="tpSolicitacaoContratacao" criteriaProperty="tpSolicitacaoContratacao"/>
		</adsm:combobox> 

		<adsm:textbox
			dataType="integer"
			property="nrSolicitacaoContratacao"
			label="numeroSolicitacao"
			mask="0000000000"
			maxLength="10"
			size="20"
			labelWidth="23%"
			width="37%"
			disabled="true"
		/>

		<adsm:combobox
			label="moeda"
			property="moedaPais.idMoedaPais"
			optionProperty="idMoedaPais"
			optionLabelProperty="moeda.siglaSimbolo"
			onDataLoadCallBack="moedaPais"
			labelWidth="15%"
			width="25%"
			boxWidth="150"
			required="true"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findComboMoedaPais"
			onchange="comboboxChange({e:this});findVlSugerido();" >
			<adsm:propertyMapping modelProperty="idFilial" criteriaProperty="filial.idFilial"/>
		</adsm:combobox>
		
		
		<adsm:hidden property="tipoMeioTransporte.tpMeioTransporte" value="R" />
		<adsm:combobox
			property="tpModal" boxWidth="150" label="modalidade" 
			optionProperty="value" onchange="comboboxChange({e:this});changeTpModal();"
			optionLabelProperty="description" 
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findComboModal"  width="77%" labelWidth="23%" required="true" onlyActiveValues="true"
		>
		</adsm:combobox>

		<adsm:hidden property="qtdeixosTemp" serializable="false" />
		<adsm:hidden property="idTipoMeioTransporteTemp" serializable="false" />
		<adsm:hidden property="dsTipoMeioTransporteTemp" serializable="false" />
		<adsm:combobox
			label="tipoMeioTransporte"
			property="tipoMeioTransporte.idTipoMeioTransporte" 
			optionLabelProperty="dsTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte" 
			onchange="comboboxChange({e:this});changeTipoMeioTransporte(this);"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findComboTpMeioTransporte" 
			labelWidth="23%"
			width="37%"
			boxWidth="230"
			required="true"
			onlyActiveValues="true"
			onDataLoadCallBack="setaTipoMeioTransporte"
		>
			<adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte" serializable="false"/>
		</adsm:combobox>

		<adsm:textbox
			label="compostoPor"
			property="compostoPor"
			dataType="text"
			size="26"
			disabled="true"
			labelWidth="15%"
			width="25%"
			serializable="false"
		/>

		<!-- LMSA-6319 -->
		<td colspan="4">
		<adsm:combobox 
			property="cargaCompartilhada.tipo" 
			label="tipoCargaCompartilhada" 
			domain="DM_TIPO_CARGA_COMPARTILHADA" 
			labelWidth="23%" width="32%"
			boxWidth="150" 
			required="false"
            disabled="true" 
			onlyActiveValues="true"/>
		</td>

		<adsm:combobox
			label="quantidadeEixos"
			property="eixosTipoMeioTransporte.qtEixos"
			optionLabelProperty="qtEixos"
			optionProperty="qtEixos"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findComboEixosTipoMeioTransporte"
			labelWidth="23%"
			width="37%"
			boxWidth="180"
			required="true"
			onchange="comboboxChange({e:this});findVlSugerido();"
			onDataLoadCallBack="setaQtdEixos"
		>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="idTipoMeioTransporte"/>
		</adsm:combobox>

		<adsm:checkbox
			property="blIndicadorRastreamento"
			label="meioTransporteRastreado"
			width="18%"
			labelWidth="22%"
		/>

		<adsm:textarea property="obObservacao" maxLength="500" label="observacao" columns="90" labelWidth="23%" width="87%" required="true"/>

		<adsm:combobox
			property="blQuebraMeioTransporte"
			label="quebraMeioTransporte"
			domain="DM_SIM_NAO"
			labelWidth="23%"
			width="37%"
			defaultValue="N"
			required="true"
			onchange="return changeQuebraMeioTransporte();"
		/>
		
		
		<adsm:lookup
			label="controleCarga"
			property="controleCarga.filialByIdFilialOrigem"
			idProperty="idFilial" 
			criteriaProperty="sgFilial"
			popupLabel="pesquisarFilial"
			dataType="text" 
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupFilialByControleCarga" 
			action="/municipios/manterFiliais"
			onchange="return changeFilialControleCarga();" 
			size="3" 
			maxLength="3" 
			labelWidth="15%" 
			width="25%" 
			picker="false" 
			serializable="false"
			disabled="true">
			
			<adsm:lookup 
				property="controleCarga"
		 		idProperty="idControleCarga"
		 		criteriaProperty="nrControleCarga"
				service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupControleCarga"
				action="/carregamento/manterControleCargas"
				onDataLoadCallBack="callbackControleCarga"
				afterPopupSetValue="findDadosControleCarga"
				dataType="integer"
				popupLabel="pesquisarControleCarga"
				size="9"
				maxLength="8"
				mask="00000000"
				disabled="true">
				
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" />
 				<adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="controleCarga.tpControleCarga" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false"/>
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox dataType="JTDate" property="dtCriacao" label="dataSolicitacao" maxLength="10" labelWidth="23%" width="77%" disabled="true"/>

		<adsm:textbox
			label="solicitadoPor"
			dataType="text"
			property="usuarioSolicitador.nrMatricula"
			size="10"
			labelWidth="23%"
			width="11%"
			disabled="true"
			serializable="false"
		/>
		<adsm:textbox
			property="usuarioSolicitador.nmUsuario"
			dataType="text"
			size="20"
			disabled="true"
			width="26%"
			required="true"
			serializable="false"
		/>
		
		<adsm:textbox
			label="telefoneSolicitante"
			property="nrDddSolicitante"
			dataType="text"
			maxLength="3"
			size="5"
			labelWidth="15%"
			width="7%"
			disabled="true"
		/>
		<adsm:textbox
			property="nrTelefoneSolicitante"
			dataType="text"
			maxLength="10"
			size="15"
			disabled="true"
			width="18%"
		/>

		<adsm:hidden property="usuarioSolicitador.idUsuario"/>
		
		<adsm:section caption="aprovacaoReprovacao"/>
		<adsm:textbox dataType="text" property="acao.usuario.nrMatricula" label="responsavel" size="10" labelWidth="23%" width="11%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="acao.usuario.nmUsuario" size="20" disabled="true" width="21%" serializable="false"/>

		<adsm:combobox property="tpSituacaoContratacao" domain="DM_SITUACAO_SOLICITACAO_CONTRATACAO" label="situacao" labelWidth="23%" width="77%" onlyActiveValues="true" disabled="true"/>
		<adsm:textbox dataType="JTDateTimeZone" property="acao.dhAcao" label="data" maxLength="10" labelWidth="23%" width="77%" disabled="true" serializable="false"/>
		<adsm:textarea property="acao.obAcao" disabled="true" label="motivo" maxLength="800" rows="2" labelWidth="23%" width="77%" columns="60" serializable="false"/>

<tr>
	<td colspan="100">
<%-- INICIO VIAGEM --%>
<div id="sectionViagem" style="display:none;border:none">
	<script type="text/javascript">
<!--
		document.write(geraColunas());
		//-->
	</script>
		<adsm:section caption="viagem"/>
		<adsm:combobox
			label="tipoRota"
			property="tpRotaSolicitacao"
			onchange="return changeTpRotaSolicitacao(false, true);"
			domain="DM_TIPO_ROTA_SOLICITACAO"
			required="true"
			labelWidth="23%"
			width="32%"
			onlyActiveValues="true"
			disabled="true"
		/>
		<adsm:combobox required="true" property="tpFluxoContratacao" onchange="changeTpFluxoContratacao(this);" domain="DM_FLUXO_CONTRATACAO" label="tipoFluxoContratacao" labelWidth="18%" width="27%" onlyActiveValues="true" disabled="true" defaultValue="O" />

		<adsm:checkbox property="blEnvolveParceira" label="envolveParceira" labelWidth="23%" width="32%" disabled="true" onclick="blEnvolveParceiraClick();" />

		<adsm:textbox dataType="JTDate" property="dtViagem" label="dataViagem" maxLength="10" labelWidth="18%" width="22%" required="true" onchange="findVlSugerido(); return true;"/>

		<adsm:listbox property="rotas" optionProperty="sgFilial" optionLabelProperty="dsRota" label="rotaEventual" size="4" onContentChange="contentChangeRota"
				labelWidth="23%" width="32%" showOrderControls="false" boxWidth="180" showIndex="false" serializable="true">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="true"
					dataType="text" size="3" maxLength="3" minLengthForAutoPopUpSearch="2" action="/municipios/manterFiliais"
					service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupFilial">
				<adsm:propertyMapping criteriaProperty="empresaRota.tpEmpresa" modelProperty="empresa.tpEmpresa" />
				<adsm:propertyMapping criteriaProperty="empresaRota.flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="empresaRota.flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="empresaRota.tpAcesso" modelProperty="tpAcesso" inlineQuery="true" />
				<adsm:propertyMapping criteriaProperty="dtAtual" modelProperty="historicoFiliais.vigenteEm" />
				<adsm:propertyMapping relatedProperty="rotas_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>

		<adsm:lookup
			label="rotaExpressa"
			property="rotaIdaVolta"
			idProperty="idRotaIdaVolta"
			criteriaProperty="nrRota"
			service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupRotasViagem"
			action="/municipios/consultarRotas"
			dataType="integer"
			onDataLoadCallBack="dataLoadRotaIdaVolta"
			onPopupSetValue="popUpRotaIdaVolta"
			cellStyle="vertical-align:top;"
			labelStyle="vertical-align:top;"
			size="4"
			maxLength="4"
			exactMatch="false"
			labelWidth="12%"
			onchange="return changeRotaIdaVolta();"
			width="33%" mask="0000"
			cmd="idaVolta"
			disabled="false"
		>
			<adsm:propertyMapping relatedProperty="rotaIdaVolta.dsRota" modelProperty="rota.dsRota"/>
			<adsm:textbox dataType="text" property="rotaIdaVolta.dsRota" size="30" disabled="true" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialOrigem.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filialOrigem.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialOrigem.nmFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="sim" modelProperty="vigentes"/>
		</adsm:lookup>

		<adsm:hidden property="sim" value="S" serializable="false"/>

		<adsm:textbox dataType="integer" property="nrDistancia" label="distancia" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true" unit="km2"/>

		<adsm:textbox dataType="text" property="hrTempViagem" label="tempoViagem" unit="h" maxLength="20" size="20" labelWidth="12%" width="33%" disabled="true"/>
		
		<adsm:textbox dataType="currency" property="vlPostoPassagem" label="valorPostoPassagem" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlFreteReferencia" label="freteValorReferencia" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlFreteSugerido" label="freteValorSugerido" maxLength="20" size="20" labelWidth="23%" width="32%" onchange="return changeVlFreteSugerido(this);" />
		<adsm:textbox dataType="currency" property="vlFreteMaximoAutorizado" label="freteValorAutorizadoAte" maxLength="20" size="20" labelWidth="21%" width="24%" disabled="true" onchange="return changeVlFreteMaximoAutorizado(this);" />
		<adsm:textbox dataType="currency" property="vlFreteNegociado" label="freteValorNegociado" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true "/>
		<adsm:textbox dataType="currency" property="vlPremio" label="valorPremio" maxLength="20" size="20" labelWidth="21%" width="24%" />
	
	</table>
</div>
<%-- FIM VIAGEM --%>
<%-- INICIO PUXADA --%>
<div id="sectionPuxadas" style="display:none;border:none">
	<script type="text/javascript">
<!--
		document.write(geraColunas());
		//-->
	</script>		
		<adsm:section caption="puxadas"/>	
		<adsm:textbox dataType="currency"  property="vlFreteSugeridoPuxada" label="freteValorSugerido" maxLength="20" size="20" labelWidth="23%" width="32%" onchange="return changeVlFreteSugerido(this);" />
		<adsm:textbox dataType="currency"  property="vlFreteMaximoAutorizadoPuxada" label="freteValorAutorizadoAte" maxLength="20" size="20" labelWidth="21%" width="24%" disabled="true" onchange="return changeVlFreteMaximoAutorizado(this);" />
		<adsm:textbox dataType="currency"  property="vlFreteNegociadoPuxada" label="freteValorNegociado" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true"/>
		<adsm:textbox dataType="currency"  property="vlPremioPuxada" label="valorPremio" maxLength="20" size="20" labelWidth="21%" width="24%"  disabled="true" />	
	</table>
</div>
<%-- FIM PUXADA --%>
<%-- INICIO COLETA/ENTREGA --%>
<div id="sectionColetaEntrega" style="display:none;border:none">
	<script type="text/javascript">
		<!--
		document.write(geraColunas());
		//-->
	</script>
		<adsm:section caption="coletaEntrega"/>
		<adsm:multicheckbox property="tabelaColetaEntrega.blDomingo|tabelaColetaEntrega.blSegunda|tabelaColetaEntrega.blTerca|tabelaColetaEntrega.blQuarta|tabelaColetaEntrega.blQuinta|tabelaColetaEntrega.blSexta|tabelaColetaEntrega.blSabado|"
				labelWidth="23%" width="35%" label="frequenciaValidade" texts="dom|seg|ter|qua|qui|sex|sab" align="top"/>

		<adsm:textbox dataType="JTTime" labelWidth="22%" width="20%" cellStyle="verticalAlignment:middle" label="horarioInicio" property="tabelaColetaEntrega.hrDiariaInicial"/>
		<adsm:hidden property="tabelaColetaEntrega.idTabelaColetaEntrega"/>

		<adsm:range label="periodoContratacao" labelWidth="23%" width="77%">
			<adsm:textbox dataType="JTDate" property="tabelaColetaEntrega.dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="tabelaColetaEntrega.dtVigenciaFinal"/>
		</adsm:range>
<%-- FIM COLETA/ENTREGA --%>
</table>
</div>
	</td>
		</tr>
<tr>
	<td colspan="100">
<%-- MEIO TRANSPORTE --%>
<div id="meioTransporte" style="display:none;border:none">
	<script type="text/javascript">
		<!--
		document.write(geraColunas());
		//-->
	</script>

		<adsm:section caption="meioTransporteContratado"/>
		
		<adsm:textbox dataType="text" property="proprietario.identificacao" label="proprietario" size="20" labelWidth="23%" width="20%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="proprietario.nmProprietario" size="50" disabled="true" width="51%" serializable="false"/>
		
		
		<adsm:textbox size="7" disabled="true" dataType="text" property="nrIdentificacaoMeioTransp.nrFrota" label="meioTransporte" labelWidth="23%" width="8%"/>
		<adsm:lookup service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findMeioTransporteAprovado"
			property="nrIdentificacaoMeioTransp" idProperty="anoFabricacao" action="f" picker="false" criteriaProperty="nrPlaca"
			dataType="text" maxLength="25" size="20" onDataLoadCallBack="dataLoadMeioTransporte" width="24%" allowInvalidCriteriaValue="true">
			<adsm:propertyMapping relatedProperty="nrIdentificacaoMeioTransp.nrFrota" modelProperty="nrFrota"/>
			<adsm:propertyMapping relatedProperty="nrAnoFabricacaoMeioTransporte" modelProperty="anoFabricacao"/>
		</adsm:lookup>	

		<adsm:textbox dataType="integer" label="anoFabricacao" labelWidth="21%" width="24%" size="10" maxLength="4" property="nrAnoFabricacaoMeioTransporte"/>

		<adsm:textbox size="7" disabled="true" dataType="text" property="nrIdentificacaoSemiReboque.nrFrota" label="semiReboque" labelWidth="23%" width="8%"/>

		<adsm:lookup service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findMeioTransporteAprovado"
			property="nrIdentificacaoSemiReboque" idProperty="anoFabricacao" action="f" picker="false" criteriaProperty="nrPlaca"
			dataType="text" maxLength="25" size="20" onDataLoadCallBack="dataLoadSemiReboque" width="24%" allowInvalidCriteriaValue="true">
			<adsm:propertyMapping relatedProperty="nrIdentificacaoSemiReboque.nrFrota" modelProperty="nrFrota"/>
			<adsm:propertyMapping relatedProperty="nrAnoFabricacaoMeioTransporteSemiReboque" modelProperty="anoFabricacao"/>
		</adsm:lookup>	
		<adsm:textbox dataType="integer" label="anoFabricacaoSemiReboque" labelWidth="21%" width="24%" size="10" maxLength="4" property="nrAnoFabricacaoMeioTransporteSemiReboque"/>
		</td>
</table>
</div>
	</td>
		</tr>
<%-- FIM DO MEIO TRANSPORTE--%>
		<adsm:hidden property="blLoadDefaultData" value="true" serializable="false"/>
		<adsm:buttonBar>
			<adsm:button caption="checkList" id="checkList" onclick="parent.parent.redirectPage('contratacaoVeiculos/manterCheckList.do?cmd=main' + buildLinkPropertiesQueryString_checklist());"/>
			<adsm:button caption="relatorioCargas" id="relatorioCargas" action="carregamento/emitirRelatorioCargas" cmd="main">
				<adsm:linkProperty src="blLoadDefaultData" target="blLoadDefaultData"/>
				<adsm:linkProperty src="filial.idFilial" target="filialLocalizacao.idFilial" disabled="true"/>
				<adsm:linkProperty src="filial.sgFilial" target="filialLocalizacao.sgFilial" disabled="true"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filialLocalizacao.pessoa.nmFantasia" disabled="true"/>
			</adsm:button>

			<adsm:button caption="cancelar" onclick="onClickCancelar()" id="cancelar"/>
			<adsm:button caption="salvar" onclick="store();" id="salvar"/>
			<adsm:newButton id="new"/>
			<adsm:removeButton id="btnRemove"/>
			<adsm:button caption="fechar" id="botaoFechar" onclick="self.close();" disabled="false" style="display: none;"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script type="text/javascript">
	var idMoedaPais;
	var nrMatricula = null;
	var nmUsuario = null;
	var idUsuario = null;
	var id = null;
	var sgFilial = null;
	var nmFilial = null;
	var dtImplantacaoLMS = null;
	//var blFilialMigrada = false;
	var placaTemp = "";
	var tpRotaSolicitacaoOld;
	var errorOnConversion = "";
	var alerta = null;

	function validaParcelaFreteColetaEntrega(){
		idTipomeioTransporte = document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").value;
		tpSolicitacaoContratacao = document.getElementById("tpSolicitacaoContratacao").value;
		var tabGroup = getTabGroup(this.document);
		
		if (tpSolicitacaoContratacao == "C" && idTipomeioTransporte != "" && getElementValue("tpVinculoContratacao") != "A") {
			document.getElementById("sectionColetaEntrega").style.display = "";
			
			if(getElementValue("tpCalculo") == "PA"){
				tabGroup.setDisabledTab("parc",true);	
			}else{
				tabGroup.setDisabledTab("parc",false);				
			}
		} else {
			document.getElementById("sectionColetaEntrega").style.display = "none";
			tabGroup.setDisabledTab("parc", true);
		}	
	}
	
	function changeTpSolicitacaoContratacao(field) {
		var callFindById = (field == undefined);
		if (callFindById) {
			field = document.getElementById("tpSolicitacaoContratacao");
			comboboxChange({e:field});
		} else if (field == false) {
			field = document.getElementById("tpSolicitacaoContratacao");
			callFindById = true;
		} else {
			comboboxChange({e:field});
		}

		var tabGroup = getTabGroup(this.document);
		document.getElementById("vlFreteSugeridoPuxada").required = "false";
		
		if (field.value == "C") {

			validaParcelaFreteColetaEntrega(field);
			
			setElementValue("tpModal", "R");			
			setDisabled("tpModal",true);
			document.getElementById("rotas").required = "false";
			document.getElementById("blEnvolveParceira").required = "false";
			document.getElementById("sectionViagem").style.display = "none";
			document.getElementById("sectionPuxadas").style.display = "none";
			document.getElementById("dtViagem").required = "false";
			document.getElementById("vlFreteMaximoAutorizado").required = "false";

			if (!callFindById) {
				resetValue("tpRotaSolicitacao");
				changeTpRotaSolicitacao(true, false);
			}
			//1.2
			document.getElementById("tpRotaSolicitacao").required = "false";
			document.getElementById("vlFreteSugerido").required = "false";
			setDisabled("blQuebraMeioTransporte", true);
			setElementValue("blQuebraMeioTransporte", "N");
			setDisabled("controleCarga.idControleCarga", true);
			resetValue("controleCarga.idControleCarga");
			setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
			resetValue("controleCarga.filialByIdFilialOrigem.idFilial");
			
			// LMSA-6319
			resetValue("cargaCompartilhada.tipo");
			setDisabled("cargaCompartilhada.tipo", true);

			notifyElementListeners({e:document.getElementById("tpModal")});
			changeTpModal();

		} else if (field.value == "V") {
			
			getElement("controleCarga.nrControleCarga").required = "false";
			getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "false";
			
			if (getElementValue("tpRotaSolicitacao") != "EX") {
				getElement("rotas").required = "true";
			} else {
				getElement("rotas").required = "false";
			}
			document.getElementById("blEnvolveParceira").required = "true";
			document.getElementById("sectionViagem").style.display = "";
			document.getElementById("sectionColetaEntrega").style.display = "none";
			document.getElementById("sectionPuxadas").style.display = "none";
			document.getElementById("dtViagem").required = "true";

			if (getElementValue("warning") == "") {
				if (getElementValue("tpSituacaoContratacao") != "CR" && getElementValue("behavior") != "9" && getElementValue("behavior") != "10" && getElementValue("idProcessoWorkFlow") != "") {
					document.getElementById("vlFreteMaximoAutorizado").required = "true";
					setDisabled("vlFreteMaximoAutorizado",false);
				} else {
					document.getElementById("vlFreteMaximoAutorizado").required = "false";
					setDisabled("vlFreteMaximoAutorizado",true);
				}
			}
			if (!callFindById) {
				setElementValue("tpRotaSolicitacao", "EV");
				resetValue("rotas");
				changeTpRotaSolicitacao(true, false);
				setFocus(field);
			}
			tabGroup.setDisabledTab("parc",true);
			document.getElementById("vlFreteSugerido").required = "true";
			document.getElementById("tpRotaSolicitacao").required = "true";
			if (getElementValue("idSolicitacaoContratacao") == "") {
				setDisabled("blQuebraMeioTransporte", false);
			}

			if (getElementValue("idSolicitacaoContratacao") == "") {
				var sdo = createServiceDataObject(
					"lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.validateUserPerfilModalAereo",
					"validateUserPerfilModalAereo"
				);
				xmit({serviceDataObjects:[sdo]});
			}
			setDisabled("tpModal", getElementValue("idSolicitacaoContratacao") != "");
			
			// LMSA-6319
			setDisabled("cargaCompartilhada.tipo", getElementValue("idSolicitacaoContratacao") != "");
		
		} else if (field.value == "P") {
			if (getElementValue("idProcessoWorkFlow") != "") {
				if(getElementValue("vlFreteMaximoAutorizadoPuxada")==""){
					document.getElementById("vlFreteMaximoAutorizadoPuxada").required = "true";
					setDisabled("vlFreteMaximoAutorizadoPuxada",false);
					setFocus("vlFreteMaximoAutorizadoPuxada");
					document.getElementById("vlFreteNegociadoPuxada").required = "false";
					setDisabled("vlFreteNegociadoPuxada",true);
				}else {
					document.getElementById("vlFreteMaximoAutorizadoPuxada").required = "false";
					setDisabled("vlFreteMaximoAutorizadoPuxada",true);
					document.getElementById("vlFreteNegociadoPuxada").required = "true";
					setDisabled("vlFreteNegociadoPuxada",false);
					setFocus("vlFreteNegociadoPuxada");
					setDisabled("vlPremioPuxada",false);
				}
			} else {
				document.getElementById("vlFreteMaximoAutorizadoPuxada").required = "false";
				setDisabled("vlFreteMaximoAutorizadoPuxada",true);
				setDisabled("vlFreteNegociadoPuxada",true);
				setDisabled("vlPremioPuxada",true);
			}

			setDisabled("tpVinculoContratacao", false);

			setElementValue("tpModal", "R");			
			setDisabled("tpModal",true);			
			setDisabled("blQuebraMeioTransporte",false);			
			tabGroup.setDisabledTab("parc",true);			
			document.getElementById("sectionPuxadas").style.display = "";			
			document.getElementById("sectionViagem").style.display = "none";			
			document.getElementById("sectionColetaEntrega").style.display = "none";	
			document.getElementById("vlFreteSugerido").required = "true";
			document.getElementById("dtViagem").required = "false";
			document.getElementById("tpRotaSolicitacao").required = "false";
			document.getElementById("vlFreteSugerido").required = "false";
			document.getElementById("vlFreteSugeridoPuxada").required = "true";
			document.getElementById("vlFreteMaximoAutorizado").required = "false";
			document.getElementById("rotas").required = "false";
			document.getElementById("blEnvolveParceira").required = "false";

			// LMSA-6319
			resetValue("cargaCompartilhada.tipo");
			setDisabled("cargaCompartilhada.tipo", true);

			changeQuebraMeioTransporte();
		} else {
			document.getElementById("rotas").required = "false";
			document.getElementById("blEnvolveParceira").required = "false";
			document.getElementById("sectionViagem").style.display = "none";
			document.getElementById("sectionColetaEntrega").style.display = "none";
			document.getElementById("sectionPuxadas").style.display = "none";
			tabGroup.setDisabledTab("parc",true);
			setElementValue("tpVinculoContratacao","");
			setDisabled("tpVinculoContratacao",false);

			setElementValue("tpModal", "");
			setDisabled("tpModal",false);
		}

		resetFluxoContratacao(false, true);
		validaDataImplantacaoMerger();
	}	
	

	function validateUserPerfilModalAereo_cb(data){
		//LMS-3187 / Solicitação de contratação aerea esta atrapalhando a regra
		setElementValue("tpModal", "R");
		if(data.permiteModalAereo != "true") {
			setDisabled("tpModal", true);
		}
		
		notifyElementListeners({e:document.getElementById("tpModal")});
		changeTpModal();	

	}

	function changeTpModal(){
		if(getElementValue("tpModal") == "A"){
			
			setElementValue("tpVinculoContratacao","P");
			setDisabled("tpVinculoContratacao",true);
			
			setDisabled("tpModal", false);
			document.getElementById("meioTransporte").style.display = "";

			setElementValue("vlFreteSugerido", "0,01");
			setElementValue("vlFreteNegociado", "0,01");
			setElementValue("vlFreteMaximoAutorizado", "0,01");
			
			setDisabled("vlFreteSugerido", true);
			setDisabled("vlFreteNegociado", true);
			setDisabled("vlFreteMaximoAutorizado", true);
			
			// LMSA-6160
			setDisabled("cargaCompartilhada.tipo", true);
			
		} else {

			if (getElementValue("idSolicitacaoContratacao") == "") {
				resetValue("vlFreteSugerido");
				resetValue("vlFreteNegociado");
				resetValue("vlFreteMaximoAutorizado");
				
				setDisabled("vlFreteSugerido", false);				
				
				if(getElementValue("tpSolicitacaoContratacao") == "V" || (getElementValue("tpSolicitacaoContratacao") == "C" && getElementValue("tpVinculoContratacao") == "A" )){
					document.getElementById("meioTransporte").style.display = "";	
				}				
				else{
					if( getElementValue("tpCalculo") == "PA"){
						document.getElementById("meioTransporte").style.display = "";
					}else{
						document.getElementById("meioTransporte").style.display = "none";															
					}
				}
			}
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" ||
				(eventObj.name == "tab_click" && 
				eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq") ||
				eventObj.name == "removeButton") {
			tpVinculo = undefined;
			stateNew();

			//Quest CQPRO00026748 
			filialMigrada_cb();

			var tabGroup = getTabGroup(this.document);
			tabGroup.getTab("parc").changed = false;
		} else if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq" || eventObj == "selectTab") {
			behavior(true);
			var tabGroup = getTabGroup(this.document);
			var changedParc = tabGroup.getTab("parc").changed;
			var changedFluxo = tabGroup.getTab("fluxoContratacao").changed;
			if (changedParc || changedFluxo) {
				tabGroup.getTab("cad").changed = true;
			}
		} else {
			var tabGroup = getTabGroup(this.document);
			tabGroup.getTab("parc").changed = false;
			tabGroup.getTab("fluxoContratacao").changed = false;
			notifyElementListeners({e:document.getElementById("tpModal")});
			changeTpModal();
		}

		/*
		 * FIXME - framework deveria cuidar disso
		 */
		document.getElementById('acao.obAcao').attachEvent('onfocus',
			function(e){
				if(e.shiftKey){
					document.getElementById('nrTelefoneSolicitante').focus();
				} else {
					try{
						document.getElementById('tpRotaSolicitacao').focus();
					} catch(e){
						try{
							document.getElementById('tabelaColetaEntrega.blDomingo').focus();
						} catch(e){
							try{
								document.getElementById('nrIdentificacaoMeioTransp.nrFrota').focus();
							}catch(e){
							}
						}
					}
				}
			}
		);
	}

	function stateNew() {
		idMoedaPais = undefined;
		setDisabled(document,false);
		setDisabled("dtCriacao",true);
		setDisabled("usuarioSolicitador.nrMatricula",true);
		setDisabled("usuarioSolicitador.nmUsuario",true);
		setDisabled("usuarioSolicitador.idUsuario",true);
		setDisabled("tpSituacaoContratacao",true);
		
		/* LMSA-6319 */
		setDisabled("cargaCompartilhada.tipo",true);
		
		setDisabled("acao.usuario.nrMatricula",true);
		setDisabled("acao.usuario.nmUsuario",true);
		setDisabled("acao.dhAcao",true);
		setDisabled("acao.obAcao",true);

		setDisabled("vlFreteReferencia",true);
		setDisabled("vlFreteNegociado",true);
		setDisabled("vlPremio",true);
		setDisabled("nrIdentificacaoMeioTransp.nrFrota",true);
		setDisabled("nrIdentificacaoSemiReboque.nrFrota",true);
		setDisabled("checkList",true);
		setDisabled("relatorioCargas",true);
		setDisabled("cancelar",true);
		setDisabled("btnRemove",true);
		setDisabled("nrSolicitacaoContratacao",true);
		setDisabled("filial.pessoa.nmFantasia",true);
		setDisabled("rotas_nmFilial",true);
		setDisabled("blEnvolveParceira",true);
		setDisabled("nrDistancia",true);
		setDisabled("hrTempViagem",true);
		setDisabled("vlPostoPassagem",true);
		setDisabled("compostoPor",true);		
		document.getElementById("meioTransporte").style.display = "none";
		setDisabled("proprietario.identificacao",true);
		setDisabled("proprietario.nmProprietario",true);

		if (document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").masterLink == "true") {
			setDisabled("nrIdentificacaoMeioTransp.nrPlaca",true);
			setDisabled("nrIdentificacaoSemiReboque.nrPlaca",true);
		}

		writeDataSession();
		changeTpSolicitacaoContratacao();

		var tabGroup = getTabGroup(this.document);
		if (tabGroup.getTab("parc") && tabGroup.getTab("parc").getDocument() &&
			tabGroup.getTab("parc").getDocument().getElementById("ParcelaTabelaCe.dataTable") && tabGroup.getTab("parc").getDocument().getElementById("ParcelaTabelaCe.dataTable").gridDefinition &&
			tabGroup.getTab("parc").getDocument().getElementById("ParcelaTabelaCe.dataTable").gridDefinition.resetGrid) {
			tabGroup.getTab("parc").getDocument().getElementById("ParcelaTabelaCe.dataTable").gridDefinition.resetGrid();
		}
		resetFluxoContratacao(false, true);
		setElementValue("tpSituacaoContratacao","CR");
		//Inicializa para quando entrar nas tabs, remontar as grids
		setElementValue("reconsultarFilho", "-1");
		setElementValue("tpSolicitacaoContratacao", "V");
		comboboxChange({e:document.getElementById("tpSolicitacaoContratacao")});
		changeTpSolicitacaoContratacao();
		setElementValue("tpRotaSolicitacao", "EV");
		tpRotaSolicitacaoOld = "EV";
		changeTpRotaSolicitacao(true, false);
		
		setDisabled("controleCarga.idControleCarga", true);
		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
		getElement("controleCarga.nrControleCarga").required = "false";
		getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "false";
		
		setElementValue("blQuebraMeioTransporte", "N");
		
		tabGroup.setDisabledTab("anexo",true);		
		
		setFocusOnFirstFocusableField();		
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);

		if (getElementValue("idTipoMeioTransporteTemp") != ""){
			notifyElementListeners({e:document.getElementById("tpModal")});
			changeTpModal();
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").masterLink = "true";
			setDisabled("tipoMeioTransporte.idTipoMeioTransporte", true);
		}
		
		document.getElementById("nrIdentificacaoSemiReboque.nrPlaca").serializable = "true";
		document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").serializable = "true";

		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1,null,true);
			getTabGroup(this.document).setDisabledTab("pesq",true);
		}

		validaDataImplantacaoMerger();
	}
	
	function myPageLoad() {
		onPageLoad();
		if(!self.window.dialogArguments) {
			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findDataSession", "dataSession", null);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function validaDataImplantacaoMerger(){
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.validaDataImplantacaoMerger", "validaDataImplantacaoMerger", null);
		xmit({serviceDataObjects:[sdo]});
	}

	function validaDataImplantacaoMerger_cb(data, error){
		if (error != undefined){
			alert(error);
		}else{
			if (getNestedBeanPropertyValue(data,"mergerImplantado") == "true"){
				setDisabled("blEnvolveParceira", true);
			}	
			
			if (getNestedBeanPropertyValue(data,"calculoPadrao") == "true"){
				if (isBlank(getElementValue("idSolicitacaoContratacao"))) {
					setElementValue("tpCalculo","PA");
				}
			}
		}
	}

	function setDtImplantacaoLMS(data) {
		dt = new Date();
		mes = dt.getMonth()+1;
		hoje = dt.getFullYear()+"-"+mes+"-"+dt.getDate();

		//Controlará se foi digitado a sigla da filial
		chLocal = false;
				
		//Conforme a origem da chamada à função, 'data' pode ser um array ou não
		if(data != null) {
			//cfe. o formato de 'data' sei se foi digitado a sigla
			if (data[0] != null) {
				dtImplantacaoLMS = getNestedBeanPropertyValue(data[0],"dtImplantacaoLMS");
				chLocal = true;
			} else {
				dtImplantacaoLMS = getNestedBeanPropertyValue(data,"dtImplantacaoLMS");
			}
		} else if (document.getElementById("filial.dtImplantacaoLMS").value != null) {
			dtImplantacaoLMS = document.getElementById("filial.dtImplantacaoLMS").value;
		}
	}
	
	//Função chamada da opção 'Manter Solicitações de Contratação'
	//Verifica se a filial já foi migrada e libera ou trava botão 'checklist' e dois combos: 'tpSolicitacao' e 'rota' (Quest CQPRO00026748)
	function filialMigrada_cb(data, error) {
		var rota = getElement("tpRotaSolicitacao");
		var tipoSolicitacao = document.getElementById("tpSolicitacaoContratacao");

		setDtImplantacaoLMS(data);
		
		if (dtImplantacaoLMS != null && dtImplantacaoLMS <= hoje && dtImplantacaoLMS != "") {
			//Com alguns usuários o setDisabled não funciona 
			//setDisabled("checkList", false);
			//setDisabled(rota, false);
			//setDisabled(tipoSolicitacao, false);
			
			document.getElementById("checkList").removeAttribute("disabled");
			getElement("tpRotaSolicitacao").removeAttribute("disabled");
			document.getElementById("tpSolicitacaoContratacao").removeAttribute("disabled");
		} else {
			//setDisabled("checkList", true);
			//setDisabled(rota, true);
			//setDisabled(tipoSolicitacao, true);
			document.getElementById("checkList").setAttribute("disabled", "true");
			getElement("tpRotaSolicitacao").setAttribute("disabled", "true");
			document.getElementById("tpSolicitacaoContratacao").setAttribute("disabled", "true");
		}

		//Atende a requisito da tag 'onDataLoadCallBack' e só entra se foi DIGITADA a sigla da filial.
		//Senão é porque ou está entrando/chegando na TAB ou foi clicado na lupa
		if (chLocal) {
			if (error != undefined) {
				alert(error);
				return false;
			}
			var r = filial_sgFilial_exactMatch_cb(data);

			return r;
		}
	}

	//Função chamada da opção 'Workflow / Cadastros Gerais / Manter Ações / Visualizar Processo'
	//retorna v/f se migrado ou não. Para habilitar botão 'checklist' (Quest CQPRO00026748)
	function filialMigrada(data, error) {

		setDtImplantacaoLMS();
		
		if (dtImplantacaoLMS != null && dtImplantacaoLMS <= hoje && dtImplantacaoLMS != "") {
			return true;
		} else {
			return false;
		}
	}

	
	function setaTipoMeioTransporte_cb(data, error){
		tipoMeioTransporte_idTipoMeioTransporte_cb(data);
		if (getElementValue("idTipoMeioTransporteTemp") != ""){
			setComboBoxElementValue(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"), getElementValue("idTipoMeioTransporteTemp"),
									getElementValue("idTipoMeioTransporteTemp"), getElementValue("dsTipoMeioTransporteTemp"));
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").onchange();
	}
	}

	function setaQtdEixos_cb(data, error){
		eixosTipoMeioTransporte_qtEixos_cb(data);
		if (getElementValue("qtdeixosTemp") != ""){
			var qtdEixos = getElementValue("qtdeixosTemp");
			for (var i = 0 ; i < document.getElementById("eixosTipoMeioTransporte.qtEixos").options.length ; i++){
				if(document.getElementById("eixosTipoMeioTransporte.qtEixos").options[i].value == qtdEixos){
					document.getElementById("eixosTipoMeioTransporte.qtEixos").selectedIndex = i;
					break
				}
			}
			setElementValue("qtdeixosTemp", "");
		}
	}

	function dataSession_cb(data) {
		nrMatricula = getNestedBeanPropertyValue(data,"usuario.nrMatricula");
		nmUsuario = getNestedBeanPropertyValue(data,"usuario.nmUsuario");
		idUsuario = getNestedBeanPropertyValue(data,"usuario.idUsuario");
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		dtImplantacaoLMS = getNestedBeanPropertyValue(data,"filial.dtImplantacaoLMS");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		getElement("dtAtual").masterLink = "true";
		setElementValue("dtAtual", data.dtAtual);
		writeDataSession();
	}

	function writeDataSession() {
		if (getElementValue("idSolicitacaoContratacao") == "") {
			if (nrMatricula != null &&
				nmUsuario != null &&
				idUsuario != null) {
				setElementValue("usuarioSolicitador.nrMatricula",nrMatricula);
				setElementValue("usuarioSolicitador.nmUsuario",nmUsuario);
				setElementValue("usuarioSolicitador.idUsuario",idUsuario);
			}
			if (idFilial != null &&
				sgFilial != null &&
				nmFilial != null) {
				setElementValue("filial.idFilial",idFilial);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFilial);
				setElementValue("filial.dtImplantacaoLMS", dtImplantacaoLMS);
				notifyElementListeners({e:document.getElementById("filial.idFilial")});
			}
		}
	}

	function behavior(notDisabledSemiReboque) {
		if (notDisabledSemiReboque == undefined) {
			setDisabled("nrIdentificacaoSemiReboque.nrPlaca", true);
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque", true);
		}

		var behavior = getElementValue("behavior");

		switch (behavior) {
			case "0" :						
				if (getElementValue("tpSituacaoContratacao") == "SA" && getElementValue("tpCalculo") != "PA"){
					document.getElementById("meioTransporte").style.display = "none";
				}else{
					document.getElementById("meioTransporte").style.display = "";
				}
				setDisabled(document, true);
				setDisabled("new", false);
				setFocus("new", false);
				break;
			case "10" :
				setDisabled(document, true);
				setDisabled("new", false);
				setFocus("new", false);
				document.getElementById("meioTransporte").style.display = "";
				if (document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").masterLink != "true") {
					setDisabled("nrIdentificacaoMeioTransp.nrPlaca", true);
					setDisabled("nrAnoFabricacaoMeioTransporte", true);
				}
				if(filialMigrada()) {
					document.getElementById("checkList").removeAttribute("disabled");
				} else {					
					document.getElementById("checkList").setAttribute("disabled", "true");
				}

				alerta = getElementValue("emailInvalido");

				//lms-233
				if(alerta != ""){
					alert(alerta);
				}
				
				break;
			case "9" :
				if (notDisabledSemiReboque == undefined){
					setDisabled(document,true);
				}
				document.getElementById("meioTransporte").style.display = "";
				setDisabled("vlFreteNegociado",false);
							
				setDisabled("salvar", false);
				setDisabled("new", false);
				setDisabled("cancelar", false);
				setDisabled("btnRemove", false);
				setDisabled("vlFreteMaximoAutorizado", true);
				setFocus("vlFreteNegociado");
				break;
			case "8" :
				setDisabled(document,true);
				if (getElementValue("warning") == "") {
					setDisabled("cancelar", false);
					setDisabled("btnRemove", false);
					setDisabled("new", false);
					if (getElementValue("idProcessoWorkFlow") != "") {
						setDisabled("vlFreteMaximoAutorizado",false);
						setFocus("vlFreteMaximoAutorizado");
						setDisabled("salvar", false);
					}else{
						setDisabled("vlFreteMaximoAutorizado",true);
						setFocus("new",false);
					}			
					
					document.getElementById("meioTransporte").style.display = "";	
					
					if (document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").masterLink != "true") {
						setDisabled("nrIdentificacaoMeioTransp.nrPlaca",true);
						setDisabled("nrAnoFabricacaoMeioTransporte",true);
					}
				}
				break;
			default :
				if (isBlank(getElementValue("idSolicitacaoContratacao"))) {
					setDisabled("salvar", false);
				}
		}

		if (getElementValue("tpSituacaoContratacao") == "AP"
			|| getElementValue("tpSituacaoContratacao") == "RE"
			|| getElementValue("tpSituacaoContratacao") == "CA"
			|| getElementValue("warning") != ""
			|| getElementValue("idSolicitacaoContratacao") == "") {
			setDisabled("cancelar", true);
		}

		if(!isBlank(getElementValue("idSolicitacaoContratacao"))) {
			if (getElementValue("tpSituacaoContratacao") == "AP"
				|| getElementValue("tpSituacaoContratacao") == "NE") {
				//setDisabled("checkList", false);
				setDisabled("relatorioCargas", true);
			} else {
				//setDisabled("checkList", true);
				setDisabled("relatorioCargas", false);
			}
		}

		if (getElementValue("idProcessoWorkFlow") != "") {
			setDisabled("cancelar", true);
			setDisabled("new", true);
			setDisabled("btnRemove", true);
			setDisabled("relatorioCargas", true);
		}

		if (getElementValue("warning") == "true") {
			setDisabled(document, true);
		}

		//Teste para verificar se a janela foi aberta como modal
		if(self.window.dialogArguments) {
			getElement("botaoFechar").style.display = "inline";
			setDisabled("botaoFechar", false);
		} else {
			getElement("botaoFechar").style.display = "none";
		}
	}

	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	
	var tpVinculo;
	function dataLoad_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		tpVinculo = data.tpVinculoContratacao;
		//qtdEixos = data.eixosTipoMeioTransporte.qtEixos;
		setElementValue("qtdeixosTemp", data.eixosTipoMeioTransporte.qtEixos);
		setElementValue("idSolicitacaoContratacao", data.idSolicitacaoContratacao);
		
		stateNew();
		idMoedaPais = data.moedaPais.idMoedaPais;
		setElementValue("filial.idFilial", data.filial.idFilial);
		notifyElementListeners({e:getElement("filial.idFilial")});
		onDataLoad_cb(data, exception);
		getElement("tpRotaSolicitacao").required = "false";
		
		
		behavior();
		idMoedaPais = data.moedaPais.idMoedaPais;
		
		if (getElementValue("behavior") != "10" && getElementValue("behavior") != "0" && 
				getElementValue("behavior") != "8") {
			changeTipoMeioTransporte(getElement("tipoMeioTransporte.idTipoMeioTransporte"));
		}
		if (getElementValue("behavior") == "9"){
			var enableMeiosTransporte = tpVinculo == "E" && getElementValue("tpSolicitacaoContratacao") == "C";
			
			setDisabled("nrIdentificacaoMeioTransp.nrPlaca",!enableMeiosTransporte);
			setDisabled("nrAnoFabricacaoMeioTransporte",!enableMeiosTransporte);
			setDisabled("nrIdentificacaoSemiReboque.nrPlaca", !enableMeiosTransporte);
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque", !enableMeiosTransporte);
		}
		
		

		findVlSugerido();
		if (getElementValue("idProcessoWorkFlow") == "") {
			var tpSituacaoContratacao = data.tpSituacaoContratacao.value;
			setDisabled("btnRemove", (tpSituacaoContratacao == "SA"));
		}
		setElementValue("compostoPor", data.dsComposto);
		setElementValue("moedaPais.idMoedaPais", idMoedaPais);
		
		var idTabelaColetaEntrega = getTabGroup(this.document).getTab("cad").getFormProperty("tabelaColetaEntrega.idTabelaColetaEntrega");
		if(idTabelaColetaEntrega != undefined && idTabelaColetaEntrega != null && idTabelaColetaEntrega != ""){
			findGridParcelas(idTabelaColetaEntrega);
		}

		var idSolicitacaoContratacao = getNestedBeanPropertyValue(data, "idSolicitacaoContratacao");
		
		if (idSolicitacaoContratacao != undefined){
			disableTabAnexos(false);
		} else {
			disableTabAnexos(true);
		}
		
		if (getNestedBeanPropertyValue(data,"calculoPadrao") == "true"){
			setElementValue("tpCalculo","PA");
		}else{
			resetValue("tpCalculo");
		}
		
		enableTpVinculoBySituacaoContratacao(data);
	}
	
	function enableTpVinculoBySituacaoContratacao(data){
		var sgFilialLogada = getElementValue("filial.sgFilial");
		if (sgFilialLogada!= undefined && sgFilialLogada == "MTZ"){
			setDisabled("tpVinculoContratacao",false);
		}else{
			if (data.tpSolicitacaoContratacao!= undefined && data.tpSolicitacaoContratacao == "C"){
				var tpSituacaoContratacao = data.tpSituacaoContratacao;

				if (tpSituacaoContratacao == "AP" || tpSituacaoContratacao == "AN"){
					setDisabled("tpVinculoContratacao",true);
					
					if(tpSituacaoContratacao == "AP"){
						setDisabled("nrIdentificacaoMeioTransp.nrPlaca",true);
						setDisabled("nrIdentificacaoSemiReboque.nrPlaca",true);
						setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",true);
						setDisabled("nrAnoFabricacaoMeioTransporte",true);
					}
				}
			}
	
		}
	}
	

	function findVlSugerido() {
		if (getElementValue("tpSolicitacaoContratacao") == "V") {
			if (getElementValue("tpRotaSolicitacao") == "EX") {
				findVlSugeridoToRotaExpressa(getElementValue("rotaIdaVolta.idRotaIdaVolta"));
			} else findVlSugeridoByRotaAndTpMeioTransporte();
		}
	}

	function store() {
		if (errorOnConversion.length > 0) {
			alert(errorOnConversion);
		} else {
			var tabGroup = getTabGroup(this.document);
				if (getElementValue("tpSolicitacaoContratacao") == "C") {
				var tabParcela = tabGroup.getTab("parc");
				storeEditGridScript('lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.store', 'storeCustom', document.forms[0], tabParcela.getDocument().forms[0])
			} else if (getElementValue("tpSolicitacaoContratacao") == "V") {
				var tabFluxoContratacao = tabGroup.getTab("fluxoContratacao");
				storeEditGridScript('lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.store', 'storeCustom', document.forms[0], tabFluxoContratacao.getDocument().forms[0])
			} else {
				storeButtonScript('lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.store', 'storeCustom', document.forms[0])
			}
		}
	}
	
	function findGridParcelas(idTabelaColetaEntrega){
		var idTipoMeioTransporte = getTabGroup(this.document).getTab("cad").getFormProperty("tipoMeioTransporte.idTipoMeioTransporte");
		var dtVigenciaInicial = dropFormat(getTabGroup(this.document).getTab("cad").getDocument().getElementById("tabelaColetaEntrega.dtVigenciaInicial"));
		var idFilial = getTabGroup(this.document).getTab("cad").getFormProperty("filial.idFilial");
		var tpSituacaoContratacao = getTabGroup(this.document).getTab("cad").getFormProperty("tpSituacaoContratacao");
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findGridParcelas",
				   "findGridParcelas",{idTabelaColetaEntrega:idTabelaColetaEntrega,idTipoMeioTransporte:idTipoMeioTransporte,dtVigenciaInicial:dtVigenciaInicial,idFilial:idFilial,tpSituacaoContratacao:tpSituacaoContratacao});
	        xmit({serviceDataObjects:[sdo]});
	}
	
	function findGridParcelas_cb(data,error){
		getTabGroup(this.document).getTab('parc').tabOwnerFrame.writeRowsGridAndBehavior(data, false);
	}
		

	function storeCustom_cb(data, exception, key) {
		if (exception != undefined) {
			if (key == "LMS-26098" || key == "LMS-26099" || key == "LMS-26100") {
				getTabGroup(this.document).selectTab(3);
				getTabGroup(this.document).getTab("fluxoContratacao").getDocument().parentWindow.onShowTemp();
			}
			if (key == "LMS-26036") {
				getTabGroup(this.document).selectTab(2);
				getTabGroup(this.document).getTab("parc").getDocument().parentWindow.onShowTemp();
			}
			if (key == "LMS-26112") {
				if (confirm(exception)) {
					setElementValue("isAnoConfirmado", "S");
					store();
				} else {
					setElementValue("isAnoConfirmado", "N");
					resetValue("nrIdentificacaoMeioTransp.nrPlaca");
					resetValue("nrAnoFabricacaoMeioTransporte");
				}
				return;
			}
			alert(exception);
			return;
		}

		if (getElementValue("idSolicitacaoContratacao") == "" && getElementValue("tpModal") != "A" ) {
			alert(i18NLabel.getLabel("LMS-00065"));
		}

		var excedeuSugerido = getNestedBeanPropertyValue(data, "excedeuSugerido");
		if (excedeuSugerido != undefined && excedeuSugerido == "true") {
			alert(i18NLabel.getLabel("LMS-26075"));
		}

		store_cb(data, exception);
		behavior(true);

		var list = getNestedBeanPropertyValue(data, "Parcela");
		if (list != undefined && list.length > 0) {
			var tabGroup = getTabGroup(this.document);
			var tabParcela = tabGroup.getTab("parc");
			for (var x = 0; x < list.length; x++) {
				setElementValue(tabParcela.getDocument().getElementById("ParcelaTabelaCe:" + x + ".id"),getNestedBeanPropertyValue(list[x],"id"));
				setElementValue(tabParcela.getDocument().getElementById("ParcelaTabelaCe:" + x + ".vlFreteReferencia"),setFormat(tabParcela.getDocument().getElementById("ParcelaTabelaCe:" + x + ".vlFreteReferencia"),getNestedBeanPropertyValue(list[x],"vlRef")));
			}
		}

		var fluxos = getNestedBeanPropertyValue(data, "FluxoContratacao");
		if (fluxos != undefined && fluxos.length > 0) {
			var tabGroup = getTabGroup(this.document);
			var tabFluxoContratacao = tabGroup.getTab("fluxoContratacao");
			for (var x = 0; x < fluxos.length; x++) {
				if(tabFluxoContratacao.getDocument().getElementById("FluxoContratacao:" + x + ".id")){
					setElementValue(tabFluxoContratacao.getDocument().getElementById("FluxoContratacao:" + x + ".id"),getNestedBeanPropertyValue(fluxos[x],"id"));
					tabFluxoContratacao.getDocument().getElementById("_FluxoContratacao:" + x + ".nrChaveLiberacao").innerHTML = getNestedBeanPropertyValue(fluxos[x],"nrChaveLiberacao");
				}
			}
		}

		if (getElementValue("idProcessoWorkFlow") == "") {
			setFocus("new", false);
		}
		// verifica se a janela é modal
		if(self.window.dialogArguments) {
			setFocus("botaoFechar", false);
		}
		
		var idSolicitacaoContratacao = getNestedBeanPropertyValue(data, "idSolicitacaoContratacao");
		
		if (idSolicitacaoContratacao != undefined){
			disableTabAnexos(false);
		} else {
			disableTabAnexos(true);
		}
	}

	function contentChangeRota(event) {
		if (event.name == "modifyButton_afterClick"
			|| event.name == "deleteButton_afterClick") {
			findVlSugeridoByRotaAndTpMeioTransporte();
			resetFluxoContratacao(false, true);
		}
		return true;
	}
	
	function findVlSugeridoByRotaAndTpMeioTransporte() {
		errorOnConversion = "";
		if (rotasListboxDef.getData().length <= 1 || getElementValue("tpModal") == "" || (getElementValue("moedaPais.idMoedaPais") == "" && idMoedaPais == null)) {
			resetValue("vlFreteReferencia");
			resetValue("nrDistancia");
			resetValue("hrTempViagem");
			resetValue("vlPostoPassagem");
		}else{
			var rota = "";
			var ids = "";
			var separador = "";
			for (var x = 0; x < rotasListboxDef.getData().length; x++) {
				rota+= separador + getNestedBeanPropertyValue(rotasListboxDef.getData()[x],"filial.sgFilial");
				ids += separador + getNestedBeanPropertyValue(rotasListboxDef.getData()[x],"filial.idFilial");
				separador = "-";
			}
			var sdo = createServiceDataObject(
				"lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findVlSugeridoToRota","onDataLoadSamples",
				{
					idSolicitacaoContratacao: getElementValue("idSolicitacaoContratacao"),
					dsRota: rota,
					idFiliais: ids,
					idTipoMeioTransporte: getElementValue("tipoMeioTransporte.idTipoMeioTransporte"),
					idMoedaPais: ((getElementValue("moedaPais.idMoedaPais") == "") ? idMoedaPais : getElementValue("moedaPais.idMoedaPais")),
					qtEixos: getElementValue("eixosTipoMeioTransporte.qtEixos"),
					dtViagem: getElementValue("dtViagem")
				}
			);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function onDataLoadSamples_cb(data, exception, key) {
		errorOnConversion = "";
		if (exception != undefined) {
			alert(exception);
			if(key == "LMS-27047") {
				errorOnConversion = exception;
			}
			return;
		}
		
		var formElem = document.getElementById(mainForm);
		fillFormWithFormBeanData(formElem.tabIndex, data);

		var url = window.parent.location
		var itens = url.search.split("&");
	
		if(itens.length != 1){
			return;
		}

		filialMigrada_cb();
	}

	function changeRotaIdaVolta() {
		var flag = rotaIdaVolta_nrRotaOnChangeHandler();
		if (getElementValue("rotaIdaVolta.idRotaIdaVolta") == "") {
			resetValue("vlFreteReferencia");
			resetValue("nrDistancia");
			resetValue("hrTempViagem");
			resetValue("vlPostoPassagem");
			resetFluxoContratacao(false, true);
		}
		return flag;
	}

	function dataLoadRotaIdaVolta_cb(data) {
		var flag = rotaIdaVolta_nrRota_exactMatch_cb(data);
		if (data != undefined && data.length == 1) {
			findVlSugeridoToRotaExpressa(getNestedBeanPropertyValue(data[0],"idRotaIdaVolta"));
		}
		resetFluxoContratacao(false, true);
		return flag;
	}

	function findVlSugeridoToRotaExpressa(idRotaIdaVolta) {
		errorOnConversion = "";
		if(idRotaIdaVolta > 0) {
			var sdo = createServiceDataObject(
				"lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findVlSugeridoToRotaExpressa",
				"onDataLoadSamples",
				{
					idSolicitacaoContratacao: getElementValue("idSolicitacaoContratacao"),
					idRotaIdaVolta: idRotaIdaVolta,
					idTipoMeioTransporte: getElementValue("tipoMeioTransporte.idTipoMeioTransporte"),
					idMoedaPais: ((getElementValue("moedaPais.idMoedaPais") == "") ? idMoedaPais : getElementValue("moedaPais.idMoedaPais")),
					qtEixos: getElementValue("eixosTipoMeioTransporte.qtEixos"),
					dtViagem: getElementValue("dtViagem")
				}
			);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function popUpRotaIdaVolta(data) {
		resetFluxoContratacao(data != undefined, true);
		findVlSugeridoToRotaExpressa(getNestedBeanPropertyValue(data,"idRotaIdaVolta"));
		return true;
	}

	function dataLoadMeioTransporte_cb(data, exception) {
		
		if (exception != undefined) {
			alert(exception);
			resetValue("nrIdentificacaoMeioTransp.nrFrota");
			resetValue("nrAnoFabricacaoMeioTransporte");			
			setElementValue("proprietario.identificacao","");
			setElementValue("proprietario.nmProprietario","");
			setElementValue("nrIdentificacaoMeioTransp.nrFrota","");
			setElementValue("nrAnoFabricacaoMeioTransporte","");
			
		}else{
			var flag = nrIdentificacaoMeioTransp_nrPlaca_exactMatch_cb(data);
			setByChave("proprietario.identificacao",data);
			setByChave("proprietario.nmProprietario",data);
			 
			setDisabled("nrAnoFabricacaoMeioTransporte",true);
			if (data != undefined && data.length != 1) {
				resetValue("nrIdentificacaoMeioTransp.nrFrota");
				resetValue("nrAnoFabricacaoMeioTransporte");
				setDisabled("nrAnoFabricacaoMeioTransporte",false);
				setElementValue("nrIdentificacaoMeioTransp.nrPlaca",getElementValue("nrIdentificacaoMeioTransp.nrPlaca").toUpperCase());
			} else {
				if (getElementValue("tipoMeioTransporte.idTipoMeioTransporte") != getNestedBeanPropertyValue(data[0],"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")) {
					alert(i18NLabel.getLabel("LMS-26080"));
					resetValue("nrIdentificacaoMeioTransp.nrFrota"); 
					resetValue("nrIdentificacaoMeioTransp.nrPlaca");
					resetValue("nrAnoFabricacaoMeioTransporte");
					setDisabled("nrAnoFabricacaoMeioTransporte",false);
					setElementValue("proprietario.identificacao","");
					setElementValue("proprietario.nmProprietario","");
					setFocus("nrIdentificacaoMeioTransp.nrPlaca");
				}
				if (getElementValue("tpVinculoContratacao") == "P" && "P" != getNestedBeanPropertyValue(data[0],"tpVinculo")) {
					alert(i18NLabel.getLabel("LMS-26092"));
					resetValue("nrIdentificacaoMeioTransp.nrFrota"); 
					resetValue("nrIdentificacaoMeioTransp.nrPlaca");
					resetValue("nrAnoFabricacaoMeioTransporte");
					setDisabled("nrAnoFabricacaoMeioTransporte",false);
					setFocus("nrIdentificacaoMeioTransp.nrPlaca");
				}
			}
			return flag;			
		}
	}
	
	function setByChave(chave,data){
		setElementValue(chave,getNestedBeanPropertyValue(data[0],chave));
	}

	function dataLoadSemiReboque_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			setElementValue("proprietario.identificacao","");
			setElementValue("proprietario.nmProprietario","");
			resetValue("nrIdentificacaoSemiReboque.nrPlaca"); 
			resetValue("nrIdentificacaoSemiReboque.nrFrota");
			resetValue("nrAnoFabricacaoMeioTransporteSemiReboque");
			setFocus("nrIdentificacaoSemiReboque.nrPlaca");
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",false);		
			
		}else{
			var flag = nrIdentificacaoSemiReboque_nrPlaca_exactMatch_cb(data);
			
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",true);
			if (data != undefined && data.length != 1) {
				setElementValue("proprietario.identificacao","");
				setElementValue("proprietario.nmProprietario","");
				resetValue("nrIdentificacaoSemiReboque.nrFrota");
				setElementValue("nrIdentificacaoSemiReboque.nrPlaca",getElementValue("nrIdentificacaoSemiReboque.nrPlaca").toUpperCase());
				setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",false);
				
			} else {
				if (idComposto != getNestedBeanPropertyValue(data[0],"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")) {
					alert(i18NLabel.getLabel("LMS-26081"));
					setElementValue("proprietario.identificacao","");
					setElementValue("proprietario.nmProprietario","");
					resetValue("nrIdentificacaoSemiReboque.nrPlaca"); 
					resetValue("nrIdentificacaoSemiReboque.nrFrota");
					resetValue("nrAnoFabricacaoMeioTransporteSemiReboque");
					setFocus("nrIdentificacaoSemiReboque.nrPlaca");
					setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",false);
					
				}
				if (getElementValue("tpVinculoContratacao") == "P" && "P" != getNestedBeanPropertyValue(data[0],"tpVinculo")) {
					alert(i18NLabel.getLabel("LMS-26092"));
					setElementValue("proprietario.identificacao","");
					setElementValue("proprietario.nmProprietario","");
					resetValue("nrIdentificacaoSemiReboque.nrPlaca"); 
					resetValue("nrIdentificacaoSemiReboque.nrFrota");
					resetValue("nrAnoFabricacaoMeioTransporteSemiReboque");
					setFocus("nrIdentificacaoSemiReboque.nrPlaca");
					setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",false);
					
				}
			}
			return flag;
		}
	}

	function changeTipoMeioTransporte(field) {
		
		validaParcelaFreteColetaEntrega();
		
		setElementValue("reconsultarFilho", "-1");
		if (getElementValue(field) != undefined) {
			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.validateTipoMeioTransporte",
					"changeTipoMeioTransporte",{id:getElementValue(field)});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	var idComposto = null;
	function changeTipoMeioTransporte_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}

		if (getElementValue("behavior") == "9") {
			return;
		}

		idComposto = getNestedBeanPropertyValue(data,"idComposto");
		if (getElementValue("warning") == "" && idComposto != undefined && idComposto != "") {
			setDisabled("nrIdentificacaoSemiReboque.nrPlaca",false);
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",false);
			setElementValue("compostoPor", getNestedBeanPropertyValue(data, "dsComposto"));
		} else {
			setDisabled("nrIdentificacaoSemiReboque.nrPlaca",true);
			setDisabled("nrAnoFabricacaoMeioTransporteSemiReboque",true);
			setElementValue("compostoPor", "");
		}
	}

	function changeTpRotaSolicitacao(callFindById, blConfirm) {
		var field = getElement("tpRotaSolicitacao");
		var tpRotaSolicitacao = getElementValue(field);
		if(tpRotaSolicitacaoOld != "") {
			if(blConfirm && (!confirmI18nMessage("LMS-26097"))) {
				setElementValue("tpRotaSolicitacao", tpRotaSolicitacaoOld);
				return false;
			}
		}
		tpRotaSolicitacaoOld = tpRotaSolicitacao;
		comboboxChange({e:field});
		if (!callFindById) {
			resetValue("rotas");
			resetValue("blEnvolveParceira");
			blEnvolveParceiraClick();
			resetValue("rotas_filial.idFilial");
			resetValue("rotaIdaVolta.idRotaIdaVolta");
			resetValue("nrDistancia");
			resetValue("hrTempViagem");
			resetValue("vlPostoPassagem");
			resetValue("vlFreteReferencia");
			resetValue("vlFreteSugerido");
		}
		document.getElementById("rotas").required = "false";
		document.getElementById("blEnvolveParceira").required = "false";
		document.getElementById("rotaIdaVolta.idRotaIdaVolta").required = "false";
		setDisabled("rotas", true);
		setDisabled("blEnvolveParceira", true);
		setDisabled("rotas_filial.idFilial", true);
		setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
		setDisabled("rotaIdaVolta.dsRota", true);
		if (tpRotaSolicitacao == "EX") {
			resetValue("rotas");
			setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
			document.getElementById("rotaIdaVolta.nrRota").required = "true";
			document.getElementById("rotaIdaVolta.nrRota").label = document.getElementById("rotaIdaVolta.idRotaIdaVolta").label;
			setDisabled("vlFreteSugerido", true);
		} else if (tpRotaSolicitacao == "EV") {
			rotasListboxDef.cleanRelateds();
			setDisabled("rotas", false);
			setDisabled("blEnvolveParceira", false);
			setDisabled("rotas_filial.idFilial", false);
			setElementValue("rotas_filial.idFilial", getElementValue("filial.idFilial"));
			setElementValue("rotas_filial.sgFilial", getElementValue("filial.sgFilial"));
			setElementValue("rotas_nmFilial", getElementValue("filial.pessoa.nmFantasia"));
			rotasListboxDef.insertOrUpdateOption();
			document.getElementById("rotaIdaVolta.nrRota").required = "false";
			document.getElementById("rotas").required = "true";
			document.getElementById("blEnvolveParceira").required = "true";
			setDisabled("vlFreteSugerido",false);
		} else {
			resetValue("rotas");
			resetValue("blEnvolveParceira");
			blEnvolveParceiraClick();
			resetValue("rotas_filial.idFilial");
			resetValue("rotaIdaVolta.idRotaIdaVolta");
			resetValue("nrDistancia");
			resetValue("hrTempViagem");
			resetValue("vlPostoPassagem");
			resetValue("vlFreteReferencia");
			resetValue("vlFreteSugerido");

			setDisabled("nrDistancia",true);
			setDisabled("hrTempViagem",true);
			setDisabled("vlPostoPassagem",true);
			setDisabled("compostoPor",true);
			setDisabled("vlFreteReferencia",true);
			setDisabled("vlFreteSugerido",true);
		}
		resetFluxoContratacao(false, true);
		validaDataImplantacaoMerger();
	}

	function changeTpFluxoContratacao(field,callFindById) {
		comboboxChange({e:field});
		resetFluxoContratacao(false, true);
	}

	function resetFluxoContratacao(force, resetGrid) {
		var tabGroup = getTabGroup(this.document);
		var tabFluxo = tabGroup.getTab("fluxoContratacao")
		if (resetGrid && tabFluxo && tabFluxo.getDocument() &&
			tabFluxo.getDocument().getElementById("FluxoContratacao.dataTable") && tabFluxo.getDocument().getElementById("FluxoContratacao.dataTable").gridDefinition &&
			tabFluxo.getDocument().getElementById("FluxoContratacao.dataTable").gridDefinition.getRowCount() > 0 &&
			tabFluxo.getDocument().getElementById("FluxoContratacao.dataTable").gridDefinition.resetGrid) {
			tabFluxo.getDocument().getElementById("FluxoContratacao.dataTable").gridDefinition.resetGrid();
		}
		setElementValue("reconsultarFluxoContratacao","-1");
		//Habilitar ao informar a rota eventual ou a expressa
		if (force ||
			(getElementValue("tpRotaSolicitacao") == "EV" && rotasListboxDef.getData().length > 1) ||
			(getElementValue("tpRotaSolicitacao") == "EX" && getElementValue("rotaIdaVolta.idRotaIdaVolta") != "")) {
			
			if (getElementValue("tpAbrangencia") != "") {
				tabGroup.setDisabledTab("fluxoContratacao", false);
			} else {
				tabGroup.setDisabledTab("fluxoContratacao", true);
			}
		} else tabGroup.setDisabledTab("fluxoContratacao",true);
	}

	function moedaPais_cb(data) {
		moedaPais_idMoedaPais_cb(data);
		if (data != undefined) {
			if (idMoedaPais != undefined) {
				setElementValue("moedaPais.idMoedaPais", idMoedaPais);
			} else {
				for (var i = 0; i < data.length; i++) {
					var blUtilizada = data[i].blIndicadorMaisUtilizada;
					if (blUtilizada == true || blUtilizada == "true") {
						setElementValue("moedaPais.idMoedaPais", data[i].idMoedaPais);
						return;
					}
				}
			}
		}
	}

	function tipoVinculo_cb(data) {
		tpVinculoContratacao_cb(data);
		if(data && tpVinculo != undefined) {
			for(var i = 0; i < data.length; i++) {
				if (data[i].value == tpVinculo) {
					document.getElementById("tpVinculoContratacao").selectedIndex = i + 1;
					changeTpSolicitacaoContratacao(false);
					break;
				}
			}
		}
	}

	function callBackCancelar_cb(data,error) {
		if (error != undefined)
			alert(error);
		else{
			fillFormWithFormBeanData(document.forms[0].tabIndex, data);
			behavior();
			showSuccessMessage();
		}
	}

	function onClickCancelar() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.executeCancel",'callBackCancelar',{e:getElementValue("idSolicitacaoContratacao")});
		xmit({serviceDataObjects:[sdo]});
	}

	function buildLinkPropertiesQueryString_checklist() {
		var qs = "";
		var tpRota = getElementValue("tpRotaSolicitacao");
		var dsRota = "";
		if (tpRota == "EX") {
			dsRota = document.getElementById("rotaIdaVolta.nrRota").value + " - " + getElementValue("rotaIdaVolta.dsRota");
		} else { 
			var separador = "";
			for (var x = 0; x < rotasListboxDef.getData().length; x++) {
				dsRota+= separador + getNestedBeanPropertyValue(rotasListboxDef.getData()[x],"filial.sgFilial");
				separador = "-";
			}
		}

		qs += "&solicitacaoContratacao.idSolicitacaoContratacao=" + document.getElementById("idSolicitacaoContratacao").value;
		qs += "&rota.dsRota=" + dsRota;
		qs += "&solicitacaoContratacao.nrSolicitacaoContratacao=" + document.getElementById("nrSolicitacaoContratacao").value;
		qs += "&solicitacaoContratacao.filial.idFilial=" + document.getElementById("filial.idFilial").value;
		qs += "&solicitacaoContratacao.filial.sgFilial=" + document.getElementById("filial.sgFilial").value;
		qs += "&solicitacaoContratacao.filial.pessoa.nmFantasia=" + document.getElementById("filial.pessoa.nmFantasia").value;
		qs += "&tipoMeioTransporte.tpMeioTransporte.description=" + document.getElementById("tipoMeioTransporte.tpMeioTransporte").value;
		qs += "&tipoMeioTransporte.tpMeioTransporte=" + document.getElementById("tipoMeioTransporte.tpMeioTransporte").value;
		qs += "&tpSolicitacaoContratacao.description=" + document.getElementById("tpSolicitacaoContratacao")[document.getElementById("tpSolicitacaoContratacao").selectedIndex].text;
		qs += "&tpSolicitacaoContratacao=" + document.getElementById("tpSolicitacaoContratacao").value;
		qs += "&dsTipoMeioTransporte=" + document.getElementById("tipoMeioTransporte.dsTipoMeioTransporte").value;
		qs += "&idTipoMeioTransporte=" + document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").value;
		qs += "&tipoMeioTransporte.idTipoMeioTransporte=" + document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").value;
		qs += "&tipoMeioTransporte.dsTipoMeioTransporte=" + document.getElementById("tipoMeioTransporte.dsTipoMeioTransporte").value;
		qs += "&solicitacaoContratacao.nrIdentificacaoMeioTransp=" + document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").value;
		qs += "&solicitacaoContratacao.nrIdentificacaoSemiReboque=" + document.getElementById("nrIdentificacaoSemiReboque.nrPlaca").value;
		qs += "&nrFrotaMT=" + document.getElementById("nrIdentificacaoMeioTransp.nrFrota").value;

		return qs;
	}

	function changeVlFreteSugerido(e) {
		notifyElementListeners({e:e});
		resetFluxoContratacao(false, false);
		return true;
	}

	function changeVlFreteMaximoAutorizado(e) {
		notifyElementListeners({e:e});
		resetFluxoContratacao(false, false);
		return true;
	}

	function blEnvolveParceiraClick() {
		if (getElementValue("blEnvolveParceira") == true) {
			setElementValue("empresaRota.tpEmpresa", "");
			setElementValue("empresaRota.flagDesabilitaEmpresaUsuarioLogado", "false");
			setElementValue("empresaRota.flagBuscaEmpresaUsuarioLogado", "false");
			setElementValue("empresaRota.tpAcesso", "A");

			//Seta tipo de fluxo contratação com 'Mista destino' e desabilita o mesmo.
			setElementValue("tpFluxoContratacao","D");
			setDisabled("tpFluxoContratacao",true);
		} else {
			setElementValue("empresaRota.tpEmpresa", "M");
			setElementValue("empresaRota.flagDesabilitaEmpresaUsuarioLogado", "true");
			setElementValue("empresaRota.flagBuscaEmpresaUsuarioLogado", "true");
			resetValue("empresaRota.tpAcesso");

			//Seta tipo de fluxo contratação com 'Direta/Mista origem' e habilita o mesmo.
			setElementValue("tpFluxoContratacao","O");
			setDisabled("tpFluxoContratacao",false);

			var obj = document.getElementById("rotas");
			for(var i=obj.options.length; i >= 0; i--) {
				obj.options.remove(i);
			}
			rotasListboxDef.cleanRelateds();
			setElementValue("rotas_filial.idFilial",getElementValue("filial.idFilial"));
			setElementValue("rotas_filial.sgFilial",getElementValue("filial.sgFilial"));
			setElementValue("rotas_nmFilial",getElementValue("filial.pessoa.nmFantasia"));
			rotasListboxDef.insertOrUpdateOption();
		}
	}
	
	function changeTpAbrangencia() {
		resetFluxoContratacao(false, true);
		setElementValue("reconsultarFluxoContratacao", "-1");
		comboboxChange({e:getElement("tpAbrangencia")});
	}
	
	function changeQuebraMeioTransporte() {
		if (getElementValue("blQuebraMeioTransporte") == "S") {
			setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
			setDisabled("controleCarga.idControleCarga", false);
			setDisabled("controleCarga.nrControleCarga", true);
			getElement("controleCarga.nrControleCarga").required = "true";
			getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "true";
		} else if(getElementValue("blQuebraMeioTransporte") == "N") {
			
			if(getElementValue("tpSolicitacaoContratacao") != "P") {
				setDisabled("controleCarga",true);
				setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
				setDisabled("controleCarga.idControleCarga", true);
				getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "false";
			} else {
				setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
				setDisabled("controleCarga.idControleCarga", false);
				setDisabled("controleCarga.nrControleCarga", true);
				getElement("controleCarga.nrControleCarga").required = "true";
				getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "true";
			}
			
		}else {
			setDisabled("controleCarga.idControleCarga", true);
			setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
			resetValue("controleCarga.filialByIdFilialOrigem.idFilial");
			resetValue("controleCarga.idControleCarga");
			getElement("controleCarga.nrControleCarga").required = "false";
			getElement("controleCarga.filialByIdFilialOrigem.sgFilial").required = "false";
		}
	}
	
	function changeFilialControleCarga() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			setDisabled("controleCarga.nrControleCarga", true);
			resetValue("controleCarga.idControleCarga");
		} else {
			setDisabled("controleCarga.idControleCarga", false);
		}
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	function callbackControleCarga_cb(data, error) {
		var result = controleCarga_nrControleCarga_exactMatch_cb(data);
		if (getElementValue("controleCarga.idControleCarga") != "") {
			findDadosControleCarga();
		}
		return result;
	}
	
	function findDadosControleCarga() {
		var service = "lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findDadosControleCarga";
		var data = {idControleCarga : getElementValue("controleCarga.idControleCarga")};
		var sdo = createServiceDataObject(service, "findDadosControleCarga", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findDadosControleCarga_cb(data, error) {
		if (error != undefined) {
			resetValue("controleCarga.idControleCarga");
			setFocus("controleCarga.nrControleCarga", false);
			alert(error);
			return;
		} else {
			preencheRotasControleCarga(data);
		}
	}
	
	function preencheRotasControleCarga(data) {
		if(getElementValue("tpSolicitacaoContratacao") != "P") {
			if (data != undefined && data.length > 0) {
				resetValue("rotas");
				for (var i = 0; i < data.length; i++) {
					var idFilial = data[i].idFilial;
					var sgFilial = data[i].sgFilial;
					var nmFantasia = data[i].pessoa.nmFantasia;
					setElementValue("rotas_filial.idFilial", idFilial);
					setElementValue("rotas_filial.sgFilial", sgFilial);
					setElementValue("rotas_nmFilial", nmFantasia);
					rotasListboxDef.insertOrUpdateOption();
				}
			}
		}
	}
</script>
