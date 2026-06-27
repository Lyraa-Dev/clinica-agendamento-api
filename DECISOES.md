# Decisões Técnicas

Aqui explico, as escolhas que fiz no projeto e o raciocínio por trás delas, muitas delas acabei reutilizando de projetos anteriores (EX: validação de CPF e o registro de agendamento).

---

## 1. Principais decisões técnicas

**Separei o projeto em camadas (Controller → Service → Repository).**
Quis manter cada parte com uma responsabilidade só: o controller cuida do HTTP, o service guarda as regras de negócio e o repository fala com o banco. Fiz assim porque deixa o código mais fácil de entender e, principalmente, deixa as regras isoladas o que permite testá-las sem subir banco nenhum.

**Não exponho as entidades direto na API; uso DTOs.**
A API recebe e devolve DTOs, nunca a entidade do banco. Optei por isso para ter controle sobre o que entra e sai: o cliente não manda `id` nem `status` na criação, por exemplo, porque quem decide isso é o servidor. Em um caso hipotético, se o banco mudar amanhã, o contrato da API não quebra junto.

**No agendamento, o cliente manda só os IDs de paciente e profissional.**
Quando alguém agenda uma consulta, o paciente e o médico já existem no sistema, então faz mais sentido referenciar pelo ID do que mandar os dados completos de novo. O service valida se esses IDs existem e, se não existirem, devolve 404. Isso evita duplicar dado e mantém um lugar só como fonte da verdade.

**Validei as regras em dois níveis.**
As validações de formato (campo obrigatório, data que não pode ser no passado) ficam no DTO; as regras de negócio (CPF repetido, horário ocupado) ficam no service. Fiz questão de não deixar a regra de negócio dependendo só da validação de entrada se uma falhar, a outra ainda segura.

**Centralizei o tratamento de erro num lugar só.**
Em vez de encher os controllers de try/catch, usei um `@RestControllerAdvice` que pega as exceções e transforma em resposta HTTP padronizada, 400 pra regra de negócio, 404 pra coisa que não existe. Assim o resto do código fica limpo e o erro sai sempre no mesmo formato.

**O cancelamento é um PATCH e não apaga nada.**
Como o desafio pede pra manter o registro, cancelar não podia ser um DELETE. Tratei como uma atualização parcial: muda o status pra CANCELADO e grava o motivo, mas o agendamento continua lá, além de liberar o horario de agendamento do doutor. Por isso escolhi PATCH, que é o verbo certo pra atualização parcial.

**Defini a unicidade pensando no mundo real da clínica.**
Paciente não pode ter CPF repetido; profissional não pode ter registro (CRM/CRO) repetido. Escolhi o registro porque é o que de fato identifica um profissional de saúde e apliquei o mesmo cuidado nas duas entidades.

**Usei H2 em modo Oracle.**
Rodei o H2 em memória com `MODE=Oracle`. A ideia foi matar dois coelhos: quem for avaliar roda o projeto sem instalar banco nenhum, e ao mesmo tempo eu demonstro a compatibilidade com Oracle que era um diferencial. No README deixei explicado como apontar pra um Oracle de verdade.

**Mantive os IDs numéricos e diferencio na tela.**
Cheguei a pensar em usar IDs tipo "P1" e "C1" pra separar profissional de paciente, mas voltei atrás: a chave primária deve ser um número limpo, sem significado embutido. Resolvi isso na interface, que mostra "C1", "P1" pro usuário, sem bagunçar o banco. Ou seja, apenas separei como o dado é guardado de como ele é mostrado.

---

## 2. O que priorizei e o que ficou de fora

**O que priorizei:**
Foquei primeiro em entregar tudo que era obrigatório, bem feito: os cinco endpoints, as regras de negócio, a persistência, os testes e o tratamento de erro. Preferi caprichar na clareza e na organização do que sair adicionando funcionalidade. Os testes automatizados eu fiz questão de colocar nas regras de negócio, que é onde mora a lógica que mais importa. Depois disso, fui atrás dos diferenciais que somavam de verdade: Swagger, compatibilidade Oracle e a interface em Angular.

**O que deixei de fora, de propósito:**

- **Duração da consulta.** Hoje bloqueio o conflito de horário exato do profissional. A evolução seria dar uma duração à consulta e barrar sobreposição (ex.: 14:00–14:30 e 14:15 com o mesmo médico). Deixei de fora pra não expandir o modelo a um dia da entrega.

- **Paciente em dois lugares ao mesmo tempo.** Reparei que hoje um paciente consegue marcar duas consultas no mesmo horário com médicos diferentes. Dava pra resolver fácil, espelhando a regra que já tenho pro profissional, mas como também está fora do que o enunciado pede, deixei anotado como melhoria.

- **Padronização de dados de entrada.** A especialidade é texto livre, então variações de digitação ("Cardiologia" vs "cardiologia") criam duplicatas, o ideal seria um enum ou tabela de especialidades. O registro (CRM) também aceita formato livre, sem máscara ou validação de padrão. Decidi deixar ambos como estão para não expandir, mas são pontos claros de evolução para garantir consistência.

- **Autenticação e paginação.** Não foram pedidas. Num sistema real eu colocaria a busca da interface, por exemplo, é feita no front e, com muito dado, eu migraria pra uma busca paginada no servidor.

- **Banco que sobrevive ao reinício.** O H2 em memória zera quando a aplicação reinicia. Pra avaliação eu até prefiro assim, porque o estado começa limpo e previsível. Em produção, usaria um banco persistente e controlaria o schema com migrations (tipo Flyway) em vez do `ddl-auto`.

---

## 3. Uso de IA

Usei IA como apoio durante o desenvolvimento, do jeito que eu usaria a documentação ou pediria ajuda pra alguém mais experiente. As decisões de arquitetura, de modelagem e das regras de negócio foram minhas a IA me ajudou a ir mais rápido e a confirmar boas práticas.

**Onde ela me ajudou:**
- Na configuração do H2 em modo Oracle e num detalhe chato de ordem do `data.sql` (as tabelas precisavam existir antes do seed rodar, além disso, como não tinha experiencia direta com Oracle, foi uma sugestão dada pela IA utilizar o H2).
- Em partes repetitivas como o Autocomplete do Copilot, a partir do padrão que eu já tinha definido, resolução de falhas/erros que vieram a ocorrer, os DTOs, as conversões entre entidade e DTO, a estrutura dos testes e verificação de erros. Tudo que solicitei revisei e adaptei.
- Pra tirar dúvida no frontend, sobre a versão mais nova do Angular, e falhas/erros que vieram a ocorrer.
- Em alguns detalhes do Spring Data, como montar os métodos de consulta e a query dos filtros opcionais, além da criação de dados sinteticos para o insert inicial.

**Como eu validei:**
- Fui testando cada camada antes de seguir pra próxima, vendo funcionar de verdade no Swagger, no console do H2 e na própria interface.
- Rodei os testes automatizados pra garantir que as regras se comportavam como eu esperava, inclusive depois de mexer em coisas.
- Revisei tudo que veio pra garantir que eu entendia cada pedaço e que batia com a arquitetura que eu tinha definido o que não encaixava, eu ajustava ou descartava.
- Em pelo menos um caso isso fez diferença: ao revisar a validação de data, percebi que a regra precisava estar alinhada entre o DTO e o service, e ajustei os dois pra não se contradizerem.

Resumindo: usei IA pra ganhar velocidade e aprender no caminho, mas mantive as decisões na minha mão e entendo todo o código que entreguei.