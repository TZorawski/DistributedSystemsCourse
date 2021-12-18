# Atividade de Serviços de Mensagens

## Serviço de notificação de tweets de determinados tópicos

### Antes de executar
Antes de começar a execução é necessário ter o RabbitMQ instalado:
> - <https://www.rabbitmq.com/>

Também será necessária a instalação dos pacotes Pika e Tweepy no Python:
> - pip install pika
> - pip install tweepy

### Arquivo de Tokens
O arquivo de tokens (tokens.txt) deve ser preenchido com suas próprias credenciais de desenvolvedor do Twitter, será necessário o nível de acesso elevado.

### Execução
A execução deve ser feita na seguinte sequência:
- Primeiro deve ser executado o arquivo client.py
> - python client.py [topico(s)]
- Segundo deve ser executado o arquivo classifier.py
> - python classifier.py
- Por último é executado o arquivo collector.py
> - python collector.py
Os resultados serão impressos no terminal contendo o client.py em execução.

#### Exemplo de uso
São quatro tópicos possíveis disponibilizados no exemplo, todos ligados à Formula 1:
- hamilton
- verstappen
- mercedes
- redbull
O arquivo client.py deve ser executado escolhendo um ou mais tópicos nessa lista:
> - python client.py hamilton mercedes
A seguir são executados o classifier e o collector:
> - python classifier.py
> - python collector.py
Os tweets resultantes dos tópicos serão impressos na tela do terminal client.py.
