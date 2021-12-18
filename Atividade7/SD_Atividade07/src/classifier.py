# Descrição: Uso de comunicação indireta para criar um
# serviço de notificação de tweets de determinados tópicos
# O classifier recebe os tweets do collector e os trata
# e envia ao client.
# Autores: Guilherme Vasco da Silva, Thais Zorawski
# Data da criação: 17/12/2021
# Data de atualização: 18/12/2021

import pika

def main():
    #Conexão com o RabbitMQ
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    #Criação do canal
    channel = connection.channel()

    #Declaração da fila
    channel.queue_declare(queue='tweets')

    print('Aguardando coletor para continuar')

    #Função para o consumo de mensagens
    def callback(ch, method, properties, body):
            key = []
            #Decodificação do tweet
            data = body.decode()
            #Verificação de palavras chave e apêndice ao vetor key
            if 'verstappen' in data.lower():
                key.append('verstappen')
            if 'hamilton' in data.lower():
                key.append('hamilton')
            if 'bottas' in data.lower():
                key.append('bottas')
            if 'perez' in data.lower():
                key.append('perez')
            if 'sainz' in data.lower():
                key.append('sainz')
            if 'norris' in data.lower():
                key.append('norris')
            if 'leclerc' in data.lower():
                key.append('leclerc')
            if 'ricciardo' in data.lower():
                key.append('ricciardo')
            if 'gasly' in data.lower():
                key.append('gasly')
            if 'alonso' in data.lower():
                key.append('alonso')

            #Impressão de confirmação
            print('Enviando log ao cliente.')

            channel.exchange_declare(exchange='direct_logs', exchange_type='direct')
            #Envia tweet para a fila de tópico correspondente
            for chave in key:
                channel.basic_publish(exchange='direct_logs', routing_key=str(chave), body=data)
    
    #Definição da função de consumo de mensagens
    channel.basic_consume(queue='tweets', on_message_callback=callback, auto_ack=True)
    #Inicia o consumo de mensagens
    channel.start_consuming()

#Chamada padrão da main
if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print("Finalizado")