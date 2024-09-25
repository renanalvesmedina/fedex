<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="dadosColeta">
	<adsm:form action="/coleta/consultarColetasNaoRealizadasDadosCliente">
		<adsm:section caption="dadosCliente" />
		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="20" labelWidth="18%" width="82%" disabled="true" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente" />
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>
		<adsm:textbox property="dataUltimaColeta" label="dataUltimaColeta" dataType="JTDate" size="16%" maxLength="16" labelWidth="18%" width="82%" disabled="true" />
		<adsm:textbox property="numeroUltimaColeta" label="numeroUltimaColeta" dataType="text" size="16%" maxLength="16" labelWidth="18%" width="82%" disabled="true" />
		<adsm:textbox property="dataGeracao" label="dataGeracao" dataType="JTDate" size="16%" maxLength="16" labelWidth="18%" width="82%" disabled="true" />
		<adsm:textbox property="dataAlteracao" label="dataAlteracao" dataType="JTDate" size="16%" maxLength="16" labelWidth="18%" width="82%" disabled="true" />
	</adsm:form>
	<adsm:form action="/coleta/consultarColetasNaoRealizadasDadosCliente" height="330" >
		<adsm:grid idProperty="id" property="id" selectionMode="none" rows="4" scrollBars="horizontal" gridHeight="127" unique="false" title="enderecosColeta">
			<adsm:gridColumn title="endereco" property="endereco" width="200"/>
			<adsm:gridColumn title="numero" property="numero" width="70" align="right"/>
			<adsm:gridColumn title="complemento" property="complemento" width="100"/>
			<adsm:gridColumn title="bairro" property="bairro" width="120"/>
			<adsm:gridColumn title="cep" property="cep" width="80" align="right" />
			<adsm:gridColumn title="municipio" property="municipio" width="150" />
			<adsm:gridColumn title="uf" property="uf" width="70" />
			<adsm:gridColumn title="rota" property="rota" width="120" />
			<adsm:gridColumn title="regiao" property="regiao" width="120" />
			<adsm:gridColumn title="telefones" property="telefones" width="100" image="popup.gif" />
		</adsm:grid>

		<adsm:grid idProperty="id" property="id" selectionMode="none" showPagging="false" rows="7" gridHeight="160" unique="false" title="coletaAutomatica">
			<adsm:gridColumn title="dia" property="dia" width="30%"/>
			<adsm:gridColumn title="coleta" property="coleta" width="30%"/>
			<adsm:gridColumn title="horaInicial" property="horaInicial" width="20%" align="center" />
			<adsm:gridColumn title="horaFinal" property="horaFinal" width="20%" align="center" />
		</adsm:grid>

	</adsm:form>
</adsm:window>