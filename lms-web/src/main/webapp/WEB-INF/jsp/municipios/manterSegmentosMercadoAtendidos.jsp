<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSegmentosMercadoAtendidos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterSegmentosMercadoAtendidos" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterSegmentosMercadoAtendidos" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>