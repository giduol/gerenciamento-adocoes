package io.github.go.gerenciamento_adocoes;

import java.io.Console;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GerenciamentoAdocoesApplication {

	public static void main(String[] args) {
		if (args.length == 1 && "--gerar-hash".equals(args[0])) {
			gerarHashBcrypt();
			return;
		}
		SpringApplication.run(GerenciamentoAdocoesApplication.class, args);
	}

	private static void gerarHashBcrypt() {
		Console console = System.console();
		if (console == null) {
			throw new IllegalStateException(
					"Este modo precisa ser executado em um terminal que ofereça entrada segura de senha."
			);
		}

		char[] senha = console.readPassword("Digite a senha para gerar o hash BCrypt: ");
		if (senha == null || senha.length == 0) {
			throw new IllegalArgumentException("A senha não pode estar vazia.");
		}

		try {
			System.out.println(new BCryptPasswordEncoder().encode(new String(senha)));
		} finally {
			Arrays.fill(senha, '\0');
		}
	}

}
