<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="conteudoVolume">
	<adsm:form action="/pendencia/manterLotesSalvados">
		<adsm:section caption="conteudoVolume" />
	</adsm:form>
	
	<adsm:grid idProperty="id" property="id" scrollBars="horizontal" unique="true" rows="11">
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
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
