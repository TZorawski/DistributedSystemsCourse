import socket
import addressbook_pb2

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 7000))

matricula = addressbook_pb2.Matricula()
while(1):
	comm = input('> ') #recebe entrada
	parts = comm.split(' ', 1) #separa a entrada por ' '

	#INSERT RA,cod_disciplina,ano,semestre
	if parts[0] == 'INSERT': #comando insert
		values = parts[1].split(',')
		if len(values) != 4:
			print('Erro nas entradas')
		else:
			matricula.RA = int(values[0])
			matricula.cod_disciplina = values[1]
			matricula.ano = int(values[2])
			matricula.semestre = int(values[3])
		
	elif parts[0] == 'UPDATE': #comandos update
		sec = parts[1]
		tmp = sec.split(' ')
		values = tmp[1].split(',')
		
		#UPDATE nota RA,cod_disciplina,nota
		if tmp[0] == 'nota' and len(values) == 3:
			matricula.RA = int(values[0])
			matricula.cod_disciplina = values[1]
			matricula.nota = float(values[2])
			
		#UPDATE faltas RA,cod_disciplina,faltas
		elif tmp[0] == 'faltas' and len(values) == 3:
			matricula.RA = int(values[0])
			matricula.cod_disciplina = values[1]
			matricula.faltas = int(values[2])
			
		else:
			print('Erro nas entradas')

	elif parts[0] == 'LIST': #comandos list
		sec = parts[1]
		tmp = sec.split(' ')
		values = tmp[1].split(',')
		
		#LIST alunos cod_disciplina,ano,semestre
		if tmp[0] == 'alunos' and len(values) == 3:
			matricula.cod_disciplina = values[0]
			matricula.ano = int(values[1])
			matricula.semestre = int(values[2])
		
		#LIST all RA,ano,semestre
		elif tmp[0] == 'all' and len(values) == 3:
			matricula.RA = int(values[0])
			matricula.ano = int(values[1])
			matricula.semestre = int(values[2])
			
		else:
			print('Erro nas entradas')
		
	#END
	elif parts[0] == 'END': #comando END
		break
		
	else: #comando inválido
		print('Comando inválido')	

	msg = matricula.SerializeToString()
	size = len(msg)

	client_socket.send((str(size) + "\n").encode())
	client_socket.send(msg)
	
	rec = client_socket.recv(1024)
	print('# ', rec)

client_socket.close()
