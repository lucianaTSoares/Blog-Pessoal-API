package org.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.model.UsuarioLogin;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe.", null);

		usuario.setSenha(criptografaSenha(usuario.getSenha()));
		return Optional.of(repository.save(usuario));
	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = repository.findByUsuario(usuario.getUsuario());

			if (buscaUsuario.isPresent()) {
				if (buscaUsuario.get().getId() != usuario.getId())
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe.", null);
			}
			usuario.setSenha(criptografaSenha(usuario.getSenha()));
			return Optional.of(repository.save(usuario));
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.", null);
	}

	public Optional<UsuarioLogin> logar(Optional<UsuarioLogin> usuarioLogin) {

		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());
		if (usuario.isPresent()) {
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setTipo(usuario.get().getTipo());
				usuarioLogin.get().setToken(generatorBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
                usuarioLogin.get().setSenha(usuario.get().getSenha());

				return usuarioLogin;
			}
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.", null);
	}

	private String criptografaSenha(String senha) {
		String senhaEncoder = encoder.encode(senha);
		return senhaEncoder;
	}

	private boolean compararSenhas(String senhaDigitada, String senhaDB) {
		return encoder.matches(senhaDigitada, senhaDB);
	}

	private String generatorBasicToken(String email, String password) {
		String structure = email + ":" + password;
		byte[] structureBase64 = Base64.encodeBase64(structure.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(structureBase64);
	}
}