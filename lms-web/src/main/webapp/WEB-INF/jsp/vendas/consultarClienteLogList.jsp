<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarClienteLogAction">
	<adsm:form action="/vendas/consultarClienteLog" >
	
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
			dataType="text"
			exactMatch="true"
			idProperty="idCliente"
			label="cliente"
			labelWidth="15%"
			maxLength="20"
			property="cliente"
			service="lms.vendas.consultarClienteLogAction.findCliente"
			size="20"
			width="44%">
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30" 
			/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idClienteLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
			onRowClick="rowClick"
  	>
  		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
  		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
  		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="cliente.pessoa.nmPessoa" title="cliente" dataType="text"/>
		<adsm:gridColumn title="identificacao" property="cliente.pessoa.nrIdentificacaoFormatado"/>
		<adsm:gridColumn property="usuarioInclusao.nmUsuario" title="usuarioInclusao" dataType="text"/>
		<adsm:gridColumn property="clienteResponsavelFrete.pessoa.nmPessoa" title="responsavelFrete" />
		<adsm:gridColumn property="filialCobranca.sgFilial" title="filialCobranca" dataType="integer"/>
		<adsm:gridColumn property="filialAtendeOperacional.sgFilial" title="filialAtendeOperacional" dataType="integer"/>
		<adsm:gridColumn property="filialAtendeComercial.sgFilial" title="filialAtendeComercial" dataType="integer"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true"/>
		<adsm:gridColumn property="tpCliente" title="tipoCliente" isDomain="true"/>
		<adsm:gridColumn property="blGeraReciboFreteEntrega" title="geraReciboFreteEntrega" renderMode="image-check"/>
		<adsm:gridColumn property="blPermanente" title="permanente" renderMode="image-check"/>
		<adsm:gridColumn property="blResponsavelFrete" title="responsavelFrete" renderMode="image-check"/>
		<adsm:gridColumn property="blBaseCalculo" title="baseCalculo" renderMode="image-check"/>
		<adsm:gridColumn property="blCobraReentrega" title="cobraReentrega" renderMode="image-check"/>
		<adsm:gridColumn property="blCobraDevolucao" title="cobraDevolucao" renderMode="image-check"/>
		<adsm:gridColumn property="blColetaAutomatica" title="coletaAutomatica" renderMode="image-check"/>
		<adsm:gridColumn property="blFobDirigido" title="fobDirigido" renderMode="image-check"/>
		<adsm:gridColumn property="blPesoAforadoPedagio" title="calculoPedagioPesoAforado" renderMode="image-check"/>
		<adsm:gridColumn property="blIcmsPedagio" title="incideICMSPedagio" renderMode="image-check"/>
		<adsm:gridColumn property="blIndicadorProtesto" title="indicadorProtesto" renderMode="image-check"/>
		<adsm:gridColumn property="pcDescontoFreteCif" title="percentualDescontoFreteCIF" dataType="currency"/>
		<adsm:gridColumn property="pcDescontoFreteFob" title="percentualDescontoFreteFOB" dataType="currency"/>
		<adsm:gridColumn property="nrCasasDecimaisPeso" title="casasDecimaisPesoMercadoria" dataType="integer"/>
		<adsm:gridColumn property="blMatriz" title="matriz" renderMode="image-check"/>
		<adsm:gridColumn property="dtGeracao" title="dataHoraGeracao" dataType="JTDate"/>
		<adsm:gridColumn property="blObrigaRecebedor" title="obrigaInformarRecebedor" renderMode="image-check"/>
		<adsm:gridColumn property="moedaLimCred.dsMoeda" title="moedaLimCred" dataType="text"/>
		<adsm:gridColumn property="moedaLimDoctos.dsMoeda" title="moedaLimDoctos" dataType="text"/>
		<adsm:gridColumn property="moedaFatPrev.dsMoeda" title="moedaFatPrev" dataType="text"/>
		<adsm:gridColumn property="moedaSaldoAtual.dsMoeda" title="moedaSaldoAtual" dataType="text"/>
		<adsm:gridColumn property="segmentoMercado.dsSegmentoMercado" title="segmentoMercado" dataType="text"/>
		<adsm:gridColumn property="regionalComercial.sgRegional" title="regionalResponsavelComercial" dataType="text"/>
		<adsm:gridColumn property="regionalOperacional.sgRegional" title="regionalResponsavelOperacional" dataType="text"/>
		<adsm:gridColumn property="regionalFinanceiro.sgRegional" title="regionalResponsavelFinanceiro" dataType="text"/>
		<adsm:gridColumn property="cedente.dsCedente" title="cedente" dataType="text"/>
		<adsm:gridColumn property="banco.dsBanco" title="banco" dataType="text"/>
		<adsm:gridColumn property="ramoAtividade.dsRamoAtividade" title="ramoAtividade" dataType="text"/>
		<adsm:gridColumn property="grupoEconomico.dsGrupoEconomico" title="grupoEconomico" dataType="integer"/>
		<adsm:gridColumn property="nrConta" title="numeroConta" dataType="integer"/>
		<adsm:gridColumn property="tpDificuldadeColeta" title="grauDificuldadeColeta" isDomain="true"/>
		<adsm:gridColumn property="tpDificuldadeEntrega" title="grauDificuldadeEntrega" isDomain="true"/>
		<adsm:gridColumn property="tpDificuldadeClassificacao" title="grauDificuldadeClassificacao" isDomain="true"/>
		<adsm:gridColumn property="blEmiteBoletoCliDestino" title="emiteBloquetoClienteDestino" renderMode="image-check"/>
		<adsm:gridColumn property="vlLimiteCredito" title="valorLimiteCredito" dataType="currency"/>
		<adsm:gridColumn property="nrDiasLimiteDebito" title="diasLimiteDebito" dataType="integer"/>
		<adsm:gridColumn property="vlFaturamentoPrevisto" title="faturamentoPrevisto" dataType="currency"/>
		<adsm:gridColumn property="vlSaldoAtual" title="saldoAtual" dataType="currency"/>
		<adsm:gridColumn property="pcJuroDiario" title="percentualJuroDiario" dataType="currency"/>
		<adsm:gridColumn property="vlLimiteDocumentos" title="valorLimiteDocumentosVencimento" dataType="currency"/>
		<adsm:gridColumn property="dtUltimoMovimento" title="ultimoMovimento" dataType="JTDate"/>
		<adsm:gridColumn property="dtFundacaoEmpresa" title="dataFundacaoEmpresa" dataType="JTDate"/>
		<adsm:gridColumn property="tpCobranca" title="tipoCobranca" isDomain="true"/>
		<adsm:gridColumn property="tpMeioEnvioBoleto" title="meioEnvioCobranca" isDomain="true"/>
		<adsm:gridColumn property="dsSite" title="site"/>
		<adsm:gridColumn property="obCliente" title="observacao"/>
		<adsm:gridColumn property="tpAtividadeEconomica" title="atividadeEconomica" isDomain="true"/>
		<adsm:gridColumn property="blAgrupaFaturamentoMes" title="agrupaFaturamentoMes" renderMode="image-check"/>
		<adsm:gridColumn property="tpFormaArredondamento" title="formaArredondamentoPeso" isDomain="true"/>
		<adsm:gridColumn property="tpLocalEmissaoConReent" title="localEmissaoConhecimentoReentrega" isDomain="true"/>
		<adsm:gridColumn property="blAgrupaNotas" title="agrupamentoNotasFiscais" renderMode="image-check" />
		<adsm:gridColumn property="blCadastradoColeta" title="cadastradoColeta" renderMode="image-check"/>
		<adsm:gridColumn property="tpPeriodicidadeTransf" title="periodicidadeTransferencia" isDomain="true"/>
		<adsm:gridColumn property="blRessarceFreteFob" title="ressarceFreteFOB" renderMode="image-check"/>
		<adsm:gridColumn property="blPreFatura" title="preFatura" renderMode="image-check"/>
		<adsm:gridColumn property="blFaturaDocsEntregues" title="faturaDocumentosEntregues" renderMode="image-check"/>
		<adsm:gridColumn property="blCobrancaCentralizada" title="cobrancaCentralizada" renderMode="image-check"/>
		<adsm:gridColumn property="blFaturaDocsConferidos" title="faturaDocumentosConferidos" renderMode="image-check"/>
		<adsm:gridColumn property="blAgendamentoPessoaFisica" title="agendamentoPessoaFisica" renderMode="image-check"/>
		<adsm:gridColumn property="blAgendamentoPessoaJuridica" title="agendamentoPessoaJuridica" renderMode="image-check"/>
		<adsm:gridColumn property="blFronteiraRapida" title="fronteiraRapida" renderMode="image-check"/>
		<adsm:gridColumn property="blOperadorLogistico" title="operadorLogistico" renderMode="image-check"/>
		<adsm:gridColumn property="tpFrequenciaVisita" title="frequenciaVisita" isDomain="true"/>
		<adsm:gridColumn property="observacaoConhecimento.dsObservacaoConhecimento" title="observacaoDoctoServico" dataType="integer"/>
		<adsm:gridColumn property="blFaturaDocReferencia" title="faturaDocumentosCReferencia" renderMode="image-check"/>
		<adsm:gridColumn property="clienteMatriz.pessoa.nmPessoa" title="clienteMatriz" dataType="text"/>
		<adsm:gridColumn property="blDificuldadeEntrega" title="dificuldadeEntrega" renderMode="image-check"/>
		<adsm:gridColumn property="blRetencaoComprovanteEnt" title="retencaoComprovanteEntrega" renderMode="image-check"/>
		<adsm:gridColumn property="blDivulgaLocalizacao" title="divulgaLocalizacao" renderMode="image-check"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script>
function rowClick(){
	return false;
}
</script>