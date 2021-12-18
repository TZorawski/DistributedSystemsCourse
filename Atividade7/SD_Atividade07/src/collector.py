# Descrição: Uso de comunicação indireta para criar um
# serviço de notificação de tweets de determinados tópicos
# O collector obtém os tweets através da API tweepy,
# utilizando chaves com acesso elevado.
# Autores: Guilherme Vasco da Silva, Thais Zorawski
# Data da criação: 17/12/2021
# Data de atualização: 18/12/2021

import pika
import tweepy

def main():
    #Leitura do arquivo de tokens
    with open('tokens.txt', 'r') as f:
        tokens = f.read().splitlines()
    #Autenticação com API key e API key secret
    auth = tweepy.OAuthHandler(tokens[0], tokens[1])
    #Acesso com Access token e Access token secret
    auth.set_access_token(tokens[2], tokens[3])
    #Autenticação pela API
    api = tweepy.API(auth)
    #Pesquisa por "formula um" no Twitter
    public_tweets = api.search_tweets(q='formula um',result_type='mixed',count=100)

    #Conexão com o RabbitMQ
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost'))
    #Criação do canal
    channel = connection.channel()
    #Percorre todos os tweets
    for tweet in public_tweets:
        #Armazena o nome de usuário do autor do tweet
        user = "Usuário: " + tweet.user.name
        #Armazena o conteúdo do tweet
        twt = "Tweet: " + tweet.text
        dadoUserTweet = user + "\n" + twt + "\n"
        #Troca as mensagens recebidas de callback da fila tweet
        channel.exchange_declare(exchange='tweets', exchange_type='direct')
        #Envia o que foi recebido da fila de tweets
        channel.basic_publish(exchange='', routing_key='tweets', body= dadoUserTweet)


    # Fecha a conexão
    connection.close()

#Chamada padrão da main
if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print("Finalizado")