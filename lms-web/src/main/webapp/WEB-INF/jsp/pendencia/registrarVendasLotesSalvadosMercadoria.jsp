<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarVendasLotesSalvados">
		<adsm:masterLink showSaveAll="true">
			<adsm:masterLinkItem label="lote" property="lote" itemWidth="50"/>
		</adsm:masterLink>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" selectionMode="none" scrollBars="horizontal" unique="true" rows="9">
		<adsm:gridColumn property="descricao" title="descricao" width="250"/>
		<adsm:gridColumn property="codigoProduto" title="codigoProduto" width="120"/>
		<adsm:editColumn property="valorPago" field="TextBox" title="valorPago" dataType="currency" unit="reais" width="140"/>
		<adsm:gridColumn property="cliente" title="cliente" width="250"/>
		<adsm:gridColumn property="natureza" title="natureza" width="140"/>
		<adsm:gridColumn property="motivoArmazenagem" title="motivoArmazenagem" width="150"/>
		<adsm:gridColumn property="volumes" title="volumes" width="100" align="right"/>
		<adsm:gridColumn property="unidadeVolume" title="unidadeVolume" width="100" align="right"/>
		<adsm:gridColumn property="valor" title="valor" width="100" unit="reais" align="right"/>
		<adsm:gridColumn property="mda" title="mda" width="100"/>
		<adsm:gridColumn property="dtRecebimento" title="dataRecebimento" width="120" />
		<adsm:gridColumn property="dtConferencia" title="dataConferencia" width="100" />
		<adsm:gridColumn property="dtVencto"        title="dtVencto" width="100" />
		<adsm:gridColumn property="terminal" title="terminal" width="100"/>
		<adsm:gridColumn property="endereco" title="endereco" width="140"/>
		<adsm:gridColumn title="foto" property="foto" width="60" image="camera.gif" />
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
