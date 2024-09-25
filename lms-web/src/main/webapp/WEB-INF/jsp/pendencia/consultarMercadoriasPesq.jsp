<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/emitirComunicadoApreensaoCliente" height="107">
		
		<adsm:range label="periodo" labelWidth="23%" width="77%" required="true">
			<adsm:textbox property="periodoInicial" dataType="JTDate"/>
			<adsm:textbox property="periodoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:lookup dataType="text" property="mda" label="mda" action="/pendencia/consultarMDA" cmd="list" service="" maxLength="18" labelWidth="23%" width="27%"/>

		<adsm:lookup dataType="text" property="numeroRIM" label="numeroRIM" action="/indenizacoes/consultarReciboIndenizacao" cmd="list" service="" maxLength="18"/>

		<adsm:textbox dataType="text" property="codProdutoCliente" label="codProdutoCliente" labelWidth="23%" width="77%"/>

		<adsm:textbox dataType="text" property="discriminacaoMercadoria" label="discriminacaoMercadoria" maxLength="50" size="50" labelWidth="23%" width="77%"/>

		<adsm:lookup property="clienteId" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" labelWidth="23%" width="77%">
			<adsm:propertyMapping modelProperty="clienteId" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="disposicao" label="disposicao" optionLabelProperty="" optionProperty="" service="" labelWidth="23%" width="27%"/>		

		<adsm:lookup dataType="text" property="lote" label="lote" action="/pendencia/manterLotesSalvados" cmd="list" service=""/>

		<adsm:combobox optionLabelProperty="numeroVolume" label="numeroVolume" optionProperty="" property="" service="" labelWidth="23%" width="77%" >
			<adsm:textbox dataType="currency" property="numeroVolume" />
		</adsm:combobox>

		<adsm:combobox optionLabelProperty="dispositivo" label="dispositivo" optionProperty="" property="" service="" labelWidth="23%" width="77%" >
			<adsm:textbox dataType="currency" property="dispositivo" />
		</adsm:combobox>	

		<adsm:combobox property="terminal" label="terminal" optionLabelProperty="" optionProperty="" service="" labelWidth="23%" width="77%"/>		

		<adsm:textbox dataType="text" property="modulo" label="endereco" maxValue="3" size="3" labelWidth="23%" width="77%">
			<adsm:textbox dataType="text" property="rua" maxValue="3" size="3"/>
			<adsm:textbox dataType="text" property="predio" maxValue="3" size="3"/>
			<adsm:textbox dataType="text" property="andar" maxValue="3" size="3"/>
			<adsm:lookup dataType="text" property="apartamento" action="/pendencia/manterEnderecos" cmd="list" service="" size="3"/>
		</adsm:textbox>

		<adsm:combobox property="naturezaMercadoria" label="naturezaMercadoria" optionLabelProperty="" optionProperty="" service="" labelWidth="23%" width="27%"/>		

		<adsm:range label="vencimento">
			<adsm:textbox property="periodoInicial" dataType="JTDate"/>
			<adsm:textbox property="periodoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" scrollBars="horizontal" rows="7">
		<adsm:gridColumn title="mercadoria" property="mercadoria" width="160"/>
		<adsm:gridColumn title="codigoMercadoria" property="codigoMercadoria" width="100"/>
		<adsm:gridColumn property="volume1" title="numeroVolume" width="75" />
		<adsm:gridColumn property="volume2" title="" width="75" align="right" />
		<adsm:gridColumn property="conteudo" title="conteudo" width="65" align="center" image="popup.gif" link="/pendencia/manterLotesSalvados.do?cmd=conteudoVolume" popupDimension="800,400" />		
		<adsm:gridColumn title="naturezaMercadoria" property="naturezaMercadoria" width="140"/>
		<adsm:gridColumn title="disposicao" property="disposicao" width="100"/>
		<adsm:gridColumn title="lote" property="lote" width="100"/>
		<adsm:gridColumn title="vencimentoMercadoria" property="vencimentoMercadoria" width="100" align="center"/>
		<adsm:gridColumn title="valorMercadoria" property="valorMercadoria" width="100" unit="reais" align="right"/>
		<adsm:gridColumn title="valorVenda" property="valorVenda" width="100" unit="reais" align="right"/>
		<adsm:gridColumn title="recebimento" property="recebimento" width="100" align="center"/>
		<adsm:gridColumn title="mda" property="mda" width="100"/>
		<adsm:gridColumn title="rim" property="rim" width="100"/>
		<adsm:gridColumn title="dataConferencia" property="dataConferencia" width="100" align="center"/>
		<adsm:gridColumn property="dispositivo1" title="dispositivo" width="75" />
		<adsm:gridColumn property="dispositivo2" title="" width="75" align="right" />			
		<adsm:gridColumn title="dataSaida" property="dataSaida" width="100" align="center"/>
		<adsm:gridColumn title="terminal" property="terminal" width="70"/>
		<adsm:gridColumn title="endereco" property="endereco" width="120"/>
		<adsm:gridColumn title="foto" property="foto" width="50" image="camera.gif" />
	</adsm:grid>
	
	<adsm:buttonBar> 
		<adsm:reportViewerButton service="pendencia/consultarMercadorias.jasper"/>
	</adsm:buttonBar>
</adsm:window>