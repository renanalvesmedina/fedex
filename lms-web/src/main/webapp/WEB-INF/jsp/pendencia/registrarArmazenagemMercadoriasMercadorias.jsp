<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarArmazenagemMercadorias" height="180">
		
		<adsm:masterLink showSaveAll="true">
			<adsm:masterLinkItem label="numeroVolume" property="numeroVolume" itemWidth="100"/>
		</adsm:masterLink>
		
		<adsm:textbox property="numeroMDA" label="numeroMDA" dataType="text" size="20%" maxLength="20" labelWidth="20%" width="80%"/>

		<adsm:textbox label="codProdCliente" property="codigoProdutoCliente" dataType="integer" labelWidth="20%" width="80%"/>

		<adsm:textarea label="descricaoProduto" property="descricaoProduto" maxLength="500" columns="70" rows="3" labelWidth="20%" width="80%" required="true"/>

		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" width="80%" labelWidth="20%" disabled="false">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente" />
			<adsm:textbox dataType="text" property="nomeCliente" size="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox label="documentoServico" property="tipoDocumento" optionLabelProperty="" optionProperty="" service="" prototypeValue="CTRC|CRT|MDA" labelWidth="20%" width="35%">
			<adsm:lookup property="documentoServico" action="/sim/consultarLocalizacoesMercadorias" cmd="cons" dataType="text" service="" size="20" disabled="false"/>
		</adsm:combobox>

		<adsm:lookup dataType="text" property="rim" label="rim" size="15%" action="indenizacoes/consultarReciboIndenizacao.do" cmd="list" service="" labelWidth="20%" width="25%"  />

		<adsm:combobox property="naturezaMercadoria" label="naturezaMercadoria" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="" labelWidth="20%" width="80%" disabled="false" required="true"/>

		<adsm:textbox label="dataVencimento" property="dataVencimento" dataType="JTDate" labelWidth="20%" width="35%"/>
		<adsm:textbox label="unidadesPorVolume" property="unidadesPorVolume" dataType="integer" size="4" maxLength="4" labelWidth="20%" width="7%" required="false"/>
		<adsm:combobox property="" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="" labelWidth="0%" width="18%" disabled="false" required="true"/>
		
		<adsm:combobox label="motivoArmazenagem" optionLabelProperty="" optionProperty="" property="motivoArmazenagem" service="" labelWidth="20%" width="35%" prototypeValue="Acerto de frete|Avaria|Sobra" required="true"/>
		<adsm:combobox label="disposicao" optionLabelProperty="" optionProperty="" property="disposicao" service="" labelWidth="20%" width="25%" required="true" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar"/>	

		<adsm:combobox label="valor" property="moeda" labelWidth="20%" width="30%" optionLabelProperty="" optionProperty="" service="" required="true">
			<adsm:textbox                property="valor" dataType="currency"/>		
		</adsm:combobox>

		<adsm:textbox label="foto" property="foto" dataType="file" labelWidth="20%" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novaMercadoria"/>
			<adsm:button caption="salvarMercadoria"/>
		</adsm:buttonBar>		
	</adsm:form>

	<adsm:grid paramProperty="id" rows="5" scrollBars="horizontal" showPagging="true" gridHeight="90">
	
		<adsm:gridColumn property="descricao" title="descricao" width="300"/>		
		<adsm:gridColumn property="codigoProduto" title="codigoProduto" width="100" align="left"/>
		<adsm:gridColumn property="cliente" title="cliente" width="200"/>
		<adsm:gridColumn property="naturezaMercadoria" title="naturezaMercadoria" width="200"/>
		<adsm:gridColumn property="motivoArmazenagem" title="motivoArmazenagem" width="150"/>
		<adsm:gridColumn property="mda" title="mda" width="100"/>
		<adsm:gridColumn property="rim" title="rim" width="100"/>
		<adsm:gridColumn property="disposicao" title="disposicao" width="100"/>
		<adsm:gridColumn property="unidadesPorVolume" title="unidadesPorVolume" width="130"/>
		<adsm:gridColumn property="dataRecebimento" title="dataRecebimento" width="120" align="center"/>
		<adsm:gridColumn property="dataConferencia" title="dataConferencia" width="100" align="center"/>
		<adsm:gridColumn property="dataVencimento" title="dataVencimento" width="120" align="center"/>
		<adsm:gridColumn title="foto" property="foto" width="50" image="camera.gif" />
		<adsm:buttonBar>
			<adsm:button caption="excluirMercadoria"/> 
		</adsm:buttonBar>
	
	</adsm:grid>

</adsm:window>