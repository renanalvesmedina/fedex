<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarCartaCorrecaoCRT" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/gerarCartaCorrecaoCRT" cmd="pesq"/>
		<adsm:tab title="efetuarCorrecaoTitulo" id="cad" src="/expedicao/gerarCartaCorrecaoCRT" cmd="efetuarCorrecao" boxWidth="120"/>
	</adsm:tabGroup>
</adsm:window>
