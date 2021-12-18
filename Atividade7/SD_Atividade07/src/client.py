# Descrição: Uso de comunicação indireta para criar um
# serviço de notificação de tweets de determinados tópicos
# O client cria filas com o(s) tópico(s) desejado(s)
# Autores: Guilherme Vasco da Silva, Thais Zorawski
# Data da criação: 17/12/2021
# Data de atualização: 18/12/2021

import pika
import sys

def main():
    #Conexão com o RabbitMQ
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost'))
    #Criação do canal
    channel = connection.channel()

    #Declaração da fila
    channel.exchange_declare(exchange='direct_logs', exchange_type='direct')

    result = channel.queue_declare(queue='', exclusive=True)
    queue_name = result.method.queue

    #Lista de topicos que podem ser selecionados
    filas = ["hamilton", "verstappen", "mercedes", "redbull"]
    topicos = sys.argv[1:]

    #Verificação se o tópico é válido
    if not topicos:
        print(
            "Siga um dos tópicos: [hamilton] [verstappen] [mercedes] [redbull]", end="")
        sys.exit(1)

    for i in range(len(topicos)):
        if topicos[i] not in filas:
            print(
                "Siga um dos tópicos : [hamilton] [verstappen] [mercedes] [redbull]", end="")
            sys.exit(1)

    #Cria a fila
    for topico in topicos:
        channel.queue_bind(exchange='direct_logs', queue=queue_name, routing_key=topico)

    print('Aguardando logs, para cancelar pressione CTRL+C')

    #Exibe os logs retornados
    def callback(ch, method, properties, body):
        data = body.decode()
        print("------------------------------------------------------\n")
        print("Tópico: %r" % (method.routing_key))
        print(data)


    #Define a função de consumo de mensagens
    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)
    #Inicia o consumo de mensagens
    channel.start_consuming()

#Chamada padrão da main
if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print("Finalizado")