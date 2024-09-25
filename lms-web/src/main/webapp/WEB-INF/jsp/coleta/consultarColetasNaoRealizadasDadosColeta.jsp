<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function carregaAwbsPageLoad() {
		setMasterLink(document, true);
		carregaGridAwbsColeta();
	}
</script>

<adsm:window service="lms.coleta.pedidoColetaService" onPageLoad="carregaAwbsPageLoad">
	<adsm:form action="/coleta/consultarColetasNaoRealizadasDadosColeta" height="264">
		<adsm:hidden property="idPedidoColeta" />		
		<adsm:textbox property="nrColeta" label="numero" dataType="text" size="30%" maxLength="30" labelWidth="18%" width="37%" disabled="true"/>
		<adsm:combobox property="status" label="status" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="" width="30%" disabled="true" />

		<adsm:lookup property="filialAtendimento.id" label="filialAtendimento" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true" >
			<adsm:propertyMapping modelProperty="filialAtendimento.id" formProperty="nomeFilialAtendimento"/>
			<adsm:textbox dataType="text" property="nomeFilialAtendimento" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="filialSolicitacao.id" label="filialSolicitacao" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true" >
			<adsm:propertyMapping modelProperty="filialSolicitacao.id" formProperty="nomeFilialAtendimento"/>
			<adsm:textbox dataType="text" property="nomeFilialSolicitacao" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox property="solicitacao" label="solicitacao" dataType="JTDate" size="16%" maxLength="16" labelWidth="18%" width="82%" disabled="true"/>

		<adsm:lookup property="solicitada.id" label="solicitadaPor" action="/" service="" dataType="text" size="18" maxLength="20" labelWidth="18%" width="82%" disabled="true" >
			<adsm:propertyMapping modelProperty="solicitada.id" formProperty="nomeSolicitada"/>
			<adsm:textbox dataType="text" property="nomeSolicitada" size="35" maxLength="35" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="20" labelWidth="18%" width="82%" disabled="true" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="35" maxLength="35" disabled="true"/>
		</adsm:lookup>

		<adsm:textarea property="endereco" label="endereco" maxLength="200" columns="70" labelWidth="18%" width="82%" disabled="true" />

		<adsm:lookup dataType="text" property="municipio" label="municipio" size="30" action="/municipios/manterMunicipios" service="" labelWidth="18%" width="37%" disabled="true" />
		<adsm:textbox property="cep" label="cep" dataType="text" size="10%" maxLength="9" width="30%" disabled="true"/>

		<adsm:textbox property="telefone" label="telefone" dataType="text" size="15%" maxLength="15" labelWidth="18%" width="37%" disabled="true"/>
		<adsm:textbox property="contato" label="contato" dataType="text" size="15%" maxLength="15" width="30%" disabled="true"/>

		<adsm:lookup dataType="text" property="rota" label="rota" size="30" action="/municipios/consultarRotasViagem" service="" labelWidth="18%" width="37%" disabled="true" />
		<adsm:lookup dataType="text" property="regiao" label="regiao" size="30" action="/municipios/manterRegioesColetaEntregaFiliais" service="" width="30%" disabled="true" />

		<adsm:textbox property="totalVolumes" label="totalVolumes" dataType="text" size="18%" maxLength="18" labelWidth="18%" width="37%" disabled="true"/>
		<adsm:textbox property="totalPeso" label="totalPeso" dataType="weight" unit="kg" size="18%" maxLength="18" width="30%" disabled="true"/>

		<adsm:textbox property="valorTotalMercadoria" label="valorTotalMercadoria" dataType="text" size="18%" maxLength="18" labelWidth="18%" width="37%" disabled="true"/>
		<adsm:textbox property="totalPesoAforado" label="totalPesoAforado" dataType="weight" unit="kg" size="18%" maxLength="18" width="30%" disabled="true"/>

		<adsm:textbox property="veiculo" label="veiculo" dataType="text" size="6%" maxLength="8" labelWidth="18%" width="8%" disabled="true" />
		<adsm:lookup dataType="text" property="placaVeiculo" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="29%" disabled="true" />
		<adsm:lookup dataType="text" property="controleCargas" label="controleCargas" size="30" action="/carregamento/manterControleCargas" cmd="list" service="" width="30%" disabled="true" />

		<adsm:textarea property="observacao" label="observacao" maxLength="200" columns="70" labelWidth="18%" width="82%" disabled="true" />
	</adsm:form>

	<adsm:grid property="pedidoColeta" idProperty="idPedidoColeta" selectionMode="none" 
		scrollBars="horizontal" unique="true" gridHeight="250" showGotoBox="false">
		<adsm:gridColumn title="destino" property="nrColeta" width="170"/>
		<adsm:gridColumn title="destinatario" property="destinatario" width="170"/>
		<adsm:gridColumn title="servico" property="servico" width="200"/>
		<adsm:gridColumn title="frete" property="frete" width="75"/>
		<adsm:gridColumn title="peso" property="peso" unit="kg" width="120" align="right" />
		<adsm:gridColumn title="pesoAforado" property="pesoAforado" unit="kg" width="150" align="right" />
		<adsm:gridColumn title="volumes" property="volumes" width="120" align="right" />
		<adsm:gridColumn title="valor" property="valor" unit="reais" width="120" align="right" />
		<adsm:gridColumn title="notaFiscal" property="notaFiscal" width="120" align="right" />
		<adsm:gridColumn title="observacao" property="observacao" width="500" />
		
		<adsm:buttonBar>
			<adsm:button caption="eventosColeta" action="/coleta/consultarEventosColeta" cmd="main" />
			<adsm:button caption="fechar" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
/**
 * Função que ira fazer a chamada do findPaginated de awbColeta
 * baseado no idDetalheColeta.
 */
function carregaGridAwbsColeta() {
	var idPedidoColeta = getElementValue("idPedidoColeta");
	var filtro = new Array();
	// Monta um map com o campo para ser realizado o filtro
	setNestedBeanPropertyValue(filtro, "idPedidoColeta", idPedidoColeta);
	//chama a pesquisa da grid 
	pedidoColetaGridDef.executeSearch(filtro);
}


</script>
