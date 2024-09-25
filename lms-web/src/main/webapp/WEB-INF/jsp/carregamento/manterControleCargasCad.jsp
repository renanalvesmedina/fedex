<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterControleCargasAction" >

	<adsm:form action="/carregamento/manterControleCargas" idProperty="idControleCarga" 
			   onDataLoadCallBack="retorno_carregaDados" service="lms.carregamento.manterControleCargasAction.findById" >

		<adsm:hidden property="blPermiteAlterar" serializable="false" />
		<adsm:hidden property="blPermiteAlterarPgtoProprietario" serializable="false" />

		<adsm:hidden property="tpStatusControleCarga.value" serializable="true" />

		<adsm:hidden property="tpSituacao" value="A" serializable="false" />

		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />

		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte" />
		<adsm:hidden property="meioTransporteByIdSemiRebocado.idMeioTransporte" />
		
		<adsm:hidden property="tpVinculo" serializable="false" />
		<adsm:hidden property="meioTransporteRodoviario.nrRastreador" serializable="false" />
		<adsm:hidden property="idTipoMeioTransporteTransportado" serializable="false" />
		
		<adsm:hidden property="idTipoMeioTransporteByRotaIdaVolta" serializable="false" />

		<adsm:hidden property="veiculoInformadoManualmente" value="false" serializable="true"/>
		<adsm:hidden property="semiReboqueInformadoManualmente" value="false" serializable="true"/>
		<adsm:hidden property="motoristaInformadoManualmente" value="false" serializable="true"/>

		<adsm:hidden property="proprietario.idProprietario" serializable="true" />
		<adsm:hidden property="motorista.idMotorista" serializable="true" />

		<adsm:hidden property="tpMeioTransporte" serializable="false" value="R"/>
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" serializable="false" />

		<adsm:hidden property="idTipoTabelaColetaEntrega" serializable="false" />
		<adsm:hidden property="tabelaColetaEntrega.idTabelaColetaEntrega" serializable="true" />

		<adsm:hidden property="blEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="blDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="blDesabilitaSolicSinal" serializable="false"/>
		<adsm:hidden property="blDesabilitaSemiReboque" serializable="false"/>

		<adsm:hidden property="idRotaViagem" serializable="false" />

		<adsm:hidden property="criterio.idRotaIdaVolta" serializable="false" />
		<adsm:hidden property="criterio.nrRota" serializable="false" />
		<adsm:hidden property="criterio.dsRota" serializable="false" />
		<adsm:hidden property="criterio.idRotaViagem" serializable="false" />
		

		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="18%" width="37%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false" />

		<adsm:textbox dataType="text" label="tipo" property="tpControleCarga.description" width="30%" disabled="true" serializable="false" />
		<adsm:hidden property="tpControleCargaValor" />
		<adsm:hidden property="rota.idRota" />


		<adsm:combobox label="tipoRota" property="tpRotaViagem" domain="DM_TIPO_ROTA_VIAGEM_CC" renderOptions="true"
					   labelWidth="18%" width="37%" disabled="true" />


		<adsm:lookup dataType="text" label="solicitContratacao"
					 property="solicitacaoContratacao.filial"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.manterControleCargasAction.findLookupFilial" 
					 onDataLoadCallBack="retornoFilialSolicitacaoContratacao"
					 onchange="return solicitacaoContratacaoFilial_OnChange();" 
					 action="/municipios/manterFiliais" 
					 size="3" maxLength="3" width="30%" picker="false" serializable="false" >
	        <adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="blEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="flagDesabilitaEmpresaUsuarioLogado" criteriaProperty="blDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" />	
			<adsm:lookup property="solicitacaoContratacao" 
						 idProperty="idSolicitacaoContratacao" criteriaProperty="nrSolicitacaoContratacao"
						 action="/contratacaoVeiculos/manterSolicitacoesContratacao"
						 service="lms.carregamento.manterControleCargasAction.findLookupSolicitacaoContratacao"
						 onPopupSetValue="popupSolicitacaoContratacao"
						 onDataLoadCallBack="retornoSolicitacaoContratacao"
						 onchange="return solicitacaoContratacao_OnChange();"
						 dataType="integer" size="10" maxLength="10" mask="0000000000" >
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="solicitacaoContratacao.filial.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="solicitacaoContratacao.filial.sgFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" disable="true" />
				<adsm:propertyMapping modelProperty="tpSolicitacaoContratacao" criteriaProperty="tpControleCargaValor" disable="true" />
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="solicitacaoContratacao.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="solicitacaoContratacao.filial.sgFilial" blankFill="false" /> 
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" blankFill="false" />
			 </adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia" serializable="false" />


		<adsm:hidden property="rotaIdaVolta.idRotaIdaVolta" />
		<adsm:textbox label="rotaViagem" property="rotaIdaVolta.nrRota" dataType="integer" size="4" 
					  labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="rotaIdaVolta.rota.dsRota" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		

		<adsm:textbox label="dataHoraSaidaProgramado" property="dhPrevisaoSaida" dataType="JTDateTimeZone" labelWidth="18%" width="37%" 
					  picker="false" disabled="true" serializable="false" />

		<adsm:textbox label="tempoViagem" property="hrTempoViagem" dataType="text" width="30%" 
					  disabled="true" picker="false" serializable="false" size="6" maxLength="6" />
		<adsm:hidden property="nrTempoViagem" />


		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoViagem2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoFrotaViagem"
					 onchange="return meioTransporteByIdTransportadoViagem2_OnChange()"
					 picker="false" label="meioTransporte" labelWidth="18%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportadoViagem.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoViagem.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportadoViagem.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteTransportado" />
			<adsm:propertyMapping modelProperty="tpVinculo" relatedProperty="tpVinculo" />
			<adsm:propertyMapping modelProperty="meioTransporteRodoviario.nrRastreador" relatedProperty="meioTransporteRodoviario.nrRastreador" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoViagem" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoPlacaViagem"
					 onPopupSetValue="popupMeioTransporteTransportadoViagem"
					 onchange="return meioTransporteByIdTransportadoViagem_OnChange()"
					 picker="true" maxLength="25" width="30%" size="24" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoViagem2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" relatedProperty="idTipoMeioTransporteTransportado" />
			<adsm:propertyMapping modelProperty="tpVinculo" relatedProperty="tpVinculo" />
			<adsm:propertyMapping modelProperty="meioTransporteRodoviario.nrRastreador" relatedProperty="meioTransporteRodoviario.nrRastreador" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoViagem2"
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoFrotaViagem"
					 picker="false" label="semiReboque" width="7%" size="6" serializable="false" maxLength="6" >
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
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoPlacaViagem"
					 onPopupSetValue="popupMeioTransporteSemiRebocadoViagem"
					 picker="true" maxLength="25" width="23%" size="24" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocadoViagem2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>


		<adsm:textbox label="proprietario" property="proprietarioViagem.pessoa.nrIdentificacaoFormatado"
					 dataType="text" size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietarioViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 idProperty="idMotorista"
					 property="motoristaViagem" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.carregamento.manterControleCargasAction.findLookupMotorista" 
					 onDataLoadCallBack="retornoMotoristaViagem"
					 onPopupSetValue="popupMotoristaViagem"
					 onchange="return motoristaViagem_OnChange()"
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="motoristaViagem.pessoa.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motoristaViagem.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="idMotorista" relatedProperty="motorista.idMotorista" />
			<adsm:propertyMapping modelProperty="rotaIdaVolta.idRotaIdaVolta" criteriaProperty="criterio.idRotaIdaVolta" disable="false" />
			<adsm:propertyMapping modelProperty="rotaIdaVolta.nrRota" criteriaProperty="criterio.nrRota" disable="false" />
			<adsm:propertyMapping modelProperty="rotaIdaVolta.dsRota" criteriaProperty="criterio.dsRota" />
			<adsm:propertyMapping modelProperty="idRotaViagem" criteriaProperty="criterio.idRotaViagem" disable="false" />
			<adsm:textbox dataType="text" property="motoristaViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:textbox label="valorFreteCarreteiro" property="moedaVlFreteCarreteiro" dataType="text" size="6" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="vlFreteCarreteiro" dataType="currency" mask="###,###,###,###,##0.00" disabled="true" serializable="true" />
		</adsm:textbox>

		<adsm:lookup label="rotaColetaEntrega" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.carregamento.manterControleCargasAction.findLookupRotaColetaEntrega" 
					 onDataLoadCallBack="retornoRotaColetaEntrega"
					 onchange="return rotaColetaEntrega_OnChange();"
					 onPopupSetValue="popupRotaColetaEntrega"
					 dataType="integer" size="3" maxLength="3" labelWidth="18%" width="82%" >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" />
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoColeta2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoFrotaColeta"
					 onchange="return meioTransporteByIdTransportadoColeta2_OnChange()"
					 picker="false" label="meioTransporte" labelWidth="18%" width="7%" size="6" serializable="false" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportadoColeta.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoColeta.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportadoColeta.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportadoColeta" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioTransportadoPlacaColeta"
					 onPopupSetValue="popupMeioTransporteTransportadoColeta"
					 onchange="return meioTransporteByIdTransportadoColeta_OnChange()"
					 picker="true" maxLength="25" width="30%" size="24" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportadoColeta2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
		</adsm:lookup>


		<adsm:lookup dataType="text" property="meioTransporteByIdSemiRebocadoColeta2"
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoFrotaColeta"
					 picker="false" label="semiReboque" width="7%" size="6" serializable="false" maxLength="6" >
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
					 service="lms.carregamento.manterControleCargasAction.findLookupMeioTransporteSemiRebocado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onDataLoadCallBack="retornoMeioSemiRebocadoPlacaColeta"
					 onPopupSetValue="popupMeioTransporteSemiRebocadoColeta"
					 picker="true" maxLength="25" width="23%" size="24" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdSemiRebocadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdSemiRebocadoColeta2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte" />
			<adsm:propertyMapping modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>


		<adsm:textbox label="proprietario" property="proprietarioColeta.pessoa.nrIdentificacaoFormatado"
					 dataType="text" size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietarioColeta.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 property="motoristaColeta" 
					 idProperty="idMotorista"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.carregamento.manterControleCargasAction.findLookupMotorista" 
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
					   boxWidth="220" labelWidth="18%" width="82%" serializable="true" autoLoad="false" >
			<adsm:propertyMapping modelProperty="idMeioTransporte" criteriaProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping modelProperty="idTabelaColetaEntrega" relatedProperty="tabelaColetaEntrega.idTabelaColetaEntrega" />
		</adsm:combobox>


		<adsm:checkbox property="blEntregaDireta" label="entregaDireta" onclick="checkBlEntregaDireta_OnClick()"
					   labelWidth="18%" width="82%" serializable="true" />


		<adsm:textbox label="quilomSaida" property="nrQuilometragemSaida" dataType="integer" size="6" 
					  labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="quilomRetorno" property="nrQuilometragemRetorno" dataType="integer" size="6" 
					  width="30%" disabled="true" serializable="false" />


		<adsm:complement label="status" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="text" property="tpStatusControleCarga.description" size="40"
						  disabled="true" serializable="false" />
	
			<adsm:textbox dataType="text" property="filialAtualizaStatus.sgFilial" size="3" disabled="true" serializable="false" />
		</adsm:complement>


					  

		<adsm:buttonBar lines="2">
			<adsm:button id="botaoDocumentos" caption="documentos" onclick="exibeDocumentos();" />
			<adsm:button id="botaoLacres" caption="lacres" onclick="exibeLacres();" />
			<adsm:button id="botaoEquipe" caption="equipeColetaEntrega" onclick="exibeEquipes();" />
			<adsm:button id="botaoVeiculos" caption="veiculos" onclick="exibeVeiculos();" />
			<adsm:button id="botaoSemiReboques" caption="semiReboques" onclick="exibeSemiReboques();" />
			<adsm:button id="botaoPagamentoProprietario" caption="pagamentoProprietario" onclick="exibePagamentos();" />
			<adsm:button id="botaoMotoristas" caption="motoristas" onclick="exibeMotoristas();" />
			<adsm:button id="botaoCancelarControleCargas" caption="cancelarControleCargasItem" action="/carregamento/cancelarControleCargas" cmd="main" >
				<adsm:linkProperty src="idControleCarga" target="controleCarga.idControleCarga" disabled="false" />
				<adsm:linkProperty src="nrControleCarga" target="controleCarga.nrControleCarga" disabled="false" />
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="controleCarga.filialByIdFilialOrigem.idFilial" disabled="false" />
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="controleCarga.filialByIdFilialOrigem.sgFilial" disabled="false" />
			</adsm:button>
			<adsm:button id="botaoEmitir" caption="emitir" onclick="exibeEmitirControleCargas();" />
			<adsm:button id="storeButton" caption="salvar" onclick="salvar_onClick();" />
		</adsm:buttonBar>
		
		<script>
			var labelSolicitacaoContratacao = '<adsm:label key="solicitContratacao"/>';
			var LMS_05132 = '<adsm:label key="LMS-05132"/>';
			var LMS_05147 = "<adsm:label key='LMS-05147'/>";
			var LMS_05162 = "<adsm:label key='LMS-05162'/>";
		</script>

	</adsm:form>
</adsm:window>


<script>
var _tabGroup = getTabGroup(this.document);
if (_tabGroup != undefined) {
	_tabGroup.getTab("cad").properties.ignoreChangedState = true;
}

function initWindow(eventObj) {
	if (eventObj.name == "gridRow_click") {
		inicializaTela();
	}
	desabilitaBotoes(false);
}


function desabilitaBotoes(valor) {
	setDisabled("botaoDocumentos", valor);
	setDisabled("botaoLacres", valor);
	setDisabled("botaoPagamentoProprietario", valor);
	setDisabled("botaoMotoristas", valor);
	setDisabled("botaoCancelarControleCargas", valor);
	setDisabled("botaoEmitir", valor);
	if (valor == false) {
		if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") != "")
			setDisabled("botaoVeiculos", false);
		else
			setDisabled("botaoVeiculos", true);
			
		if (getElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte") != "")
			setDisabled("botaoSemiReboques", false);
		else
			setDisabled("botaoSemiReboques", true);

		if (getElementValue("tpControleCargaValor") == "V")
			setDisabled("botaoEquipe", true);
		else
			setDisabled("botaoEquipe", false);
	}
	else {
		setDisabled("botaoVeiculos", true);
		setDisabled("botaoSemiReboques", true);
		setDisabled("botaoEquipe", true);
	}

	if (getElementValue("blPermiteAlterar") == "false")
		setDisabled("storeButton", true);
	else
		setDisabled("storeButton", valor);
}


function escondeCamposViagem(valor) {
	setRowVisibility("tpRotaViagem", valor);
	setRowVisibility("rotaIdaVolta.nrRota", valor);
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
	setRowVisibility("nrQuilometragemSaida", valor);
}


function retorno_carregaDados_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	desabilitaTabPostosParaViagem(true);
	desabilitaTabAdiantamento(true);
	desabilitaTabSolicitacaoSinal(true);

	onDataLoad_cb(data, error);
	
	// VIAGEM
	if (getElementValue("tpControleCargaValor") == "V") {
		if (getNestedBeanPropertyValue(data, "blFiltrarMotorista")) {
			setElementValue("criterio.idRotaIdaVolta", getElementValue("rotaIdaVolta.idRotaIdaVolta"));
			setElementValue("criterio.nrRota", getElementValue("rotaIdaVolta.nrRota"));
			setElementValue("criterio.dsRota", getElementValue("rotaIdaVolta.rota.dsRota"));
			setElementValue("criterio.idRotaViagem", getElementValue("idRotaViagem"));
		}
	}
	// COLETA
	else {
		tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(getNestedBeanPropertyValue(data, "tabelas"));
		setElementValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", getElementValue("idTipoTabelaColetaEntrega"));
	}
	inicializaCamposTela();
}


function inicializaCamposTela() {
	// VIAGEM
	if (getElementValue("tpControleCargaValor") == "V") {
		escondeCamposViagem("false");
		var tpRotaViagem = getElementValue("tpRotaViagem");
		if (tpRotaViagem == "EX" || tpRotaViagem == "EC") {
			desabilitaTab("trechos", false);
			desabilitaTab("pontosParada", false);
		}
		else
		if (tpRotaViagem == "EV") {	
			desabilitaTab("trechos", false);
		}
		desabilitaTabAdiantamento(false);
		desabilitaTabSolicitacaoSinal(false);
		desabilitaTabPostosParaViagem(false);
	}
	// COLETA
	else {
		escondeCamposColeta("false");
		desabilitaTabPostosParaColeta(false);
	}
	verificaCamposPreenchidos();
	desabilitaBotoes(false);
	setFocusOnFirstFocusableField();
}


function inicializaTela() {
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
	desabilitaSolicitacaoContratacao(true);
	escondeCamposViagem();
	escondeCamposColeta();
}


function desabilitaTab(aba, disabled) {
	getTabGroup(this.document).setDisabledTab(aba, disabled);
}


function verificaCamposPreenchidos() {
	if (getElementValue("blPermiteAlterar") == "false")
		return;

	// VIAGEM
	if (getElementValue("tpControleCargaValor") == "V") {
		if (getElementValue("meioTransporteByIdTransportadoViagem.idMeioTransporte") == "") {
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
			desabilitaSolicitacaoContratacao(false);
		}
		else {
			setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", true);
			setDisabled("solicitacaoContratacao.filial.idFilial", true);
			setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);

			if (getElementValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte") == "" && 
				getElementValue("blDesabilitaSemiReboque") == "false") 
			{
				setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", false);
				setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", false);
			}
		}
		
		if (getElementValue("motoristaViagem.idMotorista") == "") {
			setDisabled("motoristaViagem.idMotorista", false);
		}

		if (getElementValue("solicitacaoContratacao.idSolicitacaoContratacao") != "") {
			setDisabled("solicitacaoContratacao.filial.idFilial", true);
			setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
		}
	}
	// COLETA
	else {
		if (getElementValue("rotaColetaEntrega.idRotaColetaEntrega") == "") {
			setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
		}
		if (getElementValue("meioTransporteByIdTransportadoColeta.idMeioTransporte") == "") {
			setDisabled("meioTransporteByIdTransportadoColeta2.idMeioTransporte", false);
			setDisabled("meioTransporteByIdTransportadoColeta.idMeioTransporte", false);
			setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);
		}
		else {
			setDisabled("meioTransporteByIdTransportadoColeta2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdTransportadoColeta.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
			setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);

			if (getElementValue("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte") == "" &&
				getElementValue("blDesabilitaSemiReboque") == "false") 
			{
				setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", false);
				setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", false);
			}
		}
		

		if (getElementValue("tabelaColetaEntrega.idTabelaColetaEntrega") != "") {
			if (getElementValue("idTipoTabelaColetaEntrega") == "") {
				document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
			}
			setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
			setDisabled("blEntregaDireta", true);
		}
		else 
		if (getElementValue('blEntregaDireta') == true) {
			setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", true);
			setDisabled("blEntregaDireta", false);
		}
		else {
			if (getElementValue("meioTransporteByIdTransportadoColeta.idMeioTransporte") != "") {
				setDisabled("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", false);
			}
			setDisabled("blEntregaDireta", false);
		}
	}

	if (getElementValue("meioTransporteByIdTransportado.idMeioTransporte") == "")
		setElementValue("veiculoInformadoManualmente", "true");
	else
		setElementValue("veiculoInformadoManualmente", "false");
	
	if (getElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte") == "")
		setElementValue("semiReboqueInformadoManualmente", "true");
	else
		setElementValue("semiReboqueInformadoManualmente", "false");

	if (getElementValue("motorista.idMotorista") == "") {
		setElementValue("motoristaInformadoManualmente", "true");
		if (getElementValue("tpControleCargaValor") == "V")
			setDisabled("motoristaViagem.idMotorista", false);
		else
			setDisabled("motoristaColeta.idMotorista", false);
	}
	else {
		setElementValue("motoristaInformadoManualmente", "false");
		setDisabled("motoristaViagem.idMotorista", true);
		setDisabled("motoristaColeta.idMotorista", true);
	}
}


function checkBlEntregaDireta_OnClick() {
	if ( getElementValue('blEntregaDireta') == true ) {
		document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[0].selected = true; 
		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
		resetValue("tabelaColetaEntrega.idTabelaColetaEntrega");
	}
	else
	if (getElementValue("meioTransporteByIdTransportadoColeta.idMeioTransporte") != "") {
		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
	}
}


function validaPreenchimentoTabelaColetaEntrega() {
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





function validaMeioTransporteRotaViagem() {
    var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateMeioTransporteWithRotaViagem", 
    	"retornoValidaMeioTransporteRotaViagem", 
    	{idMeioTransporte:getElementValue("meioTransporteByIdTransportado.idMeioTransporte"), 
    	idRotaViagem:getElementValue("idRotaViagem")});
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


function validaPreenchimentoSolicitacaoContratacao() {
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
	return true;
}


function salvar_onClick() {
	if (getElementValue("tpControleCargaValor") == "V") {
		if (!validaPreenchimentoSolicitacaoContratacao())
			return false;
	}
	else {
		requiredSolicitacaoContratacao(false);
		if (!validaPreenchimentoTabelaColetaEntrega())
			return false;
	}
	storeGerarControleCargas();
}


function storeGerarControleCargas() {
	if (getElementValue("tpControleCargaValor") == "V") {
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
	}

	var form = document.forms[0];
	if (!validateForm(form)) {
		return false;
	}

	var tabGroup = getTabGroup(this.document);
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

	var data = buildFormBeanFromForm(form);
	if (formSolicitacaoSinal != undefined)
		setNestedBeanPropertyValue(data, "solicitacaoSinal", buildFormBeanFromForm(formSolicitacaoSinal));

    var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.store", "store", data);
    xmit({serviceDataObjects:[sdo]});
}


function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	inicializaCamposTela();
	showSuccessMessage();
	
	var blNecessitaCartaoPedagio = getNestedBeanPropertyValue(data, "blNecessitaCartaoPedagio");
	if (blNecessitaCartaoPedagio != undefined && blNecessitaCartaoPedagio == "true") {
		alert(LMS_05162);
	}
}




function exibeDocumentos() {
	showModalDialog('carregamento/manterControleCargasDocumentos.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
}

function exibeLacres() {
	showModalDialog('carregamento/manterControleCargasLacres.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
}

function exibeEquipes() {
	showModalDialog('carregamento/manterControleCargasEquipeColetaEntrega.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
}

function exibeVeiculos() {
	showModalDialog('carregamento/manterControleCargasVeiculos.do?cmd=main', window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	onDataLoad(getElementValue("idControleCarga"));
}

function exibeSemiReboques() {
	showModalDialog('carregamento/manterControleCargasSemiReboques.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	onDataLoad(getElementValue("idControleCarga"));
}

function exibeMotoristas() {
	showModalDialog('carregamento/manterControleCargasMotoristas.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	onDataLoad(getElementValue("idControleCarga"));
}

function exibePagamentos() {
	showModalDialog('carregamento/manterControleCargasPagamentoProprietario.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
}

function exibeEmitirControleCargas() {
	var parametros = 
		'&idControleCarga=' + getElementValue("idControleCarga") +
		'&nrControleCarga=' + getElementValue("nrControleCarga") +
		'&idFilialOrigem=' + getElementValue("filialByIdFilialOrigem.idFilial") +
		'&sgFilialOrigem=' + getElementValue("filialByIdFilialOrigem.sgFilial");
		
	if (getElementValue("tpControleCargaValor") == "V") {
		parent.parent.redirectPage("carregamento/emitirControleCargas.do?cmd=main" + parametros);
	}
	else
		parent.parent.redirectPage("coleta/emitirControleCargasColetaEntrega.do?cmd=main" + parametros);
}






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
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findDadosVeiculoViagem", "retornoFindDadosVeiculoViagem", 
			{idMeioTransporte:idMeioTransporte});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosVeiculoViagem_cb(data, error) {
	if (error != undefined) {
		resetValue('meioTransporteByIdTransportadoViagem2.idMeioTransporte');
		resetValue('meioTransporteByIdTransportadoViagem.idMeioTransporte');
		resetValue("meioTransporteByIdTransportado.idMeioTransporte");
		resetValue("tpVinculo");
		alert(error);
		setFocus(document.getElementById('meioTransporteByIdTransportadoViagem2.nrFrota'));
		return false;
	}
	if (data != undefined) {
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
		desabilitaTabSolicitacaoSinal(false);
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
	desabilitaTabSolicitacaoSinal(true);
	resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);
}

function resetaProprietario() {
	resetValue("proprietario.idProprietario");
	resetValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietarioViagem.pessoa.nmPessoa");
	resetValue("proprietarioColeta.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietarioColeta.pessoa.nmPessoa");
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
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateMeioTransporteSemiRebocado", 
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
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findDadosVeiculoColeta", "retornoFindDadosVeiculoColeta", 
			{idMeioTransporte:idMeioTransporte});
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
		tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(getNestedBeanPropertyValue(data, "tabelas"));

		setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', false);
		if ( document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').length == 2 ) {
			if (document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].text == "") {
				document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega').options[1].selected = true; 
				comboboxChange({e:document.getElementById('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega')});
				setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
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
	resetValue("tabelaColetaEntrega.idTabelaColetaEntrega");
	resetValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte");
	setDisabled("meioTransporteByIdSemiRebocadoColeta2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoColeta.idMeioTransporte", true);
}


function resetaComboTipoTabela() {
	tipoTabelaColetaEntrega_idTipoTabelaColetaEntrega_cb(new Array());
	setDisabled('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega', true);
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
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateMeioTransporteSemiRebocado", 
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





/************************************ INICIO - SOLICITACAO CONTRATACAO ************************************/
function solicitacaoContratacaoFilial_OnChange() {
	var r = solicitacaoContratacao_filial_sgFilialOnChangeHandler();
	if (getElementValue('solicitacaoContratacao.filial.sgFilial') == "") {
		resetValue('solicitacaoContratacao.idSolicitacaoContratacao');
		resetValue('solicitacaoContratacao.filial.idFilial');
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", false);
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
		resetaCamposBySolicitacaoContratacao();
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
		setFocus(document.getElementById("solicitacaoContratacao.nrSolicitacaoContratacao"));
	}
	return r;
}


function solicitacaoContratacao_OnChange() {
	var r = solicitacaoContratacao_nrSolicitacaoContratacaoOnChangeHandler();
	if (getElementValue('solicitacaoContratacao.nrSolicitacaoContratacao') == "") {
		resetaCamposBySolicitacaoContratacao();
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
									   getNestedBeanPropertyValue(data, "0:rota.idRota"), 
									   getNestedBeanPropertyValue(data, "0:nrIdentificacaoMeioTransp"),
									   getNestedBeanPropertyValue(data, "0:nrIdentificacaoSemiReboque"));
	}
	return r;
}

function popupSolicitacaoContratacao(data) {
	setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", false);
	callBackSolicitacaoContratacao(getNestedBeanPropertyValue(data, "idSolicitacaoContratacao"), 
								   getNestedBeanPropertyValue(data, "rota.idRota"), 
								   getNestedBeanPropertyValue(data, "nrIdentificacaoMeioTransp"),
								   getNestedBeanPropertyValue(data, "nrIdentificacaoSemiReboque"));
}


function callBackSolicitacaoContratacao(idSolicitacaoContratacao, idRota, nrIdentificacaoMeioTransp, nrIdentificacaoSemiReboque) {
	setElementValue("rota.idRota", idRota);
	if (idRota == undefined)
		idRota = "";
	if (nrIdentificacaoMeioTransp == undefined)
		nrIdentificacaoMeioTransp = "";
	if (nrIdentificacaoSemiReboque == undefined)
		nrIdentificacaoSemiReboque = "";

	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findDadosSolicitacaoContratacao", 
		"retornoFindDadosSolicitacaoContratacao", 
			{idControleCarga:getElementValue("idControleCarga"),
			 idSolicitacaoContratacao:idSolicitacaoContratacao,
			 idRota:idRota,
			 nrIdentificacaoMeioTransp:nrIdentificacaoMeioTransp,
			 nrIdentificacaoSemiReboque:nrIdentificacaoSemiReboque});
    xmit({serviceDataObjects:[sdo]});
}


function retornoFindDadosSolicitacaoContratacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("solicitacaoContratacao.filial.idFilial");
		resetValue("solicitacaoContratacao.idSolicitacaoContratacao");
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
		setFocus(document.getElementById("solicitacaoContratacao.filial.sgFilial"));
		return false;
	}
	setElementValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem2.nrFrota", getNestedBeanPropertyValue(data, "nrFrotaTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));
	setElementValue("meioTransporteByIdTransportadoViagem.nrIdentificador", getNestedBeanPropertyValue(data, "nrIdentificadorTransportado"));
	setElementValue("meioTransporteByIdTransportado.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteTransportado"));

	if (getElementValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte") != "") {
		setElementValue("proprietario.idProprietario", getNestedBeanPropertyValue(data,"proprietario.idProprietario"));
		setElementValue("proprietarioViagem.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data,"proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietarioViagem.pessoa.nmPessoa", getNestedBeanPropertyValue(data,"proprietario.pessoa.nmPessoa"));
	}

	setElementValue("tpVinculo", getNestedBeanPropertyValue(data, "tpVinculoTransportado"));
	setElementValue("meioTransporteRodoviario.nrRastreador", getNestedBeanPropertyValue(data, "nrRastreadorTransportado"));

	setElementValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem2.nrFrota", getNestedBeanPropertyValue(data, "nrFrotaSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocadoViagem.nrIdentificador", getNestedBeanPropertyValue(data, "nrIdentificadorSemiRebocado"));
	setElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte", getNestedBeanPropertyValue(data, "idMeioTransporteSemiRebocado"));

	var vlFreteCarreteiro = getNestedBeanPropertyValue(data, "vlFreteCarreteiro");
	if (vlFreteCarreteiro != undefined && vlFreteCarreteiro != "") {
		setElementValue("vlFreteCarreteiro", setFormat(document.getElementById("vlFreteCarreteiro"), vlFreteCarreteiro));
		setElementValue("moedaVlFreteCarreteiro", getNestedBeanPropertyValue(data, "moedaVlFreteCarreteiro"));
	}

	setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte", true);
	setDisabled("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte", true);

	desabilitaTabSolicitacaoSinal(false);	
}


function desabilitaSolicitacaoContratacao(valor) {
	setDisabled("solicitacaoContratacao.filial.idFilial", valor);
	setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", valor);
	if (valor == false && getElementValue("solicitacaoContratacao.filial.sgFilial") == "") {
		setDisabled("solicitacaoContratacao.nrSolicitacaoContratacao", true);
	}
}


function requiredSolicitacaoContratacao(valor) {
	document.getElementById('solicitacaoContratacao.filial.sgFilial').required = valor;
	document.getElementById('solicitacaoContratacao.nrSolicitacaoContratacao').required = valor;
	if (valor == "true") {
		document.getElementById('solicitacaoContratacao.filial.sgFilial').label = labelSolicitacaoContratacao;
		document.getElementById('solicitacaoContratacao.nrSolicitacaoContratacao').label = labelSolicitacaoContratacao;
	}
}


function resetaCamposBySolicitacaoContratacao() {
	resetValue("meioTransporteByIdTransportado.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdTransportadoViagem.idMeioTransporte");
	resetValue("tpVinculo");
	resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem2.idMeioTransporte");
	resetValue("meioTransporteByIdSemiRebocadoViagem.idMeioTransporte");
	resetValue("vlFreteCarreteiro");
	resetValue("moedaVlFreteCarreteiro");
	resetaProprietario();
	setDisabled("meioTransporteByIdTransportadoViagem2.idMeioTransporte", false);
	setDisabled("meioTransporteByIdTransportadoViagem.idMeioTransporte", false);
	desabilitaTabSolicitacaoSinal(true);
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







function desabilitaTabAdiantamento(valor) {
	if (valor == false) {
		if (getElementValue("tpVinculo") == "E" && getElementValue("motoristaViagem.idMotorista") != "") {
			desabilitaTab("adiantamento", false);
		}
	}
	else
  		desabilitaTab("adiantamento", true);
}

function desabilitaTabSolicitacaoSinal(valor) {
	if (valor == false && getElementValue("blDesabilitaSolicSinal") != "true") {
		if (getElementValue("tpVinculo") == "A" || getElementValue("tpVinculo") == "E") {
			if (getElementValue("meioTransporteRodoviario.nrRastreador") != "" && getElementValue("motoristaViagem.idMotorista") != "")
				desabilitaTab("solicitacaoSinal", false);
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







function desabilitaTabPostosParaViagem(valor) {
	if (valor == false) {
		if (getElementValue('meioTransporteByIdTransportado.idMeioTransporte') != "" && 
			(getElementValue('rotaIdaVolta.idRotaIdaVolta') != "" || getElementValue('solicitacaoContratacao.idSolicitacaoContratacao') != ""))
		{
			desabilitaTab("postos", false);
		}
	}
	else
		desabilitaTab("postos", true);
}

function desabilitaTabPostosParaColeta(valor) {
	if (valor == false) {
		if (getElementValue('meioTransporteByIdTransportado.idMeioTransporte') != "" && getElementValue('rotaColetaEntrega.idRotaColetaEntrega') != "") {
			desabilitaTab("postos", false);
		}
	}
	else
		desabilitaTab("postos", true);
}


function desabilitaTabTrechosParaViagem(valor) {
	if (valor == false) {
		if (getElementValue('rotaIdaVolta.idRotaIdaVolta') != "" || getElementValue("rota.idRota") != "") {
			desabilitaTab("trechos", false);
		}
	}
	else
		desabilitaTab("trechos", true);
}
</script>