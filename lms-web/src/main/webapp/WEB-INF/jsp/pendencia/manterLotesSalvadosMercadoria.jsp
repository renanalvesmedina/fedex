<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterLotesSalvados">
		<adsm:masterLink showSaveAll="true">
			<adsm:masterLinkItem label="lote" property="lote" itemWidth="100"/>
			<adsm:masterLinkItem label="disposicao" property="disposicao" itemWidth="100"/>
		</adsm:masterLink>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" scrollBars="horizontal" unique="true" rows="12">
		<adsm:gridColumn property="descricao" title="descricao" width="250"/>
		<adsm:gridColumn property="codigoProduto" title="codigoProduto" width="120"/>
		<adsm:gridColumn property="volume1" title="numeroVolume" width="75" />
		<adsm:gridColumn property="volume2" title="" width="75" align="right" />		
		<adsm:gridColumn property="cliente" title="cliente" width="250"/>
		<adsm:gridColumn property="natureza" title="natureza" width="140"/>
		<adsm:gridColumn property="motivoArmazenagem" title="motivoArmazenagem" width="150"/>				
		<adsm:gridColumn property="valor" title="valor" width="100" unit="reais" align="right"/>
		<adsm:gridColumn property="mda" title="mda" width="100"/>
		<adsm:gridColumn property="dtRecebimento" title="dataRecebimento" width="120" />
		<adsm:gridColumn property="dtConferencia" title="dataConferencia" width="100" />
		<adsm:gridColumn property="dtVencto" title="dtVencto" width="100" />
		<adsm:gridColumn property="dispositivo1" title="dispositivo" width="75" />
		<adsm:gridColumn property="dispositivo2" title="" width="75" align="right" />
		<adsm:gridColumn property="terminal" title="terminal" width="100"/>
		<adsm:gridColumn property="endereco" title="endereco" width="140"/>
		<adsm:gridColumn title="foto" property="foto" width="60" image="camera.gif" />
	
		<adsm:buttonBar>
			<adsm:button caption="adicionarMercadoria" onclick="showModalDialog('pendencia/manterLotesSalvados.do?cmd=adicionarMercadoria',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');"/>
			<adsm:button caption="excluirMercadoria"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
