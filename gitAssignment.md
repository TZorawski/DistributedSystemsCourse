# Git Assignment

**INDIVIDUAL**

**Entrega**: 15-Jul-21

## Como entregar
Copie o arquivo em um repositorio que seja seu e coloque as respostas nas caixas abaixo

Abra uma issue nesse repositório aqui indicando o link para o arquivo no seu repo.

### Entenda o repositorio
Baixe o arquivo [handson.zip] (handson.zip) e descompacte-o
A pasta handson é um repositório git. Usando a linha de comando, altere o diretório para "handson/"

As caixas vazias
```

```
representam o conteúdo que você precisa preencher e postar em seu repositório privado

1. Descreva qual é a saída dos seguintes comandos
    -  `git branch` 
    -  `git checkout BRANCH_NAME` (USE THE NAME OF AN EXISTING BRANCH)
    -  `git log --decorate`

```
-  `git branch`: lista as branchs existentes.
  feature-foo
* master


-  `git checkout feature-foo`: troca para a branch 'feature-foo'.
Switched to branch 'feature-foo'


- 'git log --decorate': mostra detalhes dos últimos commits.
commit a70a8e9d3c5390e367028faf033f2a9ef03d2e91 (HEAD -> feature-foo)
Author: Igor Steinmacher <igorsteinmacher@gmail.com>
Date:   Fri Aug 24 15:29:22 2018 -0700

    Adding header of method foo()

commit 309b0d73ff9c2163591c9e96e307fe61b4c8f58a
Author: Igor Steinmacher <igorsteinmacher@gmail.com>
Date:   Fri Aug 24 15:27:16 2018 -0700

    Adding class A skeleton

commit 9c1eeb8901b0926ce7fa13cc6ce0a1876fc4179b
Author: Igor Steinmacher <igorsteinmacher@gmail.com>
Date:   Fri Aug 24 15:26:44 2018 -0700

    Creating all files (all empty)
```

2. Tente usar `git log --graph --all`. O que acontece?
```
O terminal mostra detalhes dos últimos commits desenhados em árvore.

```

3. Use `git diff BRANCH_NAME`  para ver as diferenças de um ramo e do ramo atual.
   Sumarize as diferenças do master e do outro ramo.

```
Diferenças do arquivo A.java: foi adicionada a public void foo().
Diferenças do arquivo B.java: foi excluída a public class B.
```

4. Escreva uma sequencia de comandos para mesclar o ramo não-master no `master`

```
git checkout master
git merge feature-foo
```


5. Escreva um comando (ou sequência) para (i) criar um novo ramo chamado `math` (do` master`) e (ii) mudar para este ramo

```
git checkout -b math
```
   
6. Edite B.java adicionando o código abaixo ao conteúdo do arquivo
```java
System.out.println("I know math, look:")
System.out.println(2+2)
```

7. Escreva o comando (ou sequencia) para realizar o commit de suas mudanças
```
git commit -a -m "Adds java code into B"

```

8. Volte para o branch `master` e mude B.java adicionando o seguinte código-fonte (confirme sua mudança para` master`):
```java
System.out.println("Hello World")
```

9. Escreva uma sequência de comando para mesclar o branch `math` em` master` e descreva o que aconteceu
```
git commit -a -m "Adds java code into B again"
git merge math

Resultado: deu conflito entre os códigos que acabamos de adicionar nas duas branchs.
Auto-merging B.java
CONFLICT (content): Merge conflict in B.java
Automatic merge failed; fix conflicts and then commit the result.
```
   
10. Escreva um conjunto de comandos para abortar a mesclagem
```
git merge --abort
```
   
11. Agora repita o item 9, mas prossiga com a mesclagem manual (Editando B.java). Todas as funções implementadas são necessárias. Explique o seu procedimento
```
Eu excluí as anotações que o git havia feito, mantendo as duas funções, que estavam entre as anotações, no arquivo.

```

12. Escreva um comando (ou conjunto de comandos) para prosseguir com a mesclagem e atualizar o branch `master`
```
 git commit -a -m "Merges code from math into master"
```



