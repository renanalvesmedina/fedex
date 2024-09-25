<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.finalizarCarregamentoControleCargasAction">
	<adsm:form action="/carregamento/finalizarCarregamentoControleCargas" 
			   service="lms.carregamento.finalizarCarregamentoControleCargasAction.findByIdFotos" 
			   idProperty="idFotoCarregmtoDescarga">
	
		<adsm:masterLink idProperty="idCarregamentoDescarga" showSaveAll="false">
			<adsm:masterLinkItem property="controleCarga.nrControleCarga" label="controleCargas" itemWidth="50" />
			<adsm:masterLinkItem property="sgFilial" label="filial" itemWidth="50" />
			<adsm:masterLinkItem property="sgFilialPostoAvancado" label="postoAvancado" itemWidth="50" />
		</adsm:masterLink>
		
		<adsm:textbox label="descricao" property="dsFoto" dataType="text" size="50" 
					  maxLength="80" width="85%" required="true" />		
					  
		<adsm:hidden property="filePath" serializable="false"/>
		<adsm:textbox dataType="picture" property="foto.foto" label="foto" size="50" width="85%" required="true" />
		
		<adsm:buttonBar freeLayout="true">	
			<adsm:storeButton caption="salvarFoto" service="lms.carregamento.finalizarCarregamentoControleCargasAction.saveFotos"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>	
	</adsm:form>
	
	<adsm:grid idProperty="idFotoCarregmtoDescarga" property="fotoCarregmtoDescarga"
			   title="fotos" gridHeight="200" unique="true" autoSearch="false" onPopulateRow="povoaLinhas"
			   detailFrameName="fotos">	
		<adsm:gridColumn title="descricao" property="dsFoto" width="90%" />
		<adsm:gridColumn title="foto" property="idFotoCarregmtoDescarga" width="10%" align="center" image="/images/camera.gif" />
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirFoto" service="lms.carregamento.finalizarCarregamentoControleCargasAction.removeByIdsFotos"/>
		</adsm:buttonBar>			
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	
	/**
	 * Função q povoa a grid de fotos corretamente.
	 */
	function povoaLinhas(tr, data) {
		var tdblob = tr.children[2].innerHTML;
		var blob = tdblob.substring(tdblob.indexOf("</IMG>") + 6, tdblob.indexOf("</A>")).replace(" ","");
		fakeDiv = document.createElement("<DIV></DIV>");
		fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + blob + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/camera.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		tr.children[2].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
	}

</script>