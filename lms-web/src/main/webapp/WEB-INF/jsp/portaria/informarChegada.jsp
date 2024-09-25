<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="informarChegadas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="chegadasPrevistas" id="pesq" src="/portaria/selecionarMeiosTransporteChegada" cmd="list"/>
			<adsm:tab title="meiosTransporteVisitantesTitulo" id="cad" src="/portaria/informarChegada" cmd="avulso" boxWidth="200"/>
		</adsm:tabGroup>
</adsm:window>