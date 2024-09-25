<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="gerarControleCargas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="controleCargaTitulo" id="cad" src="/carregamento/gerarControleCargas" cmd="cad"/>

		<adsm:tab title="trechos" id="trechos" src="/carregamento/gerarControleCargas" cmd="trechos"
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="pontosParada" id="pontosParada" src="/carregamento/gerarControleCargas" cmd="pontosParada" 
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="postosPassagemTitulo" id="postos" src="/carregamento/gerarControleCargas" cmd="postosPassagem" 
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="equipeColetaEntregaTitulo" id="equipe" src="/carregamento/gerarControleCargas" cmd="equipeColetaEntrega" 
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="adiantamento" id="adiantamento" src="/carregamento/gerarControleCargas" cmd="adiantamento"
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />

		<adsm:tab title="solicSinal" id="solicitacaoSinal" src="/carregamento/gerarControleCargas" cmd="solicitacaoSinal" 
				  masterTabId="cad" copyMasterTabProperties="true" disabled="true" />
	</adsm:tabGroup>
</adsm:window>
