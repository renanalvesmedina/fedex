<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterBeneficiariosFreteCarreteiro" type="main">
		<adsm:i18nLabels>
                <adsm:include key="LMS-00049"/>
                <adsm:include key="beneficiario"/>
    	</adsm:i18nLabels>
		
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterBeneficiariosFreteCarreteiro" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterBeneficiariosFreteCarreteiro" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
