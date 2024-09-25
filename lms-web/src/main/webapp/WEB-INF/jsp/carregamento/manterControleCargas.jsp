<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterControleCargas" cmd="list"/>

		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterControleCargas" cmd="cad" disabled="true" />

		<adsm:tab title="trechos" id="trechos" src="/carregamento/manterControleCargas" cmd="trechos" disabled="true" />

		<adsm:tab title="pontosParadaTitulo" id="pontosParada" src="/carregamento/manterControleCargas" cmd="pontosParada" 
				  disabled="true" />

		<adsm:tab title="postosPassagemTitulo" id="postos" src="/carregamento/manterControleCargas" cmd="postosPassagem" 
				  boxWidth="117" disabled="true" />

		<adsm:tab title="adiantamentos" id="adiantamento" src="/carregamento/manterControleCargas" cmd="adiantamentos" 
				  disabled="true" />

		<adsm:tab title="solicSinal" id="solicitacaoSinal" src="/carregamento/manterControleCargas" cmd="solicitacaoSinal" 
				  disabled="true" />

	</adsm:tabGroup>
</adsm:window>
