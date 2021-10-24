# Questão 02

Descrição: Serviço de armazenamento de arquivos TCP em que cliente e servidor se comunicam através de protocolo binário.
 
Autores: Guilherme Vasco da Silva, Thais Zorawski
Data da criação: 18/10/2021
Data de atualização: 24/10/2021

## Como compilar
Sugerimos que use a IDE Netbeans para rodar a aplicação.

## Como executar
Dentro do pacote Questão 2, clique com o botão direito no arquivo TCPServer.java e selecione a opção "Executar Arquivo" para iniciar o servidor. Clique com o botão direito no arquivo TCPClient.java e selecione a opção "Executar Arquivo" para iniciar o cliente.

No terminal do cliente, você pode escolher as opções:
- ADDFILE: envia um arquivo ao servidor.
- DELETE: remove um arquivo do servidor.
- GETFILESLIST: exibe a lista de arquivos do servidor.
- GETFILE: recebe um arquivo do servidor.
- EXIT: encerra a comunicação com o servidor.

Há duas pastas que são utilizadas para o armazenamento dos arquivos de transferência:
- clientStorage: armazena os arquivos locais do cliente.
- serverStorage: armazena os arquivos locais do servidor.

No terminal do servidor é possível visualizar os logs das operações.

## Exemplo de uso
- ADDFILE: envia um arquivo ao servidor:
>>> ADDFILE arquivo4.txt

- DELETE: remove um arquivo do servidor:
>>> DELETE arquivo4.txt

- GETFILESLIST: exibe a lista de arquivos do servidor:
>>> GETFILESLIST

- GETFILE: recebe um arquivo do servidor:
>>> GETFILE arquivo1.txt

- EXIT: encerra a comunicação com o servidor:
>>> EXIT