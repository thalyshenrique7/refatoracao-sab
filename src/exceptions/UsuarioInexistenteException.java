package exceptions;

@SuppressWarnings("serial")
public class UsuarioInexistenteException extends Exception {
	public UsuarioInexistenteException(String message)
    {
       super(message);
    }
}
