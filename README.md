# barbearia-api
# Barbearia API ✂️💈

API desenvolvida em **Spring Boot** para gerenciamento de uma barbearia, incluindo cadastro de clientes, agendamento de serviços e autenticação via JWT.

---

## 🚀 Tecnologias
- [Spring Boot](ca://s?q=Spring_Boot) 3.2.5
- [PostgreSQL](ca://s?q=PostgreSQL) (Render Free Tier)
- [Hibernate JPA](ca://s?q=Hibernate_JPA)
- [JWT](ca://s?q=JWT_autenticação)
- [Lombok](ca://s?q=Lombok)

---

## ⚙️ Configuração

### Variáveis de ambiente
Configure no Render ou localmente:

```properties
DB_URL=jdbc:postgresql://<host>:5432/<database>
DB_USER=<usuario>
DB_PASSWORD=<senha>
JWT_SECRET=<sua_chave_secreta>
DEV_OWNER_KEY=<sua_chave_de_controle>

📦 Build & Deploy
Local

mvn clean install
mvn spring-boot:run

Render
Conecte o repositório GitHub ao Render.

Configure as variáveis de ambiente.

O Render faz o build automático e inicia a aplicação.

🔑 Autenticação
JWT Token obrigatório para rotas protegidas.

Configure o JWT_SECRET no ambiente.

📌 Endpoints principais
Método	Rota	Descrição
POST	/auth/login	Login e geração de token
GET	/clientes	Listar clientes
POST	/clientes	Criar cliente
GET	/agendamentos	Listar agendamentos
POST	/agendamentos	Criar agendamento


🛠️ Contribuição
Faça um fork do projeto.

Crie uma branch: git checkout -b minha-feature.

Commit suas alterações: git commit -m 'Minha feature'.

Push: git push origin minha-feature.

Abra um Pull Request.

📄 Licença
Este projeto está sob a licença MIT.
