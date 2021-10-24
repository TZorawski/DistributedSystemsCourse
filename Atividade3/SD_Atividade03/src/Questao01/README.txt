Descrição: Servidor utilizando TCP que suporta mensagens de múltiplos clientes e
processa mensagens do tipo "CONNECT user,password", "PWD", "CHDIR path","GETFILES",
"GETDIRS" e "EXIT". (TCPServer.java) e Cliente utilizando TCP que envia mensagens ao
servidor com os comandos descritos (TCPClient.java).

Autores: Guilherme Vasco da Silva, Thais Zorawski
Data da criação: 15/10/2021
Data de atualização: 18/10/2021

Execução: Deve ser executado na ordem 1º servidor e 2º cliente, podendo ter mais de
um cliente simultaneamente, contato que o servidor já esteva aberto. A execução em java
pode ser feita através do terminal, com o comando "java TCPServer.java" para executar
o servidor e "java TCPClient.java" para os clientes.

Bibliotecas: Scanner (leitura do teclado no cliente), Big Integer (valores do tipo big
integer, utilizado para a criptografia SHA-512) e Message Digest (utilizada para gerar
a criptografia SHA-512).

Exemplo de uso: O programa pode ser executado para navegar nos diretórios da máquina
e conferir quais os diretórios e arquivos disponíveis. Diferentes clientes podem
verificar diferentes diretórios simultaneamente.