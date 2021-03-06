package LaneAttack;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AtorNetGames implements OuvidorProxy {

    protected AtorJogador atorJogador;
    protected Proxy proxy;
    private boolean conectado = false;
    private boolean aguardandoJogador;

    

    public AtorNetGames(AtorJogador atorJogador) {
        proxy = Proxy.getInstance();
        proxy.addOuvinte(this);
        this.atorJogador = atorJogador;
    }

    @Override
    public void iniciarNovaPartida(Integer posicao) {
        System.out.println(posicao);
        aguardandoJogador = false;
        atorJogador.tratarInicio(posicao);
    }

    public boolean conectar(String nome) {
        try {
            proxy.conectar("localhost", nome);
            conectado = true;
        } catch (Exception e) {
            e.printStackTrace();
            conectado = false;
        }
        return conectado;
    }

    public void iniciarPartida() {
        try {
            proxy.iniciarPartida(2);
        } catch (NaoConectadoException e) {
            e.printStackTrace();
        }
    }

    public void enviarJogada(Jogada jogada) {
        try {
            proxy.enviaJogada(jogada);
        } catch (NaoJogandoException e) {
            e.printStackTrace();
        }
    }
    
    public String informarNomeAdversario(String idUsuario) {
		String aux1 = proxy.obterNomeAdversario(new Integer(1));
		String aux2 = proxy.obterNomeAdversario(new Integer(2));;
		if (aux1.equals(idUsuario)){
			return aux2;
		} else {
			return aux1;
		}		
}

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isAguardandoJogador() {
        return aguardandoJogador;
    }

    public void setAguardandoJogador(boolean aguardandoJogador) {
        this.aguardandoJogador = aguardandoJogador;
    }

    @Override
    public void receberJogada(Jogada jogada) {
        Composicao composicao = (Composicao) jogada;
        try {
            atorJogador.receberJogada(composicao);
        } catch (InterruptedException ex) {
            Logger.getLogger(AtorNetGames.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean desconectar() {
        try {
            proxy.desconectar();
            return true;
        } catch (NaoConectadoException e) {
            JOptionPane.showMessageDialog(atorJogador.informarJanela(), e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void finalizarPartidaComErro(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receberMensagem(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tratarConexaoPerdida() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tratarPartidaNaoIniciada(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
