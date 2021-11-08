Descrição: sistema de upload de arquivos via UDP. O servidor UDP receberá as 
partes dos arquivos (1024 bytes), verificará ao final a integridade via um 
checksum (SHA-1) e armazenará o arquivo em uma pasta padrão.
Sugestões: o servidor pode receber o nome e tamanho do arquivo como o primeiro
pacote e o checksum como o último. Testar o servidor com arquivos textos e 
binários (ex: imagens, pdf) de tamanhos arbitrários (ex: 100 bytes, 4KiB,
4MiB). O protocolo para a comunicação deve ser criado e especificado textualmente
ou graficamente.
 
Autores: Guilherme Vasco da Silva, Thais Zorawski
Data da criação: 31/10/2021
Data de atualização: 01/11/2021

Execução: Dentro do pacote Questão 2, clique com o botão direito no arquivo 
TCPServer.java e selecione a opção "Executar Arquivo" para iniciar o servidor.
Clique com o botão direito no arquivo TCPClient.java e selecione a opção
"Executar Arquivo" para iniciar o cliente.


Bibliotecas: Scanner (leitura do teclado nos clientes), Integer (valores do tipo
integer, utilizado para a conversão em byte[]).

Exemplo de uso: O programa pode ser utilizado para armazenar arquivos em um 
servidor de arquivos dentro de um mesmo localhost ou em redes diferentes.