Descrição: Chat P2P que possibilita dois clientes trocarem mensagens entre si.
As mensagens possuem o formato: Message Type (1 byte) + Nick Size (1 byte) +
Nick [Nick Size] (1-64 bytes) + Message Size (1 byte) + Message [Message Size]
(0-255 bytes).
Os tipo de mensagem são: 1 - mensagem normal, 2 - emoji (inativo), 3 - URL,
4 - ECHO.


Autores: Guilherme Vasco da Silva, Thais Zorawski
Data da criação: 22/10/2021
Data de atualização: 27/10/2021

Execução: Devem ser executados dois clientes (UDPClient.java), isso é possível através
do comando "java UDPClient.java". Após iniciados, é necessário escolher IP (apenas Enter
para localhost), é mostrada sua porta para ser passada ao outro cliente e digitada a porta
do outro cliente. Inserir seu nickname e digitar qual mensagem deseja ser enviada. No momento,
a atualização de mensagens recebidas é manual.

Bibliotecas: Scanner (leitura do teclado nos clientes), Integer (valores do tipo
integer, utilizado para a conversão em byte[]).

Exemplo de uso: O programa pode ser utilizado para comunicação entre dois clientes
diferentes dentro de um mesmo localhost.