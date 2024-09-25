<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargasVeiculos" >
	<adsm:form action="/recepcaoDescarga/iniciarDescarga">
		<adsm:section caption="conferenciaLacre" />
		<adsm:combobox property="conferencia" label="conferencia" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Conferido e aberto|Violado|Não encontrado|Número não confere" width="85%" />
		<adsm:textarea property="observacao" label="observacao" maxLength="200" rows="3" columns="70" width="85%" />
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="salvar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>