package models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import exceptions.AdicionarLivroInexistenteException;
import exceptions.BuscaUsuarioComNomeNuloException;
import exceptions.BuscaUsuarioComNomeVazioException;
import exceptions.DevolveLivroDisponivelParaEmprestimoException;
import exceptions.DevolveLivroNuloParaEmprestimoException;
import exceptions.LivroIndisponivelParaEmprestimoException;
import exceptions.LivroOuUsuarioNulosException;
import exceptions.TituloOuAutorNuloException;
import exceptions.TituloOuAutorVazioException;
import exceptions.UsuarioComNomeVazioException;
import exceptions.UsuarioInexistenteException;
import exceptions.UsuarioJaRegistradoException;

public class Biblioteca {
	
	private String nome;
	private int nrUnico = 0; // nrUnico > zero!
	private TreeSet<Livro> repositorioDeLivros;
	private HashSet<Usuario> listaDeUsuarios;

	public Biblioteca(String nome) {
		this.nome = nome;
		repositorioDeLivros = new TreeSet<Livro>();
		listaDeUsuarios = new HashSet<Usuario>();
	}

	public void adicionaLivroCatalogo(Livro livro)
			throws AdicionarLivroInexistenteException {
		if (livro != null) {
			livro.setNrCatalogo(this.getNrUnico());
			repositorioDeLivros.add(livro);
		} else
			throw new AdicionarLivroInexistenteException(
					"--->Não pode adicionar livro inexistente!");
	}

	public void registraUsuario(String nome)
			throws UsuarioJaRegistradoException, UsuarioComNomeVazioException,
			UsuarioInexistenteException {
		if (nome == null) throw new UsuarioInexistenteException("--->Não pode registrar usuário inexistente!");
			if (nome.isEmpty()) throw new UsuarioComNomeVazioException("--->Não pode registrar usuário com nome vazio!");
				Usuario usuario = new Usuario(nome);
				
				if (listaDeUsuarios.contains(usuario)) throw new UsuarioJaRegistradoException("--->Já existe usuário com o nome \""
						+ nome + "\"! Use outro nome!");
					listaDeUsuarios.add(usuario);
				}

	public void emprestaLivro(Livro livro, Usuario usuario)
			throws LivroIndisponivelParaEmprestimoException,
			LivroOuUsuarioNulosException {
		if ((livro == null) && (usuario == null))
			throw new LivroOuUsuarioNulosException(
					"--->Livro e Usu�rio inexistentes!");
		if (livro != null) {
			if (usuario != null) {
				if (livro.getUsuario() == null) {
					usuario.anexaLivroAoUsuario(livro);
					livro.anexaUsuarioAoLivro(usuario);
				} else
					throw new LivroIndisponivelParaEmprestimoException(
							"--->Livro " + livro
									+ " indisponível para empréstimo!");
			} else
				throw new LivroOuUsuarioNulosException(
						"--->Não pode emprestar livro a Usuário inexistente!");
		} else
			throw new LivroOuUsuarioNulosException(
					"--->Não pode emprestar livro inexistente!");
	}

	public void devolveLivro(Livro livro)
			throws DevolveLivroDisponivelParaEmprestimoException,
			DevolveLivroNuloParaEmprestimoException {
		if (livro != null) {
			Usuario usuario = livro.getUsuario();
			if (usuario != null) {
				usuario.desanexaLivroDoUsuario(livro);
				livro.desanexaUsuarioDoLivro();
			} else
				throw new DevolveLivroDisponivelParaEmprestimoException(
						"---> Tentou devolver livro " + livro
								+ " que está disponível para empréstimo!");
		} else
			throw new DevolveLivroNuloParaEmprestimoException(
					"--->Não pode emprestar livro inexistente!");
	}

	public Livro buscaLivroPorNrCatalogo(int nrUnico) {
		// nrUnico <= zero devolve nulo: n�o encontrou livro algum!
		Livro livroAchado = null;
		Iterator<Livro> iter = repositorioDeLivros.iterator();
		while ((iter.hasNext() == true) && (livroAchado == null)) {
			Livro livro = (Livro) iter.next();
			int oNrUnico = livro.getNrCatalogo();
			if (oNrUnico == nrUnico)
				livroAchado = livro;
		}
		return livroAchado;
	}

	public Livro buscaLivroPorTituloAutor(String titulo, String autor)
			throws TituloOuAutorVazioException, TituloOuAutorNuloException {
		Livro livroAchado = null;
		if ((titulo != null) && (autor != null)) {
			if (!titulo.isEmpty() && !autor.isEmpty()) {
				Iterator<Livro> iter = repositorioDeLivros.iterator();
				while ((iter.hasNext() == true) && (livroAchado == null)) {
					Livro livro = (Livro) iter.next();
					String oTitulo = livro.getTitulo();
					String oAutor = livro.getAutor();
					if ((oTitulo.equals(titulo)) && (oAutor.equals(autor))) {
						livroAchado = livro;
					}
				}
			} else
				throw new TituloOuAutorVazioException(
						"--->Nome do titulo e/ou do autor �(são) vazio(s)<<<");
		} else
			throw new TituloOuAutorNuloException(
					"--->Nome do titulo e/ou do autor �(são) nulo(s)<<<");
		return livroAchado;
	}

	public Usuario buscaUsuarioPorNome(String nome)
			throws BuscaUsuarioComNomeVazioException,
			BuscaUsuarioComNomeNuloException {
		Usuario usuarioAchado = null;
		if ((nome != null)) {
			if (!nome.isEmpty()) {
				Iterator<Usuario> iter = listaDeUsuarios.iterator();
				while ((iter.hasNext() == true) && (usuarioAchado == null)) {
					Usuario usuario = (Usuario) iter.next();
					String oNome = usuario.getNome();
					if (oNome == nome) {
						usuarioAchado = usuario;
					}
				}
			} else
				throw new BuscaUsuarioComNomeVazioException(
						"--->Nome do usuário � vazio<<<");
		} else
			throw new BuscaUsuarioComNomeNuloException(
					"--->Nome do usuário é nulo<<<");
		return usuarioAchado;
	}

	public void exibeLivrosDisponiveis() {
		System.out.println("Biblioteca: " + nome);
		System.out.println(">>>Livros Disponíveis para Empréstimo<<<");
		if (repositorioDeLivros.size() != 0) {
			Iterator<Livro> iter = repositorioDeLivros.iterator();
			while (iter.hasNext() == true) {
				Livro livro = (Livro) iter.next();
				if (livro.getUsuario() == null) {
					livro.exibe();
				}
			}
		} else
			System.out.println("---> Nenhum livro no repositório");
		System.out.println("<<< Livros Disponíveis >>>");
		System.out.println();
	}

	public void exibeLivrosEmprestados() {
		System.out.println("Biblioteca: " + nome);
		System.out.println(">>>Livros Emprestados<<<");
		if (repositorioDeLivros.size() != 0) {
			Iterator<Livro> iter = repositorioDeLivros.iterator();
			while (iter.hasNext() == true) {
				Livro livro = (Livro) iter.next();
				if (livro.getUsuario() != null) {
					System.out.println("\t\t"
							+ "--------------------------------------------");
					livro.exibe();
				}
			}
		} else
			System.out.println("---> Nenhum livro no repositório");
		System.out.println("<<< Livros Emprestados >>>");
		System.out.println();
	}

	public void exibeUsuarios() {
		System.out.println("Biblioteca: " + nome);
		System.out.println(">>>Usuários da Biblioteca<<<");
		if (listaDeUsuarios.size() != 0) {
			Iterator<Usuario> iter = listaDeUsuarios.iterator();
			while (iter.hasNext() == true) {
				Usuario usuario = (Usuario) iter.next();
				usuario.exibe();
			}
		} else
			System.out.println("---> Nenhum usuário na Biblioteca");
		System.out.println("<<< Usu�rios >>>");
		System.out.println();
	}

	private int getNrUnico() {
		// Assumo que cada livro recebe um nrUnico diferente
		return nrUnico = nrUnico + 1;
	}

	public int sizeRepositorioLivros() {
		return repositorioDeLivros.size();
	}

	public int sizeUsuarios() {
		return listaDeUsuarios.size();
	}

	
}
