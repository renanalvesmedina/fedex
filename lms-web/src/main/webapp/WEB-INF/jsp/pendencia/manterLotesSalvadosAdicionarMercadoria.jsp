<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="adicionarMercadoriasTitulo">
	<adsm:form action="/pendencia/manterLotesSalvados">
		<adsm:section caption="adicionarMercadoria"/>
		
		<adsm:textbox dataType="text" property="codigoProduto" label="codigoProduto" width="35%"/>
		<adsm:combobox label="disposicao" property="disposicao" optionLabelProperty="" optionProperty="" service="" width="35%" disabled="true"/>
				
		<adsm:textbox dataType="text" property="numeroVolume" label="numeroVolume" width="85%"/>
		
		<adsm:textbox dataType="text" property="descricao" label="descricao" size="50" maxLength="50" width="85%"/>

		<adsm:lookup property="mda" label="mda" dataType="text" criteriaProperty="" action="/pendencias/manterMDA" service="" width="85%"/>
		
		<adsm:lookup property="cliente.id" label="cliente" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" size="18" maxLength="18" width="85%">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox property="maxLength" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox label="natureza" property="natureza" optionLabelProperty="" optionProperty="" service=""/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" scrollBars="horizontal" unique="true" rows="8">
		<adsm:gridColumn property="descricao" title="descricao" width="250"/>
		<adsm:gridColumn property="codigoProduto" title="codigoProduto" width="120"/>
		<adsm:gridColumn property="volume1" title="numeroVolume" width="75" />
		<adsm:gridColumn property="volume2" title="" width="75" align="right" />
		<adsm:gridColumn property="conteudo" title="conteudo" width="65" align="center" image="popup.gif" link="/pendencia/manterLotesSalvados.do?cmd=conteudoVolume" popupDimension="800,400" />
		<adsm:gridColumn property="cliente" title="cliente" width="250"/>
		<adsm:gridColumn property="natureza" title="natureza" width="140"/>
		<adsm:gridColumn property="motivoArmazenagem" title="motivoArmazenagem" width="150"/>				
		<adsm:gridColumn property="valor" title="valor" width="100" unit="reais" align="right"/>
		<adsm:gridColumn property="mda" title="mda" width="100"/>
		<adsm:gridColumn property="dtRecebimento" title="dataRecebimento" width="120" />
		<adsm:gridColumn property="dtConferencia" title="dataConferencia" width="100" />
		<adsm:gridColumn property="dtVencto"        title="dtVencto" width="100" />
		<adsm:gridColumn property="dispositivo1" title="dispositivo" width="75" />
		<adsm:gridColumn property="dispositivo2" title="" width="75" align="right" />		
		<adsm:gridColumn property="terminal" title="terminal" width="100"/>
		<adsm:gridColumn property="endereco" title="endereco" width="140"/>
		<adsm:gridColumn title="foto" property="foto" width="60" image="camera.gif" />
		<adsm:buttonBar>
			<adsm:button caption="adicionar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
