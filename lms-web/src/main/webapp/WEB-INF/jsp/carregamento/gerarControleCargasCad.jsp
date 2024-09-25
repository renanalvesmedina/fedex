<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.gerarControleCargasAction" onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/gerarControleCargas" idProperty="idControleCarga" >
		
		<adsm:hidden property="origem" value="controleCarga" />
		<adsm:hidden property="tpManifesto" />
		
		<adsm:hidden property="controleCargaConcatenado" serializable="false"/>
		<adsm:hidden property="idEquipe" />

		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte" />
		<adsm:hidden property="meioTransporteByIdSemiRebocado.idMeioTransporte" />
		<adsm:hidden property="proprietario.idProprietario" />
		<adsm:hidden property="motorista.idMotorista" />
		<adsm:hidden property="tpControleCargaValor" />
		
		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="tpVigente" value="S" serializable="false" />

		<adsm:hidden property="tpVinculo" serializable="false" />
		<adsm:hidden property="idTipoMeioTransporteTransportado" serializable="false" />
		<adsm:hidden property="tpMeioTransporte" serializable="false" value="R" />
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" serializable="false" />
		<adsm:hidden property="meioTransporteRodoviario.nrRastreador" serializable="false" />
		
		<adsm:hidden property="idTipoMeioTransporteByRotaIdaVolta" serializable="false" />
		<adsm:hidden property="idRotaViagemByRotaIdaVolta" serializable="false" />

		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false" />
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="19%" width="31%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:combobox label="tipo" property="tpControleCarga" domain="DM_TIPO_CONTROLE_CARGAS" 
					   onchange="return tpControleCarga_OnChange(this);" renderOptions="true" 
					   labelWidth="19%" width="31%" required="true" />


		<adsm:combobox label="tipoRota" property="tpRotaViagem" domain="DM_TIPO_ROTA_VIAGEM_CC" 
					   onchange="return tpRotaViagem_OnChange(this);" renderOptions="true" labelWidth="19%" width="31%"/>


		<adsm:lookup dataType="text" label="solicitContratacao"
					 property="solicitacaoContratacao.filial"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupFilialBySolicitacaoContratacao" 
					 onDataLoadCallBack="retornoFilialSolicitacaoContratacao"
					 onchange="return solicitacaoContratacaoFilial_OnChange();" 
					 action="/municipios/manterFiliais" 
					 size="3" maxLength="3" labelWidth="19%" width="31%" picker="false" serializable="false" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" />	
			<adsm:lookup property="solicitacaoContratacao" 
						 idProperty="idSolicitacaoContratacao" criteriaProperty="nrSolicitacaoContratacao"
						 action="/contratacaoVeiculos/manterSolicitacoesContratacao"
						 service="lms.carregamento.gerarControleCargasAction.findLookupSolicitacaoContratacao"
						 onDataLoadCallBack="retornoSolicitacaoContratacao"
						 onPopupSetValue="popupSolicitacaoContratacao"
						 onchange="return solicitacaoContratacao_OnChange();"
						 dataType="integer" size="10" maxLength="10" mask="0000000000" >
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="solicitacaoContratacao.filial.idFilial" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="solicitacaoContratacao.filial.sgFilial" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" />
				<adsm:propertyMapping modelProperty="tpSolicitacaoContratacao" criteriaProperty="tpControleCarga" disable="true" />
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="solicitacaoContratacao.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="solicitacaoContratacao.filial.sgFilial" blankFill="false" /> 
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" blankFill="false" />
				<adsm:propertyMapping modelProperty="vlFreteNegociado" relatedProperty="vlFreteCarreteiro" />
			 </adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia" serializable="false"/>


		<adsm:hidden property="rota.idRota" serializable="true" />

		<adsm:lookup label="rotaViagem" property="rotaIdaVolta" 
			 		 idProperty="idRotaIdaVolta" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/consultarRotas" cmd="idaVolta" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupRotaIdaVolta" 
					 onDataLoadCallBack="retornoRotaIdaVolta"
					 onchange="return rotaIdaVolta_OnChange();"
					 onPopupSetValue="popupRotaIdaVolta"
					 dataType="integer" size="4" maxLength="4" labelWidth="19%" width="81%" >
			<adsm:propertyMapping modelProperty="vigentes" criteriaProperty="tpVigente" disable="true" />
			<adsm:propertyMapping modelProperty="rota.idRota" relatedProperty="rota.idRota" />
			<adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="dsRota" />
			<adsm:propertyMapping modelProperty="rotaViagem.idRotaViagem" relatedProperty="idRotaViagemByRotaIdaVolta" />
			<adsm:propertyMapping modelProperty="rotaViagem.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteByRotaIdaVolta" />
			<adsm:textbox property="dsRota" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:textbox label="dataHoraSaida" property="dhPrevisaoSaida" dataType="JTDateTimeZone" labelWidth="19%" width="31%" 
					  picker="false" disabled="true" serializable="true" />

		<adsm:textbox label="tempoViagem" property="hrTempoViagem" dataType="text" labelWidth="19%" width="31%" size="6" maxLength="6"
					  disabled="true" picker="false" serializable="false" />
		<adsm:hidden property="nrTempoViagem" />

		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoViagem2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoFrotaViagem"
					 onchange="return meioTransporteByIdTransportadoViagem2_OnChange()"
					 picker="false" label="meioTransporte" labelWidth="19%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportadoViagem.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoViagem.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportadoViagem.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteTransportado" />
			<adsm:propertyMapping modelProperty="meioTransporteRodoviario.nrRastreador" relatedProperty="meioTransporteRodoviario.nrRastreador" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoViagem" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoPlacaViagem"
					 onPopupSetValue="popupMeioTransporteTransportadoViagem"
					 onchange="return meioTransporteByIdTransportadoViagem_OnChange()"
					 picker="true" maxLength="25" width="24%" size="22" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoViagem2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteTransportado" />
			<adsm:propertyMapping modelProperty="meioTransporteRodoviario.nrRastreador" relatedProperty="meioTransporteRodoviario.nrRastreador" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoViagem2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoFrotaViagem"
					 picker="false" label="semiReboque" labelWidth="19%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdSemiRebocadoViagem.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoViagem.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdSemiRebocadoViagem.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoViagem" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoPlacaViagem"
					 onPopupSetValue="popupMeioTransporteSemiRebocadoViagem"
					 picker="true" maxLength="25" width="24%" size="22" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>


		<adsm:textbox property="proprietarioViagem.pessoa.nrIdentificacaoFormatado" label="proprietario" dataType="text" size="20" 
					  labelWidth="19%" width="81%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="proprietarioViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		

		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="19%" width="81%"
					 idProperty="idMotorista"
					 property="motoristaViagem" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupMotorista" 
					 onDataLoadCallBack="retornoMotoristaViagem"
					 onPopupSetValue="popupMotoristaViagem"
					 onchange="return motoristaViagem_OnChange()"
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" serializable="false" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motoristaViagem.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="idMotorista" relatedProperty="motorista.idMotorista" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motoristaViagem.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="motoristaViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:textbox label="valorFreteCarreteiro" property="moedaVlFreteCarreteiro" dataType="text" size="6" 
					  labelWidth="19%" width="81%" disabled="true" serializable="false" >
			<adsm:textbox property="vlFreteCarreteiro" dataType="currency" mask="###,###,###,###,##0.00" disabled="true" />
		</adsm:textbox>


		<adsm:lookup label="rotaColetaEntrega" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupRotaColetaEntrega" 
					 onDataLoadCallBack="retornoRotaColetaEntrega"
					 onchange="return rotaColetaEntrega_OnChange();"
					 onPopupSetValue="popupRotaColetaEntrega"
					 dataType="integer" size="3" maxLength="3" labelWidth="19%" width="81%"   >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="dsRotaColeta" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" />
			<adsm:textbox property="dsRotaColeta" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoColeta2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoFrotaColeta"
					 onchange="return meioTransporteByIdTransportadoColeta2_OnChange()"
					 picker="false" label="meioTransporte" labelWidth="19%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportadoColeta.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoColeta.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportadoColeta.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
		</adsm:lookup>

		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoColeta" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoPlacaColeta"
					 onchange="return meioTransporteByIdTransportadoColeta_OnChange()"
					 onPopupSetValue="popupMeioTransporteTransportadoColeta"
					 picker="true" maxLength="25" width="24%" size="22" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoColeta2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="tpVinculo.value" relatedProperty="tpVinculo" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoColeta2"
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoFrotaColeta"
					 picker="false" label="semiReboque" labelWidth="19%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdSemiRebocadoColeta.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoColeta.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdSemiRebocadoColeta.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoColeta" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.gerarControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoPlacaColeta"
					 onPopupSetValue="popupMeioTransporteSemiRebocadoColeta"
					 picker="true" maxLength="25" width="24%" size="22" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>


		<adsm:textbox property="proprietarioColeta.pessoa.nrIdentificacaoFormatado" label="proprietario" dataType="text" size="20" labelWidth="19%" 
					  width="81%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietarioColeta.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="19%" width="81%"
					 property="motoristaColeta" 
					 idProperty="idMotorista"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.coleta.emitirControleCargasColetaEntregaAction.findLookupMotorista" 
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" serializable="false" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motoristaColeta.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motoristaColeta.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="idMotorista" relatedProperty="motorista.idMotorista" />
			<adsm:textbox dataType="text" property="motoristaColeta.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>
		
		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tabelaColetaEntrega" 
					   service="" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" 
					   labelWidth="19%" width="81%" serializable="true" autoLoad="false" >
			<adsm:propertyMapping modelProperty="idMeioTransporte" criteriaProperty="meioTransporteByIdTransportadoColeta.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="idTabelaColetaEntrega" relatedProperty="tabelaColetaEntrega.idTabelaColetaEntrega" />
			<adsm:propertyMapping modelProperty="blNormal" relatedProperty="blNormal" />
		</adsm:combobox>
		<adsm:hidden property="blNormal" serializable="false" />
		<adsm:hidden property="tabelaColetaEntrega.idTabelaColetaEntrega" serializable="true" />

		<adsm:checkbox property="blEntregaDireta" label="entregaDireta" onclick="checkBlEntregaDireta_OnClick()"
					   labelWidth="19%" width="81%" serializable="true" />

		<adsm:buttonBar>
			<adsm:button caption="gerarPreManifesto" id="botaoGerarPreManifesto" action="/carregamento/manterGerarPreManifesto" cmd="main">
				<adsm:linkProperty src="origem" target="origem"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="controleCarga.filialByIdFilialOrigem.idFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="controleCarga.filialByIdFilialOrigem.sgFilial" disabled="false"/>
				<adsm:linkProperty src="idControleCarga" target="controleCarga.idControleCarga" disabled="false"/>
				<adsm:linkProperty src="nrControleCarga" target="controleCarga.nrControleCarga" disabled="false"/>
				<adsm:linkProperty src="tpManifesto" target="tpManifesto" disabled="false"/>
			</adsm:button>			

			
			<adsm:button id="storeButton" caption="salvar" onclick="salvar_onClick();"/>
			<adsm:button id="resetButton" caption="novo" onclick="novo_onClick()" />
		</adsm:buttonBar>
		
		<script>
			var labelSolicitacaoContratacao = '<adsm:label key="solicitContratacao"/>';
			var LMS_05091 = "<adsm:label key='LMS-05091'/>";
			var LMS_05132 = "<adsm:label key='LMS-05132'/>";
			var LMS_05147 = "<adsm:label key='LMS-05147'/>";
		</script>
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	setDisabled("resetButton", false);
	if (eventObj != undefined) {
		if (eventObj.name == "tab_load") {
			resetaDesabilitaCampos();
			desabilitaTabs();
			escondeCamposColeta();
			escondeCamposViagem();
		}
	}
	if (getElementValue("idControleCarga") == "" || getElementValue("idControleCarga") == "-1") {
		setDisabled("storeButton", false);
		setDisabled("botaoGerarPreManifesto", true);
		setFocusOnFirstFocusableField();
	}
	else {
		setDisabled("storeButton", true);
		setDisabled("botaoGerarPreManifesto", false);
		setFocus(document.getElementById("resetButton"));
	}
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	loadDataUsuario();
	inicializaSecao();
	escondeCamposColeta();
	escondeCamposViagem();
}

function inicializaSecao() {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.newMaster", "retornoPadrao");
	xmit({serviceDataObjects:[sdo]});
}

function retornoPadrao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
}

function inicializaTela() {
	inicializaSecao();
	resetaDesabilitaCampos();
	desabilitaTab("equipe", false);
	getTabGroup(this.document).getTab("equipe").tabOwnerFrame.window.inicializaTela();
	desabilitaTab("equipe", true);
	setElementValue("idControleCarga", -1);
	escondeCamposColeta();
	escondeCamposViagem();
}

function loadDataUsuario() {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.getDataUsuario", "resultado_loadDataUsuario");
   	xmit({serviceDataObjects:[sdo]});
}

function resultado_loadDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data,"filial.idFilial"));
	setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data,"filial.sgFilial"));
	setElementValue("filialUsuario.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
}

function escondeCamposViagem(valor) {
	setRowVisibility("tpRotaViagem", valor);
	setRowVisibility("rotaIdaVolta.idRotaIdaVolta", valor);
	setRowVisibility("dhPrevisaoSaida", valor);
	setRowVisibility("meioTransporteByIdTransportadoViagem2.idMeioTransporte", valor);
	setRowVisibility("proprietarioViagem.pessoa.nrIdentificacaoFormatado", valor);
	setRowVisibility("motoristaViagem.idMotorista", valor);
	setRowVisibility("moedaVlFreteCarreteiro", valor);
}

function escondeCamposColeta(valor) {
	setRowVisibility("rotaColetaEntrega.idRotaColetaEntrega", valor);
	setRowVisibility("meioTransporteByIdTransportadoColeta2.idMeioTransporte", valor);
	setRowVisibility("proprietarioColeta.pessoa.nrIdentificacaoFormatado", valor);
	setRowVisibility("motoristaColeta.idMotorista", valor);
	setRowVisibility("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", valor);
	setRowVisibility("blEntregaDireta", valor);
}

/************************************ Reseta campos ************************************/

function resetaDesabilitaCampos() {
	resetaTodosCampos();
	setDisabled("tpControleCarga", false);
	setDisabled("tpRotaViagem", true);
	setDisabled("solicitacaoContratacao.filial.idFilial", true);
	setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
	setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
	setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
	setDisabled("motoristaViagem.idMotorista", true);
	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", true);
	setDisabled("meioTransporteByIdTransportadoColeta2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdTransportadoColeta.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);
	setDisabled("motoristaColeta.idMotorista", true);
	setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
	setDisabled("botaoGerarPreManifesto", true);
}

function resetaTabsComDados() {
	var tabGroup = getTabGroup(this.document);
    desabilitaTab("equipe", false);
    desabilitaTab("adiantamento", false);
    desabilitaTab("solicitacaoSinal", false);

	resetValue(tabGroup.getTab("equipe").tabOwnerFrame.window.document);
	tabGroup.getTab("equipe").tabOwnerFrame.window.idEquipeOld = "";
	tabGroup.getTab("equipe").tabOwnerFrame.window.dsEquipeOld = "";

	resetValue(tabGroup.getTab("adiantamento").tabOwnerFrame.window.document);
   	resetValue(tabGroup.getTab("solicitacaoSinal").tabOwnerFrame.window.document);

    desabilitaTab("equipe", true);
    desabilitaTab("adiantamento", true);
    desabilitaTab("solicitacaoSinal", true);
}


function resetaTodosCampos() {
	resetValue("idControleCarga");
	resetValue("controleCargaConcatenado");
	resetValue("idEquipe");
	resetValue("meioTransporteByIdTransportado.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte")
	resetValue("motorista.idMotorista");
	resetValue("filialByIdFilialOrigem.idFilial");
	resetValue("filialByIdFilialOrigem.sgFilial");
	resetValue("nrControleCarga");
	resetValue("tpRotaViagem");
	resetValue("solicitacaoContratacao.filial.idFilial");
	resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
	resetValue("rotaIdaVolta.idRotaIdaVolta");
	resetValue("dhPrevisaoSaida");
	resetValue("hrTempoViagem");
	resetValue("nrTempoViagem");
	resetValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoViagem.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	resetValue("motoristaViagem.idMotorista");
	resetValue("moedaVlFreteCarreteiro");
	resetValue("rotaColetaEntrega.idRotaColetaEntrega");
	resetValue("meioTransporteByIdTransportadoColeta2.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoColeta.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte");
	resetValue("motoristaColeta.idMotorista");
	resetValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega");
	resetValue("tabelaColetaEntrega.idTabelaColetaEntrega");
	resetValue("blEntregaDireta");
	resetaProprietario();
	requiredSolicitacaoContratacao("false");
	resetaTabsComDados();
	resetaComboTipoTabela();
}

/************************************ Reseta campos ************************************/



function tpControleCarga_OnChange(combo) {
	inicializaTela();
	desabilitaTabs();
	requiredCamposViagem("false");
	requiredCamposColeta("false");
	escondeCamposColeta();
	escondeCamposViagem();
	if (getElementValue('tpControleCarga') == "C") {
		escondeCamposColeta("false");
		setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
		setDisabled("meioTransporteByIdTransportadoColeta2.idMeioTransporte", false);
		setDisabled("meioTransporteByIdTransportadoColeta.idMeioTransporte", false);
		setDisabled("motoristaColeta.idMotorista", false);
		setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
		setDisabled("blEntregaDireta", false);
		desabilitaTab("equipe", false);
		requiredCamposColeta("true");
	}
	else
		if (getElementValue('tpControleCarga') == "V") {
			escondeCamposViagem("false");
			setDisabled("tpRotaViagem", false);
			requiredCamposViagem("true");
		}
	
	setElementValue("tpControleCargaValor", getElementValue('tpControleCarga'));
	return comboboxChange({e:combo});
}


function requiredCamposViagem(valor) {
	document.getElementById("tpRotaViagem").required = valor;
}

function requiredCamposColeta(valor) {
	document.getElementById("rotaColetaEntrega.nrRota").required = valor;
}


function tpRotaViagem_OnChange(combo) {
	if (getElementValue('tpRotaViagem') == "EV") {
		requiredSolicitacaoContratacao("true");
		desabilitaCamposByTpRotaViagem("EX", true);
		desabilitaCamposByTpRotaViagem("EV", false);
	}
	else {
		requiredSolicitacaoContratacao("false");
		if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
			desabilitaCamposByTpRotaViagem("EV", true);
			desabilitaCamposByTpRotaViagem("EX", false);
		}
		// Não foi selecionado nenhum item
		else {
			desabilitaCamposByTpRotaViagem("EX", true);
			desabilitaCamposByTpRotaViagem("EV", true);
		}
	}
	desabilitaTab("trechos", true);
	desabilitaTab("pontosParada", true);
	desabilitaTab("postos", true);
	desabilitaTabSolicitacaoSinal(true);
	return comboboxChange({e:combo});
}


function desabilitaCamposByTpRotaViagem(tipo, valor) {
	resetValue("solicitacaoContratacao.filial.idFilial");
	resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
	resetValue("rotaIdaVolta.idRotaIdaVolta");
	resetValue("dsRota");
	resetValue("dhPrevisaoSaida");
	resetValue("hrTempoViagem");
	resetValue("nrTempoViagem");
	resetValue("moedaVlFreteCarreteiro");
	resetValue("vlFreteCarreteiro");
	resetValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoViagem.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	resetValue("meioTransporteByIdTransportado.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
	resetValue("solicitacaoContratacao.filial.idFilial");
	resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
	resetValue("motoristaViagem.idMotorista");
	resetaProprietario();

	if (tipo == "EX" || tipo == "EC") {
		setDisabled("rotaIdaVolta.idRotaIdaVolta", valor);
		setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", valor);
		setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", valor);
		if (valor == true) {
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
		}
		setDisabled("solicitacaoContratacao.filial.idFilial", valor);
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", valor);
		setDisabled("motoristaViagem.idMotorista", valor);
	}
	else
	if (tipo == "EV") {
		setDisabled("solicitacaoContratacao.filial.idFilial", valor);
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", valor);
		setDisabled("motoristaViagem.idMotorista", valor);
	}
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
}


function validaPreenchimentoRota() {
	if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
		if (getElementValue('solicitacaoContratacao.idSolicitacaoContratacao') == "" && getElementValue('rotaIdaVolta.idRotaIdaVolta') == "") {
			alert(LMS_05091);
			return false;
		}
	}
	return true;
}


function validaMeioTransporteRotaViagem() {
	if (!validaPreenchimentoRota())
		return false;

    var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.validateMeioTransporteWithRotaViagem", 
    	"retornoValidaMeioTransporteRotaViagem", 
    	{idMeioTransporte:getElementValue("meioTransporteByIdTransportado.idMeioTransporte"), 
    	idRotaViagem:getElementValue("idRotaViagemByRotaIdaVolta")});
    xmit({serviceDataObjects:[sdo]});
}


function retornoValidaMeioTransporteRotaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var blRetorno = getNestedBeanPropertyValue(data, "blRetorno");
	if (blRetorno != undefined && blRetorno == "false")
		requiredSolicitacaoContratacao("true");

	storeGerarControleCargas();
}


function checkBlEntregaDireta_OnClick() {
	if ( getElementValue('blEntregaDireta') == true ) {
		document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[0].selected = true; 
		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
	}
	else 
	if (getElementValue("meioTransporteByIdTransportadoColeta.idMeioTransporte") != "") {
		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
	}
}


function validaPreenchimentoSolicitacaoContratacao() {
	if (getElementValue("solicitacaoContratacao.idSolicitacaoContratacao") == "") {
		if (getElementValue('tpRotaViagem') == "EV") {
			requiredSolicitacaoContratacao("true");
		}
		else {
			var tpVinculo = getElementValue("tpVinculo");
			if (tpVinculo != "") {
				if (tpVinculo == "E") {
					requiredSolicitacaoContratacao("true");
				}
				else
				if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
					if (tpVinculo == "A") {
						validaMeioTransporteRotaViagem();
						return false;
					}
					else
					if (tpVinculo == "P" && getElementValue("idTipoMeioTransporteTransportado") != getElementValue("idTipoMeioTransporteByRotaIdaVolta"))
						requiredSolicitacaoContratacao("true");
				}
			}
		}
	}
	return true;
}


function salvar_onClick() {
	if (getElementValue("tpControleCarga") == "V") {
		if (!validaPreenchimentoSolicitacaoContratacao())
			return false;
	}
	else {
		if (!validaPreenchimentoTabelaColetaEntrega())
			return false;
	}
	storeGerarControleCargas();
}


function storeGerarControleCargas() {
	// Trecho utilizado para personalizar uma msg referente a obrigatoriedade do preenchimento da solicitação de contratação
	if (getElementValue("tpVinculo") == "P" && (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") &&
			getElementValue("idTipoMeioTransporteTransportado") != getElementValue("idTipoMeioTransporteByRotaIdaVolta") )
	{
		if (getElementValue("solicitacaoContratacao.idSolicitacaoContratacao") == "") {
			alert(LMS_05132);
			setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
			return false;
		}
	}

	var form = document.forms[0];
	if (!validateForm(form)) {
		return false;
	}

	if (!validaPreenchimentoRota())
		return false;

	var tabGroup = getTabGroup(this.document);

    var tabDetEquipe = tabGroup.getTab("equipe");
    setElementValue('idEquipe', tabDetEquipe.getFormProperty("equipe.idEquipe"));

  	var tabDetAdiantamento = tabGroup.getTab("adiantamento");
  	var formAdiantamento;
	if (tabGroup.getTab("adiantamento").properties.disabled == false) {
		formAdiantamento = tabDetAdiantamento.tabOwnerFrame.window.document.forms[0]; 
	}

    var tabDetSolicitacaoSinal = tabGroup.getTab("solicitacaoSinal");
    var formSolicitacaoSinal;
	if (tabGroup.getTab("solicitacaoSinal").properties.disabled == false) {
		tabDetSolicitacaoSinal.tabOwnerFrame.window.validaCamposSolicSinal();
		formSolicitacaoSinal = tabDetSolicitacaoSinal.tabOwnerFrame.window.document.forms[0];
		if (!validateForm(formSolicitacaoSinal))
			return false;
	}

    var tabDetPostos = tabGroup.getTab("postos");
    var formPagamentos = tabDetPostos.tabOwnerFrame.window.document.forms[1];
    var formPostos = tabDetPostos.tabOwnerFrame.window.document.forms[2];

    if (!validateForm(formPostos)) {
		return false;
	}
	
	var data = editGridFormBean(form, formPostos);
	var pagamentos = getNestedBeanPropertyValue(editGridFormBean(form, formPagamentos), "pagamentos");
	if (pagamentos != undefined)
		setNestedBeanPropertyValue(data, "pagamentos", pagamentos);

	if (formAdiantamento != undefined)
		setNestedBeanPropertyValue(data, "adiantamento", buildFormBeanFromForm(formAdiantamento));

	if (formSolicitacaoSinal != undefined)
		setNestedBeanPropertyValue(data, "solicitacaoSinal", buildFormBeanFromForm(formSolicitacaoSinal));

    var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.store", "store", data);
    xmit({serviceDataObjects:[sdo]});
}


function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("idControleCarga", getNestedBeanPropertyValue(data, "idControleCarga"));
	var nrControleCarga = getFormattedValue("integer",  getNestedBeanPropertyValue(data, "nrControleCarga"), "00000000", true);
  	setElementValue("nrControleCarga", nrControleCarga);
  	setElementValue("filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial"));
   	setElementValue("filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "filialByIdFilialOrigem.sgFilial"));
   	setElementValue("controleCargaConcatenado", getElementValue("filialByIdFilialOrigem.sgFilial") + " " + nrControleCarga);

	if (getElementValue("tpControleCarga") == "C") {
		setElementValue("tpManifesto", "E");
	} else if (getElementValue("tpControleCarga") == "V") {
		setElementValue("tpManifesto", "V");
	}

	var tabGroup = getTabGroup(this.document);
	var docTrechos = tabGroup.getTab("trechos").tabOwnerFrame.document;
	var docPontosParada = tabGroup.getTab("pontosParada").tabOwnerFrame.document;
	var docPostos = tabGroup.getTab("postos").tabOwnerFrame.document;
    var docEquipe = tabGroup.getTab("equipe").tabOwnerFrame.document;
  	var docAdiantamento = tabGroup.getTab("adiantamento").tabOwnerFrame.document;
    var docSolicitacaoSinal = tabGroup.getTab("solicitacaoSinal").tabOwnerFrame.document;

	setElementValue(docEquipe.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));
	setElementValue(docAdiantamento.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));
	setElementValue(docSolicitacaoSinal.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));
	setElementValue(docPostos.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));
	setElementValue(docTrechos.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));
	setElementValue(docPontosParada.getElementById("_controleCargaConcatenado"), getElementValue("controleCargaConcatenado"));

   	desabilitaTodosCampos();
   	showSuccessMessage();
   	initWindow();
}


function desabilitaTodosCampos() {
	var tabGroup = getTabGroup(this.document);
	var tabDetPostos = tabGroup.getTab("postos");
	var tabDetEquipe = tabGroup.getTab("equipe");
  	var tabDetAdiantamento = tabGroup.getTab("adiantamento");
    var tabDetSolicitacaoSinal = tabGroup.getTab("solicitacaoSinal");
    
	setDisabledDocument(this.document, true);

	if (tabDetPostos.properties.disabled == false)
		setDisabledDocument(tabDetPostos.tabOwnerFrame.window.document, true);

	if (tabDetEquipe.properties.disabled == false)
		tabDetEquipe.tabOwnerFrame.window.desabilitaTela();

	if (tabDetAdiantamento.properties.disabled == false)
		setDisabledDocument(tabDetAdiantamento.tabOwnerFrame.window.document, true);

	if (tabDetSolicitacaoSinal.properties.disabled == false)
    	setDisabledDocument(tabDetSolicitacaoSinal.tabOwnerFrame.window.document, true);

    setDisabled("storeButton", true);
    setDisabled("resetButton", false);
}


function novo_onClick() {
	inicializaTela();
	desabilitaTabs();
	resetValue("tpControleCarga");
	setDisabled("storeButton", false);
	setFocusOnFirstFocusableField();
}


function povoaDadosBeneficiario(data) {
	var statusTab = getTabGroup(this.document).getTab("adiantamento").properties.disabled;
	desabilitaTab("adiantamento", false);

	var documentAdiantamento = getTabGroup(this.document).getTab("adiantamento").tabOwnerFrame.window.document;
	var formAdiantamento = documentAdiantamento.forms[0]; 
	resetDocumentValue(documentAdiantamento);

	formAdiantamento.nrIdentificacaoProprietario.value = getElementValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado");
	formAdiantamento.nomeProprietario.value = getElementValue("proprietarioViagem.pessoa.nmPessoa");
	if (getNestedBeanPropertyValue(data, "nrIdentificacaoBeneficiario") != undefined)
		formAdiantamento.nrIdentificacaoBeneficiario.value = getNestedBeanPropertyValue(data, "nrIdentificacaoBeneficiario");
	if (getNestedBeanPropertyValue(data, "nmPessoaBeneficiario") != undefined)
		formAdiantamento.nomeBeneficiario.value = getNestedBeanPropertyValue(data, "nmPessoaBeneficiario");
	if (getNestedBeanPropertyValue(data, "nrBanco") != undefined)
		formAdiantamento.nrBanco.value = getNestedBeanPropertyValue(data, "nrBanco");
	if (getNestedBeanPropertyValue(data, "nmBanco") != undefined)
		formAdiantamento.nmBanco.value = getNestedBeanPropertyValue(data, "nmBanco");
	if (getNestedBeanPropertyValue(data, "nrAgenciaBancaria") != undefined)
		formAdiantamento.nrAgenciaBancaria.value = getNestedBeanPropertyValue(data, "nrAgenciaBancaria");
	if (getNestedBeanPropertyValue(data, "nmAgenciaBancaria") != undefined)
		formAdiantamento.nmAgenciaBancaria.value = getNestedBeanPropertyValue(data, "nmAgenciaBancaria");
	if (getNestedBeanPropertyValue(data, "nrContaBancaria") != undefined)
		formAdiantamento.nrContaBancaria.value = getNestedBeanPropertyValue(data, "nrContaBancaria");
	if (getNestedBeanPropertyValue(data, "dvContaBancaria") != undefined)
		formAdiantamento.dvContaBancaria.value = getNestedBeanPropertyValue(data, "dvContaBancaria");

	desabilitaTab("adiantamento", statusTab);
}

function limparDadosAbaAdiantamento() {
	var tabDetAdiantamento = getTabGroup(this.document).getTab("adiantamento");
	if (tabDetAdiantamento.properties.disabled == false) {
		resetDocumentValue(tabDetAdiantamento.tabOwnerFrame.window.document);
	}
}

function resetaProprietario() {
	resetValue("proprietario.idProprietario");
	resetValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietarioViagem.pessoa.nmPessoa");
	resetValue("proprietarioColeta.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietarioColeta.pessoa.nmPessoa");
	limparDadosAbaAdiantamento();
	desabilitaTabAdiantamento(true);
}

function resetaComboTipoTabela() {
	tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(new Array());
	resetValue("tabelaColetaEntrega.idTabelaColetaEntrega");
	setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
	setElementValue("blEntregaDireta", false);
}


function validaPreenchimentoTabelaColetaEntrega() {
	if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") == "")
		return true;

	if (document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega").disabled == false) {
		if (getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") == "" && 
			document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').selectedIndex == 0)
		{
			alert(LMS_05147);
			setFocus(document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega'));
			return false;
		}
	}
	return true;
}





/************************************ INICIO - TABS ************************************/
/**
 * Responsável por habilitar/desabilitar uma tab
 */
function desabilitaTab(aba, disabled) {
	getTabGroup(this.document).setDisabledTab(aba, disabled);
}

function desabilitaTabs() {
	desabilitaTab("trechos", true);
	desabilitaTab("pontosParada", true);
	desabilitaTab("postos", true);
	desabilitaTab("equipe", true);
	desabilitaTabAdiantamento(true);
	desabilitaTabSolicitacaoSinal(true);
}

function desabilitaTabPostosParaViagem(valor) {
	if (valor == false) {
		if (getElementValue('meioTransporteByIdTransportado.idMeioTransporte') != "" && 
			(getElementValue('rotaIdaVolta.idRotaIdaVolta') != "" || getElementValue('solicitacaoContratacao.idSolicitacaoContratacao') != ""))
		{
			desabilitaTab("postos", false);
			inicializaTabPostos(true);
		}
	}
	else {
		desabilitaTab("postos", true);
		inicializaTabPostos(false);
	}
}

function desabilitaTabPostosParaColeta(valor) {
	if (valor == false) {
		if (getElementValue('meioTransporteByIdTransportado.idMeioTransporte') != "" && getElementValue('rotaColetaEntrega.idRotaColetaEntrega') != "") {
			desabilitaTab("postos", false);
			inicializaTabPostos(true);
		}
	}
	else {
		desabilitaTab("postos", true);
		inicializaTabPostos(false);
	}
}


function inicializaTabPostos(blInicializaPostos) {
	if (blInicializaPostos == true) {
		var data = {idControleCarga:getElementValue("idControleCarga"),
					blInicializaPostos:true,
					idRotaIdaVolta:getElementValue("rotaIdaVolta.idRotaIdaVolta"),
					idRota:getElementValue("rota.idRota"),
					idRotaColetaEntrega:getElementValue("rotaColetaEntrega.idRotaColetaEntrega"),
					idMeioTransporteTransportado:getElementValue("meioTransporteByIdTransportado.idMeioTransporte"),
					idMeioTransporteSemiRebocado:getElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte"),
					tpControleCarga:getElementValue("tpControleCargaValor")};
	}
	else {
		data = {idControleCarga:getElementValue("idControleCarga"),
				blInicializaPostos:false};
	}
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findPostosPagamentosByVeiculo", "retornoPadrao", data);
    xmit({serviceDataObjects:[sdo]});
}


function desabilitaTabTrechosParaViagem(valor) {
	if (valor == false) {
		if (getElementValue('rotaIdaVolta.idRotaIdaVolta') != "" || getElementValue("rota.idRota") != "") {
			desabilitaTab("trechos", false);
			resetDataByTrechosByViagem();
		}
	}
	else
		desabilitaTab("trechos", true);
}

function resetDataByTrechosByViagem() {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.resetDataByTrechosByViagem", "retornoPadrao", 
			{idControleCarga:getElementValue('idControleCarga'), 
			 idRotaIdaVolta:getElementValue('rotaIdaVolta.idRotaIdaVolta'),
			 idRota:getElementValue('rota.idRota'),
			 dhPrevisaoSaida:getElementValue('dhPrevisaoSaida')});

    xmit({serviceDataObjects:[sdo]});
}

function desabilitaTabSolicitacaoSinal(valor) {
	if (valor == false) {
		if (getElementValue("tpVinculo") == "A" || getElementValue("tpVinculo") == "E") {
			if (getElementValue("meioTransporteRodoviario.nrRastreador") != "" && getElementValue("motoristaViagem.idMotorista") != "") {
				desabilitaTab("solicitacaoSinal", false);
			}
		}
	}
	else {
	    var tabSolicitacaoSinal = getTabGroup(this.document).getTab("solicitacaoSinal");
		if (tabSolicitacaoSinal.properties.disabled == false) {
			resetValue(tabSolicitacaoSinal.tabOwnerFrame.window.document);
		}
   		desabilitaTab("solicitacaoSinal", true);
	}
}

function desabilitaTabAdiantamento(valor) {
	if (valor == false) {
		if (getElementValue("tpVinculo") == "E" && getElementValue("motoristaViagem.idMotorista") != "") {
			desabilitaTab("adiantamento", false);
		}
	}
	else
  		desabilitaTab("adiantamento", true);
}
/************************************ FIM - TABS ************************************/






/************************************ INICIO - ROTA IDA VOLTA ************************************/
function retornoRotaIdaVolta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = rotaIdaVolta_nrRota_exactMatch_cb(data);
	if (r == true) {
		if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
			findDadosRotaIdaVolta(	getNestedBeanPropertyValue(data, "0:idRotaIdaVolta"), 
									getNestedBeanPropertyValue(data, "0:vlFreteKm"), 
									getNestedBeanPropertyValue(data, "0:nrDistancia"),
								 	getNestedBeanPropertyValue(data, "0:moedaPais.moeda.sgMoeda"),
								 	getNestedBeanPropertyValue(data, "0:moedaPais.moeda.dsSimbolo"));
		}
	}
	return r;
}

function popupRotaIdaVolta(data) {
	if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
		setElementValue("rotaIdaVolta.idRotaIdaVolta", getNestedBeanPropertyValue(data, "idRotaIdaVolta"));
		findDadosRotaIdaVolta(	getNestedBeanPropertyValue(data, "idRotaIdaVolta"), 
								getNestedBeanPropertyValue(data, "vlFreteKm"), 
							 	getNestedBeanPropertyValue(data, "nrDistancia"),
							 	getNestedBeanPropertyValue(data, "moedaPais.moeda.sgMoeda"),
							 	getNestedBeanPropertyValue(data, "moedaPais.moeda.dsSimbolo"));
	}
}

function rotaIdaVolta_OnChange() {
	var r = rotaIdaVolta_nrRotaOnChangeHandler();
	if (getElementValue('rotaIdaVolta.nrRota') == "") {
		resetaCamposRelacionadosRotaIdaVolta();

		if (getElementValue("tpRotaViagem") == "EX" || getElementValue("tpRotaViagem") == "EC") {
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
		}
	}
	return r;
}

function resetaCamposRelacionadosRotaIdaVolta() {
	desabilitaTabTrechosParaViagem(true);
	desabilitaTabPostosParaViagem(true);
	desabilitaTab("pontosParada", true);
	resetValue("vlFreteCarreteiro");
	resetValue("moedaVlFreteCarreteiro");
	resetValue("dhPrevisaoSaida");
	resetValue("hrTempoViagem");
	resetValue("nrTempoViagem");
	resetValue("motoristaViagem.idMotorista");
	resetValue("motoristaViagem.pessoa.nrIdentificacao");
	resetValue("motoristaViagem.pessoa.nmPessoa");
}

function findDadosRotaIdaVolta(idRotaIdaVolta, vlFreteKm, nrDistancia, sgMoeda, dsSimbolo) {
	if (vlFreteKm == undefined) 
		vlFreteKm = "";
	if (nrDistancia == undefined) 
		nrDistancia = "";
	if (sgMoeda == undefined) 
		sgMoeda = "";
	if (dsSimbolo == undefined) 
		dsSimbolo = "";

	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findDadosRotaIdaVolta", "retornoFindDadosRotaIdaVolta", 
			{idRotaIdaVolta:idRotaIdaVolta, vlFreteKm:vlFreteKm, 
			 nrDistancia:nrDistancia, sgMoeda:sgMoeda, dsSimbolo:dsSimbolo, idRota:getElementValue("rota.idRota")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosRotaIdaVolta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("rotaIdaVolta.idRotaIdaVolta");
		resetaCamposRelacionadosRotaIdaVolta();
		setFocus(document.getElementById("rotaIdaVolta.nrRota"));
		return false;
	}
	if (data != undefined) {
		setElementValue("vlFreteCarreteiro", setFormat(document.getElementById("vlFreteCarreteiro"), getNestedBeanPropertyValue(data, "vlFreteCarreteiro")) );
		setElementValue("moedaVlFreteCarreteiro", getNestedBeanPropertyValue(data, "moedaVlFreteCarreteiro"));
		setElementValue("dhPrevisaoSaida", setFormat(document.getElementById("dhPrevisaoSaida"), getNestedBeanPropertyValue(data, "dhPrevisaoSaida")) );
		setElementValue("nrTempoViagem", getNestedBeanPropertyValue(data, "nrTempoViagem"));
		setElementValue("hrTempoViagem", setFormat(document.getElementById("hrTempoViagem"), getNestedBeanPropertyValue(data, "hrTempoViagem")) );

		setElementValue("motoristaViagem.idMotorista",  getNestedBeanPropertyValue(data, "motorista.idMotorista"));
		setElementValue("motoristaViagem.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "motorista.pessoa.nrIdentificacaoFormatado"));
		setElementValue("motoristaViagem.pessoa.nmPessoa",  getNestedBeanPropertyValue(data, "motorista.pessoa.nmPessoa"));

		verificaPermissaoParaInformarVeiculo(getNestedBeanPropertyValue(data, "blPermiteInformarVeiculo"));

		desabilitaTabTrechosParaViagem(false);
		desabilitaTabPostosParaViagem(false);
		desabilitaTab("pontosParada", false);
		
		if (document.getElementById("meioTransporteByIdTransportadoViagem2.idMeioTransporte").disabled == true)
			setFocus(document.getElementById("motoristaViagem.pessoa.nrIdentificacao"));
		else
			setFocus(document.getElementById("meioTransporteByIdTransportadoViagem2.nrFrota"));
	}
}


function verificaPermissaoParaInformarVeiculo(valor) {
	if (valor == "false") {
		resetValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte");
		resetValue("meioTransporteByIdTransportadoViagem.idMeioTransporte");
		resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
		resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
		resetValue("meioTransporteByIdTransportado.idMeioTransporte");
		resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
		resetaProprietario();
		setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", true);
		setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", true);
		setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
		setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
	}
	else {
		if (getElementValue("tpRotaViagem") == "EX" || getElementValue("tpRotaViagem") == "EC") {
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
		}
	}
}


/************************************ FIM - ROTA IDA VOLTA ************************************/





/************************************ INICIO - VEICULO - VIAGEM ************************************/
function retornoMeioTransportadoFrotaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdTransportadoViagem2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransportadoViagem(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function retornoMeioTransportadoPlacaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdTransportadoViagem_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransportadoViagem(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function popupMeioTransporteTransportadoViagem(data) {
	callBackMeioTransportadoViagem(getNestedBeanPropertyValue(data,"idMeioTransporte"));
}

function callBackMeioTransportadoViagem(idMeioTransporte) {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findDadosVeiculoViagem", "retornoFindDadosVeiculoViagem", 
			{idMeioTransporte:idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosVeiculoViagem_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdTransportadoViagem2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportadoViagem.idMeioTransporte');
		resetValue("meioTransporteByIdTransportado.idMeioTransporte");
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdTransportadoViagem2.nrFrota'));
		return false;
	}
	if (data != undefined) {
		requiredSolicitacaoContratacao("false");
		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		if (getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado") != undefined) {
			setElementValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
			setElementValue("proprietarioViagem.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		}
		if (getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != undefined && getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != "") {
			setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", 46);
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", false);
		}
		else {
			resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
		}

		if (getElementValue("tpVinculo") == "E") {
			desabilitaTabAdiantamento(false);
			povoaDadosBeneficiario(data);
		}
		desabilitaTabSolicitacaoSinal(false);
		desabilitaTabPostosParaViagem(false);
	}
}


function meioTransporteByIdTransportadoViagem2_OnChange() {
	var r = meioTransporteByIdTransportadoViagem2_nrFrotaOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportadoViagem2.nrFrota') == "") {
		onChangeMeioTransportadoViagem();
	}
	return r;
}

function meioTransporteByIdTransportadoViagem_OnChange() {
	var r = meioTransporteByIdTransportadoViagem_nrIdentificadorOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportadoViagem.nrIdentificador') == "") {
		onChangeMeioTransportadoViagem();
	}
	return r;
}

function onChangeMeioTransportadoViagem() {
	resetaProprietario();
	desabilitaTabPostosParaViagem(true);
	desabilitaTabSolicitacaoSinal(true);
	resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
}
/************************************ FIM - VEICULO - VIAGEM ************************************/






/************************************ INICIO - SEMI REBOQUE - VIAGEM ************************************/
function retornoMeioSemiRebocadoFrotaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocadoViagem2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioSemiRebocadoViagem(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function retornoMeioSemiRebocadoPlacaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocadoViagem_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioSemiRebocadoViagem(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function popupMeioTransporteSemiRebocadoViagem(data) {
	callBackMeioSemiRebocadoViagem(getNestedBeanPropertyValue(data,"idMeioTransporte"));
}

function callBackMeioSemiRebocadoViagem(idMeioTransporte) {	
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.validateMeioTransporteSemiRebocado", 
			"retornoValidateMeioTransporteSemiRebocadoViagem", 
			{idMeioTransporte:idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

function retornoValidateMeioTransporteSemiRebocadoViagem_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte');
		resetValue('meioTransporteByIdSemiRebocadoViagem.idMeioTransporte');
		resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdSemiRebocadoViagem2.nrFrota'));
		return false;
	}
}
/************************************ FIM - SEMI REBOQUE - VIAGEM ************************************/






/************************************ INICIO - SOLICITACAO CONTRATACAO ************************************/
function solicitacaoContratacaoFilial_OnChange() {
	var r = solicitacaoContratacao_filial_sgFilialOnChangeHandler();
	if (getElementValue('solicitacaoContratacao.filial.sgFilial') == "") {
		resetValue('solicitacaoContratacao.idSolicitacaoContratacao');
		resetValue('solicitacaoContratacao.filial.idFilial');
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", false);
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
		disabledCamposBySolicitacaoContratacao(false);
		desabilitaTabTrechosParaViagem(true);
	}
	else
		setDisabled('solicitacaoContratacao.idSolicitacaoContratacao', false);
	return r;
}

function retornoFilialSolicitacaoContratacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = solicitacaoContratacao_filial_sgFilial_exactMatch_cb(data);
	if (r == true) {
		setDisabled('solicitacaoContratacao.idSolicitacaoContratacao', false);
		desabilitaTabTrechosParaViagem(true);
		setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
	}
	return r;
}

function solicitacaoContratacao_OnChange() {
	var r = solicitacaoContratacao_nrSolicitacaoContratacaoOnChangeHandler();
	if (getElementValue('solicitacaoContratacao.nrSolicitacaoContratacao') == "") {
		disabledCamposBySolicitacaoContratacao(false);
		desabilitaTabTrechosParaViagem(true);
	}
	return r;
}

function retornoSolicitacaoContratacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = solicitacaoContratacao_nrSolicitacaoContratacao_exactMatch_cb(data);
	if (r == true) {
		callBackSolicitacaoContratacao(getNestedBeanPropertyValue(data, "0:idSolicitacaoContratacao"), 
									   getNestedBeanPropertyValue(data, "0:rota.idRota"));
	}
	return r;
}

function popupSolicitacaoContratacao(data) {
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", false);
	callBackSolicitacaoContratacao(getNestedBeanPropertyValue(data, "idSolicitacaoContratacao"), 
								   getNestedBeanPropertyValue(data, "rota.idRota"));
}


function callBackSolicitacaoContratacao(idSolicitacaoContratacao, idRota) {
	if (idRota == undefined)
		idRota = "";

	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findDadosSolicitacaoContratacao", 
		"retornoFindDadosSolicitacaoContratacao", 
			{idSolicitacaoContratacao:idSolicitacaoContratacao,
			 idRota:idRota,
			 tpRotaViagem:getElementValue("tpRotaViagem")});
    xmit({serviceDataObjects:[sdo]});
}


function retornoFindDadosSolicitacaoContratacao_cb(data, error) {
	if (error != undefined) {
		disabledCamposBySolicitacaoContratacao(false);
		desabilitaTabTrechosParaViagem(true);
		alert(error);
		resetValue("solicitacaoContratacao.filial.idFilial");
		resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
		setFocus(document.getElementById("solicitacaoContratacao.filial.sgFilial"));
		return false;
	}
	disabledCamposBySolicitacaoContratacao(true);

	var idRotaIdaVolta = getNestedBeanPropertyValue(data, "rotaIdaVolta.idRotaIdaVolta") ;
	if (idRotaIdaVolta != undefined && idRotaIdaVolta != "") {
		setElementValue("rotaIdaVolta.idRotaIdaVolta", idRotaIdaVolta);
		setElementValue("rotaIdaVolta.nrRota", getNestedBeanPropertyValue(data, "rotaIdaVolta.nrRota"));

		setElementValue("idRotaViagemByRotaIdaVolta", getNestedBeanPropertyValue(data, "rotaIdaVolta.rotaViagem.idRotaViagem"));

		setElementValue("dhPrevisaoSaida", setFormat(document.getElementById("dhPrevisaoSaida"), getNestedBeanPropertyValue(data, "dhPrevisaoSaida")) );
		setElementValue("nrTempoViagem", getNestedBeanPropertyValue(data, "nrTempoViagem"));
		setElementValue("hrTempoViagem", setFormat(document.getElementById("hrTempoViagem"), getNestedBeanPropertyValue(data, "hrTempoViagem")) );

		setElementValue("motoristaViagem.idMotorista",  getNestedBeanPropertyValue(data, "motorista.idMotorista"));
		setElementValue("motoristaViagem.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "motorista.pessoa.nrIdentificacaoFormatado"));
		setElementValue("motoristaViagem.pessoa.nmPessoa",  getNestedBeanPropertyValue(data, "motorista.pessoa.nmPessoa"));
		desabilitaTab("pontosParada", false);
	}

	setElementValue("rota.idRota", getNestedBeanPropertyValue(data, "rota.idRota"));
	setElementValue("dsRota", getNestedBeanPropertyValue(data, "rota.dsRota"));
	setElementValue("moedaVlFreteCarreteiro", getNestedBeanPropertyValue(data, "moedaPais.moeda.siglaSimbolo"));
	
	setElementValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem2.nrFrota", getNestedBeanPropertyValue(data, "nrFrotaTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem.nrIdentificador", getNestedBeanPropertyValue(data, "nrIdentificadorTransportado"));
	setElementValue("meioTransporteByIdTransportado.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));

	setElementValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem2.nrFrota", getNestedBeanPropertyValue(data, "nrFrotaSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem.nrIdentificador", getNestedBeanPropertyValue(data, "nrIdentificadorSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));

	if (getElementValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte") != "") {
		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		setElementValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietarioViagem.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));

		if (getElementValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte") == "" && 
			getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != undefined && 
			getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != "") 
		{
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", false);
		}
		else {
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
		}
	}

	setElementValue("tpVinculo", getNestedBeanPropertyValue(data, "tpVinculoTransportado"));
	setElementValue("meioTransporteRodoviario.nrRastreador", getNestedBeanPropertyValue(data, "nrRastreadorTransportado"));
	if (getElementValue("tpVinculo") == "A" || getElementValue("tpVinculo") == "E") {
		desabilitaTabAdiantamento(false);
		if (getElementValue("proprietario.idProprietario") != "") {
			povoaDadosBeneficiario(data);
		}
		desabilitaTabSolicitacaoSinal(false);
	}
	setElementValue("nrTempoViagem", getNestedBeanPropertyValue(data, "nrTempoViagem"));
	setElementValue("hrTempoViagem", setFormat(document.getElementById("hrTempoViagem"), getNestedBeanPropertyValue(data, "hrTempoViagem")) );

	desabilitaTabPostosParaViagem(false);
	desabilitaTabTrechosParaViagem(false);
}


function disabledCamposBySolicitacaoContratacao(valor) {
	limparCamposBySolicitacaoContratacao();
	setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", valor);
	setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", valor);
	setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
	if (!valor) {
		if (getElementValue('tpRotaViagem') == "EX" || getElementValue('tpRotaViagem') == "EC") {
			setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", false);
			setDisabled("motoristaViagem.idMotorista", false);
		}
		else {
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", true);
		}
	}
	else {
		setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
	}
}


function limparCamposBySolicitacaoContratacao() {
	resetValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoViagem.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	resetValue("meioTransporteByIdTransportado.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
	resetValue("rotaIdaVolta.idRotaIdaVolta");
	resetValue("rota.idRota");
	resetValue("dsRota");
	resetValue("idRotaViagemByRotaIdaVolta");
	resetValue('moedaVlFreteCarreteiro');
	resetValue('dhPrevisaoSaida');
	resetValue('nrTempoViagem');
	resetValue('hrTempoViagem');
	desabilitaTab("trechos", true);
	desabilitaTab("pontosParada", true);
	desabilitaTab("postos", true);
	desabilitaTabSolicitacaoSinal(true);
	resetaProprietario();
}


function requiredSolicitacaoContratacao(valor) {
	document.getElementById('solicitacaoContratacao.filial.sgFilial').required = valor;
	document.getElementById('solicitacaoContratacao.nrSolicitacaoContratacao').required = valor;
	if (valor == "true") {
		document.getElementById('solicitacaoContratacao.filial.sgFilial').label = labelSolicitacaoContratacao;
		document.getElementById('solicitacaoContratacao.nrSolicitacaoContratacao').label = labelSolicitacaoContratacao;
	}
}
/************************************ FIM - SOLICITACAO CONTRATACAO ************************************/




/************************************ INICIO - MOTORISTA VIAGEM ************************************/
function retornoMotoristaViagem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = motoristaViagem_pessoa_nrIdentificacao_exactMatch_cb(data);
	if (r == true) {
		desabilitaTabSolicitacaoSinal(false);
		desabilitaTabAdiantamento(false);
	}
	return r;
}

function popupMotoristaViagem(data) {
	setElementValue("motoristaViagem.idMotorista", getNestedBeanPropertyValue(data, "idMotorista"));
	desabilitaTabSolicitacaoSinal(false);
	desabilitaTabAdiantamento(false);
}

function motoristaViagem_OnChange() {
	var r = motoristaViagem_pessoa_nrIdentificacaoOnChangeHandler();
	if (getElementValue('motoristaViagem.pessoa.nrIdentificacao') == "") {
		desabilitaTabSolicitacaoSinal(true);
		desabilitaTabAdiantamento(true);
	}
	return r;
}
/************************************ FIM - MOTORISTA VIAGEM ************************************/




/************************************ INICIO - ROTA COLETA ENTREGA ************************************/
function retornoRotaColetaEntrega_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = rotaColetaEntrega_nrRota_exactMatch_cb(data);
	if (r == true) {
		desabilitaTabPostosParaColeta(false);
	}
	return r;
}

function rotaColetaEntrega_OnChange() {
	var r = rotaColetaEntrega_nrRotaOnChangeHandler();
	if (getElementValue('rotaColetaEntrega.nrRota') == "") {
		desabilitaTabPostosParaColeta(true);
	}
	return r;
}

function popupRotaColetaEntrega(data) {
	desabilitaTabPostosParaColeta(false);
}
/************************************ FIM - ROTA COLETA ENTREGA ************************************/







/************************************ INICIO - VEICULO - COLETA ************************************/
function retornoMeioTransportadoFrotaColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdTransportadoColeta2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransportadoColeta(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function retornoMeioTransportadoPlacaColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdTransportadoColeta_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioTransportadoColeta(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function popupMeioTransporteTransportadoColeta(data) {
	callBackMeioTransportadoColeta(getNestedBeanPropertyValue(data,"idMeioTransporte"));
}

function callBackMeioTransportadoColeta(idMeioTransporte) {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findDadosVeiculoColeta", "retornoFindDadosVeiculoColeta", 
			{idMeioTransporte:idMeioTransporte, blEntregaDireta:getElementValue("blEntregaDireta"), idRotaColetaEntrega:getElementValue("idRotaColetaEntrega")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosVeiculoColeta_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdTransportadoColeta2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportadoColeta.idMeioTransporte');
		resetValue("meioTransporteByIdTransportado.idMeioTransporte");
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdTransportadoColeta2.nrFrota'));
		return false;
	}
	if (data != undefined) {
		var arrayTabelas = getNestedBeanPropertyValue(data, "tabelas");
		if (arrayTabelas != undefined) {
			tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(arrayTabelas);

			setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
			if ( document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').length == 2 ) {
				if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "") {
					document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
					comboboxChange({e:document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega')});
					setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
				}
			}
		}

		if (getElementValue("blEntregaDireta") == true) {
			setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
		}

		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		if (getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado") != undefined) {
			setElementValue("proprietarioColeta.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
			setElementValue("proprietarioColeta.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
		}
		if (getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != undefined && getNestedBeanPropertyValue(data,"idTipoMeioTransporte") != "") {
			setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", 46);
			setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", false);
		}
		else {
			resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
			setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);
		}
		desabilitaTabPostosParaColeta(false);
	}
}

function meioTransporteByIdTransportadoColeta2_OnChange() {
	var r = meioTransporteByIdTransportadoColeta2_nrFrotaOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportadoColeta2.nrFrota') == "") {
		onChangeMeioTransportadoColeta();
	}
	return r;
}

function meioTransporteByIdTransportadoColeta_OnChange() {
	var r = meioTransporteByIdTransportadoColeta_nrIdentificadorOnChangeHandler();
	if (getElementValue('meioTransporteByIdTransportadoColeta.nrIdentificador') == "") {
		onChangeMeioTransportadoColeta();
	}
	return r;
}

function onChangeMeioTransportadoColeta() {
	resetaComboTipoTabela();
	resetaProprietario();
	desabilitaTabPostosParaColeta(true);
	resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte");
	setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);
}
/************************************ FIM - VEICULO - COLETA ************************************/






/************************************ INICIO - SEMI REBOQUE - COLETA ************************************/
function retornoMeioSemiRebocadoFrotaColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocadoColeta2_nrFrota_exactMatch_cb(data);
	if (r == true) {
		callBackMeioSemiRebocadoColeta(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function retornoMeioSemiRebocadoPlacaColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = meioTransporteByIdSemiRebocadoColeta_nrIdentificador_exactMatch_cb(data);
	if (r == true) {
		callBackMeioSemiRebocadoColeta(getNestedBeanPropertyValue(data,"0:idMeioTransporte"));
	}
	return r;
}

function popupMeioTransporteSemiRebocadoColeta(data) {
	callBackMeioSemiRebocadoColeta(getNestedBeanPropertyValue(data,"idMeioTransporte"));
}

function callBackMeioSemiRebocadoColeta(idMeioTransporte) {	
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.validateMeioTransporteSemiRebocado", 
			"retornoValidateMeioTransporteSemiRebocadoColeta", 
			{idMeioTransporte:idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

function retornoValidateMeioTransporteSemiRebocadoColeta_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte');
		resetValue('meioTransporteByIdSemiRebocadoColeta.idMeioTransporte');
		resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdSemiRebocadoColeta2.nrFrota'));
		return false;
	}
}
/************************************ FIM - SEMI REBOQUE - COLETA ************************************/

</script>