package exceptions;

@SuppressWarnings("serial")
public class LivroIndisponivelParaEmprestimoException extends Exception {
	public LivroIndisponivelParaEmprestimoException(String message)
    {
       super(message);
    }
}