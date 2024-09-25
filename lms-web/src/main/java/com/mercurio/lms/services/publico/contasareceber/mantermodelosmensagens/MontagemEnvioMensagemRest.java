package com.mercurio.lms.services.publico.contasareceber.mantermodelosmensagens;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.costasreceber.EventoMensagem;

import com.itextpdf.text.pdf.codec.Base64;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.service.LoteCobrancaTerceiraService;
import com.mercurio.lms.contasreceber.model.service.MonitoramentoMensagemService;
import com.mercurio.lms.contasreceber.model.service.MonitorarMensagemComunicacaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Public
@Path("/publico/contasareceber/montagemEnvioMensages")
public class MontagemEnvioMensagemRest extends BaseRest {
	private static String IMAGE_CONTENT = "iVBORw0KGgoAAAANSUhEUgAAAMwAAAByCAYAAAAf4j73AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBNYWNpbnRvc2giIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTFGMjFENDczMDFDMTFFNEI0OEJGNENEMzJFMTA1N0YiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTFGMjFENDgzMDFDMTFFNEI0OEJGNENEMzJFMTA1N0YiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1MUYyMUQ0NTMwMUMxMUU0QjQ4QkY0Q0QzMkUxMDU3RiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1MUYyMUQ0NjMwMUMxMUU0QjQ4QkY0Q0QzMkUxMDU3RiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PkNuwT4AABI5SURBVHja7F1LcuM4EgUVtW/2Ccw6QcknsHQCyyewtemtrImYxaxkrWYxESN7OxtJJ7B8AtMnsOoEzboBb9BDuBN2CgbJBAiCII2MUPgjfsDHfPnDL2KO5a8/2Lj4MSk+P4pPUnz433HNacfikxefF/g9jf739ncvpcAgQRiMAYek5rQMPvz5fwIGWY8xiAED/vwXoAPjmtNyeP4MYXB02e7IETCz4nMJAMWWLv1GnOKzdw2aIQ4Yg8TSZTPA4KnA4NADDDghrhFRbEguMCg+h7YNadQiOBMA58bBu+CKsy8+9z55HvAkC8Agbvl2/Ll3xefBJ88DBvMWdCFxcMsdGNG0F4QpAOLKsXIEThlg6y6VBozFCixpF5ICBmnHxmLlyGCWGVGOwc5LwoCSbDskiiz3AFjuWEm2HRJFRZy5S+MBHmUFXsUHyQCD1AvCAEBbyFN8kxzAOjhQlDtQFB+FG447Bxhwb7JxEH6ayAF0Ie+MMJDIbj0FyDpYFYns1mIS25YcAYNjCxj4bDStGtCoAUjbDuNTU7CmNhUGLOqW9UvmNuN6CMUfe2A0T/LcAoO5E8KANXm2aFEz9PklffeDfdTnY58UxrLBwP0LZRgkFvNDY4Vp0WAIDPjPn9J3Z+j5bWFwBAOat0YYCD8eGzaaN5TXzFOdRAx19l2A629CIF5+XjYIPx4bJvYZhIkvTKMTFnX2CQyavAeO/ZVpmFq0ZdMwsc8lDDKNe08Ah8uGhls7TI00yfJsqKgcjD1YNisVG8ifrhvEzdpWtqF3FQryYCsshHeyaGBAjKxsQ+/KMdjbKsSAIb1h5v08WqF61DJZMtZCLVwBmGm9n0yaBmThL+SBtdipijoHFwbvSIs0DciyYy33jzXoAySTJmpJUXKwpHeusjgg9cYgVCLlNIaKwq+7dNUXBO9qY9JOiuEwzFlSwODoUBfuDIwHyXBQCPOqSZaUOe4sk9p7C1ZGB6yrqhDBIF7PmMXOMsPKlW4ncmVeByHwo6bR5B7lviMMEqbfiXws2ntuTBgDRVm79Co13kanb4S/3HMVyQ0UpbU+HwNvo9s3ojQcoHyvGkaotT4fQ2+zsmU4IosWZd5mrmKoMDqh5CfrYqAoVsq1lnHQCSWVhkMzyjAqJLSMgW4oWRpxjGqskw7IO58Uhb8wIAC1XWOwRlh0RjHMfSML4MDbRG3Xp/cOmIw1DMa5b3OVQDenoKsU2QIHaIRheuOBrnyejwIKQy1hrsCrCKtEjX+XvhkMhcJQ+50m8Oy4AkkKRX00GAgDnk9eaRiODSkkg4TxuY9hmKXwTAD7J9Fo7HxWFMPwjFvi74zeQetdGGYpPJvKhRuVh6FalPs+kEWEZxoueaIRiqV9IQvytqlGaDYhkmvalynjoLPUyt2q0sNoeBeuKFPWM9H0niQrXKYoqDOxzujo9rLfKsh8gX7PWEXfD7SL6j2NrHBPdIFayDh5vm8G3iWnJJFFg/hLSRzjsKyq+/MHL9p1z+xMbqorHY8JePKxUOea990QjtmXeRLe5gKDOdOrgFYRPiUQfONYD3jB5/e63JvRKqArjOUIPdiY6IKpc8aTDgwHxWquNaolVR7WxlioMfR1uQ5LDsTQrM5wrgnHXfqoB6DDD8QiyFiVwywIJ2c+dExayGeWDS9jM2+5hVDRtTR9hmWfl7oCXbiDELZOFirCUHqE12wAAolfZnj6roVhP49ldf8WMeDPYFq0yfpS8LGk07MTwkCvfvyFQGpC/jaMRmwpp+gzBj4bzxg48u5hKHHmng1LDga5TJurTU4gQXbtZXRzGTGvZ0hC0e1LTBhKDD0k7yJymUMLwDaRDU4wPVKWE0PT99zFULffODKC4Q9JzcHHPq/jWyFPBl6pbXGdzxxaxsx7vQHdrhvelXCufCN6lyeDdvA6d521/I3R+kSOxDbcawJ1KEDIGa0c7cqycuPFS81zR8rC+2UOjFb0yQ3K6bwi+YsQ5l0zWkfimqgvJsaz7v6Tb4zWX5IavIhDnfWCcuot8UXdtaQzKVFZXhwavZsCmxeHRZYXIgYmesDJUPvuiuel9tekLY0s4Nes62hOeA5zQXjolA1XfhKPcz0ieyNGTjuQo2Ws+pjTUnT8YkQIR4ZMFvLzdWA0nJWaNZ5t6LpQZzjiEaNtYjNkoSSlXWHgcuhM7jEOvujCePSV3TCqkPgWjmFxNXTmSMDqOHDC1Or6iAXpgzgfOhMkEMZXoZRpuxo6EyQQxjuZE3MD50NnggTC+JhDkSbkgawCYoEwgTR/d/JSRimEPKYHhPltyAAQk+nYAWn4EJIuq1CxJaz6LGcUwtTFz+OBgzS2dIzLfGboOHQlSc33+Yhg1b46SMK6to4D9HMsXQOg8WzJwHWhDocjJ0xW56odjmnqQi4sgWmLNDvmfoLW2DJWfQzNE0JYmnHCUHryJwMmzMRDZZkzt/NILixjNVQ9+EkJyQZrWYiT55wrC5SarzxTlreQbMDRBkXHjyPiSNXZQEHSea7E5fRhyGfWDozGWDM3+bK6wLkiysp1MXMsVnQfmFy3fHxT0tyx9ofUe42Bo0jjhpC/vHFEEOblqwEFI4B1PcZNB30RPDRrczNZXUM47mjhwa6NxgsmDKUqM+lgRROfLCsDK+Q0JNEcOmMShsSOsPPVcFKXSP7wMBrrU60GAlLCzPeZX3WwSiV16Iz2sxiedzOg5J8yQe99PTo8NIayPtVsIO542+BcrijORw3bHjoDW/ElHWHoU1hO0ed3bozQC9kxWu1/03OQZqx5iXjRkYW1MnQG2r5oeJmJWD514N7lZInkURmTapK+u56SRWez27pcpu461ufIawydqbuuzma3ldfp64BMjc1uTzgRKRSKujvVedM53hr3q9w7XeN+j8Sknbq4X+UGTp4qCnWDI50FDq8stGtDCHUz0Lu84b04UV6JGJzsMhc1BPR7X9bZBYtC3WGNKwB1a7/ebFmnuWUh35LxkUiadV/2DdJ0Cp8M4kjh9u+JuczbzsR9cMnQMUWtCC2BADvi8Y99KLdDG6nrAuwAA6pXX/WhYxvtpk3R2UwVPYwqkkuKjH0njeY20ylK8JbEHEMYjrHnZKEqyvsObYAF1XtufSaN5tbzpRwYlSSXHKT7vpNGkywnHYSaHYbekkaTLG+KIoXZOpU5L0ljQJbSzW6rpijzgX9HTdKMPQLpjulVxJbyon7QYbjTJM3MIwxmmmTZyavzAyY6BZetT1VUZDCoulk56DWybJ1yULxdx9aEE0VHcbmizC1ZJy+SYI0iB1aUaVkRp7gex1THexxY/dbsLiKMjab+TquqvxHRSukuIndQWWxHFlW3j4FvFnVec92E0fZ0lxVw7np5VTByW02Cc0U5r3tfxbVfDa47t7RFuw4GCRBF19tf1bU10mCqbocfB+sB4sG8ZYAmYE0nmqdWWtWG3vbde4HHyRwoyYrpj5GrtaoNvS2DwsG67fI7tI93iywM3tOcEhlFGo0xIQ0mjvXtuhsQRYssFkgjiPNg2+NAmxbMbDApmSwWSNMacdBg2oXhu5lT04hIs2GmpMGhGt8azXj7O1CQa3C3iWE7tMliiTTi3nvAIGugIDNG3+bOClkskYZLBrqwNzUg0AaOwSVrNuVirpNzRwYNbUoarDj884t91PpzASAaFZ3A5wJeUGzhvtMmYaIF0mDF4e35CT9zCQPxvDE8+w/4mTS8rzFZLJIGt4W34wXweDMiwgtJ6y5wnTiDe9qoyM51C1RRxwrjWnZQjLAx4teWwriWIyS3mSUMNoz1bvq6scGIGoL1yPqx9E5r5W7ioEFfxMog1pKoQ6d826WkYDCMjGZkAaxbSLxjjwGat1mlgvBxy/xdGTIDDNIWMUgAA18NaA4Fh0ajyyOLYJnUvXvpVSo8rjAePsmaOSjte+5trPULRpbBmjDzMq9Nojy4VBKF8TDpD2kjX1u77jyWjMeiY+KkzHIZO2oJME6Ya8dKwxVj3xVRKogzc6g0OVjTTohSQZxrx+EqNxb7NkLQyIHSNO0voCjIk+vhF5pKY6O/oC7kaNS/5QAHjEEbBqRx/1bnhFGQh3se0Z8yNiSIqNmnfZnpKJEHY2AauqYSDnnPcJhIOMSGBMEYOPGoUcfACbCqQBMdese+KYaGIcGfsnDz7eNDqNUiBnGFIRXGMnc9oDVIkCBBggQJEiRIkCBBggQJEiRIkCBBggQJEiRIkCBBggQJEiRIkC8gUYCgO/n3f/5L2Vpk+a9//uO+OJYf9zbOqvh7Kl3n/Ts4/qj4/yeRryPLX39Ubo2xx5PzYOJY1WaxS3kMGIxgFisACeHHPKgm/lW0J4dzUnTsGGG7L5tICFPMSzEqzjvB6FtQ204l1jimanQz/i4u+b+JVJ3Lt+z7gdYISGqOjyVFLVt69m3lzuJ7PvlMXt2n6vp8/1W8CkyMjn+pOE8Lo0CYbiVFv58hBUrRS04t3Wvd4NyMfWxdh9t5Wyipam0xrrS/FNfAVv1G8ip8Ps8P5G3EfjbTmvbgczjRmswJWgfCeCxFSJQKQhTh0wQp0Uvx3Z3lezW5XoYXVy+UkpNZrE03Y593eSid7QhD+fEqO3MptMNLeHEvNlFcS24P/32F2rMzyk8IC8gHwvQz95nohnaKc3KR6xgo1g5CqtIwp/heFjGfCbdjJ+cW3FsV5y4RIS8JXjZFhElMcUWLR75jJHvPQJh+yrOlc0wXcrypOURVyJiCYmOFLtu1+4AIQ5mZO1GFfm1gFAgThCIJ2iTpNyn/sL6WAvdECg9V1p4zqT1pm0AEwvRTpgqLPtY8RycxTph6vbVdyXThpSKvUR03Vim4tItbptGepmuP1WIUCNPfYgHOT3LdcxqImFu/r1gk8VixQMkBKfuiIMdOUdVaoN9fCO1JoT2NvB1lUZVAmK9bKNAiUaFMOvmOKul/W8ADkvoU8g7uKfi+oA/gSWIgywSRQUWCVO5QrJEzRUL/aVEVxTGfjguE+cKFgoJEU4uepy7p530cdyhkE6XjMSvfPuXK0kpBN+xzJyluT1XSP8Vh4yjoURDXAnnPeUWCzv9/7uO6c8HD+CNHlHSqEuQ5K+9jeP9O8hhLVtFHQ/Au53A+xcrfs+oKVSaRhv89hY5MsbBjBiHQsSYpzwmkTItrT2vwrsWob4tFBgkSJEiQ3oaTAYL+S5G8JxCSHYswK6/4/iQMK/5ftUTv29CZ4piq5VtxuJWj47Li3Ey+t6p9qA3v56Dv5Hsrnw+OnZQ9Q8nx4r6fjsHXksPWkMMMQ27Y330bWfGyzxVKJb6XK2OvBIMqBkNWCb/eGh0nknouvBNSVM12kG8JxeTfPcrfAcmUG3QV3/HjlvgZQcGfS4iRw/E76atX1PYpOp5jJap2efH3d3yvUCUblnBFewbL7FSAhEKxxqD0XHA/ikyAS/T7E7L8r6x8axCu0H+i69cJx2ILRKjz1DE7HUGwlo1PIMzwZIysto5Mpc858hbif3hD2R36/7yMHBBmiZAnBq8iE4iHY+LcRxQmZnBPce8Mk6DkOXC78PwWylaKt1L4+Gk/zBCSDVMmhWJuixc+Jx6flZWYwcKmYIHxV78U5zywjw5CPvX4HjzBWPIqByBOjIkGXiDBREUWPoVw7BWO4c84VuQouF38HDE4MyHkgZhUyt2mg4cZcF4Dc/qphYM76aO9lToobyaFZXJoNVOEY3j25Dv55HAI/l5XhHiUkOtTyAY5EMbqvsyABA8zPOEvWlR5bgtl+EnMfVQhi8kW3Qf2MaNSLHIhK+hMCseOKJzEz6H0hjX3P0NVrgnyeFlJhU1V1Hgqu3jwMMOTNTudoruVrDlVTMdw4UlheG9TrOh4W/K9bc8KBHiWE3iNaywCYb6QQO6SSla00isV50TS53fDe5+EZVJOkCOPhpN0IXgo/6QsPzMg9VpRVj55digSiOvNyiqNgTDDlSvGOtsL8kFRVDiwz0P1j1JnJSb5Ri4dQ8l5IYV/KnJEUjh5SSB6KpH3JhDma3mZHKxmRjg8UST9dw1ufyj5+6kifDtZRQdCtlde7YP28NDyFYVyO3lkgHStpVSAoDwPbs9CNXogEGb4pLkihC4i6Zc/pvfNJO+2h/8fpLYcajxjzD5GKWCLf2QlZV9J5hIBEo1wkh/7LJMmEGZ4kiuUAMfneYPEXuf4BxR2HRU5S6ryEMgzrhXeMYOQ67xsTBluoxRmxey0dJxXkCwrO+7/AgwAK1mR9LbQ+CoAAAAASUVORK5CYII=";
	private static byte[] IMAGE = Base64.decode(IMAGE_CONTENT);
	private static 	InputStream IMAGE_STREAM = new ByteArrayInputStream(IMAGE);

	private Logger log = LogManager.getLogger(this.getClass());

	@InjectInJersey
	private MonitoramentoMensagemService monitoramentoMensagemService;
	
	@InjectInJersey
	private ParametroGeralService parametro;

	@InjectInJersey
	private LoteCobrancaTerceiraService loteCobrancaTerceiraService;
	
	@InjectInJersey
	private MonitorarMensagemComunicacaoService monitorarMensagemComunicacaoService;
	
	@GET
	@Path("processaMonitoraMensagemComunicacao")
	public Response executeMonitoraMensagemComunicacao() {
		monitorarMensagemComunicacaoService.executaMonitoraMensagemComunicacao();
		return Response.ok().build();
	}
	
	@GET
	@Path("loteCobrancaTerceiraService")
	public Response loteCobrancaTerceiraService(@QueryParam("id") Long id) {
		LoteCobrancaTerceira lote = loteCobrancaTerceiraService.executeGeraArquivoLoteCobrancaTerceira(id);
		byte[] arquivoname = Arrays.copyOfRange(lote.getDcArquivo(), 0, 1024);
		byte[] arquivo = Arrays.copyOfRange(lote.getDcArquivo(), 1024, lote.getDcArquivo().length);
		String name = new String(arquivoname);
		InputStream in = new ByteArrayInputStream(arquivo);
		return Response
				.ok(in, "image/jpeg")
				.header("content-disposition",
						"attachment; filename = " + name).build();
	}
	
	@POST
	@Path("registrarEventoMsg")
	@Consumes(MediaType.APPLICATION_JSON)
	public void registrarEventoMsg(EventoMensagem evt) {
		monitoramentoMensagemService.saveEventoMensagem(evt.getIdMonitMsg(), evt.getTpEvento(), evt.getDsEvento());
	}
	
	@GET
	@Path("imagem")
	public Response imagem(@QueryParam("idMonitMsg") Long idMonitMsg) {
		monitoramentoMensagemService.saveEventoMensagem(idMonitMsg, "I", "LMS-36307");
		return Response
				.ok(IMAGE_STREAM, "image/jpeg")
				.header("content-disposition",
						"attachment; filename = " + "imagem.jpg").build();
	}

}
